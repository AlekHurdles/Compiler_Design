package projectPt3;
import ADT.*;
import projectPt2.*;
import project1a.*;

/**
 *
 * @author abrouill
 */
public class Syntactic {
	private String filein;              //The full file path to input file
	private SymbolTable symbolList;     //Symbol table storing ident/const
	private QuadTable quadTable;
	private Interpreter interp;
	private Lexical lex;                //Lexical analyzer
	private Lexical.token token;        //Next Token retrieved
	private boolean traceon;            //Controls tracing mode
	private int level = 0;              //Controls indent for trace mode
	private boolean anyErrors;          //Set TRUE if an error happens
	private final int symbolSize = 250;
	private final int quadSize = 500;
	private int Minus1Index;
	private int Plus1Index;
	private int genCount = 0;
	private int patchElse = 0;
	
    public Syntactic(String filename, boolean traceOn) {
    	filein = filename;
    	traceon = traceOn;
    	symbolList = new SymbolTable(symbolSize);
    	Minus1Index = symbolList.AddSymbol("-1", symbolList.constantkind, -1);
    	
    	Plus1Index = symbolList.AddSymbol("1", symbolList.constantkind, 1);
    	quadTable = new QuadTable(quadSize);
    	interp = new Interpreter();
    	
    	lex = new Lexical(filein, symbolList, true);
    	lex.setPrintToken(traceOn);
    	anyErrors = false;
    }
//The interface to the syntax analyzer, initiates parsing
// Uses variable RECUR to get return values throughout the non-terminal methods    
    public void parse() {
    	// make filename pattern for symbol table and quad table output later
    	String filenameBase = filein.substring(0, filein.length() - 4);
    	System.out.println(filenameBase);
    	int recur = 0;
    	
    	// prime the pump, get first token
    	token = lex.GetNextToken();
    	
    	// call PROGRAM
    	recur = Program();
    	
    	// done, so add the final STOP quad
    	quadTable.AddQuad(0, 0, 0, 0);
    	
    	//print ST, QUAD before execute
    	symbolList.print(filenameBase + "ST-before.txt");
    	quadTable.PrintQuadTable(filenameBase + "QUADS.txt");
    	
    	//interpret
    	if (!anyErrors) {
    		interp.InterpretQuads(quadTable, symbolList, false, filenameBase + "TRACE.txt");
    	} else {
    		System.out.println("Errors, unable to run program.");
    	}
    	
    	symbolList.print(filenameBase + "ST-after.txt");
    }
    
// Handle write line
    public int handleWriteln() {
    	int recur = 0;
    	int toprint = 0;
    	if (anyErrors) {
    	return -1;
    	}
    	trace("handleWriteln", true);
    	//got here from a WRITELN token, move past it...
    	token = lex.GetNextToken();
    	//look for ( stringconst, ident, simpleexp )
    	
    	if (token.code == lex.codeFor("OPAR")) {
    		
	    	//move on
	    	token = lex.GetNextToken();
	    	
	    	if ((token.code == lex.codeFor("IDENT"))) { 
	    		
		    	// save index for string literal or identifier
		    	toprint = symbolList.LookupSymbol(token.lexeme);
		    	
		    	//move on
		    	token = lex.GetNextToken();
	    	} else {
	    		toprint = SimpleExpression();
	    		}
	    	
	    	//Print opCode = 6
	    	quadTable.AddQuad(6, toprint, 0, 0);
	    	
	    	//now need right ")"
	    	if (token.code == lex.codeFor("CPAR")) {
		    	//move on
		    	token = lex.GetNextToken();
	    	} else {
	    		error(lex.reserveFor("CPAR"), token.lexeme);
	    	  }
    	} else {
    		error(lex.reserveFor("OPAR"), token.lexeme);
    		}
    	// end lpar group
    	trace("handleWriteln", false);
    	return recur;
    	}
    
//Non Terminal     
    private int ProgIdentifier() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        // This non-term is used to uniquely mark the program identifier
        if (token.code == lex.codeFor("IDENT")) {
            // Because this is the progIdentifier, it will get a 'p' type to 
        	//prevent re-use as a var
        	
            symbolList.UpdateSymbol(symbolList.LookupSymbol(token.lexeme), 'p', 0);
            //move on
            token = lex.GetNextToken();
        }
        
