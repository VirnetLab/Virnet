package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ExpVerifyPingDAO;
import virnet.experiment.entity.ExpVerifyPing;

public class ExpVerifyPingCDAO {
	
	private ExpVerifyPingDAO vpDAO = new ExpVerifyPingDAO();
	
	public void delete(Integer expVerifyId){
		
		//删除原记录,如果原来没有记录，则list长度为0
		@SuppressWarnings("unchecked")
		List<ExpVerifyPing> vplist = this.vpDAO.getListByProperty("expVerifyId", expVerifyId);
		int i=0;
		while(i!=vplist.size()){
			this.vpDAO.delete(vplist.get(i));
			i++;
		}
	}
	
	//逐个设备重写
	public boolean edit(Integer expVerifyId,String equipmentNumber, String[] ipAddress , String[] verifyResult,
						String leftNUM_Str, String rightNUM_Str,String leftport_Str,String rightport_Str){
		
		//第一个PC序号
		Integer PCNumber =  Integer.parseInt(equipmentNumber) - 3;
		
		String leftNUM[] = leftNUM_Str.split("##");
		String rightNUM[] = rightNUM_Str.split("##");
		String leftport[] = leftport_Str.split("##");
		String rightport[] = rightport_Str.split("##");
		
		//获取PC设备连接的设备序号及端口序号,分别对应4个PC的连接
		Integer[] device =  new Integer[4];
		Integer[] port = new Integer[4];
		
		int i = 0;
		while(i < leftNUM.length){
			
			Integer left = Integer.parseInt(leftNUM[i]);
			Integer right = Integer.parseInt(rightNUM[i]);
			
			if( left >= PCNumber){             //左端是PC			
				device[left-PCNumber] = right;
				port[left-PCNumber] = Integer.parseInt(rightport[i]);
			}
			
			else if(right >= PCNumber){         //右侧是PC
				device[right-PCNumber] = left;
				port[right-PCNumber] = Integer.parseInt(leftport[i]);
			}
			i++;
		}
		
		boolean success = true;
		//verifyResult 中为  1-2 1-3 1-4 2-3 2-4 3-4  的顺序
		int j = 0, ptr = 1;
		i = 1;
		while(i<4){
			j=i+1;
			while(j<=4){
				
				ExpVerifyPing vp= new ExpVerifyPing();
				
				vp.setExpVerifyId(expVerifyId);
				vp.setSourcePCOrder(PCNumber + i -1);
				vp.setSourcePCIp(ipAddress[i]);
				vp.setSourcePCDeviceOrder(device[i - 1]);
				vp.setSourcePCPortOrder(port[i - 1]);
				vp.setDestPCOrder(PCNumber + j - 1);
				vp.setDestPCIp(ipAddress[j]);
				vp.setDestPCDeviceOrder(device[j - 1]);
				vp.setDestPCPortOrder(port[j-1]);
				if(verifyResult[ptr].equals("connected"))
					vp.setSuccessFlag(1);
				else if(verifyResult[ptr].equals("disconnected"))
					vp.setSuccessFlag(0);
				else
					return false;
				
				if(this.vpDAO.add(vp) == false)
					success = false;
				j++;
				ptr++;
		   }
		   i++;
		}
		return success;
	}	
}

