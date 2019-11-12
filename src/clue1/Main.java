package clue1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("clue1.png");
			BufferedImage image = ImageIO.read(stream);
			int width  = image.getWidth();
			int height = image.getHeight();
			int[] raw  = image.getRGB(0, 0, width, height, null, 0, width);

			FileOutputStream output = new FileOutputStream("clue1_output.txt");
			byte yeet = 1;
			for (int i = 0; i < raw.length && yeet != 0; i += (8 * 34)) {
				int index = i / 8;
				yeet = 0;
				for (int j = 0; j < 8; ++j) {
					yeet <<= 1;
					yeet |= (raw[i + j * 34] & 1);
				}
				if (yeet != 0)
					output.write(yeet);
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
