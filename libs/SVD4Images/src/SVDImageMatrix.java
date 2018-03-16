import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * SVDImageMatrix.java, a class designed to create new image matrices and
 * their metadata using matrices and if desired manual ranks.
 * 
 * <BR><BR>
 * @author Andrew Trusty
 */
public class SVDImageMatrix {

    /**
     * Takes as input an integer k, and an m×n matrix A, and returns
     * A(k), sigma 1, and sigma k+1
     * <BR><BR>
     * @param k the rank to use
     * @param A the matrix to calculate on
     */
    public static Object[] svdImageMatrix(int k, Matrix A) {
        SingularValueDecomposition svd = new SingularValueDecomposition(A);//.svd();
        double[] svdVals = svd.getSingularValues();
        double sigmaOne = svdVals[0];
        double sigmaKpOne = svdVals[k];
        
        Matrix Ak = new Matrix(A.getColumnDimension(), A.getRowDimension());
        
                           // row returns width       column returns height
        //System.out.println(A.getRowDimension() + " " + A.getColumnDimension());
        //System.out.println(Ak.getRowDimension() + " " + Ak.getColumnDimension());
        
        Matrix U = svd.getU(), V = svd.getV();
        for (int i = 0; i < k; i++) {
            Matrix currentV = V.getMatrix(0, V.getRowDimension() - 1, i, i);
            Matrix currentU = U.getMatrix(0, U.getRowDimension() - 1, i, i);
            
            Ak.plusEquals((currentV.times(svdVals[i]))
                    .times(currentU.transpose()));
        }        
        
        return new Object[]{Ak.transpose(), new Double(sigmaOne), 
                new Double(sigmaKpOne)};
    }
    
    /**
     * Does a whole ARGB matrix array to the given rank.
     * <BR><BR>
     * @param k the rank to make the image with
     * @param img matrices
     * @return new image data
     */
    public static Object[] svdImageMatrices(int k, Matrix[] img) {
    	Matrix[] newIMG = new Matrix[4];
        double[] sigmas = new double[4];
        double[] lastsigmas = new double[4];
        Object[] tmp;
        
        newIMG[0] = (Matrix) (tmp = svdImageMatrix(k, img[0]))[0];
        sigmas[0] = ((Double) tmp[1]).doubleValue();
        lastsigmas[0] = ((Double) tmp[2]).doubleValue();
        newIMG[1] = (Matrix) (tmp = svdImageMatrix(k, img[1]))[0];
        sigmas[1] = ((Double) tmp[1]).doubleValue();
        lastsigmas[1] = ((Double) tmp[2]).doubleValue();
        newIMG[2] = (Matrix) (tmp = svdImageMatrix(k, img[2]))[0];
        sigmas[2] = ((Double) tmp[1]).doubleValue();
        lastsigmas[2] = ((Double) tmp[2]).doubleValue();
        newIMG[3] = (Matrix) (tmp = svdImageMatrix(k, img[3]))[0];
        sigmas[3] = ((Double) tmp[1]).doubleValue();
        lastsigmas[3] = ((Double) tmp[2]).doubleValue();
        
        return new Object[]{newIMG, sigmas, lastsigmas};
    }
    /**
     * Does a whole ARGB matrix array on the best calculated rank.
     * <BR><BR>
     * @param img matrices
     * @return new image data
     */
    public static Object[] svdAutoImageMatrices(Matrix[] img) {
        Matrix[] newIMG = new Matrix[4];
        double[] sigmas = new double[4];
        double[] lastsigmas = new double[4];
        Object[] tmp;
        Integer big, newest;
        
        newIMG[0] = (Matrix) (tmp = svdAutoImageMatrix(img[0]))[0];
        sigmas[0] = ((Double) tmp[1]).doubleValue();
        lastsigmas[0] = ((Double) tmp[2]).doubleValue();
        big = (Integer) tmp[3];
        
        newIMG[1] = (Matrix) (tmp = svdAutoImageMatrix(img[1]))[0];
        sigmas[1] = ((Double) tmp[1]).doubleValue();
        lastsigmas[1] = ((Double) tmp[2]).doubleValue();
        newest = (Integer) tmp[3];
        if (newest.intValue() > big.intValue()) {
        	big = newest;
        }
        
        newIMG[2] = (Matrix) (tmp = svdAutoImageMatrix(img[2]))[0];
        sigmas[2] = ((Double) tmp[1]).doubleValue();
        lastsigmas[2] = ((Double) tmp[2]).doubleValue();
        newest = (Integer) tmp[3];
        if (newest.intValue() > big.intValue()) {
            big = newest;
        }
        
        newIMG[3] = (Matrix) (tmp = svdAutoImageMatrix(img[3]))[0];
        sigmas[3] = ((Double) tmp[1]).doubleValue();
        lastsigmas[3] = ((Double) tmp[2]).doubleValue();
        newest = (Integer) tmp[3];
        if (newest.intValue() > big.intValue()) {
            big = newest;
        }
        
        return new Object[]{newIMG, sigmas, lastsigmas, big};
    }
    
