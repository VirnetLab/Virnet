package virnet.management.combinedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.dao.ClassDAO;
import virnet.management.dao.ClassTeacherDAO;
import virnet.management.dao.CourseDAO;
import virnet.management.entity.Class;
import virnet.management.entity.ClassTeacher;
import virnet.management.entity.Classgroup;
import virnet.management.entity.Course;
import virnet.management.entity.User;
import virnet.management.util.ViewUtil;

public class ClassInfoCDAO {
	private static String EditTittle = "班级信息";
	
	private ClassDAO cDAO = new ClassDAO();
	private ClassTeacherDAO ctDAO = new ClassTeacherDAO();
	private GroupInfoCDAO gDAO = new GroupInfoCDAO();
	private UserInfoCDAO uDAO = new UserInfoCDAO();
	
	private ViewUtil vutil = new ViewUtil();
	
	public Map<String, Object> showClassDetail(String id, String name){
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", "班级信息 <i class='icon-double-angle-right'></i> " + name);		
		
		switch(id){
		case "exp-management" :
		case "course-management" : 
		case "class-management" :
		case "time-management" : list = this.classManagement(name);
								 Map<String, Object> button = new HashMap<String, Object>();
								 button.put("content", "修改班级信息");
								 button.put("class", "btn button-new");
								 button.put("click", "editContent();");
								 map.put("button", button);
								 break;
		case "class-detail" : 
		case "group" :
		case "exp-arrangement" : list = this.ClassOfTeacher(name);
		}
		
		map.put("data", list);
		map.put("tittle", tittle);
		
		return map;
	}
	
	private List<List<Map<String, Object>>> classManagement(String name){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Class c = this.getClassbyname(name);
		
		if(c != null){
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map11 = new HashMap<String, Object>();
			Map<String, Object> map12 = new HashMap<String, Object>();
			map11.put("name", "班级名称");
			map12.put("name", name);
			list1.add(map11);
			list1.add(map12);
			list.add(list1);
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map21 = new HashMap<String, Object>();
			Map<String, Object> map22 = new HashMap<String, Object>();
			map21.put("name", "所属课程");
			map22.put("name", name.split(" ")[0]);
			map22.put("onclick", "showDetail('" + name.split(" ")[0] + "' , 'course');");
			map22.put("class", "btn btn-link");
			list2.add(map21);
			list2.add(map22);
			list.add(list2);
			
			List<User> ulist = this.getClassTeacher(c.getClassId());
			List<Object> teacherlist = new ArrayList<Object>();
			int usize = ulist.size();
			for(int i = 0; i < usize; i++){
				Map<String, Object> umap = new HashMap<String, Object>();
				
				umap.put("name", ulist.get(i).getUserNickname());
				umap.put("onclick", "showDetail('" +  ulist.get(i).getUserNickname() + "' , 'user');");
				umap.put("class",  "btn btn-link");
				
				teacherlist.add(umap);
			}
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map31 = new HashMap<String, Object>();
			Map<String, Object> map32 = new HashMap<String, Object>();
			map31.put("name", "班级教师");
			map32.put("name", teacherlist);
			map32.put("class", "collapse");
			list3.add(map31);
			list3.add(map32);
			list.add(list3);
		}
		
		return list;
	}
	
