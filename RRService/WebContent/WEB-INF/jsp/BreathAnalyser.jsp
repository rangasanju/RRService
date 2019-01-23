<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page pageEncoding="UTF-8"%>


<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<i18n:bundle baseName="com.railways.tayal.rr.properties.ApplicationResources" localeRef="userLocale"/>


<html lang="en" >
<head>

 	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="images/tayallogo.ico">
    
    <spring:url value="/resources/static/css/bootstrap.min.css" var="bootstrapCSS" />
    <spring:url value="/resources/assets/css/ie10-viewport-bug-workaround.css" var="workaroundCSS" />
    <spring:url value="/resources/static/cover.css" var="coverCSS" />
    
	<spring:url value="/resources/assets/js/ie-emulation-modes-warning.js" var="ieemulationJS" />
	
	
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	
	<script src="${ieemulationJS}"></script>
	


<title><i18n:message key="label.APPTITLE.title"/></title>
<link rel="stylesheet" href="css/mybuttons.css" />
    <!-- Bootstrap core CSS -->
    <link href="${bootstrapCSS}" rel="stylesheet" />

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="${workaroundCSS}" rel="stylesheet" />

    <!-- Custom styles for this template -->
    <link href="${coverCSS}" rel="stylesheet" />

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="${ieemulationJS}"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body  >

 
  
  

    <div class="site-wrapper">

      <div class="site-wrapper-inner">

        <div class="cover-container">

          <div class="masthead clearfix">
            <div class="inner">
            <img class="masthead-brand masthead-brand-img" src="images/tayallogo.jpg" > 
            <h3 class="masthead-brand">&nbsp;&nbsp;&nbsp;&nbsp;<i18n:message key="label.PAGETITLE.appname"/></h3>
              
              <nav>
                <ul class="nav masthead-nav">
                  <li class="active"><a href="#">Home</a></li>
                  <li><a href="#">Features</a></li>
                  <li><a href="#">Contact</a></li>
                </ul>
              </nav>
            </div>
          </div>

          <div class="inner cover">
            <h1 class="cover-heading"><i18n:message key="label.PAGETITLE.runningroom.ba"/></h1>
          
            
           <div class="col-sm-12">
              &nbsp;
	      </div>
	      
          <div class="col-sm-12">
              &nbsp;
	      </div>
	      
         
          <div id="output" class="col-sm-12">
              <p class="lead">Click on the button to start the breath analysis</p>
	      </div>
	      
	      <div class="col-sm-12">
              &nbsp;
	      </div>
	      
	      
	      <div class="col-sm-12">
	      	<a href="#" class="btn btn-lg btn-primary btn-primary-spacing" onclick="startBreath()">Start BA</a> 	
          </div>
         
          </div>

          <div class="mastfoot">
            <div class="inner">
              <p><i18n:message key="label.PAGEFOOTER.trademark"/><a href="https://twitter.com/mdo"><i18n:message key="label.PAGEFOOTER.company"/></a>.</p>
            </div>
          </div>

        </div>

      </div>

    </div>
    
 
	<spring:url value="/resources/static/jquery.min.js" var="jqueryJS" />
	<spring:url value="/resources/assets/js/vendor/jquery.min.js" var="assetjqueryJS" />
	<spring:url value="/resources/static/js/bootstrap.min.js" var="bootstrapJS" />
	<spring:url value="/resources/assets/js/ie10-viewport-bug-workaround.js" var="workaroundJS" />
	
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${jqueryJS}"></script>
    <script>window.jQuery || document.write('<script src="${jqueryJS}"><\/script>')</script>
    <script src="${bootstrapJS}"></script>
    
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${workaroundJS}"></script>
 

  

<script>



function trim(str)
{
   return str.replace(/^\s*|\s*$/g,"");
}





var reqFeature;
var xmlhtp ;
var res;
var resfordisplay;
var size=0;






function startBreath()
{
	
	
	document.forms[0].method.disabled="true";
	
	document.getElementById("output").innerHTML="Please Wait .... ";
	
	
	blinkFont();

		var url="performBA";
						if (window.XMLHttpRequest){ // Non-IE browsers
							reqFeature = new XMLHttpRequest();
						try{
							reqFeature.open("GET", url, true);
							}catch (e){
							alert(e);
							}
							reqFeature.onreadystatechange = receiveOutput;
							reqFeature.send(null);
							}
							else if (window.ActiveXObject){ // IE
							reqFeature = new ActiveXObject("Microsoft.XMLHTTP");
							if (reqFeature){
							//alert('IE');
							reqFeature.open("GET", url, true);
							reqFeature.onreadystatechange = receiveOutput;
							reqFeature.send(null);
						}
					}	
	
						
	
	
}


