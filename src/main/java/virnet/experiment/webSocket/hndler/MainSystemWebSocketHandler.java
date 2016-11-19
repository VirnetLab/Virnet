/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virnet.experiment.webSocket.hndler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net.sf.json.JSONObject;
import virnet.experiment.assistantapi.FacilityOutPut;
import virnet.experiment.operationapi.FacilityConfigure;
import virnet.experiment.operationapi.NTCEdit;
import virnet.experiment.operationapi.PCExecute;
import virnet.experiment.resourceapi.ResourceAllocate;
import virnet.experiment.resourceapi.ResourceRelease;

//import virnet.assistantapi.ExperimentInit;
//import virnet.resourceapi.ResourceAllocate;
@Component
public class MainSystemWebSocketHandler extends TextWebSocketHandler implements WebSocketHandler {
	
	private static final ArrayList<WebSocketSession> arrangeUsers;
    static {
    	arrangeUsers = new ArrayList<>();
    }
    
    private static final ArrayList<WebSocketSession> expUsers;
    static {
    	expUsers = new ArrayList<>();
    }
    
    private static final ArrayList<String> groupExisted = new ArrayList<>();
    
    //静态变量，用来记录新产生的分组数量
    public static int newGroupNum = 0;
    
    //用来标记实验资源分配的次数
    public static int cc = 0;
	
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private WebSocketSession session;
    
    //该实验需要的设备列表
    private static String equip = "SW2##PC##PC##PC##PC";

    //记录实验主界面用户名和用户组的session，用来传递用户名
  	private static ConcurrentHashMap<WebSocketSession, String> arrangeUserName = new ConcurrentHashMap<WebSocketSession, String>();
    
	//记录排队时用户和用户组的map，用来分配不同的实验组，线程安全
	private static ConcurrentHashMap<WebSocketSession, String> userGroupMap = new ConcurrentHashMap<WebSocketSession, String>();  
	
	//记录管理界面进入时用户和用户组的map，线程安全
	private static ConcurrentHashMap<WebSocketSession, String> userGroupMapPro = new ConcurrentHashMap<WebSocketSession, String>();
	
	//记录实验主界面中的用户和用户组的map,用与组内通讯
	private static ConcurrentHashMap<WebSocketSession, String> userMap = new ConcurrentHashMap<WebSocketSession, String>(); 
	
	//存储用户组（group）和实验机柜编号的map
	private static ConcurrentHashMap<String, String> MapEquipment = new ConcurrentHashMap<String, String>();	
	
	//存储用户组（group）和FacilityConfigure的map
	private static ConcurrentHashMap<String, FacilityConfigure> groupFacilityConfigureMap = new ConcurrentHashMap<String, FacilityConfigure>();
	
	//存储用户组（group）和FacilityConfigure的map
	private static ConcurrentHashMap<String, FacilityOutPut> groupFacilityOutPut = new ConcurrentHashMap<String, FacilityOutPut>();
	
	//存储用户组（group）和FacilityConfigure的map
	private static ConcurrentHashMap<String, PCExecute> groupPcConfigureMap = new ConcurrentHashMap<String, PCExecute>();
	
	
	
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......");
        
   
        //从web socket session中取回用户名和组别
        String userName = (String) session.getAttributes().get("WS_USER_Name");
        String workgroup = (String) session.getAttributes().get("WS_USER_WorkGroup");
        String pageType = (String) session.getAttributes().get("WS_USER_pageType");
       
