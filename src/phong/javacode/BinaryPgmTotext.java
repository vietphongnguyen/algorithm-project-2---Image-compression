/**
 * 
 */
package phong.javacode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author phong
 *
 */
public class BinaryPgmTotext {

	public BinaryPgmTotext(String inputFileName) {
		int numberOf2ByteSaved =2; // write the first 2 number in 2 Byte format (short) and the rest will be written in 1 Byte 
		int countNumber =0;
		int number2Byte;
		int number1Byte, number1Byte_2;
		int column = 100;
		String outputFileName = makeOutputFileNameFromBinaryName(inputFileName);
        try {
        	InputStream inputStream = new FileInputStream(inputFileName);
        	BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"));
        	bufWriter.write("P2" + System.lineSeparator());
        	do {
        		number1Byte = inputStream.read();
        		if (number1Byte < 0) break;
        		countNumber ++;
				if (countNumber <= numberOf2ByteSaved) { // write in 2 byte
					number1Byte_2 = inputStream.read();	
					number2Byte =  ((number1Byte << 8) | number1Byte_2);
					bufWriter.write(number2Byte + " ");
					if (countNumber == 2 ) column = number2Byte;
				} else {	// write in 1 byte
					bufWriter.write(number1Byte + " ");
					if (   (countNumber > 3) &&  ( (countNumber-3) % column == 0)   ) bufWriter.write(System.lineSeparator());
				}
				
				if ( (countNumber ==2 ) || (countNumber ==3 ) )  bufWriter.write(System.lineSeparator());
				
            } while (true); // always in the loop until the break command inside   
        	
        	inputStream.close();  
        	bufWriter.close();
            System.out.println("\nSuccessfully read data from file '" + inputFileName + "' and saved to '" + outputFileName + "'");
        }
        catch(FileNotFoundException ex) {
        	System.out.println( "______ Error: file '" + inputFileName + "' NOT FOUND !!!"  ); 
        	ex.printStackTrace();
            return;
        }
        catch(IOException ex) {
            System.out.println( "______ Error: IOException when reading from '" + inputFileName + "' and writing to '" + outputFileName + "' !!! ");                  
            ex.printStackTrace();
            return;
        }
	}

	private String makeOutputFileNameFromBinaryName(String inputFileName) {  // image_b.pgm

		int dotPosition = inputFileName.lastIndexOf('.');
		if (dotPosition < 0) {
			String last2Character = inputFileName.substring(inputFileName.length() - 2);
			if (last2Character.equalsIgnoreCase("_b")) 	return inputFileName.substring(0, inputFileName.length()-2) ; 
			else return inputFileName + "_t" ; 
		} else {
			if (dotPosition == inputFileName.length() - 1) { // . at the end
				String last3Character = inputFileName.substring(inputFileName.length() - 3);
				if (last3Character.equalsIgnoreCase("_b.")) 	return inputFileName.substring(0, inputFileName.length()-3) +"." ; 
				else return inputFileName.substring(0, inputFileName.length()-3) + "_t." ; 
			}

			if (dotPosition == 0) { // . at the beginning
				return "_t" + inputFileName;
			} else {
				String ext = inputFileName.substring(dotPosition + 1);
				inputFileName = inputFileName.substring(0, dotPosition);
				String last2Character = inputFileName.substring(inputFileName.length() - 2);
				if (last2Character.equalsIgnoreCase("_b")) 	return inputFileName.substring(0, inputFileName.length()-2) + "." + ext ; 
				else return inputFileName + "_t." + ext ; 
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BinaryPgmTotext("input_files/image2_b.pgm");
		//printAllByte("input_files/image2_b.pgm");
	}

	private static void printAllByte(String inputFileName) {
		int number1Byte;
        try {
        	InputStream inputStream = new FileInputStream(inputFileName);
        	do {
        		number1Byte = inputStream.read();
        		if (number1Byte < 0) break;
        		System.out.print(number1Byte + ", ");
            } while (true); // always in the loop until the break command inside   
        	
        	inputStream.close();  
            System.out.println("\nSuccessfully read data from file '" + inputFileName + "'");
        }
        catch(FileNotFoundException ex) {
        	System.out.println( "______ Error: file '" + inputFileName + "' NOT FOUND !!!"  ); 
        	ex.printStackTrace();
            return;
        }
        catch(IOException ex) {
            System.out.println( "______ Error: IOException when reading from '" + inputFileName +  "' !!! ");                  
            ex.printStackTrace();
            return;
        }
		
	}

}
