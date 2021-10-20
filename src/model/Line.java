package src.model;

public class Line {

    private Point point1,point2;
    private final int color;

    public Line(int x1, int y1, int x2, int y2, int color) {
        point1 = new Point(x1,y1);
        point2 = new Point(x2,y2);
        this.color = color;
    }

    public Line(int x1, int y1, int x2, int y2){
        this(x1,y1,x2,y2,0xFFFFFF);
    }

    public Line(Point p1, Point p2){
        this(p1,p2,0xFFFFFF);
    }

    public Line(Point p1, Point p2, int color) {
        point1 = p1;
        point2 = p2;
        this.color = color;
    }

    public Point getCenterPoint(){
        return new Point((point1.getX()+point2.getX())/2,(point1.getY()+point2.getY())/2);
    }

    public Point getPoint1(){
        return point1;
    }

    public Point getPoint2(){
        return point2;
    }

    public int getColor() {
        return color;
    }

    public int getX1() {
        return point1.getX();
    }

    public int getX2() {
        return point2.getX();
    }

    public int getY1() {
        return point1.getY();
    }

    public int getY2() {
        return point2.getY();
    }
}
