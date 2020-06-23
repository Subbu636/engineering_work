package learnJava;

import java.util.Random;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Scanner;

public class DataTypes {
	
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

        System.out.println(1.0 / 0.0 == Double.POSITIVE_INFINITY);
        System.out.println(-1.0 / 0.0 == Double.NEGATIVE_INFINITY);
        System.out.println(0.0 / 0.0 == Double.NaN);
        
        // Character literals        
        System.out.println('J'); 
        System.out.println('J' == 74); 
        System.out.println('\u004A'); 
        System.out.println('\u263A'); 
    
        // Infinity and NaN
        System.out.println(1.0 / 0.0); 
        System.out.println(-1.0 / 0.0);
        System.out.println(0.0 / 0.0);
        //  Infinity
        // -Infinity
        // NaN

        // PREDICT OUTPUT
            /*
            A. T T F
            B. F F T
            C. T T T
            D. F F F
            */
        System.out.println(1.0 / 0.0 == Double.POSITIVE_INFINITY);
        System.out.println(-1.0 / 0.0 == Double.NEGATIVE_INFINITY);
        System.out.println(0.0 / 0.0 == Double.NaN);

        System.out.println(Double.isInfinite(1.0 / 0.0));
        System.out.println(Double.isFinite(0.0 / 0.0));
        System.out.println(Double.isNaN(0.0 / 0.0));
    }

    public static void playWithOperations() {
        // Division and remainder
        
        System.out.println(17 / 5);
        System.out.println(17 % 5);
        System.out.println(Math.floorMod(17, 5));
        
        System.out.println(-17 / 5);
        System.out.println(-17 % 5);
        System.out.println(Math.floorMod(-17, 5));
    
        // Increment and decrement
        int[] a = { 17, 29 };
        int n = 0;
        System.out.printf("%d %d\n", a[n++], n); 
        
        n = 0;
        System.out.printf("%d %d\n", a[++n], n);
        
        // Powers and roots
        System.out.println(Math.pow(10, 9));
        System.out.println(Math.sqrt(1000000));
        
        // Number conversions        
        double x = 42;
        System.out.println(x); // 42.0
        
        float f = 123456789;
        System.out.println(f); // 1.23456792E8
        
        x = 3.75;
        n = (int) x;
        System.out.println(n); // 3
        
        n = (int) Math.round(x); 
        System.out.println(n); // 4
        
        System.out.println('J' + 1); // 75
        char next = (char)('J' + 1); 
        System.out.println(next); // 'K'
        
        n = (int) 3000000000L; 
        System.out.println(n); // -1294967296
    }

    public static void playWithBigIntegers() {
        BigInteger n = BigInteger.valueOf(876543210123456789L);
        BigInteger k = new BigInteger("9876543210123456789");
        BigInteger r = BigInteger.valueOf(5).multiply(n.add(k)); // r = 5 * (n + k)
        System.out.println(r);
        System.out.println(2.0 - 1.1);
        BigDecimal d = BigDecimal.valueOf(2, 0).subtract(BigDecimal.valueOf(11, 1));
        System.out.println(d);
    }

    public static void playWithRelationalOperators() {
        int length = 10;
        int n = 7;
        System.out.println(0 <= n && n < length);
        
        // Short circuit evaluation
        int s = 30;
        System.out.println(n != 0 && s + (100 - s) / n < 50);
        n = 0;
        System.out.println(n != 0 && s + (100 - s) / n < 50);
        System.out.println(n == 0 || s + (1 - s) / n >= 50);
        
        int time = 7;
        System.out.println(time < 12 ? "am" : "pm");
    }

    public static void playWithStrings() {
        String location = "Java"; //Immutable
        String greeting = "Hello " + location; //Immutable
        System.out.println(greeting);
        
        int age = 42;
        // Implicit Type Casting int to String
        // Making Your Intention Clear To Fellow Programmers
        String output = Integer.toString(age) + " years";
        System.out.println(output);
        
        // Logical Error
        System.out.println("Next year, you will be " + age + 1); 
        System.out.println("Next year, you will be " + (age + 1)); 
        // Ok
        
        String names = String.join(", ", "Peter", "Paul", "Mary");
        System.out.println(names);
    
        //Mutable String Builder
        StringBuilder builder = new StringBuilder();
        
        // Fast Enumeration Loop
        for (String id : ZoneId.getAvailableZoneIds()) {
            builder.append(id); // Mutating!
            builder.append(", ");
        }
        
        String result = builder.toString(); // Get Immutable String
        System.out.println(result.substring(0, 200) + "...");
        System.out.println(result.length());

        // Substring
        greeting = "Hello, World!";
        location = greeting.substring(7, 12);
        System.out.println(location);
        
        // Equality testing
        System.out.println(location.equals("World"));
        System.out.println(location == "World");
        System.out.println(location.equalsIgnoreCase("world"));
        System.out.println("word".compareTo("world"));

        int n = 42;
        String str = Integer.toString(n, 2);
        System.out.println(str);
        
        n = Integer.parseInt(str);
        System.out.println(n);

        n = Integer.parseInt(str, 2);
        System.out.println(n);
        
        double x = Double.parseDouble("3.14"); 
        System.out.println(x);
        
        System.out.println(greeting.toUpperCase());
        System.out.println(greeting); // greeting is not changed

        // Unicode
        String javatm = "Java\u2122";
        System.out.println(javatm);
        
        System.out.println(Arrays.toString(javatm.codePoints().toArray()));
        System.out.println(javatm.length());
        
        String octonions = "\ud835\udd46";
        System.out.println(octonions);
        System.out.println(Arrays.toString(octonions.codePoints().toArray()));
        System.out.println(octonions.length()); // Counts code units, not Unicode code points
    }

    public static void playWithInputs() {
        Scanner in = new Scanner(System.in);

        System.out.println("What is your name?");
        String name = in.nextLine();

        System.out.println("How old are you?");
        if (in.hasNextInt()) {
            int age = in.nextInt();
            System.out.printf("Hello, %s. Next year, you'll be %d.\n", name, age + 1);
        } else {
            System.out.printf("Hello, %s. Are you too young to enter an integer?", name);
        }

    }

    public static void playWithDoWhile() {
          Random generator = new Random();      
          int target = 5;
          int count = 1;
          int next;

          do {
             next = generator.nextInt(10);
             count++;
          } while (next != target);
             
          System.out.println("After " + count + " iterations, there was a values of " + target);

    }

    public static void playWithForLoop() {
          Random generator = new Random();      
          int count = 20;
          int sum = 0;

          // Indexing For Loop
          for (int i = 1; i <= count; i++) {
             int next = generator.nextInt(10);
             sum = sum + next;         
          }
          System.out.println("After " + count 
             + " iterations, the sum is " + sum);
     }

     public static void playWithArrays() {
        
        String[] names = new String[10];

        for (int i = 0; i < names.length / 2; i++) {
            names[i] = "";
        }

        names[0] = "Fred";
        names[1] = names[0];
        System.out.println("names="+Arrays.toString(names));

        // primes declared and defined together.
        int[] primes = { 17, 19, 23, 29, 31 };
        
        // Allocate 5 Consecutive Memory Location in Heap
        // Sizeof Each Member will BlockSize of int
        // primes is in stack and Array Object will be In Heap
        // 17, 19... These are also in Heap and Immutable
        // primes is Reference and Mutable
        // Array Members are Also Mutable

        // Redefining It
        primes = new int[] { 2, 3, 5, 7, 11, 13 };
        
        // Enhanced For Loop or Enumeration For Loop
        int sum = 0;
        for (int n : primes) {
            sum += n;
        }
        
        System.out.println("sum=" +sum);

        // Aliasing and copying
        int[] numbers = primes; // Reference Assignments
        //Shallow Copy

        numbers[5] = 42; // Also changes primes
        System.out.println("primes=" + Arrays.toString(primes));
        System.out.println("numbers=" + Arrays.toString(numbers));
        
        primes[5] = 13;
        int[] copiedPrimes = Arrays.copyOf(primes, primes.length);
        // Deep Copy

        copiedPrimes[5] = 31; // Doesn't change primes
        System.out.println("primes=" + Arrays.toString(primes));
        System.out.println("copiedPrimes=" + Arrays.toString(copiedPrimes));
     }

     public static void playWithArrayLists() {
        ArrayList<String> friends = new ArrayList<>();

        // Type Inferencing Happens At Compile Time

        friends.add("Peter");
        friends.add("Paul");
        friends.remove(1);
        friends.add(0, "Paul"); // Adds before index 0
        System.out.println("friends=" + friends);
        String first = friends.get(0);
        System.out.println("first=" + first);
        friends.set(1, "Mary");
        for (int i = 0; i < friends.size(); i++) {
            System.out.println(friends.get(i));
        }
        
        ArrayList<String> people = friends;
        people.set(0, "Mary"); // now friends.get(0) is also "Mary"
        System.out.println("friends=" + friends);
        
        ArrayList<String> copiedFriends = new ArrayList<>(friends);
        copiedFriends.set(0, "Fred"); // doesn't change friends
        System.out.println("copiedFriends=" + copiedFriends);
        System.out.println("friends=" + friends);
        
        friends = new ArrayList<>(List.of("Peter", "Paul", "Mary"));
        String[] names = friends.toArray(new String[0]);
        System.out.println("names=" + Arrays.toString(names));               
        
        ArrayList<String> moreFriends = new ArrayList<>(List.of(names));
        System.out.println("moreFriends=" + moreFriends);         
        
        Collections.reverse(friends);
        System.out.println("After reversing, friends=" + friends);
        Collections.shuffle(friends);
        System.out.println("After shuffling, friends=" + friends);
        Collections.sort(friends);        
        System.out.println("After sorting, friends=" + friends);
    }

     }

	 public static void main(String[] args) {
        System.out.println("\nFunction: playWithVariables ");
		playWithVariables();

        System.out.println("\nFunction: PlayWithNumber");
		PlayWithNumber();

        System.out.println("\nFunction: playWithOperations");
        playWithOperations();

        System.out.println("\nFunction: playWithBigIntegers ");
        playWithBigIntegers();

        System.out.println("\nFunction: playWithRelationalOperators");
        playWithRelationalOperators();

        System.out.println("\nFunction: playWithStrings");
        playWithStrings();

        System.out.println("\nFunction: playWithInputs ");
        playWithInputs();

        System.out.println("\nFunction: playWithDoWhile");
        playWithDoWhile();

        System.out.println("\nFunction: playWithForLoop");
        playWithForLoop();

        System.out.println("\nFunction: playWithArrays");
        playWithArrays();

        // System.out.println("\nFunction: ");
        // System.out.println("\nFunction: ");
        // System.out.println("\nFunction: ");
        // System.out.println("\nFunction: ");
        // System.out.println("\nFunction: ");
        // System.out.println("\nFunction: ");
        // System.out.println("\nFunction: ");
	}
}

/*
javac DataTypes.java -d ClassFiles
java -cp ClassFiles/ learnJava.DataTypes

// ++++++++++++++++++++++++++++++++++++++++
// RAISE HAND WHEN ABOVE EXPERIMENT DONE
// ++++++++++++++++++++++++++++++++++++++++
*/
