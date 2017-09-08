package com.otsi.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

import com.google.gson.Gson;

/**
 * Servlet implementation class DistSubAggregation
 */
public class DistSubAggregation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DistSubAggregation() {
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
			String dist_name=null;
		String crntStDt="c_district_name.keyword";
			String vchName="vh_class_desc.keyword";
			String bodyType="body_type.keyword";
			ArrayList<DataBean> bodyTypeDetails=new ArrayList<>();
			ArrayList<RTIDataBean> yearDetails=new ArrayList<>();
			ArrayList<RTIDataBean> distDetails=new ArrayList<>();
		dist_name=  request.getParameter("name");
		String[] split = dist_name.split(",");
		dist_name=split[0];
		name=split[1];
		 String crntSt="c_state_name.keyword";
			ArrayList Details=new ArrayList<>();
		BoolQueryBuilder qb=null;
		 qb =  QueryBuilders.boolQuery()
				    .mustNot( QueryBuilders.termQuery(bodyType, ""))    
				    .mustNot( QueryBuilders.termQuery(bodyType, "0"))
				    .must( QueryBuilders.termQuery(crntSt, "Delhi")) 
				    .must( QueryBuilders.termQuery(crntStDt, dist_name)) 
				    .must( QueryBuilders.termQuery(vchName, name))  ;  
		 System.out.println(dist_name+"     "+name);
		 
		 

			SearchResponse  response1 = client.prepareSearch("rtaproject").setQuery(qb).addAggregation(  AggregationBuilders.terms("bodyTypes").field(bodyType).size(7)).execute().actionGet();
			
			Terms  terms = response1.getAggregations().get("bodyTypes");
			Collection<Terms.Bucket> buckets = terms.getBuckets();
			
			
			
			
		                for (Bucket b : buckets ) {
		                	DataBean bean=new DataBean();
		                	bean.setBodyType((String) b.getKey());
		    					bean.setNoOfVechicles((int) b.getDocCount());
		    					bodyTypeDetails.add(bean);  } 
		 
		 
		 
		 
		// Details.add(yearDetails);
		 Details.add(bodyTypeDetails);
		// Details.add(distDetails);
			Gson gson=new Gson();
			String json = gson.toJson(Details);
			response.getWriter().println(json);
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
