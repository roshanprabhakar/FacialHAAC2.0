import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Compression algorithm 1: K means clustering to segment image colors
 */
public class Compression1 extends ImageCompressor {

    private int n;
    private ArrayList<Cluster> clusters;

    public Compression1(BufferedImage image, int n) {
        super(image);
        this.n = n;
    }

    @Override
    public void compress() {
        initializeClusters();
        generateClusters();
        generateCompressed();
        if (Double.toString(success()).equals("NaN")) compress();
    }

    @Override
    public double success() {
        double averageUniformation = 0;
        for (Cluster cluster : clusters) {
            averageUniformation += cluster.getUniformation();
        }
        return averageUniformation / clusters.size();
    }

    public void generateCompressed() {
        compressed = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (Cluster cluster : clusters) {
            for (ColorPoint point : cluster.getPoints()) {
                compressed.setRGB(point.getC(), point.getR(), cluster.getCenter().getColor().getRGB());
            }
        }
    }

    public void generateClusters() {
        for (int rep = 0; rep < 100; rep++) {
            clearClusters();
            assignPointsToClusters();
            recalculateCenters();
        }
        finalizeClusters();
    }

    private void finalizeClusters() {
        for (Cluster cluster : clusters) {
            cluster.finalize();
        }
    }

    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private void recalculateCenters() {
        for (Cluster cluster : clusters) {
            cluster.recalculateCenter();
        }
    }

    private void assignPointsToClusters() {
        for (ColorPoint point : points) {
            double longestDistance = Double.MAX_VALUE;
            Cluster closest = null;
            for (Cluster cluster : clusters) {
                if (point.distance(cluster) < longestDistance) {
                    longestDistance = point.distance(cluster);
                    closest = cluster;
                }
            }
            closest.add(point);
        }
    }

    private void initializeClusters() {
        clusters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            clusters.add(new Cluster(ColorPoint.randomPoint()));
        }
    }
}
