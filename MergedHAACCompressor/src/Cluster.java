import java.util.ArrayList;

public class Cluster {

    private ArrayList<ColorPoint> points;
    private ColorPoint center;

    private double uniformation;

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

    //returns average deviation from mean distance from mean
    public void finalize() {
        uniformation = 0;
        for (ColorPoint p : points) {
            uniformation += p.distance(this);
        }
        uniformation /= points.size();

        double meanDev = uniformation;
        uniformation = 0;
        for (ColorPoint p : points) {
            uniformation += (Math.abs(p.distance(this) - meanDev));
        }
        uniformation /= points.size();
    }

    public double getUniformation() {
        return uniformation;
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
}
