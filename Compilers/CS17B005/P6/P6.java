import syntaxtree.Node;
import visitor.GJDepthFirst;
import visitor.GJDepthFirst2;

public class P6 {
   public static void main(String [] args) {
      try {
         Node root = new MiniRAParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         GJDepthFirst pass1 = new GJDepthFirst<String,String>();
         root.accept(pass1,"");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}