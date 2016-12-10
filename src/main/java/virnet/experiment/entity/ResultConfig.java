package virnet.experiment.entity;

public class ResultConfig implements java.io.Serializable {


	/**
	 *  实验结果配配置表
	 */
	private static final long serialVersionUID = 7855981000166010468L;
	private Integer resultConfigId;
	private Integer resultTaskId;
	private String  resultConfigNum;
	
	public ResultConfig(Integer resultConfigId, Integer resultTaskId, String resultConfigNum) {
		super();
		this.resultConfigId = resultConfigId;
		this.resultTaskId = resultTaskId;
		this.resultConfigNum = resultConfigNum;
	}

	public Integer getResultConfigId() {
		return resultConfigId;
	}

	public void setResultConfigId(Integer resultConfigId) {
		this.resultConfigId = resultConfigId;
	}

	public Integer getResultTaskId() {
		return resultTaskId;
	}

	public void setResultTaskId(Integer resultTaskId) {
		this.resultTaskId = resultTaskId;
	}

	public String getResultConfigNum() {
		return resultConfigNum;
	}

	public void setResultConfigNum(String resultConfigNum) {
		this.resultConfigNum = resultConfigNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}