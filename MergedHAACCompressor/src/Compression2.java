import java.awt.image.BufferedImage;

//Gaussian blur
public class Compression2 extends ImageCompressor {
    public Compression2(BufferedImage image) {
        super(image);

    }

    @Override
    public void compress() {
        ImageCompressor compressor = new Compression1(image, 3);
        compressor.compress();
        this.compressed = compressor.getCompressed();
    }

    @Override
    public double success() {
        return 0;
    }
}