	/**
	 * 根据班级名称得到教师所教班级信息
	 * @param name class name
	 * @return
	 */
	public List<List<Map<String, Object>>> ClassOfTeacher(String name){
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Class c = this.getClassbyname(name);
		
		if(c != null){
			//get the class experiments arrangement, class student and group, and time
			List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map11 = new HashMap<String, Object>();
			Map<String, Object> map12 = new HashMap<String, Object>();
			map11.put("name", "班级名称");
			map12.put("name", name);
			list1.add(map11);
			list1.add(map12);
			list.add(list1);
			
			List<User> ulist = this.getClassTeacher(c.getClassId());
			List<Object> teacherlist = new ArrayList<Object>();
			int usize = ulist.size();
			for(int i = 0; i < usize; i++){
				Map<String, Object> umap = new HashMap<String, Object>();
				
				umap.put("name", ulist.get(i).getUserNickname());
				umap.put("onclick", "showDetail('" +  ulist.get(i).getUserNickname() + "' , 'user');");
				umap.put("class",  "btn btn-link");
				
				teacherlist.add(umap);
			}
			
			List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map21 = new HashMap<String, Object>();
			Map<String, Object> map22 = new HashMap<String, Object>();
			map21.put("name", "课程教师");
			map22.put("name", teacherlist);
			map22.put("class", "collapse");
			list2.add(map21);
			list2.add(map22);
			list.add(list2);
			
			List<Classgroup> glist = this.gDAO.getClassGroup(c.getClassId());
			List<Object> grouplist = new ArrayList<Object>();
			int gsize = glist.size();
			for(int i = 0; i < gsize; i++){
				Map<String, Object> gmap = new HashMap<String, Object>();
				
				gmap.put("name", glist.get(i).getClassgroupName());
				gmap.put("onclick", "showDetail('" +  glist.get(i).getClassgroupName() + "', 'group');");
				gmap.put("class",  "btn btn-link");
				
				grouplist.add(gmap);
			}
			
			List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> map31 = new HashMap<String, Object>();
			Map<String, Object> map32 = new HashMap<String, Object>();
			map31.put("name", "课程小组");
			map32.put("name", grouplist);
			map32.put("class", "collapse");
			list3.add(map31);
			list3.add(map32);
			list.add(list3);
		}
		
		return list;
	}

	/**
	 * 根据班级编号得到班级实体类
	 * @param classid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class getClass(int classid){
		List<Class> clist = this.cDAO.getListByProperty("classId", classid);
		
		if(clist.isEmpty() || clist.size() > 1){
			return null;
		}
		else{
			return clist.get(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getClassName(int classid){
		ClassDAO cDAO = new ClassDAO();
		CourseDAO courseDAO = new CourseDAO();
		
		String classname = new String();
		
		List<Class> clist = cDAO.getListByProperty("classId", classid);
		int courseid;
		if(clist.isEmpty() || clist.size() > 1){
			//database error
			courseid = -1;
		}
		else{
			courseid = clist.get(0).getClassCourseId();
		}
		
		List<Course> courselist = courseDAO.getListByProperty("courseId", courseid);
		
		if(courselist.isEmpty() || courselist.size() > 1){
			//database error
			
		}
		else{
			classname = courselist.get(0).getCourseName() + " " + clist.get(0).getClassName();
		}
		
		return classname;
	}
	
	public Class getClassbyname(String name){
		CourseInfoCDAO courseDAO = new CourseInfoCDAO();
		Class c = new Class();
		
		String[] slist = name.split(" ");
		
		c = (Class) this.cDAO.getByNProperty("className", slist[1], "classCourseId", courseDAO.queryByName(slist[0]).getCourseId() + "");

		return c;
	}

	@SuppressWarnings("unchecked")
	public List<Class> getMyClass(int teacherid){
		List<Class> clist = new ArrayList<Class>();
		
		List<ClassTeacher> ctlist = this.ctDAO.getListByProperty("classTeacherTeacherId", teacherid);
		int size = ctlist.size();
		for(int i = 0; i < size; i++){
			clist.add(this.getClass(ctlist.get(i).getClassTeacherClassId()));
		}
		
		return clist;
	}

	@SuppressWarnings("unchecked")
	public List<User> getClassTeacher(int classid){
		List<ClassTeacher> ctlist = this.ctDAO.getListByProperty("classTeacherClassId", classid);
		
		List<User> ulist = new ArrayList<User>();
		int size = ctlist.size();
		for(int i = 0; i < size; i++){
			User u = this.uDAO.getUser(ctlist.get(i).getClassTeacherTeacherId());
			
			if(u != null){
				ulist.add(u);
			}
		}
		
		return ulist;
	}

	public Map<String, Object> Edit(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		
		Map<String, Object> tittle = new HashMap<String, Object>();
		tittle.put("data", EditTittle + " <i class='icon-double-angle-right'></i> " + name);
		
		//get the class name
		List<Map<String, Object>> cname = this.vutil.createList("班级名称", "", "", name, "btn btn-link edit", "editable(this);", "className");
		int classid = this.getClassbyname(name).getClassId();
		
		//get the course list and the course
		
		
		//get the class teacher and course teacher
		
		list.add(cname);
		
		Map<String, Object> button = new HashMap<String, Object>();
		button.put("content", "提交更改");
		button.put("class", "btn button-new");
		button.put("click", "submit();");
		
		map.put("tittle", tittle);
		map.put("data", list);
		map.put("button", button);
		
		return map;
	}

}
