package rasterize;

import java.awt.*;

public class FilledLineRasterizer extends LineRasterizer {


    public FilledLineRasterizer(Raster raster) {
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

        float k = (float)(y2-y1)/(float)(x2-x1);
        float q = y1-k*x1;

        if(Math.abs(y2-y1) < Math.abs(x2-x1)){
            if(x2 < x1) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            for(int x = x1; x <= x2; x ++) {
                int y = (int)(k*x+q);
                raster.setPixel(x,y,color);
            }
        } else{
            if(y2 < y1){
                int tmp = y1;
                y1 = y2;
                y2 = tmp;
            }

            for(int y = y1; y <= y2; y ++) {
                int x = (int)((y-q)/k);
                if(x2-x1 == 0)
                    x = x1;
                raster.setPixel(x,y,color);
            }
        }
    }
}
