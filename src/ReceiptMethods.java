import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ReceiptMethods {

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
			Methods.printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
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
			Methods.printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
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
			Methods.printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
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

	
}
