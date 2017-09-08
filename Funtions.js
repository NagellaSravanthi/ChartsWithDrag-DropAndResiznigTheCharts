

function f1(a,b) {
	dist_wise=true;
	   myFunc(a,b);
}//f1

function myAlert( msg) {
	 
		 alert(msg);
	 
}//myAlert
function myFunc2(evt, item,val,name) {
	
	alert(val+"   "+name);
}
function myFunc1(evt, item,val,name) {
	
	try{
		if(evt.target.id=="pie-chartcanvas-1"){
			i=val+1}
		else if(evt.target.id=="bar-chartcanvas"){i=val+2}
		else if(evt.target.id=="pie-chartcanvas-2"){i=val+3}
		else if(evt.target.id=="chart6"){i=val+4}
		else if(evt.target.id=="mycanvas"){}
	}catch(err){}

 
		var msg;
		 try{
			msg=item[0]._chart.data.datasets[0].labels[item[0]._index];
			}
		 catch(err){
			 
			 try{
			 msg=item[0]._view.label;
			 }
			 catch(err){
				
					 msg=dist_name[item[0]._index];
				 
				 }
			 }
		 
		 $("#alertMsg").append(' <div id="alert'+item[0]._index+'"    style=" width: 98px; height: 45px; padding-top: 3px;float:left	"  class="alert alert-info alert-dismissable fade in">'+ ' <a href="" onclick=false;  class="close" data-dismiss="alert" aria-label="close">&times;</a>'
				   +'<div onclick=myAlert("'+msg+'")><strong>'+ msg   +'</strong></div> </div>');
		 $("#alertMsg").append(' <div id="alert'+item[0]._index+'"    style=" width: 98px; height: 45px; padding-top: 3px;float:left	" class="alert alert-info">'+ ' <a href="" onclick=false;  class="close" data-dismiss="alert" aria-label="close">&times;</a>'
				   +'<strong>'+ msg   +'</strong> </div>');
	  msg=name+","+msg
	   //ajax
	   ajaxCall(msg);
}

//onClick event funtion
function myFunc(evt, item) {
	
try{
	if(evt.target.id=="pie-chartcanvas-1"){dist_wise=true;}
	else if(evt.target.id=="bar-chartcanvas"){year_wise=true;}
	else if(evt.target.id=="pie-chartcanvas-2"){vch_ctg=true;}
	else if(evt.target.id=="chart6"){maker_wise=true;}
	else if(evt.target.id=="mycanvas"){}
}catch(err){}


	 
	var msg;
	 try{
		msg=item[0]._chart.data.datasets[0].labels[item[0]._index];
		}
	 catch(err){
		 
		 try{
		 msg=item[0]._view.label;
		 }
		 catch(err){
			
				 msg=dist_name[item[0]._index];
			 
			 }
		 }
	 
	 $("#alertMsg").append(' <div id="alert'+item[0]._index+'"    style=" width: 98px; height: 45px; padding-top: 3px;float:left	" class="alert alert-info">'+ ' <a href="" onclick=false;  class="close" data-dismiss="alert" aria-label="close">&times;</a>'
			   +'<div onclick=myAlert("'+msg+'") class="alert alert-info"><strong>'+ msg   +'</strong></div> </div>');
  
   //ajax
   ajaxCall(msg);
   
}// myFunc  function


