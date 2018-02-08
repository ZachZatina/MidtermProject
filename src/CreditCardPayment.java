public class CreditCardPayment extends Payment {

	private String ccNumber;
	private String expDate;
	private String ccv;
	private double total;
	
	public CreditCardPayment() {
		
	}
	
	public CreditCardPayment(double subtotal, double tax, double total, String ccNumber, String expDate, String ccv) {
		super(subtotal, tax, total);
		this.ccNumber = ccNumber;
		this.expDate = expDate;
		this.ccv = ccv;
		
	}
	

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getCcv() {
		return ccv;
	}

	public void setCcv(String ccv) {
		this.ccv = ccv;
	}

}
