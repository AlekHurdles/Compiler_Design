package mainPackage;

import project1a.*;
import ADT.*;
/**
 *
 * @author abrouill
 */
public class InterpreterMain {
	
    public static void main(String[] args) {
        String filePath = "d:\\"; 
        Interpreter interp = new Interpreter();
        SymbolTable st;
        QuadTable qt;
        
        // interpretation FACTORIAL
        st = new SymbolTable(20);     //Create an empty SymbolTable
        qt = new QuadTable(20);       //Create an empty QuadTable
        interp.initializeFactorialTest(st,qt);  //Set up for FACTORIAL
        interp.InterpretQuads(qt, st, true, filePath+"traceFact.txt");
        st.print("symbolTableFact.txt");
        qt.PrintQuadTable("quadTableFact.txt");
        
        // interpretation SUMMATION
        st = new SymbolTable(20);     //Create an empty SymbolTable
        qt = new QuadTable(20);       //Create an empty QuadTable
        interp.initializeSummationTest(st,qt);  //Set up for SUMMATION
        interp.InterpretQuads(qt, st, true, filePath+"traceSum.txt");
        st.print("symbolTableSum.txt");
        qt.PrintQuadTable("quadTableSum.txt");
        }
}
    