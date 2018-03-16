import java.io.PrintWriter;

import Jama.Matrix;


public class methodClass {
	/**
	 * Append zeros to the binary string if the length is < 8
	 * 
	 * @param binaryString
	 * @param numberOfBytes
	 * @return
	 */
	public static String appendZerosToBinaryString(String binaryString,
			int numberOfBytes) {

		int length = binaryString.length();
		int numberOfZerosToAppend = (numberOfBytes * 8) - length;
		for (int i = 0; i < numberOfZerosToAppend; i++) {
			binaryString = "0".concat(binaryString);
		}

		return binaryString;
	}
	public static double getRelativeError(int Urow, int vRow, int rank, Matrix sigma )
	   {
	      int size = Math.min(Urow,vRow);

	      double norm1 = 0;
	      for (int i = 0; i < size; i++)
	      {
	         norm1 += sigma.get(i, i) * sigma.get(i, i);
	      }

	      double norm2 = 0;
	      for (int i = rank; i < size; i++)
	      {
	         norm2 += sigma.get(i, i) * sigma.get(i, i);
	      }
	      return Math.sqrt(norm2 / norm1);
	   }
	   
	public static int[] splitNumbers(int number) {

		String binaryValue = Integer.toBinaryString(number);
		double binaryLength = binaryValue.length();
		double byteLength = 8;
		int numberOfBytes = (int) Math.ceil(binaryLength / byteLength);
		int[] outputArray = new int[numberOfBytes];
		int counter = 0;
		if (binaryValue.length() > 8) {
			// append zeros
			binaryValue = appendZerosToBinaryString(binaryValue, numberOfBytes);

			int start = 0;
			int end = 8;
			for (int i = binaryValue.length(); i > 0; i -= 8) {
				String partialNumber = binaryValue.substring(start, end);
				int a = Integer.valueOf(partialNumber, 2);
				outputArray[counter] = a;
				start += 8;
				end = end + 8 > binaryValue.length() ? binaryValue.length()
						: end + 8;
				counter++;

			}
		} else {

			outputArray[0] = number;

		}

		return outputArray;

	}
	public static String convertToBinary(int a)
	{
		String x=Integer.toBinaryString(a);
		if(x.length()<=8)
		{
			/*for(int i=x.length();i<8;i++)
				x="0"+x;*/
			int i=x.length();
			while(i<8)
			{   x="0"+x;
				i++;
			}
		}
		return x;
	}
	static int byteToInt(Byte b) {
		// TODO Auto-generated method stub
		if(b.intValue()<0)
		{
			return 128+(127+b.intValue()+1);
		}
		else
			return b.intValue();
	}

	static int integerVal(String substring) {
		// TODO Auto-generated method stub
		int intgrVal=0;
		StringBuffer stb=new StringBuffer(substring);
		stb.reverse();
		//System.out.println(b1);
		/*for(int i1=0;i1<stb.length();i1++)
		{
			if(stb.charAt(i1)=='1')
				intgrVal+=Math.pow(2, i1);
		}*/
		int i1=0;
		while(i1<stb.length())
		{
			
			if(stb.charAt(i1)=='1')
				intgrVal+=Math.pow(2, i1);

			i1++;
		}
	    return intgrVal;	
		
	}
	

	static String convrtToBinary(int imageWidth2) {
		// TODO Auto-generated method stub
		
		String s=Integer.toBinaryString(imageWidth2);
		if(s.length()<=16)
		{
			/*for(int i=s.length();i<16;i++)
				s="0"+s;*/
			int i=s.length();
		    while(i<16)
		    {
		    	s="0"+s;
		    	i++;
		    }
		}
		return s;
	}
	/*public static void SaveImage(Matrix a, String target) {
		// TODO Auto-generated method stub
		try
	      {
			int height = a.getRowDimension();
			int width=a.getColumnDimension();
	         PrintWriter output = new PrintWriter(target);
	         output.println("P2");
	         output.println("#Final Image");
	         output.println(width + " " + height);
	         output.println(255);

	         int i = 0;
	         while (i < height * width)
	         {
	            output.print(a.get(i/width, i%width) + " ");
	            if ((i + 1) % 16 == 0)
	            {
	               output.println();
	            }
	            i++;
	         }
	         output.close();
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }

	}
*/	public static void SaveImage(int height, int width, int[][] grays,
			String target) {
		// TODO Auto-generated method stub
	try
    {
       PrintWriter output = new PrintWriter(target);
       output.println("P2");
       output.println("#Final Image");
       output.println(width + " " + height);
       output.println(255);

       int i = 0;
       while (i < height * width)
       {
          output.print(grays[i / width][i % width] + " ");
          if ((i + 1) % 16 == 0)
          {
             output.println();
          }
          i++;
       }
       output.close();
    }
    catch (Exception e)
    {
       e.printStackTrace();
    }

	}




}
