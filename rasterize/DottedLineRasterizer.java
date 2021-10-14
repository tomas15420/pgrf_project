package rasterize;

import model.Line;

import java.awt.*;

public class DottedLineRasterizer extends LineRasterizer {


    public DottedLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        Graphics g = ((RasterBufferedImage)raster).getImg().getGraphics();
        g.setColor(this.color);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        float k = (float)(y2-y1)/(x2-x1);
        float q = y1-k*x1;

        if(Math.abs(y2-y1) < Math.abs(x1-x2)) {
            if (x2 < x1) {
                int pam = x1;
                x1 = x2;
                x2 = pam;
                pam = y1;
                y1 = y2;
                y2 = pam;
            }
            for (int x = x1; x <= x2; x += 2) {
                int y = (int) (k * x + q);
                raster.setPixel(x, y, color);
            }
        }else{
            if(y2 < y1){
                int pam = x1;
                x1 = x2;
                x2 = pam;
                pam = y1;
                y1 = y2;
                y2 = pam;
            }
            for(int y = y1; y <= y2; y +=2) {
                int x = (int)((y-q)/k);
                raster.setPixel(x,y,color);
            }
        }
    }
}
