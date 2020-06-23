package learnJava;

import java.util.Random;

public class Variables {
	
	// enum is keyword to create newer type
	// Type with fixed and well defined values
	// Weekday is A Type
	// Range Weekday = { MON, TUE, WED, THU, FRI, SAT, SUN }
	enum Weekday { MON, TUE, WED, THU, FRI, SAT, SUN };

	public static void playWithVariables() {
		int total = 100;
		int i = 0, count = 90;

		System.out.println(total);
		System.out.println(i);
		System.out.println(count);
	
        Random generator = new Random();
    
        double lotus = 1000000000.0; // Legal, but not a good idea
        double elevation = 0.0;
        double pi = 3.141592653589793;
        String Count = "Dracula"; // Not the same as count
        int countOfInvalidInputs = 0; // Example of camelCase
        final int DAYS_PER_WEEK = 7; // Immutable 
        Weekday startDay = Weekday.MON;
	}

	public static void PlayWithNumber() {
        //4000000000;
        System.out.println(4000000000L); // long literal
        //  4000000000
	
        System.out.println(0xCAFEBABE); // hex literal
		// -88927571
        
        System.out.println(0b1001); // binary literal
        System.out.println(011); // octal literal
        //System.out.println(078); // Octal Literal 

        // Underscores in literals   
        System.out.println(1_000_000_000); 
        System.out.println(0b1111_0100_0010_0100_0000);
        
        // Advanced topic: Unsigned quantities
        System.out.println(Byte.toUnsignedInt((byte )-127) );
        // 129
        
        System.out.println(3.14F); // float literal
        System.out.println(3.14); // double literal
        System.out.println(3.14D); // double literal
        System.out.println(0x1.0p-3); // hex double literal

        // Infinity and NaN
        System.out.println(1.0 / 0.0); 
        System.out.println(-1.0 / 0.0);
        System.out.println(0.0 / 0.0);
		//  Infinity
		// -Infinity
		// NaN

        System.out.println(1.0 / 0.0 == Double.POSITIVE_INFINITY);
        System.out.println(-1.0 / 0.0 == Double.NEGATIVE_INFINITY);
        System.out.println(0.0 / 0.0 == Double.NaN);
        
        // Character literals        
        System.out.println('J'); 
        System.out.println('J' == 74); 
        System.out.println('\u004A'); 
        System.out.println('\u263A'); 
    }

	public static void main(String[] args) {
		playWithVariables();
		PlayWithNumber();
	}
}


/*
javac Variables.java -d ClassFiles
java -cp ClassFiles/ learnJava.Variables

// ++++++++++++++++++++++++++++++++++++++++
// RAISE HAND WHEN ABOVE EXPERIMENT DONE
// ++++++++++++++++++++++++++++++++++++++++
*/