        return recur;
    }
//Non Terminals
    private int Program() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Program", true);
        if (token.code == lex.codeFor("PRGRM")) {
            token = lex.GetNextToken();
            recur = ProgIdentifier();
            if (token.code == lex.codeFor("SEMI")) {
                token = lex.GetNextToken();
                recur = Block();
                if (token.code == lex.codeFor("PRIOD")) {
                    if (!anyErrors) {
                        System.out.println("Success.");
                    } else {
                        System.out.println("Compilation failed.");
                    }
                } else {
                    error(lex.reserveFor("PRIOD"), token.lexeme);
                }
            } else {
                error(lex.reserveFor("SEMI"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("PRGRM"), token.lexeme);
        }
        trace("Program", false);
        return recur;
    }
//Non Terminal    
    private int Block() {
        int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Block", true);
        if (token.code == lex.codeFor("BEGIN")) {
            token = lex.GetNextToken();
            recur = Statement();
            //System.out.print(token.lexeme);
            while ((token.code == lex.codeFor("SEMI")) && (!lex.EOF()) && (!
            		anyErrors)) {
                token = lex.GetNextToken();
                recur = Statement();
            }
            if (token.code == lex.codeFor("END__")) {
                token = lex.GetNextToken();
            } else {
                error(lex.reserveFor("END__"), token.lexeme);
            }
        } else {
            error(lex.reserveFor("BEGIN"), token.lexeme);
        }
        trace("Block", false);
        return recur;
    }
//Not a NT, but used to shorten Statement code body   
    //<variable> $COLON-EQUALS <simple expression>
    private int handleAssignment() {
        int recur = 0;
        int left = 0;
        int right = 0;
        if (anyErrors) {
            return -1;
        }
        trace("handleAssignment", true);
        //have an ident already in order to get to here, handle as Variable
        
        recur = Variable();
       //Variable moves ahead, next token ready
        if (token.code == lex.codeFor("ASGN")) { // If there is an assign token, get the next token and jump into SimpleExpression
            token = lex.GetNextToken();
            right = SimpleExpression();
        } else { // Else, should not have entered this method and return error 
            error(lex.reserveFor("ASGN"), token.lexeme);
        }
        trace("handleAssignment", false);
        return right;
    }
    
// Enter Variable and get next token, exits variable 
    private int Variable() {
	int result = -1;
    if (anyErrors) {
        return -1;
    }
    trace("Variable", true);
    //If the token code is 50 (IDENT) then find the index that the current token is at in the symbol table and return that location 
    if(token.code == lex.codeFor("IDENT")) {
	    result = symbolList.LookupSymbol(token.lexeme);
		token = lex.GetNextToken();
    } else {
    	error("Variable", token.lexeme);
    }
	trace("Variable", false);
	return result;
	}

// SimpleExpression MUST BE 
//  COMPLETED TO IMPLEMENT CFG for <simple expression>
    private int SimpleExpression() {
        int recur = 0;
        int left = 0;
        int right = 0;
        int opCode = 0;
        int temp = 0;
        int signval = 0;
        
        if (anyErrors) {
            return -1;
        }
        trace("SimpleExpression", true);
        
        // Is there a sign value for this token?
        signval = sign();
        
        // <term> 
        left = Term();
        
        // Add a negation quad
        if(signval == -1) {
        	quadTable.AddQuad(2, left,Minus1Index, left);
        }
        
        //System.out.print(token.lexeme);
        // {<addOp> <term>}* 
        //Here we are adding terms together, and as they are found adding quads 
        while(isAddOp(token.code)) {
        	trace("Addop", true);
        	//Set Opcode for quad table
        	if(token.code == lex.codeFor("ADDIT")) {
        		opCode = 4;
        	} else if (token.code == lex.codeFor("SUBTR")) {
        		opCode = 3;
        	}
        	
        	token = lex.GetNextToken();
        	right = Term();
        	
        	//Add temp = GenerateSymbol, where a temp symbol is added to the symbol table
        	//temp = symbolList.AddSymbol("tempVar",'v',0);
        	temp = GenSymbol();
        	
        	quadTable.AddQuad(opCode, left, right, temp);
        	left = temp;
        	trace("Addop", false);
        }
        
        trace("SimpleExpression", false);
        return left;
    }
    
    public int sign(){
    	int result=1; //only move ahead if + or - found; optional sign
	     if (token.code==lex.codeFor("ADDIT")){
	    	 result = -1;
	    	 token = lex.GetNextToken();
	     } else if (token.code==lex.codeFor("SUBTR")) {
	    	 token = lex.GetNextToken();}
	     
	     return(result);
    }

    private int GenSymbol() {
    	String temp = "Temp" + Integer.toString(genCount);
    	genCount++;
    	symbolList.AddSymbol(temp, 'v', 0);
    	return symbolList.LookupSymbol(temp);
    	}
    // NT for Term ,which is called by factor and Simple Expression 
    private int Term() {
	
	int recur = 0;
	int left = 0;
	int right = 0;
	int temp = 0;
	int opCode = 0;
	
    if (anyErrors) {
        return -1;
    }
    trace("Term", true);
	
    // <factor>
    left = Factor();
    
    
    // {<mulop> <factor>}*
    while(isMulOp(token.code) && !anyErrors) {
    	trace("Mulop", true);
    	
    	if (token.code == lex.codeFor("MULTP")) {
    		opCode = 2;
    	} else if(token.code == lex.codeFor("DIVID")) {
    		opCode = 1;
    	}
    	
    	
    	token = lex.GetNextToken();
    	
    	right = Factor();
    	
    	//Add temp = GenerateSymbol, where a temp symbol is added to the symbol table
    	temp = symbolList.AddSymbol("tempVarTerm",'v',0);
    	
    	quadTable.AddQuad(opCode, left, right, temp);
    	left = temp;
    	
    	trace("Mulop", false);
    	//recur = Factor(); // Calls factor to get the number or variable or simple expression
    	
    }
    
    trace("Term", false);
	return left;
	}
    
    // NT that is called by term and mulOp
    private int Factor() {
    	int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Factor", true);
        
        if(token.code == lex.codeFor("OPAR")) {
        	token=lex.GetNextToken();
        	recur = SimpleExpression();
        	
        	if(token.code == lex.codeFor("CPAR")) {
        		token=lex.GetNextToken();
        		trace("Factor", false);
        		return recur;
        	}
        	
        } 
        
        if(token.code == lex.codeFor("SEMI") || token.code == lex.codeFor("IDENT")){
        	// <Variable>
        	//token = lex.GetNextToken();
        	recur = Variable();
        	trace("Factor", false);
        	return recur;
        }
        
        if(token.code != lex.codeFor("IDENT") && token.code != lex.codeFor("MULTP") && token.code != lex.codeFor("DIVID")) { // <Unsigned Constant> --> <Unsigned Number> --> $FLOAT | $INTEGER
        		//token = lex.GetNextToken();
        		recur = UnsignConstant();
        		trace("Factor", false);
        		return recur;
        } 
        
        //Error Handling if Factor receives the wrong token 
        error("IDENT",token.lexeme);
        anyErrors = true;
        trace("Factor", false);
		return recur;
	}

    //NT called by factor 
    private int UnsignConstant() {
		int recur = 0;
        if (anyErrors) {
            return -1;
        }
        trace("Unsigned Constant", true);
        
        //<Unsigned Number> --> $FLOAT | $INTEGER
        recur = UnsignNumber();
        
        trace("Unsigned Constant",false);
		return recur;
	}
    
    // NT called by UnsingedConstant 
    private int UnsignNumber() {
		int recur = 0;
        if (anyErrors) {
            return -1;
        }
        
        trace("Unsigned Number", true);

    	// float or int or ERROR
    	// unsigned constant starts with integer or float number
    	if ((token.code == lex.INTEGER_ID || (token.code == lex.FLOAT_ID))) {
    		
	    	// return the s.t. index
	    	recur = symbolList.LookupSymbol(token.lexeme);
	    	token = lex.GetNextToken();
    	} else {
    		error("Integer or Floating Point Number", token.lexeme);
    		}

        trace("Unsigned Number",false);		
        return recur;
	}

    // Test for mult / divide operation 
    private boolean isMulOp(int code) {
		
		// Check to see if the token code is * or / 
    	if(code == 30 || code == 31) {
			
    		return true;
		}
    	
    	return false;
	}

    //Test for addition / subtraction operations 
    private boolean isAddOp(int code) {
		
		//Check to see id token code is + or - 
		if(code == 33 || code == 32) {
			
			return true;
		}
		
		return false;
	}
	//Non Terminal    
    private int Statement() {
        int recur = 0;
        int left = 0;
        int right = 0;
        int saveTop = 0;
        int branchQuad = 0;
        
        if (anyErrors) {
            return -1;
        }
        trace("Statement", true);
        
        // Block Body 
        if(token.code == lex.codeFor("BEGIN")) {
        	recur = Block();
        }
        
        // If printline 
        if(token.code == lex.codeFor("PRNTL")) {
        	recur = handlePrintln();}
        
        // If readLine
        if (token.code == lex.codeFor("READL")) {
        	recur = handleReadln();}
        
        // If Identifier
        if (token.code == lex.codeFor("IDENT")) {  //must be an ASSIGNMENT
        	
        	left = Variable();
        	
        	if(token.code == lex.codeFor("ASGN")) {
        		token = lex.GetNextToken();
        		right = SimpleExpression();
        		quadTable.AddQuad(5, right, 0, left);}
        }
        
        // If statements 
        if (token.code == lex.codeFor("IF___")) { 
        	
        	token = lex.GetNextToken();
        	branchQuad = relExpression();
        	
        	if(token.code == lex.codeFor("THEN_")) {
        		token = lex.GetNextToken();
        		recur = Statement();
        		
        		if(token.code == lex.codeFor("ELSE_")) {
        			token = lex.GetNextToken();
        			patchElse = quadTable.NextQuad();
        			
        			quadTable.AddQuad(8, 0, 0, 0);
        			quadTable.UpdateQuad(branchQuad, quadTable.GetQuad(branchQuad, 0), quadTable.GetQuad(branchQuad, 1),
                			quadTable.GetQuad(branchQuad, 2), quadTable.NextQuad());
        			
        			recur = Statement();
        			
        			quadTable.UpdateQuad(patchElse, quadTable.GetQuad(patchElse, 0), quadTable.GetQuad(patchElse, 1),
                			quadTable.GetQuad(patchElse, 2), quadTable.NextQuad());
        		} else {
        			quadTable.UpdateQuad(branchQuad, quadTable.GetQuad(branchQuad, 0), quadTable.GetQuad(branchQuad, 1),
                			quadTable.GetQuad(branchQuad, 2), quadTable.NextQuad());
        		}
        	} else {
        		error("THEN",token.lexeme);
        	}
        } 
        
        // Do While statements 
        if(token.code == lex.codeFor("DOWHL")) {
        	
        	trace("handleWhile", true);

        	token = lex.GetNextToken();
        	
        	// Save the top of the loop where the code will jump
        	saveTop = quadTable.NextQuad();
        	
        	// Where to set branch target 
        	branchQuad = relExpression();
        	
        	//Process the loop body
        	recur = Statement();
        	
        	//Jump to top of loop
        	quadTable.AddQuad(8, 0, 0, saveTop);
        	
        	// Quad function for ease --> set OP3 ... Conditional jumps nextQuad 
        	quadTable.UpdateQuad(branchQuad, quadTable.GetQuad(branchQuad, 0), quadTable.GetQuad(branchQuad, 1),
        			quadTable.GetQuad(branchQuad, 2), quadTable.NextQuad());
        	
        	trace("handleWhile", false);

        }
        
        trace("Statement", false);
        return recur;
    }
    
    private int handlePrintln() {

    	int recur = 0;
    	int toprint = 0;
    	if (anyErrors) {
    	return -1;
    	}
    	trace("handlePrintln", true);
    	
    	//got here from a PRINT token, move past it...
    	token = lex.GetNextToken();
    	
    	//look for ( stringconst, ident, simpleexp )
    	if (token.code == lex.codeFor("OPAR")) {
    		
    	//move on
    	token = lex.GetNextToken();
    	
    	if ((token.code == lex.STRING_ID) || (token.code == lex.IDENT_ID)) {
    		
	    	// save index for string literal or identifier
	    	toprint = symbolList.LookupSymbol(token.lexeme);
	    	
	    	//move on
	    	token = lex.GetNextToken();
    	} else {
    		toprint = SimpleExpression();
    	}
    	
    	//Print op code == 6
    	quadTable.AddQuad(6, toprint, 0, 0);
    	
    	//now need right ")"
    	if (token.code == lex.codeFor("CPAR")) {
    		
	    	//move on
	    	token = lex.GetNextToken();
    	} else {
    		error(lex.reserveFor("CPAR"), token.lexeme);
    	}
    	} else {
    		error(lex.reserveFor("OPAR"), token.lexeme);
    	}
    	// end lpar group
    	
    	trace("handlePrintn", false);
    	return recur;
    	}
    
    public int handleReadln() {
    	
    	int recur = 0;
    	int toread = 0;
    	if (anyErrors) {
    	return -1;
    	}
    	trace("handleReadln", true);
    	
    	//got here from a PRINT token, move past it...
    	token = lex.GetNextToken();
    	
    	//look for ( stringconst, ident, simpleexp )
    	if (token.code == lex.codeFor("OPAR")) {
    		
    	//move on
    	token = lex.GetNextToken();
    	
    	toread = Variable();
    	
    	//read line op code == 7
    	quadTable.AddQuad(7, 0, 0, toread);
    	
    	//now need right ")"
    	if (token.code == lex.codeFor("CPAR")) {
    		
	    	//move on
	    	token = lex.GetNextToken();
    	} else {
    		error(lex.reserveFor("CPAR"), token.lexeme);
    	}
    	} else {
    		error(lex.reserveFor("OPAR"), token.lexeme);
    	}
    	// end lpar group
    	
    	trace("handleReadln", false);
    	return recur;
    }

    public int relopToOpcode(int relop) {
    	
    	int result = 0;
    	
    	switch(relop){
    		case 38: // Greater 
    			result = 13;
    			break;
    		
    		case 39: // Less 
    			result = 14;
    			break;
    			
    		case 40: // Greater Equal 
    			result = 11;
    			break;
    			
    		case 41: // Less Equal 
    			result = 10;
    			break;
    			
    		case 42:// EQLS
    			result = 12;
    			break;
    		
    		case 43: // Not Equal 
    			result = 9;
    			break;
    	}
    	
    	return result;
    }
    
    public int relExpression() {
    	
    	// Variable declarations
    	int left = 0;
    	int right = 0;
    	int saveRelop = 0;
    	int result = 0;
    	int temp = 0;
    	
    	//Simple Expression Call 
    	left = SimpleExpression();
    	
    	// Relop Call --> returns token code of the relational operator 
    	saveRelop = relOp();
    	
    	token = lex.GetNextToken();
    	
    	//Simple Expression Call 
    	right = SimpleExpression();
    	
    	// Create temp variable and save to symbol table 
    	temp = GenSymbol();
    	
    	//Compare with subtraction quad 
    	quadTable.AddQuad(3, left, right, temp);
    	
    	result = quadTable.NextQuad();
    	
    	// Target is unset 
    	quadTable.AddQuad(relopToOpcode(saveRelop), temp, 0, 0);
    	return result;
    }
    
    public int relOp() {
    	
    	int result = 0;
    	// return token code of relational operator 
    	if(token.code == 38 || token.code == 39 || token.code == 40 || token.code == 41 || token.code == 42 ||token.code ==  43) {
    		return token.code;
    	}
    	
		return result;
    }
    
    /**
 * *************************************************
*/
    /*     UTILITY FUNCTIONS USED THROUGHOUT THIS CLASS */
// error provides a simple way to print an error statement to standard output
//     and avoid reduncancy
    private void error(String wanted, String got) {
        anyErrors = true;
        System.out.println("ERROR: Expected " + wanted + " but found " + got);
    }
// trace simply RETURNs if traceon is false; otherwise, it prints an
    // ENTERING or EXITING message using the proc string
    private void trace(String proc, boolean enter) {
        String tabs = "";
        if (!traceon) {
            return;
        }
        if (enter) {
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("--> Entering " + proc);
            level++;
        } else {
            if (level > 0) {
                level--;
            }
            tabs = repeatChar(" ", level);
            System.out.print(tabs);
            System.out.println("<-- Exiting " + proc);
        }
    }
// repeatChar returns a string containing x repetitions of string s; 
//    nice for making a varying indent format
    private String repeatChar(String s, int x) {
        int i;
        String result = "";
        for (i = 1; i <= x; i++) {
            result = result + s;
        }
        return result;
    }
   
}
