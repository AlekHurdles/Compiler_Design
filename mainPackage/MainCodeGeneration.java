package mainPackage;

//import ADT.SymbolTable;
//import ADT.Lexical;
import ADT.*;
import projectPt3.Syntactic;
/**
*
* @author abrouill FALL 2021
*/
public class MainCodeGeneration {
  public static void main(String[] args) {
      String filePath = "CodeGenFULL-sp22.txt";
      System.out.print("Aleksandra Anderson\n\n");
      System.out.println("Parsing "+filePath);
      boolean traceon = false; //true; //false;
      Syntactic parser = new Syntactic(filePath, traceon);
      parser.parse();
      
      System.out.println("Done.");
  }
}
