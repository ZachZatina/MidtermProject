import java.util.Scanner;

public class Validator {
	public static String getString(Scanner sc, String prompt) {
		System.out.print(prompt);
		String s = sc.next(); // read user entry
		return s;
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
	

		/** Return true if the card number is valid */
		public static boolean isValid(long number) {
			boolean valid =
				(getSize(number) >= 13 && getSize(number) <= 16) &&
				(prefixMatched(number, 4) || prefixMatched(number, 5) ||
				prefixMatched(number, 37) || prefixMatched(number, 6)) &&
				((sumOfDoubleEvenPlace(number) + sumOfOddPlace(number)) % 10 == 0);

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
		
		/** Return this number if it is a single digit, otherwise,
		* return the sum of the two digits */
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
		
		/** Return the first k number of digits from number. If the
		* number of digits in number is less than k, return number. */
		public static long getPrefix(long number, int k) {
			if (getSize(number) > k)  {
				String num = number + "";
				return  Long.parseLong(num.substring(0, k));
			}
			return number;
		}
	}
	
