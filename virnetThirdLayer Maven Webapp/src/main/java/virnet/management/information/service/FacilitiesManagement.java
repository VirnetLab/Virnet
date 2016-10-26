package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.FacilitiesDAO;
import virnet.management.entity.Facilities;
import virnet.management.util.PageUtil;

public class FacilitiesManagement implements InformationQuery{
	private FacilitiesDAO facilitiesDAO = new FacilitiesDAO();

	/*
	 * @param
	 * page --- required page in database
	 * @return
	 * map : "data" the query list
	 *       "page" total pages
	 * @see virnet.management.information.service.InformationQuery#query(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, String>>> list = new ArrayList<List<Map<String, String>>>();
		List<Map<String, String>> head = new ArrayList<Map<String, String>>();
		Map<String, String> head_id = new HashMap<String, String>();
		head_id.put("name", "设备编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, String> head_name = new HashMap<String, String>();
		head_name.put("name", "设备名称");
		head_name.put("class", "");
		head.add(head_name);		
		
		list.add(head);
		
		PageUtil<Facilities> pageUtil = new PageUtil<Facilities>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		this.facilitiesDAO.getListByPage(pageUtil);
		
		List<Facilities> facilitieslist = new ArrayList<Facilities>();
		facilitieslist = pageUtil.getList();
		
		int size = facilitieslist.size();
		System.out.println("facilities list size : " + size);
		
		for(int i = 0; i < size; i++){
			List<Map<String, String>> facilitiesInfo = new ArrayList<Map<String, String>>();
			
			Map<String, String> map_id = new HashMap<String, String>();
			map_id.put("name", facilitieslist.get(i).getFacilitiesId() + "");
			map_id.put("class", "");
			facilitiesInfo.add(map_id);
			
			Map<String, String> map_name = new HashMap<String, String>();
			map_name.put("name", facilitieslist.get(i).getFacilitiesId()+ "");
			map_name.put("class", "");
			map_name.put("onclick", "showDetail('" + facilitieslist.get(i).getFacilitiesId() + "', 'facilities');");//facilities detail
			facilitiesInfo.add(map_name);
			
			System.out.println("index : " + i + ", exp id : " + facilitieslist.get(i).getFacilitiesId() + ", exp type : " + facilitieslist.get(i).getFacilitiesType());
			
			list.add(facilitiesInfo);
		}
		
		int total = this.facilitiesDAO.getList().size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		Map<Object, Object> button = new HashMap<Object, Object>();
		button.put("content", "+ 新增设备");
		button.put("class", "btn button-new");
		button.put("click", "addContent('facilities-management');");
		
		map.put("data", list);
		map.put("page", pageNO);
		map.put("button_new", button);
		
		return map;
	}

}
