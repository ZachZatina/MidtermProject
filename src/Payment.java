
public abstract class Payment {
	
	private double subtotal = 0.00;
	private double tax;
	private double total;
	
	public abstract void payment();
	
	public Payment() {
		
	}
	
	public double getSubtotal(int quantity, double price) {
		double subTotal = 0;
		subTotal = subtotal + (price * quantity);
		this.subtotal = subTotal;
		return subTotal;
	}
	
	public double getTax() {
		final double TAXRATE = 0.06;
		double tax;
		tax = subtotal * TAXRATE;
		this.tax = tax;
		return tax;	
	}
	
	public double getTotal() {
		double total;
		total = subtotal + tax;
		this.total = total;
		return total;
	}
	
	public double toPaymentTotal() {
		return total;
	}

}
