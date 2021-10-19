import model.Line;
import model.Point;
import model.Polygon;
import rasterize.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * trida pro kresleni na platno: zobrazeni pixelu
 *
 * @author PGRF FIM UHK
 * @version 2020
 */

public class Canvas {

	private JFrame frame;
	private JPanel panel;
	private Raster raster;
	private LineRasterizer lineRasterizer;
	private DottedLineRasterizer dottedLineRasterizer;
	private Polygon currentPol = new Polygon();
	private List<Polygon> polygons = new ArrayList<>();
	private Line[] tempLine = new Line[2];
	private Point editPoint;
	private boolean drawMode = false; //False - Polygon | True = Trojúhelník

	public Canvas(int width, int height) {
		frame = new JFrame();

		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		raster = new RasterBufferedImage(width, height);
		lineRasterizer = new FilledLineRasterizer(raster);
		dottedLineRasterizer = new DottedLineRasterizer(raster,3);

		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				present(g);
			}
		};

		panel.setPreferredSize(new Dimension(width, height));

		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				List<Point> points = currentPol.getPoints();
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight()){
					Point mousePoint = new Point(e.getX(),e.getY());
					switch (e.getButton()){
						case MouseEvent.BUTTON1: {
							Point finalPoint = null;
							if(drawMode == true && points.size() == 2){
								Point a = points.get(0);
								Point b = points.get(1);

								int diameter = a.getDistanceToPoint(b);
								int radius = diameter/2;
								Point center = new Line(a,b).getCenterPoint();

								Point vector = new Point(mousePoint.getX() - center.getX(),mousePoint.getY() - center.getY());
								double vectorDistance = Math.sqrt(Math.pow(vector.getX(), 2)+Math.pow(vector.getY(),2));
								if(vectorDistance*radius != 0){
									Point normalVector = new Point(vector.getX()/vectorDistance*radius,vector.getY()/vectorDistance*radius);
									int x = center.getX() + normalVector.getX();
									int y = center.getY() + normalVector.getY();
									if(x >= 0 && y >= 0 && x < panel.getWidth() && y < panel.getHeight()){
										finalPoint = new Point(x,y);
										currentPol.addPoint(finalPoint);
										polygons.add(currentPol);
										currentPol = new Polygon();
									}

								}
							}
							else {
								finalPoint = new Point(e.getX(), e.getY());
								currentPol.addPoint(finalPoint);
							}
							break;
						}
						case MouseEvent.BUTTON2:{
							if(points.size() == 0 || editPoint == null || drawMode == true) return;
							currentPol.setPoint(mousePoint,points.indexOf(editPoint));
							editPoint = null;
							break;
						}
						case MouseEvent.BUTTON3:{
							if(points.size() == 0 || drawMode == true) return;
							Line line = currentPol.getNearestLineToPoint(mousePoint);
							Point addPoint;
							if(line != null){
								addPoint = line.getPoint2();
							}
							else{
								addPoint = points.get(0);
							}

							currentPol.addPoint(mousePoint,points.indexOf(addPoint));
							break;
						}
					}
				}
				redraw();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				List<Point> points = currentPol.getPoints();
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight() && points.size() != 0) {
					if(e.getButton() == MouseEvent.BUTTON2){
						Point mousePoint = new Point(e.getX(),e.getY());

						Point nearestPoint = currentPol.getNearestPoint(mousePoint);
						if(nearestPoint.getDistanceToPoint(mousePoint) < 5){
							currentPol.removePoint(nearestPoint);
							redraw();
						}
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				List<Point> points = currentPol.getPoints();

				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight() && points.size() != 0) {

					Point mousePoint = new Point(e.getX(),e.getY());

					if (e.getButton() == MouseEvent.BUTTON2) {
						editPoint = currentPol.getNearestPoint(mousePoint);
					}
				}
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight()){
					List<Point> points = currentPol.getPoints();
					if(points.size() == 0) return;
					Point pointA = points.get(0);
					Point pointB = points.get(points.size()-1);
					Point mousePoint = new Point(e.getX(),e.getY());

					if(drawMode == true && points.size() == 2){
						if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK){
							Point a = points.get(0);
							Point b = points.get(1);

							int diameter = a.getDistanceToPoint(b);
							int radius = diameter/2;
							Point center = pointA = new Line(a,b).getCenterPoint();
							Point vector = new Point(mousePoint.getX() - center.getX(),mousePoint.getY() - center.getY());
							double vectorDistance = Math.sqrt(Math.pow(vector.getX(), 2)+Math.pow(vector.getY(),2));
							if(vectorDistance*radius > 0) {
								Point normalVector = new Point(vector.getX() / vectorDistance * radius, vector.getY() / vectorDistance * radius);

								int x = center.getX() + normalVector.getX();
								int y = center.getY() + normalVector.getY();
								if(x >= 0 && y >= 0 && x < panel.getWidth() && y < panel.getHeight()){
									pointB = new Point(x,y);

									tempLine[0] = new Line(pointA, pointB);
								}
							}
						}

					}
					else {
						if(editPoint != null){
							pointA = editPoint;
						}
						if(e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK){
							Line nearestLine = currentPol.getNearestLineToPoint(mousePoint);
							pointA = nearestLine.getPoint1();
							pointB = nearestLine.getPoint2();
						}
						tempLine[0] = new Line(pointA,mousePoint,0x00FFFF);
						if(e.getModifiersEx() != MouseEvent.BUTTON2_DOWN_MASK)
							tempLine[1] = new Line(pointB,mousePoint,0x00FFFF);

					}


					redraw();

					tempLine = new Line[2];
				}
			}
		});
		panel.requestFocus();
		panel.requestFocusInWindow();
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()){
					case KeyEvent.VK_C:{
						currentPol.reset();
						for(Polygon pol : polygons){
							pol.reset();
						}
						polygons.clear();
						redraw();
						break;
					}
					case KeyEvent.VK_P:{
						if(currentPol.getPoints().size() > 2){
							polygons.add(currentPol);
							currentPol = new Polygon();
							redraw();
						}
						break;
					}
					case KeyEvent.VK_DELETE:{
						if(currentPol.getPoints().size() > 0){
							currentPol = new Polygon();
						}
						else{
							if(polygons.size() > 0){
								polygons.remove(polygons.size()-1);
							}
						}

						redraw();
					}
					case KeyEvent.VK_T:{
						List<Point> points = currentPol.getPoints();
						if(points.size() == 0 && drawMode == false) {
							drawMode = true;
						}
						else {
							drawMode = !drawMode;
						}
						break;
					}
				}
			}
		});
	}

	private void redraw(){
		clear();
		lineRasterizer.rasterize(currentPol);
		for(Polygon pol : polygons){
			lineRasterizer.rasterize(pol);
		}

		for(Line line : tempLine){
			if(line != null){
				dottedLineRasterizer.rasterize(line);
			}
		}

		BufferedImage img = ((RasterBufferedImage)raster).getImg();
		img.getGraphics().drawString("Pro kreslení trojúhelníku, stiskněte T",0, img.getHeight()-20);
		img.getGraphics().drawString("Kreslíte polygon č. "+polygons.size() + ", stiskněte P pro další polygon, DELETE pro odstranění předchozího polygonu, C pro vymazání plátna",0,img.getHeight()-5);
		panel.repaint();
	}

	public void clear() {
		raster.clear();
	}

	public void present(Graphics graphics) {
		BufferedImage img = ((RasterBufferedImage)raster).getImg();
		graphics.drawImage(img, 0, 0, null);
	}

	public void start() {
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Canvas(800, 600).start());
	}

}