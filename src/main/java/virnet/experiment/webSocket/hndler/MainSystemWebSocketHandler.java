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
import virnet.experiment.assistantapi.ExperimentSave;
import virnet.experiment.assistantapi.FacilityOutPut;
import virnet.experiment.assistantapi.PCConfigureInfo;
import virnet.experiment.assistantapi.PingVerify;
import virnet.experiment.operationapi.FacilityConfigure;
import virnet.experiment.operationapi.NTCEdit;
import virnet.experiment.operationapi.PCExecute;
import virnet.experiment.resourceapi.ResourceAllocate;
import virnet.experiment.resourceapi.ResourceRelease;
import virnet.management.combinedao.CabinetTempletDeviceInfoCDAO;
import virnet.management.combinedao.TaskInfoCDAO;
import virnet.experiment.combinedao.ExpConnectCDAO;
import virnet.experiment.combinedao.ExpTopoCDAO;
import virnet.experiment.combinedao.ExpTopoPositionCDAO;
import virnet.experiment.combinedao.ExpVerifyCDAO;
import virnet.experiment.combinedao.ExpVerifyPingCDAO;
import virnet.experiment.combinedao.ResultConnectCDAO;
import virnet.experiment.combinedao.ResultTaskCDAO;
import virnet.experiment.combinedao.ResultTopoCDAO;
import virnet.experiment.combinedao.ResultTopoPositionCDAO;
import virnet.experiment.combinedao.ResultVerifyCDAO;
import virnet.experiment.combinedao.ResultVerifyPingCDAO;
import virnet.experiment.dao.ExpTopoDAO;
import virnet.experiment.entity.ExpTopo;;

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
        		queueingLogic(1,jsonString);                      //至少两人
        	  //endPointQueueingLogic(4,3,jsonString)
        	}
    	}
    	//实验界面综合信息处理域
    	if(jsonString.getString("flag").equals("experiment"))
    	{
    		//进入实验界面请求设备信息生成域
        	if(jsonString.getString("type").equals("requestEquipment"))
        	{
        		//以实验Id查询设备数量
        		String expId = jsonString.getString("expId");
        		CabinetTempletDeviceInfoCDAO ctdDAO = new CabinetTempletDeviceInfoCDAO();
        		
        		//记录设备数量
        		Integer equipmentNumber = ctdDAO.equipmentNumber(expId);
    			
        		//设备名串，“##”隔开，排列顺序即为设备在实验机柜中的序号(RT##SW2##SW2#SW3#PC##PC)
    			String name_Str = ctdDAO.equipment(expId);
        		
    			//任务个数
    			TaskInfoCDAO tcDAO = new TaskInfoCDAO();
    			Integer taskNum = tcDAO.taskNum(expId);
    			
    			jsonString.put("equipmentNumber",""+equipmentNumber);
        		jsonString.put("equipmentName", name_Str);
        		jsonString.put("expTaskNum", ""+taskNum);
        		String mess = jsonString.toString();
        		wss.sendMessage(new TextMessage(mess));
        	}
        	
        	//实验开始初始化资源域
        	if(jsonString.getString("experimentStatus").equals("start"))
        	{	
        		JSONObject ss = jsonString;
        		
        		cc++;
        		if(cc==1){
        			/*资源分配*/
        			String cabinet_num="";
        			//设备名串，“##”隔开，排列顺序即为设备在实验机柜中的序号(RT##SW2##SW2#SW3#PC##PC)
        			String expId = jsonString.getString("expId");
            		CabinetTempletDeviceInfoCDAO ctdDAO = new CabinetTempletDeviceInfoCDAO();
        			String name_Str = ctdDAO.equipment(expId);
        			String duration = "90";	//该实验最长持续时间(90)
        			long start = System.currentTimeMillis();
        			ResourceAllocate resourceAllocate = new ResourceAllocate(name_Str, duration);
        			if(resourceAllocate.allocate()){
        				cabinet_num = resourceAllocate.getCabinetNum();	//实验机柜编号
        				String num_str = resourceAllocate.getNumStr();	//设备序号串
        				String port_str = resourceAllocate.getPortInfoStr();//设备序号串对应下的各设备可用端口号串(1@2@3@4@5@6##1@2@3@4@6##1##1@2@3@4@5@6##1@2@3@4@5@6)
        				//experimentInit.setCabinet_num(cabinet_num);	//将实验机柜编号暂时保存*/
        			
        				//将参数传递到前端
        				jsonString.put("type", "sendEquipment"); 
        				jsonString.put("equipmentName",name_Str);
        				jsonString.put("equipmentNumStr", num_str);
        				jsonString.put("equipmentPortStr", port_str);
        				jsonString.put("cabinet_num",cabinet_num);
        				sendToGroup(wss,jsonString);            	
        				System.out.println("机柜号"+cabinet_num);
        				System.out.println("设备序号串"+num_str);
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
        			
        			//显示初始拓扑，没有则按默认位置放置
        			
        			//画图所需要的信息
        			String leftNUM_Str = "";
        			String rightNUM_Str = "";
        			String leftport_Str = "";
        			String rightport_Str = "";
        			String position = "";
        			
   //     			String expId = jsonString.getString("expId");
        			ExpTopoDAO tDAO = new ExpTopoDAO();
        			String para[] ={"expId",expId,"expTaskOrder","0"};
        			ExpTopo topo = (ExpTopo)tDAO.getByNProperty(para);
        			//不存在初始拓扑
        			if(topo == null){
        				//各设备采用默认位置
        				ExpTopoPositionCDAO tpDAO = new ExpTopoPositionCDAO();
        				//返回各个设备的默认位置，参数为设备名串
        				position = tpDAO.defaultPosition(name_Str);
        				ss.put("position", position);
        				ss.put("type", "equipConnectionInfo");
        				String mess1 = ss.toString();
        				//发送到前端
        				wss.sendMessage(new TextMessage(mess1));
        				
        			}
        			//存在初始拓扑
        			else{
        				
        				//返回各个设备的位置，参数为实验模板拓扑Id
        				ExpTopoPositionCDAO tpDAO = new ExpTopoPositionCDAO();
        				position = tpDAO.position(topo.getExpTopoId());
        				
        				ExpConnectCDAO  ctDAO = new ExpConnectCDAO();
        				String connectInfo = ctDAO.connectInfo(topo.getExpTopoId());
        				
        				String connect[] = connectInfo.split(",");
        				//返回左端设备串
        				leftNUM_Str = connect[0];
        				//返回右端设备串
        				rightNUM_Str = connect[1];
        				//返回左端设备端口
        				leftport_Str = connect[2];
        				//返回右端设备端口
        				rightport_Str = connect[3];
        				
        				ss.put("position", position);
        				ss.put("leftNUM_Str", leftNUM_Str);
        				ss.put("rightNUM_Str", rightNUM_Str);
        				ss.put("leftport_Str", leftport_Str);
        				ss.put("rightport_Str", rightport_Str);
        			
        				ss.put("type", "equipConnectionInfo");
        				String mess1 = ss.toString();
        				wss.sendMessage(new TextMessage(mess1));
        			}
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
        		//保存为任务标准拓扑，任务号为0时为初始拓扑
            	if(jsonString.getString("type").equals("topoSaveToDatabase")){
            		    
            		String leftNUM_Str = jsonString.getString("leftNUM_Str");		//左端设备序号串，“##”隔开
            		String rightNUM_Str = jsonString.getString("rightNUM_Str");	    //右端设备序号串，“##”隔开
            		String leftport_Str = jsonString.getString("leftport_Str");	    //左端设备端口序号串，“##”隔开
            		String rightport_Str = jsonString.getString("rightport_Str");	//右端设备端口序号串，“##”隔开
            		String position = jsonString.getString("position");				//设备的位置 设备的X，Y坐标以空格相隔，设备间以逗号相隔
            		String expId = jsonString.getString("expId");
            		String expTaskOrder = jsonString.getString("expTaskOrder");  
            		String expCaseId = jsonString.getString("expCaseId");
            		String expRole = jsonString.getString("expRole");
            		
            		//学生实验保存到结果表
            		if(expRole.equals("stu")){ 
            			
            			//修改实验结果拓扑表
            			ResultTaskCDAO taskcDAO = new ResultTaskCDAO();
            			Integer resultTaskId = taskcDAO.getResultTaskId(expCaseId, expId, expTaskOrder);
            			
                		if(resultTaskId!=0){
                			ResultTopoCDAO rtcDAO = new ResultTopoCDAO();
                    		Integer resultTopoId = rtcDAO.edit(resultTaskId,leftNUM_Str);
                    		
                    		if(resultTopoId!=0){
                    		
                    			//修改实验结果连接表
                    			ResultConnectCDAO rcCDAO = new ResultConnectCDAO();
                    			boolean connectSuccess = rcCDAO.edit(resultTopoId, leftNUM_Str, rightNUM_Str, leftport_Str, rightport_Str);
                    			
                    			//修改实验结果位置表
                    			ResultTopoPositionCDAO rtpcDAO = new ResultTopoPositionCDAO();
                    			boolean positionSuccess = rtpcDAO.edit(resultTopoId, position);
                    			
                    			if(connectSuccess&&positionSuccess)
                    				jsonString.put("success", true);
                    			else
                    				jsonString.put("success", false);
                    		}
                    		else
                    			jsonString.put("success", false);
                		}
                		else
                			jsonString.put("success", false);
            		}	
            		
            		//管理员保存到模板表
            		else if(expRole.equals("GM")){
            			
            			//修改实验模板拓扑表
                		ExpTopoCDAO tcDAO = new ExpTopoCDAO();
                		Integer ExpTopoId = tcDAO.edit(expId,expTaskOrder,leftNUM_Str);
                		if(ExpTopoId!=0){
                			//修改实验模板连接表
                			ExpConnectCDAO cCDAO = new ExpConnectCDAO();
                			boolean connectSuccess = cCDAO.edit(ExpTopoId, leftNUM_Str, rightNUM_Str, leftport_Str, rightport_Str);
                			
                			//修改实验模板拓扑位置表
                			ExpTopoPositionCDAO tpCDAO = new ExpTopoPositionCDAO();
                			boolean positionSuccess = tpCDAO.edit(ExpTopoId,position);
                			
                			if(connectSuccess&&positionSuccess)
                				jsonString.put("success", true);
                			else
                				jsonString.put("success", false);
                		}
                		else{
                			System.out.println("false");
                			jsonString.put("success", false);
                		}	
            		}
            		else
            			jsonString.put("success", false);
            		
            		String mess = jsonString.toString();
            		//发送到前端
            		wss.sendMessage(new TextMessage(mess));
            	}
        	
        	//输入的设备命令和输出设备结果处理域
        	if(jsonString.getString("type").equals("command"))
        	{
        		JSONObject ss = jsonString;
        		String equipmentNumber = jsonString.getString("inputEquipmentNumber");
        		String commandDetail = jsonString.getString("content");
        		String[] sourceStrArray = jsonString.getString("equipmentName").split("##");
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
        		String[] sourceStrArray =  jsonString.getString("equipmentName").split("##");
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
        	//保存实验配置
        	if(jsonString.getString("type").equals("saveConfigureFile")){
        		String cabinet_num = jsonString.getString("cabinet_num");
        		String expId = jsonString.getString("expId");
        		String expTaskOrder = jsonString.getString("expTaskOrder");
        		String equipmentNumber = jsonString.getString("equipmentNumber");
        		String expRole = jsonString.getString("expRole");
        		
        		boolean success = false;
        		//保存到实验结果的表
        		if(expRole.equals("stu")){
        			
        			String expCaseId = jsonString.getString("expCaseId");
        			ResultTaskCDAO taskcDAO = new ResultTaskCDAO();
        			Integer resultTaskId = taskcDAO.getResultTaskId(expCaseId, expId, expTaskOrder);
        			
        			ExperimentSave es = new ExperimentSave(cabinet_num , expId, expTaskOrder,equipmentNumber,expRole,resultTaskId);
            		success = es.save();
        			
        		}
        		//保存到实验模板的表
        		else if(expRole.equals("GM")){
        			
        			//管理员操作不需要最后一个参数
        			ExperimentSave es = new ExperimentSave(cabinet_num , expId, expTaskOrder,equipmentNumber,expRole,0);
            		success = es.save();
        		}
        		else{
        			success = false;
        		} 		
        		
        		jsonString.put("success", success);
        		String mess = jsonString.toString();
        		//发送到前端
        		wss.sendMessage(new TextMessage(mess));	
        	}
        	//ping验证
        	if(jsonString.getString("type").equals("pingVerify")){
        		
        		boolean success = true;
        		
        		String cabinet_num = jsonString.getString("cabinet_num");
        		String expId = jsonString.getString("expId");
        		String expTaskOrder = jsonString.getString("expTaskOrder");
        		String equipmentNumber = jsonString.getString("equipmentNumber");
        		
        		String leftNUM_Str = jsonString.getString("leftNUM_Str");		//左端设备序号串，“##”隔开
        		String rightNUM_Str = jsonString.getString("rightNUM_Str");	    //右端设备序号串，“##”隔开
        		String leftport_Str = jsonString.getString("leftport_Str");	    //左端设备端口序号串，“##”隔开
        		String rightport_Str = jsonString.getString("rightport_Str");	//右端设备端口序号串，“##”隔开
        		
        		String expRole = jsonString.getString("expRole");
        		
        		//获取各网卡ip地址
            	PCConfigureInfo pcInfo = new PCConfigureInfo(cabinet_num,Integer.parseInt(equipmentNumber));
            	//获取地址
            	String[] pcip = pcInfo.getIpAddress();
            	if(pcip[0].equals("fail"))
            		//获取地址失败
            		success = false;
            	else{
            		//验证
            		PingVerify pv = new PingVerify(cabinet_num,Integer.parseInt(equipmentNumber));
            		String verifyResult[] = pv.getVerifyResult(pcip); 
            		
            		if(verifyResult[0].equals("fail"))
            			//验证失败
            			success = false;
            		else{
            			
            			Integer VerifyId = 0;
            			
            			if(expRole.equals("GM")){   //实验模板
            				//修改实验模板验证表
                    		ExpVerifyCDAO vcDAO = new ExpVerifyCDAO();
                    		VerifyId = vcDAO.edit(expId, expTaskOrder);
                    		
                    		if(VerifyId == 0)
                    			//修改验证表失败
                    			success = false;
                    		else{
                    			//删除相应的实验模板ping验证表
                    			ExpVerifyPingCDAO vpCDAO = new ExpVerifyPingCDAO();
                    			vpCDAO.delete(VerifyId);
                    			
                    			boolean write = vpCDAO.edit(VerifyId, equipmentNumber, pcip, verifyResult, 
                    										leftNUM_Str, rightNUM_Str, leftport_Str, rightport_Str);
                    			if(write)
                    			  success = true;
                    			else
                    			  success = false;
                    		}
            			}
            			
            			else if(expRole.equals("stu")){    //学生实验结果
            				
            				//获取实验结果任务ID
            				String expCaseId = jsonString.getString("expCaseId");
                			ResultTaskCDAO taskcDAO = new ResultTaskCDAO();
                			Integer resultTaskId = taskcDAO.getResultTaskId(expCaseId, expId, expTaskOrder);
            				
            				//修改实验结果验证表
                    		ResultVerifyCDAO rvcDAO = new ResultVerifyCDAO();
                    		VerifyId = rvcDAO.edit(resultTaskId);
                    		
                    		if(VerifyId == 0)
                    			//修改验证表失败
                    			success = false;
                    		else{
                    			//删除相应的实验模板ping验证表
                    			ResultVerifyPingCDAO rvpCDAO = new ResultVerifyPingCDAO();
                    			rvpCDAO.delete(VerifyId);
                    			
                    			boolean write = rvpCDAO.edit(VerifyId, equipmentNumber, pcip, verifyResult, 
                    										leftNUM_Str, rightNUM_Str, leftport_Str, rightport_Str);
                    			if(write)
                    			  success = true;
                    			else
                    			  success = false;
                    		}
            			}
            			else
            				success = false;
            			
            		}
            	}
            	jsonString.put("success", success);
				String mess = jsonString.toString();
				//发送到前端
				wss.sendMessage(new TextMessage(mess));		
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
