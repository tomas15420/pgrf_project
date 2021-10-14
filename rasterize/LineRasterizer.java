package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

import java.awt.*;
import java.util.List;

public abstract class LineRasterizer {
    Raster raster;
    Color color;

    public LineRasterizer(Raster raster){
        this.raster = raster;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = new Color(color);
    }

    public void rasterize(Line line) {
        int x1 = line.getX1();
        int y1 = line.getY1();
        int x2 = line.getX2();
        int y2 = line.getY2();

        rasterize(x1,y1,x2,y2,line.getColor());
    }

    public void rasterize(Polygon pol){
        rasterize(pol,0xFF00FF);
    }

    public void rasterize(Polygon pol, int color){
        List<Point> points = pol.getPoints();
        for(int i = 0; i < points.size(); i ++){
            int x1 = points.get(i).getX();
            int y1 = points.get(i).getY();
            int x2 = points.get((i+1)%points.size()).getX();
            int y2 = points.get((i+1)%points.size()).getY();

            rasterize(x1,y1,x2,y2, color);
        }
    }

    public void rasterize(int x1, int y1, int x2, int y2, int color) {

    }

    protected void drawLine(int x1, int y1, int x2, int y2) {

    }
}
