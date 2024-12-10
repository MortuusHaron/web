package check;

public class Checker {
    public boolean isHit(int x, float y, float r){
        return inRectangle(x,y,r) || inCircle(x,y,r) || inTriangle(x,y,r);
    }

    public boolean inRectangle(int x, float y, float r){
        return x <= 0 && x >= -r/2 && y >= 0 && y <= r;
    }

    public boolean inCircle(int x, float y, float r){
        return (x*x + y*y <= r*r) && x <= 0 && x >=- r && y <= 0 && y >= -r;
    }

    public boolean inTriangle(int x, float y, float r){
        return x>=0 && y>=0 && (y<=(-2*x)+r);
    }
}
