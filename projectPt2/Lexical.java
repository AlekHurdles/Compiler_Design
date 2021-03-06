package projectPt2;
import ADT.*;

/*
The following code is provided by the instructor for the completion of PHASE 2 
of the compiler project for CS4100.

STUDENTS ARE TO PROVIDE THE FOLLOWING CODE FOR THE COMPLETION OF THE ASSIGNMENT:

1) Initialize the 2 reserve tables, which are fields in the Lexical class,
   named reserveWords and mnemonics.  Create the following functions,
   whose calls are in the lexical constructor:
       initReserveWords(reserveWords);
       initMnemonics(mnemonics);

2) 


*/


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
*
* @author abrouill
*/
import java.io.*;

public class Lexical {

   private File file;                        //File to be read for input
   private FileReader filereader;            //Reader, Java reqd
   private BufferedReader bufferedreader;    //Buffered, Java reqd
   private String line;                      //Current line of input from file   
   private int linePos;                      //Current character position
   //  in the current line
   private SymbolTable saveSymbols;          //SymbolTable used in Lexical
   //  sent as parameter to construct
   private boolean EOF;                      //End Of File indicator
   private boolean echo;                     //true means echo each input line
   private boolean printToken;               //true to print found tokens here
   private int lineCount;                    //line #in file, for echo-ing
   private boolean needLine;                 //track when to read a new line
   //Tables to hold the reserve words and the mnemonics for token codes
   private ReserveTable reserveWords = new ReserveTable(50); //a few more than # reserves
   private ReserveTable mnemonics = new ReserveTable(50); //a few more than # reserves
//global char
   char currCh;

   //constructor
   public Lexical(String filename, SymbolTable symbols, boolean echoOn) {
       saveSymbols = symbols;  //map the initialized parameter to the local ST
       echo = echoOn;          //store echo status
       lineCount = 0;          //start the line number count
       line = "";              //line starts empty
       needLine = true;        //need to read a line
       printToken = false;     //default OFF, do not print tokesn here
       //  within GetNextToken; call setPrintToken to
       //  change it publicly.
       linePos = -1;           //no chars read yet
       //call initializations of tables
       initReserveWords(reserveWords);
       initMnemonics(mnemonics);

       //set up the file access, get first character, line retrieved 1st time
       try {
           file = new File(filename);    //creates a new file instance  
           filereader = new FileReader(file);   //reads the file  
           bufferedreader = new BufferedReader(filereader);  //creates a buffering character input stream  
           EOF = false;
           currCh = GetNextChar();
       } catch (IOException e) {
           EOF = true;
           e.printStackTrace();
       }
   }

   // token class is declared here, no accessors needed
   public class token {

       public String lexeme;
       public int code;
       public String mnemonic;

       token() {
           lexeme = "";
           code = 0;
           mnemonic = "";
       }
   }

   private final int UNKNOWN_CHAR = 99;
   public final int _GRTR = 38;
   public final int _LESS = 39;
   public final int _GREQ = 40;
   public final int _LEEQ = 41;
   public final int _EQLS = 42;
   public final int _NEQL = 43;

   public final int IDENT_ID = 50;
   public final int INTEGER_ID = 51;
   public final int FLOAT_ID = 52;
   public final int STRING_ID = 53;

   /* @@@ These are nice for syntax to call later 
   // given a mnemonic, find its token code value
   public int codeFor(String mnemonic) {
       return mnemonics.LookupName(mnemonic);
   }
   // given a mnemonic, return its reserve word
   public String reserveFor(String mnemonic) {
       return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
   }
    */
   // Public access to the current End Of File status
   public boolean EOF() {
       return EOF;
   }

   // DEBUG enabler, turns on token printing inside of GetNextToken
   public void setPrintToken(boolean on) {
       printToken = on;
   }
   
   

