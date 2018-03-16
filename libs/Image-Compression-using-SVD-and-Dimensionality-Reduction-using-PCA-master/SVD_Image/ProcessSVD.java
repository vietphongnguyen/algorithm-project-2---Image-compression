import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import Jama.Matrix;


public class ProcessSVD {

	private static boolean checkIfTransposed;
	private static Matrix uMat;
	private static Matrix sigMat;
	private static Matrix vMat;
	private static int rank;
	private static DataOutputStream pw;
	private static DataInputStream input;
	private static Matrix A;
	private static String fileName;
	private static ArrayList<ArrayList<Integer>> pixelValues;
	static int imageHeight = 0, imageWidth = 0, maxPixValue;
	
	public static ArrayList<ArrayList<Integer>> p;
	private static Scanner imagereader;
	private static Scanner s;
	private static DataInputStream dis;


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		if (args != null && args.length > 0) {
			int option = Integer.parseInt(args[0]);
			switch (option) {
			
			case 1: {
				File fpntr1 = new File(args[1]);
				imagereader = new Scanner(fpntr1);
				pixelValues = new ArrayList<ArrayList<Integer>>();
				String temp=imagereader.nextLine()+" "+imagereader.nextLine();
				String tempFinal[]=temp.split(" ");
				File fpntr = new File(args[1]);
				fileName = fpntr1.getPath();
				s = new Scanner(fpntr);
				int i1=0;
				while(i1 <tempFinal.length )
				{
					s.next();
					i1++;
				}
				imageWidth = Integer.parseInt(s.next());
				imageHeight = Integer.parseInt(s.next());
				maxPixValue = Integer.parseInt(s.next());
				int i2=0;
				while(i2<imageHeight)
				{   
					ArrayList<Integer> temp1 = new ArrayList<Integer>();
				    for (int jm = 0; jm < imageWidth; jm++)
				    {
					temp1.add(Integer.parseInt(s.next()));
				    }
				    pixelValues.add(temp1);
					i2++;
				}
				
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new
		                 FileOutputStream(fileName.substring(0,fileName.indexOf("."))+ "_b" + ".pgm")));
				
				 String binaryWidth=methodClass.convrtToBinary(imageWidth);
				 String binaryHeight=methodClass.convrtToBinary(imageHeight);
				 int byteWidth1=methodClass.integerVal(binaryWidth.substring(0, 8));
				 int byteWidth2=methodClass.integerVal(binaryWidth.substring(8, 16));
				 int byteHeight1=methodClass.integerVal(binaryHeight.substring(0, 8));
				 int byteHeight2=methodClass.integerVal(binaryHeight.substring(8, 16));
				 out.writeByte(byteWidth1);
				 out.writeByte(byteWidth2);
				 out.writeByte(byteHeight1);
				 out.writeByte(byteHeight2);
				 out.writeByte(maxPixValue);
				for (ArrayList<Integer> a1 : pixelValues) {
					int i3=0;
					while(i3<a1.size())
					{   
						out.writeByte(a1.get(i3));
						i3++;
					}
				}
				out.flush();
				out.close();
				System.out.println("Binary Image Saved");
				break;

			}
			case 2: 
				File file = new File(args[1]);
				dis = new DataInputStream(new BufferedInputStream(new FileInputStream(args[1])));
				pixelValues = new ArrayList<ArrayList<Integer>>();
				fileName = file.getPath();
				try {
					PrintWriter prntwtr = new PrintWriter(fileName.substring(0,fileName.indexOf("."))+ "_2" + ".pgm");
					prntwtr.println("P2");
					prntwtr.println("# Created by IrfanView");
					byte hghtWdth[]=new byte[5];
					dis.read(hghtWdth);
					imageWidth=methodClass.integerVal( methodClass.convertToBinary(methodClass.byteToInt(hghtWdth[0]))+ methodClass.convertToBinary(methodClass.byteToInt(hghtWdth[1])));
					imageHeight=methodClass.integerVal( methodClass.convertToBinary(methodClass.byteToInt(hghtWdth[2]))+ methodClass.convertToBinary(methodClass.byteToInt(hghtWdth[3])));
					
					maxPixValue=methodClass.byteToInt(hghtWdth[4]);
					prntwtr.write(imageWidth+" ");
					prntwtr.write(imageHeight+"\n");
					prntwtr.write(maxPixValue+"\n");
					byte temp[]=new byte[imageWidth*imageHeight];
					dis.read(temp);
					int t_index=0;
					int ti=0;
					while(ti<imageHeight)
					{   
						for(int tj=0;tj<imageWidth;tj++)
					    {
						int pixel;
						if((new Byte(temp[t_index])).intValue()<0)
						{
							pixel=128+(127+(new Byte(temp[t_index])).intValue()+1);
						}
						else
							pixel=(new Byte(temp[t_index])).intValue();
						prntwtr.write(pixel+" ");
						t_index++;
					    }
					    prntwtr.write("\n");
						ti++;
					}
					prntwtr.close();
				} 
				catch (FileNotFoundException e) {
					System.out.println("output file can't be created. . . .");
				}
				
