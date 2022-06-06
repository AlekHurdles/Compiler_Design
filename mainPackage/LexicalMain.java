package mainPackage;
import ADT.*;
import projectPt2.*;

public class LexicalMain {
    public static void main(String[] args) {
        String filePath = "d:\\"; 
final int numSymbols = 100;
        SymbolTable mySymbols;
    mySymbols = new SymbolTable(numSymbols);
//Constructor for the Lexical Analyzer
        Lexical myLexer = new Lexical("LexicalTestSP22.txt", 
mySymbols, true);     
// The token class is declared as a public inner class
  // having String lexeme, int code, String mnemonic fields
        Lexical.token currToken;
        // Initial read to set up WHILE loop 
// Returns a null token at EOF
        currToken = myLexer.GetNextToken();
        while (currToken != null) {
          System.out.println(currToken.lexeme+" \t| "+currToken.code+
" \t| "+currToken.mnemonic);
          currToken = myLexer.GetNextToken();      
          }
mySymbols.print("symbolTable2.txt");
}}