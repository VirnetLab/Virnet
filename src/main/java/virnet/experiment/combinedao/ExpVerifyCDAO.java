package virnet.experiment.combinedao;

import virnet.experiment.dao.ExpVerifyDAO;
import virnet.experiment.entity.ExpVerify;

public class ExpVerifyCDAO {
	
	private ExpVerifyDAO vDAO = new ExpVerifyDAO();
	
	//返回实验模板验证Id
	public Integer edit(String expId, String expTaskOrder){    
		
		String para[]={"expId",expId,"expTaskOrder",expTaskOrder};
		ExpVerify verify = (ExpVerify)this.vDAO.getByNProperty(para);
		
		//找不到该实验模板验证，即未设置过该实验模板对应任务的验证信息，任务号为0指的是初始验证
		if(verify==null){
			
			ExpVerify newverify = new ExpVerify();
			newverify.setExpTaskOrder(Integer.parseInt(expTaskOrder));
			newverify.setExpId(Integer.parseInt(expId));
			
			//未确定验证类型
			newverify.setVerifyType(1);
			
			if(this.vDAO.add(newverify)){
				return newverify.getExpVerifyId();
			}
			else
				return 0;
		}
		else{             //已经存在该实验模板对应的任务的验证信息,则修改
			
			verify.setExpTaskOrder(Integer.parseInt(expTaskOrder));
			verify.setExpId(Integer.parseInt(expId));
			verify.setVerifyType(null);
			
			if(this.vDAO.update(verify)){
				return verify.getExpVerifyId();
			}
			else
				return 0;
		}
	}
}
