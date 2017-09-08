package com.otsi.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.elasticsearch.index.query.QueryBuilders.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetDistrictWiseDetails
 */
public class GetDistrictWiseDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDistrictWiseDetails() {
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
		
		
		   BoolQueryBuilder qbSt =  QueryBuilders.boolQuery()
				    .mustNot( QueryBuilders.termQuery(crntSt, ""))    
				    .mustNot( QueryBuilders.termQuery(crntSt, "0"))
				    .must( QueryBuilders.termQuery(crntSt, "Delhi"))  ;  
			
		
		Order order=Order.KEY_DESC;
		AggregationBuilder aggrg=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(0).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR).order(order);
		
		String dist_name=  request.getParameter("list");
		//String dist_name=  "East";
		

	   String st_name="Delhi";
	   QueryBuilder qbb = matchPhraseQuery(
			    "c_state_name",                  
			    st_name  
			);
	   
	   
	 // System.out.println("  District wise  "+dist_name+"  Aggregation details");
	   BoolQueryBuilder qb =  QueryBuilders.boolQuery()
			   .mustNot( QueryBuilders.termQuery(crntSt, ""))    
			    .mustNot( QueryBuilders.termQuery(crntSt, "0"))
			    .mustNot( QueryBuilders.termQuery(crntStDt, ""))    
			    .mustNot( QueryBuilders.termQuery(crntStDt, "0"))
			    .must( QueryBuilders.termQuery(crntSt, "Delhi"))// state name
			    .must( QueryBuilders.termQuery(crntStDt, dist_name))  ;  
	   
	  
	   
	 //makerWiseDetails	
		
		SearchResponse  response1 = client.prepareSearch("rtaproject").setQuery(qb).addAggregation( AggregationBuilders.terms("keys").field("maker_name.keyword").size(6))
				.execute().actionGet();
		Terms  terms = response1.getAggregations().get("keys");
		Collection<Terms.Bucket> buckets1 = terms.getBuckets();
	
		ArrayList maker=new ArrayList();
		
		for (Bucket bucket : buckets1) {
			DataBean bean1=new DataBean();
			bean1.setMakerName((String) bucket.getKey());
			bean1.setNoOfVechicles((int) bucket.getDocCount());
			maker.add(bean1); 
		}

	   
	   //1.district wise imported
		SearchResponse curnt_dt_year_count =client.prepareSearch("rtaproject")
    .setQuery(qb)
    .addAggregation( AggregationBuilders.terms("crntStDt").field(crntStDt).subAggregation(AggregationBuilders.terms("vch").field(vch_cls_desc)))
    .execute()
    .actionGet();
Terms  termss =	curnt_dt_year_count.getAggregations().get("crntStDt");
		
		List<Bucket> buckets = termss.getBuckets();
		
		for (Bucket bucket : buckets) {
			
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
				//pw.println(" key :"+key+"    count:"+count);
			}
		}
		//2
		SearchResponse year_count =client.prepareSearch("rtaproject")
				.setQuery(qb)
				.addAggregation( AggregationBuilders.terms("crntStDt").field(crntStDt).subAggregation( aggrg))
		        .execute()
		        .actionGet();
		
Terms  year_wise =	year_count.getAggregations().get("crntStDt");
		
		List<Bucket> year = year_wise.getBuckets();
		
		for (Bucket bucket : year) {
			InternalDateHistogram  y_terms =	bucket.getAggregations().get("yr");
			
			for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucket1 : y_terms.getBuckets()) {
		int key = ((org.joda.time.DateTime) bucket1.getKey()).getYear();
		int count=(int) bucket1.getDocCount();
		if(key==2017||key==2016||key==2015||key==2014){
			RTIDataBean bean=new RTIDataBean();
			bean.setCount(count);
			bean.setYear(key);
			year_data.add(bean);
			
		}	
			}
			
		}
		
	   ArrayList<RTIDataBean> acImportedCount=new ArrayList<>();	
	 		RTIDataBean  bean=new RTIDataBean();
	   
	   SearchResponse imported =client.prepareSearch("rtaproject")
			    .setQuery(qb.must( QueryBuilders.termQuery("imported_vch.keyword", "Y")))
			    .addAggregation( AggregationBuilders.terms("import").field(crntStDt))
			    .execute()
			  .actionGet();

				//	System.out.println("imported count");