function dynamicChart(){
	$("#dropDown").html("");

	 try{LineGraph.destroy(); BarChart.destroy(); chart2.destroy();	HighChart.destroy();chart1.destroy();chart6.destroy();
	 }
	catch(err){}
if(i==1){
		var data1 = {labels : vch_name,datasets : [ {label:"Customers Count State Wise",data :vch_name_count,backgroundColor : ["#DEB887","#A9A9A9","#DC143C","#F4A460","#2E8B57","#9ACD32",'moccasin', 'saddlebrown', 'lightpink'],labels :vch_name,
			borderColor : [ "#CDA776","#989898","#CB252B","#E39371","#1D7A46" ],borderWidth : [ 1, 1, 1, 1,	1 ]} ]};
		for ( var k in vch_name ){
	    	$("#dropDown").append('<p  id="' + k + '" onClick=f1(0,[{_index:'+k+'}])>' +vch_name[k]+ '<p>');
	    	}
	}else if(i==2||i==3||i==4){
		
		var data1 = {labels : dist_name,datasets : [ {label:"Customers Count State Wise",data :dist_name_count,backgroundColor : ["#DEB887","#A9A9A9","#DC143C","#F4A460","#2E8B57","#9ACD32",'moccasin', 'saddlebrown', 'lightpink'],labels :dist_name,
			borderColor : [ "#CDA776","#989898","#CB252B","#E39371","#1D7A46" ],borderWidth : [ 1, 1, 1, 1,	1 ]} ]};
		for ( var k in dist_name ){
	    	$("#dropDown").append('<p  id="' + k + '" onClick=f1(0,[{_index:'+k+'}])>' +dist_name[k]+ '<p>');
	    	}
	}
	  //create Chart class object
chart1 = new Chart(ctxPie, {type : "pie",data : data1,options : options});
 
	 if(i==4){
		chartdata = {labels:vch_name,datasets: [{labels: vch_name,label: "Vechicle Name Wise count  ",fill: false,lineTension: 0.1,backgroundColor: "rgba(59, 89, 152, 0.75)",borderColor: "rgba(59, 89, 152, 1)",
pointHoverBackgroundColor: "rgba(59, 89, 152, 1)",pointHoverBorderColor: "rgba(59, 89, 152, 1)",data: vch_name_count}]};
	 
	 LineGraph = new Chart(ctxLine, { type: 'line', data: chartdata, options:options});
	 }
	    //pie chart data

if(i==1){
var	data2 = {labels: ["impoted count","ac count","ac and video count"],datasets: [ {labels: ["impoted count","ac count","ac and video count"],data: [imported_count,ac_count,video_count],
		  backgroundColor: ["#DCDCDC","#E9967A","#9ACD32",'indigo','saddlebrown','lightpink',"aqua","red","blue","green" ],
			                borderWidth: [1, 1, 1, 1, 1]  }]};
}else if(i==2||i==3||i==4){
	
var	data2 = {labels: bodyType,datasets: [ {labels: bodyType,data: noOfVechiclesBodyType,
		  backgroundColor: ["#DCDCDC","#E9967A","#9ACD32",'indigo','saddlebrown','lightpink',"aqua","red","blue","green" ],
			                borderWidth: [1, 1, 1, 1, 1]  }]};
}

 
 chart2 = new Chart(ctxDht, { type: "doughnut", data: data2, options:options });
 if(i==1||i==2||i==4){
	 var databar = {labels:year,datasets: [{labels: year,label: " year wise count", data: year_count,
		 backgroundColor: [ "green","#FAEBD7", "red", "#E9967A","aqua","#9ACD32",'moccasin',"blue",'saddlebrown', 'lightpink',"red" ], borderWidth: 1  }] }; 
	 }
 else if(i==3){
	 var databar = {labels:year,datasets: [{labels: year,label: " Month  wise count", data: year_count,
		 backgroundColor: [ "green","#FAEBD7", "red", "#E9967A","aqua","#9ACD32",'moccasin',"blue",'saddlebrown', 'lightpink',"red" ], borderWidth: 1  }] }; 
		}
	    BarChart = new Chart(ctxBar, {type: "bar", data: databar, options: options});
	    if(i==4){
	    	 var config = { type: 'horizontalBar', data: {  labels:imported, datasets: [{label: 'ac count',data: ac_count, backgroundColor:  'green', fill: false}, { label: 'Video count', backgroundColor: "blue",data:  video_count }
	    ] }, options: {'onClick' : function(evt, item) {myFunc(evt, item)},scales: { xAxes: [{stacked: true }], yAxes: [{ stacked: true }] }}};
	    		 HighChart=new Chart(ctxHigh, config);
	    		 	 	
	    }

}//dynamic chart


