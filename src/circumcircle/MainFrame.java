package circumcircle;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.Canvas;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame {

	private JFrame frmCircumcircle;
	
	private static int drawFlg = 0;	 // Flag for checking circumcircle menu item is selected
	private int[] xPoints = new int[3]; // x coordinates of three points
	private int[] yPoints = new int[3]; // y coordinates of three points
	private int pointCount; // count points of specified
	private double centerX; // x coordinates of circumcircle
	private double centerY; // y coordinates of circumcircle
	private double radius; // radius of circumcircle
	private static int screenWidth;
	private static int screenHeight;
	private int hValue; // value of x slider
	private int vValue; // value of y slider
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
				    window.frmCircumcircle.setExtendedState(JFrame.MAXIMIZED_BOTH);
					window.frmCircumcircle.setVisible(true);
					screenWidth = window.frmCircumcircle.getWidth();
					screenHeight = window.frmCircumcircle.getHeight();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		this.pointCount = 0;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCircumcircle = new JFrame();
		frmCircumcircle.setResizable(false);
		frmCircumcircle.setTitle("Circumcircle");
		frmCircumcircle.setBounds(100, 100, 758, 535);
		frmCircumcircle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmCircumcircle.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Menu");
		menuBar.add(mnNewMenu);
		
		JMenuItem circumcircleMenuItem = new JMenuItem("Curcumcircle");
		mnNewMenu.add(circumcircleMenuItem);
		circumcircleMenuItem.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	drawFlg = 1;	//Enable drawing
		    }
		});
		
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		mnNewMenu.add(quitMenuItem);
		quitMenuItem.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	frmCircumcircle.dispatchEvent(new WindowEvent(frmCircumcircle, WindowEvent.WINDOW_CLOSING));	//Close app
		    }
		});
				
		JSlider horizontalSlider = new JSlider();
		horizontalSlider.setValue(0);
		horizontalSlider.setPaintTrack(false);
		horizontalSlider.setEnabled(false);
		frmCircumcircle.getContentPane().add(horizontalSlider, BorderLayout.SOUTH);
		
		JSlider verticalSlider = new JSlider();
		verticalSlider.setValue(0);
		verticalSlider.setPaintTrack(false);
		verticalSlider.setEnabled(false);
		verticalSlider.setOrientation(SwingConstants.VERTICAL);
		verticalSlider.setInverted(true);
		frmCircumcircle.getContentPane().add(verticalSlider, BorderLayout.EAST);
		
		Canvas canvas = new Canvas();
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (drawFlg == 1) {		// Check draw enabled
					
					if (pointCount == 3) {
						pointCount = 0;
					}
					
					int mouseX = e.getX();
				    int mouseY = e.getY();
				    
				    // Specify points by mouse click
				    canvas.getGraphics().setColor(Color.black);
				    canvas.getGraphics().fillOval(mouseX - 3, mouseY - 3, 6, 6);
	
			    	xPoints[pointCount] = mouseX;
			    	yPoints[pointCount] = mouseY;
			    	
				    if (pointCount == 2) {		// Check specified all three points			
						drawCircumCircle(canvas, xPoints, yPoints);
	
						// Set x-slider options
						horizontalSlider.setMaximum(screenWidth);
						horizontalSlider.setEnabled(true);
						horizontalSlider.setValue((int) centerX);
				    	hValue = horizontalSlider.getValue();
						horizontalSlider.addChangeListener(new ChangeListener() {
						      public void stateChanged(ChangeEvent event) {
						    	  hValue = horizontalSlider.getValue();
						  		  canvas.getGraphics().clearRect(0, 0, screenWidth, screenHeight);
						    	  reDrawHorizontal(canvas, hValue);
						      }
						});
	
						// Set y-slider options
						verticalSlider.setMaximum(screenHeight);
						verticalSlider.setEnabled(true);
						verticalSlider.setValue((int) centerY);
				    	vValue = verticalSlider.getValue();
						verticalSlider.addChangeListener(new ChangeListener() {
						      public void stateChanged(ChangeEvent event) {
						    	  vValue = verticalSlider.getValue();
						    	  canvas.getGraphics().clearRect(0, 0, screenWidth, screenHeight);
						    	  reDrawVertical(canvas, vValue);
						      }
						});
				    }
	
					pointCount++;	

				}
			}
		});
		
		canvas.setBackground(Color.WHITE);
		frmCircumcircle.getContentPane().add(canvas, BorderLayout.CENTER);
	}
	
	// drawCircumCircle on Canvas based on three points x, y-coordinates
	private void drawCircumCircle(Canvas c, int[] x, int[] y) {		
    	double Det = getDeterminant(x[1] - x[0], y[1] - y[0], x[2] - x[1], y[2] - y[1]);
    	double Det1 = getDeterminant((x[1] * x[1] - x[0] * x[0] + y[1] * y[1] - y[0] * y[0])/2, y[1]-y[0], (x[2] * x[2] - x[1] * x[1] + y[2] * y[2] - y[1] * y[1])/2, y[2]-y[1]);
    	double Det2 = getDeterminant(x[1] - x[0], (x[1] * x[1] - x[0] * x[0] + y[1] * y[1] - y[0] * y[0])/2, x[2]-x[1], (x[2] * x[2] - x[1] * x[1] + y[2] * y[2] - y[1] * y[1])/2);
		
		centerX = Det1 / Det;
		centerY = Det2 / Det;
		radius = Math.sqrt((centerX - x[0]) * (centerX - x[0]) + (centerY - y[0]) * (centerY - y[0]));		

	    c.getGraphics().fillOval((int) (centerX - 3), (int) (centerY - 3), 6, 6);
	    c.getGraphics().drawOval((int) (centerX - radius), (int) (centerY - radius), (int) (2 * radius), (int) (2 * radius));
	}
	
	// refresh according to x-slider movement
	private void reDrawHorizontal(Canvas c, int x) {
		int diffX = (int) centerX - x;
		centerX = x;
		
		c.getGraphics().clearRect(0, 0, screenWidth, screenHeight);
		
		for (int i = 0; i < 3; i++) {
			xPoints[i] = xPoints[i] - diffX;
			c.getGraphics().fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
		}
		
		drawCircumCircle(c, xPoints, yPoints);		
	}
	
	// refresh according to y-slider movement
	private void reDrawVertical(Canvas c, int y) {
		int diffY = (int) centerY - y;
		centerY = y;		
		
		for (int i = 0; i < 3; i++) {
			yPoints[i] = yPoints[i] - diffY;
			c.getGraphics().fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
		}
		
		drawCircumCircle(c, xPoints, yPoints);		
	}
	
	// Get Determinant
	private double getDeterminant(int a11, int a12, int a21, int a22) {
		return (a11 * a22 - a12 * a21);
	}

}
