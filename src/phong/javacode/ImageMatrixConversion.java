package phong.javacode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Jama.Matrix;
/**
 * Image2Matrix.java, a class designed to load images and convert them
 * to matrices and vice versa.
 * 
 * <BR><BR>
 * @author Andrew
 */

public class ImageMatrixConversion {

    /**
     * Loads and returns an image file denoted by the given string.
     * <BR><BR>
     * @param fileName of image
     * @return image as a PixelImage
     */
    public static BufferedImage loadImage(String imageToLoad) {
        Image loadImage = new ImageIcon(imageToLoad).getImage();
        return loadImage(loadImage);
    }
    /**
     * Converts the given image to a buffered image.
     * <BR><BR>
     * @param loadImage the image to convert
     * @return new buffered iamge
     */
    public static BufferedImage loadImage(Image loadImage) {
        BufferedImage retImage = new
        BufferedImage(loadImage.getWidth(null),
                      loadImage.getHeight(null),
                      BufferedImage.TYPE_INT_ARGB);
        retImage.createGraphics().drawImage(loadImage, 0, 0, null);
        return retImage;
    }
    
    /**
     * Converts the given floating point matrix to a PixelImage.
     * <BR><BR>
     * @param matrix to convert to an image
     * @return PixelImage of the matrix
     */
    public static BufferedImage loadImage(float[][] matrix) {
        int[] rgbs = new int[matrix.length * matrix[0].length];
        int k = 0;
        for (int i = 0; i < matrix.length; i++) {
    		for (int j = 0; j < matrix[0].length; j++) {
                rgbs[k] = (int) matrix[i][j];
                k++;
            }
        }
        return matrix2Image(array2Matrix(rgbs, matrix.length, 
                matrix[0].length));
    }
    
    /**
     * Converts a buffered image to an array of Matrices holding
     * the pixel values.
     * <BR><BR>
     * @param buffImg the image
     * @return the matrices
     */
    public static Matrix[] image2Matrix(BufferedImage buffImg) {
        int w = buffImg.getWidth();
        int h = buffImg.getHeight();
        int[] rgbs = new int[w*h]; // r g b - all stored in each int
        // get the pixel data to rgbs matrix
        buffImg.getRGB(0, 0, w, h, rgbs, 0, w);
        return array2Matrix(rgbs, w, h);
    }    
    
    /**
     * Converts a buffered images getRGB array to a matrix array.
     * <BR><BR>
     * @param rgbs the buffered image data array
     * @param w the image width
     * @param h the image height
     * @return the matrices containing the buffered image data
     */
    private static Matrix[] array2Matrix(int[] rgbs, int w, int h) {
    	// change array to a matrix
        Matrix[] RGBmatrices = new Matrix[]{new Matrix(w, h),
                new Matrix(w, h), new Matrix(w, h), new Matrix(w, h)};

        int k = 0, a, r, g, b;
        for (int i = 0; i < w; i++) {
           for (int j = 0; j < h; j++) {
               // set the Alpha pixel value
               a = (rgbs[k] >> 24) & 255;
               RGBmatrices[0].set(i, j, a);
               // set the Red pixel value
               r = (rgbs[k] >> 16) & 255;
               RGBmatrices[1].set(i, j, r);
               // set the Green pixel value
               g = (rgbs[k] >> 8) & 255;
               RGBmatrices[2].set(i, j, g);
               // set the Blue pixel value
               b = rgbs[k] & 255;
               RGBmatrices[3].set(i, j, b);
               
               k++;
           }
        }
        return RGBmatrices;   
    }
    
    /**
     * Correct double to int conversions for the purpose of image pixel
     * value.
     * <BR><BR>
     * @param m the matrix to check in
     * @param i the row number 
     * @param j the column number
     */
    private static void correctDouble(Matrix m, int i, int j) {
        if (m.get(i, j) > 255) {
            m.set(i, j, 255);
        } else if (m.get(i, j) < 0) {
            m.set(i, j, 0);
        }
    }
    
    /**
     * Converts a set of matrices to a buffered image.
     * <BR><BR>
     * @param matrices to convert
     * @return buffered image
     */
    public static BufferedImage matrix2Image(Matrix[] matrices) {
        int h = matrices[0].getColumnDimension();
        int w = matrices[0].getRowDimension();
        int[] rgbs = new int[w*h];
        
        int k = 0, argb;
        for (int i = 0; i < w; i++) {
           for (int j = 0; j < h; j++) {
              // get the Alpha component
              correctDouble(matrices[0], i, j);
              argb = ((int) matrices[0].get(i, j)) << 24;
              // get the Red component
              correctDouble(matrices[1], i, j);
              argb += ((int) matrices[1].get(i, j)) << 16;
              // get the Green component
              correctDouble(matrices[2], i, j);
              argb += ((int) matrices[2].get(i, j)) << 8;
              // get the Blue component
              correctDouble(matrices[3], i, j);
              argb += (int) matrices[3].get(i, j);
              
              rgbs[k] = argb;
              k++;
           }
        }
        // create a new bufferedimage
        BufferedImage newbuffImg = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        // set matrix data into image
        newbuffImg.setRGB(0, 0, w, h, rgbs, 0, w);
        return newbuffImg;
    }
    
    /**
     * Writes out the given matrices image as the given named file.
     * <BR><BR>
     * @param name of file
     * @param img the image matrices
     */
    public static void writeSVDImage(String name, Matrix[] img, String type) {
        BufferedImage imgdata = ImageMatrixConversion.matrix2Image(img);
        
        try {
            ImageIO.write(imgdata, type, new File(name));
        } catch (IOException oie) {
            System.err.println("IOException writing image " + name);
        }     
    }    
}
