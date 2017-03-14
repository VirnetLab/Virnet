package virnet.experiment.combinedao;

import java.util.List;

import virnet.experiment.dao.ResultTaskDAO;
import virnet.management.dao.ExpTaskDAO;
import virnet.management.entity.ExpTask;
import virnet.experiment.entity.ResultTask;

public class ResultTaskCDAO {
	
	private ResultTaskDAO rtDAO = new ResultTaskDAO();
	
	//创建实验结果任务表    expCaseId 为实验实例Id  对应entity为resultCaseId
	public boolean init(Integer expCaseId, Integer expId){
		
		@SuppressWarnings("unchecked")
		List<ResultTask>  rtlist= rtDAO.getListByProperty("resultCaseId", expCaseId);
		
		if(rtlist.size()!=0){
			//删除
			int i=0;
			while(i!=rtlist.size()){
				this.rtDAO.delete(rtlist.get(i));
				i++;
			}	
		}
			
		ExpTaskDAO tDAO = new ExpTaskDAO();
		@SuppressWarnings("unchecked")
		List<ExpTask>  tlist = tDAO.getListByProperty("expId", expId);
			
		boolean success = true;
		for(int i=0;i<tlist.size();i++){
				
			ResultTask resultTask = new ResultTask();
			resultTask.setResultCaseId(expCaseId);
			resultTask.setResultExpId(expId);
			resultTask.setResultTaskOrder(tlist.get(i).getExpTaskOrder());
			resultTask.setResultTaskType(tlist.get(i).getExpTaskType());
			resultTask.setResultTaskContent(tlist.get(i).getExpTaskContent());
				
			if(!this.rtDAO.add(resultTask))
				success = false;
		}
		return success;	
	}
	public Integer getResultTaskId(String expCaseId, String expId ,String expTaskOrder){
		
		String[] para = {"resultCaseId",expCaseId,"resultExpId",expId,"resultTaskOrder",expTaskOrder};
		ResultTask resulttask = (ResultTask)this.rtDAO.getByNProperty(para);
		if(resulttask!=null)
			return resulttask.getResultTaskId();
		else
			return 0;
	}
}

