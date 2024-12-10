package validation;

import java.util.LinkedList;
import java.util.List;

public class Validate {
    private final List<Integer> xRange = new LinkedList<>();
    private final List<Float> rRange = new LinkedList<>();
    private String log = "all ok";
    public Validate(){
        xRange.add(-3);
        xRange.add(-2);
        xRange.add(-1);
        xRange.add(0);
        xRange.add(1);
        xRange.add(2);
        xRange.add(3);
        xRange.add(4);
        xRange.add(5);

        rRange.add(1f);
        rRange.add(1.5f);
        rRange.add(2f);
        rRange.add(2.5f);
        rRange.add(3f);
    }

    public boolean check(Integer x, Float y, Float r){
        return checkX(x) && checkY(y) && checkR(r);
    }

    public String getErr(){
        return log;
    }

    public boolean checkX(int x){
        if (xRange.contains(x)){
            return true;
        }
        log = "X must be selected";
        return false;
    }

    public boolean checkY(Float y){
        if (-3 <= y && y <= 5){
            return true;
        }
        log = "Y value must be -3<=y<=5";
        return false;
    }

    public boolean checkR(Float r){
        if (rRange.contains(r)){
            return true;
        }
        log = "R must be selected";
        return false;
    }
}
