package virnet.experiment.assistantapi;

import java.io.IOException;
import java.io.InputStream;

import virnet.experiment.operationapi.PCExecute;
import virnet.experiment.assistantapi.PCConfigureInfo;

public class PingVerify {
	
	private String cabinet_NUM;	
    private Integer equipmentNumber;
    private Integer PCNum;
    
    public PingVerify(String cabinet_NUM,Integer equipmentNumber){
    	this.cabinet_NUM = cabinet_NUM;
    	this.equipmentNumber = equipmentNumber;	
    	this.PCNum = 4;
    }
    
    public String[] getVerifyResult(String[] pcip){
    	
    	//验证信息，首字符串为验证状态，success 或者 fail
    	String[] verifyResult = new String[PCNum*(PCNum-1)/2+1];
    	
    	//取第一个网卡为发送ping命令的载体
    	Integer PCNumber =  equipmentNumber - PCNum + 1;
    	PCExecute pcExecute = new PCExecute(cabinet_NUM,""+PCNumber);
    	
    	//这里sleep是因为可能前面的操作中一层还没有完成断开之前的PC连接，此处等待一下
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	if(pcExecute.connect()){
    		
    		System.out.println("开始验证");
    		verifyResult[0] = "success";
    		
    		//验证信息记录号
    		Integer record = 1;
    		
			InputStream isFromFacility = pcExecute.getInputStream();
    		
    		//只需要单向ping 因此一个需要ping PCNum*(PCNum-1)/2 次
        	for(int i=1 ;i < PCNum+1 ;i++)
        		for(int j=i+1; j<PCNum+1 ;j++){
    				
        			String command = "ping " + pcip[i] + " " + pcip[j];
        			System.out.println("验证命令为："+command);
    				pcExecute.execute(command);
    				
    				boolean stop = false;
    				String feedback = "";
    				while(!stop){
    					
    					//接受反馈
    					int count = 0;
    					while (count == 0) {
    						try {
    							count = isFromFacility.available();
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    							break;
    						}
    					}
    					//System.out.println(count);
    					byte[] PCbuffer=new byte[count];
    					int readCount = 0; // 
    					while (readCount < count) {
    						try {
    							readCount += isFromFacility.read(PCbuffer, readCount, count - readCount);
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    							break;
    						}
    					}
    					//获取部分反馈
    					String feedback_temp = new String(PCbuffer);
    					 
    					//完整反馈
    					feedback = feedback + feedback_temp;
    					
    					//如果反馈中出现了"统计信息"字样，说明4条来自ping的回复都已经接受到，可以跳出接受反馈的循环
    					if(feedback.indexOf("统计信息")!=-1)
    						stop = true;
    				}
    				//出现了TTL表明ping连通了
					if(feedback.indexOf("TTL=")!=-1){
						verifyResult[record] = "connected";
						System.out.println("connected");
					}
					//没有连通
					else{
						verifyResult[record] = "disconnected";
						System.out.println("disconnected");
					}					
    				
					record ++;
        		}
        	pcExecute.end();
    	}
    	else{
    		System.out.println("验证失败");
    		verifyResult[0] = "fail";
    		System.out.println(pcExecute.getReturnDetail());
    	}
    	
    	return verifyResult;
    }
}