
import rasterize.LineRasterizer;
import rasterize.FilledLineRasterizer;
import rasterize.Raster;
import rasterize.RasterBufferedImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * trida pro kresleni na platno: zobrazeni pixelu, ovladani mysi
 * 
 * @author PGRF FIM UHK
 * @version 2020
 */
public class CanvasMouse {

	private JPanel panel;
	private Raster raster;
	private LineRasterizer lineRasterizer;

	private int x,y;

	public CanvasMouse(int width, int height) {
		JFrame frame = new JFrame();

		frame.setLayout(new BorderLayout());
		frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		raster = new RasterBufferedImage(width, height);
		lineRasterizer = new FilledLineRasterizer(raster);

		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				present(g);
			}
		};
		panel.setPreferredSize(new Dimension(width, height));

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					x = e.getX();
					y = e.getY();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1){
					lineRasterizer.rasterize(x,y,e.getX(),e.getY(),0x0077FF);
					panel.repaint();
				}
			}
		});

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				clear();
				lineRasterizer.rasterize(x,y,e.getX(),e.getY(),0x0077FF);
				panel.repaint();
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

	public void start() {
		clear();
		BufferedImage img = ((RasterBufferedImage)raster).getImg();
		img.getGraphics().drawString("Use mouse buttons", 5, img.getHeight() - 5);
		panel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new CanvasMouse(800, 600).start());
	}

}
