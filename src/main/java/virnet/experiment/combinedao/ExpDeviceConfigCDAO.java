package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ExpDeviceConfigDAO;
import virnet.experiment.entity.ExpDeviceConfig;

public class ExpDeviceConfigCDAO {
	
	private ExpDeviceConfigDAO dcDAO = new ExpDeviceConfigDAO();
	
	public void delete(Integer expConfigId){
		
		//删除原记录,如果原来没有记录，则list长度为0
		@SuppressWarnings("unchecked")
		List<ExpDeviceConfig> dclist = this.dcDAO.getListByProperty("expConfigId", expConfigId);
		int i=0;
		while(i!=dclist.size()){
			this.dcDAO.delete(dclist.get(i));
			i++;
		}
	}
	
	//逐个设备重写
	public boolean edit(Integer expConfigId,Integer deviceOrder,String configFile){
		
		ExpDeviceConfig newDeviceConfig = new ExpDeviceConfig();
		newDeviceConfig.setExpConfigId(expConfigId);
		newDeviceConfig.setExpDeviceOrder(deviceOrder);
		newDeviceConfig.setConfigFile(configFile);
		
		if(this.dcDAO.add(newDeviceConfig))
		    return true;
		else
			return false;
	}
		
}