        if(pageType.equals("experiment"))
        {
        	expUsers.add(session);
        	userMap.put(session,workgroup);  //将该用户的分组信息加入分组map中
        //	userNameMap.put(session, userName);
        }
        if(pageType.equals("arrange"))
        {
        	userGroupMapPro.put(session,workgroup);
        	arrangeUsers.add(session);
        	arrangeUserName.put(session, userName);
        }
        this.session =  session;     
        //webSocketMap.put(this.session,this);     //将该用户的连接信息加入map中
		//userGroupMap.put(this.session,workgroup);  //将该用户的分组信息加入分组map中
        //session.sendMessage(new TextMessage("Server:connected OK!"));
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {      
    	String message = wsm.getPayload().toString();
    	System.out.println("message:" + message);
    	//json 解析
    	JSONObject jsonString = JSONObject.fromObject(message);

    	//判断是否属于排队页面通信
    	if(jsonString.getString("flag").equals("arrange"))
    	{
    		String arrangeStatus  = jsonString.getString("arrangeStatus");
    		
        	if(arrangeStatus.equals("true"))
        	{
        		userGroupMap.put(wss, userGroupMapPro.get(wss));  //将该用户的分组信息加入分组map中
        		queueingLogic(2,jsonString);
        	  //endPointQueueingLogic(4,3,jsonString)
        	}
    	}
    	//实验界面综合信息处理域
    	if(jsonString.getString("flag").equals("experiment"))
    	{
    		//进入实验界面请求设备信息生成域
        	if(jsonString.getString("type").equals("requestEquipment"))
        	{
        		jsonString.put("equipmentNumber","5");
        		jsonString.put("equipmentName", equip);
        		String mess = jsonString.toString();
        		wss.sendMessage(new TextMessage(mess));
        	}
        	
        	//实验开始初始化资源域
        	if(jsonString.getString("experimentStatus").equals("start"))
        	{	
        		cc++;
        		if(cc==1){
        		/*资源分配*/
        		String cabinet_num="";
        		long start = System.currentTimeMillis();
        		String name_Str = "SW2##PC##PC##PC##PC";	//设备名串，“##”隔开，排列顺序即为设备在实验机柜中的序号(RT##SW2##SW2#SW3#PC##PC)
        		String duration = "90";	//该实验最长持续时间(90)
        		ResourceAllocate resourceAllocate = new ResourceAllocate(name_Str, duration);
        		if(resourceAllocate.allocate()){
        			cabinet_num = resourceAllocate.getCabinetNum();	//实验机柜编号
        			String num_str = resourceAllocate.getNumStr();	//设备序号串(1##3##5##4##2)
        			String port_str = resourceAllocate.getPortInfoStr();//设备序号串对应下的各设备可用端口号串(1@2@3@4@5@6##1@2@3@4@6##1##1@2@3@4@5@6##1@2@3@4@5@6)
        			//experimentInit.setCabinet_num(cabinet_num);	//将实验机柜编号暂时保存*/
        			
        			//将参数传递到前端
        			jsonString.put("type", "sendEquipment");   
        			jsonString.put("equipmentName",name_Str);
        			jsonString.put("equipmentNumStr", num_str);
        			jsonString.put("equipmentPortStr", port_str);        			
        			sendToGroup(wss,jsonString);            	
        			//System.out.println(cabinet_num);
        			//System.out.println(num_str);
        			//System.out.println(port_str);
        		}
        		else{
        			System.out.println("false!");
        			System.out.println(resourceAllocate.getReturnDetail());
        		}
        		long end = System.currentTimeMillis();
        		System.out.println("资源分配用时："+(end-start)+"ms");
        		
        		//存储用户组和实验机柜编号的map
        		MapEquipment.put(userMap.get(wss), cabinet_num); 
        		
        		jsonString.put("experimentStatus","");
        	}
        	}
        	
        	//Jtopu提交后，拓扑连接域
        	if(jsonString.getString("type").equals("topoedit")){
        		
        		JSONObject ss = jsonString;
        		
        		long start = System.currentTimeMillis(); 	
        		String cabinet_NUM = MapEquipment.get(userMap.get(wss));		//实验机柜编号       
        		String leftNUM_Str = jsonString.getString("leftNUM_Str");		//左端设备序号串，“##”隔开
        		String rightNUM_Str = jsonString.getString("rightNUM_Str");	//右端设备序号串，“##”隔开
        		String leftport_Str = jsonString.getString("leftport_Str");	//左端设备端口序号串，“##”隔开
        		String rightport_Str = jsonString.getString("rightport_Str");	//右端设备端口序号串，“##”隔开
        		System.out.println("leftNUM_Str:"+ leftNUM_Str+"----rightNUM_Str:"+ rightNUM_Str +"----leftport_Str:"+leftport_Str+"----rightport_Str:"+rightport_Str);
        		NTCEdit ntcEdit = new NTCEdit(cabinet_NUM,leftNUM_Str,rightNUM_Str,leftport_Str,rightport_Str);
        		if(ntcEdit.edit()){
        			String connection_str = leftNUM_Str+"%%"+rightNUM_Str+"%%"+leftNUM_Str+"%%"+rightport_Str;
        			System.out.println(connection_str);
            		jsonString.put("success", true);
        		}
        		else{
        			jsonString.put("success", false);
        			System.out.println("false!");
        			System.out.println(ntcEdit.getReturnDetail());
        		}
        		long end = System.currentTimeMillis();
        		System.out.println("拓扑连接用时："+(end-start)+"ms");
        		String mess1 = jsonString.toString();
        		wss.sendMessage(new TextMessage(mess1));
        		
        		ss.put("type", "equipConnectionInfo");
        		String mess2 = ss.toString();
        		String groupid = userMap.get(wss);
        		for (WebSocketSession user : expUsers) {
                    try {
                        if (user.isOpen() && (userMap.get(user).equals(groupid)) && (!user.equals(wss)) ) {
                        	System.out.println(user.equals(wss));
                            user.sendMessage(new TextMessage(mess2));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }     
        		
        		
        		
        		
        	}
        	
        	//输入的设备命令和输出设备结果处理域
        	if(jsonString.getString("type").equals("command"))
        	{
        		JSONObject ss = jsonString;
        		String equipmentNumber = jsonString.getString("inputEquipmentNumber");
        		String commandDetail = jsonString.getString("content");
        		String[] sourceStrArray = equip.split("##");
        		if(sourceStrArray[Integer.parseInt(jsonString.getString("inputEquipmentNumber"))].equals("PC"))
        		{
        			
        			String groupid = userMap.get(wss);
        			ss.put("content", "Administrator:/>" + commandDetail + "\n");
            		String mess1 = ss.toString();
        			for (WebSocketSession user : expUsers) {
                        try {
                            if (user.isOpen()&&(userMap.get(user).equals(groupid))) {
                                user.sendMessage(new TextMessage(mess1));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
        			pcCommandConfigure(equipmentNumber,commandDetail,wss);
        		}
        		else
        		{
        			FacilityCommandConfigure(equipmentNumber,commandDetail,wss);
        		}      		
        	}
        	
        	//聊天信息处理域
        	if(jsonString.getString("type").equals("communication"))
        	{
        		String groupid = userMap.get(wss);
        		String mess = jsonString.toString();        		
        		for (WebSocketSession user : expUsers) {
                    try {
                        if (user.isOpen()&&(userMap.get(user).equals(groupid))) {
                            user.sendMessage(new TextMessage(mess));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        	}
        	
        	//加锁域
        	if(jsonString.getString("type").equals("lock"))
        	{       
        		String[] sourceStrArray = equip.split("##");
        		if(sourceStrArray[Integer.parseInt(jsonString.getString("inputEquipmentNumber"))].equals("PC"))
        		{
        			if(jsonString.getString("lock").equals("lock"))	
		        	{
        				pcInitial(jsonString.getString("inputEquipmentNumber"), wss,jsonString);
		        	}
        			if(jsonString.getString("lock").equals("unlock"))
	    			{
    					pcCancel(jsonString.getString("inputEquipmentNumber"), wss, jsonString);
	    			}
        		}
        		else{
	        		if(jsonString.getString("lock").equals("lock"))	
			        	{
	        				FacilityInitial(jsonString.getString("inputEquipmentNumber"), wss,jsonString);
			        	}
	    			if(jsonString.getString("lock").equals("unlock"))
		    			{
	    					FacilityCancel(jsonString.getString("inputEquipmentNumber"), wss, jsonString);
		    			}
        		}
    			String groupid = userMap.get(wss);
        		String mess = jsonString.toString();
        		for (WebSocketSession user : expUsers) {
                    try {
                        if (user.isOpen()&&(userMap.get(user).equals(groupid)&&(!user.equals(wss)))) {
                            user.sendMessage(new TextMessage(mess));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }        		
        	}
        	//释放资源域
        	if(jsonString.getString("type").equals("release"))
        	{
        		releaseEquipment(wss,jsonString);	
        	}
    	}
    	
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        if(wss.isOpen()){
        	if(expUsers.contains(wss))
        	expUsers.remove(wss);
        	else
        	arrangeUsers.remove(wss);
            wss.close();
        }
       System.out.println("websocket connection closed......ERROR");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
    	if(expUsers.contains(wss))
        	expUsers.remove(wss);
    	else
    		arrangeUsers.remove(wss);
        wss.close();
        System.out.println("websocket connection closed......CLOSE");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    
    /**
     * 以下是自定义方法部分
     */
    public void sendMessageo(String message) throws IOException{
    	this.session.sendMessage(new TextMessage(message));
    }
 
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
    	MainSystemWebSocketHandler.onlineCount++;
    }
     
    public static synchronized void subOnlineCount() {
    	MainSystemWebSocketHandler.onlineCount--;
    }
    
  //分组逻辑    
    public static synchronized void queueingLogic(int minNumber,JSONObject jsonString) throws IOException{
    	 System.out.println("enter the method!");
        	 for(WebSocketSession tmp:arrangeUsers)
        	 {        
    	    		 String group = userGroupMap.get(tmp);
    	    		 int number = 0;
    	    		 if(!groupExisted.contains(group))
    	    		 {   
    		    		 for (ConcurrentHashMap.Entry<WebSocketSession, String> entry : userGroupMap.entrySet()) 
    		    		 {
    			      		   if(entry.getValue().equals(group))
    			      		   {
    			      			 number++;
    			      		   }
    		    		 }
    		    		 if(number >= minNumber)
    		    		 {
    		    			 for (ConcurrentHashMap.Entry<WebSocketSession, String> entry : userGroupMap.entrySet()) 
    		        		 { 
    		    	      		   if(entry.getValue().equals(group))
    		    	      		   {
    		    	      			 System.out.println("ready to send");
    		    	      			 sendStatausToGroup(jsonString,entry.getKey(),group);
    		    	      			 userGroupMap.remove(entry.getKey());
    		    	      		   }
    		        		 }
    		    			 groupExisted.add(group);
    		    		 }
    	    		 }
    	    		 else
    	    		 {   
    	    			 if(userGroupMap.remove(tmp)!=null)
    	    			 {
    	    				 sendStatausToGroup(jsonString,tmp,group);
    	    			 }
    	    		 }
        	 }	
    }
  //到达实验开始点后的分组逻辑
    public static void endPointQueueingLogic(int standardNumber,int minNumber,JSONObject jsonString) throws IOException{
    	for(WebSocketSession tmp:arrangeUsers)
    	 {   
    		 String group = userGroupMap.get(tmp);
    	
    		 if(groupExisted.contains(group))
    		 {
    			 if(userGroupMap.remove(tmp)!=null)
    			 {
    				 sendStatausToGroup(jsonString,tmp,group);
    			 }
    		 }
    		 else
    		 { 
    			 //大于标准人数时确定分组
    			 if(userGroupMap.size() >= standardNumber)
    			 {
    				 int number = userGroupMap.size() / standardNumber;
    				 int i = 0;
    				 for (ConcurrentHashMap.Entry<WebSocketSession, String> entry : userGroupMap.entrySet()) 
    	    		 {		 					
    					 	 i++;
    						 if(i <= number*standardNumber);	
    						 {
    							 int record =((i-1) / standardNumber) + 1;
    							 sendStatausToGroup(jsonString,entry.getKey(),"new" + record);
    							 userGroupMap.remove(entry.getKey());
    							 newGroupNum = record;
    						 }
    	    		 }
    			 }
    			 //大于最小人数小于标准人数时确定分组
    			 else if (userGroupMap.size() >= minNumber)
    			 {
    				 int number = userGroupMap.size() / minNumber;
    				 int i = 0;
    				 for (ConcurrentHashMap.Entry<WebSocketSession, String> entry : userGroupMap.entrySet()) 
    	    		 {		 
    				 	 i++;
    					 if(i <= number*minNumber);	
    					 {
    						 int record = newGroupNum + ((i-1) / standardNumber) + 1;
    						 sendStatausToGroup(jsonString,entry.getKey(),"new" + record);
    						 userGroupMap.remove(entry.getKey());
    					 }
    	    		 }
    			 }
    		 }
    	 }	
    }
  //给默认用户组的的用户发送状态消息
    public static synchronized void sendStatausToGroup(JSONObject jsonString, WebSocketSession webSS,String finalGroup) throws IOException
    {		System.out.println("send");
    		jsonString.put("ready","true");
    		System.out.println(jsonString.getString("ready"));
    		jsonString.put("finalGroup",finalGroup);
    		System.out.println(jsonString.getString("finalGroup"));
    		System.out.println("before jump:");
    		jsonString.put("userName", arrangeUserName.get(webSS));
    		String mess = jsonString.toString();
    		webSS.sendMessage(new TextMessage(mess));
     }
    
    public void job1() {  
        System.out.println("loading");  
    }  
    
    public void sendToGroup(WebSocketSession wss, JSONObject jsonString){
    	
    	String groupid = userMap.get(wss);
		String mess = jsonString.toString();
		System.out.println(groupid);
		
		for (WebSocketSession user : expUsers) {
            try {
                if (user.isOpen()&&(userMap.get(user).equals(groupid))) {
                    user.sendMessage(new TextMessage(mess));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	
    }
    
//初始化设备连接
public void FacilityInitial(String equipmentNumber, WebSocketSession wss,JSONObject jsonString){
	String cabinet_NUM = MapEquipment.get(userMap.get(wss));		//实验机柜编号
	String facility_NUM = (Integer.parseInt(equipmentNumber) + 1)+"";
	System.out.println("cabinet_NUM:" + cabinet_NUM);
	System.out.println("facility_NUM:" + facility_NUM);
	FacilityConfigure facilityConfigure = new FacilityConfigure(cabinet_NUM,facility_NUM);
	if(facilityConfigure.connect())
	{
		
		FacilityOutPut facilityOutPutThread = new FacilityOutPut(facilityConfigure.getInputStream(),wss,jsonString,expUsers,userMap);
		facilityOutPutThread.start();
		groupFacilityConfigureMap.put(userMap.get(wss) + equipmentNumber, facilityConfigure);
		groupFacilityOutPut.put(userMap.get(wss) + equipmentNumber, facilityOutPutThread);
		
	}
	else{
		System.out.println("false!");
		System.out.println(facilityConfigure.getReturnDetail());
	}
}     

//注销设备连接
public void FacilityCancel(String equipmentNumber, WebSocketSession wss,JSONObject jsonString){
	FacilityConfigure facilityConfigure = groupFacilityConfigureMap.get(userMap.get(wss) + equipmentNumber);
 	FacilityOutPut facilityOutPutThread = groupFacilityOutPut.get(userMap.get(wss) + equipmentNumber);
 	facilityOutPutThread.stopThread();
	System.out.println("结束配置");
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	facilityConfigure.end();
	groupFacilityConfigureMap.remove(userMap.get(wss) + equipmentNumber);
	groupFacilityOutPut.remove(userMap.get(wss) + equipmentNumber);
}     
 
//PC连接
public void pcInitial(String equipmentNumber, WebSocketSession wss,JSONObject jsonString){
	String cabinet_NUM = MapEquipment.get(userMap.get(wss));		//实验机柜编号
	String facility_NUM = (Integer.parseInt(equipmentNumber) + 1)+"";
	PCExecute pcExecute = new PCExecute(cabinet_NUM,facility_NUM);
	if(pcExecute.connect())
	{
		
		FacilityOutPut facilityOutPutThread = new FacilityOutPut(pcExecute.getInputStream(),wss,jsonString,expUsers,userMap);
		facilityOutPutThread.start();
		groupPcConfigureMap.put(userMap.get(wss) + equipmentNumber, pcExecute);
		groupFacilityOutPut.put(userMap.get(wss) + equipmentNumber, facilityOutPutThread);
		
	}
	else{
		System.out.println("false!");
		System.out.println(pcExecute.getReturnDetail());
	}
}

public void pcCancel(String equipmentNumber, WebSocketSession wss,JSONObject jsonString){
	PCExecute pcExecute = groupPcConfigureMap.get(userMap.get(wss) + equipmentNumber);
 	FacilityOutPut facilityOutPutThread = groupFacilityOutPut.get(userMap.get(wss) + equipmentNumber);
 	facilityOutPutThread.stopThread();
	System.out.println("结束配置");
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	pcExecute.end();
	groupFacilityConfigureMap.remove(userMap.get(wss) + equipmentNumber);
	groupPcConfigureMap.remove(userMap.get(wss) + equipmentNumber);
}     
 

    
  //与第一层进行设备交互，输入命令，返回输出命令
    public void FacilityCommandConfigure(String equipmentNumber, String commandDetail,WebSocketSession wss)
    {
    	 	FacilityConfigure facilityConfigure = groupFacilityConfigureMap.get(userMap.get(wss) + equipmentNumber);
			if(commandDetail.equals("end")) {
			}
			if(commandDetail.equals("")) {
				commandDetail = "NEWLINE";
			}
			facilityConfigure.configure(commandDetail);		
	}
    
  //与第一层进行PC交互，输入命令，返回输出命令
    public void pcCommandConfigure(String equipmentNumber, String commandDetail,WebSocketSession wss)
    {
    		PCExecute pcExecute = groupPcConfigureMap.get(userMap.get(wss) + equipmentNumber);		
	    	if(commandDetail.equals("end")) {
			}
			if(commandDetail.equals("")) {
				commandDetail = "NEWLINE";
			}
			pcExecute.execute(commandDetail);
    }
   //释放机柜资源
    public void releaseEquipment(WebSocketSession wss,JSONObject jsonString){
		long start = System.currentTimeMillis();
		String cabinet_NUM = MapEquipment.get(userMap.get(wss));		//实验机柜编号
		ResourceRelease resourceRelease = new ResourceRelease(cabinet_NUM);
		if(resourceRelease.release()){
			System.out.println("成功释放资源");
    		jsonString.put("success",true);
		}
		else{
			System.out.println("false!");
			System.out.println(resourceRelease.getReturnDetail());
			jsonString.put("success",false);
		}
		long end = System.currentTimeMillis();
		System.out.println("资源释放用时："+(end-start)+"ms");
		String mess = jsonString.toString();
		try {
			wss.sendMessage(new TextMessage(mess));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
