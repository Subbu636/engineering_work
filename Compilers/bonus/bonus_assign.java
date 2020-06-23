import syntaxtree.Node;
import visitor.GJDepthFirst;
import visitor.GJDepthFirst2;

public class bonus_assign {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         GJDepthFirst pass1 = new GJDepthFirst<String,String>();
         GJDepthFirst2 pass2 = new GJDepthFirst2<String,String>();
         root.accept(pass1,"");
         pass2.classNames = pass1.classNames;
         pass2.variables = pass1.variables;
         pass2.methods = pass1.methods;
         pass2.vars = pass1.vars;
         pass2.classes = pass1.classes;
         pass2.mtds = pass1.mtds;
         root.accept(pass2,"");
         System.out.println("Program type checked successfully");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}