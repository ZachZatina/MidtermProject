
public class CheckPayment extends Payment {

	private String checkNum;
	
	public CheckPayment () {
		
	}
	
	// constructor for this class
	public CheckPayment(double subtotal, double tax, double total, String checkNum) {
		super(subtotal, tax, total);
		this.checkNum = checkNum;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

}
