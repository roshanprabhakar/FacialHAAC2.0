import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ImageCompressor {

    ColorPoint[][][] CUBE = new ColorPoint[256][256][256];
    ArrayList<ColorPoint> points;
    BufferedImage image;
    BufferedImage compressed;
    Cluster uniqueColors;

    public ImageCompressor(BufferedImage image) {
        points = new ArrayList<>();
        this.image = image;
        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {
                Color pixel = new Color(image.getRGB(c, r));
                CUBE[pixel.getRed()][pixel.getGreen()][pixel.getBlue()] = new ColorPoint(pixel, r, c);
                points.add(new ColorPoint(pixel, r, c));
            }
        }
        findMaxN();
    }

    private void findMaxN() {
        uniqueColors = new Cluster(ColorPoint.randomPoint());
        for (ColorPoint p : points) {
            if (!uniqueColors.contains(p)) uniqueColors.add(p);
        }
    }

    public void finalize() {
        try {
            File input = new File("input.jpg");
            ImageIO.write(image, "jpg", input);

            File output = new File("output.jpg");
            ImageIO.write(compressed, "jpg", output);
        } catch (IOException e) {
            System.out.println("Could not write finalization files for file size analysis");
        }
    }

    public int getMaxN() {
        return uniqueColors.size();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public BufferedImage getCompressed() {
        return compressed;
    }

    public abstract void compress();
    public abstract double success(); //generate a measure of success
}
