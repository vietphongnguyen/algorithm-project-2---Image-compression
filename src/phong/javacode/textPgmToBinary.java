/**
 * 
 */
package phong.javacode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author phong
 *
 */
public class textPgmToBinary {
	
	
	// Constructor
	public textPgmToBinary(String inputFileName) {
		String line = null;
		int numberOf2ByteSaved =2; // write the fisrt 2 number in 2 Byte format (short) and the rest will be written in 1 Byte 
		int countNumberOf2ByteSaved =0;
		short number2Byte;
		byte number1Byte;
		String outputFileName = makeOutputFileNameFrom(inputFileName);
        try {
        	BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName));
        	OutputStream outputStream = new FileOutputStream(outputFileName);
        	while((line = bufferedReader.readLine()) != null) {
                String[] word = line.split(" ");
                if (word[0].equals("#") || (word.length <=0)) continue; 	// Ignore the comments line starting with "#" or blank line 
                
                int l = word.length;
				int i = 0;
				
				while (i < l) {
					try {
						number1Byte = (byte) Integer.parseInt(word[i]);
						number2Byte = (short) Integer.parseInt(word[i]);
					} catch (NumberFormatException e) {
						i++;
						continue; // Ignore the line starting with any non integer number as "P2"
					}
					if (number2Byte < 0) {
						i++;
						System.out.println("_______ Warning: the negative number (" + number2Byte + ") has been igrored");
						continue;
					}
					countNumberOf2ByteSaved ++;
					i++;
					if (countNumberOf2ByteSaved <= numberOf2ByteSaved) { // write in 2 byte
						outputStream.write((byte)(number2Byte >> 8));
						outputStream.write((byte)number2Byte  );
						
					} else {	// write in 1 byte
						outputStream.write(number1Byte);
					}
				}
            }   
            bufferedReader.close();  
            outputStream.close();
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

	private String makeOutputFileNameFrom(String inputFileName) {
		
		int dotPosition = inputFileName.lastIndexOf('.');
		if (dotPosition < 0) { 
			return inputFileName + "_b";
		} else {
			if (dotPosition == inputFileName.length() -1 ) { // . at the end
				return inputFileName.substring(0, dotPosition) + "_b.";
			}
		
			if (dotPosition == 0 ) { 		// . at the beginning
				return "_b" + inputFileName;
			} else {
				String ext = inputFileName.substring(dotPosition + 1);
				return inputFileName.substring(0, dotPosition) + "_b." + ext;
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new textPgmToBinary("input_files/image2.pgm");

	}

}