Terms  terms_imported =	imported.getAggregations().get("import");
					
					List<Bucket>dist_details = terms_imported.getBuckets();
					
					for (Bucket bucket : dist_details ) {
						int docCount = (int) bucket.getDocCount();
						bean.setCount(docCount);
					//	System.out.println(" name :"+bucket.getKey()+" imported   count:"+bucket.getDocCount());
					}
					
					
	 
		SearchResponse ACFitted =client.prepareSearch("rtaproject")
				 .setQuery( QueryBuilders.boolQuery()
						   .mustNot( QueryBuilders.termQuery(crntSt, ""))    
						    .mustNot( QueryBuilders.termQuery(crntSt, "0"))
						    .mustNot( QueryBuilders.termQuery(crntStDt, ""))    
						    .mustNot( QueryBuilders.termQuery(crntStDt, "0"))
						    .must( QueryBuilders.termQuery(crntSt, "Delhi"))// state name
						    .must( QueryBuilders.termQuery(crntStDt, dist_name)).must( QueryBuilders.termQuery("ac_fitted.keyword", "Y")).must( QueryBuilders.termQuery("audio_fitted.keyword", "Y")))
				.addAggregation( AggregationBuilders.terms("ac").field(crntStDt))
			    .execute()
			    .actionGet();
		//System.out.println("ac count");
			Terms  ac =	ACFitted.getAggregations().get("ac");
					
					List<Bucket>ac_details = ac.getBuckets();
					
					
					
					for (Bucket bucket :ac_details ) {
						int docCount = (int) bucket.getDocCount();
						String key = bucket.getKeyAsString();
						bean.setAccount(docCount);
						bean.setDistName(key);
					//	System.out.println(" name :"+key+" AC and audio   count:"+docCount);
					}
		
					SearchResponse ACandAudioandVideoFitted =client.prepareSearch("rtaproject")
							 .setQuery( QueryBuilders.boolQuery()
									   .mustNot( QueryBuilders.termQuery(crntSt, ""))    
									    .mustNot( QueryBuilders.termQuery(crntSt, "0"))
									    .mustNot( QueryBuilders.termQuery(crntStDt, ""))    
									    .mustNot( QueryBuilders.termQuery(crntStDt, "0"))
									    .must( QueryBuilders.termQuery(crntSt, "Delhi"))// state name
									    .must( QueryBuilders.termQuery(crntStDt, dist_name)).must( QueryBuilders.termQuery("ac_fitted.keyword", "Y")).must( QueryBuilders.termQuery("audio_fitted.keyword", "Y")).must( QueryBuilders.termQuery("video_fitted.keyword", "Y")))
							.addAggregation( AggregationBuilders.terms("ac").field(crntStDt))
						    .execute()
						    .actionGet();
					//System.out.println("ac count");
						Terms  acAudioVideo =	ACandAudioandVideoFitted.getAggregations().get("ac");
								
								List<Bucket>ac_AudioVideodetails = acAudioVideo.getBuckets();
								
								
								
								for (Bucket bucket :ac_AudioVideodetails ) {
									int docCount = (int) bucket.getDocCount();
									bean.setVideocount(docCount);
								//	System.out.println(" name :"+bucket.getKey()+" ac audio video   count:"+docCount);
								}
						acImportedCount.add(bean);		
						
						
						
						

					
	Gson gson=new Gson();

	ArrayList data=new ArrayList<>();
	data.add(year_data);
	data.add(vch);
	data.add(acImportedCount);
	data.add(maker);
	String json = gson.toJson(data);
	
	pw.println(json);
	//System.out.println(json);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
