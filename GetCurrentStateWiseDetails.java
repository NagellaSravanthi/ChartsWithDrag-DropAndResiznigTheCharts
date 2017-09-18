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
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter pw = response.getWriter();
		
		
		
		   ArrayList<RTIDataBean> year_data=getYear_data();
		   ArrayList<RTIDataBean> year_imported=get_year_imported();
		   ArrayList<RTIDataBean> dist=get_dist();
		   ArrayList<RTIDataBean> vch=get_vch();
		   //state wise details
				
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request,response);
}
