package clue5;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("clue5.png");
			BufferedImage image = ImageIO.read(stream);
			stream.close();
			int width  = image.getWidth();
			int height = image.getHeight();
			int[] raw  = image.getRGB(0, 0, width, height, null, 0, width);
			int[] output = new int[707 * 707];

			int index = 0;
			for (int i = 0; i < 707 * 707; ++i) {
				for (int j = 0; j < 24; ++j) {
					output[i] <<= 1;
					output[i]  |= raw[index + j] & 1;
				}
				output[i] |= 0xFF000000;
				index += 24;
			}

			BufferedImage write = new BufferedImage(707, 707, BufferedImage.TYPE_INT_ARGB);
			write.setRGB(0, 0, 707, 707, output, 0, 707);
			ImageIO.write(write, "png", new File("clue5_output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