    /**
     * Auto-Calculates the best rank image of the given matrix data.
     * <BR><BR>
     * @param A the matrix to calculate on
     **/
    public static Object[] svdAutoImageMatrix(Matrix A) {
        SingularValueDecomposition svd = new SingularValueDecomposition(A);
        double[] svdVals = svd.getSingularValues();
        int singular_values = 1;
        
        Matrix Ak = new Matrix(A.getColumnDimension(), A.getRowDimension());
        
                           // row returns width       column returns height
        //System.out.println(A.getRowDimension() + " " + A.getColumnDimension());
        //System.out.println(Ak.getRowDimension() + " " + Ak.getColumnDimension());
        
        double sigmaOne = svdVals[0];
        double sigmaKpOne = svdVals[svdVals.length - 1];
        Matrix U = svd.getU(), V = svd.getV();
        
        int i;
        for (i = 0; i < svdVals.length; i++) {
            singular_values = i + 1;
            if (svdVals[i] < (svdVals[0] * .01)) {
                
                singular_values = i; // set to previous rank
                sigmaKpOne = svdVals[singular_values - 1];
                break;
            }
            
            Matrix currentV = V.getMatrix(0, V.getRowDimension() - 1, i, i);
            Matrix currentU = U.getMatrix(0, U.getRowDimension() - 1, i, i);
            
            Ak.plusEquals((currentV.times(svdVals[i]))
                    .times(currentU.transpose()));
        }
        
        return new Object[]{Ak.transpose(), new Double(sigmaOne), 
                new Double(sigmaKpOne), new Integer(singular_values)};
    }
    
    
    
    /*****************************
     *  .SVD ENCODING METHODS    *
     *****************************/
    
    /**
     * Encodes the given matrix to SVD format using the given approximation.
     * <BR><BR>
     * @param k rank approximation to use
     * @param A matrix to calculate with
     * @return encoded matrix
     */
    public static Object[] svdImageEncodedMatrices(int k, Matrix A) {
        SingularValueDecomposition svd = new SingularValueDecomposition(A);
        double[] svdVals = svd.getSingularValues();
        
        // get the SVD vals we need
        double[] sigmas = new double[k];
        for (int j = 0; j < k; j++) {
            sigmas[j] = svdVals[j];
        }
        
        Matrix[] Umatrices = new Matrix[k];
        Matrix[] Vmatrices = new Matrix[k];
        
        Matrix U = svd.getU(), V = svd.getV();
        for (int i = 0; i < k; i++) {
            Vmatrices[i] = V.getMatrix(0, V.getRowDimension() - 1, i, i);
            Umatrices[i] = U.getMatrix(0, U.getRowDimension() - 1, i, i).transpose();
        }
        
        return new Object[]{sigmas, Umatrices, Vmatrices};
    }
    
    /**
     * Encodes the given matrix to SVD format deciding on best approximation.
     * <BR><BR>
     * @param A matrix to encode
     * @return the encoded matrix
     */
    public static Object[] svdAutoImageEncodedMatrices(Matrix A) {
        SingularValueDecomposition svd = new SingularValueDecomposition(A);
        double[] svdVals = svd.getSingularValues();
        
        // detemine how many Singular values i need
        int j;
        for (j = 0; j < svdVals.length; j++) {
            if (svdVals[j] < (svdVals[0] * .01)) {
                break;
            }
        }
        
        // get the SVD vals we need
        double[] sigmas = new double[j];
        for (int k = 0; k < j; k++) {
            sigmas[k] = svdVals[k];
        }
        
        Matrix[] Umatrices = new Matrix[j];
        Matrix[] Vmatrices = new Matrix[j];
        
        Matrix U = svd.getU(), V = svd.getV();
        for (int i = 0; i < j; i++) {
            Vmatrices[i] = V.getMatrix(0, V.getRowDimension() - 1, i, i);
            Umatrices[i] = U.getMatrix(0, U.getRowDimension() - 1, i, i).transpose();
        }
        
        return new Object[]{sigmas, Umatrices, Vmatrices};
    }
    
    /**
     * Decodes the given SVD encoded Object array to a valid image matrix.
     * <BR><BR>
     * @param encoded matrix
     * @return image matrix
     */
    public static Matrix svdDecodeObject(Object[] encoded) {
        double[] svdVals = (double[]) encoded[0];
        
        Matrix[] Umatrices = (Matrix[]) encoded[1];
        Matrix[] Vmatrices = (Matrix[]) encoded[2];

        Matrix Ak = new Matrix(Vmatrices[0].getRowDimension(),
                Umatrices[0].getColumnDimension());
        
        for (int i = 0; i < svdVals.length; i++) {
            Matrix currentV = Vmatrices[i];
            Matrix currentU = Umatrices[i];
            
            Ak.plusEquals((currentV.times(svdVals[i]))
                    .times(currentU));
        }
        return Ak.transpose();
    }
}