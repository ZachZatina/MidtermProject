public class Cart {
	
	private int quantity;
	private String name;
	private double lineTotal;
	
	// constructor for cart
	public Cart(int quantity, String name, double lineTotal) {
		this.quantity = quantity;
		this.name = name;
		this.lineTotal = lineTotal;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}
	
	// to string with format
	public String toString() {
		return String.format("%1$-4d %2$-20s $%3$-6.2f \n", quantity, name, lineTotal);
	}
}
