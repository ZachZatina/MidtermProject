import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class POSTerminal {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		// prompt user: begin transaction?, [maybe: examine old transaction]
		System.out.println("Welcome to the [name] POS Terminal! \n ---> Options (1) Begin transaction (2) Examine transactions");
		int userChoice = scan.nextInt();

		ArrayList<Product> productList;

		if (userChoice == 1) {
			// if no, exit; if yes: Display menu
			productList = new ArrayList<Product>();
			productList = createProductList();
			System.out.println("Menu: (Item, Category, Description, Price)\n");

			int i = 1;
			for (Product e : productList) {
				// System.out.println(e.getProductName() + ", " + e.getProductCat() + ", " +
				// e.getProductDesc() + ", " + e.getPrice());
				System.out.printf("%s. %s   /   %s   /   %s   /   $%s\n", i, e.getProductName(), e.getProductCat(), e.getProductDesc(),
						e.getPrice());
				i++;

			}
			// prompt: choose item? [bonus options: add an item? remove an item?]
			System.out.println("Enter item number to add to order.");
			int itemChoice = scan.nextInt()-1;
			// Display choice and price
			System.out.println("Enter quantity:");
			int itemQuantity = scan.nextInt();

			// display: line total (current item price * quantity) -- use method
			// add to cart
			
			System.out.printf("%s, $%s", productList.get(itemChoice).getProductName(),
					productList.get(itemChoice).getPrice());
			//

			
		} // end if == 1



		// prompt: view cart? complete order? add another item? remove item?

	}

	// method to create receipt as txt file
	public static void createFile(String dirString, String fileString) {
		
		Path filePath = Paths.get(dirString, fileString);
		
		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
				System.out.println("Receipt was created successfully.");
			} catch (IOException e) {
				System.out.println("Something went wrong, receipt not created.");
				e.printStackTrace();
			}
		}
	}
	
	// method to write to receipt file
	// name, phone, email
	// use toString from Cart
	// blank space btn items
	// subtotal
	// tax
	// total
	
	
	public static void writeReceipt(String dirString, String filePath, Cart cart) {

		Path writeFile = Paths.get(dirString, filePath);

		File file = writeFile.toFile();

		try {
			PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));

			printOut.println(cart.toString());

			printOut.close(); //
		} catch (FileNotFoundException e) {
			// output comment
			e.printStackTrace();
		}
	}


	// method to create product list as an ArrayList, reading from txt file
	public static ArrayList<Product> createProductList() {

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
				String singleProductArray[] = line.split(",");
				productArrayList.add(new Product(singleProductArray[0], singleProductArray[1], singleProductArray[2],
						Double.parseDouble(singleProductArray[3])));

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
}
