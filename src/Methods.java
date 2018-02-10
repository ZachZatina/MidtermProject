import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Methods {

	public static void createDirectory(String dirString) { // referencing directory path

		Path dirPath = Paths.get(dirString);

		if (Files.notExists(dirPath)) {
			try {
				Files.createDirectory(dirPath);
				System.out.println("New folder created: " + dirPath.toAbsolutePath());

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Not sure what happened, contact customer service.");
			}

		}

	}

	// method iterates through the arrayList for cart and utilizes a toString method
	// to display each item in a cart then, print Subtotal, tax, and total.
	public static void printCart(double subtotal, double tax, double total, ArrayList<Cart> cart) {
		System.out.println("");
		for (int i = 0; i < cart.size(); i++) {
			System.out.println(cart.get(i).toString());
			// System.out.println(""); -ACC: removed extra space
		}
		System.out.println(String.format("%1$-10s: $%2$-8.2f", "Subtotal:", subtotal));
		System.out.println(String.format("%1$-10s: $%2$-8.2f", "Tax:", tax));
		System.out.println(String.format("%1$-10s: $%2$-8.2f", "Total:", total));

	}

	// This method iterates through the Product arrayList and uses print format the
	// getProductCategory, getProductName, getPrice, and getProductDescription
	// methods
	public static void displayProductList(ArrayList<Product> productList) {
		int i = 1;
		for (Product e : productList) {
			System.out.printf("%s. %-12s %-30s $%-10.4s\n%-150s\n", i, e.getProductCat(), e.getProductName(),
					e.getPrice(), e.getProductDesc());
			i++;
		}
	}
	
	
	public static ArrayList<Product> createProductList() {
		// method to create product list as an ArrayList, reading from txt file

		ArrayList<Product> productArrayList = new ArrayList<Product>();

		Path readFile = Paths.get("productList.txt");
		File file = readFile.toFile();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			String line = reader.readLine();

			while (line != null) {
				// System.out.println(line);
				// splits the line up on commas, then adds them to an array
				// then uses the pieces from the array to create a new object and add
				// that to productArrayList
				String singleProductArray[] = line.split("-");
				productArrayList.add(new Product(singleProductArray[1], singleProductArray[0], singleProductArray[3],
						Double.parseDouble(singleProductArray[2])));

				line = reader.readLine();
			}
			reader.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Something went wrong with this!");
			e.printStackTrace();
		}
		return productArrayList;
	}
	
	// method to display itemized total for each line of the order
	public static double LineTotal(double price, int quantity) {
		return price * quantity;
	}
	
	
	// this method populates the cart arrayList with selected product
	public static ArrayList<Cart> convertToCart(int quantity, Product product, double lineTotal, ArrayList<Cart> cart) {
		String name = product.getProductName();
		Cart input = new Cart(quantity, name, lineTotal);
		cart.add(input);
		return cart;
	}

	// This method allows the user to select an item in the cart and remove the item
	// from the arrayList
	public static ArrayList<Cart> removeFromCart(Payment payment, ArrayList<Cart> cart, Scanner scan) {
		for (int i = 0; i < cart.size(); i++) {
			System.out.println((i + 1) + ") " + cart.get(i).toString());
			System.out.println("");
		}

		System.out.println(
				"Select the item you would like to remove, or select " + (cart.size() + 1) + " to exit this option: ");
		int userChoice = scan.nextInt();

		if (userChoice > cart.size()) {
			return cart;
		} else {
			for (int i = userChoice; i < cart.size(); i++) {
				cart.set((i - 1), cart.get(i));
			}
			cart.remove(cart.size() - 1);

			payment.setSubtotal(0);
			for (int i = 0; i < cart.size(); i++) {
				payment.calcSubtotal(cart.get(i).getQuantity(),
						(cart.get(i).getLineTotal() / cart.get(i).getQuantity()));
				payment.calcTax();
				payment.calcTotal();
			}
			return cart;
		}
	}


	
}
