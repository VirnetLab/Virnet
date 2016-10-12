package virnet.management.information.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassTeacherDAO;
import virnet.management.dao.ClassarrangeCaseDAO;
import virnet.management.dao.ClassarrangeDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.dao.ExpDAO;
import virnet.management.dao.PeriodarrangeDAO;
import virnet.management.entity.Class;
import virnet.management.entity.ClassTeacher;
import virnet.management.entity.Classarrange;
import virnet.management.entity.ClassarrangeCase;
import virnet.management.entity.Course;
import virnet.management.entity.Exp;
import virnet.management.entity.Periodarrange;
import virnet.management.util.DateUtil;
import virnet.management.util.PageUtil;
import virnet.management.util.UserInfoProcessUtil;

public class ExpArrangement implements InformationQuery {

	private ClassarrangeCaseDAO cacDAO = new ClassarrangeCaseDAO();
	private ClassarrangeDAO caDAO = new ClassarrangeDAO();
	private PeriodarrangeDAO pDAO = new PeriodarrangeDAO();
	private ClassTeacherDAO ctDAO = new ClassTeacherDAO();
	private ExpDAO expDAO = new ExpDAO();
	private DateUtil dateutil = new DateUtil();
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		UserInfoProcessUtil usercheck = new UserInfoProcessUtil();
		int userid = usercheck.checkUsername(user);
		
		List<Map<String, Object>> head = new ArrayList<Map<String, Object>>();
		Map<String, Object> head_id = new HashMap<String, Object>();
		head_id.put("name", "时段编号");
		head_id.put("class", "");
		head.add(head_id);
		
		Map<String, Object> head_name = new HashMap<String, Object>();
		head_name.put("name", "上课时间");
		head_name.put("class", "");
		head.add(head_name);
		
		Map<String, Object> head_member = new HashMap<String, Object>();
		head_member.put("name", "课程实验");
		head_member.put("class", "");
		head.add(head_member);
		
		list.add(head);
		
		//search the class teacher table, to find out the user's class list, and then find out the class experiment arrangement
		
		String hql = "SELECT model from " + ClassTeacher.class.getName() + " as model where model.classTeacherTeacherId ='" + userid + "'";
		
		List<ClassTeacher> ctlist = new ArrayList<ClassTeacher>();
		ctlist = this.ctDAO.getListByHql(hql);
		
		int size = ctlist.size();
		System.out.println("Class list size : " + size);
		
		List<Object> selectlist = new ArrayList<Object>();
		for(int i = 0; i < size; i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			
			int classid = ctlist.get(i).getClassTeacherClassId();
			cmap.put("id", classid);
			
			ClassDAO cDAO = new ClassDAO();
			List<Class> clist = cDAO.getListByProperty("classId", classid);
			int courseid = clist.get(0).getClassCourseId();
			CourseDAO courseDAO = new CourseDAO();
			List<Course> courselist = courseDAO.getListByProperty("courseId", courseid);
			
			cmap.put("class", courselist.get(0).getCourseName() + " " + clist.get(0).getClassName());
			selectlist.add(cmap);
		}
		
		
		int s = selectlist.size();
		int classid;
		if(s == 0){
			//list is null
			classid = -1;
		}
		else{
			classid = (int) ((Map<String, Object>) selectlist.get(0)).get("id");
		}
		
		for(int i = 0; i < s; i++){
			if(select.equals(((Map<String, Object>) selectlist.get(i)).get("class"))){
				classid = (int) ((Map<String, Object>) selectlist.get(i)).get("id");
			}
		}
		
		
		PageUtil<Class> pageUtil = new PageUtil<Class>();
		if(page == 0){
			page = 1;
		}
		pageUtil.setPageNo(page);
		
		String ghql =  "SELECT model from " + Periodarrange.class.getName() + " as model where model.periodarrangeClassId ='" + classid + "'";
		this.pDAO.getListByPage(ghql, pageUtil);
		
		List<Periodarrange> plist = pageUtil.getList();
		
		int l = plist.size();
		for(int i = 0; i < l; i++){
			List<Map<String, Object>> pInfo = new ArrayList<Map<String, Object>>();
			
			int pid = plist.get(i).getPeriodarrangeId();
			Map<String, Object> map_id = new HashMap<String, Object>();
			map_id.put("name", i + "");
			map_id.put("class", "");
			pInfo.add(map_id);
			
			Map<String, Object> map_time = new HashMap<String, Object>();
			
			int[] tempdate = this.dateutil.StringToIntlist(this.dateutil.dateToString(plist.get(i).getPeriodarrangeStartDate()));
			int startday = this.dateutil.weekdayJudge(tempdate[0], tempdate[1], tempdate[2]) - 1;
			if(startday == 0){
				startday = 7;
			}
			
			tempdate = this.dateutil.StringToIntlist(this.dateutil.dateToString(plist.get(i).getPeriodarrangeEndDate()));
			int endday = this.dateutil.weekdayJudge(tempdate[0], tempdate[1], tempdate[2]) - 1;
			if(endday == 0){
				endday = 7;
			}
			
			map_time.put("name", this.dateutil.dateToString(plist.get(i).getPeriodarrangeStartDate()) + "\n" + this.dateutil.processDate(startday, plist.get(i).getPeriodarrangeStartTime(), endday, plist.get(i).getPeriodarrangeEndTime()));
			map_time.put("class", "");
			pInfo.add(map_time);
			
			Map<String, Object> map_exp = new HashMap<String, Object>();
			map_exp.put("class", "collapse");
			
			List<Object> explist = new ArrayList<Object>();
			List<Classarrange> calist = this.caDAO.getListByProperty("classarrangePeriodArrangeId", pid);
			int casize = calist.size();
			for(int j = 0; j < casize; j++){
				List<ClassarrangeCase> caclist = this.cacDAO.getListByProperty("classarrangeCaseClassArrangeId", calist.get(j).getClassarrangeId());
				
				//get the experiments id
				int cacsize = caclist.size();
				for(int z = 0; z < cacsize; z++){
					int expid = caclist.get(z).getClassarrangeCaseExpId();
					
					List<Exp> elist = this.expDAO.getListByProperty("expId", expid);
					for(int g = 0; g < elist.size(); g++){
						Map<String, Object> mapexplist = new HashMap<String, Object>();
						mapexplist.put("name", elist.get(g).getExpName());
						mapexplist.put("class", "btn btn-link");
						mapexplist.put("onclick", "showDetail('" + elist.get(g).getExpName() + "', 'exp');");
						explist.add(mapexplist);
					}
				}
			}
			
			map_exp.put("name", explist);
			pInfo.add(map_exp);
			
			list.add(pInfo);
		}
		
		int total = this.ctDAO.getListByHql(hql).size();
		int pagesize = pageUtil.getPageSize();
		int pageNO = total / pagesize + 1;
		
		map.put("select", selectlist);
		map.put("data", list);
		map.put("page", pageNO);
		
		return map;
	}

}
