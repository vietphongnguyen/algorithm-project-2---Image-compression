/**
 * 
 */
package phong.javacode;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import Jama.Matrix;

/**
 * @author phong
 *
 */
public class binarySVDtoKPgmImage {

	double [][] U ; 
	double[][] RD;
	double [][] V;
	int M,N, K, GreyScaleLevel;
	Matrix RDMatrix ;
	Matrix UMatrix ;
	Matrix VMatrix ;
	
	// Constructor
	public binarySVDtoKPgmImage(String inputFileName) throws IOException {
		InputStream inputStream = new FileInputStream(inputFileName);
		
		M = read2ByteInt(inputStream);
		N= read2ByteInt(inputStream);
		K = read2ByteInt(inputStream);
		GreyScaleLevel = inputStream.read();
		
		U = new double [M][M];
		for (int i = 0; i < M; i++)
			for (int j = 0; j < i; j++)
				U[i][j] = 0;
		
		// Read U matrix m x m (only upper half of U)
		for (int i = 0; i < M; i++)
			for (int j = 0; j < M; j++)
				U[i][j] = read2ByteDouble(inputStream);

		// Read RD: Rectangular diagonal matrix m x n (Only m value on the diagonal )
		RD = new double [M][N];
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				RD[i][j] =0;
		for (int i = 0; i < min(M,N); i++)
			RD[i][i] = read2ByteDouble(inputStream);

		// Read V matrix n x n
		V = new double [N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				V[i][j] = read2ByteDouble(inputStream);

		inputStream.close();
		System.out.println("\nSuccessfully read from '" + inputFileName + "'");

		System.out.println("Unitary matrix U ( " + M + " x " + M + " ) :");
		UMatrix = new Matrix(U,M,M);
		UMatrix.print(M, 5);
		
		System.out.println("Rectangular diagonal matrix  RD ( " + M + " x " + N + " ) :");
		RDMatrix = new Matrix(RD,M,N);
		RDMatrix.print(N, 5);
		
		System.out.println("V matrix ( " + N + " x " + N + " ) :");
		VMatrix = new Matrix(V,N,N);
		VMatrix.print(N, 5);
		
	}

	private int min(int m2, int n2) {
		if (m2>n2) return n2;
		else return m2;
	}

	private float read2ByteDouble(InputStream inputStream) throws IOException {
		byte [] bb = new byte[2];
		bb[0] = (byte) inputStream.read();
		bb[1] = (byte) inputStream.read();
		
		return twoByteToDouble(bb);
		
	}

	private static float twoByteToDouble(byte[] bb) {
		
		if (  (bb[0] ==0) && (bb[1] ==0) )  return 0.0f;
				
		float f =0;
		byte[] bit = new byte[16];
		for (int i =0; i<16 ; i++) bit[i] = 0;
		
		int full16Bit_1 = Byte.toUnsignedInt(bb[0]);
		int full16Bit_2 = Byte.toUnsignedInt(bb[1]);

		int full16Bit =  (full16Bit_1 *256) + full16Bit_2;  
		
		int powers = 32768;
		for (int i = 15; i >= 0; i--) {
			bit[i] = (byte) (full16Bit / powers);
			full16Bit %= powers;
			powers /= 2;
		}

		int exponent = bit[10] + 2* bit[11] + 4* bit[12] + 8* bit[13] ;
		if (bit[14] == 1) exponent = - exponent;
		
		// mantissa always have 3 digits: 100 to 999
		int mantissa=0;
		powers = 1;
		for (int i = 0; i<=9 ; i++) { 
			mantissa += bit[i]*powers;
			powers *=2;
		}
		
		f = (float) (mantissa * Math.pow(10, exponent));
		
		if (bit[15] == 1) f= -f ;
		
//		for (int i =15; i>=0 ; i--) System.out.print(bit[i]);
//		System.out.println();
		
		return f;
	}

	private int read2ByteInt(InputStream inputStream) throws IOException {
		int number1Byte = inputStream.read();
		int number1Byte_2 = inputStream.read();	
		int number2Byte =  ((number1Byte << 8) | number1Byte_2);
		return number2Byte;
		
	}



	public void saveToK_PGMTextFile(String outputFileName) throws IOException {
		BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"));
    	bufWriter.write("P2" + System.lineSeparator());
    	bufWriter.write(M + " " + N + System.lineSeparator());
    	bufWriter.write(GreyScaleLevel + System.lineSeparator() );
    	
    	double[][] V_transpose = new double[N][N];
    	V_transpose = MatrixTranspose(V,N,N);
    	byte[][] image = new byte[M][N];
    	image = MatrixMutiply(U,M,M,RD,M,N,V_transpose,N,N);
    	
    	for (int i=0; i<M ; i++) {
    		for (int j=0; j<N ; j++)
    			bufWriter.write(image[i][j] + " ");
    		bufWriter.write(System.lineSeparator());
    	}
    			
    	bufWriter.close();
    	System.out.println("\nSuccessfully saved image to '" + outputFileName + "'");
		
	}

	private byte[][] MatrixMutiply(double[][] u2, int m2, int m3, double[][] rD2, int m4, int n2, double[][] v_transpose, int n3, int n4) {
		if ( (m3 != m4) ||  (n2 != n3) ) {
			// Can not multiply 
			System.out.println("Cannot multiply matrix because they are invalid size! ");
			return null;
		}
		
		byte[][] matrixMutiply = new byte[m2][n4];
		double[][] fMatrix1 = new double[m2][n2];
		fMatrix1 = twoDoubleMatrixMutiply(u2, m2, m3, rD2, m4, n2);
		
		double[][] fMatrix2 = new double[m2][n4];
		fMatrix2 = twoDoubleMatrixMutiply(fMatrix1, m2, n2, v_transpose, n3, n4);
		
		for (int i=0; i<m2 ; i++) 
    		for (int j=0; j<n4 ; j++) 
    			matrixMutiply[i][j] = (byte) fMatrix2 [i][j];
		
		return matrixMutiply;
	}

	private double[][] twoDoubleMatrixMutiply(double[][] u2, int m2, int m3, double[][] rD2, int m4, int n2) {
		if ( m3 != m4 ) {
			// Can not multiply 
			System.out.println("Cannot multiply matrix because they are invalid size! ");
			return null;
		}
		
		double[][] fMatrix = new double[m2][n2];
		
		for (int i=0; i<m2 ; i++) 
    		for (int j=0; j<n2 ; j++) {
    			// Calculate fMatrix[i][j]
    			fMatrix[i][j] =0;
    			for (int k=0; k<m3 ; k++) 
    				fMatrix[i][j] += u2[i][k] * rD2[k][j];
    		}
		
		return fMatrix;
	}

	private double[][] MatrixTranspose(double[][] v2, int n2, int n3) {
		double[][] T = new double[n3][n2];
		for (int i=0; i<n3 ; i++) 
    		for (int j=0; j<n2 ; j++)
    			T[i][j] = v2[j][i];
		
		return T;
	}

	public void saveToK_PGMTextFile_UsingJamaMatrix(String outputFileName) throws IOException {
		
		BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"));
    	bufWriter.write("P2" + System.lineSeparator());
    	bufWriter.write(M + " " + N + System.lineSeparator());
    	bufWriter.write(GreyScaleLevel + System.lineSeparator() );
    	
    	Matrix ReconstructImage = new Matrix(M,N);
    	
		ReconstructImage = UMatrix.times(RDMatrix).times(VMatrix.transpose());
    	ReconstructImage.print(N, 5);
    	
    	int cellValue;
    	for (int i=0; i<M ; i++) {
    		for (int j=0; j<N ; j++) {
    			cellValue = (int) Math.round( ReconstructImage.getArray()[i][j]);
    			if (cellValue < 0) cellValue = 0;
    			if (cellValue > GreyScaleLevel) cellValue = GreyScaleLevel;
    			bufWriter.write(cellValue + " ");
    		}
    			
    		bufWriter.write(System.lineSeparator());
    	}
    			
    	bufWriter.close();
    	System.out.println("Successfully saved image to '" + outputFileName + "'");
    	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] bb = new byte[2];
		bb[0] = 78;
		bb[1] = 82;
		
		float f = twoByteToDouble(bb);
		//float f = 0.70710678f;
		
		System.out.println(f);

	}

}
