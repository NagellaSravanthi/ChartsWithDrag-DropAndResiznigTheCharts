<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.ArrayList,com.otsi.action.DataBean"
	isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<meta charset="utf-8">
<head>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>


<script	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.js" />
<script	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js" />
<script	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.js" />
<script	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js" />
<script	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js" />
<script src="path/to/chartjs/dist/Chart.js"></script>
<!--  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"> -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 <link href = "https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css"
         rel = "stylesheet">
   <script src = "https://code.jquery.com/jquery-1.10.2.js"></script>
      <script src = "https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
       <style>
        
         .ui-widget-content {
            background: #cedc98;
            border: 1px solid #DDDDDD;
            color: #333333;
         }
          .ui-resizable-helper { border: 2px dotted #00F; }
        /*  #resizable { width: 150px; height: 150px; padding: 0.5em;
            text-align: center; margin: 0; } */
      </style>
      
  <script>
  $( function() {
     $( "#d1" ).draggable();$( "#d2" ).draggable();$( "#d3" ).draggable();
     $( "#d4" ).draggable();$( "#d5" ).draggable();$( "#d6" ).draggable();
    $( "#d1" ).resizable({ cancel: ".cancel" ,  animate: true, helper: "ui-resizable-helper", minHeight: 290, minWidth: 340});$( "#d2" ).resizable({ cancel: ".cancel" ,  animate: true, helper: "ui-resizable-helper", minHeight: 300, minWidth: 340});
    $( "#d3" ).resizable({ cancel: ".cancel" ,  animate: true, helper: "ui-resizable-helper", minHeight: 290, minWidth: 340}); $( "#d4" ).resizable({ cancel: ".cancel" ,  animate: true, helper: "ui-resizable-helper", minHeight: 290, minWidth: 340});
    $( "#d5" ).resizable({ cancel: ".cancel" ,  animate: true, helper: "ui-resizable-helper", minHeight: 290, minWidth: 340});$( "#d6" ).resizable({ cancel: ".cancel" ,  animate: true, helper: "ui-resizable-helper", maxHeight: 250, maxWidth: 350, minHeight: 290, minWidth: 340});
    
  } );
  </script>

</head>
<body >

<div id="RadioType" align="center" style="margin-top: 5px;"  onresize="">
<b>CurrentStateWiseDetails:: </b><input type="radio" name="state"      onclick="curntState()" checked="checked"><b>PermenetStateWiseDetails::</b> <input type="radio" name="state"  onclick="prmtState()">
</div><br>
<div id="alertMsg" style="padding-left: 12px"></div>
<div></div>
	<div id="Charts" style="float: none;" >
		<table id="draggable"  style="border:2px solid black; width: 100%"  >
			<tr id="r1" >
			<td id="d1" style="border:1px solid red; padding: 2px;" class="ui-widget-content" width="33%">
			<div class="dropdown"style="float: right; padding-right: 80px;" >
						<button class="dropbtn">Dropdown</button> 
						<div id="dropDown" class="dropdown-content"  ></div> 
 					</div> 
					<canvas id="pie-chartcanvas-1" width="250" height="200" ></canvas></td>
				<td id="d2"  style="border:1px solid red;"  class="ui-widget-content" width="33%"><canvas id="bar-chartcanvas"
						width="450" height="400"></canvas></td>
<td id="d3"  style="border:1px solid red;"  class="ui-widget-content" width="33%"> <canvas id="highChart" width="500" height="400"></canvas></td>
					
			</tr>
			<tr id="r2"    >
				<td id="d4" style="border:1px solid red;" class="ui-widget-content" width="33%"><canvas id="pie-chartcanvas-2"
						width="250" height="200"    ></canvas></td>
				<td id="d5"  style="border:1px solid red;" class="ui-widget-content" width="33%"><canvas id="mycanvas" width="400"
						height="200"></canvas></td>
    <td id="d6"  style="border:1px solid red;"  class="ui-widget-content" width="33%"><canvas id="chart6" 
						width="250" height="200"></canvas></td>

			</tr>
	</table>
	</div>
	
</body>


<script type="text/javascript" src="Funtions.js"></script>
<link rel="stylesheet" href="StyleSheet.css">
<script>
//common field information 
var ctxPie = $("#pie-chartcanvas-1");var ctxDht = $("#pie-chartcanvas-2");
var ctxBar = $("#bar-chartcanvas");var ctxLine = $("#mycanvas");var ctxHigh=$("#highChart"); var ctx6=$("#chart6");
var chart1;var googleplus_follower = [];
var makerNameClick="" ;var LineGraph ;var BarChart;var HighChart;var chart2;var chart6;
var options;
var i=0;
var year=[];var imported=[];var year_count=[];var imported_count=[];
var vch_name=[];var vch_name_count=[];var dist_name=[];var dist_name_count=[];var ac_count;var video_count;var makerNameCount = [];   var makerName = [];var noOfVechicles = [];var noOfVechiclesBodyType = [];var bodyType = [];
var dist_wise=false;var year_wise=false;var vch_ctg=false;var maker_wise=true;
curntState();
	 function onLoad(){
		 
	 
 try{LineGraph.destroy(); BarChart.destroy(); chart2.destroy();	HighChart.destroy();chart1.destroy(); chart6.destroy();
 year=[]; imported=[]; year_count=[]; imported_count=[];
  vch_name=[];vch_name_count=[];dist_name=[]; dist_name_count=[]; makerNameCount = [];  makerName = []; 
 }
catch(err){}

		$.ajax({url : '<%=application.getContextPath()%>/GetCurrentStateWiseDetails'
			,type : "GET",	success : function(data) {
				$("#dropDown").html("");
												console.log(data);	
												for ( var i in data[0]) {
													console.log(data[0][i].count);
													imported.push(data[0][i].year);
													imported_count.push(data[0][i].count);
													}
												
												for ( var i in data[1]) {
													year.push(data[1][i].year);
										    		year_count.push(data[1][i].count);	
												}
											for ( var i in data[2]) {
												vch_name.push(data[2][i].vechicleName);
								        		vch_name_count.push(data[2][i].count);	
												}
											for ( var i in data[3]) {
												dist_name.push(data[3][i].distName);
							            		dist_name_count.push(data[3][i].count);
											}
											for ( var i in data[4]) {
												makerName.push(data[4][i].makerName);
												makerNameCount.push(data[4][i].noOfVechicles);
											}
											
												for ( var k in dist_name ){
											    	$("#dropDown").append('<p  id="' + k + '" onClick=f1(0,[{_index:'+k+'}])>' +dist_name[k]+ '<p>');
											    	}
			options = {	'onClick' : function(evt, item) {myFunc(evt, item)},
								tooltips : {callbacks : {}}
								};

	var chartdata = {labels : imported,datasets : [{labels: imported,label : "Imported vechicle count ",
		fill : false,lineTension : 0.1,backgroundColor : "rgba(29, 202, 255, 0.75)",borderColor : "rgba(29, 202, 255, 1)",
		pointHoverBackgroundColor : "rgba(29, 202, 255, 1)",pointHoverBorderColor : "rgba(29, 202, 255, 1)",data : imported_count} ,
	{labels: imported,label : " Imported vechicle count ",fill : false,lineTension : 0.1,backgroundColor : "rgba(59, 89, 152, 0.75)",borderColor : "rgba(59, 89, 152, 1)",
pointHoverBackgroundColor : "rgba(59, 89, 152, 1)",pointHoverBorderColor : "rgba(59, 89, 152, 1)",data :imported_count}]};
			
		LineGraph = new Chart(ctxLine, {type : 'line',data : chartdata,options : options});

	   //pie chart data
	var data1 = {labels : dist_name,datasets : [ {label:"Customers Count State Wise",data : dist_name_count,backgroundColor : ["#DEB887","#A9A9A9","#DC143C","#F4A460","#2E8B57","#9ACD32",'moccasin','saddlebrown','lightpink'],
labels : dist_name,borderColor : [ "#CDA776","#989898","#CB252B","#E39371","#1D7A46" ],borderWidth : [ 1, 1, 1, 1,1 ]} ]};

												//pie chart data
var data2 = {labels : vch_name,datasets : [ {	labels: vch_name,label : "Avg Balance in each state ",data : vch_name_count,backgroundColor : ["#DCDCDC","#E9967A","#9ACD32",'indigo','saddlebrown', 'lightpink',"aqua","red","blue","green" ],borderWidth : [ 1, 1, 1, 1,1 ]} ]};
//create Chart class object
		chart1 = new Chart(ctxPie, {type : "pie",data : data1,options : options});

	//create Chart class object
		chart2 = new Chart(ctxDht, {type : "doughnut",data : data2,options : options});
		var data6 = {labels : makerName,datasets : [ {	labels:makerName,label : "Maker Name wise details ",data : makerNameCount,backgroundColor : ["#DCDCDC","#E9967A","#9ACD32",'indigo','saddlebrown', 'lightpink',"aqua","red","blue","green" ],borderWidth : [ 1, 1, 1, 1,1 ]} ]};
		chart6 = new Chart(ctx6, {type : "doughnut",data : data6,options : options});
		
	
	
	
//get the bar chart canvas
   //bar chart data
		var data3 = {labels : year,datasets : [ {labels : year,label: " Year wise no.of Vechicle count",data :year_count,
	backgroundColor : ["#FAEBD7", "#DCDCDC","#E9967A","#F5DEB3","#9ACD32",'moccasin','saddlebrown','lightpink' ],
	borderColor : ["#CDA776", "#989898","#CB252B","#E39371","#1D7A46"],borderWidth : 1} ]};
	//create Chart class object
	BarChart = new Chart(ctxBar, {type : "bar",data : data3,options : options});
	//highchart
												
		var stackedBar = {labels : year,datasets : [ {label: 'year_vechicle_count', data: year_count,
backgroundColor:  'green' }, {label: 'imported_vechicle_count', backgroundColor: "orange", data: imported_count } ]};
											
HighChart = new Chart(ctxHigh,{type : 'horizontalBar',data : stackedBar,
			options : {
//  				'onClick' : function(evt, item) {myFunc(evt, item)},
			
		scales: { xAxes: [{  stacked: true }], yAxes: [{  stacked: true }] }}});},//sucess
	error : function(data) {}
		});
	 }//onLoad
	 
	 
	 
	 
	 
	 function prmtStDetails(){
		 
		 
		 try{LineGraph.destroy(); BarChart.destroy(); chart2.destroy();	HighChart.destroy();	 chart1.destroy(); chart6.destroy();
		 year=[]; imported=[]; year_count=[]; imported_count=[];
		  vch_name=[];vch_name_count=[];dist_name=[]; dist_name_count=[]; makerNameCount = [];  makerName= []; 
		 }
		catch(err){}
		
			
				$.ajax({url : '<%=application.getContextPath()%>/GetPermanentStateWiseDetails',type : "POST",
													success : function(data) {
														$("#dropDown").html("");
														console.log(data);	
														for ( var i in data[0]) {
															console.log(data[0][i].count);
															imported.push(data[0][i].stateName);
															imported_count.push(data[0][i].count);
															}
														
														for ( var i in data[1]) {
															year.push(data[1][i].year);
												    		year_count.push(data[1][i].count);	
														}
													for ( var i in data[2]) {
														vch_name.push(data[2][i].vechicleName);
										        		vch_name_count.push(data[2][i].count);	
														}
													for ( var i in data[3]) {
														dist_name.push(data[3][i].stateName);
									            		dist_name_count.push(data[3][i].count);
													}
				
														for ( var k in dist_name ){
													    	$("#dropDown").append('<p  id="' + k + '" onClick=f1(0,[{_index:'+k+'}])>' +dist_name[k]+ '<p>');
													    	}
					options = {	'onClick' : function(evt, item) {myFunc(evt, item)},
										tooltips : {callbacks : {}}};

			var chartdata = {labels : imported,datasets : [{labels: imported,label : "Imported vechicle count",
				fill : false,lineTension : 0.1,backgroundColor : "rgba(29, 202, 255, 0.75)",borderColor : "rgba(29, 202, 255, 1)",
				pointHoverBackgroundColor : "rgba(29, 202, 255, 1)",pointHoverBorderColor : "rgba(29, 202, 255, 1)",data : imported_count} ,
			{labels: imported,label : "Imported vechicle count",fill : false,lineTension : 0.1,backgroundColor : "rgba(59, 89, 152, 0.75)",borderColor : "rgba(59, 89, 152, 1)",
		pointHoverBackgroundColor : "rgba(59, 89, 152, 1)",pointHoverBorderColor : "rgba(59, 89, 152, 1)",data :imported_count}]};
					
				LineGraph = new Chart(ctxLine, {type : 'line',data : chartdata,options : options});

			   //pie chart data
			var data1 = {labels : dist_name,datasets : [ {label:"Customers Count State Wise",data : dist_name_count,backgroundColor : ["#DEB887","#A9A9A9","#DC143C","#F4A460","#2E8B57","#9ACD32",'moccasin','saddlebrown','lightpink'],
		labels : dist_name,borderColor : [ "#CDA776","#989898","#CB252B","#E39371","#1D7A46" ],borderWidth : [ 1, 1, 1, 1,1 ]} ]};

														//pie chart data
		var data2 = {labels : vch_name,datasets : [ {	labels: vch_name,label : "Avg Balance in each state ",data : vch_name_count,backgroundColor : ["#DCDCDC","#E9967A","#9ACD32",'indigo','saddlebrown', 'lightpink',"aqua","red","blue","green" ],borderWidth : [ 1, 1, 1, 1,1 ]} ]};
		//create Chart class object
				chart1 = new Chart(ctxPie, {type : "pie",data : data1,options : options});

			//create Chart class object
				chart2 = new Chart(ctxDht, {type : "doughnut",data : data2,options : options});
		//get the bar chart canvas
		   //bar chart data
				var data3 = {labels : year,datasets : [ {labels : year,label: " Year wise vechicle count ",data :year_count,
			backgroundColor : ["#FAEBD7", "#DCDCDC","#E9967A","#F5DEB3","#9ACD32",'moccasin','saddlebrown','lightpink' ],
			borderColor : ["#CDA776", "#989898","#CB252B","#E39371","#1D7A46"],borderWidth : 1} ]};
			//create Chart class object
			BarChart = new Chart(ctxBar, {type : "bar",data : data3,options : options});
			//highchart
														
				var stackedBar = {labels : year,datasets : [ {label: 'year_vechicle_count', data: year_count,
		backgroundColor:  'green' }, {label: 'imported_vechicle_count', backgroundColor: "orange", data: imported_count } ]};
													
		HighChart = new Chart(ctxHigh,{type : 'horizontalBar',data : stackedBar,
					options : {'onClick' : function(evt, item) {myFunc(evt, item)},
				scales: { xAxes: [{  stacked: true }], yAxes: [{  stacked: true }] }}});},//sucess
			error : function(data) {}
				});
			 }//onLoad

function curntState(){
	onLoad();	
}//curntState
function prmtState() {
	prmtStDetails();
}//prmtState

 
function ajaxCall(name){
	 year=[]; imported=[]; year_count=[]; imported_count=[];
	  vch_name=[];vch_name_count=[];dist_name=[]; dist_name_count=[];noOfVechiclesBodyType=[]; bodyType = []; makerNameCount = [];  makerName = []; 
	
	  
		if(i==11){
			
			i=2;
			 
			$.ajax({
				url : '<%=application.getContextPath()%>/DistSubAggregation',
				type : "GET",
				data:{"name":name},
				success : function(data){
					
					/* for ( var i in data[0]) {
						year.push(data[0][i].year);
			    		year_count.push(data[0][i].count);	
					} */
				for ( var i in data[0]) {
					bodyType.push(data[0][i].bodyType);
					noOfVechiclesBodyType.push(data[0][i].noOfVechicles);	
					}
				/*  for ( var i in data[2]) {
					 dist_name.push(data[2][i].distName);
					 dist_name_count.push(data[2][i].count);
						 
						} */
				dynamicChart();
				i=0;
			}})
		}//if
			else if(dist_wise){
		    i=1;
		 options= {	'onClick' : function(evt, item) {myFunc1(evt, item,10,name)},
					tooltips : {callbacks : {}}};
		$.ajax({url : '<%=application.getContextPath()%>/GetDistrictWiseDetails',
				type : "GET",
				data:{"list":name},
				success : function(data){
					
						for ( var i in data[0]) {
							year.push(data[0][i].year);
				    		year_count.push(data[0][i].count);	
						}
					for ( var i in data[1]) {
						vch_name.push(data[1][i].vechicleName);
		        		vch_name_count.push(data[1][i].count);	
						}
					 for ( var i in data[2]) {
							imported.push(data[2][i].stateName);
							imported_count.push(data[2][i].count);
							 ac_count=data[2][i].account;
							 video_count=data[2][i].videocount;
							}
					 
					dynamicChart();
					
				}});//sucess
		dist_wise=false;
	} else if(vch_ctg){
		i=2;
		 options= {	'onClick' : function(evt, item) {myFunc1(evt, item)},
					tooltips : {callbacks : {}}};
		$.ajax({
			url : '<%=application.getContextPath()%>/GetMakerBodyType2',
			type : "GET",
			data:{"list":name},
			success : function(data){
				
				for ( var i in data[0]) {
					year.push(data[0][i].year);
		    		year_count.push(data[0][i].count);	
				}
			for ( var i in data[1]) {
				bodyType.push(data[1][i].bodyType);
				noOfVechiclesBodyType.push(data[1][i].noOfVechicles);	
				}
			 for ( var i in data[2]) {
				 dist_name.push(data[2][i].distName);
				 dist_name_count.push(data[2][i].count);
					 
					}
			dynamicChart();
			
			}//sucess
			
	})//ajax
	vch_ctg=false;
	}//else if
	else if(year_wise){
		i=3;
		 options= {	'onClick' : function(evt, item) {myFunc2(evt, item,10,name)},
					tooltips : {callbacks : {}}};
		$.ajax({
			url : '<%=application.getContextPath()%>/GetCurrentStateWiseDetails',
			type : "POST",
			data:{"year":name},
			success : function(data){
				
				for ( var i in data[0]) {
					year.push(data[0][i].month);
		    		year_count.push(data[0][i].count);	
				}
			for ( var i in data[1]) {
				bodyType.push(data[1][i].vechicleName);
				noOfVechiclesBodyType.push(data[1][i].count);	
				}
			 for ( var i in data[2]) {
				 dist_name.push(data[2][i].distName);
				 dist_name_count.push(data[2][i].count);
					 
					}
			dynamicChart();
			
			}//sucess
			
	})//ajax
	year_wise=false;
	}//else if
	else if(maker_wise){
		i=4;
		 options= {	'onClick' : function(evt, item) {myFunc1(evt, item)},
					tooltips : {callbacks : {}}};
		$.ajax({
			url : '<%=application.getContextPath()%>/GetMakerBodyType2',
			type : "POST",
			data:{"name":name},
			success : function(data){
				
				for ( var i in data[0]) {
					year.push(data[0][i].year);
		    		year_count.push(data[0][i].count);	
				}
			for ( var i in data[1]) {
				bodyType.push(data[1][i].bodyType);
				noOfVechiclesBodyType.push(data[1][i].noOfVechicles);	
				}
			 for ( var i in data[2]) {
				 dist_name.push(data[2][i].distName);
				 dist_name_count.push(data[2][i].count);
					}
				for ( var i in data[3]) {
					vch_name.push(data[3][i].vechicleName);
	        		vch_name_count.push(data[3][i].count);	
					}
				 for ( var i in data[4]) {
						 ac_count=data[2][i].account;
						 video_count=data[2][i].videocount;
						}
				
			dynamicChart();
		
		
			}//sucess
			
	})//ajax
	maker_wise=false;
	}//else if

	
	
	
}// ajaxCall function


</script>

</div>

</html>