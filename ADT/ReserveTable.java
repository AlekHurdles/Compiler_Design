package ADT;
import java.io.FileWriter;
import java.io.IOException;

public class ReserveTable {
	
	// This class represents the reserve word name and code. Stored in an object for easy access 
	public class ReserveWord{

        String name;
        int code;

        public ReserveWord(String name, int code){
            this.name=name;
            this.code=code;}
    }

    ReserveWord[] reserveArray;
    
    
    // This method creates a Reserve Table of max size
    public ReserveTable(int maxSize){ reserveArray = new ReserveWord[maxSize]; }
    
    // This method adds a new reserve word object to the next available index of the reserve table. 
    public int Add(String name, int code){

            for(int i = 0; i<reserveArray.length; i++){
                if(reserveArray[i]==null){
                    reserveArray[i]= new ReserveWord(name,code);
                    return i;}}
        
            return 0;
    }

    // This method looks ups a non case sensitive name within the reserve table
    public int LookupName(String names){

        for(int i=0; i<reserveArray.length; i++){
            if(reserveArray[i]!=null){
                if(names.compareToIgnoreCase(reserveArray[i].name)==0 || names.contentEquals(reserveArray[i].name)==true) {
                    return reserveArray[i].code;}
                
            } else
            	return -1;
        }

        return -1;
    }

    // This method looks up the opcode specified in the reserve table
    public String LookupCode(int code){
        for(int i=0; i<reserveArray.length;){
            if(reserveArray[i]==null || reserveArray[i].code !=code){
                i++;
            }

            else if(reserveArray[i].code==code && reserveArray[i] !=null ){
               String value = reserveArray[i].name;
               i++;
                return value;
            }  
        }
        return "";
    }
    
    // This method prints out the reserve table, including the index / name / code 
    public void PrintReserveTable(String filename)throws IOException{

        FileWriter myObj = new FileWriter(filename);
        myObj.write("Index \t Name \t Code \n");

        for(int i=0; i<reserveArray.length; i++){
            if(reserveArray[i]!= null){
                myObj.write(i+1 +"\t"+ reserveArray[i].name+"\t"+reserveArray[i].code+"\n");
            }
        }

        myObj.close();
    }
}
