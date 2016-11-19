package virnet.management.entity;

/**
 * Classarrange entity. @author MyEclipse Persistence Tools
 */

public class Classarrange implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1635055167869150021L;
	private Integer classarrangeId;
	private Integer classarrangePeriodArrangeId;
	private Integer classarrangeStartClassOrder;
	private Integer classarrangeClassNum;
	private String classarrangeName;
	private Short classarrangeUnited;
	private String classarrangeStartOrderDate;
	private String classarrangeStartOrderTime;
	private String classarrangeEndOrderDate;
	private String classarrangeEndOrderTime;

	// Constructors

	/** default constructor */
	public Classarrange() {
	}

	/** minimal constructor */
	public Classarrange(Integer classarrangePeriodArrangeId,
			Integer classarrangeStartClassOrder) {
		this.classarrangePeriodArrangeId = classarrangePeriodArrangeId;
		this.classarrangeStartClassOrder = classarrangeStartClassOrder;
	}

	/** full constructor */
	public Classarrange(Integer classarrangePeriodArrangeId,
			Integer classarrangeStartClassOrder, Integer classarrangeClassNum,
			String classarrangeName, Short classarrangeUnited,
			String classarrangeStartOrderDate,
			String classarrangeStartOrderTime, String classarrangeEndOrderDate,
			String classarrangeEndOrderTime) {
		this.classarrangePeriodArrangeId = classarrangePeriodArrangeId;
		this.classarrangeStartClassOrder = classarrangeStartClassOrder;
		this.classarrangeClassNum = classarrangeClassNum;
		this.classarrangeName = classarrangeName;
		this.classarrangeUnited = classarrangeUnited;
		this.classarrangeStartOrderDate = classarrangeStartOrderDate;
		this.classarrangeStartOrderTime = classarrangeStartOrderTime;
		this.classarrangeEndOrderDate = classarrangeEndOrderDate;
		this.classarrangeEndOrderTime = classarrangeEndOrderTime;
	}

	// Property accessors

	public Integer getClassarrangeId() {
		return this.classarrangeId;
	}

	public void setClassarrangeId(Integer classarrangeId) {
		this.classarrangeId = classarrangeId;
	}

	public Integer getClassarrangePeriodArrangeId() {
		return this.classarrangePeriodArrangeId;
	}

	public void setClassarrangePeriodArrangeId(
			Integer classarrangePeriodArrangeId) {
		this.classarrangePeriodArrangeId = classarrangePeriodArrangeId;
	}

	public Integer getClassarrangeStartClassOrder() {
		return this.classarrangeStartClassOrder;
	}

	public void setClassarrangeStartClassOrder(
			Integer classarrangeStartClassOrder) {
		this.classarrangeStartClassOrder = classarrangeStartClassOrder;
	}

	public Integer getClassarrangeClassNum() {
		return this.classarrangeClassNum;
	}

	public void setClassarrangeClassNum(Integer classarrangeClassNum) {
		this.classarrangeClassNum = classarrangeClassNum;
	}

	public String getClassarrangeName() {
		return this.classarrangeName;
	}

	public void setClassarrangeName(String classarrangeName) {
		this.classarrangeName = classarrangeName;
	}

	public Short getClassarrangeUnited() {
		return this.classarrangeUnited;
	}

	public void setClassarrangeUnited(Short classarrangeUnited) {
		this.classarrangeUnited = classarrangeUnited;
	}

	public String getClassarrangeStartOrderDate() {
		return this.classarrangeStartOrderDate;
	}

	public void setClassarrangeStartOrderDate(String classarrangeStartOrderDate) {
		this.classarrangeStartOrderDate = classarrangeStartOrderDate;
	}

	public String getClassarrangeStartOrderTime() {
		return this.classarrangeStartOrderTime;
	}

	public void setClassarrangeStartOrderTime(String classarrangeStartOrderTime) {
		this.classarrangeStartOrderTime = classarrangeStartOrderTime;
	}

	public String getClassarrangeEndOrderDate() {
		return this.classarrangeEndOrderDate;
	}

	public void setClassarrangeEndOrderDate(String classarrangeEndOrderDate) {
		this.classarrangeEndOrderDate = classarrangeEndOrderDate;
	}

	public String getClassarrangeEndOrderTime() {
		return this.classarrangeEndOrderTime;
	}

	public void setClassarrangeEndOrderTime(String classarrangeEndOrderTime) {
		this.classarrangeEndOrderTime = classarrangeEndOrderTime;
	}

}