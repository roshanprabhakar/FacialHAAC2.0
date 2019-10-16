import java.awt.*;

public class ColorPoint {

    private int red;
    private int green;
    private int blue;

    private int r;
    private int c;

    public ColorPoint(Color pixel, int r, int c) {
        red = pixel.getRed();
        green = pixel.getGreen();
        blue = pixel.getBlue();
        this.r = r;
        this.c = c;
    }

    public double distance(ColorPoint other) {
        double rsum = (red - other.getRed()) * (red - other.getRed());
        double gsum = (green - other.getGreen()) * (green - other.getGreen());
        double bsum = (blue - other.getBlue()) * (blue - other.getBlue());
        return Math.sqrt(rsum + gsum + bsum);
    }

    public double distance(Cluster cluster) {
        return distance(cluster.getCenter());
    }

    public static ColorPoint randomPoint() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return new ColorPoint(new Color(red, green, blue), 0, 0); //r and c placeholder, cluster centers never reassigned, therefore unimportant
    }

    public Color getColor() {
        return new Color(red, green, blue);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setC(int c) {
        this.c = c;
    }

    //by value
    public boolean equals(ColorPoint other) {
        return (red == other.getRed() && blue == other.getBlue() && green == other.getGreen());
    }

    public String toString() {
        return "{(" + red + ", " + green + ", " + blue + "), " + r + " , " + c + "}";
    }
}
