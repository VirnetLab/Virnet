package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.AppointmentDAO;
import virnet.management.entity.Appointment;
import virnet.management.util.PageUtil;

public class ExpAppointment implements InformationQuery{
	private AppointmentDAO appointmentDAO = new AppointmentDAO();

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		
		List<Map<String, String>> head = new ArrayList<Map<String, String>>();
		Map<String, String> head_id = new HashMap<String, String>();
		head_id.put("name", "预约时间编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> head_name = new HashMap<String, String>();
		head_name.put("name", "预约机柜编号");
		head_name.put("class", "");
		head.add(head_name);
		
		list.add(head);
		
		PageUtil<Appointment> pageUtil = new PageUtil<Appointment>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		this.appointmentDAO.getListByPage(pageUtil);
		
		List<Appointment> Appointmentlist = new ArrayList<Appointment>();
		Appointmentlist = pageUtil.getList();
		
		int size = Appointmentlist.size();
		System.out.println("Course list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, String>> AppointmentInfo = new ArrayList<Map<String, String>>();
			
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("name", Appointmentlist.get(i).getAppointmentTimeId() + "");//此处getAppointmentTimeId()返回的是Interger类型，通过加上""转换为String类型
			map_id.put("class", "");
			AppointmentInfo.add(map_id);
			
			Map<String, String> map_name = new HashMap<String, String>();
			map_name.put("name", Appointmentlist.get(i).getAppointmentCabinetId() + "");
			map_name.put("class", "");
			AppointmentInfo.add(map_name);
			
			System.out.println("index : " + i + ", Appointment time  : " + Appointmentlist.get(i).getAppointmentTimeId() + ", Appointment cabinet : " + Appointmentlist.get(i).getAppointmentCabinetId());
			
			list.add(AppointmentInfo);
		}
		
		int total = this.appointmentDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新增预约");
		button.put("class", "btn button-new");
		button.put("click", "addContent('exp-appointment');");
		
		map.put("button_new", button);		
		map.put("data", list);
		map.put("page", pageNO);
		
		return map;
	}

}
