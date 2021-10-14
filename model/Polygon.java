package model;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Polygon {

	private List<Point> points = new ArrayList<>();

    public void addPoint(Point point){
        points.add(point);
    }

    public void reset(){
        points = new ArrayList<>();
    }

    public List<Point> getPoints() {
        return points;
    }
}
