<%@ page language="java" contentType="text/html; charset=UTF8"
    pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>试验管理界面</title>
<link rel="stylesheet" href="./res/css/loginstyle.css" media="screen" type="text/css" />
<script src="./res/js/support.js"></script>
<script src="./res/js/grouparrange.js"></script>
<script src="./res/js/modernizr.js"></script>
</head>
<body>
	<div>
		<%
		String userName = request.getParameter("username");  
		String workGroup = request.getParameter("workGroup"); 
		session.setAttribute("username",userName);
		session.setAttribute("workgroup",workGroup);
		session.setAttribute("pageType","arrange");
		%>
	<button id="sendBtn" onclick="showAndHideThings()" >点击排队</button> 
	</div>
	 <div class="loader" id="loader" style="display:none;">队列中...</div>
	 <form id = "submitform" action="exp.jsp" method="get" style="display:none;" >
              <input id = "userName" type="text" name="username"></input>
              <input id ="groupID" type="text" name="workGroup"></input>        
     </form>
</body>
</html>