/**
 * 
 */
package phong.javacode;

import java.io.IOException;

/**
 * @author phong
 *
 */
public class ImageCompressionSVD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length <= 0) { // run GUI mode

			System.out.println("Please provide the arguments: \n 1 image.pgm \n 2 image_b.pgm \n 3 header.txt SVD.txt k \n 4 image_b.pgm.SVD \n");
			
			
		} else { // run in command line mode
			String optionMode = args[0];
			if (optionMode.equals("1")) {	// Yourprogram.exe 1 image.pgm 		"1 test01_txt.pgm"		1 P.pgm
				String inputFileName = args[1];
				new textPgmToBinary(inputFileName);
			}
			if (optionMode.equals("2")) {	// Yourprogram.exe 2 image_b.pgm							2 P_b.pgm
				String inputFileName = args[1];
				new BinaryPgmTotext(inputFileName);
			}
			if (optionMode.equals("3")) {	// Yourprogram.exe 3 header.txt SVD.txt k   ->	 Example: "3 input_files\header.txt input_files\SVD.txt 2"      "3 header.txt SVD.txt 5"
											//															3 header.txt SVD.txt 5
				String headerFileName="";
				try {
					headerFileName = args[1];
				} catch (Exception e) {
					headerFileName = "header.txt";
					System.out.println("Use default headerFileName : " + headerFileName);
				}
				if (headerFileName.equals("")) headerFileName = "header.txt";
				Header header = new Header(headerFileName);
				
				String svdFileName ="";
				try {
					svdFileName = args[2];
				} catch (Exception e) {
					svdFileName = "SVD.txt";
					System.out.println("Use default svdFileName : " + svdFileName);
				}
				if (svdFileName.equals("")) svdFileName = "header.txt";
				SVD svd = new SVD(header.getM(), header.getN(), header.getGreyScaleLevel(), svdFileName);
				
				int k = (header.getM() > header.getN())? header.getN() : header.getM() ;
				try {
					k = Integer.parseInt(args[3]);
				} catch (Exception e) {
					System.out.println("Use default K : " + k);
				}
				svd.setK(k);

				try {
					svd.saveKToFile("image_b.pgm.SVD");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (optionMode.equals("4")) {	// Yourprogram.exe 4 image_b.pgm.SVD
				String inputFileName;
				try {
					inputFileName = args[1];
				} catch (Exception e) {
					inputFileName = "image_b.pgm.SVD";
					System.out.println("Use default inputFileName : " + inputFileName);
				}
				try {
					binarySVDtoKPgmImage binarySVD = new binarySVDtoKPgmImage(inputFileName);
					
					//binarySVD.saveToK_PGMTextFile(inputFileName.substring(0, inputFileName.length() - 9) + "k.pgm");
					binarySVD.saveToK_PGMTextFile_UsingJamaMatrix(inputFileName.substring(0, inputFileName.length() - 9) + "k.pgm");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
		}

	}
}
