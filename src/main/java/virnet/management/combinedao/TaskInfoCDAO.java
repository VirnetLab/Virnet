package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ExpTaskDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.entity.Exp;
import virnet.management.entity.ExpTask;

public class TaskInfoCDAO {
	
	private ExpTaskDAO tDAO = new ExpTaskDAO();
	
	//通过实验名称查找所有任务信息
	public List<List<Map<String, Object>>> showTaskDetail(List<List<Map<String, Object>>> list,String expName,boolean isEdit){
		
		ExpDAO eDAO = new ExpDAO();
		
		@SuppressWarnings("unchecked")
		List<Exp> elist = eDAO.getListByProperty("expName", expName);
		//获得实验Id,以此查询该实验任务
		Integer expId=elist.get(0).getExpId();
		
		//获得该实验的所有任务信息
		@SuppressWarnings("unchecked")
		List<ExpTask> tlist=tDAO.getListByProperty("expId", expId);
		
		Integer i=0;
		while(i<tlist.size()){
			
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("name", "任务序号");
		
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("name", tlist.get(i).getExpTaskOrder());
			map12.put("value", "expTaskOrder"+"#"+tlist.get(i).getExpTaskOrder());
			
			Map<String, Object> map13 = new HashMap<String, Object>();
			map13.put("name", "删除");

			list1.add(map11);
			list1.add(map12);
			list1.add(map13);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map21 = new HashMap<String, Object>();
			map21.put("name", "任务类型");
		
			//在value值中附上任务的次序，以区分不同任务的信息，格式为“数据类型#任务次序”
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("name", tlist.get(i).getExpTaskType());
			if(isEdit){
				map22.put("class", "btn btn-link edit");
				map22.put("onclick", "editable(this);");
				map22.put("value", "expTaskType"+"#"+tlist.get(i).getExpTaskOrder());
			}
			Map<String, Object> map23 = new HashMap<String, Object>();
			map23.put("name", "");
			
			list2.add(map21);
			list2.add(map22);
			list2.add(map23);
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> map31 = new HashMap<String, Object>();
			map31.put("name", "任务内容");
		
			Map<String, Object> map32 = new HashMap<String, Object>();
			map32.put("name", tlist.get(i).getExpTaskContent());
			if(isEdit){
				map32.put("class", "btn btn-link edit");
				map32.put("onclick", "editable(this);");
				map32.put("value", "expTaskContent"+"#"+tlist.get(i).getExpTaskOrder());
			}
			Map<String, Object> map33 = new HashMap<String, Object>();
			map33.put("name", "");
			
			list3.add(map31);
			list3.add(map32);
			list3.add(map33);
			
			list.add(list1);
			list.add(list2);
			list.add(list3);
			
			i++;
		}
		return list;
	}
	
	public boolean save(Integer expId,String Key,Map<String, Object> map){
		//首先解析字符串Key，以#分割,k[0]为数据类型，k[1]为任务序号
		String[] k=Key.split("#");
		//找到任务表中的对应行	
		String[] para={"expId",""+expId,"expTaskOrder",k[1]};
		ExpTask task = (ExpTask) this.tDAO.getByNProperty(para);
				
		switch(k[0]){
			case "expTaskType" : task.setExpTaskType(Integer.parseInt((String) map.get(Key))); break;
			case "expTaskContent" : task.setExpTaskContent((String)map.get(Key)); break;
		}
		
		if(this.tDAO.update(task))
			return true;
		else
			return false;
	}
	public boolean addtask(Integer expId){
		//从1开始搜索未使用的任务号
		Integer i=1,taskOrder=1;
		while(i!=0){
			String[] para={"expId",""+expId,"expTaskOrder",""+i};
			ExpTask task = (ExpTask) this.tDAO.getByNProperty(para);
			//找不到该任务，即此任务次序号未占用
			if(task==null){
				taskOrder=i;
				i=-1;
			}
			i++;
		}
		ExpTask expTask;
		expTask = new ExpTask();
		expTask.setExpId(expId);
		expTask.setExpTaskOrder(taskOrder);
		if(this.tDAO.add(expTask))
			return true;
		else
			return false;
		
	}
	//任务个数
	public Integer taskNum(String expId){
		Integer EXPID = Integer.parseInt(expId);
		@SuppressWarnings("unchecked")
		List<ExpTask> tlist = this.tDAO.getListByProperty("expId", EXPID);
		return tlist.size();
	}
}
