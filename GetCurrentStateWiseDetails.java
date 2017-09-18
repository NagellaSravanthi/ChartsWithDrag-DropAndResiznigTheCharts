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
public class GetCurrentStateWiseDetails extends HttpServlet {
	//rtaproject
	String index="icjsdata";
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCurrentStateWiseDetails() {
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
		   String crntSt="c_state_name.keyword";
		   String prmntSt="p_state_name.keyword";
		   String crntStDt="c_district_name.keyword";
		   String manu_yr="manu_yr.keyword"; 
		   String vch_cls_desc="vh_class_desc.keyword";
		   ArrayList<RTIDataBean> year_data=new ArrayList<>();
		   ArrayList<RTIDataBean> year_imported=new ArrayList<>();
		   ArrayList<RTIDataBean> dist=new ArrayList<>();
		   ArrayList<RTIDataBean> vch=new ArrayList<>();
		   //state wise details
		  
	   BoolQueryBuilder qbSt =  QueryBuilders.boolQuery()
				    .mustNot( QueryBuilders.termQuery(crntSt, ""))    
				    .mustNot( QueryBuilders.termQuery(crntSt, "0"))
				    .must( QueryBuilders.termQuery(crntSt, "Delhi"))  ;  
			
		
		Order order=Order.KEY_ASC;
		AggregationBuilder aggrg=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(5).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR).order(order);
	
		
		
	 
			// current state wise details in jsp when click on current state then this data will display
			 //  1
			SearchResponse curnt_st_year_count =client.prepareSearch(index)
       .setQuery(qbSt)
       .addAggregation( AggregationBuilders.terms("crntSt").field(crntSt).subAggregation(aggrg).size(5))
       .execute()
       .actionGet();
		   
			Terms  termsSt =	curnt_st_year_count.getAggregations().get("crntSt");
			
			List<Bucket> buckets_St = termsSt.getBuckets();
			
