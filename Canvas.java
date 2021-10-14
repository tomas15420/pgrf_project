import model.Line;
import model.Point;
import model.Polygon;
import rasterize.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
	private Polygon polygon = new Polygon();
	private Line[] tempLine = new Line[2];

	public Canvas(int width, int height) {
		frame = new JFrame();


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
				redraw();
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.getX() >= 0 && e.getX() < panel.getWidth() && e.getY() >= 0 && e.getY() < panel.getHeight()){
					List<Point> points = polygon.getPoints();
					if(points.size() == 0) return;

					Point point = new Point(e.getX(),e.getY());

					int x1 = points.get(points.size()-1).getX();
					int y1 = points.get(points.size()-1).getY();

					tempLine[0] = new Line(x1,y1,point.getX(),point.getY(),0x00FFFF);

					x1 = points.get(0).getX();
					y1 = points.get(0).getY();

					tempLine[1] = new Line(x1,y1,point.getX(),point.getY(),0x00FFFF);

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
						redraw();
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