package model;

public class Point {

    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
    }

    public int getDistance(Point point){
        return (int) Math.abs(Math.sqrt(Math.pow(point.getX()-getX(),2)+Math.pow(point.getY()-getY(),2)));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]",getX(),getY());
    }
}
