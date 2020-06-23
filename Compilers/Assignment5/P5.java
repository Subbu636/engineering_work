import syntaxtree.Node;
import visitor.GJDepthFirst;
import visitor.GJDepthFirst2;

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         GJDepthFirst pass1 = new GJDepthFirst<String,String>();
         GJDepthFirst2 pass2 = new GJDepthFirst2<String,String>();
         root.accept(pass1,"");
         pass2.In = pass1.In;
         pass2.Out = pass1.Out;
         pass2.maxi = pass1.maxi;
         pass2.proc = pass1.proc;
         pass2.box2 = pass1.box2;
         pass2.box3 = pass1.box3;
         pass2.cnt_args = pass1.cnt_args;
         root.accept(pass2,"");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}