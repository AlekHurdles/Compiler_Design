package mainPackage;
import projectPt2.*;
import projectPt3.Syntactic;
import ADT.*;
/**
 *
 * @author abrouill SPRING 2022
 */
public class SyntacticMain {
    public static void main(String[] args) {
        String filePath = "GoodSyntaxASP22.txt";
        boolean traceon = true;
        System.out.print("Aleksandra Anderson - 4451 - CS4100, SPRING 2022 - Compiler IDE used: Eclipse \n\n");
        Syntactic parser = new Syntactic(filePath, traceon);
        parser.parse();
        System.out.println("Done.");
    }
}
