import java.util.Scanner;

public class Validator {
	
	public static String getString(Scanner sc, String prompt) {
		System.out.print(prompt);
		String s = sc.next(); // read user entry
		return s;
	}

	// method to pseudo-validate CC
	public static String getCC(Scanner sc, String prompt) {

		String s = null;
		String ccNumber = null;
		boolean isValid = false;
		while (isValid == false) {
			System.out.print(prompt);
			s = sc.next(); // read user entry

			if (s.length() == 15) {
				ccNumber =  s.replace(s.subSequence(0, 11), "XXXXXXXXXXXX"); // blocks out first 11 digits of number
				isValid = true;
			} else if (s.length() == 16) {
				ccNumber = s.replace(s.subSequence(0, 12), "XXXX-XXXX-XXXX-"); // blocks out first 12 digits of number
				isValid = true;
			} else {
				System.out.println("Invalid credit card number");
			}
		}
		return ccNumber;
	}

	// method to pseudo-encrypt CC
	public static String getEncryptCC(String s) {

		String ccNumber = null;
		// String cardType = null;
			if (s.length() == 15) {
				// cardType = "AmEx";
				ccNumber =  s.replace(s.subSequence(0, 11), "XXXXXXXXXXXX"); // blocks out first 11 digits of number
			} else if (s.length() == 16) {
				ccNumber = s.replace(s.subSequence(0, 12), "XXXX-XXXX-XXXX-"); // blocks out first 12 digits of number
			} else {
				System.out.println("Invalid credit card number");
			}
		return ccNumber;
	}
	
	// method to validate expDate
	public static String getExpDate(Scanner sc, String prompt) {
		
		String expDateStr = null;
		boolean isValid = false;
		while (isValid == false) {
			System.out.print(prompt);
			expDateStr = sc.next();
			
			String[] expDateArray = expDateStr.split("/"); // convert input to String array for validation
			
			try {
				int month = Integer.parseInt(expDateArray[0]); // convert element to int
				int year = Integer.parseInt(expDateArray[1]); 
				
				if (!(month < 1) && !(month > 12)) {
					if (!(year < 1950) && !(year > 2018)) {
						isValid = true;
					} else {
					System.out.println("Error! Invalid year.");
					}
				} else {
					System.out.println("Error! Invalid entry.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Error! Invalid entry.");
				// e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Error! Invalid entry.");
			}
		}
		return expDateStr;
	}
	
	// method to validate CVV
	public static String getCVV(Scanner sc, String prompt) {
		int i = 0;
		boolean isValid = false;
		while (isValid == false) {
			System.out.print(prompt);
			if (sc.hasNextInt()) {
				i = sc.nextInt();
				if (!(i < 99) && !(i > 999)) {
					isValid = true;
				} else {
					System.out.println("Error! Invalid entry.");
				}
			} else {
				System.out.println("Error! Invalid entry.");
				sc.next(); // discard any other data entered on the line
			}
		}
		String cvv = Integer.toString(i); // int to String
		return cvv;
	}
	
	public static int getInt(Scanner sc, String prompt) {
		int i = 0;
		boolean isValid = false;
		while (isValid == false) {
			System.out.print(prompt);
			if (sc.hasNextInt()) {
				i = sc.nextInt();
				isValid = true;
			} else {
				System.out.println("Error! Invalid integer value. Try again.");
			}
		}
		return i;
	}
	

	public static int getInt(Scanner sc, String prompt, int min, int max) {
		int i = 0;
		boolean isValid = false;
		while (isValid == false) {
			i = getInt(sc, prompt);
			if (i < min)
				System.out.println("Error! Number must be " + min + " or greater.");
			else if (i > max)
				System.out.println("Error! Number must be " + max + " or less.");
			else
				isValid = true;
		}
		return i;
	}

	public static double getDouble(Scanner sc, String prompt) {
		double d = 0;
		boolean isValid = false;
		while (isValid == false) {
			System.out.print(prompt);
			if (sc.hasNextDouble()) {
				d = sc.nextDouble();
				isValid = true;
			} else {
				System.out.println("Error! Invalid decimal value. Try again.");
			}
		}
		return d;
	}

	public static double getDouble(Scanner sc, String prompt, double min, double max) {
		double d = 0;
		boolean isValid = false;
		while (isValid == false) {
			d = getDouble(sc, prompt);
			if (d < min)
				System.out.println("Error! Number must be " + min + " or greater.");
			else if (d > max)
				System.out.println("Error! Number must be " + max + " or less.");
			else
				isValid = true;
		}
		return d;

	}

	// following series of methods to validate credit card, courtesy jsquared
	/** Return true if the card number is valid */

	public static boolean isValid(long number) {
		boolean valid = (getSize(number) >= 13 && getSize(number) <= 16)
				&& (prefixMatched(number, 4) || prefixMatched(number, 5) || prefixMatched(number, 37)
						|| prefixMatched(number, 6))
				&& ((sumOfDoubleEvenPlace(number) + sumOfOddPlace(number)) % 10 == 0);

		return valid;
	}

	/** Get the result from Step 2 */
	public static int sumOfDoubleEvenPlace(long number) {
		int sum = 0;
		String num = number + "";
		for (int i = getSize(number) - 2; i >= 0; i -= 2) {
			sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);
		}
		return sum;
	}

	/**
	 * Return this number if it is a single digit, otherwise, return the sum of the
	 * two digits
	 */
	public static int getDigit(int number) {
		if (number < 9)
			return number;
		else
			return number / 10 + number % 10;
	}

	/** Return sum of odd-place digits in number */
	public static int sumOfOddPlace(long number) {
		int sum = 0;
		String num = number + "";
		for (int i = getSize(number) - 1; i >= 0; i -= 2) {
			sum += Integer.parseInt(num.charAt(i) + "");
		}
		return sum;
	}

	/** Return true if the digit d is a prefix for number */
	public static boolean prefixMatched(long number, int d) {
		return getPrefix(number, getSize(d)) == d;
	}

	/** Return the number of digits in d */
	public static int getSize(long d) {
		String num = d + "";
		return num.length();
	}

	/**
	 * Return the first k number of digits from number. If the number of digits in
	 * number is less than k, return number.
	 */
	public static long getPrefix(long number, int k) {
		if (getSize(number) > k) {
			String num = number + "";
			return Long.parseLong(num.substring(0, k));
		}
		return number;
	}
}
