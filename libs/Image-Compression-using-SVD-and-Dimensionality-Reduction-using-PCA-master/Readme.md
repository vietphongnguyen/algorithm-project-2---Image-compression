Jar files used:
Jama-1.0.1.jar for SVD

Description:

1. You can Deploy our project by extracting all the files into a folder
2. Please include Jama-1.0.1.jar file into the class path for compiling and execution, syntax given below

javac -cp ".;Jama-1.0.1.jar" methodClass.java


Execution:

  1. Reads the input PGM(Portable Gray Map-P2) file and convert the image into a binary file (P5 PGM). Also creates header.txt and SVD.txt files
		(Saves as filename_b.pgm),
		
		Execution:
		java -cp ".;Jama-1.0.1.jar" ProcessSVD 1 image.pgm
		
  2. Reverses what is done to the PGM in module 1 
		(Saves image as filename2.pgm)
		
		Execution:
		java -cp ".;Jama-1.0.1.jar" ProcessSVD 2 image_b.pgm
		
  3. Find out the rank approximation and save the result in SVD format (Header.txt, SVD.txt and Rank)
		(Saves as image_b.pgm.SVD)
		
		Execution:
		java -cp ".;Jama-1.0.1.jar" ProcessSVD 3 Header.txt SVD.txt 15
		
  4. Given an SVD file reconstruct the image that has an introduced error
		(Saves as image_k.pgm)
		
		Execution:
		java -cp ".;Jama-1.0.1.jar" ProcessSVD 4 image_b.pgm.SVD
		