function receiveOutput(){

			var status;
			var crewid = document.forms[0].crewid.value;		
			
			
			try{
				status=reqFeature.status;
				if (reqFeature.readyState == 3){ // Complete					
						if (reqFeature.status == 200)
						{ // OK response
							
							
							//document.getElementById("output").style.color="green";
							xmlhtp = reqFeature.responseText;	
							
							
							if(xmlhtp.indexOf("mg/100ml") > -1 || xmlhtp.indexOf("mg/100mL") > -1)
							{
								
									
									res=xmlhtp.substring(xmlhtp.indexOf("TAYAL"));
									var today = new Date();
									
									// TWO DIGIT DAY
									var day = today.getDate();
									var twodigitday = day < 10 ? '0' + day : '' + day;   // BECAUSE ONE DIGIT DAY CREATES STRING REPRESENTATION PROBLEM IN DB2 QUERY
									
									// TWO DIGIT MONTH
									var month = today.getMonth() + 1;
									var twodigitmonth = month < 10 ? '0' + month : '' + month;
									
									// DATE									
									var date = twodigitday + "/" + twodigitmonth + "/" + today.getFullYear();
									
									
									
									// TWO DIGIT HRS
									var hrs = today.getHours();
									var twodigithrs = hrs < 10 ? '0' + hrs : '' + hrs;   // BECAUSE ONE DIGIT HRS CREATES STRING REPRESENTATION PROBLEM IN DB2 QUERY
									
									
									// TWO DIGIT MIN
									var min = today.getMinutes();
									var twodigitmin = min < 10 ? '0' + min : '' + min;   // BECAUSE ONE DIGIT MIN CREATES STRING REPRESENTATION PROBLEM IN DB2 QUERY
									
									
									var time = twodigithrs + ":" + twodigitmin ;
									
									
									
									
									var newdatetime = "Date          : " + date + "\nTime          : " + time +"\n";
									
									var strtobereplaced = res.substring(res.indexOf("Date"),res.indexOf("Result"));
									
									res = res.replace(strtobereplaced,newdatetime);
									//alert(res);
									var resfordisplaytemp=xmlhtp.substring(xmlhtp.indexOf("TAYAL"),xmlhtp.indexOf("image64:")).replace(/^\s*[\r\n]/gm, "");
									resfordisplay=     resfordisplaytemp.replace(strtobereplaced,newdatetime);                                        
									//alert(resfordisplay);
							
									 res1 = parseFloat(res.substring(res.indexOf("Result")+14,res.indexOf("mg/100ml")));
								


								

								
								if(res1 > 0)
								{
									document.getElementById("result").innerHTML="<table border='0' bgcolor='red'><tr><td> <textarea rows='11' cols='30' style='overflow: hidden; background-color: red'>" + res + " </textarea></td></tr></table>";
									
								}else
								{
									document.getElementById("result").innerHTML="<table border='0' ><tr><td> <textarea style='overflow: hidden;font-size : 12pt' rows='11' cols='30'>" + res + " </textarea></td></tr></table>";
									
								}
								document.getElementById("output").style.visibility="hidden";
							}
							document.getElementById("output").innerHTML='';
							document.getElementById("output").innerHTML=xmlhtp.substring(size);
							size=xmlhtp.length;
							
							//document.getElementById("output").scrollTop = document.getElementById("output").scrollHeight; 
							
							
							if(xmlhtp.indexOf("ERROR") > -1)
							{
								res="Device ERROR : Device is not connected. Connect the device properly and try again. If still problem persists, contact CMS Helpdesk";
								window.opener.postMessage({ message: res, result: true }, "*");
							}
							
							if(xmlhtp.indexOf("TimeOut") > -1)
							{
								res="TimeOut";
								window.opener.postMessage({ message: res, result: true }, "*");
							}
							
							if(xmlhtp.indexOf("Please Start Blowing") > -1 || xmlhtp.indexOf("Please blow into the BA device") > -1)
							{
								
								clearInterval(timer); 
								document.getElementById("output").style.background="green";  
								
																
							}
						
							
					}
				  }
				}
				catch(e)
				{
					status="Not found";
				}
		}




</script>
</body>
</html>

