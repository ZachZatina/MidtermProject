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
		System.out.println(
				"Welcome to the [name] POS Terminal! \n ---> Options (1) Begin transaction (2) Examine transactions");
		int userChoice = scan.nextInt();

		ArrayList<Product> productList;
		ArrayList<Cart> cartList;
		Payment payment = new SubPayment();
		

		if (userChoice == 1) {
			// if no, exit; if yes: Display menu
			productList = new ArrayList<Product>();
			productList = createProductList();
			System.out.println("Menu: (Item, Category, Description, Price)\n");
			
			displayProductList(productList);
			
			// prompt: choose item? [bonus options: add an item? remove an item?]
			System.out.println("Enter item number to add to order.");
			int itemChoice = scan.nextInt() - 1;
			// Display choice and price
			System.out.print("Enter quantity: ");
			int itemQuantity = scan.nextInt();
			double lineTotal = LineTotal(productList.get(itemChoice).getPrice(), itemQuantity);

			// display: line total (current item price * quantity) -- use method
			// add to cart

			System.out.printf("%1$-4d %2$-20s $%3$-6.2f \n", itemQuantity, productList.get(itemChoice).getProductName(),
					lineTotal);

			cartList = convertToCart(itemQuantity, productList.get(itemChoice), lineTotal);
			
			// put this inside the loop to allow the user to keep picking things from the menu so it keeps adding to the subtotal.
			double subtotal = payment.getSubtotal(itemQuantity, productList.get(itemChoice).getPrice());
			
			// these will need to be used in checkout, 
			double tax = payment.getTax();
			double total = payment.getTotal();
			

			scan.nextLine();// may not need this here to clear the scanner
			String payType;
			boolean correctType = false;
			while (correctType == false) {
				System.out.print("How is the customer paying? (Cash, CC (CreditCard), Check): ");
				payType = scan.nextLine();

				if (payType.equalsIgnoreCase("CASH")) {
					correctType = true;
					System.out.print("Enter cash value: ");
					double tendered = scan.nextDouble();
					double change = tendered - lineTotal;
					System.out.print("Change = " + change);
					Payment p = new CashPayment(); // type the needed inputs here for the cashPayment class
				} else if (payType.equalsIgnoreCase("CHECK")) {
					correctType = true;
					System.out.print("Enter check number: ");
					String checkNum = scan.next();
					System.out.println("The check number entered is: " + checkNum);
					Payment p = new CheckPayment(); // put corresponding inputs in ().
				} else if (payType.equalsIgnoreCase("CC")) {
					correctType = true;
					System.out.print("Enter credit card number: ");
					String ccNumber = scan.next();
					ccNumber = ccNumber.replace(ccNumber.subSequence(0, 11), "XXXX-XXXX-XXXX-");
					System.out.print("Enter the expiration date: ");
					String expDate = scan.next();
					System.out.print("Enter the CVV: ");
					String cvv = scan.next();
					System.out.println(ccNumber +" "+ cvv +" "+ expDate);
					Payment p = new CreditCardPayment(); // type corresponding inputs.
				} else {
					System.out.println("This is not a valid input");
				}
			}
			System.out.println("Exiting the loop worked");
			

		} // end if == 1

		// prompt: view cart? complete order? add another item? remove item?

		
		
		// Create a directory called "transactions" to hold receipts
		// If directory doesn't exist, create one
		createDirectory("transactions"); 
		// create receipt file, put in directory
		String receiptNum = "receipt001"; // FIXME: add a counter to add number to receipt? or time stamp?
		createReceipt(receiptNum); 
		
		// write to receipt
	
		//Cart cart = new Cart(2, "taco", 40.5);  	// test code, create sample cart
		//System.out.println(cart.getLineTotal());
		
		String payType = "cash"; // test code
		
		// writeReceipt(receiptNum, cart, payType); -- change to cartList
		
	} // end Main
	
	
	public static void createDirectory(String dirString) { // referencing directory path

		Path dirPath = Paths.get(dirString);
		System.out.println("New folder created: " + dirPath.toAbsolutePath());

		if (Files.notExists(dirPath)) {
			try {
				Files.createDirectory(dirPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Not sure what happened, contact customer service.");
			}

		}

	}

	// method to create receipt as txt file
	public static void createReceipt(String fileString) {
		
		Path filePath = Paths.get("transactions", fileString); // hardcoded directory

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
	
	public static void writeReceipt(String filePath, ArrayList<Cart> cartList, String payType) {


		Path writeFile = Paths.get("transactions", filePath);

		File file = writeFile.toFile();

		System.out.println(payType);
		
		//for loop
		//cartList.get(arg0).toString
		
		//System.out.println("Test:" + cart.toString()); // test code
		
		try {
			PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));

			// printOut.println(cart.toString()); // print to txt file

			printOut.close(); //
		} catch (FileNotFoundException e) {
			// output an error comment
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

	public static double LineTotal(double price, int quantity) {
		return price * quantity;
	}

	public static ArrayList<Cart> convertToCart(int quantity, Product product, double lineTotal) {

		ArrayList<Cart> cart = new ArrayList<>();
		String name = product.getProductName();
		Cart input = new Cart(quantity, name, lineTotal);
		cart.add(input);
		return cart;
	}
	
	public static void printCart(double subtotal, double tax, double total, ArrayList<Cart> cart) {
		System.out.println("You currently have in your cart:");
		System.out.println("");
		for(int i = 0; i < cart.size(); i++) {
			System.out.println(cart.get(i).toString());
			System.out.println("");
		}
		System.out.println(String.format("%1$-10s: $%2$-8.2f", "Subtotal:", subtotal));
		System.out.println(String.format("%1$-10s: $%2$-8.2f", "Tax:", tax));
		System.out.println(String.format("%1$-10s: $%2$-8.2f", "Total:", total));

	}
	
	public static void displayProductList(ArrayList<Product> productList) {
		int i = 1;
		for (Product e : productList) {
			System.out.printf("%s. %s   /   %s   /   %s   /   $%s\n", i, e.getProductName(), e.getProductCat(),
					e.getProductDesc(), e.getPrice());
			i++;
		}
	}
	
}
