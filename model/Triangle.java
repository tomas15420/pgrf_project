package model;

import java.util.ArrayList;
import java.util.List;

public class Triangle {
    private List<Point> points = new ArrayList<>();
    private int color;

    public Triangle(int color){
        this.color = color;
    }

    public void addPoint(Point point){
        if(points.size() < 3)
            points.add(point);
    }

    public Point calculateThirdPointOnCircle(Point reference){
        Point point = null;
        if(points.size() == 2){
            Point a = points.get(0);
            Point b = points.get(1);

            int diameter = a.getDistanceToPoint(b);
            int radius = diameter/2;
            Point center = new Line(a,b).getCenterPoint();
            Point vector = new Point(reference.getX() - center.getX(),reference.getY() - center.getY());
            double vectorDistance = Math.sqrt(Math.pow(vector.getX(), 2)+Math.pow(vector.getY(),2));
            if(vectorDistance*radius > 0) {
                Point normalVector = new Point(vector.getX() / vectorDistance * radius, vector.getY() / vectorDistance * radius);

                int x = center.getX() + normalVector.getX();
                int y = center.getY() + normalVector.getY();
                point = new Point(x, y);
            }
        }
        return point;
    }
}
