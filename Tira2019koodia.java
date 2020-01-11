
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Integer;

public class Tira2019
{
private void readInput()
{
String line;

	 try {
            BufferedReader br = new BufferedReader( new FileReader("setA.txt"));
            
              for(int i=0; i<5; i++){
              line=br.readLine();
              String[] values=line.split("\n");	  
              System.out.println(values[0]);
	}
         
	 
	 } catch(IOException e)
	 {
	    System.out.println("File not found.");;
	 }
}
private void writeOutput()
{
    int esim1=5;
    int	esim2=1;
    String outputrow = esim1+ " "+esim2;
    try {
		 BufferedWriter bw = new BufferedWriter(new FileWriter("outp.txt")); 		
         	 //bw.write(outputrow);
		 //bw.newLine();
		 bw.write(outputrow);
		 bw.close();
    } catch (IOException e) {
    System.err.format("IOException: %s%n", e);
}
  System.out.println("Writing file...");
}
public static void main(String[] args)
	{
	    Tira2019 ht=new Tira2019();		      
		ht.readInput();
		ht.writeOutput();
	}

}