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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class POSTerminal {

	public static void main(String[] args) {
		String cont = "n"; // variable for while loop
		int userOption = 0; // variable for option menu
		double lineTotal = 0;
		String payType;
		Scanner scan = new Scanner(System.in);
		// Create a directory called "transactions" to hold receipts
		// If directory doesn't exist, create one
		createDirectory("transactions");

		// prompt user: begin transaction
		System.out.print(
				"Welcome to the Mr. Roboto's Seoul Taco POS Terminal! \n ---> Options (1) Begin transaction (2) Examine transactions: ");
		int userChoice = scan.nextInt();

		ArrayList<Product> productList;
		ArrayList<Cart> cartList;
		Payment payment = new Payment();

		while (userChoice == 1) {
			cartList = new ArrayList<>();
			cont = "n";
			payment.setSubtotal(0.00);
			payment.setTax(0.00);
			payment.setTotal(0.00);

			// if no, exit; if yes: Display menu
			productList = new ArrayList<Product>();
			productList = createProductList();
			System.out.println("");
			System.out.println("");
			System.out.println("Menu: (Item, Category, Description, Price)\n");

			displayProductList(productList);
			// begin loop
			while (cont.equalsIgnoreCase("n")) {// while loop added for adding aditional items to transaction
				boolean exitprompt = false;
				// prompt: choose item
				System.out.print("Enter item number to add to order: ");
				int itemChoice = scan.nextInt() - 1;

				System.out.print("Enter quantity: ");
				int itemQuantity = scan.nextInt();
				// line total calculated once the user selects and item and amount to order
				lineTotal = LineTotal(productList.get(itemChoice).getPrice(), itemQuantity);
				// item and quantity printed with the total cost for specified item
				System.out.printf("%1$-4d %2$-20s $%3$-6.2f \n", itemQuantity,
						productList.get(itemChoice).getProductName(), lineTotal);
				// the item is added to the cart
				cartList = convertToCart(itemQuantity, productList.get(itemChoice), lineTotal, cartList);
				// the item and quantity is added to the payment object, for later
				payment.calcSubtotal(itemQuantity, productList.get(itemChoice).getPrice());
				// tax is added and new total with tax calculated
				payment.calcTax();
				payment.calcTotal();
				// the user is then presented with the following options to continue the order
				// process
				while (exitprompt == false) {
					userOption = Validator.getInt(scan,
							"Would you like to: (1) Add another item / (2) Remove an item / (3) View cart / (4) View Menu / (5) Proceed to check-out: ",
							1, 5);
					System.out.println("");

					if (userOption == 1) {
						exitprompt = true;
						continue;
					} else if (userOption == 2) { // remove an item option, removes item from cart list and the prints
													// updated cart
						cartList = removeFromCart(payment, cartList, scan);
						printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);
					} else if (userOption == 3) { // prints cart
						printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);
					} else if (userOption == 4) { // displays menu for user to reference
						displayProductList(productList);
					} else if (userOption == 5) { // sets conditional to true and ends loops
						cont = Validator.getString(scan,
								"You entered 'Proceed to Checkout' 'Y' to confirm, 'N' to go back: ");
						if (cont.equalsIgnoreCase("y")) {
							exitprompt = true;
							continue;
						} else {
							continue;// end while loop for adding additional items to transaction
						}
					} // end else if conditional for option 5
				} // end while loop for conditional exitPrompt == false

			} // end to while for cont

			printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);// cart prints

			boolean correctType = false; // correct type established and while loop is created to go through payment
											// options
			while (correctType == false) { // user prompt to enter payment method
				System.out.print("How is the customer paying? (Cash, CC (CreditCard), Check): ");
				payType = scan.nextLine();
				
				String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
				String receiptNum = "receipt" + timeStamp + ".txt"; // add timestamp to filename
				createReceipt(receiptNum);
				
				// payment method cash, prompts the user to enter cash tendered then subtracts
				// total from the tendered amount
				if (payType.equalsIgnoreCase("CASH")) {
					correctType = true;
					System.out.print("Enter cash value: ");
					double tendered = scan.nextDouble();

					CashPayment cp = new CashPayment(payment.getSubtotal(), payment.getTax(), payment.getTotal(), tendered);  // create as CashPayment
					System.out.printf("Change due customer = $%.2f\n", cp.getChange()); // display change owed customer to console
					Payment cpAsP = (CashPayment)cp; // cast CashPayment as Payment to pass to receipt method
					
					System.out.printf("Change to customer = $%.2f\n", cp.getChange()); // display change owed customer
																						// to console
					// receipt created with payment type, time stamp, and then utilize writeReceipt
					writeReceipt(receiptNum, cartList, payType, cpAsP, payment, timeStamp);
					// check payment option below
				} else if (payType.equalsIgnoreCase("CHECK")) {
					correctType = true;
					System.out.print("Enter check number: "); // prompts user to enter check number
					String checkNum = scan.next();
					System.out.println("The check number entered is: " + checkNum);
				
					CheckPayment ckp = new CheckPayment(payment.getSubtotal(), payment.getTax(), payment.getTotal(), checkNum);
					Payment ckpAsP = (CheckPayment)ckp; // cast CheckPayment as Payment
					
					writeReceipt(receiptNum, cartList, payType, ckpAsP, payment, timeStamp);

				} else if (payType.equalsIgnoreCase("CC")) {
					correctType = true;
					System.out.print("Enter credit card number (Do not include spaces or hypens): "); // prompts user to enter credit card number, then
					// blocks out first 12 digits for customer security
					String ccNumber = scan.nextLine(); // FIXME: validator number and length
					ccNumber = ccNumber.replace(ccNumber.subSequence(0, 12), "XXXX-XXXX-XXXX-");
					System.out.print("Enter the expiration date (mm/dd): ");
					String expDate = scan.next(); // FIXME: validator date // prompts user to input expiration date
					System.out.print("Enter the CVV: "); // prompts the user to input the cvv security code
					String cvv = scan.next();
					
					CreditCardPayment ccp = new CreditCardPayment(payment.getSubtotal(), payment.getTax(), payment.getTotal(), ccNumber, expDate, cvv);
					Payment ccpAsP = (CreditCardPayment)ccp;

					writeReceipt(receiptNum, cartList, payType, ccpAsP, payment, timeStamp);
					
				} else { // if not a valid entry for payment option user informed and loop retartes
					System.out.println("This is not a valid input");
				}
			} // end while loop with payment conditional
			scan.nextLine();
			System.out.print("Would you like to complete another transaction? (y/n): "); // app ends and user prompted
																							// to complete another
																							// transaction, if no
																							// POSTerminal closes
			String again = scan.nextLine();
			if (again.equalsIgnoreCase("Y")) {
				userChoice = 1;
			} else {
				userChoice = 0;
			}

			// create receipt file, put in directory

		}
		// end if == 1

		System.out.println("Thank you for using the POS Terminal!");

	} // end Main

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

	public static void createReceipt(String fileString) {
		// method to create receipt as txt file

		Path filePath = Paths.get("transactions", fileString); // hardcode directory

		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
			} catch (IOException e) {
				System.out.println("Something went wrong, receipt not created.");
				e.printStackTrace();
			}
		}
	}

	public static void writeReceipt(String filePath, ArrayList<Cart> finalCart, String payType, Payment cpAsP,
			Payment payment, String timeStamp) {

		Path writeFile = Paths.get("transactions", filePath);
		File file = writeFile.toFile();
		System.out.println("\nOrder Details:");

		if (payType.equalsIgnoreCase("CASH")) {

			CashPayment cpAgain = (CashPayment) cpAsP; // cast back to CashPayment, to access methods

			// Console receipt:
			printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
			System.out.println("");
			System.out.println(String.format("%1$-10s: $%2$-8.2f", "Cash:", cpAgain.getTendered()));
			System.out.println(String.format("%1$-10s: $%2$-8.2f", "Change:", cpAgain.getChange()));

			try { // txt receipt:
				PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));
				printOut.println("Mr. Roboto's Seoul Taco\n\nThank you for your order!");

				printOut.println(timeStamp); // FIXME: reformat
				printOut.println("");

				for (int i = 0; i < finalCart.size(); i++) {
					printOut.println(finalCart.get(i).toString());
				}

				printOut.println(cpAgain.toString());
				printOut.close();
				System.out.println("\n--- Receipt generated ---\n");
			} catch (FileNotFoundException e) {
				// output an error comment
				e.printStackTrace();
			}

		} else if (payType.equalsIgnoreCase("CHECK")) {
			
			// Console receipt:
			CheckPayment cpAgain = (CheckPayment) cpAsP; // cast back to CheckPayment
			printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
			System.out.println("");
			System.out.println("Payment type: CHECK");
			System.out.printf("Check Number: %s\n\n", cpAgain.getCheckNum());
			
			try { // txt receipt:
				PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));
				printOut.println("Mr. Roboto's Seoul Taco\n\nThank you for your order!");
				printOut.println(timeStamp); // FIXME: reformat
				printOut.println("");
				
				for (int i = 0; i < finalCart.size(); i++) {
					printOut.println(finalCart.get(i).toString());
				}

				printOut.println(cpAgain.toString());
				printOut.close();
				System.out.println("--- Receipt generated ---\n");
			} catch (FileNotFoundException e) {
				// output an error comment
				e.printStackTrace();
			}
		} else {
			
			// Console receipt:
			CreditCardPayment cpAgain = (CreditCardPayment) cpAsP; // cast back to CreditCardPayment
			printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
			System.out.println("");
			System.out.println("Payment type: Credit Card");
			System.out.printf("Card Number: %s\n", cpAgain.getCcNumber());
			System.out.printf("Exp. Date: %s\n", cpAgain.getExpDate());
			System.out.printf("CCV: %s\n", cpAgain.getCcv());
			System.out.println("Authorization: Approved\n");			
			
			try { // txt receipt:
				PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));
				printOut.println("Mr. Roboto's Seoul Taco\n\nThank you for your order!");
				printOut.println(timeStamp); // FIXME: reformat
				printOut.println("");
				
				for (int i = 0; i < finalCart.size(); i++) {
					printOut.println(finalCart.get(i).toString());
				}

				printOut.println(cpAgain.toString());
				printOut.close();
				System.out.println("--- Receipt generated ---\n");
			} catch (FileNotFoundException e) {
				// output an error comment
				e.printStackTrace();
			}
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
