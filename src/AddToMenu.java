import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class AddToMenu {

	// method that reads the menu text file and returns it as a Linked list
	public static LinkedList<String> readStringsFromFile() {
		LinkedList<String> menuStrings = new LinkedList<>();

		Path readFile = Paths.get("productList.txt");
		File file = readFile.toFile();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			String line = reader.readLine();

			while (line != null) {
				menuStrings.add(line);
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

		return menuStrings;
	}

	// method to get the information needed for the menu and returns it as a
	// formatted string
	public static String parseInfo(Scanner sc, String cat) {
		System.out.print("The name of the food: ");
		String name = sc.nextLine();
		System.out.print("The price of the food: ");
		String price = sc.nextLine();
		System.out.print("Short description of food: ");
		String desc = sc.nextLine();

		return cat + "-" + name + "-" + price + "-" + desc;
	}

	// gets the index of the last example of the category type
	public static int getIndex(ArrayList<Product> product, String cat) {
		// creates array list
		ArrayList<String> arr = new ArrayList<String>();
		// a for loop to add the category from each element of productList to an array
		// list
		for (int i = 0; i < product.size(); i++) {
			arr.add(product.get(i).getProductCat());
		}
		// uses the category that was parsed earlier and finds the last index of it so
		// the new item can be added at the end of the same section
		int index = arr.lastIndexOf(cat);
		return index + 1;
	}
	
	// method gets category, validates to make sure its a category that is on the menu.
	public static String getCategory(Scanner sc) {
		boolean correct = false;
		System.out.print("The type of food: ");
		String cat = sc.nextLine();
		while (correct == false) {
			if (cat.equals("Entree") || cat.equals("Sides") || cat.equals("Beverages")) {
				correct = true;
			} else {
				System.out.println("Not a valid type of food (choose Entree, Sides, or Beverages");
				System.out.println("");
				System.out.print("The type of food: ");
				cat = sc.nextLine();
			}
		}
		return cat;
	}
	
	// method that writes to the file by over writing the old .txt fill with the new information
	public static void writeToFile(LinkedList<String> list) {
		Path writeFile = Paths.get("productList.txt");
		File file = writeFile.toFile();

		try {
			PrintWriter printOut = new PrintWriter(new FileOutputStream(file)); // this will overwrite each time.

			for (int i = 0; i < list.size(); i++) {
				printOut.println(list.get(i));
			}

			printOut.close(); // closing flushes our data and closes the PrintWriter (object)
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
