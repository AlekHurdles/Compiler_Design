// Aleksandra Anderson 
//CS4100 SP2022

package ADT;

import java.io.FileWriter; // Import the File class
import java.io.IOException;

public class SymbolTable {
	
	//Build the symbol objects called data 
	public class SymbolData{
		
				String name;
				char kind;
				char data_type;
				int integerValue;
				double floatValue;
				String stringValue;
				
				public SymbolData(String name, char kind, char data_type, int integerValue, double floatValue, String stringValue) {
					
					this.name=name;
					this.kind=kind;
					this.data_type=data_type;
					this.integerValue=integerValue;
					this.floatValue=floatValue;
					this.stringValue=stringValue;}}
		
	//Symbol table array	
	SymbolData[] arrayElement;
	public char constantkind = 'v';
		
	//Max size of symbol table 
	public SymbolTable(int maxSize){
		arrayElement= new SymbolData[maxSize];}
		
//-------------------------------------------------------------------------------------------//
			//Add Int Symbol to table 
			
			public int AddSymbol(String name, char kind, int value) {
				
				for(int i=0; i<arrayElement.length; i++) {
					
					if (i >= arrayElement.length) {
						return -1;}
					
					//Add the element to the array 
					
					if(arrayElement[i] != null){
						if(name.compareToIgnoreCase(arrayElement[i].name)==0){
							return i;}
					}

					else if (arrayElement[i] == null) {
						
						arrayElement[i]= new SymbolData(name, kind, 'i',value,0.0,"null");
						
						return i;}}
				
				return 0;} 
			
			
		//Add double Symbol to table 
			
			public int AddSymbol(String names, char kind, double value) {

				for(int i=0; i<arrayElement.length; i++) {
					
					if(i >= arrayElement.length){
						return -1;}

					if(arrayElement[i] != null){
						if(names.compareToIgnoreCase(arrayElement[i].name)==0){
							return i;}
						}
					//Add the element to the array 
					
					if (arrayElement[i] == null) {
						
						arrayElement[i]= new SymbolData(names, kind, 'f',0,value,"null");
						
						return i; }}

						
				return 0;} 
			
		//Add String Symbol to table 
			public int AddSymbol(String name, char kind, String value) {
				
				for(int i=0; i<arrayElement.length; i++) {
					if(arrayElement[i] !=null){
						if(name.compareToIgnoreCase(arrayElement[i].name)==0) {
						return i;}
					}

					//Add the element to the array 
					if (arrayElement[i] == null) {
						
						arrayElement[i]= new SymbolData(name, kind, 's',0,0.0,value);
						
						return i;}
					
						if (i >= arrayElement.length) {
							return -1;}}
				return 0;} 
		
		

//-------------------------------------------------------------------------------------------//
//symbol look up 
		
		public int LookupSymbol(String symbol) {
			
			
			// return the index where the name is found 
			for(int i=0; i<arrayElement.length; i++) {

				if(arrayElement[i]!=null){
					if(symbol.compareToIgnoreCase(arrayElement[i].name)==0) {
						return i;}}

				if(arrayElement[i]==null){return -1;}
			}
			
			return -1;}

//-------------------------------------------------------------------------------------------//
//Get Symbol
		public String GetSymbol(int index) {
			return arrayElement[index].name;}
//Get kind
		public char GetKind(int index) {
			return arrayElement[index].kind;}
//Get DataType
		public char GetDataType(int index) {
			return arrayElement[index].data_type;}
//Get String Value
		public String GetString(int index) {
			return arrayElement[index].stringValue;}
//Get Int Value
		public int GetInteger(int index) {
			return arrayElement[index].integerValue;}
//Get float Value
		public double GetFloat(int index) {
			return arrayElement[index].floatValue;}

//-------------------------------------------------------------------------------------------//
// update symbols

		public void UpdateSymbol(int index, char kind, int value){
				arrayElement[index].kind= kind;
				arrayElement[index].integerValue=value;}
			
		public void UpdateSymbol(int index, char kind, double value){
				arrayElement[index].kind= kind;
				arrayElement[index].floatValue=value;}
			
		public void UpdateSymbol(int index, char kind, String value){
				arrayElement[index].kind= kind;
				arrayElement[index].stringValue=value;}//UpdateSymbol Class 
			
//-------------------------------------------------------------------------------------------//
// print method 
		public void print(String filename){
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
				
				for(int i=0; i<arrayElement.length; i++) {
					
					if(arrayElement[i]!= null) { 

						if(arrayElement[i].data_type=='i'){
							try {
								//myObj.write(i+"|\t"+arrayElement[i].name+"|"+arrayElement[i].kind+"|"+arrayElement[i].data_type+"|"+arrayElement[i].integerValue+"\n");
								myObj.write(String.format("%s | %-13s \t|%c|%c|%-13d%n", String.valueOf(i) , arrayElement[i].name.trim(),arrayElement[i].kind,arrayElement[i].data_type,arrayElement[i].integerValue));

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if(arrayElement[i].data_type=='s'){
							try {
								//myObj.write(i+"|\t"+arrayElement[i].name+"|"+arrayElement[i].kind+"|"+arrayElement[i].data_type+"|"+arrayElement[i].stringValue+"\n");
								myObj.write(String.format("%s | %-13s \t|%c|%c|%-13s%n", String.valueOf(i) , String.valueOf(arrayElement[i].name).trim(),arrayElement[i].kind,arrayElement[i].data_type,arrayElement[i].stringValue));

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if(arrayElement[i].data_type=='f'){
							try {
								//myObj.write(i+"|\t"+String.format("%-8s",arrayElement[i].name)+"|"+arrayElement[i].kind+"|"+arrayElement[i].data_type+"|"+arrayElement[i].floatValue+"\n");
								myObj.write(String.format("%s | %-13s \t|%c|%c|%-14.2e%n", String.valueOf(i) , arrayElement[i].name.trim(),arrayElement[i].kind,arrayElement[i].data_type,arrayElement[i].floatValue));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
			
}

