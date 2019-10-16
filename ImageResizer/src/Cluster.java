import java.util.ArrayList;

public class Cluster {

    private ArrayList<ColorPoint> points;
    private ColorPoint center;

//    report information
    private double avgDistToCenter;
    private double avgDistToDistToCenter;

    public Cluster(ColorPoint center) {
        this.center = center;
        points = new ArrayList<>();
    }

    public void clear() {
        points.clear();
    }

    public void add(ColorPoint point) {
        points.add(point);
    }

    public double distance(Cluster other) {
        return center.distance(other.getCenter());
    }

    public double distance(ColorPoint point) {
        return center.distance(point);
    }

    public void recalculateCenter() {
        int sumR = 0, sumG = 0, sumB = 0;
        for (ColorPoint point : points) {
            sumR += point.getRed();
            sumG += point.getGreen();
            sumB += point.getBlue();
        }
        try {
            center.setRed(sumR / points.size());
            center.setGreen(sumG / points.size());
            center.setBlue(sumB / points.size());
        } catch (ArithmeticException e) {
//            System.out.println("Cluster is empty");
        }
    }

    //returns average distance from average distance from center
    public double uniformation() {
        double averageDistanceFromCenter = 0;
        for (ColorPoint p : points) {
            averageDistanceFromCenter += p.distance(this);
        }
        averageDistanceFromCenter /= points.size();
        avgDistToCenter = averageDistanceFromCenter;

        double avgDevFromAvgDist = 0;
        for (ColorPoint p : points) {
            avgDevFromAvgDist += Math.abs(p.distance(this) - averageDistanceFromCenter);
        }
        avgDevFromAvgDist /= points.size();
        avgDistToDistToCenter = avgDevFromAvgDist;

        return avgDevFromAvgDist;
    }

    //passed by value
    public boolean contains(ColorPoint point) {
        for (ColorPoint p : points) {
            if (p.equals(point)) return true;
        }
        return false;
    }

    public ColorPoint getCenter() {
        return this.center;
    }

    public int size() {return points.size();}

    public ArrayList<ColorPoint> getPoints() {return points;}

    public String toString() {
        return "(center: " + center + ") points: " + points;
    }

    public double getAvgDistToCenter() {
        return avgDistToCenter;
    }

    public double getAvgDistToDistToCenter() {
        return avgDistToDistToCenter;
    }
}
