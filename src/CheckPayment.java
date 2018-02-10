
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

	@Override
	public String toString() {
		
		String line1 = String.format("%1$-10s: $%2$-8.2f\n", "Subtotal:", super.getSubtotal());
		String line2 = String.format("%1$-10s: $%2$-8.2f\n", "Tax:", super.getTax());
		String line3 = String.format("%1$-10s: $%2$-8.2f\n\n", "Total:", super.getTotal());

		String line4 = "Check Number: " + checkNum + "\n";
		String line5 = String.format("%1$-10s: $%2$-8.2f\n\n", "Check Amount", super.getTotal());
		return line1 + line2 + line3 + line4 + line5;
		
	}
}
