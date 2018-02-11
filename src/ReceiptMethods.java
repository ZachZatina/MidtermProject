import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceiptMethods {

	// method to create directory called "transactions" if one does not exist yet
	public static void createDirectory() { // referencing directory path

		String dirString = "transactions";
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
	
	// method to create receipt as txt file
	public static String createNewReceipt(String timeStamp) {
		String receiptNum = "receipt" + timeStamp + ".txt"; // add timestamp to filename
		Path filePath = Paths.get("transactions", receiptNum); 

		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
			} catch (IOException e) {
				System.out.println("Something went wrong, receipt not created.");
				e.printStackTrace();
			}
		}
		return receiptNum;
	}
	
	// method to write receipt (+ create directory and receipt)
	public static void writeReceipt(ArrayList<Cart> finalCart, String payType, Payment cpAsP,
			Payment payment) {

		createDirectory(); // create directory if one does not exist 
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()); // generate timeStamp
		String receiptNum = createNewReceipt(timeStamp); // create the new receipt
		
		Path writeFile = Paths.get("transactions", receiptNum);
		File file = writeFile.toFile();
		System.out.println("\nOrder Details:");
		
		String dateAndTime = formatDate(timeStamp); // call method to reform dateStamp
		System.out.println(dateAndTime);

		if (payType.equalsIgnoreCase("CASH")) { // cash receipt

			CashPayment cpAgain = (CashPayment) cpAsP; // cast back to CashPayment, to access methods

			// Console receipt:
			Methods.printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
			System.out.println("");
			System.out.println(String.format("%1$-10s: $%2$-8.2f", "Cash:", cpAgain.getTendered()));
			System.out.println(String.format("%1$-10s: $%2$-8.2f", "Change:", cpAgain.getChange()));

			try { // txt receipt:
				PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));
				printOut.println("Mr. Roboto's Seoul Taco\n\nThank you for your order!\n");

				printOut.println(dateAndTime);
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

		} else if (payType.equalsIgnoreCase("CHECK")) { // check receipt

			// Console receipt:
			CheckPayment cpAgain = (CheckPayment) cpAsP; // cast back to CheckPayment
			Methods.printCart(cpAgain.getSubtotal(), cpAgain.getTax(), cpAgain.getTotal(), finalCart);
			System.out.println("");
			System.out.println("Payment type: CHECK");
			System.out.printf("Check Number: %s\n\n", cpAgain.getCheckNum());

			try { // txt receipt:
				PrintWriter printOut = new PrintWriter(new FileOutputStream(file, true));
				printOut.println("Mr. Roboto's Seoul Taco\n\nThank you for your order!");
				printOut.println(dateAndTime); 
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
		} else {  // Credit card receipt

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
				printOut.println(dateAndTime); 
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

	// method to convert timeStamp to dd/mm/yyyy hh:mm:ss
	public static String formatDate (String timeStamp) {
		
		String[] dateTimeArray = timeStamp.split("\\.");
		
		String year = dateTimeArray[0];
		String month = dateTimeArray[1];
		String day = dateTimeArray[2];
		String hour = dateTimeArray[3];
		String min = dateTimeArray[4];
		String sec = dateTimeArray[5];
		String dateAndTime = month + "/" + day + "/" + year + "   " + hour + ":" + min + ":" + sec;
		return dateAndTime;
		
	}
}
