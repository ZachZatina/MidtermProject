import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class POSTerminal {

	public static void main(String[] args) {
		System.out.println("place holder");

		ArrayList<Product> productList = new ArrayList<Product>();

		productList = createProductList();
		
		System.out.println("Menu: (Item, Category, Description, Price)\n");
		for (Product e : productList) {
			//System.out.println(e.getProductName() + ", " + e.getProductCat() + ", " + e.getProductDesc() + ", " + e.getPrice());
			System.out.printf("%s, %s, %s, $%s\n", e.getProductName(), e.getProductCat(), e.getProductDesc(), e.getPrice());
		}
		
	}

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
				productArrayList.add(new Product(singleProductArray[0],singleProductArray[1],singleProductArray[2],Double.parseDouble(
						singleProductArray[3])));
						
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
