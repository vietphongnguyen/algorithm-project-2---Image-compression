/**
 * 
 */
package phong.javacode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

import javax.swing.ImageIcon;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * @author phong
 *
 */
public class GenerateSVDimage {

	/**
	 * @param args
	 */
	 Matrix UMatrix ; 
	 double [][] RD;
	 double [][] UArray;
	 double [][] VArray;
	 Matrix VMatrix;
	 int M;
	 int N;
	 int K;
	 int GreyScaleLevel;
	 double [][] grayImage;
	
	//Constructor
	public GenerateSVDimage() {
		K=5;
	}
	
	public static void main(String[] args) {
		// EncodeSVDGrayImageFromJPG("flower01.jpg");
		GenerateSVDimage svdImage = new GenerateSVDimage();
		svdImage.ReadGrayImageFromText("P.pgm");
		try {
			svdImage.EncodeSVDGrayImage();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

	private void EncodeSVDGrayImage() throws IOException {
		Matrix imageMatrix = new Matrix(grayImage,M,N);
		SingularValueDecomposition svd = new SingularValueDecomposition(imageMatrix);
		double[] singularValuesArray = svd.getSingularValues();

		double[] sigmas = new double[K];
		for (int j = 0; j < K; j++) {
			sigmas[j] = singularValuesArray[j];
		}
		
		RD = new double [M][N];
		for (int i = 0; i < singularValuesArray.length ; i++) 
			for (int j = 0; j < singularValuesArray.length ; j++) 
				if (i==j) RD[i][j] = singularValuesArray[i];
				else  RD[i][j] = 0;
		
		UMatrix = svd.getU();
		VMatrix = svd.getV();
		UArray = UMatrix.getArray(); 
		VArray = VMatrix.getArray();
		
		String outputFileName = "header.txt";
		BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"));
    	bufWriter.write(M +" " + N + " " + GreyScaleLevel);
    	bufWriter.close();
    	System.out.println("\nSuccessfully saved to '" + outputFileName + "'");
    	
    	outputFileName = "SVD.txt";
    	BufferedWriter bufWriter1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"));
    	writeArrayToSVDTextFile(bufWriter1,UArray,M,M);
    	writeArrayToSVDTextFile(bufWriter1,RD,M,N);
    	writeArrayToSVDTextFile(bufWriter1,VArray,N,N);
    	VMatrix.print(N, 5);
    	bufWriter1.close();
    	System.out.println("\nSuccessfully saved to '" + outputFileName + "'");
    	
    	// test SVD
    	Matrix ReconstructImage = new Matrix(M,N);
    	Matrix RDMatrix = new Matrix(RD);
    	ReconstructImage = UMatrix.times(RDMatrix).times(VMatrix.transpose());
    	ReconstructImage.print(N, 5);
    	
	}

	private static void writeArrayToSVDTextFile(BufferedWriter bufWriter, double[][] A, int m, int n) throws IOException {
		
		NumberFormat formatter = new DecimalFormat("#0.00000000");     
		
		double epsilon = 0.00000001f;
		
		for (int i = 0; i < m ; i++) {
			for (int j = 0; j < n ; j++) 
				if (  (A[i][j] > epsilon)|| (A[i][j] < -epsilon)   ) bufWriter.write(formatter.format(A[i][j]) + "\t");
				else bufWriter.write("0 \t\t");
			bufWriter.write(System.lineSeparator());
		}
		
		bufWriter.write(System.lineSeparator());
	}

	private void ReadGrayImageFromText(String inputTextFileName) {
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(inputTextFileName));
			scanner.nextLine(); // Ignore the 1st line: P2
			
			M = scanner.nextInt();
			N = scanner.nextInt();
			GreyScaleLevel = scanner.nextInt();
			
			// read image matrix M x N
			grayImage = new double [M][N];
			for (int i=0; i<M; i++ )
				for (int j=0; j<N; j++ )
					grayImage[i][j] = scanner.nextInt();
			scanner.close();
			System.out.println("\nSuccessfully read data from file '" + inputTextFileName + "'");
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

	private static void EncodeSVDGrayImageFromJPG(String jpgFileName) {
		
		// load image
		Image loadImage = new ImageIcon(jpgFileName).getImage();
		BufferedImage pxImg = new BufferedImage(loadImage.getWidth(null), loadImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		pxImg.createGraphics().drawImage(loadImage, 0, 0, null);

		

		// convert to pixels
		Matrix[] img = ImageMatrixConversion.image2Matrix(pxImg);
		
        
        
        
	}

}
