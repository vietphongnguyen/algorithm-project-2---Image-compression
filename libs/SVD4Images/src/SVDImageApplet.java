import java.applet.Applet;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Jama.Matrix;


/**
 * SVDImageApplet.java, a class designed to provide an example of the workings
 * of ImageMatrixConversion.java and SVDImageMatrix.java.
 *
 * <BR><BR>
 * @author Andrew Trusty
 */
public class SVDImageApplet extends Applet {
    /** GUI objects **/
    private JButton sunsetBtn;
    private JButton earthBtn;
    private JButton einsteinBtn;
    private JPanel mainPanel;
    private JLabel jLabel4;
    private JPanel rankTwoImgPanel;
    private JPanel rankFiveImgPanel;
    private JPanel myImgPanel;
    private JPanel customImgPanel;
    private JPanel rankTwoPanel;
    private JPanel rankFivePanel;
    private JPanel rankTenPanel;
    private JPanel customPanel;
    private JPanel innerMainPanel;
    private JTextField rankTwoSigmaTwo;
    private JTextField rankTwoSigmaOne;
    private JTextField rankTenSigmaTen;
    private JTextField rankTenSigmaOne;
    private JTextField rankFiveSigmaFive;
    private JTextField rankFiveSigmaOne;
    private JTextField customSigmaLast;
    private JTextField customSigmaOne;
    private JLabel overRideMsg;
    private JCheckBox autoK;
    private JLabel jLabel6;
    private JLabel jLabel5;
    private JLabel jLabel3;
    private JLabel jLabel2;
    private JLabel jLabel1;
    private JLabel sigmaCustomLbl;
    private JLabel sigc;
    private JLabel rankTwo;
    private JLabel rankFive;
    private JLabel rankTen;
    private JLabel rankCustom;
    private JButton generateImgs;
    private JTextField kInputField;
    private JLabel kInput;
    private JPanel optionsPanel;
    private JPanel imgButtons;
    private JLabel customImg;

    /** Setup the image matrices **/
    private Matrix[] einsteinMatrices;
    private Matrix[] earthMatrices;
    private Matrix[] sunsetMatrices;

    /** Setup the GUI values **/
    private int selected;
    private boolean auto;
    /** Images used **/
    private Image ei, ea, sun;

    /**
     * Runs the SVD algorithm on the selected matrix using the
     * inputed parameters.
     */
    private void generate() {
        Matrix[] toUse = null;

        switch (selected) {
            case 1: toUse = einsteinMatrices;
                break;
            case 2: toUse = earthMatrices;
                break;
            case 3: toUse = sunsetMatrices;
                break;
            default: break;
        }
        
        Object[] data = SVDImageMatrix.svdImageMatrices(2, toUse); // issue here
        Matrix[] newImg = (Matrix[]) data[0];
        BufferedImage svdImage = ImageMatrixConversion.matrix2Image(newImg); 
        double[] sigmas = (double[]) data[1];
        double[] lastsigmas = (double[]) data[2];
        Graphics g = rankTwoImgPanel.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 148, 128);
        g.drawImage(svdImage, 0, 0, null);
        rankTwoSigmaOne.setText("" + sigmas[1]);
        rankTwoSigmaTwo.setText("" + lastsigmas[1]);
        
