var status = document.getElementById('status');
var color = document.getElementById('colorStyle');
var messageinput = document.getElementById('messageInput');
var sendbutton = document.getElementById('sendBtn');
var historyMessage = document.getElementById('historyMsg');
var clearScreen = document.getElementById('clearBtn');
var websocket = null;
var showingTabItem = 0; //正在显示的cmd窗口编号
var EquipmentAmount = null;
var keyArr=[];
var name_str,num_str,port_str;
var messageJson = { 
					"flag":"experiment",
					"experimentStatus":"default",
					"type":"default",
					"cabinet_num":"default",
					"color":"default",
					"content":"default",
					"user":"default",
					"lock":"unlock",
					"equipmentNumber":"default",	//设备数量
					"equipmentName":"default",		//设备名串
					"inputEquipmentNumber":"default",   //当前操作的设备号
					"equipmentNumStr":"default",  	//设备序号串
					"equipmentPortStr":"default",	//设备序号串对应下的各设备可用端口号串
					"position":"default",		//设备的位置 设备的X，Y坐标以空格相隔，设备间以逗号相隔
					"leftNUM_Str":"default",   	//左端设备序号串，“##”隔开
					"rightNUM_Str":"default",  	//右端设备序号串，“##”隔开
					"leftport_Str":"default", 	//左端设备端口序号串，“##”隔开
					"rightport_Str":"default",	//右端设备端口序号串，“##”隔开
					"success":"default",
					"expId":"default",          //实验Id     
					"expTaskOrder":"default",    //当前操作的任务序号
					"expTaskNum":"default"     //本实验的任务个数（包含初始）
				  };
//判断当前浏览器是否支持WebSocket
  if ('WebSocket' in window) {
	  
  websocket= new WebSocket("ws://" + host + root_path + "websck");
  
  }
  else {
	  
   websocket = new SockJS("http://" + host + root_path + "sockjs/websck");
	
  }
  this.initialEmoji();

//连接发生错误的回调方法
websocket.onerror = function(event){
	
};

//连接成功建立的回调方法
websocket.onopen = function(event){
    
}

//接收到消息的回调方法
websocket.onmessage = function(event){
	  var object = JSON.parse(event.data);
	  if(object.type == "requestEquipment")
	  {
		 var temp = object.equipmentName;
		 messageJson.equipmentName = object.equipmentName;
		 messageJson.equipmentNumber = object.equipmentNumber;
		 messageJson.expTaskNum = object.expTaskNum;
		 EquipmentAmount = object.equipmentNumber;
		 var keyValueArr = temp.split('##');		
		 for(var i=0;i<keyValueArr.length;i++)
		 {
			   keyArr[i] = keyValueArr[i]; 
		 }
		 showCmdTab(object.equipmentNumber,keyArr);
		 showHideCmdTab(object.equipmentNumber);
		 topoAndConfigureButton();
	  }
	  
	  if(object.type == "sendEquipment"){
		//给jtopo需要的数据赋值
		  messageJson.name_str = object.equipmentName;
		  messageJson.num_str = object.equipmentNumStr;
		  messageJson.port_str = object.equipmentPortStr;
		  messageJson.cabinet_num = object.cabinet_num;
		  num_str = object.equipmentNumStr;
		  port_str = object.equipmentPortStr;
		  name_str = object.equipmentName;
			 canvas = $("#expCanvas").get(0);
			 topo.init(canvas,name_str,num_str,port_str);
			 topo.show();
	  }
	  
	  if(object.type == "communication")
	  {
		  setMessageInnerHTML(event.data);
	  }
	  if(object.type == "command")
	  {
		  setCommandInnerHTML(event.data);
	  } 
	  if(object.type == "lock")
	  {
		  if(object.lock == "unlock")
		  {enableButton(object.inputEquipmentNumber);}
		  if(object.lock == "lock")
		  {disableButton(object.inputEquipmentNumber);}
	  }
	  if(object.type == "topolock")
	  {
	  	  if(object.lock == "unlock")
		  {disableTopo();}
		  if(object.lock == "lock")
		  {enableTopo();}
	  }
	  if(object.type == "topoedit")
	  {
		  if(object.success == true)
			  {
			  alert("提交成功");
			  }
		  else{alert("提交失败");}
	  }
	  
	  if(object.type == "equipConnectionInfo")
	  {	
			  topo.clone(object);
	  }
	  if(object.type == "release")
	  {
		  if(object.success == true)
			  {
			  	alert("释放成功");
			    document.getElementById("release").disabled= true;
			    document.getElementById("release").style.background = 'grey';
			  }
		  else{alert("释放失败");}
	  }
	  if(object.type == "topoSaveToDatabase")
	  {	
		  //关闭加载页面
		  document.getElementById("cover").style.display = "none";
		  document.getElementById("layout").style.display = "none";
		  if(object.success == true)
		  {
			  alert("保存成功");
		  }
		  else{alert("保存失败");}
	  }
	  if(object.type == "saveConfigureFile")
	  {	
		  //关闭加载页面
		  document.getElementById("cover").style.display = "none";
		  document.getElementById("layout").style.display = "none";
		  if(object.success == true)
		  {
			  alert("保存成功");
		  }
		  else{alert("保存失败");}
	  }	 
	  if(object.type == "pingVerify")
	  {	
		  //关闭加载页面
		  document.getElementById("cover").style.display = "none";
		  document.getElementById("layout").style.display = "none";
		  if(object.success == true)
		  {
			  alert("验证完成");
		  }
		  else{alert("验证被中断");}
	  }
}

