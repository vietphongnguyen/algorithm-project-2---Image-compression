import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import Jama.Matrix;

/**
 * Decodes the given file to a normal PNG image file.
 * 
 * <BR><BR>
 * @author Andrew Trusty
 */
public class DecodeSVD {

    /**
     * Asks user for an EncodeSVD encoded image and converts it to a PNG
     * image.
     * <BR><BR>
     * @param args
     */
	public static void main(String[] args) {
		String command = "";
        BufferedReader bfin = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            System.out.println("Enter a valid image file (svd) to decode:");
            command = bfin.readLine();   
            
            ObjectInputStream oin = new ObjectInputStream(
                    new FileInputStream(command));
            Object[] encoded;
            encoded = (Object[]) oin.readObject();
            
            // should be 4 object[] in encoded, need to decode all 4 and write
            // out as a PNG image
            Matrix[] matrices = new Matrix[4];
            matrices[0] = SVDImageMatrix.svdDecodeObject((Object[]) encoded[0]);
            matrices[1] = SVDImageMatrix.svdDecodeObject((Object[]) encoded[1]);
            matrices[2] = SVDImageMatrix.svdDecodeObject((Object[]) encoded[2]);
            matrices[3] = SVDImageMatrix.svdDecodeObject((Object[]) encoded[3]);
            
            // convert matrix to image
            String filename = command.substring(0, command.length() - 4) + "_DEC";
            filename += ".png";
            ImageMatrixConversion.writeSVDImage(filename, matrices, "png");
            System.out.println("File decoded to " + filename);
        } catch (IOException e) {
            System.err.println("IOException in "
                    + "DevcodeSVD.main();\n" + e.toString());
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException in "
                    + "DevcodeSVD.main();\n" + cnfe.toString());
        }
    }
}
