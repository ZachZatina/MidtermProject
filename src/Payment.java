
public class Payment {
	
	private double subtotal = 0.00;
	private double tax;
	private double total;
		
	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Payment() {
		
	}
	
	// constructor for class
	public Payment(double subtotal, double tax, double total) {
		this.subtotal = subtotal;
		this.tax = tax;
		this.total = total;
	}
	
	// method to calculate subtotal in class
	public void calcSubtotal(int quantity, double price) {
		double subTotal = 0;
		subTotal = subtotal + (price * quantity);
		this.subtotal = subTotal;
	}
	
	// calculates tax
	public void calcTax() {
		final double TAXRATE = 0.06;
		double tax;
		tax = subtotal * TAXRATE;
		this.tax = tax;
	}
	
	public double getTax() {
		return tax;
	}

	public double getTotal() {
		return total;
	}
	
	// calculates total
	public void calcTotal() {
		double total;
		total = subtotal + tax;
		this.total = total;
	}
	
	public double toPaymentTotal() {
		return total;
	}

}