   /* @@@ */
   private void initReserveWords(ReserveTable reserveWords) {
       reserveWords.Add("GOTO", 0);
       reserveWords.Add("INTEGER", 1);
       reserveWords.Add("TO", 2);
       reserveWords.Add("DO", 3);
       reserveWords.Add("IF", 4);
       reserveWords.Add("THEN", 5);
       reserveWords.Add("ELSE", 6);
       reserveWords.Add("FOR", 7);
       reserveWords.Add("OF", 8);
       reserveWords.Add("PRINTLN", 9);
       reserveWords.Add("READLN", 10);
       reserveWords.Add("BEGIN", 11);
       reserveWords.Add("END", 12);
       reserveWords.Add("VAR", 13);
       reserveWords.Add("DOWHILE", 14);
       reserveWords.Add("PROGRAM", 15);
       reserveWords.Add("LABEL", 16);
       reserveWords.Add("REPEAT", 17);
       reserveWords.Add("UNTIL", 18);
       reserveWords.Add("PROCEDURE", 19);
       reserveWords.Add("DOWNTO", 20);
       reserveWords.Add("FUNCTION", 21);
       reserveWords.Add("RETURN", 22);
       reserveWords.Add("FLOAT", 23);
       reserveWords.Add("STRING", 24);
       reserveWords.Add("ARRAY", 25);

       // add 1 and 2-char tokens here
       reserveWords.Add("/", 30);
       reserveWords.Add("*", 31);
       reserveWords.Add("+", 32);
       reserveWords.Add("-", 33);
       reserveWords.Add("(", 34);
       reserveWords.Add(")", 35);
       reserveWords.Add(";", 36);
       reserveWords.Add(":=", 37);
       reserveWords.Add(">", 38);
       reserveWords.Add("<", 39);
       reserveWords.Add(">=", 40);
       reserveWords.Add("<=", 41);
       reserveWords.Add("=", 42);
       reserveWords.Add("<>", 43);
       reserveWords.Add(",", 44);
       reserveWords.Add("[", 45);
       reserveWords.Add("]", 46);
       reserveWords.Add(":", 47);
       reserveWords.Add(".", 48);

       reserveWords.Add("UNKNOWN", 99);

       

   }

   /* @@@ */
   private void initMnemonics(ReserveTable mnemonics) {
//add 5-character student created mnemonics corresponding to reserve values
       mnemonics.Add("GOTO_", 0);
       mnemonics.Add("INTGE", 1);
       mnemonics.Add("TO___", 2);
       mnemonics.Add("DO___", 3);
       mnemonics.Add("IF___", 4);
       mnemonics.Add("THEN_", 5);
       mnemonics.Add("ELSE_", 6);
       mnemonics.Add("FOR__", 7);
       mnemonics.Add("OF___", 8);
       mnemonics.Add("PRNTL", 9);
       mnemonics.Add("READL", 10);
       mnemonics.Add("BEGIN", 11);
       mnemonics.Add("END__", 12);
       mnemonics.Add("VAR__", 13);
       mnemonics.Add("DOWHL", 14);
       mnemonics.Add("PRGRM", 15);
       mnemonics.Add("LABEL", 16);
       mnemonics.Add("RPEAT", 17);
       mnemonics.Add("UNTIL", 18);
       mnemonics.Add("PRCDR", 19);
       mnemonics.Add("DWNTO", 20);
       mnemonics.Add("FNCTN", 21);
       mnemonics.Add("RETRN", 22);
       mnemonics.Add("FLOAT", 23);
       mnemonics.Add("STRNG", 24);
       mnemonics.Add("ARRAY", 25);

       mnemonics.Add("DIVID", 30);
       mnemonics.Add("MULTP", 31);
       mnemonics.Add("ADDIT", 32);
       mnemonics.Add("SUBTR", 33);
       mnemonics.Add("OPAR", 34);
       mnemonics.Add("CPAR", 35);
       mnemonics.Add("SEMI", 36);
       mnemonics.Add("ASGN", 37);
       mnemonics.Add("GRTR", 38);
       mnemonics.Add("LESS", 39);
       mnemonics.Add("GRTEQ", 40);
       mnemonics.Add("LSSEQ", 41);
       mnemonics.Add("EQUAL", 42);
       mnemonics.Add("NEQL", 43);
       mnemonics.Add("COMA", 44);
       mnemonics.Add("OBRKT", 45);
       mnemonics.Add("CBRKT", 46);
       mnemonics.Add("COLON", 47);
       mnemonics.Add("PRIOD", 48);
       mnemonics.Add("IDENT", IDENT_ID);
       mnemonics.Add("UNKWN", 99);

   }

