
public class CashPayment extends Payment{
	
	private double change;
	private double tendered;
	private double total;
	
	@Override
	public void payment() {
		this.total = super.toPaymentTotal();
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
