
public class Product {

	String productName;
	String productCat;
	String productDesc;
	double price;
	
	public Product(String productName, String productCat, String productDesc, double price) {
		super();
		this.productName = productName;
		this.productCat = productCat;
		this.productDesc = productDesc;
		this.price = price;
			}
	
	public Product() {
		productName = "Coca-Cola";
		productCat = "Beverage";
		productDesc = "World-famous soda";
		price = 2.5;
		
	}
	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCat() {
		return productCat;
	}
	public void setProductCat(String productCat) {
		this.productCat = productCat;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toString() {
		return productCat + ": " + productName + ": " + productDesc + " $ " + price;
	}

	
} // end class body
