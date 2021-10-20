package src.model;

import java.util.*;
import java.util.List;

public class Polygon {

	private List<Point> points = new ArrayList<>();

    public void addPoint(Point point){
        points.add(point);
    }
    public void addPoint(Point point, int index){
        points.add(index,point);
    }

    public Point getNearestPoint(Point point){
        int distance = -1;
        Point nearestPoint = null;
        for (Point a : points) {
            int currentDistance = a.getDistanceToPoint(point);
            if (currentDistance < distance || distance == -1) {
                distance = currentDistance;
                nearestPoint = a;
            }
        }
        return nearestPoint;
    }

    public Line getNearestLineToPoint(Point point){
        int distance = -1;
        Line nearestLine = null;
        for(int i = 0; i < points.size(); i ++){
            Point a = points.get(i);
            Point b = points.get((i+1)%points.size());
            Line line = new Line(a,b);
            int currentDistance = line.getCenterPoint().getDistanceToPoint(point);
            if(currentDistance < distance || distance == -1) {
                distance = currentDistance;
                nearestLine = line;
            }
        }
        return nearestLine;
    }

    public void setPoint(Point point, int index){
        points.set(index, point);
    }
    public Point getNearestPoint(int x, int y){
        return getNearestPoint(new Point(x,y));
    }

    public void reset(){
        points.clear();
    }

    public void removePoint(Point point){
        points.remove(point);
    }

    public List<Point> getPoints() {
        return points;
    }
}
