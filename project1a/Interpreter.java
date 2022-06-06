package project1a;
import java.util.Arrays;
import java.util.Scanner;

import ADT.*;

public class Interpreter{
    
    private ReserveTable optable;  //This is the lookup table for the opcodes to be printed out
    
    public Interpreter (){
        //init reserve
        optable = new ReserveTable(17);  //there are 17 opcodes, 0..16
        initReserve(optable);  // this is a private method in my interpreter class which loads the reserve table with opcode mnemonics
    }

    private void initReserve(ReserveTable optable){      
    
    	// Initializes a reserve table with 16 op-codes
    	optable.Add("STOP", 0);
        optable.Add("DIV", 1);
        optable.Add("MUL", 2);
        optable.Add("SUB", 3);
        optable.Add("ADD", 4);
        optable.Add("MOV", 5);
        optable.Add("PRINT", 6);
        optable.Add("READ", 7);
        optable.Add("JMP", 8);
        optable.Add("JZ", 9);
        optable.Add("JP", 10);
        optable.Add("JN", 11);
        optable.Add("JNZ", 12);
        optable.Add("JNP", 13);
        optable.Add("JNN", 14);
        optable.Add("JINDR", 15);
        }


        public boolean initializeFactorialTest(SymbolTable stable, QuadTable qtable){

        	// Create symbol table for factorial computation program
            stable.AddSymbol("n", 'v', 10);
            stable.AddSymbol("i", 'v', 0);
            stable.AddSymbol("product",'v', 0);
            stable.AddSymbol("1",'c', 1);
            stable.AddSymbol("$temp", 'v', 0);

            // Create Quad table with calls needed for the factorial computation, using the op-codes from the reserve table
            qtable.AddQuad(5, 3, 0, 2);
            qtable.AddQuad(5, 3, 0, 1);
            qtable.AddQuad(3, 1, 0, 4);
            //JP
            qtable.AddQuad(10, 4, 0, 7);
            qtable.AddQuad(2, 2, 1, 2);
            qtable.AddQuad(4, 1, 3, 1);
            //JMP
            qtable.AddQuad(8, 0, 0, 2);
            //PRINT
            qtable.AddQuad(6, 2, 0, 0);    
            qtable.AddQuad(0, 0, 0, 0);

            return true;
        }

        public boolean initializeSummationTest(SymbolTable stable, QuadTable qtable ){

        	// Create the symbol table for the summation program
            stable.AddSymbol("n", 'v', 11);
            stable.AddSymbol("i", 'v', 0);
            stable.AddSymbol("sum", 'v', 0);
            stable.AddSymbol("1", 'c', 1);
            stable.AddSymbol("$temp", 'v', 0);
            stable.AddSymbol("0", 'c', 0);
            
            // Create the Quad table for summation program, using op-codes from reserve table 
            qtable.AddQuad(5, 5, 0, 2);
            qtable.AddQuad(5, 3, 0, 1);
            qtable.AddQuad(3, 1, 0, 4);
            //JP
            qtable.AddQuad(10, 4, 0, 7);
            qtable.AddQuad(4, 2, 1, 2);
            qtable.AddQuad(4, 1, 3, 1);
            //JMP
            qtable.AddQuad(8, 0, 0, 2);
            //PRINT
            qtable.AddQuad(6, 2, 0, 0); 
            qtable.AddQuad(0, 0, 0, 0);


            return true;
        }
        public String makeTraceString(int pc, int opcode, int op1, int op2, int op3){
        	// Result string is output to the console as the PC increments to track the progress of the program. 
            String result= "";
            
            // Output Example: PC =0004: MUL   02,01,02 
            result = "PC =" + String.format("%04d", pc)+": " + (optable.LookupCode(opcode)+"   ")+String.format("%02d", op1)+
                        ","+String.format("%02d", op2)+","+String.format("%02d", op3);
            
            return result;
        }

