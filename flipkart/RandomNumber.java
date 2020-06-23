
package learnJava;

//#include <stdio.h>
import java.util.Random;

public class RandomNumber {
	public static void main(String[] args) {

		Random generator = new Random();
		System.out.println(generator.nextInt());
		System.out.println(generator.nextInt());
	}
}

/*
javac RandomNumber.java -d ClassFiles
java -cp ClassFiles/ learnJava.RandomNumber

// ++++++++++++++++++++++++++++++++++++++++
// RAISE HAND WHEN ABOVE EXPERIMENT DONE
// ++++++++++++++++++++++++++++++++++++++++
*/
