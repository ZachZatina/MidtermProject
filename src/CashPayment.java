
public class CashPayment extends Payment{
	
	private double change;
	private double tendered;
	private double total;

	public CashPayment() {
		
	}
	
	public CashPayment (double subtotal, double tax, double total, double change, double tendered) {
		super(subtotal, tax, total);
		this.change = change;
		this.tendered = tendered;
	}
	
	public double getChange() {
		change = tendered - total;
		return change;
	}


	public double getTendered() {
		return tendered;
	}

	public void setTendered(double tendered) {
		this.tendered = tendered;
	}
	
	

}