        public void InterpretQuads(QuadTable Q, SymbolTable S, boolean TraceOn, String filePath){
        	
        	// Initialize the variables needed for the point counter, op-codes, and operands 
            int PC, OP1, OP2, OP3;
            int OPCODE;
            
            PC=0;
            while(PC < Q.GetSize()){

                //Get the Quad at Index PC
                OPCODE =Q.GetQuad(PC, 0);
                OP1 = Q.GetQuad(PC, 1);
                OP2 = Q.GetQuad(PC, 2);
                OP3 = Q.GetQuad(PC, 3);

                if(TraceOn==true){
                    System.out.println(makeTraceString(PC, OPCODE, OP1, OP2, OP3));
                }
                
                //If the opcode value is in the reserve table
                if(OPCODE>=0 && OPCODE<17){
                    
                    switch(OPCODE){
                        //STOP
                        case 0:
                            System.out.print("\nExecution terminated by program stop.\n\n");
                            PC = 500;
                            break;
                            
                        //DIV  
                        case 1:
                        	if(S.GetInteger(OP2) != 0)
                        		S.UpdateSymbol(OP3, 'i', S.GetInteger(OP1) / S.GetInteger(OP2));
                        	
                        	PC = PC +1;
                        	break;
                        
                        //MUL
                        case 2:
                        	S.UpdateSymbol(OP3, 'i', S.GetInteger(OP1) * S.GetInteger(OP2));
                        	PC = PC +1;
                        	break;

                        //SUB
                        case 3:
                            
                            S.UpdateSymbol(OP3, 'i', S.GetInteger(OP1)-S.GetInteger(OP2));
                            PC = PC + 1;
                            break;

                        //ADD
                        case 4:
                            S.UpdateSymbol(OP3, 'i', S.GetInteger(OP1)+S.GetInteger(OP2));
                            PC = PC +1;
                            break;

                        //MOV
                        case 5:
                            S.UpdateSymbol(OP3, 'v', S.GetInteger(OP1));
                            PC = PC +1;
                            break;         
                            
                        //PRINT
                        case 6:
                        	if (S.GetKind(OP1) == 'i') {
                        		System.out.println(S.GetInteger(OP1));
                        	} else 
                        		System.out.println(S.GetString(OP1));
                            PC = PC + 1;
                            break;
                            
                         // ReadLn
                        case 7:
                        	//create scanner 
                        	Scanner sc = new Scanner(System.in);
                        	
                        	// prompt user 
                        	System.out.print('>');
                        	
                        	// Read one integer only
                        	int readval = sc.nextInt();
                        	
                        	// Op3 has the ST index we need, update it
                        	 
                        	S.UpdateSymbol(OP3,'v',readval);
                        	
                        	// Deallocate the scanner
                        	sc = null;
                        	PC = PC +1;
                        	break;
                        	
                        //JMP
                        case 8:
                            PC= OP3;
                            break;  
                        
                        //JZ 9
                        case 9:
                        	if(S.GetInteger(OP1) == 0) {
                        		PC = OP3;
                        	} else 
                        		PC = PC + 1;
                        	break;
                        	
                        //JP
                        case 10:
                            if (S.GetInteger(OP1) > 0){
                                 PC = OP3; }
                            else 
                                PC = PC + 1;
                            break;
                            
                        //JN 11
                        case 11: 
                        	if(S.GetInteger(OP1) < 0) {
                        		PC = OP3;
                        	} else 
                        		PC = PC +1;
                        	break;
                            
                        //JNZ 12
                        case 12:
                        	if(S.GetInteger(OP1) != 0) {
                        		PC = OP3;
                        	} else 
                        		PC = PC +1;
                        	break;
                        	
                        //JNP 13 
                        case 13:
                        	if(S.GetInteger(OP1) <=0) {
                        		PC = OP3;
                        	} else 
                        		PC = PC +1;
                        	break;
                        	
                        //JNN 
                        case 14:
                        	if(S.GetInteger(OP1)>=0) {
                        		PC = OP3;
                        	}
                        	else
                        		PC = PC +1;                        	
                        	break;
                       
                        // JINDR 15 
                        case 15:
                        	PC = S.GetInteger(OP3);
                        	
                        	break;
                        	          
                    }
                 }
            }

        }
}
