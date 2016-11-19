package virnet.management.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import javax.servlet.http.HttpServletResponse;  

import org.apache.struts2.ServletActionContext;  

public class UserCheck extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3116891264112116606L;

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		// TODO Auto-generated method stub
		//取得请求的URL  
        String url = ServletActionContext.getRequest().getRequestURL().toString();  
        HttpServletResponse response=ServletActionContext.getResponse();  
        response.setHeader("Pragma","No-cache");            
        response.setHeader("Cache-Control","no-cache");     
        response.setHeader("Cache-Control", "no-store");     
        response.setDateHeader("Expires",0);  

        String user = new String();
        
        System.out.println("interceptor : " + user); 
        
        //对登录与注销请求直接放行,不予拦截  
        if (url.indexOf("login.action")!=-1 || url.indexOf("logout.action")!=-1){  
            return arg0.invoke();  
        }  
        else{  
            //验证Session是否过期  
            if(!ServletActionContext.getRequest().isRequestedSessionIdValid()){  
                //session过期,转向session过期提示页,最终跳转至登录页面  
                return "login";  
            }  
            else{  
                user = (String)ServletActionContext.getRequest().getSession().getAttribute("User");  
                //验证是否已经登录  
                if (user==null){  
                    //尚未登录,跳转至登录页面  
                    return "login";  
                }else{                      
                    return arg0.invoke();  
                                  
                }                  
            }              
        }
	}

}
