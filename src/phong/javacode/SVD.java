/**
 * 
 */
package phong.javacode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import Jama.Matrix;

/**
 * @author phong
 *
 */
public class SVD {
	double [][] U ; 
	double [][] RD;
	double [][] V;
	int M,N, K, GreyScaleLevel;
	Matrix RDMatrix ;
	Matrix UMatrix ;
	Matrix VMatrix ;
	
	// Constructor
	public SVD(int m, int n, int greyScaleLevel, String inputMatrixFileName) {
		M=m;
		N=n;
		GreyScaleLevel = greyScaleLevel;
		Scanner scanner = null;
		System.out.println("Reading from file: " + inputMatrixFileName);
		try {
			scanner = new Scanner(new File(inputMatrixFileName));
			
			// read U matrix m x m
			U = new double[m][m];
			for (int i=0; i<m; i++ )
				for (int j=0; j<m; j++ )
					U[i][j] = scanner.nextDouble() ;
			
			// read Rectangular diagonal matrix m x n
			RD = new double[m][n];
			for (int i=0; i<m; i++ )
				for (int j=0; j<n; j++ )
					RD[i][j] = scanner.nextDouble();
			
			// read V matrix n x n
			V = new double[n][n];
			for (int i=0; i<n; i++ )
				for (int j=0; j<n; j++ )
					V[i][j] = scanner.nextDouble();
			
			scanner.close();
			
			UMatrix = new Matrix(U,M,M);
			//System.out.println("Unitary matrix U ( " + M + " x " + M + " ) :");
			//UMatrix.print(M, 5);
			//System.out.println();
			
			RDMatrix = new Matrix(RD,M,N);
			//System.out.println("Rectangular diagonal matrix  RD ( " + M + " x " + N + " ) :");
			//RDMatrix.print(N, 5);
			//System.out.println();
			
			VMatrix = new Matrix(V,N,N);
			//System.out.println("V matrix ( " + N + " x " + N + " ) :");
			//VMatrix.print(N, 5);
			//System.out.println();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	public void setK(int k) {
		K=k;
	}

	private int min(int m2, int n2) {
		if (m2>n2) return n2;
		else return m2;
	}

	public void saveKToFile(String outputFileName) throws IOException {
		
		OutputStream outputStream = new FileOutputStream(outputFileName);
		
		if (K>M) K=M;
		if (K>N) K=N;
		
		write2ByteInt(outputStream, M);
		write2ByteInt(outputStream, N);
		write2ByteInt(outputStream, K);
		outputStream.write(GreyScaleLevel);
		
		// Write U matrix m x m 
		for (int i = 0; i < M; i++)
			for (int j = 0; j < K; j++)
				write2ByteDouble(outputStream, U[i][j]);

		// write RD: Rectangular diagonal matrix m x n (Only m value on the diagonal )
		for (int i = 0; i < K; i++) 
			write2ByteDouble(outputStream, RD[i][i]);
		
		// write V matrix n x n
		for (int i = 0; i < K; i++)
			for (int j = 0; j < N; j++)
				write2ByteDouble(outputStream, V[j][i]); // Must swap i,j to j,i because V need to be transpose
		
		
		outputStream.flush();
		outputStream.close();
		System.out.println("\nSuccessfully saved to '" + outputFileName + "'");
		
		
	}
	
	public void saveEverythingToFile(String outputFileName) throws IOException {
		
		OutputStream outputStream = new FileOutputStream(outputFileName);
		
		write2ByteInt(outputStream, M);
		write2ByteInt(outputStream, N);
		write2ByteInt(outputStream, K);
		outputStream.write(GreyScaleLevel);
		
		// Write U matrix m x m 
		for (int i = 0; i < M; i++)
			for (int j = 0; j < M; j++)
				write2ByteDouble(outputStream, U[i][j]);

		// write RD: Rectangular diagonal matrix m x n (Only m value on the diagonal )
		for (int i = 0; i < min(M,N); i++) 
			write2ByteDouble(outputStream, RD[i][i]);
		
		// write V matrix n x n
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				write2ByteDouble(outputStream, V[i][j]);
		
		
		outputStream.flush();
		outputStream.close();
		System.out.println("\nSuccessfully saved to '" + outputFileName + "'");
		
		
	}

	private void write2ByteInt(OutputStream outputStream, int intValue) throws IOException {
		outputStream.write((byte)(intValue >> 8));
		outputStream.write((byte)intValue  );
		
	}

	private void write2ByteDouble(OutputStream outputStream, double f) throws IOException {
		byte [] bb = new byte[2];
		doubleTo2Byte(f,bb);
		outputStream.write(bb[0]);
		outputStream.write(bb[1]);
	}

	private static void doubleTo2Byte(double f, byte[] bb) {
		if (f==0) { 
			bb[0] =0;
			bb[1] =0;
			return;
		}
		
		byte[] bit = new byte[16];
		for (int i =0; i<16 ; i++) bit[i] = 0;
		
		if (f<0) { 
			bit[15] = 1;								// bit 15 is a sign bit. if this number is negative then bit 15 =1, otherwise bit 15 =0.
			f= -f;
		}
		
		int exponent=0;										//
		double mantissa = f;
		if (mantissa >= 1000) { // f= 80032     800 . 10 ^2
			while (mantissa >= 1000) {
				mantissa /= 10;
				exponent ++;
			}
		} else {
			while (mantissa < 100) {  // f= 8		800 .  10 ^ -2
				mantissa *= 10;
				exponent --;
			}
		}
		// mantissa always have 3 digits: 100 to 999
		int powers = 512;
		for (int i = 9; i>=0 ; i--) { 						// mantissa will use 10 bit from bit 0 to bit 9 to store the value from 100 to 999
			bit[i] = (byte) (mantissa / powers);
			mantissa %= powers;
			powers /=2;
		}
		
		if (exponent <0) { 									// bit 14 is a sign bit for exponent. if the exponent is negative then bit 14 =1, otherwise bit 14 =0.
			bit[14] = 1;
			exponent = - exponent;
		}
		
		powers = 8;
		for (int i = 3; i>=0 ; i--) { 						// exponent will use 4 bit from bit 10 to bit 13 to store the value from up to 2^5-1 = 63. [-63 < exponent < +63 ]
			bit[i+10] = (byte) (exponent / powers);
			exponent %= powers;
			powers /=2;
		}
															// The double range can store:      -999 x 10^63  ... +999 x 10^63
															// The precision it can store:      100 x 10^(-63)  
		
		// read from bit array [15..0]
		byte byte1 =  (byte) (bit[15] * 128 +  bit[14] * 64 +  bit[13] * 32 +  bit[12] * 16 + bit[11] * 8 +  bit[10] * 4 +  bit[9] * 2 +  bit[8]) ;
		bb[0] = byte1;
		byte byte2 =  (byte) (bit[7] * 128 +  bit[6] * 64 +  bit[5] * 32 +  bit[4] * 16 + bit[3] * 8 +  bit[2] * 4 +  bit[1] * 2 +  bit[0]) ;
		bb[1] = byte2;
		//for (int i = 15; i>=0 ; i--) System.out.print(bit[i] + " "); System.out.println();
		
	}
	
	public static void main(String[] args) {
		byte[] bb = new byte[2];
		
		double f =0.59497426f;
		
		doubleTo2Byte(f, bb);
		System.out.println(bb[0] + "   " + bb[1]);
		
	}

}
