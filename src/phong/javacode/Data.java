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
public class Data {

	int M;
	int N;
	double [][] data;
	
	public int getM() { return M; };
	public int getWidth() { return M; };
	public int getN() { return N; };
	public int getHeight() { return N; };
	public double [][] getData() { return data; };
	
	public double [] Means()
	{
		double [] means = new double[M];
		double sum;
		
		for (int i = 0; i < M; i++)
		{
			sum = 0;
			
			for (int j = 0; j < N; j++)
			{
				sum += data[j][i];
			}
			
			means[i] = sum/N;
		}
		
		return means;
	}
	
	public void subtractMeans()
	{
		double [] means = Means();
		
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				data[j][i] -= means[i];
	}
	
	// Constructor
	public Data(String inputFileName) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(inputFileName));
			N = scanner.nextInt();
			M = scanner.nextInt();
			
			data = new double[N][M];
			
			for (int i = 0; i < N; i++)
				for(int j = 0; j < M; j++)
					data[i][j] = scanner.nextDouble();
			
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
