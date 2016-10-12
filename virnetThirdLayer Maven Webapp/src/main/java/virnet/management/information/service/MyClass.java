package virnet.management.information.service;

import java.util.Map;

import virnet.management.combinedao.StudentInfoCDAO;

public class MyClass implements InformationQuery {
	
	@SuppressWarnings("unused")
	private StudentInfoCDAO sDAO = new StudentInfoCDAO();

	@Override
	/*
	 * @Documented( get the student user id, then find out the classes he/she belong to)
	 * @see virnet.management.information.service.InformationQuery#query(java.lang.String, int, java.lang.String)
	 */
	public Map<String, Object> query(String user, int page, String select) {
		// TODO Auto-generated method stub
		
		
		
		return null;
	}

}
