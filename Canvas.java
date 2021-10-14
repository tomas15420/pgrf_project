import model.Line;
import model.Point;
import model.Polygon;
import rasterize.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;
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

	public Canvas(int width, int height) {
		frame = new JFrame();
		Polygon polygon = new Polygon();

		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		raster = new RasterBufferedImage(width, height);
		lineRasterizer = new LineRasterizerGraphics(raster);
		dottedLineRasterizer = new DottedLineRasterizer(raster);


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
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight()){
					switch (e.getButton()){
						case MouseEvent.BUTTON1: {
							polygon.addPoint(new Point(e.getX(),e.getY()));
							break;
						}
					}
				}
				clear();
				lineRasterizer.rasterize(polygon);

				panel.repaint();
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight()){
					List<Point> points = polygon.getPoints();
					if(points.size() == 0) return;
					clear();

					Point point = new Point(e.getX(),e.getY());

					int x1 = points.get(points.size()-1).getX();
					int y1 = points.get(points.size()-1).getY();

					Line line = new Line(x1,y1,point.getX(),point.getY());

					dottedLineRasterizer.rasterize(new Line(x1,y1,point.getX(),point.getY(),0x00FFFF));
					x1 = points.get(0).getX();
					y1 = points.get(0).getY();

					dottedLineRasterizer.rasterize(new Line(x1,y1,point.getX(),point.getY(),0x00FFFF));
					lineRasterizer.rasterize(polygon);
					panel.repaint();
				}
			}
		});
		panel.requestFocus();
		panel.requestFocusInWindow();
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_C){
					polygon.reset();
					clear();
					panel.repaint();
				}
			}
		});
	}

	public void clear() {
		raster.clear();
	}

	public void present(Graphics graphics) {
		BufferedImage img = ((RasterBufferedImage)raster).getImg();
		graphics.drawImage(img, 0, 0, null);
	}

	public void draw() {
		clear();
		lineRasterizer.rasterize(10,10,50,10,0xFFFF00);
		lineRasterizer.rasterize(new Line(10,30,50,30,0xFF0000));

		Polygon pol = new Polygon();
		pol.addPoint(new Point(50,100));
		pol.addPoint(new Point(150,250));
		pol.addPoint(new Point(100,10));

		lineRasterizer.rasterize(pol);
	}

	/*
		DÚ:
		CanvasMouse

	 */

	public void start() {
		draw();
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Canvas(800, 600).start());
	}

}