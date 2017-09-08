package com.otsi.action;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.google.gson.Gson;

import static org.elasticsearch.index.query.QueryBuilders.*;
/**
 * Servlet implementation class GetMakerBodyType
 */
//@WebServlet("/GetMakerBodyType")
public class GetMakerBodyType2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMakerBodyType2() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  
	    TransportClient client=GetClientObjectUtil.getObject();
		response.setContentType("text/json");
		String name=null;
		
			name=  request.getParameter("list");
		
		
		
		//String name= "TVS MOTOR COMPANY LTD" ;
		System.out.println("   "+name);
		
		 String crntStDt="c_district_name.keyword";
		String vchName="vh_class_desc.keyword";
		String bodyType="body_type.keyword";
		ArrayList<DataBean> bodyTypeDetails=new ArrayList<>();
		ArrayList<RTIDataBean> yearDetails=new ArrayList<>();
		ArrayList<RTIDataBean> distDetails=new ArrayList<>();
		 String crntSt="c_state_name.keyword";
		ArrayList Details=new ArrayList<>();
		BoolQueryBuilder qb=null;
		String dist_name=null;
		
			 qb =  QueryBuilders.boolQuery()
					    .mustNot( QueryBuilders.termQuery(bodyType, ""))    
					    .mustNot( QueryBuilders.termQuery(bodyType, "0"))
					    .must( QueryBuilders.termQuery(vchName, name))  ;  
		
		  
		
		   
		   Order order=Order.KEY_DESC;
			AggregationBuilder aggrg=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(0).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR).order(order);
			
		   
		
			SearchResponse  response1 = client.prepareSearch("rtaproject").setQuery(qb).addAggregation( AggregationBuilders.terms("keys").field(vchName).subAggregation( AggregationBuilders.terms("bodyTypes").field(bodyType).size(7))).execute().actionGet();
			
			Terms  terms = response1.getAggregations().get("keys");
			Collection<Terms.Bucket> buckets = terms.getBuckets();
			
			
			
			 for (Bucket bucket : buckets) { Terms types =bucket.getAggregations().get("bodyTypes");
		                Collection<Terms.Bucket> bkts = types.getBuckets();
		                for (Bucket b : bkts) {
		                	DataBean bean=new DataBean();
		                	bean.setBodyType((String) b.getKey());
		    					bean.setNoOfVechicles((int) b.getDocCount());
		    					bodyTypeDetails.add(bean);  } }
			 
			 //  1
			   
			   
			SearchResponse curnt_st_year_count =client.prepareSearch("rtaproject")
       .setQuery(qb)
       .addAggregation( AggregationBuilders.terms("keys").field(vchName).subAggregation(aggrg).size(5))
       .execute()
       .actionGet();
		   
			Terms  termsSt =	curnt_st_year_count.getAggregations().get("keys");
			
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
				yearDetails.add(data);
			}}}

			 

				// 2 distc
				SearchResponse curnt_st_dist_count =client.prepareSearch("rtaproject")
					       .setQuery(qb.mustNot( QueryBuilders.termQuery(crntStDt, ""))    
								    .mustNot( QueryBuilders.termQuery(crntStDt, "0")))
					       .addAggregation( AggregationBuilders.terms("keys").field(vchName).subAggregation(AggregationBuilders.terms("dist").field(crntStDt).size(5)))
					       .execute()
					       .actionGet();
							   
								Terms  termsDt =	curnt_st_dist_count.getAggregations().get("keys");
								
								List<Bucket> buckets_Dt = termsDt.getBuckets();
								
								for (Bucket bucket : buckets_Dt) {
									Terms  terms1 = bucket.getAggregations().get("dist");
									
									List<Bucket> buckets2 = terms1.getBuckets();
									//pw.println(bucket.getKey()+"  ");
									for (Bucket bucket2 : buckets2) {
										
										RTIDataBean dist_count=new RTIDataBean();
												
										long count=bucket2.getDocCount();
										Object key=bucket2.getKey();
										dist_count.setDistName((String) key);
										dist_count.setCount((int) count);
										distDetails.add(dist_count);
										//pw.println(" key :"+key+"    count:"+count);
									}
									
								}
								
								
						
			Details.add(yearDetails);
			 Details.add(bodyTypeDetails);
			 Details.add(distDetails);
				Gson gson=new Gson();
				String json = gson.toJson(Details);
				response.getWriter().println(json);
				System.out.println(json);
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
TransportClient client=GetClientObjectUtil.getObject();
		
		
		response.setContentType("text/json");
		String name=  request.getParameter("name");
		
		 String crntSt="c_state_name.keyword";
		   String manu_yr="manu_yr.keyword"; 
		 String crntStDt="c_district_name.keyword";
		String vchName="vh_class_desc.keyword";
		String bodyType="body_type.keyword";
		String makerName="maker_name.keyword";
		ArrayList<DataBean> bodyTypeDetails=new ArrayList<>();
		ArrayList<RTIDataBean> yearDetails=new ArrayList<>();
		ArrayList<RTIDataBean> distDetails=new ArrayList<>();
		ArrayList<RTIDataBean> VchName=new ArrayList<>();
		ArrayList<RTIDataBean> acAudioVideo=new ArrayList<>();
		ArrayList makerDetails=new ArrayList<>();
		RTIDataBean  bean=new RTIDataBean();
		   BoolQueryBuilder qb =  QueryBuilders.boolQuery()
					    .mustNot( QueryBuilders.termQuery(bodyType, ""))    
					    .mustNot( QueryBuilders.termQuery(bodyType, "0"))
					    .mustNot( QueryBuilders.termQuery(crntStDt, ""))    
					    .mustNot( QueryBuilders.termQuery(crntStDt, "0"))
					    .must( QueryBuilders.termQuery(makerName, name))  ;  
		
		   
		   Order order=Order.KEY_DESC;
			AggregationBuilder aggrg=AggregationBuilders.dateHistogram("yr").field("regn_dt").minDocCount(0).interval(1).dateHistogramInterval(DateHistogramInterval.YEAR).order(order);
			SearchResponse  response1 = client.prepareSearch("rtaproject").setQuery(qb).addAggregation( AggregationBuilders.terms("keys").field(makerName).subAggregation( AggregationBuilders.terms("bodyTypes").field(bodyType).size(7))).execute().actionGet();
			
			Terms  terms = response1.getAggregations().get("keys");
			Collection<Terms.Bucket> buckets = terms.getBuckets();
			 for (Bucket bucket : buckets) {
    Terms types =bucket.getAggregations().get("bodyTypes");
		                Collection<Terms.Bucket> bkts = types.getBuckets();
		                for (Bucket b : bkts) {
		                	DataBean bean1=new DataBean();
		                     
		                    	bean1.setBodyType(b.getKeyAsString());
		    					bean1.setNoOfVechicles((int) b.getDocCount());
		    					bodyTypeDetails.add(bean1); 

		        }}
			 
		
			  //  1
			   
			SearchResponse curnt_st_year_count =client.prepareSearch("rtaproject")
       .setQuery(qb)
       .addAggregation( AggregationBuilders.terms("keys").field(makerName).subAggregation(aggrg).size(5))
       .execute()
       .actionGet();
		   
			Terms  termsSt =	curnt_st_year_count.getAggregations().get("keys");
			
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
				yearDetails.add(data);
			}}}
			
			
			
	// 2 distc
				SearchResponse curnt_st_dist_count =client.prepareSearch("rtaproject")
					       .setQuery(qb)
					       .addAggregation( AggregationBuilders.terms("keys").field(makerName).subAggregation(AggregationBuilders.terms("dist").field(crntStDt).size(5)))
					       .execute()
					       .actionGet();
							   
								Terms  termsDt =	curnt_st_dist_count.getAggregations().get("keys");
								
								List<Bucket> buckets_Dt = termsDt.getBuckets();
								
								for (Bucket bucket : buckets_Dt) {
									Terms  terms1 = bucket.getAggregations().get("dist");
									
									List<Bucket> buckets2 = terms1.getBuckets();
									//pw.println(bucket.getKey()+"  ");
									for (Bucket bucket2 : buckets2) {
										
										RTIDataBean dist_count=new RTIDataBean();
												
										long count=bucket2.getDocCount();
										Object key=bucket2.getKey();
										dist_count.setDistName((String) key);
										dist_count.setCount((int) count);
										distDetails.add(dist_count);
										//pw.println(" key :"+key+"    count:"+count);
									}
									
									//pw.println("--------------------------");
								}
								
								
								  
								//vchName
								SearchResponse curnt_dt_year_count =client.prepareSearch("rtaproject")
									    .setQuery(qb)
									    .addAggregation( AggregationBuilders.terms("key").field(makerName).subAggregation(AggregationBuilders.terms("vch").field(vchName)))
									    .execute()
									    .actionGet();
									Terms  term =	curnt_dt_year_count.getAggregations().get("key");
											
											List<Bucket> bucket = term.getBuckets();
											
											for (Bucket bucket1 : bucket) {
												
												Terms  terms1 = bucket1.getAggregations().get("vch");
												
												List<Bucket> buckets2 = terms1.getBuckets();
												//pw.println(bucket.getKey()+"  ");
												for (Bucket bucket2 : buckets2) {
													RTIDataBean vch_count=new RTIDataBean();
													long count=bucket2.getDocCount();
													Object key=bucket2.getKey();
													vch_count.setVechicleName((String) key);
													vch_count.setCount((int) count);
													VchName.add(vch_count);
													//pw.println(" key :"+key+"    count:"+count);
												}
											}
						

											SearchResponse ACFitted =client.prepareSearch("rtaproject")
													 .setQuery( QueryBuilders.boolQuery()
															   .must( QueryBuilders.termQuery(makerName, name))
															    .must( QueryBuilders.termQuery(crntSt, "Delhi"))// state name
															    .must( QueryBuilders.termQuery("ac_fitted.keyword", "Y")).must( QueryBuilders.termQuery("audio_fitted.keyword", "Y")))
													.addAggregation( AggregationBuilders.terms("ac").field(makerName))
												    .execute()
												    .actionGet();
											
												Terms  ac =	ACFitted.getAggregations().get("ac");
														
														List<Bucket>ac_details = ac.getBuckets();
														
														
														
														for (Bucket bucket11 :ac_details ) {
															int docCount = (int) bucket11.getDocCount();
															String key = bucket11.getKeyAsString();
															bean.setAccount(docCount);
															
														//	System.out.println(" name :"+key+" AC and audio   count:"+docCount);
														}
											
														SearchResponse ACandAudioandVideoFitted =client.prepareSearch("rtaproject")
																 .setQuery( QueryBuilders.boolQuery()
																		   .must( QueryBuilders.termQuery(makerName, name))
																		    .must( QueryBuilders.termQuery(crntSt, "Delhi"))// state name
																		   .must( QueryBuilders.termQuery("ac_fitted.keyword", "Y")).must( QueryBuilders.termQuery("audio_fitted.keyword", "Y")).must( QueryBuilders.termQuery("video_fitted.keyword", "Y")))
																.addAggregation( AggregationBuilders.terms("ac").field(makerName))
															    .execute()
															    .actionGet();
													
															Terms  acAudiVideo =	ACandAudioandVideoFitted.getAggregations().get("ac");
																	
																	List<Bucket>ac_AudioVideodetails = acAudiVideo.getBuckets();
																	for (Bucket bucket22 :ac_AudioVideodetails ) {
																		int docCount = (int) bucket22.getDocCount();
																	//	String key=bucket22.getKeyAsString();
																		bean.setVideocount(docCount);
																		//System.out.println(" name :"+key+" ac audio video   count:"+docCount);
																	}
															
														acAudioVideo.add(bean);
								System.out.println(bean);
								
								
			 makerDetails.add(yearDetails);
			 makerDetails.add(bodyTypeDetails);
			 makerDetails.add(distDetails);
			 makerDetails.add(VchName);
			 makerDetails.add(acAudioVideo);
				Gson gson=new Gson();
				String json = gson.toJson(makerDetails);
				response.getWriter().println(json);
				
	}

}
