package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.CabinetTempletDeviceDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.entity.Exp;
import virnet.management.entity.CabinetTempletDevice;

public class CabinetTempletDeviceInfoCDAO {
	
	private CabinetTempletDeviceDAO ctdDAO = new CabinetTempletDeviceDAO();
	
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> showTaskDetail(List<List<Map<String, Object>>> list,String expName,boolean isEdit){
		
		ExpDAO eDAO = new ExpDAO();
		
		List<Exp> elist = eDAO.getListByProperty("expName", expName);
		//获得实验Id,以此查询该实验模板ID
		Integer cabinetTempletId=elist.get(0).getExpCabinetTempletId();
		
		//获得该实验所需的路由器数量,以list.size记录
		String[] para1={"cabinetTempletId",""+cabinetTempletId,"deviceType","1"};
		List<CabinetTempletDevice> result1 =  this.ctdDAO.getListByNProperty(para1);
	
		List<Map<String, Object>> Rtlist = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map11 = new HashMap<String, Object>();
		map11.put("name", "路由器数量");
		
		Map<String, Object> map12 = new HashMap<String, Object>();
		map12.put("name", result1.size());
		if(isEdit){
			map12.put("class", "btn btn-link a edit");
			map12.put("onclick", "editable(this);");
			map12.put("value", "Rt");
		}
			
		Map<String, Object> map13 = new HashMap<String, Object>();
		map13.put("name", "");

		Rtlist.add(map11);
		Rtlist.add(map12);
		Rtlist.add(map13);
		
		
		
		//获得该实验所需的二层交换机数量,以list.size记录
		String[] para2={"cabinetTempletId",""+cabinetTempletId,"deviceType","2"};
		List<CabinetTempletDevice> result2 =  this.ctdDAO.getListByNProperty(para2);
			
		List<Map<String, Object>> sw2list = new ArrayList<Map<String, Object>>();
				
		Map<String, Object> map21 = new HashMap<String, Object>();
		map21.put("name", "二层交换机数量");
				
		Map<String, Object> map22 = new HashMap<String, Object>();
		map22.put("name", result2.size());
		if(isEdit){
			map22.put("class", "btn btn-link a edit");
			map22.put("onclick", "editable(this);");
			map22.put("value", "Sw2");
		}
		
					
		Map<String, Object> map23 = new HashMap<String, Object>();
		map23.put("name", "");

		sw2list.add(map21);
		sw2list.add(map22);
		sw2list.add(map23);
		
		
		//获得该实验所需的三层交换机数量,以list.size记录
		String[] para3={"cabinetTempletId",""+cabinetTempletId,"deviceType","3"};
		List<CabinetTempletDevice> result3 =  this.ctdDAO.getListByNProperty(para3);
					
		List<Map<String, Object>> sw3list = new ArrayList<Map<String, Object>>();
						
		Map<String, Object> map31 = new HashMap<String, Object>();
		map31.put("name", "三层交换机数量");
						
		Map<String, Object> map32 = new HashMap<String, Object>();
		map32.put("name", result3.size());
		if(isEdit){
			map32.put("class", "btn btn-link a edit");
			map32.put("onclick", "editable(this);");
			map32.put("value", "Sw3");
		}
							
		Map<String, Object> map33 = new HashMap<String, Object>();
		map33.put("name", "");

		sw3list.add(map31);
		sw3list.add(map32);
		sw3list.add(map33);
		
		list.add(Rtlist);
		list.add(sw2list);
		list.add(sw3list);

		return list;
	}
	@SuppressWarnings("unchecked")
	public boolean save(Map<String, Object> deviceMap, Integer cabinetTempletId) {
		
		boolean flag=true;
		boolean success=true;
		//清空本表中此实验机柜模板表的该实验设备，而后重写
		List<CabinetTempletDevice> list = this.ctdDAO.getListByProperty("cabinetTempletId", cabinetTempletId);
		int i=0;
		while(i!=list.size()){
			this.ctdDAO.delete(list.get(i));
			i++;
		}
		int Order = 1;
		int count = 1;
		//重写路由器
		while(count<=(Integer)deviceMap.get("Rt")){
			CabinetTempletDevice ctd = new CabinetTempletDevice();
			ctd.setCabinetTempletId(cabinetTempletId);
			ctd.setDeviceOrder(Order);
			ctd.setDeviceType(1);
			ctd.setLanPortNum(8);    //赋予默认值为8,日后再改
			ctd.setWanPortNum(8);
			flag=this.ctdDAO.add(ctd);
			if(flag==false)  success=flag;
			count++;
			Order++;
		}
		count = 1;
		//重写二层交换机
		while(count<=(Integer)deviceMap.get("Sw2")){
			CabinetTempletDevice ctd = new CabinetTempletDevice();
			ctd.setCabinetTempletId(cabinetTempletId);
			ctd.setDeviceOrder(Order);
			ctd.setDeviceType(2);
			ctd.setLanPortNum(8);
			ctd.setWanPortNum(8);
			flag=this.ctdDAO.add(ctd);
			if(flag==false)  success=flag;
			count++;
			Order++;
		}
		count = 1;
		//重写三层交换机
		while(count<=(Integer)deviceMap.get("Sw3")){
			CabinetTempletDevice ctd = new CabinetTempletDevice();
			ctd.setCabinetTempletId(cabinetTempletId);
			ctd.setDeviceOrder(Order);
			ctd.setDeviceType(3);
			ctd.setLanPortNum(8);
			ctd.setWanPortNum(8);
			flag=this.ctdDAO.add(ctd);
			if(flag==false)  success=flag;
			count++;
			Order++;
		}
		
		return success;
	}
	
}