        data = SVDImageMatrix.svdImageMatrices(5, toUse);
        newImg = (Matrix[]) data[0];
        svdImage = ImageMatrixConversion.matrix2Image(newImg);
        sigmas = (double[]) data[1];
        lastsigmas = (double[]) data[2];
        g = rankFiveImgPanel.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 148, 128);
        g.drawImage(svdImage, 0, 0, null);
        rankFiveSigmaOne.setText("" + sigmas[1]);
        rankFiveSigmaFive.setText("" + lastsigmas[1]);

        data = SVDImageMatrix.svdImageMatrices(10, toUse);
        newImg = (Matrix[]) data[0];
        svdImage = ImageMatrixConversion.matrix2Image(newImg);
        sigmas = (double[]) data[1];
        lastsigmas = (double[]) data[2];
        g = customImgPanel.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 148, 128);
        g.drawImage(svdImage, 0, 0, null);
        rankTenSigmaOne.setText("" + sigmas[1]);
        rankTenSigmaTen.setText("" + lastsigmas[1]);

        // unless choose best K is selected
        int rank = Integer.parseInt(kInputField.getText());

        if (auto) {
            data = SVDImageMatrix.svdAutoImageMatrices(toUse);
            rank = ((Integer) data[3]).intValue();
            kInputField.setText("" + rank);
        } else {
            data = SVDImageMatrix.svdImageMatrices(rank, toUse);
        }

        rankCustom.setText("Rank " + rank + " Approximation");
        sigmaCustomLbl.setText("Sigma(" + rank + "):");
        newImg = (Matrix[]) data[0];
        svdImage = ImageMatrixConversion.matrix2Image(newImg);
        sigmas = (double[]) data[1];
        lastsigmas = (double[]) data[2];
        g = myImgPanel.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 148, 128);
        g.drawImage(svdImage, 0, 0, null);
        customSigmaOne.setText("" + sigmas[1]); // R value
        customSigmaLast.setText("" + lastsigmas[1]);
    }

    /**
     * Load the necessary images.
     */
    public void loadImages() {
    	// Load and track images
        MediaTracker tracker = new MediaTracker(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        ei = toolkit.getImage("einstein.jpg");
        ea = toolkit.getImage("earth.jpg");
        sun = toolkit.getImage("sunset.jpg");
        
        tracker.addImage(ei, 0);
        tracker.addImage(ea, 1);
        tracker.addImage(sun, 2);
        
        try {
            tracker.waitForID(0);
            tracker.waitForID(1);
            tracker.waitForID(2);
            } catch (InterruptedException e) {
              // uh oh...
        }
        
        einsteinBtn.setIcon(new ImageIcon(ei));
        earthBtn.setIcon(new ImageIcon(ea));
        sunsetBtn.setIcon(new ImageIcon(sun));
        
        einsteinMatrices = ImageMatrixConversion.image2Matrix(
                ImageMatrixConversion.loadImage(ei));
        earthMatrices = ImageMatrixConversion.image2Matrix(
                ImageMatrixConversion.loadImage(ea));
        sunsetMatrices = ImageMatrixConversion.image2Matrix(
                ImageMatrixConversion.loadImage(sun));
    }

    
    /**
     * Applet implementation of init.
     */
    public void init() {
        setLayout(null);
        setSize(654, 475);
        setBackground(new java.awt.Color(0, 64, 128));
        
        selected = 1;
        auto = false;
        
        // init buttons first
        einsteinBtn = new JButton();
        earthBtn = new JButton();
        sunsetBtn = new JButton();
        generateImgs = new JButton();
        
        loadImages();
        initGUI();
    }
    /**
     * Initializes all the GUI components.
     */
    private void initGUI() {
        customImg = new JLabel();
        customImg.setBounds(10, 121, 148, 128);
        mainPanel = new JPanel();

        BoxLayout mainPanelLayout = new BoxLayout(mainPanel,
                javax.swing.BoxLayout.Y_AXIS);
        mainPanel.setLayout(mainPanelLayout);
        add(mainPanel);
        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBounds(195, 5, 455, 465);

        optionsPanel = new JPanel();
        mainPanel.add(optionsPanel);

        FlowLayout optionsPanelLayout = new FlowLayout();
        optionsPanel.setPreferredSize(new java.awt.Dimension(460, 60));
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanel.setBackground(new java.awt.Color(255, 255, 255));
        kInput = new JLabel();
        optionsPanel.add(kInput);
        kInput.setText("Input custom K value: ");
        kInput.setBounds(63, 0, 128, 26);
        kInputField = new JTextField();
        optionsPanel.add(kInputField);
        kInputField.setText("25");
        kInputField.setPreferredSize(new java.awt.Dimension(47, 20));
        kInputField.setBounds(191, 1, 70, 26);
        //generateImgs.setEnabled(false);
        optionsPanel.add(generateImgs);
        generateImgs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    generate();
                }
            });
        generateImgs.setText("Generate New Images");
        generateImgs.setBounds(62, 28, 199, 26);
        autoK = new JCheckBox();
        optionsPanel.add(autoK);
        autoK.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    auto = !auto;

                    if (auto) {
                        kInputField.enable(false);
                    } else {
                        kInputField.enable(true);
                    }
                }
            });
        autoK.setText("Choose Best K");
        autoK.setBounds(267, 4, 121, 20);
        autoK.setBackground(new java.awt.Color(255, 255, 255));

        overRideMsg = new JLabel();
        optionsPanel.add(overRideMsg);
        overRideMsg.setText("( Overrides custom K )");
        overRideMsg.setBounds(270, 20, 148, 22);

        innerMainPanel = new JPanel();
        mainPanel.add(innerMainPanel);

        GridLayout jPanel1Layout1 = new GridLayout(2, 2);
        innerMainPanel.setPreferredSize(new java.awt.Dimension(455, 400));
        jPanel1Layout1.setColumns(2);
        jPanel1Layout1.setRows(2);
        innerMainPanel.setLayout(jPanel1Layout1);
        customPanel = new JPanel();
        innerMainPanel.add(customPanel);
        customPanel.setBackground(new java.awt.Color(255, 255, 255));
        customPanel.setLayout(null);
        customPanel.setAutoscrolls(true);

        rankCustom = new JLabel();
        customPanel.add(rankCustom);
        rankCustom.setText("Rank 25 Approximation");
        rankCustom.setBounds(5, 5, 145, 15);

        sigc = new JLabel();
        customPanel.add(sigc);
        sigc.setText("Sigma(1): ");
        sigc.setBounds(5, 20, 60, 20);

        customSigmaOne = new JTextField();
        customPanel.add(customSigmaOne);
        customSigmaOne.setBounds(85, 20, 130, 20);

        sigmaCustomLbl = new JLabel();
        customPanel.add(sigmaCustomLbl);
        sigmaCustomLbl.setText("Sigma(25):");
        sigmaCustomLbl.setBounds(5, 40, 80, 20);

        customSigmaLast = new JTextField();
        customPanel.add(customSigmaLast);
        customSigmaLast.setBounds(85, 40, 130, 20);

        myImgPanel = new JPanel();
        customPanel.add(myImgPanel);
        myImgPanel.setLayout(null);
        myImgPanel.setBounds(45, 65, 150, 130);
        myImgPanel.setBackground(new java.awt.Color(255, 255, 255));

        rankTenPanel = new JPanel();
        innerMainPanel.add(rankTenPanel);
        rankTenPanel.setBackground(new java.awt.Color(255, 255, 255));
        rankTenPanel.setLayout(null);

        customImgPanel = new JPanel();
        rankTenPanel.add(customImgPanel);
        customImgPanel.setLayout(null);
        customImgPanel.setBounds(50, 65, 150, 130);
        customImgPanel.setBackground(new java.awt.Color(255, 255, 255));

        rankTenSigmaOne = new JTextField();
        rankTenPanel.add(rankTenSigmaOne);
        rankTenSigmaOne.setBounds(75, 20, 120, 20);
        rankTenSigmaOne.setPreferredSize(new java.awt.Dimension(4, 20));

        rankTenSigmaTen = new JTextField();
        rankTenPanel.add(rankTenSigmaTen);
        rankTenSigmaTen.setBounds(75, 40, 120, 20);
        rankTenSigmaTen.setPreferredSize(new java.awt.Dimension(4, 20));

        rankTen = new JLabel();
        rankTenPanel.add(rankTen);
        rankTen.setText("Rank 10 Approximation");
        rankTen.setBounds(10, 5, 145, 15);
        rankTen.setPreferredSize(new java.awt.Dimension(132, 16));

        jLabel3 = new JLabel();
        rankTenPanel.add(jLabel3);
        jLabel3.setText("Sigma(1): ");
        jLabel3.setBounds(10, 20, 60, 15);
        jLabel3.setPreferredSize(new java.awt.Dimension(57, 16));

        jLabel6 = new JLabel();
        rankTenPanel.add(jLabel6);

        jLabel6.setText("Sigma(10):");
        jLabel6.setBounds(10, 40, 65, 15);
        jLabel6.setPreferredSize(new java.awt.Dimension(61, 16));

        rankFivePanel = new JPanel();
        innerMainPanel.add(rankFivePanel);
        rankFivePanel.setBackground(new java.awt.Color(255, 255, 255));
        rankFivePanel.setLayout(null);

        rankFiveSigmaOne = new JTextField();
        rankFivePanel.add(rankFiveSigmaOne);
        rankFiveSigmaOne.setBounds(85, 20, 140, 20);

        rankFiveSigmaFive = new JTextField();
        rankFivePanel.add(rankFiveSigmaFive);
        rankFiveSigmaFive.setBounds(85, 40, 140, 20);

        jLabel1 = new JLabel();
        rankFivePanel.add(jLabel1);
        jLabel1.setText("Sigma(1): ");
        jLabel1.setBounds(10, 20, 60, 15);

        rankFive = new JLabel();
        rankFivePanel.add(rankFive);
        rankFive.setText("Rank 5 Approximation");
        rankFive.setBounds(10, 5, 125, 15);

        jLabel4 = new JLabel();
        rankFivePanel.add(jLabel4);
        jLabel4.setText("Sigma(5):");
        jLabel4.setBounds(10, 40, 60, 15);

        rankFiveImgPanel = new JPanel();
        rankFivePanel.add(rankFiveImgPanel);
        rankFiveImgPanel.setBounds(45, 65, 150, 130);
        rankFiveImgPanel.setBackground(new java.awt.Color(255, 255, 255));

        rankTwoPanel = new JPanel();
        innerMainPanel.add(rankTwoPanel);
        rankTwoPanel.setBackground(new java.awt.Color(255, 255, 255));
        rankTwoPanel.setLayout(null);

        rankTwoSigmaOne = new JTextField();
        rankTwoPanel.add(rankTwoSigmaOne);
        rankTwoSigmaOne.setBounds(80, 20, 120, 20);

        rankTwoSigmaTwo = new JTextField();
        rankTwoPanel.add(rankTwoSigmaTwo);
        rankTwoSigmaTwo.setBounds(80, 40, 120, 20);

        jLabel2 = new JLabel();
        rankTwoPanel.add(jLabel2);
        jLabel2.setText("Sigma(1): ");
        jLabel2.setBounds(15, 25, 60, 15);

        rankTwo = new JLabel();
        rankTwoPanel.add(rankTwo);
        rankTwo.setText("Rank 2 Approximation");
        rankTwo.setBounds(15, 5, 125, 15);

        jLabel5 = new JLabel();
        rankTwoPanel.add(jLabel5);
        jLabel5.setText("Sigma(2):");
        jLabel5.setBounds(15, 45, 60, 15);

        rankTwoImgPanel = new JPanel();
        rankTwoPanel.add(rankTwoImgPanel);
        rankTwoImgPanel.setBounds(50, 65, 150, 130);
        rankTwoImgPanel.setBackground(new java.awt.Color(255, 255, 255));

        imgButtons = new JPanel();
        add(imgButtons);

        GridLayout jPanel1Layout = new GridLayout(3, 1);
        imgButtons.setBounds(6, 5, 182, 414);
        jPanel1Layout.setRows(3);
        imgButtons.setLayout(jPanel1Layout);

        imgButtons.add(einsteinBtn);
        einsteinBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selected = 1;
                }
            });
        einsteinBtn.setBackground(new java.awt.Color(0, 0, 0));
        
        imgButtons.add(earthBtn);
        earthBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selected = 2;
                }
            });
        earthBtn.setBackground(new java.awt.Color(0, 0, 0));

        imgButtons.add(sunsetBtn);
        sunsetBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selected = 3;
                }
            });
        sunsetBtn.setPreferredSize(new java.awt.Dimension(182, 136));
        sunsetBtn.setBackground(new java.awt.Color(0, 0, 0));
    }
    
    /**
     * Constructor for java application running.
     */
    public SVDImageApplet() {
        super();
        init();
    }
    
    /**
     * Allows the applet to be run as a java application.
     * <BR><BR>
     * @param args
     */
    public static void main(String[] args) {
    	JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SVDImageApplet inst = new SVDImageApplet();
        frame.getContentPane().add(inst);
        ((JComponent)frame.getContentPane()).setPreferredSize(inst.getSize());
        frame.pack();
        frame.setVisible(true);
    }
}
