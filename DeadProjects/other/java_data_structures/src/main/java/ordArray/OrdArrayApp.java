package ordArray;

import static java.lang.System.out;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class OrdArrayApp {
	static private final int MAX_MULTIPLE_ADD_ELEMENTS = 100;

    public static void main(String[] args) {
		try (Scanner myInput = new Scanner(System.in)) {
			out.print("Enter max array size: ");
			int maxSize = myInput.nextInt();
			OrdArray arr = new OrdArray(maxSize);

		main:
			while (true) {
				out.print(
					"Enter command: (0 - display, 1 - insert, 2 - find, 3 - delete, 4 - add multiple) ");

				switch (myInput.nextInt()) {
					case 0:
						out.print("The array: ");
						out.println(arr.toString());
						break;
					case 1:
						out.print("Enter value to insert: ");
						out.println(arr.insert(myInput.nextLong()));
						break;
					case 2:
						out.print("Enter value to find: ");
						out.println(arr.find(myInput.nextLong()));
						break;
					case 3:
						out.print("Enter value to delete: ");
						out.println(arr.delete(myInput.nextLong()));
						break;
					case 4:
						out.print(String.format("Enter multiple values (enter 0 to stop input; max %s elements): ",
								  OrdArrayApp.MAX_MULTIPLE_ADD_ELEMENTS));
						OrdArray other = new OrdArray(OrdArrayApp.MAX_MULTIPLE_ADD_ELEMENTS);
						for (long i = 0, newElement = 0;
								i < OrdArrayApp.MAX_MULTIPLE_ADD_ELEMENTS && (newElement = myInput.nextLong()) != 0L;
								i++) {
							if (!other.insert(newElement)) {
								out.println(String.format("Can not add value '%d', maybe it was already passed?", newElement));
							}
						}
						out.println(arr.merge(other) ? 
								"The original array size was too small to completely add all passed values, sorry." : 
								String.format("Successfully added %d values.", other.size()));
						break;
					default:
						out.println("Invalid command. Exiting...");
						break main;
				}
			}
		} catch (NoSuchElementException|IllegalStateException e) {
			out.println("Input is bad. Exiting...");
		}
	}
}
