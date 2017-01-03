package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ExpConnectDAO;
import virnet.experiment.entity.ExpConnect;

public class ExpConnectCDAO {
	
	private ExpConnectDAO cDAO = new ExpConnectDAO();
	
	public boolean edit(Integer ExpTopoId,String leftNUM_Str,String rightNUM_Str, String leftport_Str,String rightport_Str){
		
		String leftNUM[] = leftNUM_Str.split("##");
		String rightNUM[] = rightNUM_Str.split("##");
		String leftport[] = leftport_Str.split("##");
		String rightport[] = rightport_Str.split("##");
		
		System.out.println("连接数："+leftNUM.length);
		
		//首先删除原来的记录,如果原来没有记录，则list长度为0
		@SuppressWarnings("unchecked")
		List<ExpConnect> clist = this.cDAO.getListByProperty("expTopoId", ExpTopoId); 
		int i=0;
		while(i!=clist.size()){
			this.cDAO.delete(clist.get(i));
			i++;
		}
		//重写
		boolean flag = true;
		boolean success = true;
		i=0;
		while(i<leftNUM.length){
			ExpConnect expconnect = new ExpConnect();
			expconnect.setExpTopoId(ExpTopoId);
			expconnect.setDevice1Order(Integer.parseInt(leftNUM[i]));
			expconnect.setDevice2Order(Integer.parseInt(rightNUM[i]));
			expconnect.setDevice1PortOrder(Integer.parseInt(leftport[i]));
			expconnect.setDevice2PortOrder(Integer.parseInt(rightport[i]));
			flag = this.cDAO.add(expconnect);
			i++;
			if(flag == false)
				success = false;	
		}
		return success;
	}
	//从数据库中取出连接信息
	public String connectInfo (Integer expTopoId){

		String leftNUM_Str = "";
		String rightNUM_Str = "";
		String leftport_Str = "";
		String rightport_Str = "";
		
		@SuppressWarnings("unchecked")
		List<ExpConnect> clist = this.cDAO.getListByProperty("expTopoId", expTopoId);
		int i=0;
		while(i < clist.size()){
			
			leftNUM_Str = leftNUM_Str + clist.get(i).getDevice1Order() + "##";
			rightNUM_Str = rightNUM_Str + clist.get(i).getDevice2Order() + "##";
			leftport_Str = leftport_Str + clist.get(i).getDevice1PortOrder() + "##";
			rightport_Str = rightport_Str + clist.get(i).getDevice2PortOrder() + "##";
			
			i++;
		}
		//字符串间以逗号相隔，并去掉每个字符串最后的两个##
		String connectInfo = leftNUM_Str.substring(0,leftNUM_Str.length()-2) + "," +
							 rightNUM_Str.substring(0,rightNUM_Str.length()-2) + "," +
							 leftport_Str.substring(0,leftport_Str.length()-2) + "," +
							 rightport_Str.substring(0,rightport_Str.length()-2) ;
		
		return connectInfo;	
	}
}
