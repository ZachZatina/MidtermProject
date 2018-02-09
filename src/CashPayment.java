
public class CashPayment extends Payment{
	
	private double change;
	private double tendered;
	private double total;

	public CashPayment() {
		
	}
	
	public CashPayment (double subtotal, double tax, double total, double tendered) {
		super(subtotal, tax, total);
		this.change = tendered - total;
		this.tendered = tendered;
	}
	
	public double getChange() {
		// change = tendered - total;
		return change;
	}


	public void setChange(double change) {
		this.change = change;
	}

	public double getTendered() {
		return tendered;
	}

	public void setTendered(double tendered) {
		this.tendered = tendered;
	}
	
	@Override
	public String toString() {
		
		String line1 = String.format("%1$-10s: $%2$-8.2f\n", "Subtotal:", super.getSubtotal());
		String line2 = String.format("%1$-10s: $%2$-8.2f\n", "Tax:", super.getTax());
		String line3 = String.format("%1$-10s: $%2$-8.2f\n\n", "Total:", super.getTotal());
		
		String line4 = String.format("%1$-10s: $%2$-8.2f\n", "Cash:", tendered);
		String line5 = String.format("%1$-10s: $%2$-8.2f\n", "Change:", change);
		return line1 + line2 + line3 + line4 + line5;
		
	}

}
