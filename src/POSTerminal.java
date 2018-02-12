import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class POSTerminal {

	public static void main(String[] args) {
		String cont = "n"; // variable for while loop
		int userOption = 0; // variable for option menu
		double lineTotal = 0;
		String payType;
		int userChoice;
		Scanner scan = new Scanner(System.in);

		// prompt user: begin transaction
		do {
			
		System.out.print(
				"Welcome to the Mr. Roboto's Seoul Taco POS Terminal! \n ---> Options (1) Begin transaction (2) Add item to the menu: ");
		userChoice = scan.nextInt();

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
			productList = Methods.createProductList();
			System.out.println("");
			System.out.println("");
			System.out.println("Menu: (Item, Category, Description, Price)\n");

			Methods.displayProductList(productList);
			// begin loop
			while (cont.equalsIgnoreCase("n")) {// while loop added for adding additional items to transaction
				boolean exitprompt = false;
				// prompt: choose item
				int itemChoice = (Validator.getInt(scan, "\nEnter item number to add to order: ", 1,
						(productList.size())) - 1);
				int itemQuantity = Validator.getInt(scan, "Enter quantity: ", 1, 100);

				// line total calculated once the user selects and item and amount to order
				lineTotal = Methods.LineTotal(productList.get(itemChoice).getPrice(), itemQuantity);
				// item and quantity printed with the total cost for specified item
				System.out.printf("%1$-4d %2$-20s $%3$-6.2f \n", itemQuantity,
						productList.get(itemChoice).getProductName(), lineTotal);
				// the item is added to the cart
				cartList = Methods.convertToCart(itemQuantity, productList.get(itemChoice), lineTotal, cartList);
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
						cartList = Methods.removeFromCart(payment, cartList, scan);
						Methods.printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);
					} else if (userOption == 3) { // prints cart
						Methods.printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);
					} else if (userOption == 4) { // displays menu for user to reference
						Methods.displayProductList(productList);
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

			Methods.printCart(payment.getSubtotal(), payment.getTax(), payment.getTotal(), cartList);// cart prints

			boolean correctType = false; // correct type established and while loop is created to go through payment
											// options
			while (correctType == false) { // user prompt to enter payment method
				payType = Validator.getString(scan, "\nHow is the customer paying? (Cash, CC (CreditCard), Check): ");

				// payment method cash, prompts the user to enter cash tendered then subtracts
				// total from the tendered amount
				if (payType.equalsIgnoreCase("CASH")) {
					correctType = true;
					double tendered = Validator.getDouble(scan, "Enter cash value: ", .01, 100000);
					CashPayment cp = new CashPayment(payment.getSubtotal(), payment.getTax(), payment.getTotal(),
							tendered); // create as CashPayment
					System.out.printf("Change due customer = $%.2f\n", cp.getChange()); // display change owed customer
																						// to console
					Payment cpAsP = (CashPayment) cp; // cast CashPayment as Payment to pass to receipt method
					ReceiptMethods.writeReceipt(cartList, payType, cpAsP, payment);

					// check payment option below
				} else if (payType.equalsIgnoreCase("CHECK")) {
					correctType = true;
					String checkNum = Validator.getString(scan, "Enter check number: ");
					System.out.println("The check number entered is: " + checkNum);
					CheckPayment ckp = new CheckPayment(payment.getSubtotal(), payment.getTax(), payment.getTotal(),
							checkNum);
					Payment ckpAsP = (CheckPayment) ckp; // cast CheckPayment as Payment
					ReceiptMethods.writeReceipt(cartList, payType, ckpAsP, payment);

					// CC payment option below
				} else if (payType.equalsIgnoreCase("CC")) {
					correctType = true;

					// --- OPTION 1: method for validating credit card number:
					long ccLong = 0;
					boolean isValidCC = false;
					while (isValidCC == false) {
						System.out.print("Enter a valid credit card number (Do not include spaces or hyphens):\n (e.g. 5466380004069060) ");
						ccLong = scan.nextLong();
						if (Validator.isValid(ccLong)) {
							isValidCC = true;
						} else {
							System.out.println("Invalid.");
						}
					}
					
					String ccString = Long.toString(ccLong); // convert long to string and block
					String ccNumber = Validator.getEncryptCC(ccString); // method to encrypt cc number
					
					// --- OPTION 2: method for pseudo-validation of credit card number:
					//	String ccNumber = Validator.getCC(scan, "Enter credit card number (Do not include spaces or hyphens): ");
					
					String expDate = Validator.getExpDate(scan, "Enter the expiration date (mm/yy): "); // prompt user for expDate
					String cvv = Validator.getCVV(scan, "Enter the CVV: "); // prompts the user to input the cvv security code
					CreditCardPayment ccp = new CreditCardPayment(payment.getSubtotal(), payment.getTax(), payment.getTotal(), ccNumber, expDate, cvv);

					Payment ccpAsP = (CreditCardPayment) ccp;
					ReceiptMethods.writeReceipt(cartList, payType, ccpAsP, payment);

				} else { // if not a valid entry for payment option user informed and loop restarts
					System.out.println("This is not a valid input");
				}
			} // end while loop with payment conditional
			scan.nextLine();
			String again = Validator.getString(scan, "Would you like to complete another transaction? (y/n): "); //
			if (again.equalsIgnoreCase("Y")) {
				userChoice = 1;
			} else {
				userChoice = 0;
			} // end else
		} // end if == 1

		while (userChoice == 2) {
			// clears scanner
			scan.nextLine();

			// creates lists needed by these methods
			ArrayList<Product> products = new ArrayList<Product>();
			LinkedList<String> addToMenuList = new LinkedList<String>();

			// calls method that creates literal list of productList.txt
			addToMenuList = AddToMenu.readStringsFromFile();
			// calls method to create menu will be used to determine index
			products = Methods.createProductList();
			// method that gets the category of food by itself so that information can be
			// used later when adding to the list
			String category = AddToMenu.getCategory(scan);
			// method that parses the rest of the information needed for the menu and
			// returns it in the correct order so that the product list can get the
			// information needed from it without issue
			String newItem = AddToMenu.parseInfo(scan, category);
			// calls method to return the last index of the category type of the food
			int index = AddToMenu.getIndex(products, category);
			// adds the new item to the list at the index given
			addToMenuList.add(index, newItem);
			// over writes the product list with the new list
			AddToMenu.writeToFile(addToMenuList);
			
			// loop to see if the user wants to be able to take orders or just leave the POS Terminal
			System.out.print("What would you like to do now? (1) Restart the POS system, or (2) Exit the POS Terminal: ");
			userChoice = scan.nextInt();
			System.out.println("");
			
			if (userChoice == 2) {
				userChoice = 0;
			}
		}
		
		} while(userChoice != 0);

		System.out.println("Thank you for using the POS Terminal!");

	} // end Main

}