			for (Bucket bucket : buckets_St) {
		InternalDateHistogram   terms1 = bucket.getAggregations().get("yr");
				for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucket2 : terms1.getBuckets()) {
			int key = ((org.joda.time.DateTime) bucket2.getKey()).getYear();
			long count=bucket2.getDocCount();
			if(key==2017||key==2016||key==2015||key==2014){
				//pw.println(  " year  :"+key+"    count:"+count);
				RTIDataBean data=new RTIDataBean();
				data.setYear(key);
				data.setCount((int) count);
				year_data.add(data);
			}}}
		
			//imported vechicle count
			
			SearchResponse curnt_st_year_imp_count =client.prepareSearch(index)
				       .setQuery(QueryBuilders.boolQuery()
							    .must( QueryBuilders.termQuery(crntSt, "Delhi"))
							    .must( QueryBuilders.termQuery("imported_vch.keyword", "Y")) )
				       .addAggregation( AggregationBuilders.terms("crntSt").field(crntSt).subAggregation(aggrg))
				       .execute()
				       .actionGet();
						   
							Terms  termsSt_y_imp =	curnt_st_year_imp_count.getAggregations().get("crntSt");
							
							List<Bucket> buckets_St_y_imp = termsSt_y_imp.getBuckets();
							//pw.println("imported vechicle count");
							
							for (Bucket bucket : buckets_St_y_imp) {
								InternalDateHistogram   terms1 = bucket.getAggregations().get("yr");
								
								
								for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucket2 : terms1.getBuckets()) {
									int key = ((org.joda.time.DateTime) bucket2.getKey()).getYear();
									long count=bucket2.getDocCount();
									if(key==2017||key==2016||key==2015||key==2014){
										RTIDataBean  imported_count=new RTIDataBean ();
										imported_count.setYear(key);
										imported_count.setCount((int) count);
										year_imported.add(imported_count);
								
							}
								}
								
								
							}
			
							// 3 vch
							SearchResponse curnt_st_vch_count =client.prepareSearch(index)
								       .setQuery(qbSt)
								       .addAggregation( AggregationBuilders.terms("crntSt").field(crntSt).subAggregation(AggregationBuilders.terms("vch").field(vch_cls_desc)))
								       .execute()
								       .actionGet();
										   
											Terms  termsVch =	curnt_st_vch_count.getAggregations().get("crntSt");
											
											List<Bucket> buckets_vch = termsVch.getBuckets();
											
											for (Bucket bucket : buckets_vch) {
												Terms  terms1 = bucket.getAggregations().get("vch");
												
												List<Bucket> buckets2 = terms1.getBuckets();
												//pw.println(bucket.getKey()+"  ");
												for (Bucket bucket2 : buckets2) {
													RTIDataBean vch_count=new RTIDataBean();
													long count=bucket2.getDocCount();
													Object key=bucket2.getKey();
													vch_count.setVechicleName((String) key);
													vch_count.setCount((int) count);
													vch.add(vch_count);
													
												}
												
												
											}
											
											
											// 2 distc
											SearchResponse curnt_st_dist_count =client.prepareSearch(index)
												       .setQuery(qbSt .mustNot( QueryBuilders.termQuery(crntStDt, ""))    
															    .mustNot( QueryBuilders.termQuery(crntStDt, "0")))
												       .addAggregation( AggregationBuilders.terms("crntSt").field(crntSt).subAggregation(AggregationBuilders.terms("dist").field(crntStDt).size(5)))
												       .execute()
												       .actionGet();
														   
															Terms  termsDt =	curnt_st_dist_count.getAggregations().get("crntSt");
															
															List<Bucket> buckets_Dt = termsDt.getBuckets();
															
															for (Bucket bucket : buckets_Dt) {
																Terms  terms1 = bucket.getAggregations().get("dist");
																
																List<Bucket> buckets2 = terms1.getBuckets();
																
																for (Bucket bucket2 : buckets2) {
																	
																	RTIDataBean dist_count=new RTIDataBean();
																			
																	long count=bucket2.getDocCount();
																	Object key=bucket2.getKey();
																	dist_count.setDistName((String) key);
																	dist_count.setCount((int) count);
																	dist.add(dist_count);
																
																}
																
															}
															
														
														//makerWiseDetails	
															
															SearchResponse  response1 = client.prepareSearch(index).addAggregation( AggregationBuilders.terms("keys").field("maker_name.keyword").size(6))
																	.execute().actionGet();
															Terms  terms1 = response1.getAggregations().get("keys");
															Collection<Terms.Bucket> buckets1 = terms1.getBuckets();
														
															ArrayList maker=new ArrayList();
															
															for (Bucket bucket : buckets1) {
																DataBean bean=new DataBean();
																bean.setMakerName((String) bucket.getKey());
																bean.setNoOfVechicles((int) bucket.getDocCount());
																maker.add(bean); 
															}
											
				
		Gson gson=new Gson();
		ArrayList data=new ArrayList<>();
		data.add(year_imported);
		data.add(year_data);
		data.add(vch);
		data.add(dist);
		data.add(maker);
		String json = gson.toJson(data);
		
		pw.println(json);
		
										
	
		}
		
		

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter pw = response.getWriter();
		
		  TransportClient client=GetClientObjectUtil.getObject();
		   String crntSt="c_state_name.keyword";
		   String prmntSt="p_state_name.keyword";
		   String crntStDt="c_district_name.keyword";
		   String manu_yr="manu_yr.keyword"; 
		   String vch_cls_desc="vh_class_desc.keyword";
		   
		   ArrayList<RTIDataBean> dist=new ArrayList<>();
		   ArrayList<RTIDataBean> vch=new ArrayList<>();
		   ArrayList<RTIDataBean> month=new ArrayList<>();
		   //state wise details
		  
	   BoolQueryBuilder qbSt =  QueryBuilders.boolQuery()
				    .mustNot( QueryBuilders.termQuery(crntSt, ""))    
				    .mustNot( QueryBuilders.termQuery(crntSt, "0"))
				    .must( QueryBuilders.termQuery(crntSt, "Delhi"))  ;  
			
		
		Order order=Order.KEY_ASC;
		AggregationBuilder aggrg=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(5).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR).order(order);
	
		
		String ActYear=  request.getParameter("year");
		int YearI=0;
		String Year_dec=null;
		try{
			YearI=Integer.parseInt(ActYear);
			YearI=YearI-1;
			Year_dec=String.valueOf(YearI);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		QueryBuilder qyy = QueryBuilders
                .rangeQuery("regn_dt")
              .format("dd/MM/yyyy||yyyy")
                .lte("31/12/"+ActYear)
                .gt("31/12/"+Year_dec);
		 System.out.println(ActYear+"  dec year:"+Year_dec);
		   BoolQueryBuilder qb =  QueryBuilders.boolQuery()
				   .mustNot( QueryBuilders.termQuery(crntSt, ""))    
				    .mustNot( QueryBuilders.termQuery(crntSt, "0"))
				    .mustNot( QueryBuilders.termQuery(crntStDt, ""))    
				    .mustNot( QueryBuilders.termQuery(crntStDt, "0"))
				    .must( QueryBuilders.termQuery(crntSt, "Delhi"))// state name
				    .must(QueryBuilders.rangeQuery("regn_dt").format("dd/MM/yyyy||yyyy").lte("31/12/"+ActYear).gt("31/12/"+Year_dec));  
					;  
		   AggregationBuilder aggY=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(0).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR).order(order);
		   BoolQueryBuilder qb1 =  QueryBuilders.boolQuery()
				    .must( QueryBuilders.termQuery(crntSt, "Delhi"))// state name
				    .must(QueryBuilders.rangeQuery("regn_dt").format("dd/MM/yyyy||yyyy").lte("31/12/"+ActYear).gt("31/12/"+Year_dec));  
		
		   
		   //1.year    district wise
			SearchResponse curnt_dt_year_count =client.prepareSearch(index)
        .setQuery(qb)
        .addAggregation(aggY.subAggregation( AggregationBuilders.terms("crntStDt").field(crntStDt)))
        .execute()
        .actionGet();
