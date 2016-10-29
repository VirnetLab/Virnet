package virnet.management.information.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import virnet.management.information.service.InformationService;

import com.opensymphony.xwork2.ActionSupport;

public class InformationAction extends ActionSupport implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2480610387419106153L;

	private InformationService infoService = new InformationService();
	private HttpServletRequest request;
	
	private Map<String, Object> results = new HashMap<String, Object>();
	private Map<String, Object> detail = new HashMap<String, Object>();
	private Map<String, Object> edit = new HashMap<String, Object>();
	private Map<String, Object> submit = new HashMap<String, Object>();
		private Map<String, Object> add = new HashMap<String, Object>();
	
	public String loadInfo(){
		//question remaining : user power is required or not when query object details?
		//A user with two or more role may want to see more information about the object
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String page = this.request.getParameter("page");
		String select = this.request.getParameter("select");
		
		System.out.println("user : " + user + ", id : " + id);
		
		this.results = this.infoService.loadInfo(user, id, Integer.parseInt(page), select);
		
		return SUCCESS;
	}
	
	/*
	 * 
	 * @return
	 * detail
	 */
	public String showDetail(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String key = this.request.getParameter("key");
		String name = this.request.getParameter("name");
		
		//System.out.println("！！！！！！！user : " + user + ", id : " + id + ", key : " + key + ", name : " + name);
		
		//user check
		
		//Divide by key : user, class, course, experiment, group，facilities
		
		this.setDetail(this.infoService.showDetail(user, id, key, name));
		
		return SUCCESS;
	}
	
	public String edit(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String name = this.request.getParameter("name");
		System.out.println(id);
		this.setEdit(this.infoService.Edit(user, id, name));
		
		return SUCCESS;
	}
	
	public String submit(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		String name = this.request.getParameter("name");
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, String[]> data = this.request.getParameterMap();
		
		Set<String> k = data.keySet();
		Iterator<String> i = k.iterator();
		while(i.hasNext()){
			String s = i.next();
			if(s.contains("data[")){
				if(s.contains("[]")){				
					map.put(s.substring(5, s.indexOf("[]") - 1), data.get(s));
				}
				else{
					String key = s.substring(5, s.length() - 1);
					String w=data.get(s)[0];
	//				System.out.println("key:"+key);
					map.put(key, w);
				}	
			}
		}
		
		
		Map<String, Object> returndata = new HashMap<String, Object>();
		
		Map<String, Object> key = this.infoService.submit(user, id, name, map);
		if(key != null && (boolean) key.get("isSuccess")){
			returndata.put("data", "更新成功！");
			returndata.put("key", key.get("key"));
			returndata.put("name", key.get("name"));
		}
		else{
			returndata.put("data", "更新失败");
		}
		
		this.setSubmit(returndata);
		
		return SUCCESS;
	}
	
	public String add(){
		String user = this.request.getParameter("user");
		String id = this.request.getParameter("id");
		
		this.setAdd(this.infoService.add(user, id));
		
		return SUCCESS;
	}
	
	public InformationService getInformationService(){
		return this.infoService;
	}
	
	public void setInformationService(InformationService info){
		this.infoService = info;
	}
	
	public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }
	
	public void setResults(Map<String, Object> list){
		this.results = list;
	}
	
	public Map<String, Object> getResults(){
		return this.results;
	}
	
	public void setDetail(Map<String, Object> list){
		this.detail = list;
	}
	
	public Map<String, Object> getDetail(){
		return this.detail;
	}
	
	public void setEdit(Map<String, Object> list){
		this.edit = list;
	}
	
	public Map<String, Object> getEdit(){
		return this.edit;
	}
	
	public void setSubmit(Map<String, Object> s){
		this.submit = s;
	}
	
	public Map<String, Object> getSubmit(){
		return this.submit;
	}
	
	public void setAdd(Map<String, Object> s){
		this.add = s;
	}
	
	public Map<String, Object> getAdd(){
		return this.add;
	}
}
