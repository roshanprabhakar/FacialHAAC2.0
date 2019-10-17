import java.awt.image.BufferedImage;

//Gaussian blur
public class Compression2 extends ImageCompressor {
    public Compression2(BufferedImage image) {
        super(image);

    }

    @Override
    public void compress() {
        compressed = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    }

    @Override
    public double success() {
        return 0;
    }
}
