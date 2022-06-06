// Aleksandra Anderson 
//CS4100 SP2022

package ADT;

import java.io.FileWriter;
import java.io.IOException;

public class QuadTable{
    
	//Object used to represent the Quad Table entries 
    public class Data{
			
        int opCode;
        int operand1;
        int operand2;
        int operand3;
        
        public Data(int opCode, int operand1, int operand2, int operand3) {
            
            this.opCode=opCode;
            this.operand1=operand1;
            this.operand2=operand2;
            this.operand3=operand3;}
        
    }
    
    //Initialize Data array and size

    Data[] quadArray; 
    int nextAvailable;
    int maxSizeQ;

    public QuadTable(int maxSize){
    	maxSizeQ = maxSize;
        quadArray=new Data[maxSize]; 
    }

    public int GetSize(){
    	return maxSizeQ;
    }
    
    //Return the next open slot in table
    public int NextQuad(){
        for(int i=0; i<quadArray.length; i++){
            if(quadArray[i]==null){

                nextAvailable=i;
                return nextAvailable;
            }
        }
        return 0;
    }

    //Add row to the table at the nextAvailable slot, and increment the slot
    public void AddQuad(int opcode, int op1, int op2, int op3){
            
        int value=NextQuad();
            quadArray[value] = new Data(opcode, op1, op2, op3);
    }

    //Return data at the specified index
    public int GetQuad(int index, int column){
    	
    	
        switch(column){

            case 0:
                return quadArray[index].opCode;
                //break;
            case 1:
                return quadArray[index].operand1;
                
            case 2:
                return quadArray[index].operand2;
                //break;
            case 3:
                return quadArray[index].operand3;
                //break;
            default:
                return 0;
                //break;

        }
    }

    //Change the contents of a quad entry at specified index
    public void UpdateQuad(int index, int opcode, int op1,int op2, int op3){
            quadArray[index]= new Data(opcode, op1, op2, op3);
    }

    //Prints the currently used contents of the table
    public void PrintQuadTable(String filename){

        FileWriter myObj = null;
		try {
			myObj = new FileWriter(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			myObj.write("Aleksandra Anderson SP2022 \n\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        for(int i=0; i<quadArray.length; i++){
            if(quadArray[i]!= null){
                try {
					myObj.write(quadArray[i].opCode+"|"+quadArray[i].operand1+"|"+quadArray[i].operand2+"|"+quadArray[i].operand3+"|"+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

        try {
			myObj.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}//End of Quad Table Class
