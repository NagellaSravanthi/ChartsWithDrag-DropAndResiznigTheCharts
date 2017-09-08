package com.otsi.action;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.HdrHistogram.Histogram;
import org.apache.lucene.index.Term;
import static org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.DocValueFormat.DateTime;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetCurrentStateWiseDetails
 */
//@WebServlet("/GetCurrentStateWiseDetails")
public class GetPermanentStateWiseDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPermanentStateWiseDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		response.setContentType("text/json");
		PrintWriter pw = response.getWriter();
		   TransportClient client=GetClientObjectUtil.getObject();
		 
		   String prmntSt="p_state_name.keyword";
		   String prmntStDt="c_district_name.keyword";
		   String manu_yr="manu_yr.keyword"; 
		   String vch_cls_desc="vh_class_desc.keyword";
		   
		   
		   //state wise details
		  
	   BoolQueryBuilder qbSt =  QueryBuilders.boolQuery()
				    .mustNot( QueryBuilders.termQuery(prmntSt, ""))    
				    .mustNot( QueryBuilders.termQuery(prmntSt, "0"));  
			
		
		Order order=Order.KEY_DESC;
		AggregationBuilder aggrg=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(5).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR).order(order);
		
	   
	   //  1 overal year wise details
	   
	   
			SearchResponse curnt_st_year_count =client.prepareSearch("rtaproject")
       .setQuery(qbSt)
       .addAggregation(aggrg)
       .execute()
       .actionGet();
		   
			InternalDateHistogram  terms =	curnt_st_year_count.getAggregations().get("yr");
			
			ArrayList<RTIDataBean> year_data=new ArrayList<>();
			System.out.println("Year wise count");
				
				for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucket2 : terms.getBuckets()) {
					
			int key = ((org.joda.time.DateTime) bucket2.getKey()).getYear();
			long count=bucket2.getDocCount();
			if(key==2017||key==2016||key==2015||key==2014){
				System.out.println(  " year  :"+key+"    count:"+count);
				RTIDataBean data=new RTIDataBean();
				data.setYear(key);
				data.setCount((int) count);
				year_data.add(data);
			}
				
				}
				//pw.println("========================");
				
			
		
			//imported vechicle count
			
			SearchResponse curnt_st_year_imp_count =client.prepareSearch("rtaproject")
				       .setQuery(QueryBuilders.boolQuery()
							    .mustNot( QueryBuilders.termQuery(prmntSt, ""))
							    .mustNot( QueryBuilders.termQuery(prmntSt, "0"))
							    .must( QueryBuilders.termQuery("imported_vch.keyword", "Y")) )
				       .addAggregation( AggregationBuilders.terms("prmntSt").field(prmntSt))
				       .execute()
				       .actionGet();
						   
							Terms  termsSt_y_imp =	curnt_st_year_imp_count.getAggregations().get("prmntSt");
							
							List<Bucket> buckets_St_y_imp = termsSt_y_imp.getBuckets();
							System.out.println("imported vechicle count");
							ArrayList<RTIDataBean> st_imported=new ArrayList<>();
							for (Bucket bucket : buckets_St_y_imp) {
								String key =  bucket.getKeyAsString();
									long count=bucket.getDocCount();
								
										RTIDataBean  imported_count=new RTIDataBean ();
										
										imported_count.setCount((int) count);
										imported_count.setStateName(key);
										st_imported.add(imported_count);
										System.out.println(  " stateName  :"+key+"    count:"+count);
								
							
								}
								//pw.println("========================");
								
								
			
			// 2 state
			SearchResponse curnt_st_dist_count =client.prepareSearch("rtaproject")
				       .setQuery(qbSt)
				       .addAggregation( AggregationBuilders.terms("prmntSt").field(prmntSt))
				       .execute()
				       .actionGet();
			System.out.println("State wise vechicle count");
							Terms  termsDt =	curnt_st_dist_count.getAggregations().get("prmntSt");
							
							List<Bucket> buckets_Dt = termsDt.getBuckets();
							ArrayList<RTIDataBean> st=new ArrayList<>();
							for (Bucket bucket : buckets_Dt) {
								
								
									RTIDataBean st_count=new RTIDataBean();
											
									long count=bucket.getDocCount();
									Object key=bucket.getKey();
									st_count.setCount((int) count);
									st_count.setStateName((String) key);
									st.add(st_count);
									System.out.println(" state :"+key+"    count:"+count);
								}
								
								//pw.println("--------------------------");
							
							
							
		
							// 3 vch
							SearchResponse curnt_st_vch_count =client.prepareSearch("rtaproject")
								       .setQuery(qbSt)
								       .addAggregation( AggregationBuilders.terms("vch").field(vch_cls_desc))
								       .execute()
								       .actionGet();
										   
											Terms  termsVch =	curnt_st_vch_count.getAggregations().get("vch");
											System.out.println("Vechicle ctg wise count");
											List<Bucket> buckets_vch = termsVch.getBuckets();
											ArrayList<RTIDataBean> vch=new ArrayList<>();
											for (Bucket bucket : buckets_vch) {
												
													RTIDataBean vch_count=new RTIDataBean();
													long count=bucket.getDocCount();
													Object key=bucket.getKey();
													vch_count.setVechicleName((String) key);
													vch_count.setCount((int) count);
													vch.add(vch_count);
													System.out.println(" vch :"+key+"    count:"+count);
												}
												
												
											
											
		/*	
		
			// state name,particular year district wise
		   String st_name="New Delhi";
		   QueryBuilder qbb = matchPhraseQuery(
				    "c_state_name",                  
				    st_name  
				);
		   
		   
		   pw.println("  District wise  where year is 2017 ");
		   BoolQueryBuilder qb =  QueryBuilders.boolQuery()
				   .mustNot( QueryBuilders.termQuery(prmntSt, ""))    
				    .mustNot( QueryBuilders.termQuery(prmntSt, "0"))
				    .mustNot( QueryBuilders.termQuery(prmntStDt, ""))    
				    .mustNot( QueryBuilders.termQuery(prmntStDt, "0"))
				    .must( QueryBuilders.termQuery(prmntSt, "Delhi"))// state name
				    .must(QueryBuilders.termQuery(manu_yr, 2017));  
		   
		   
		   //1.year    district wise
			SearchResponse curnt_dt_year_count =client.prepareSearch("rtaproject")
        .setQuery(qb)
        .addAggregation( AggregationBuilders.terms("prmntStDt").field(prmntStDt))
        .execute()
        .actionGet();
	Terms  terms =	curnt_dt_year_count.getAggregations().get("prmntStDt");
			
			List<Bucket> buckets = terms.getBuckets();
			
			for (Bucket bucket : buckets) {
				
				pw.println(" name :"+bucket.getKey()+"    count:"+bucket.getDocCount());
			}
			pw.println("================     ========");
			
			//------------------------or------------------------
			QueryBuilder qy = termQuery(
				    manu_yr,                  
				    "2017" 
				);
			QueryBuilder qy = QueryBuilders
                    .rangeQuery("regn_dt")
                    .from(2017)
                    .to(2017);
			RangeQueryBuilder queryDate = QueryBuilders.rangeQuery("regn_dt").to(2017).from(2017);
			
			
			SearchResponse year_count =client.prepareSearch("rtaproject")
					.setQuery(qy)
			        .addAggregation( aggrg)
			        .execute()
			        .actionGet();
			InternalDateHistogram  y_terms =	year_count.getAggregations().get("yr");
						
				
						pw.println();
						for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucket : y_terms.getBuckets()) {
					org.joda.time.DateTime key = (org.joda.time.DateTime) bucket.getKey();
							pw.println(  " year  :"+key.getYear()+"    count:"+bucket.getDocCount());
						}
						pw.println("========================");
						
					
			
		//  2. year   vechicle category
			 BoolQueryBuilder qb1 =  QueryBuilders.boolQuery()
					    .must( QueryBuilders.termQuery(prmntSt, "Delhi"))// state name
					    .must(QueryBuilders.termQuery(manu_yr, 2017));  
			SearchResponse year_vcg_count =client.prepareSearch("rtaproject")
			        .setQuery(qb1)
			        .addAggregation(AggregationBuilders.terms("vch").field(vch_cls_desc))
			        .execute()
			        .actionGet();
Terms  y_vch_cls =	year_vcg_count.getAggregations().get("vch");
			
			List<Bucket> vch_buckets = y_vch_cls.getBuckets();
			
			for (Bucket bucket :vch_buckets) {
				pw.println(" name:"+bucket.getKey()+"    count:"+bucket.getDocCount());
				
				}
				
		//  3. year   month category
					  
					SearchResponse year_month_count =client.prepareSearch("rtaproject")
					        .setQuery(qb1)
					        .addAggregation(AggregationBuilders.terms("vch").field(vch_cls_desc))
					        .execute()
					        .actionGet();
		Terms  y_month_cls =	year_month_count.getAggregations().get("vch");
					
					List<Bucket> month_buckets = y_month_cls.getBuckets();
					
					for (Bucket bucket :month_buckets) {
						pw.println(" name:"+bucket.getKey()+"    count:"+bucket.getDocCount());
						
						}
				*/
		Gson gson=new Gson();
		
		
		ArrayList data=new ArrayList<>();
		data.add(st_imported);
		data.add(year_data);
		data.add(vch);
		data.add(st);
		String json = gson.toJson(data);
		
		pw.println(json);
		System.out.println(json);
		
		
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
