package virnet.management.log.action;

import java.util.Map;

import virnet.management.log.service.LogService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LogAction  extends ActionSupport{
	public int state;
	public String statement;
	
	private static final long serialVersionUID = -4256483977507568916L;
	
	private LogService logservice = new LogService();
	
	private String user_id = new String();
	private String password = new String();
	
	private Map<String, Object> session; 
	
	public String login(){
		String user_id = this.getUser_id();
		String password = this.getPassword();
		System.out.println("this is login action");
		
		LogEntity data = this.logservice.login(user_id, password);
		
		this.setSession();
		this.session.put("state", (Object)data.getState());
		this.session.put("statement", data.getStatement());
		
		if(data.getState() != 0){
			return ERROR;
		}
		
		this.session.put("cplist", data.getCharacterPowerlist());
		return SUCCESS;
	}
	
	public LogService getLogService(){
		return this.logservice;
	}
	
	public void setLogService(LogService log){
		this.logservice = log;
	}
	
	public String getUser_id() {
		 return this.user_id;
	}
	 
	 public void setUser_id(String user_id) {
	     this.user_id = user_id;
	 }
	 
	 public String getPassword() {
		 return password;
	 }
	 
	 public void setPassword(String password) {
		 this.password = password;
	 }

	 public void setSession(){
		 this.session = ActionContext.getContext().getSession();
	 }
}
