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

		// prompt user: begin transaction?, [maybe: examine old transaction]
		System.out.print(
				"Welcome to the [name] POS Terminal! \n ---> Options (1) Begin transaction (2) Examine transactions: ");
		int userChoice = scan.nextInt();

		ArrayList<Product> productList;
		ArrayList<Cart> cartList = new ArrayList<>();
		Payment payment = new Payment();

		if (userChoice == 1) {
			// if no, exit; if yes: Display menu
			productList = new ArrayList<Product>();
			productList = createProductList();
			System.out.println("Menu: (Item, Category, Description, Price)\n");

			displayProductList(productList);
			// begin loop
			while (cont.equalsIgnoreCase("n")) {// while loop added for adding aditional items to transaction
				boolean exitprompt = false;
				// prompt: choose item? [bonus options: add an item? remove an item?]
				System.out.print("Enter item number to add to order: ");
				int itemChoice = scan.nextInt() - 1;
				// Display choice and price
				System.out.print("Enter quantity: ");
				int itemQuantity = scan.nextInt();
				lineTotal = LineTotal(productList.get(itemChoice).getPrice(), itemQuantity);

				// add to cart

				System.out.printf("%1$-4d %2$-20s $%3$-6.2f \n", itemQuantity,
						productList.get(itemChoice).getProductName(), lineTotal);

				cartList = convertToCart(itemQuantity, productList.get(itemChoice), lineTotal, cartList);

				payment.calcSubtotal(itemQuantity, productList.get(itemChoice).getPrice());

				payment.calcTax();
				payment.calcTotal();

				while (exitprompt == false) {
					userOption = Validator.getInt(scan,
							"Would you like to: (1) Add another item / (2) Remove an item / (3) View cart / (4) View Menu / (5) Proceed to check-out: ",
							1, 5);
					System.out.println("");
					
					if (userOption == 1) {
						exitprompt = true;
						continue;
					} else if (userOption == 2) {
						cartList = removeFromCart(payment, cartList, scan);
						printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);
					} else if (userOption == 3) {
						printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);
					} else if (userOption == 4) {
						displayProductList(productList);
					} else if (userOption == 5) {
						cont = Validator.getString(scan, "Would you like to proceed to checkout: (y/n): ");
						if (cont.equalsIgnoreCase("y")) {
							exitprompt = true;
							continue;
						} else {
							continue;// end while loop for adding additional items to transaction
						}
					}
				}


			} // temp end to while for cont
				// scan.nextLine();// if not commented out you would have to hit enter again
				// after typing y to continue

			boolean correctType = false;
			while (correctType == false) {
				System.out.print("How is the customer paying? (Cash, CC (CreditCard), Check): ");
				payType = scan.nextLine();

				if (payType.equalsIgnoreCase("CASH")) {
					correctType = true;
					System.out.print("Enter cash value: ");
					double tendered = scan.nextDouble();
					double change = tendered - payment.getTotal();
					System.out.printf("Change = $%.2f\n", change); // changed to printf
					CashPayment cp = new CashPayment(payment.getSubtotal(), payment.getTax(), payment.getTotal(),
							change, tendered); // create as CashPayment

					Payment cpAsP = (CashPayment) cp; // cast CashPayment as Payment to pass to method

					String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
					String receiptNum = "receipt" + timeStamp + ".txt"; // add timestamp to filename
					createReceipt(receiptNum);
					writeReceipt(receiptNum, cartList, payType, cpAsP, payment, timeStamp);

				} else if (payType.equalsIgnoreCase("CHECK")) {
					correctType = true;
					System.out.print("Enter check number: ");
					String checkNum = scan.next();
					System.out.println("The check number entered is: " + checkNum);
					Payment p = new CheckPayment();
					((CheckPayment) p).setCheckNum(checkNum);

				} else if (payType.equalsIgnoreCase("CC")) {
					correctType = true;
					System.out.print("Enter credit card number: ");
					String ccNumber = scan.next();
					ccNumber = ccNumber.replace(ccNumber.subSequence(0, 11), "XXXX-XXXX-XXXX-");
					System.out.print("Enter the expiration date: ");
					String expDate = scan.next();
					System.out.print("Enter the CVV: ");
					String cvv = scan.next();
					System.out.println(ccNumber + " " + cvv + " " + expDate);
					Payment p = new CreditCardPayment();
					((CreditCardPayment) p).setCcNumber(ccNumber);
					((CreditCardPayment) p).setCcv(cvv);
					((CreditCardPayment) p).setExpDate(expDate);

				} else {
					System.out.println("This is not a valid input");
				}
			}

			System.out.println("Exiting the loop worked");

			// prompt: view cart? complete order? add another item? remove item?

			// create receipt file, put in directory

		} // end if == 1

	} // end Main

	public static void createDirectory(String dirString) { // referencing directory path

		Path dirPath = Paths.get(dirString);
		System.out.println("New folder created: " + dirPath.toAbsolutePath());

		if (Files.notExists(dirPath)) {
			try {
				Files.createDirectory(dirPath);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Not sure what happened, contact customer service.");
			}

		}

	}

	// method to create receipt as txt file
	public static void createReceipt(String fileString) {

		Path filePath = Paths.get("transactions", fileString); // hardcode directory

		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
				System.out.println("Receipt was created successfully.\n");
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

	public static void writeReceipt(String filePath, ArrayList<Cart> finalCart, String payType, Payment cpAsP,
			Payment payment, String timeStamp) {

		Path writeFile = Paths.get("transactions", filePath);

		File file = writeFile.toFile();

		System.out.println("Thank you for your order!");

		if (payType.equalsIgnoreCase("CASH")) {

			CashPayment cpAgain = (CashPayment) cpAsP; // cast back to CashPayment, to access methods

			printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);

			System.out.println(String.format("%1$-10s: $%2$-8.2f", "Cash:", cpAgain.getTendered()));
			System.out.println(String.format("%1$-10s: $%2$-8.2f", "Change:", cpAgain.getChange()));

			try {
				PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));

				printOut.println("Name\nAddress\n\nThank you for your order!");

				printOut.println(timeStamp); // FIXME: reformat
				printOut.println("");

				// printOut.println(printCart(cpAgain.getSubtotal(), cpAgain.getTax(),
				// cpAgain.getTotal(), finalCart));

				printOut.println(String.format("%1$-10s: $%2$-8.2f", "Cash:", cpAgain.getTendered()));
				printOut.println(String.format("%1$-10s: $%2$-8.2f", "Change:", cpAgain.getChange()));

				System.out.println("TEST: write to receipt completed"); // test code

				printOut.close();
			} catch (FileNotFoundException e) {
				// output an error comment
				e.printStackTrace();
			}

		}
		// else if (check) {
		//
		// } else {
		// (credit)
		// }

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

	public static ArrayList<Cart> convertToCart(int quantity, Product product, double lineTotal, ArrayList<Cart> cart) {
		String name = product.getProductName();
		Cart input = new Cart(quantity, name, lineTotal);
		cart.add(input);
		return cart;
	}

	public static void printCart(double subtotal, double tax, double total, ArrayList<Cart> cart) {
		System.out.println("");
		for (int i = 0; i < cart.size(); i++) {
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
			System.out.printf("%s. %-12s %-30s $%-10.4s\n%-150s\n", i, e.getProductCat(), e.getProductName(),
					e.getPrice(), e.getProductDesc());
			i++;
		}
	}

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
