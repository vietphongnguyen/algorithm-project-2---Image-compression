/**
 * 
 */
package phong.javacode;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import Jama.Matrix;

public class EncodeSVD {

    /**
     * Asks user for an image and converts it to a re-encoded PNG
     * image or an SVD image.
     * <BR><BR>
     * @param args cmd line arguments.
     */
	public static void main(String[] args) {
        String command = "", k = "", type = "";
        BufferedReader bfin = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            System.out.println("Enter a valid image file (jpg, png, gif) to encode:");
            command = bfin.readLine();
            System.out.println("Enter an K to compress with or press Enter"
                    + " to let the program decide for you:");
                
            k = bfin.readLine();
           /* System.out.println("Would you like to encode to (png) or (svd) encoding?");
            System.out.println("or press Enter to use the default " + "(svd) encoding:");
            type = bfin.readLine();*/
            type = "svd";
            
        } catch (IOException e) {
            System.err.println("IOException in "
                    + "EnvcodeSVD.main();\n" + e.toString());
        }
        
        // load image
        BufferedImage pxImg = ImageMatrixConversion.loadImage(command);
        // convert to pixels
        Matrix[] img = ImageMatrixConversion.image2Matrix(pxImg);
        
        
        String filename = command.substring(0, command.length() - 4);
        if (type.equals("") || type.equals("svd")) { // ENCODE TO .SVD
            Object[] encoded = new Object[4];
            if (k.equals("")) {
                // do AUTO encoding
                encoded[0] = SVDImageMatrix.svdAutoImageEncodedMatrices(img[0]);
                encoded[1] = SVDImageMatrix.svdAutoImageEncodedMatrices(img[1]);
                encoded[2] = SVDImageMatrix.svdAutoImageEncodedMatrices(img[2]);
                encoded[3] = SVDImageMatrix.svdAutoImageEncodedMatrices(img[3]);
            } else {
                // encode with given k
                int kval = Integer.parseInt(k);
                encoded[0] = SVDImageMatrix.svdImageEncodedMatrices(kval, img[0]);
                encoded[1] = SVDImageMatrix.svdImageEncodedMatrices(kval, img[1]);
                encoded[2] = SVDImageMatrix.svdImageEncodedMatrices(kval, img[2]);
                encoded[3] = SVDImageMatrix.svdImageEncodedMatrices(kval, img[3]);
            }
            
            // write encoded SVD image to new file
            filename += ".svd";
            try {
            	ObjectOutputStream oout = new ObjectOutputStream(
            			new FileOutputStream(filename));
                oout.writeObject(encoded);
                oout.flush();
                oout.close();
            } catch (IOException ioe) {
            	System.err.println("Unable to write encoded matrix.");
            }
        } else { // ENCODE TO INPUTTED TYPE
            Matrix[] matrices = new Matrix[4];
            if (k.equals("")) {
                // do AUTO encoding
                matrices[0] = (Matrix) (SVDImageMatrix.svdAutoImageMatrix(img[0]))[0];
                matrices[1] = (Matrix) (SVDImageMatrix.svdAutoImageMatrix(img[1]))[0];
                matrices[2] = (Matrix) (SVDImageMatrix.svdAutoImageMatrix(img[2]))[0];
                matrices[3] = (Matrix) (SVDImageMatrix.svdAutoImageMatrix(img[3]))[0];
            } else {
            	// encode with given k
                int kval = Integer.parseInt(k);
                matrices[0] = (Matrix) (SVDImageMatrix.svdImageMatrix(kval, img[0]))[0];
                matrices[1] = (Matrix) (SVDImageMatrix.svdImageMatrix(kval, img[1]))[0];
                matrices[2] = (Matrix) (SVDImageMatrix.svdImageMatrix(kval, img[2]))[0];
                matrices[3] = (Matrix) (SVDImageMatrix.svdImageMatrix(kval, img[3]))[0];
            }
            filename += "_ENC." + type;
            ImageMatrixConversion.writeSVDImage(filename, matrices, type);
        }
        
        System.out.println("File encoded to " + filename);
	}
}
