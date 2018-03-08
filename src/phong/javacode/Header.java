/**
 * 
 */
package phong.javacode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author phong
 *
 */
public class Header {

	int M;
	int N;
	int GreyScaleLevel;
	
	public int getM() { return M; };
	public int getWidth() { return M; };
	public int getN() { return N; };
	public int getHeight() { return N; };
	public int getGreyScaleLevel() {return GreyScaleLevel;};
	
	// Constructor
	public Header(String inputFileName) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(inputFileName));
			M = scanner.nextInt();
			N = scanner.nextInt();
			GreyScaleLevel = scanner.nextInt();
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
