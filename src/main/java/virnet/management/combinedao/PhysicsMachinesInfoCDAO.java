package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import virnet.management.dao.PhysicsMachinesDAO;
import virnet.management.entity.PhysicsMachines;
import virnet.management.util.ViewUtil;

public class PhysicsMachinesInfoCDAO {
	
	private PhysicsMachinesDAO pDAO = new PhysicsMachinesDAO();
	private ViewUtil vutil = new ViewUtil();
	
 	public Map<String, Object> showPhysicsMachinesDetail(String id, String name){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "机柜  " + name +"<i class='icon-double-angle-right'></i> ");		
		
		String returnid = id;
		
		switch(id){
		case "time-management" :
		case "class-management" :
		case "course-management" :
		case "exp-management" : 
		case "physicsMachines-management" : Map<String, Object> button = new HashMap<String, Object>();
									 button.put("content", "修改设备详情");
									 button.put("class", "btn button-new");
									 button.put("click", "editContent();");
									 list = PhysicsMachinesManagement(name, false); 
									 map.put("button", button);
									 returnid = "physicsMachines-management";
									 break;//管理员的机柜管理 
		}
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("id", returnid);
		
		return map;
	}
	
 	/**
 	 * 
 	 * 在管理员的权限中查看机柜内容，修改配置
 	 * @param name the name of experiment want to query
 	 * @param isEdit check whether the list is for edit page
 	 * 
 	 * @return a list contains experiment name, standard time, profile and instruction, enter experiment 
 	 *  in the format of map                    //待定
 	 */
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> PhysicsMachinesManagement(String name, boolean isEdit){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		//机柜不能重名
		List<PhysicsMachines> plist = this.pDAO.getListByProperty("physicsMachinesName", name);   //待定

		if(plist.isEmpty() || plist.size() > 1){
			//error
			list.clear();
		}
		else{
 
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "物理机柜编号");
			
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", plist.get(0).getPhysicsMachinesName());
			if(isEdit){
				map12.put("class", "btn btn-link edit");
				map12.put("onclick", "editable(this);");
				map12.put("value", "physicsMachinesName");
			}
			
			list1.add(map11);
			list1.add(map12);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "于实验室编号");
			
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", plist.get(0).getPhysicsMachinesLabId());
			if(isEdit){
				map22.put("class", "btn btn-link edit");
				map22.put("onclick", "editable(this);");
				map22.put("value", "physicsMachinesLabId");
			}
			
			list2.add(map21);
			list2.add(map22);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "厂家信息");
			
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", plist.get(0).getPhysicsMachinesFactory());
			if(isEdit){
				map32.put("class", "btn btn-link edit");
				map32.put("onclick", "editable(this);");
				map32.put("value", "physicsMachinesFactory");
			}
			
			list3.add(map31);
			list3.add(map32);
			
			List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map41 = new HashMap<String, Object>();
			map41.put("name", "物理机柜ip");
			
			Map<String, Object> map42 = new HashMap<String, Object>();
			map42.put("name", plist.get(0).getphysicsMachinesIp());
			if(isEdit){
				map42.put("class", "btn btn-link edit");
				map42.put("onclick", "editable(this);");
				map42.put("value", "physicsMachinesIp");
			}
			
			list4.add(map41);
			list4.add(map42);
			
			List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map51 = new HashMap<String, Object>();
			map51.put("name", "路由器数量");
			
			Map<String, Object> map52 = new HashMap<String, Object>();
			map52.put("name", plist.get(0).getPhysicsMachinesRtNumber());
			if(isEdit){
				map52.put("class", "btn btn-link edit");
				map52.put("onclick", "editable(this);");
				map52.put("value", "physicsMachinesRtNumber");
			}
			
			list5.add(map51);
			list5.add(map52);
			
			List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map61 = new HashMap<String, Object>();
			map61.put("name", "三层交换机数量");
			
			Map<String, Object> map62 = new HashMap<String, Object>();
			map62.put("name", plist.get(0).getPhysicsMachinesLayer3SwNumber());
			if(isEdit){
				map62.put("class", "btn btn-link edit");
				map62.put("onclick", "editable(this);");
				map62.put("value", "physicsMachinesLayer3SwNumber");
			}
			
			list6.add(map61);
			list6.add(map62);
			
			List<Map<String, Object>> list7 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map71 = new HashMap<String, Object>();
			map71.put("name", "二层交换机数量");
			
			Map<String, Object> map72 = new HashMap<String, Object>();
			map72.put("name", plist.get(0).getPhysicsMachinesLayer2SwNumber());
			if(isEdit){
				map72.put("class", "btn btn-link edit");
				map72.put("onclick", "editable(this);");
				map72.put("value", "physicsMachinesLayer2SwNumber");
			}
			
			list7.add(map71);
			list7.add(map72);
			
			List<Map<String, Object>> list8 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map81 = new HashMap<String, Object>();
			map81.put("name", "上联端口数");
			
			Map<String, Object> map82 = new HashMap<String, Object>();
			map82.put("name", plist.get(0).getPhysicsMachinesUpport());
			if(isEdit){
				map82.put("class", "btn btn-link edit");
				map82.put("onclick", "editable(this);");
				map82.put("value", "physicsMachinesUpport");
			}
			
			list8.add(map81);
			list8.add(map82);
			
			List<Map<String, Object>> list9 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map91 = new HashMap<String, Object>();
			map91.put("name", "机柜端口数");
			
			Map<String, Object> map92 = new HashMap<String, Object>();
			map92.put("name", plist.get(0).getPhysicsMachinesNumberPort());
			if(isEdit){
				map92.put("class", "btn btn-link edit");
				map92.put("onclick", "editable(this);");
				map92.put("value", "physicsMachinesNumberPort");
			}
			
			list9.add(map91);
			list9.add(map92);
			

			list.add(list1);
			list.add(list2);
			list.add(list3);
			list.add(list4);
			list.add(list5);
			list.add(list6);
			list.add(list7);
			list.add(list8);
			list.add(list9);
		}
		
		return list;
	}
	
	/**
	 * 老师查看设备信息
	 * 
	 * @param name the name of experiment want to query
	 * 
	 * @return a list contains experiment name, standard time, profile and instruction 
 	 *  in the format of map
 	 *                                           //待定
	 */


	/**
	 * 生成编辑设备界面的数据
	 * @param name the name of experiment want to query
	 * @return the data show on the page
	 */
	public Map<String, Object> Edit(String name){

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "机柜"+name+" <i class='icon-double-angle-right'></i> "  );
		
		list = PhysicsMachinesManagement(name, true);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}
	
	public Map<String, Object> Add(){

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "物理机柜 <i class='icon-double-angle-right'></i> 新增机柜");
		
		List<Map<String, Object>> Name = this.vutil.createList("物理机柜编号", "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesName");
		List<Map<String, Object>> Labid = this.vutil.createList("于实验室编号", "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesLabId");
		List<Map<String, Object>> factory = this.vutil.createList("厂家信息", "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesFactory");
		List<Map<String, Object>> ip = this.vutil.createList("物理机柜ip", "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesIp");
		List<Map<String, Object>> rt = this.vutil.createList("路由器数量", "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesRtNumber");
		List<Map<String, Object>> sw3 = this.vutil.createList("三层交换机数量"    , "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesLayer3SwNumber");
		List<Map<String, Object>> sw2 = this.vutil.createList("二层交换机数量"    , "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesLayer2SwNumber");
		List<Map<String, Object>> upport = this.vutil.createList("上联端口数"    , "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesUpport");
		List<Map<String, Object>> numberport = this.vutil.createList("机柜端口数"    , "", "", "", "btn btn-link edit", "editable(this);", "physicsMachinesNumberPort");
		
		list.add(Name);
		list.add(Labid);
		list.add(factory);
		list.add(ip);
		list.add(rt);
		list.add(sw3);
		list.add(sw2);
		list.add(upport);
		list.add(numberport);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "保存信息");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);	
		return map;
	}
	
	public Map<String, Object> save(String name, Map<String, Object> map){
		Map<String, Object> r = new HashMap<String, Object>();
		
		PhysicsMachines physicsMachines;

		if(name.equals("")){
			physicsMachines = new PhysicsMachines();
		}
		else{
			physicsMachines = (PhysicsMachines) this.pDAO.getUniqueByProperty("physicsMachinesName", name);
		}
		
		Set<String> key = map.keySet();
		Iterator<String> keylist = key.iterator();
		while(keylist.hasNext()){
			String k = keylist.next();
			switch(k){
			case "physicsMachinesName" : physicsMachines.setPhysicsMachinesName((String) map.get(k)); break;
			case "physicsMachinesLabId" : physicsMachines.setPhysicsMachinesLabId(Integer.parseInt((String) map.get(k))); break;
			case "physicsMachinesFactory" : physicsMachines.setPhysicsMachinesFactory((String) map.get(k)); break;
			case "physicsMachinesIp" : physicsMachines.setphysicsMachinesIp((String) map.get(k)); break;
			case "physicsMachinesRtNumber" : physicsMachines.setPhysicsMachinesRtNumber(Integer.parseInt((String) map.get(k))); break;
			case "physicsMachinesLayer3SwNumber" : physicsMachines.setPhysicsMachinesLayer3SwNumber(Integer.parseInt((String) map.get(k))); break;
			case "physicsMachinesLayer2SwNumber" : physicsMachines.setPhysicsMachinesLayer2SwNumber(Integer.parseInt((String) map.get(k))); break;
			case "physicsMachinesUpport" : physicsMachines.setPhysicsMachinesUpport(Integer.parseInt((String) map.get(k))); break;
			case "physicsMachinesNumberPort" : physicsMachines.setPhysicsMachinesNumberPort(Integer.parseInt((String) map.get(k))); break;
			}
		}

//		System.out.println(facilities.getFacilitiesBelongPhysicsMachines());
//		System.out.println(facilities.getFacilitiesNumberUsePort());
//		System.out.println(facilities.getFacilitiesIp());
////		System.out.println(facilities.getFacilitiesId());
		if(name.equals("")){
			if(this.pDAO.add(physicsMachines)){
				r.put("isSuccess", true);
				r.put("id", map.get("physicsMachinesId"));
				r.put("key", "physicsMachines");
			}
			else{
				r.put("isSuccess", false);
			}
		}
		else{
			if(this.pDAO.update(physicsMachines)){
				r.put("isSuccess", true);
				r.put("id", map.get("physicsMachinesId"));
				r.put("key", "physicsMachines");
			}
			else{
				r.put("isSuccess", false);
			}
		}
		
		return r;
	}

}