import syntaxtree.*;
import visitor.*;
import visitor.GJDepthFirst;

public class P4 {
   public static void main(String [] args) {
      try {
         Node root = new MiniIRParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         GJDepthFirst pass = new GJDepthFirst<String,String>();
         root.accept(pass,""); // Your assignment part is invoked here.
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