//连接关闭的回调方法
websocket.onclose = function(){
	setMessageInnerHTML("已断开连接");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function(){
	websocket.close();
}

//关闭连接
function closeWebSocket(){
	websocket.close();
}
function start(expId){
	messageJson.type = "requestEquipment";
	messageJson.expId = expId;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

//发送消息
function send(){
	var message = messageinput.value;
	messageinput.value="";
	messageInput.focus();
	messageJson.color = color.value;
	messageJson.content = message;
	messageJson.type = "communication";
	messageJson.user = document.getElementById('userName').value;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}
//编辑锁定
function editLock(){
	messageJson.lock = "lock";
	messageJson.type = "lock";
	messageJson.inputEquipmentNumber = showingTabItem;
	document.getElementById("editBtn" + showingTabItem).disabled= true;
	document.getElementById("editBtn" + showingTabItem).name  = "show";
	document.getElementById("cancelBtn" + showingTabItem).disabled= false;
	document.getElementById("editBtn" + showingTabItem).style.background  = 'grey';
	document.getElementById("cancelBtn" + showingTabItem).style.background = '#3bb3e0';
	document.getElementById("dosform"+showingTabItem).style.display = "";
	document.getElementById("dosform"+showingTabItem).disable = false;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}
//解除锁定
function releaseLock(){
	messageJson.lock = "unlock";
	messageJson.type = "lock";
	messageJson.inputEquipmentNumber = showingTabItem;
	document.getElementById("editBtn" + showingTabItem).disabled= false;
	document.getElementById("editBtn" + showingTabItem).name  = "";
	document.getElementById("cancelBtn" + showingTabItem).disabled= true;
	document.getElementById("cancelBtn" + showingTabItem).style.background = 'grey';
	document.getElementById("editBtn" + showingTabItem).style.background  = '#3bb3e0';
	document.getElementById("dosform"+showingTabItem).style.display = "none";
	document.getElementById("dosform"+showingTabItem).disable = true;
	var mess = JSON.stringify(messageJson);
	websocket.send(mess);
}

function editTopoLock(){
	  //为编辑拓扑取锁
	  messageJson.lock = "lock";
	  messageJson.type = "topolock";
	  var mess = JSON.stringify(messageJson);
	  websocket.send(mess);
	}

	/**
	 * 
	 */
	function releaseTopoLock(){
	  //为编辑拓扑解锁
	  messageJson.lock = "unlock";
	  messageJson.type = "topolock";
	  var mess = JSON.stringify(messageJson);
	  websocket.send(mess);
	}

function disableButton(number){
	document.getElementById("editBtn" + number).disabled= true;
	document.getElementById("cancelBtn" + number).disabled= false;
	document.getElementById("editBtn" + number).style.background  = 'grey';
	document.getElementById("cancelBtn" + number).style.background = '#3bb3e0';
	document.getElementById("dosform"+showingTabItem).style.display = "none";
	document.getElementById("dosform"+number).disable = true;
}
function enableButton(number){
	document.getElementById("editBtn" + number).disabled= false;
	document.getElementById("cancelBtn" + number).disabled= true;
	document.getElementById("cancelBtn" + number).style.background = 'grey';
	document.getElementById("editBtn" + number).style.background  = '#3bb3e0';
	document.getElementById("dosform"+showingTabItem).style.display = "none";
	document.getElementById("dosform"+number).disable = true;
}
function enableTopo(){
	$(".canvasButtonGroup button").each(function(index , e){
		if (index == 0) {
			e.disabled = true;
			e.style.background = 'grey';
		}
		else{
			e.disabled = false;
			e.style.background = '#3bb3e0';	
		}
	});
}

function disableTopo(){
	$(".canvasButtonGroup button").each(function(index , e){
		if (index == 0) {
			e.disabled = false;
			e.style.background = '#3bb3e0';	
		}
		else{
			e.disabled = true;
			e.style.background = 'grey';	
		}		
	});
}

	//清屏
	clearScreen.addEventListener('click', function() {
		document.getElementById('historyMsg').innerHTML = '';
	}, false);

	//表情控制项
	document.getElementById('emoji').addEventListener('click', function(e) {
		var emojiwrapper = document.getElementById('emojiWrapper');
		emojiwrapper.style.display = 'block';
		e.stopPropagation();
	}, false);
	
	document.body.addEventListener('click', function(e) {
		var emojiwrapper = document.getElementById('emojiWrapper');
		if (e.target != emojiwrapper) {
			emojiwrapper.style.display = 'none';
		};
	});
	 document.getElementById('emojiWrapper').addEventListener('click', function(e) {
		var target = e.target;
		if (target.nodeName.toLowerCase() == 'img') {
			var messageInput = document.getElementById('messageInput');
			messageInput.focus();
			messageInput.value = messageInput.value + '[emoji:' + target.title + ']';
		};
	}, false);

//显示表情的函数		
function  initialEmoji() {
	var emojiContainer = document.getElementById('emojiWrapper'),
		docFragment = document.createDocumentFragment();
	for (var i = 1; i <= 60; i++) {
		var emojiItem = document.createElement('img');
		emojiItem.src = 'emotions/emoji/' + i + '.png';
		emojiItem.title = i;
		docFragment.appendChild(emojiItem);
	};
	emojiContainer.appendChild(docFragment);
}	
function showEmoji(msg) {
	var match, result = msg,
		reg = /\[emoji:\d+\]/g,
		emojiIndex,
		totalEmojiNum = document.getElementById('emojiWrapper').children.length;
	while (match = reg.exec(msg)) {
		emojiIndex = match[0].slice(7, -1);
		if (emojiIndex > totalEmojiNum) {
			result = result.replace(match[0], '[X]');
		} else {
			result = result.replace(match[0], '<img class="emoji" src="emotions/emoji/' + emojiIndex + '.png" />');//todo:fix this in chrome it will cause a new request for the image
		};
	};
	return result;
}	 



//聊天消息显示窗口
function setMessageInnerHTML(msg){
	var msgToDisplay = document.createElement('p'),
	date = new Date().toTimeString().substr(0, 5);
	var obj = JSON.parse(msg);
	msgToDisplay.style.color = obj.color || '#000';
	 obj.content = this.showEmoji(obj.content);
	msgToDisplay.id = 'fontsize1';
	msgToDisplay.innerHTML = obj.user+ '<span class="timespan" style="color: red;">(' + date + '): </span>' + obj.content;
	historyMessage.appendChild(msgToDisplay);
	historyMessage.scrollTop = historyMessage.scrollHeight; 
}	 

//控制台信息显示窗口
function setCommandInnerHTML(msg){

 var jsonCommand = JSON.parse(msg); 
 var temp = jsonCommand.content;
 var equipNumber = jsonCommand.inputEquipmentNumber;
 	 temp = temp.replace("<","&lt;");
 	 temp = temp.replace(">","&gt;"); 
 	 var command = temp.replace(/\r\n/g,"<br/>");  
 	 command =  command.replace(/\n/g,"<br/>");
 	 var jtp = document.getElementById('showTab' + equipNumber);
 	 var msgToShow = document.createElement('font');
	 msgToShow.style.color = '#FFFFFF';
	 msgToShow.id = 'fontsize1';
	 msgToShow.innerHTML = command;
	jtp.appendChild(msgToShow);
	$("#cmdtbody").scrollTop(9999);	
}

//控制台输入窗口
function cmdSend(){
	var cmdMessageInput = document.getElementById('dosform' + showingTabItem);
	var message = cmdMessageInput.value;
		cmdMessageInput.value="";
		cmdMessageInput.focus();
        messageJson.content = message;
        messageJson.type = "command";
        messageJson.inputEquipmentNumber = showingTabItem;
        var mess = JSON.stringify(messageJson);
        websocket.send(mess);
}
function releaseEquipment(){
	messageJson.type = "release";
	var mess = JSON.stringify(messageJson);
    websocket.send(mess);
}


//将拓扑连接信息发送到一层NTC
function topoSend(position , leftNUM_Str , rightNUM_Str , leftport_Str , rightport_Str){
	messageJson.position = position;
    messageJson.leftNUM_Str = leftNUM_Str;
    messageJson.rightNUM_Str = rightNUM_Str;
    messageJson.leftport_Str = leftport_Str;
    messageJson.rightport_Str = rightport_Str;
    messageJson.type = "topoedit";
    var mess = JSON.stringify(messageJson);
    websocket.send(mess);
}

//保存拓扑信息到数据库
function topoSaveToDatabase(position , leftNUM_Str , rightNUM_Str , leftport_Str , rightport_Str , expTaskOrder){
	messageJson.position = position;
    messageJson.leftNUM_Str = leftNUM_Str;
    messageJson.rightNUM_Str = rightNUM_Str;
    messageJson.leftport_Str = leftport_Str;
    messageJson.rightport_Str = rightport_Str;
    messageJson.position = position;
    messageJson.expTaskOrder = expTaskOrder;
    messageJson.type = "topoSaveToDatabase";
    var mess = JSON.stringify(messageJson);
    websocket.send(mess);
}

//遮罩层控制函数   
function showdiv() { 
	 document.getElementById("override").style.display ="none";
	 messageJson.experimentStatus = "start";
	 messageJson.type = "";
	 var mess = JSON.stringify(messageJson);
	 websocket.send(mess); 
	 messageJson.experimentStatus = "";
}
//这里是动态生成cmd窗口的逻辑
function showCmdTab(number, name_Str){
	var parent = document.getElementById("parentDiv");
	var liParent = document.getElementById("tabItemContainer");
	var inputParent = document.getElementById("inputParent");
	var buttonParent = document.getElementById("menuControlTab");
	
	for(var i=0; i<number; i++){
		var newli = document.createElement('li');
		var newa = document.createElement('a');
		var j = i+1;
		newa.innerHTML = name_Str[i] + "_" + j;
		newa.setAttribute("id", "tabItema"+i);
		newa.setAttribute("value", i);
		if(i==0)
		{
			newa.setAttribute("class", "tabItemCurrent");
		}
		newli.appendChild(newa);
		liParent.appendChild(newli);
		
		var newDiv = document.createElement('div');
		newDiv.setAttribute("id", "showTab"+ i);
		newDiv.setAttribute("class", "showTab");
		newDiv.style.display = 'none';
		if(i==0)
		{
			newDiv.style.display = '';
		}
		parent.appendChild(newDiv);
		
		//input框自动生成
		var newInput = document.createElement('input');
		newInput.setAttribute("type", "text");
		newInput.setAttribute("id", "dosform" + i);
		newInput.setAttribute("class", "dosform");
		newInput.setAttribute("autocomplete", "off");
		newInput.setAttribute("name", "input");
		newInput.setAttribute("size", "64");
		newInput.setAttribute("maxlength", "100");
		newInput.setAttribute("onkeypress", "if (event.keyCode == 13){cmdSend();}");
		newInput.style.display = 'none';


		inputParent.appendChild(newInput);
		//编辑、撤销按钮动态生成
		var newEditButton = document.createElement('button');
		newEditButton.setAttribute("id", "editBtn" + i);
		newEditButton.setAttribute("class", "editBtn");
		newEditButton.setAttribute("onclick", "editLock()");
		newEditButton.innerHTML = "编辑";
		newEditButton.style.display = 'none';
		if(i==0)
		{
			newEditButton.style.display = '';
		}
		
		var newReleaseButton = document.createElement('button');
		newReleaseButton.setAttribute("id", "cancelBtn" + i);
		newReleaseButton.setAttribute("class", "cancelBtn");
		newReleaseButton.setAttribute("onclick", "releaseLock()");		
		newReleaseButton.setAttribute("disabled", "true");
		newReleaseButton.innerHTML = "撤销";
		newReleaseButton.style.display = 'none';
		if(i==0)
		{
			newReleaseButton.style.display = '';
		}
		
		buttonParent.appendChild(newEditButton);
		buttonParent.appendChild(newReleaseButton);
	}
}
//显示或隐藏cmd窗口的逻辑
function showHideCmdTab(order){
	for(var i=0;i<order;i++)
		 {
			document.getElementById('tabItema'+i).onclick=function(e) 
			 {
				var target = e.target;
				var id = target.id;
				var cc = id.substring(id.length-1,id.length);
			    for(var j = 0; j<order;j++)
		    	{ 
		    	  if(j ==cc )
		    		   {
			    		  	document.getElementById('showTab'+j).style.display='';
			    		  	if(document.getElementById('editBtn'+j).name == "show")
			    		  		{
			    		  			document.getElementById('dosform'+j).style.display='';
			    		  		}
			    		  	document.getElementById('editBtn'+j).style.display='';
			    		  	document.getElementById('cancelBtn'+j).style.display='';
			    		  	document.getElementById('tabItema'+j).className='tabItemCurrent';
			    		  	showingTabItem = cc;
		    		   }
		    	  else
		    		    {
		    		  		document.getElementById('showTab'+j).style.display='none';
			    		  	document.getElementById('dosform'+j).style.display='none';
			    		  	document.getElementById('editBtn'+j).style.display='none';
				    		document.getElementById('cancelBtn'+j).style.display='none';
			    		  	document.getElementById('tabItema'+j).className='';
		    		    }
		    	}
			 }
		 }
}
function pingVerify(taskOrder){
	
	//开启加载页面
	document.getElementById("cover").style.display = "block";
    document.getElementById("layout").style.display = "block";
	
	messageJson.type = "pingVerify";
	messageJson.expTaskOrder = taskOrder;
    var mess = JSON.stringify(messageJson);
    websocket.send(mess);
	
}

//按照任务号保存配置文件
function saveConfigureFile(taskOrder){
	
	//开启加载页面
	document.getElementById("cover").style.display = "block";
    document.getElementById("layout").style.display = "block";
	
	messageJson.type = "saveConfigureFile";
	messageJson.expTaskOrder = taskOrder;
    var mess = JSON.stringify(messageJson);
    websocket.send(mess);
}

//动态生成拓扑及配置操作按钮（按任务分类）
function topoAndConfigureButton(){
	
	var expId = messageJson.expId;
	var taskNum = parseInt(messageJson.expTaskNum);

	//按照任务个数动态生成按钮,循环中taskNum加1是因为任务0不在任务表中
	for(var i=0;i<taskNum+1;i++){
		//拓扑
		var buttonParent1 = document.getElementById("TopoSaveButton");
		var newTopoButton = document.createElement('button');
		newTopoButton.setAttribute("id", "topoSave");
		//这里复用了editBtn的样式，以后可修改
		newTopoButton.setAttribute("class", "editBtn");
		newTopoButton.setAttribute("onclick", "topo.saveToDatabase(" + i + ")");
		
		if(i==0)
			newTopoButton.innerHTML = "保存初始拓扑";
		else
			newTopoButton.innerHTML = "保存拓扑为任务"+i;
	
		buttonParent1.appendChild(newTopoButton);
		
		//配置
		var buttonParent2 = document.getElementById("ConfigureSaveButton");
		var newConfigureButton = document.createElement('button');
		newConfigureButton.setAttribute("id", "configureSave");
		//这里复用了editBtn的样式，以后可修改
		newConfigureButton.setAttribute("class", "editBtn");
		newConfigureButton.setAttribute("onclick", "saveConfigureFile(" + i + ")");
		
		if(i==0)
			newConfigureButton.innerHTML = "保存初始配置";
		else
			newConfigureButton.innerHTML = "保存配置为任务"+i;
	
		buttonParent2.appendChild(newConfigureButton);
		
		//ping验证
		var buttonParent3 = document.getElementById("pingVerifyButton");
		var newPingVerifyButton = document.createElement('button');
		newPingVerifyButton.setAttribute("id", "pingVerify");
		//这里复用了editBtn的样式，以后可修改
		newPingVerifyButton.setAttribute("class", "editBtn");
		newPingVerifyButton.setAttribute("onclick", "pingVerify(" + i + ")");
		
		if(i==0)
			newPingVerifyButton.innerHTML = "验证初始状态ping";
		else
			newPingVerifyButton.innerHTML = "验证任务"+i+"ping";

		buttonParent3.appendChild(newPingVerifyButton);
	}
	
	
}
