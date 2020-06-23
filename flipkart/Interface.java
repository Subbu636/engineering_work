
package learnJava;

interface IntSequence {
    boolean hasNext();
    int next();
}

class DigitSequence implements IntSequence {
    private int number;

    public DigitSequence(int n) { number = n;  }
    public boolean hasNext() 	{ return number != 0; }

    public int next() {
        int result = number % 10;
        number /= 10;
        return result;
    }
    
    public int rest() {  return number;  }
}

public class Interfaces {
	public static double average(IntSequence seq, int n) {
		int count = 0;
		double sum = 0;
		while(seq.hasNext() && count < n) {
			count++;
			sum += seq.next();
		}
		return count == 0 ? 0 : sum / count;
	}

	public static void main(String[] args) {
		// SquareSequence squares = new SquareSequence();
		// double avg = average(squares, 100);
		// System.out.println("Average of First 100 Squares: ");

		IntSequence digits = new DigitSequence(1729);
		while (digits.hasNext()) System.out.println(digits.next() + " ");
		System.out.println();

		digits = new DigitSequence(1729);
		double avg = average(digits, 100);
		System.out.println("Average of Digits: " + avg);
	}
}
