<%@ page language="java" contentType="text/html; charset=UTF8"
    pageEncoding="UTF8"%>
<html lang="zh">
 <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="cache-control" content="maxAge=20000">
	<title>实验平台</title>
	<link href="./res/css/bootstrap.min.css" rel="stylesheet">
	<link href="./res/css/base.css" rel="stylesheet">
	<link rel="shortcut icon" href="./res/images/favicon.ico" type="image/x-icon">
	<link href="./res/css/exp.css" rel="stylesheet">
	<script src="./res/js/jquery.js"></script>
	<script src="./res/js/json2.js"></script>
	<script src="./res/js/modernizr.custom.js"></script>
	<script src="./res/js/support.js"></script>
	<script src="./res/js/sockjs.min.js"></script>
	<!-- [if IE]<script> push_alert("Please use Chrome or other webkit kernel browsers");</script>-->
 </head>
 <body class="cbp-spmenu-push">

 <div id = "override" >
         <input id="btnshow000" type="button" value="开始实验" onclick="showdiv();"/>
 </div>
 	    <input id="userName" type="hidden" value="<%=request.getParameter("username")%>">
 		<%session.setAttribute("pageType","experiment");
 		  session.setAttribute("workgroup",request.getParameter("workGroup"));
 		%>
	<%//session.setAttribute("username","用户001");%>
	<%//session.setAttribute("workgroup","01");%>
	<%//session.setAttribute("pageType","experiment");%>
	<nav role="navigation" class="navbar navbar-inverse">
		<div class="navbar-header">
			<button type="button" data-toggle="collapse" data-target="#example-navbar-collapse" class="navbar-toggle">
				<span class="sr-only">切换导航</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a href="" class="navbar-brand"> Virnet</a>
		</div>
		<div id="example-navbar-collapse" class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-right">
				<li class="active">
					<a href="">切换导航</a>
				</li>
				<li>
					<a href="">leahic</a>
				</li>
				<li>
					<a href="">登出</a>
				</li>
				<li>
					<a href="">导出模板</a>
				</li>
				<li>
					<a href="">登录</a>
				</li>
				<li>
					<a href="">注册</a>
				</li>
			</ul>
		</div>
	</nav>
	<!-- 这里各种选项卡-->
	<div id="TabMain">
		<div class="tabItemContainer" id = "tabItemContainer">
		</div>
		<div class="tabBodyContainer">
			<div class="tabBodyItem tabBodyCurrent">
			   <table id="tabobj">
					<tbody id = "cmdtbody">
						<tr>
							<td>
								<font id="fontsize1">All Rights Reserved (C) VIR_NETWORK.com<br></font>
								<!-- 这里追加滚动显示内容-->
								<div id = "parentDiv">
								
								</div>					
								<div id = "inputParent">
								
								</div>		
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<div class = "menuControlTab" id = "menuControlTab">
		</div>
	</div>
	
	<!-- 滑动按钮-->
	<input type="button" id="showRightPush" value="«"/>
	<div class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-right" id="cbp-spmenu-s2">
		<!-- 这里滑出菜单-->
		<div class="wrapper"> 
			<div id = "status">
					<div style="text-align: center;" id="clock" ></div>
					<button  id = "release" class = "release" onclick="releaseEquipment()">释放资源</button>
			</div>
			
			<!-- 这里是输出结果面板-->		
			<div id="historyMsg"></div>		
			<!-- 这里是控制栏-->			
			<div class="controls" > 
				<div class="items"> 
					<input id="colorStyle" type="color" placeHolder='#000' title="font color" /> 
					<input id="emoji" type="button" value="emoji" title="emoji" /> 
					<input id="clearBtn" type="button" value="clear" title="clear screen" /> 
				</div> 
				<textarea id="messageInput" placeHolder="enter to send" style="font-size:36px; color:#F00"></textarea> 
				<button id="sendBtn" onclick="send()" >SEND</button> 
				<button id="sendBtn"  onclick="closeWebSocket()">CLOSE</button> 
				<div id="emojiWrapper"> 
				</div> 
			</div> 
        </div> 
	</div>
	<div class="container-fluid">
		<canvas width="850" height="550" id="expCanvas" style="cursor: default;"></canvas>
			<div class="canvasButtonGroup">
				<button onclick="editTopoLock()"> 锁定</button>
				<button onclick="releaseTopoLock()">解锁</button>
	
				<button style="margin-left:20px" onclick="topo.create('SW3')">添加SW3</button>
				<button onclick="topo.create('SW2')">添加SW2</button>
				<button onclick="topo.create('RT')">添加RT</button>
				<button onclick="topo.create('PC')">添加PC</button>		
					
				<button style="margin-left:20px" onclick="topo.restart()">重置</button>
				<button onclick="topo.undo()">撤销</button>
				
				<button style="margin-left:20px" onclick="topo.save()">暂存</button>
				<button onclick="topo.submit()">提交</button>				
			</div>
	</div>	
	<script src="./res/js/jtopo-0.4.8-min.js"></script>
	<script src="./res/js/topoCore.js"></script>
	<script src="./res/js/exp.js"></script>
    <script src="./res/js/classie.js"></script>
	<script src="./res/js/commuicate.js"></script>	
 </body> 
</html>