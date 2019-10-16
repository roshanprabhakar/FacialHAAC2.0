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
        initializeClusters();
    }

    @Override
    public void compress() {
        generateClusters();
        generateCompressed();
    }

    @Override
    public void generateReport() {
        double averageUniforization = 0;
        int numValidClusters = 0;
        for (Cluster cluster : clusters) {
            cluster.uniformation();
            String distToCenter = ((Double) cluster.getAvgDistToCenter()).toString(); //placeholder var that could be NaN
            if (!distToCenter.equals("NaN")) {
                System.out.println("---------- Cluster ----------");
                System.out.println("Uniformation: " + cluster.getAvgDistToDistToCenter());
                System.out.println("Average Distance to center: " + cluster.getAvgDistToCenter());
                System.out.println();
                averageUniforization += cluster.getAvgDistToDistToCenter();
                numValidClusters++;
            }
        }
        averageUniforization /= clusters.size();
        System.out.println("average uniformation value: " + averageUniforization + " for " + numValidClusters + "/" + n + "/" /*+ getMaxN()*/ + " clusters.");
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
            System.out.println(rep + "%");
            clearClusters();
            assignPointsToClusters();
            recalculateCenters();
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