   // Character category for alphabetic chars, upper and lower case
   private boolean isLetter(char ch) {
       return (((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z')));
   }

   // Character category for 0..9 
   private boolean isDigit(char ch) {
       return ((ch >= '0') && (ch <= '9'));
   }

   // Category for any whitespace to be skipped over
   // space, tab, and newline
   private boolean isWhitespace(char ch) {
       return ((ch == ' ') || (ch == '\t') || (ch == '\n'));
   }

   // Returns the VALUE of the next character without removing it from the
   //    input line.  Useful for checking 2-character tokens that start with
   //    a 1-character token.
   private char PeekNextChar() {
       char result = ' ';
       if ((needLine) || (EOF)) {
           result = ' '; //at end of line, so nothing
       } else // 
       {
           if ((linePos + 1) < line.length()) { //have a char to peek
               result = line.charAt(linePos + 1);
           }
       }
       return result;
   }

   // Called by GetNextChar when the characters in the current line
   // buffer string (line) are used up.
   private void GetNextLine() {
       try {
           line = bufferedreader.readLine();  //returns a null string when EOF
           if ((line != null) && (echo)) {
               lineCount++;
               System.out.println(String.format("%04d", lineCount) + " " + line);
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
       if (line == null) {    // The readLine returns null at EOF, set flag
           EOF = true;
       }
       linePos = -1;      // reset vars for new line if we have one
       needLine = false;  // we have one, no need
       //the line is ready for the next call to get a char with GetNextChar
   }

   // Returns the next character in the input file, returning a 
   // /n newline character at the end of each input line or at EOF
   public char GetNextChar() {
       char result;
       if (needLine) //ran out last time we got a char, so get a new line
       {
           GetNextLine();
       }
       //try to get char from line buff
       // if EOF there is no new char, just return endofline, DONE
       if (EOF) {
           result = '\n';
           needLine = false;
       } else {
           // if there are more characters left in the input buffer
           if ((linePos < line.length() - 1)) { //have a character available
               linePos++;
               result = line.charAt(linePos);
           } else {
               //need a new line, but want to return eoln on this call first
               result = '\n';
               needLine = true; //will read a new line on next GetNextChar call
           }
       }
       return result;
   }

   final char comment_start1 = '{';
   final char comment_end1 = '}';
   final char comment_start2 = '(';
   final char comment_startend = '*';
   final char comment_end2 = ')';

   // skips over any comment as if it were whitespace, so it is ignored
   public char skipComment(char curr) {
       // if this is the start of a comment...
       if (curr == comment_start1) {
           curr = GetNextChar();
           // loop until the end of comment or EOF is reached
           while ((curr != comment_end1) && (!EOF)) {
               curr = GetNextChar();
           }
           // if the file ended before the comment terminated
           if (EOF) {
               System.out.println("Comment not terminated before End Of File");
           } else {
               // keep getting the next char
               curr = GetNextChar();
           }
       } else {
           // this is for the 2-character comment start, different start/end
           if ((curr == comment_start2) && (PeekNextChar() == comment_startend)) {
               curr = GetNextChar(); // get the second
               curr = GetNextChar(); // into comment or end of comment
               //while comment end is not reached
               while ((!((curr == comment_startend) && (PeekNextChar() == comment_end2))) && (!EOF)) {
                   curr = GetNextChar();
               }
               // EOF before comment end
               if (EOF) {
                   System.out.println("Comment not terminated before End Of File");
               } else {
                   curr = GetNextChar();          //must move past close
                   curr = GetNextChar();          //must get following
               }
           }

       }
       return (curr);
   }

   //reads past any white space, blank lines, comments
   public char skipWhiteSpace() {

       do {
           while ((isWhitespace(currCh)) && (!EOF)) {
               currCh = GetNextChar();
           }
           currCh = skipComment(currCh);
       } while (isWhitespace(currCh) && (!EOF));
       return currCh;
   }

   // returns TRUE if ch is a prefix to a 2-character token like := or <=
   private boolean isPrefix(char ch) {
       return ((ch == ':') || (ch == '<') || (ch == '>'));
   }

   // returns TRUE if ch is the string delmiter
   private boolean isStringStart(char ch) {
       return ch == '"';
   }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//Student supplied methods
   private token getIdent() {

	   //int lookup = IDENT;
	   token result = new token();
	   result.lexeme = "" + currCh; //have the first char
	   currCh = GetNextChar();
	   while (isLetter(currCh)||(isDigit(currCh)||(currCh == '$')||(currCh=='_'))) {
	   result.lexeme = result.lexeme + currCh; //extend lexeme
	   currCh = GetNextChar();
	   }
	   // end of token, lookup or IDENT
	   int lookup = reserveWords.LookupName(result.lexeme);
	   
	   //The lexeme is in the reserve table
	   if (lookup != -1) {
		   result.code = reserveWords.LookupName(result.lexeme);
		   result.mnemonic = mnemonics.LookupCode(result.code);
	   	   
	   	   return result;
	   	   } else { // If lexeme is not in the reserve table; IDENT
	   		   result.code = IDENT_ID;
	    	   result.mnemonic = "IDENT";
	   	   }
     
	   return result;
   }

   private token getNumber() {
       /* a number is:   <digit>+[.<digit>*[E[+|-]<digit>+]] */
	   
	   token result = new token();
	   result.lexeme = "" + currCh; //have the first char
	   currCh = GetNextChar();
	   while (isDigit(currCh)) {
	   result.lexeme = result.lexeme + currCh; //extend lexeme
	   currCh = GetNextChar();
	   }
	   if(currCh == '.') {
		   result.lexeme = result.lexeme + currCh; //extend lexeme
		   currCh = GetNextChar();
		   while (isDigit(currCh)) {
			   result.lexeme = result.lexeme + currCh; //extend lexeme
			   currCh = GetNextChar();
			   }
		   if(currCh == 'E') {
			   result.lexeme = result.lexeme + currCh; //extend lexeme
			   currCh = GetNextChar();
			   
			   if((currCh == '+') || (currCh == '-')) {
				   result.lexeme = result.lexeme + currCh; //extend lexeme
				   currCh = GetNextChar();
				   while (isDigit(currCh)) {
					   result.lexeme = result.lexeme + currCh; //extend lexeme
					   currCh = GetNextChar();
					   }
			   }
			   
			   while (isDigit(currCh)) {
				   result.lexeme = result.lexeme + currCh; //extend lexeme
				   currCh = GetNextChar();
				   }
		   }
	   }
	   
	   
	   //Set float or integer ID
	   if(result.lexeme.contains(".")==true) {
		   result.code = FLOAT_ID;
		   result.mnemonic = "FLOAT";
		   
//		   //Truncate if longer than 12 chars, and save the truncated string in the symbol table 
//		   if(result.lexeme.length() > 12) {
//			   String truncatedFloat = result.lexeme.substring(0,12);
//			   System.out.print("Float length > 12, truncated " + result.lexeme + "to " + truncatedFloat +"\n");
//		   }
		   
	   } else {
		   result.code = INTEGER_ID;
		   result.mnemonic = "INTGR";
		   
	   }
	   
	   return result;
   }

   private token getString() {
	   token result = new token();
	   result.lexeme = "";
	   currCh = GetNextChar();
	   
	   while (currCh != '"') {
		   
		   if(needLine == true) {
			   result.code = UNKNOWN_CHAR;
			   result.mnemonic = "UNKWN";
			   System.out.print("ERROR: Unterminated String\n");
			   currCh = GetNextChar();
			   return result;
		   }
	   result.lexeme = result.lexeme + currCh; //extend lexeme
	   currCh = GetNextChar();
	   }
	   result.code = STRING_ID;
	   result.mnemonic = "STRING";
	   currCh = GetNextChar();
	   return result;
   }

   private token getOneTwoChar() {
       
	   token result = new token();
	   result.lexeme = "" + currCh; //have the first char
	   
	   //returns -1 if the char is not in the reserve table 
	   int oneChar = reserveWords.LookupName(result.lexeme);
	   
	   //First char in the symbol
	   char firstChar = currCh;
	   
	 //is one char in the reserve table? 
	   if(oneChar == -1) {
		   
		   result.code = UNKNOWN_CHAR;
		   result.mnemonic = mnemonics.LookupCode(result.code);
		   currCh = GetNextChar();
		   return result;
		   
	   } else { // if the first char is in the reserve table 
		   
		   // Check for := , <> , <= combinations as 2char
		   
		 //if the first char is in the reserve array, and not : or < return the code and mnemonic
		   if(firstChar != ':' && firstChar != '<') {
			   
			   result.code = reserveWords.LookupName(String.valueOf(firstChar));
			   result.mnemonic = mnemonics.LookupCode(result.code);
			   
			   currCh = GetNextChar();
			   return result;
		   }
		   
		   
		   //So if the first char is : or < check for the other half of the potential 2char 
		   if(firstChar == ':') {
			   currCh = GetNextChar();
			   
			   String twoCharacters = result.lexeme + currCh;
			   
			 //check if these 2 characters match :=
			   if(currCh == '=') {
				   
				   result.lexeme = result.lexeme + currCh;
				   result.code = reserveWords.LookupName(result.lexeme);
				   result.mnemonic = mnemonics.LookupCode(result.code);
				   
				   currCh = GetNextChar();
				   return result;
				   
			   } else if (twoCharacters.contentEquals(":=")==false) {
				   result.code = reserveWords.LookupName(String.valueOf(firstChar));
				   result.mnemonic = mnemonics.LookupCode(result.code);
				   
				   currCh = GetNextChar();
				   return result;
			   }
			   
		   }
		   
		   if(firstChar == '<') {
			   currCh = GetNextChar();
			   
			   String twoCharacters = result.lexeme + currCh;
			   
			 //check if these 2 characters match <=
			   if(currCh == '=') {
				   
				   result.lexeme = result.lexeme + currCh;
				   result.code = reserveWords.LookupName(result.lexeme);
				   result.mnemonic = mnemonics.LookupCode(result.code);
				   
				   currCh = GetNextChar();
				   return result;
				   
			   } else if (currCh == '>') {
				   result.lexeme = result.lexeme + currCh;
				   result.code = reserveWords.LookupName(result.lexeme);
				   result.mnemonic = mnemonics.LookupCode(result.code);
				   
				   currCh = GetNextChar();
				   return result;
			   } else {
				   result.code = reserveWords.LookupName(String.valueOf(firstChar));
				   result.mnemonic = mnemonics.LookupCode(result.code);
				   
				   currCh = GetNextChar();
				   return result;
			   }
			   
		   }
   
	   }
	   
	   return result;
   }
	   

   public token checkTruncate(token result) {
       // truncate long lexemes, validate doubles and integers
       // handle appropriate types and add to symbol table as needed
       switch (result.code) {
           case IDENT_ID:
        	   
        		 //Truncate the string if longer than 30, and save the truncated string in the symbol table 
        		   if(result.lexeme.length() > 20) {
        			   String truncatedIdentifier = result.lexeme.substring(0,20);
        			   saveSymbols.AddSymbol(truncatedIdentifier, 'V', truncatedIdentifier);
        			   System.out.print("Integer length > 20, truncated " + result.lexeme + "to " + truncatedIdentifier +"\n");
        		   } else {
        			   saveSymbols.AddSymbol(result.lexeme, 'V', "0");
        		   }
               break;
           case INTEGER_ID:
        	 //Truncate the int if longer than 6, and save the truncated int in the symbol table 
    		   if(result.lexeme.length() > 6) {
    			   String truncatedInt = result.lexeme.substring(0,6);
    			   saveSymbols.AddSymbol(truncatedInt, 'C', Integer.valueOf(truncatedInt));
    			   System.out.print("Integer length > 6, truncated " + result.lexeme + "to " + truncatedInt +"\n");
    		   } else {
    			   saveSymbols.AddSymbol(result.lexeme, 'C', Integer.valueOf(result.lexeme));
    		   }
               break;
           case FLOAT_ID:
        	 //Truncate the Float if longer than 12, and save the truncated Float in the symbol table 
    		   if(result.lexeme.length() > 12) {
    			   String truncatedFloat = result.lexeme.substring(0,12);
    			   saveSymbols.AddSymbol(truncatedFloat, 'C', Float.valueOf(truncatedFloat));
    			   System.out.print("Integer length > 6, truncated " + result.lexeme + "to " + truncatedFloat +"\n");
    		   } else {
    			   saveSymbols.AddSymbol(result.lexeme, 'C', Float.valueOf(result.lexeme));
    		   }
               break;
           case STRING_ID:
        	   //Save string to symbol table
    		   saveSymbols.AddSymbol(result.lexeme, 'C', result.lexeme);
    		   
               break;
           default:
               break; //don't add                           
       }

       return result;
   }

//End of student supplied methods
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//numberic validation freebie code!
   // Checks to see if a string contains a valid DOUBLE 
   public boolean doubleOK(String stin) {
       boolean result;
       Double x;
       try {
           x = Double.parseDouble(stin);
           result = true;
       } catch (NumberFormatException ex) {
           result = false;
       }
       return result;
   }

   // Checks the input string for a valid INTEGER
   public boolean integerOK(String stin) {
       boolean result;
       int x;
       try {
           x = Integer.parseInt(stin);
           result = true;
       } catch (NumberFormatException ex) {
           result = false;
       }
       return result;
   }

//Main method of Lexical
   public token GetNextToken() {
       token result = new token();
       
       currCh = skipWhiteSpace();
       if (isLetter(currCh)) { //is ident
           result = getIdent();
       } else if (isDigit(currCh)) { //is numeric
           result = getNumber();
       } else if (isStringStart(currCh)) { //string literal
           result = getString();
       } else //default char checks
       {
           result = getOneTwoChar();
       }

       if ((result.lexeme.equals("")) || (EOF)) {
           result = null;
       }
//set the mnemonic
       if (result != null) {
//THIS LINE REMOVED-- PUT BACK IN TO USE LOOKUP            result.mnemonic = mnemonics.LookupCode(result.code);
           result = checkTruncate(result);
           if (printToken) {
               System.out.println("\t" + result.mnemonic + " | \t" + String.format("%04d", result.code) + " | \t" + result.lexeme);
           }
       }
       
       if (printToken) {
    	   System.out.println("\t" + result.mnemonic + " | \t" + 
    	   String.format("%04d", result.code) + " | \t" + result.lexeme);
    	   }
       
       return result;

   }

   //Returns the integer token code for the given mnemonic 
   public int codeFor(String mnemonic){
       return mnemonics.LookupName(mnemonic);
   }
   //Returns the Reserve Word for the given mnemonic
   public String reserveFor(String mnemonic){
       return reserveWords.LookupCode(mnemonics.LookupName(mnemonic));
   }
   

}