				System.out.println("Image retrived");
				break;
			

			case 3:
				processHeaderSVD(args[1],args[2]);
				ConvertToBinary(args[3]);
				break;
			case 4:
				ReadUSVfromSVDfile(args[1]);
				break;
			}
			}

	}

	private static void ReadUSVfromSVDfile(String file) {
		// TODO Auto-generated method stub
		  try
	      {
	         input = new DataInputStream(
	               new FileInputStream(file));
	         if (input.readBoolean() == true)
	         {
	            checkIfTransposed = true;
	         }
	         else
	         {
	            checkIfTransposed = false;
	         }
	         int uRowDimension = input.readShort(); // First number is row dimension
	         int vRowDimension = input.readShort(); // Second is column dimension
	         rank = input.readShort();              // Third number is rank
	         uMat = new Matrix(uRowDimension, rank);
	         sigMat = new Matrix(rank, rank);
	         vMat = new Matrix(vRowDimension, rank);
	         	
	         for (int i = 0; i < uRowDimension; i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               uMat.set(i, j, input.readShort() / 32768d);
	            }
	         }
	         for (int i = 0; i < rank; i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               if (i == j)
	               {
	                  sigMat.set(i, j, input.readFloat());
	               }
	               else
	               {
	                  sigMat.set(i, j, 0.0);
	               }
	            }
	         }
	         for (int i = 0; i < vRowDimension; i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               vMat.set(i, j, input.readShort() / 32768d);
	            }
	         }
	         input.close();
	         
	         A=uMat.times(sigMat.times(vMat.transpose()));
	         if(checkIfTransposed)
	         {
	        	 A=A.transpose();
	         }
	         double[][] arr = A.getArray();
	         
	         int[][] grays =
		               new int[A.getRowDimension()][A.getColumnDimension()];
		         int n = 0;
		         for (int i = 0; i < grays.length; i++)
		         {
		            for (int j = 0; j < grays[i].length; j++)
		            {
		               n = (int) A.get(i, j);
		               if (n < 0)
		               {
		                  grays[i][j] = 0;
		               }
		               else if (n > 255)
		               {
		                  grays[i][j] = 255;
		               }
		               else
		               {
		                  grays[i][j] = n;
		               }
		            }
		         }
		         
		         String filename = file.split("_b\\.pgm\\.")[0];
		         String target = filename + "_k.pgm";
		         methodClass.SaveImage(grays.length,grays[0].length,grays,target);
		         // methodClass.SaveImage(A,target);
			
	         
	         
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	}

	private static void ConvertToBinary(String args) {
		// TODO Auto-generated method stub
		rank=Integer.parseInt(args);
		try
	      {
			pw = new DataOutputStream(new FileOutputStream("Image_b.pgm.SVD"));
	         if (checkIfTransposed)
	         {
	            pw.writeBoolean(true);
	         }
	         else
	         {
	            pw.writeBoolean(false);
	         }
	         pw.writeShort(uMat.getRowDimension());     // height
	         pw.writeShort(vMat.getColumnDimension());  // width
	         pw.writeShort(rank);                    // rank
	         for (int i = 0; i < uMat.getRowDimension(); i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               pw.writeShort((short) (uMat.get(i, j) * 32768d));
	            }
	         }
	         for (int i = 0; i < rank; i++)
	         {
	            pw.writeFloat((float) (sigMat.get(i, i)));
	         }
	         for (int i = 0; i < vMat.getRowDimension(); i++)
	         {
	            for (int j = 0; j < rank; j++)
	            {
	               pw.writeShort((short) (vMat.get(i, j) * 32768d));
	            }
	         }
	         pw.close();
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }

		
	}

	private static void processHeaderSVD(String filename, String file2) {
		// TODO Auto-generated method stub
		  try
	      {
	         Scanner scanner = new Scanner(new File(filename));
	         int width = scanner.nextInt();
	         int height = scanner.nextInt();
	         if (width > height)
	         {
	            checkIfTransposed = true;
	          int c=height;
	          height=width;
	          width=c;
	         }
	         else
	         {
	            checkIfTransposed = false;
	           }
	         
	         uMat = new Matrix(height, width);
	            sigMat = new Matrix(width, width);
	            vMat = new Matrix(width, width);
	         scanner.close();

	         Scanner scanner2 = new Scanner(new File(file2));
	         for (int i = 0; i < uMat.getRowDimension(); i++)
	         {
	            for (int j = 0; j < uMat.getColumnDimension(); j++)
	            {
	               uMat.set(i, j, scanner2.nextDouble());
	            }
	         }
	         for (int i = 0; i < sigMat.getRowDimension(); i++)
	         {
	            for (int j = 0; j < sigMat.getColumnDimension(); j++)
	            {
	               sigMat.set(i, j, scanner2.nextDouble());
	            }
	         }
	         for (int i = 0; i < vMat.getRowDimension(); i++)
	         {
	            for (int j = 0; j < vMat.getColumnDimension(); j++)
	            {
	               vMat.set(i, j, scanner2.nextDouble());
	            }
	         }
	         scanner2.close();
	        
	         
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }

	
	}
}
