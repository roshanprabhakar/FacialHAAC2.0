import javax.imageio.ImageIO;
import javax.print.attribute.standard.Compression;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {


    public static ArrayList<int[]> FEATURES;
    public static ArrayList<int[]> FACES;

    public static final int GLOBAL_I = 5;
    public static final int[] ORIGIN = new int[]{0, 0};

    public static void main(String[] args) throws InterruptedException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(previousDirectory() + "haar_knn/" + "features.txt")));
            init(reader);

            System.out.println("Initializing original...");
            BufferedImage original = ImageIO.read(new File(inDirectory() + "original.jpg"));

            HashMap<BufferedImage, int[]> compressedFaces = new HashMap<>();
            int rank = 0;
            for (int[] face : FACES) {
                System.out.println("processing face " + rank + " out of " + FACES.size() + "...");
                //compressed face
                BufferedImage faceSection = original.getSubimage(face[0], face[1], face[2], face[3]);
                ImageCompressor compressor = new Compression1(faceSection, GLOBAL_I);
                compressor.compress();
                faceSection = compressor.getCompressed();

                //copy over features
                for (int i = 1; i <= 4; i++) {
                    System.out.println("Copying over feature " + i + "...");
                    int[] feature = FEATURES.get(rank * 4 + i - 1);
                    int[] featureLocalLoc = new int[]{feature[0], feature[1]};
                    feature = new int[] {feature[0] + face[0], feature[1] + face[1], feature[2], feature[3]}; //getting landmark loc on original
                    copySectionFixedLocs(original, faceSection, feature, featureLocalLoc);
                }
                rank++;
                compressedFaces.put(faceSection, new int[]{face[0], face[1]});
            }
            ImageCompressor secondLayer = new Compression2(original);
            secondLayer.compress();
            BufferedImage out = secondLayer.getCompressed();

            System.out.println("copying over features...");
            for (BufferedImage face : compressedFaces.keySet()) {
                copySectionFixedLocs(face, out, new int[]{0, 0, face.getWidth(), face.getHeight()}, compressedFaces.get(face));
            }

            writeImage(out, previousDirectory() + "Out/processed" +
                    new File(previousDirectory() + "Out/").listFiles().length);

//            copySectionFixedLocs(original, original, new int[]{0,0,40,40}, new int[]{500,400});
//            display(original, "original");

            System.exit(0);
//
//            System.out.println("FACES: ");
//            for (int[] face : FACES) {
//                System.out.println(Arrays.toString(face));
//            }
//            System.out.println("FEATURES");
//            for (int[] feature : FEATURES) {
//                System.out.println(Arrays.toString(feature));
//            }
//
//            for (int[] face : FACES) {
//                ImageCompressor compressor = new Compression1(original.getSubimage(face[0], face[1], face[2], face[3]), GLOBAL_I);
//                compressor.compress();
//                BufferedImage compressed = compressor.getCompressed();
//                //add in all lines
//            }
//
//
//            ImageCompressor compressor = new Compression1(original, 7);
//            compressor.compress();
//
//            //copy landmarks over
//            BufferedImage processed = compressor.getCompressed();
//
//            int featureI = 0;
//            int faceI = 0;
//            for (int[] feature : FEATURES) {
//                featureI++;
//                copySection(original, processed, feature, FACES.get(faceI));
//                if (featureI % 4 == 0) {
//                    faceI++;
//                }
//            }
//
//            //write image to disk
//            writeImage(processed, previousDirectory() + "Out/processed" +
//                    new File(previousDirectory() + "Out/").listFiles().length);

        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    // Utils
    public static void init(BufferedReader reader) throws IOException {

        FEATURES = new ArrayList<>();
        FACES = new ArrayList<>();

        String line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            count++;
            if (count % 5 == 0) {
                FACES.add(parseLine(line));
            } else {
                FEATURES.add(parseLine(line));
            }
        }
    }

    public static void display(BufferedImage image, String title) {
        JFrame frame = new JFrame(title);
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

    public static void display(BufferedImage image) {
        display(image, "image");
    }

    public static BufferedImage getImage(String filepath) {
        try {
            return ImageIO.read(new File(filepath));
        } catch (IOException e) {
            return null;
        }
    }

    public static void writeImage(BufferedImage image, String name) {
        File outputfile = new File(name + ".jpg");
        try {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            return;
        }
    }

    public static String previousDirectory() {
        File file = new File("");
        String[] pathChain = file.getAbsolutePath().split("/");

        StringBuilder path = new StringBuilder();
        for (int i = 0; i < pathChain.length - 1; i++) {
            path.append(pathChain[i]);
            path.append("/");
        }
        return path.toString().trim();
    }

    public static String inDirectory() {
        return previousDirectory() + "in/";
    }

    public static String outDirectory() {
        return previousDirectory() + "out/";
    }

    public static int[] parseLine(String line) {

        int[] features = new int[4];

        int i = 0;
        for (String indice : line.replaceAll("\\[", "").
                replaceAll("]", "").split(",")) {
            features[i] = Integer.parseInt(indice.trim());
            i++;
        }

        return features;
    }

    public static void copySection(BufferedImage from, BufferedImage to, int[] feature, int[] face) {
        for (int r = feature[1] + face[1]; r < face[1] + feature[1] + feature[3]; r++) {
            for (int c = feature[0] + face[0]; c < face[0] + feature[0] + feature[2]; c++) {
                to.setRGB(c, r, from.getRGB(c, r));
            }
        }
    }

    public static void copySectionFixedLocs(BufferedImage from, BufferedImage to, int[] fromFeature, int[] toFeature) { //toFeature is 2D
        BufferedImage section = from.getSubimage(fromFeature[0], fromFeature[1], fromFeature[2], fromFeature[3]);
        for (int r = toFeature[1]; r < toFeature[1] + section.getHeight(); r++) {
            for (int c = toFeature[0]; c < toFeature[0] + section.getWidth(); c++) {
                to.setRGB(c, r, section.getRGB(c - toFeature[0], r - toFeature[1]));
            }
        }
    }
}
