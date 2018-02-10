public class CreditCardPayment extends Payment {

	private String ccNumber;
	private String expDate;
	private String ccv;
	
	public CreditCardPayment() {
		
	}
	// constructor for this class
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

	@Override
	public String toString() {
		
		String line1 = String.format("%1$-10s: $%2$-8.2f\n", "Subtotal:", super.getSubtotal());
		String line2 = String.format("%1$-10s: $%2$-8.2f\n", "Tax:", super.getTax());
		String line3 = String.format("%1$-10s: $%2$-8.2f\n\n", "Total:", super.getTotal());

		String line4 = "Credit Card\n";
		String line5 = "Card Number: " + ccNumber + "\n";
		String line6 = "Exp. Date: " + expDate + "\n";
		String line7 = "CVV: " + ccv + "\n";
		String line8 = "Authorization: Approved";
				
		return line1 + line2 + line3 + line4 + line5 + line6 + line7 + line8;
		
	}
}
