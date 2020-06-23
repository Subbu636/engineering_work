package learnJava;

class ClassDemo {
    public static void main(String[] args) throws ReflectiveOperationException {
        Object obj = System.out;
        Class<?> cl = obj.getClass();
        
        System.out.println("This object is an instance of " + cl.getName());
		// This object is an instance of java.io.PrintStream

        String className = "java.util.Scanner";
        cl = Class.forName(className);
            // An object describing the java.util.Scanner class
        cl = java.util.Scanner.class;
        System.out.println(cl.getName());
        // java.util.Scanner

        Class<?> cl2 = String[].class; // Describes the array type String[]
        System.out.println(cl2.getName());
        // [Ljava.lang.String;

        System.out.println(cl2.getCanonicalName());
        // java.lang.String[]

        Class<?> cl3 = Runnable.class; // Describes the Runnable interface 
        System.out.println(cl3.getName());
		// java.lang.Runnable

        Class<?> cl4 = int.class; // Describes the int type
        System.out.println(cl4.getName());
		// int
        
        Class<?> cl5 = void.class; // Describes the void type
        System.out.println(cl5.getName());
		// void
    }
}

