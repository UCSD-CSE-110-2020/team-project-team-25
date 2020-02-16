package edu.ucsd.cse110.walkstatic.runs;

public class MileCalculator {
    private int height;
    public MileCalculator(String height){
        if(height.equals("")){
            height = "0";
        }
        if(height.equals("-1")) {
            height = "65";
        }
        this.height = Integer.valueOf(height);
    }

    public double getMiles(long steps){
        return (steps * (0.43 * (double)height/12))/5280;
    }
}
