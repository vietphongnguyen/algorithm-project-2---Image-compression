package phong.javacode;

import java.io.*;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class PCA {

	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub
		// Yourprogram.exe data.txt k PCA.txt
		String dataFileName="";
		try {
				dataFileName = args[0];
		} catch (Exception e) {
			dataFileName = "data.txt";
			System.out.println("Use default dataFileName : " + dataFileName);
		}
		if (dataFileName.equals("")) dataFileName = "data.txt";
		Data data = new Data(dataFileName);
		
		long startTime = System.nanoTime(); // start counting time
		
		data.subtractMeans();
					
		Matrix dataMatrix = new Matrix(data.getData());
		
		Matrix S = (dataMatrix.transpose()).times(dataMatrix);
		S = S.times(1/(data.getN()-1.));
		
		SingularValueDecomposition svd = S.svd();

		Matrix newData = dataMatrix.times(svd.getV());
		
		long endTime   = System.nanoTime();  //end counting time
		long totalTime = endTime - startTime;
		
		System.out.println("\n_____________________\nPrincipal Component Analysis (PCA) with " + data.getN() + " observations and " + data.getM() + " takes: " + totalTime + " nanoseconds (~" + totalTime/1000000 + " milliseconds)");
		
		int k = (data.getM() > data.getN())? data.getN() : data.getM() ;
		try {
			k = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("Use default K : " + k);
		}
		
		if (k>data.getM()) k=data.getM();
		if (k>data.getN()) k=data.getN();
		
		String PCAFileName="";
		try {
				PCAFileName = args[2];
		} catch (Exception e) {
			PCAFileName = data.getN() + "_" + data.getM() + "_" + k + "_PCA.txt";
		}
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PCAFileName)));
		
		startTime = System.nanoTime();
		
		writer.write(data.getN() + " " + k + System.getProperty( "line.separator"));
		
		for (int j = 0; j < data.getN(); j++)
		{
			for (int i = 0; i < k; i++)
				writer.write(newData.get(j, i) + " ");
		
			writer.write(System.getProperty( "line.separator"));
		}
		
		endTime = System.nanoTime();
		totalTime = endTime - startTime;
					
		System.out.println("\n_____________________\nOutputting the first " + k + " principal component(s) with " + data.getN() + " observations to file (" + PCAFileName + ") takes: " + totalTime + " nanoseconds (~" + totalTime/1000000 + " milliseconds)");
		
		writer.close();
	}
	

}