InternalDateHistogram   year =	curnt_dt_year_count.getAggregations().get("yr");
			  
			
			for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucketYear :  year.getBuckets()) {
				
		int key_year = ((org.joda.time.DateTime) bucketYear.getKey()).getYear();
	//	System.out.println("1. year"+key_year);
Terms  y_dist_cls =	bucketYear.getAggregations().get("crntStDt");
			List<Bucket> dist_buckets = y_dist_cls.getBuckets();
		
			for (Bucket bucket :dist_buckets) {
				String key=bucket.getKeyAsString();
				int count=(int) bucket.getDocCount();
				RTIDataBean bean=new RTIDataBean();
				bean.setDistName(key);
				bean.setCount(count);
				dist.add(bean);
				//System.out.println(" name :"+key+"    count:"+count);
			}
			}
	//		System.out.println("================     ========");
						
		//  2. year   vechicle category
			SearchResponse year_vcg_count =client.prepareSearch(index)
			        .setQuery(qb1)
			        .addAggregation(aggY.subAggregation(AggregationBuilders.terms("vch").field(vch_cls_desc)))
			        .execute()
			        .actionGet();
			
			
			InternalDateHistogram   y =	year_vcg_count.getAggregations().get("yr");
			  
			
			for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucketY :  y.getBuckets()) {
				
		int key_y = ((org.joda.time.DateTime) bucketY.getKey()).getYear();
		
Terms  y_vch_cls =	bucketY.getAggregations().get("vch");
		//	System.out.println("2.year vch catg "+key_y);
			List<Bucket> vch_buckets = y_vch_cls.getBuckets();
		
			for (Bucket bucket :vch_buckets) {
				String key=bucket.getKeyAsString();
				int count=(int) bucket.getDocCount();
				RTIDataBean bean=new RTIDataBean();
				bean.setVechicleName(key);
				bean.setCount(count);
				vch.add(bean);
				//System.out.println("vch name:"+bucket.getKey()+" count:"+bucket.getDocCount());
				
				}
			}
				
		//  3. year   month category
			AggregationBuilder agg=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(0).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR.MONTH).order(order);
			  
					SearchResponse year_month_count =client.prepareSearch(index)
					        .setQuery(qyy)
					        .addAggregation(agg)
					        .execute()
					        .actionGet();
					InternalDateHistogram   y_month_cls =	year_month_count.getAggregations().get("yr");
		  
		
		for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucket2 :  y_month_cls.getBuckets()) {
			
	int key = ((org.joda.time.DateTime) bucket2.getKey()).getMonthOfYear();
	int key_year = ((org.joda.time.DateTime) bucket2.getKey()).getYear();
	long count=bucket2.getDocCount();
	String monthNames[]={"","January","Febreary","March","April","May","June","July","August","September","October","November","December"};
		RTIDataBean data=new RTIDataBean();
		data.setMonth(monthNames[key]);
		data.setCount((int) count);
		month.add(data);
//System.out.println(key_year+" month:"+key+" count:"+count);
		
		}
		
		
		Gson gson=new Gson();
		ArrayList data=new ArrayList<>();
		data.add(month);
		data.add(vch);
		data.add(dist);
	
		String json = gson.toJson(data);
		
		pw.println(json);
		//System.out.println(json);
		
	}

}
