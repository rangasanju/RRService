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
	<spring:url value="/resources/images/hands.png" var="hands" />
	<spring:url value="/resources/images/circle-512.png" var="circle" />
	
	
	
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
<body> 

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
            <h1 class="cover-heading">Authenticating .....</h1>
            
          <div class="col-sm-12">
              &nbsp;
	      </div>
	      
          <div class="col-sm-12">
              &nbsp;
	      </div>
	      
	      <div id="output" class="col-sm-12">
              <p class="lead">Please Wait</p>
	      </div>
	      
	      
	      <div class="col-sm-12">
              &nbsp;
	      </div>
	      
	      <div class="col-sm-12">
              &nbsp;
	      </div>
          <div class="col-sm-12">
              &nbsp;
	      </div>
	      
          <div class="col-sm-12">
              &nbsp;
	      </div>
	        <div class="col-sm-12">
              &nbsp;
	      </div>
	      
	      <div class="col-sm-12">
              &nbsp;
	      </div>
	          
          <div class="col-sm-12">
              &nbsp;
	      </div>
	      
          <div class="col-sm-12">
              &nbsp;
	      </div>
	        <div class="col-sm-12">
              &nbsp;
	      </div>
	      
	      <div class="col-sm-12">
              &nbsp;
	      </div>
          <div class="col-sm-12">
              &nbsp;
	      </div>
	    
	          
        
	      
	     
        
	      <div class="col-sm-12">
              &nbsp;
	      </div>
	      
	      <div class="col-sm-12">
              &nbsp;
	      </div>
	    

          <div class="mastfoot">
            <div class="inner">
              <p><i18n:message key="label.PAGEFOOTER.trademark"/><a href="https://twitter.com/mdo"><i18n:message key="label.PAGEFOOTER.company"/></a>.</p>
            </div>
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

window.onload=showUsers;


var reqFeature;
var xmlhtp ;


function trim(str)
{
   return str.replace(/^\s*|\s*$/g,"");
}

function onLoad()
{	
	
}

function returnToParent()
{
	window.opener.postMessage({ message: "OK", result: true }, "*");
}


function showUsers() {            	
   
    $.ajax({
    	
        url : "getMacAddress",
        type : "GET",
        
        success : function(response) {
        	        	
        	window.opener.postMessage({ message: response, result: true }, "*");	
        	        	
        },
        error : function(xhr, status, error) {
            alert(" ERROR : "+ xhr.responseText);
        }
    });
    return false;
}


function getMacAddress()
{
	
			
		var url="getMacAddress";
		if (window.XMLHttpRequest)
		{ // Non-IE browsers
				reqFeature = new XMLHttpRequest();
			try{
				reqFeature.open("GET", url, true);
				}catch (e){
				alert(e);
				}
				reqFeature.onreadystatechange = receiveOutput;
				reqFeature.send(null);
		}
		else if (window.ActiveXObject)
		{ // IE
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
			try{
				status=reqFeature.status;
				if (reqFeature.readyState == 3){ // Complete					
						if (reqFeature.status == 200)
						{ // OK response
							xmlhtp = reqFeature.responseText;						
						
							window.opener.postMessage({ message: xmlhtp, result: true }, "*");	
						}
					}
				}
				catch(e)
				{
					status="Not found";
				}
		}




function ReturnToCMS(){
	

	if(no_of_fingers == 0)
		{
		window.opener.postMessage({ message: "cancel", result: true }, "*");		
		}

	

}






</script>
</body>
</html>

