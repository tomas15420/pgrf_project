package model;

import java.awt.*;
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

    public Point findNearestPoint(Point point){
        int distance = -1;
        Point nearestPoint = null;
        for(int i = 0; i < points.size(); i ++) {
            Point a = points.get(i);

            int currentDistance = a.getDistance(point);
            if(currentDistance < distance || distance == -1){
                distance = currentDistance;
                nearestPoint = a;
            }
        }
        return nearestPoint;
    }

    public void setPoint(Point point, int index){
        points.set(index, point);
    }
    public Point findNearestPoint(int x, int y){
        return findNearestPoint(new Point(x,y));
    }

    public void reset(){
        points = new ArrayList<>();
    }

    public List<Point> getPoints() {
        return points;
    }
}
