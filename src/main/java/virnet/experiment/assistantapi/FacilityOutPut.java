package virnet.experiment.assistantapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.sf.json.JSONObject;

public class FacilityOutPut extends Thread {
	public volatile boolean stop = false;
	InputStream isFromFacility = null;	
	String feedbackFromFacility = null;
	WebSocketSession wss = null;
	JSONObject jsonString = null;
	ArrayList<WebSocketSession> expUsers;
	ConcurrentHashMap<WebSocketSession, String> userMap;
	
	public FacilityOutPut(InputStream isFromFacility,WebSocketSession wss, JSONObject jsonString, 
			ArrayList<WebSocketSession> expUsers, ConcurrentHashMap<WebSocketSession, String> userMap) {
		this.isFromFacility = isFromFacility;
		this.wss =  wss;
		this.jsonString = jsonString;
		this.expUsers = expUsers;
		this.userMap = userMap;
	}
	@Override
	public void run() {
		while(!stop){
            int count = 0;
        	while (count == 0&&(!stop)) {
        		try {
					count = isFromFacility.available();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stop = true;
					break;
				}
        	}
        	if(stop)
    			break;
        	//System.out.println(count);
        	byte[] buffer=new byte[count];
        	int readCount = 0; // 
        	while (readCount < count) {
        		try {
					readCount += isFromFacility.read(buffer, readCount, count - readCount);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					stop = true;
					break;
				}
        	}
        	feedbackFromFacility = new String(buffer);
        	System.out.print(feedbackFromFacility);
        	jsonString.put("type", "command");
        	jsonString.put("content", feedbackFromFacility);
        	String mess = jsonString.toString();
			String groupid = userMap.get(wss);
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
	}
	public void stopThread(){
		this.stop = true;
	}
	public String returnMessage(String message){
		
		return message;
		
	}
}
