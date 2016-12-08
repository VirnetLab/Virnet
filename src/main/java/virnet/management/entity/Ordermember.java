package virnet.management.entity;



/**
 * Ordermember entity. @author MyEclipse Persistence Tools
 */

public class Ordermember  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -2417840036589319594L;
	private Integer ordermemberId;
     private Integer ordermemberOrderId;
     private String ordermemberStuCaseId;
     private Integer ordermemberSignUp;


    // Constructors

    /** default constructor */
    public Ordermember() {
    }

    
    /** full constructor */
    public Ordermember(Integer ordermemberId, Integer ordermemberOrderId, String ordermemberStuCaseId, Integer ordermemberSignUp) {
        this.ordermemberId = ordermemberId;
        this.ordermemberOrderId = ordermemberOrderId;
        this.ordermemberStuCaseId = ordermemberStuCaseId;
        this.ordermemberSignUp = ordermemberSignUp;
    }

   
    // Property accessors

    public Integer getOrdermemberId() {
        return this.ordermemberId;
    }
    
    public void setOrdermemberId(Integer ordermemberId) {
        this.ordermemberId = ordermemberId;
    }

    public Integer getOrdermemberOrderId() {
        return this.ordermemberOrderId;
    }
    
    public void setOrdermemberOrderId(Integer ordermemberOrderId) {
        this.ordermemberOrderId = ordermemberOrderId;
    }

    public String getOrdermemberStuCaseId() {
        return this.ordermemberStuCaseId;
    }
    
    public void setOrdermemberStuCaseId(String ordermemberStuCaseId) {
        this.ordermemberStuCaseId = ordermemberStuCaseId;
    }

    public Integer getOrdermemberSignUp() {
        return this.ordermemberSignUp;
    }
    
    public void setOrdermemberSignUp(Integer ordermemberSignUp) {
        this.ordermemberSignUp = ordermemberSignUp;
    }
   








}