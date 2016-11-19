package virnet.management.logger;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class Log {
	private static Logger log = Logger.getLogger(Log.class);
	
	public void before(JoinPoint joinPoint){
		  Log.log.info("before class " + joinPoint.getClass().getName());
	}
}