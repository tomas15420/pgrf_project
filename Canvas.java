import model.Line;
import model.Point;
import model.Polygon;
import rasterize.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

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
	private Polygon polygon = new Polygon();
	private Line[] tempLine = new Line[2];
	private Point editPoint;

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
			List<Point> points = polygon.getPoints();
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight()){
					Point mousePoint = new Point(e.getX(),e.getY());
					switch (e.getButton()){
						case MouseEvent.BUTTON1: {
							polygon.addPoint(new Point(e.getX(),e.getY()));
							break;
						}
						case MouseEvent.BUTTON2:{
							if(points.size() == 0 || editPoint == null) return;
							polygon.setPoint(mousePoint,points.indexOf(editPoint));
							editPoint = null;
							break;
						}
						case MouseEvent.BUTTON3:{
							if(points.size() == 0) return;
							Line line = polygon.getNearestLineToPoint(mousePoint);
							Point addPoint;
							if(line != null){
								addPoint = line.getPoint2();
							}
							else{
								addPoint = points.get(0);
							}

							polygon.addPoint(mousePoint,points.indexOf(addPoint));
							break;
						}
					}
				}
				redraw();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight() && points.size() != 0) {

					Point mousePoint = new Point(e.getX(),e.getY());

					if (e.getButton() == MouseEvent.BUTTON2) {
						editPoint = polygon.getNearestPoint(mousePoint);
					}
				}
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight()){
					List<Point> points = polygon.getPoints();
					if(points.size() == 0) return;

					Point pointA = points.get(0);
					if(editPoint != null){
						pointA = editPoint;
					}

					Point pointB = points.get(points.size()-1);
					Point mousePoint = new Point(e.getX(),e.getY());

					if(e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK){
						Line nearestLine = polygon.getNearestLineToPoint(mousePoint);
						pointA = nearestLine.getPoint1();
						pointB = nearestLine.getPoint2();
					}

					tempLine[0] = new Line(pointA,mousePoint,0x00FFFF);
					if(e.getModifiersEx() != MouseEvent.BUTTON2_DOWN_MASK)
						tempLine[1] = new Line(pointB,mousePoint,0x00FFFF);

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
						polygon.reset();
						redraw();
						break;
					}
					case KeyEvent.VK_T:{
						List<Point> points = polygon.getPoints();
						if(points.size() == 2){
							Point a = points.get(0);
							Point b = points.get(1);

							int diameter = a.getDistanceToPoint(b);
							int radius = diameter/2;
							Point center = new Line(a,b).getCenterPoint();

//							Point mousePoint = new Point(MouseInfo.getPointerInfo().getLocation().x-panel.getLocationOnScreen().x,MouseInfo.getPointerInfo().getLocation().y-panel.getLocationOnScreen().y);

							if(Math.abs(b.getX()-a.getX()) < 25)
								polygon.addPoint(new Point(center.getX()+radius,center.getY()));
							else
								polygon.addPoint(new Point(center.getX(),center.getY()+radius));

							clear();
							lineRasterizer.rasterize(polygon);
							BufferedImage img = ((RasterBufferedImage)raster).getImg();
							img.getGraphics().drawOval(center.getX()-radius,center.getY()-radius,diameter,diameter);
							panel.repaint();
						}
						break;
					}
				}
			}
		});
	}

	private void redraw(){
		clear();
		lineRasterizer.rasterize(polygon);

		for(Line line : tempLine){
			if(line != null){
				dottedLineRasterizer.rasterize(line);
			}
		}
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