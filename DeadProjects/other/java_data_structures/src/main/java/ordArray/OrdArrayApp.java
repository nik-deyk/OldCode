package ordArray;

import static java.lang.System.out;
import java.util.Scanner;

public class OrdArrayApp {
    public static void main(String[] args) {
		try (Scanner myInput = new Scanner(System.in)) {
			out.print("Enter max array size: ");
			int maxSize = myInput.nextInt(); // array size
			 OrdArray 	arr = new OrdArray(maxSize); // create the array

		main:
			while (true) {
				out.print(
				    "Enter command: (0 - display, 1 - insert, 2 - find, 3 - delete) ");

				switch (myInput.nextInt()) {
					case 0:
						out.print("The array: ");
						arr.display();
						break;
					case 1:
						out.print("Enter value to insert: ");
						out.println(arr.insert(myInput.nextInt()));
						break;
					case 2:
						out.print("Enter value to find: ");
						out.println(arr.find(myInput.nextInt()));
						break;
					case 3:
						out.print("Enter value to delete: ");
						out.println(arr.delete(myInput.nextInt()));
						break;
					default:
						out.println("Exiting...");
						break main;
				}
			}
		}
	}
}
