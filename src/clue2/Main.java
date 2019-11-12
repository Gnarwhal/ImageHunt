package clue2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("clue2.png");
			BufferedImage image = ImageIO.read(stream);
			stream.close();
			int width  = image.getWidth();
			int height = image.getHeight();
			int[] raw  = image.getRGB(0, 0, width, height, null, 0, width);
			int[] output = new int[width * height];

			int x = 0;
			int y = 0;
			int dir = 1;
			for (int i = 0; i < width * height; ++i) {
				output[i] = raw[x + y * width];
				if (dir == -1 && y == height - 1) {
					++x;
					dir = -dir;
				}
				else if (dir == -1 && x == 0) {
					 ++y;
					dir = -dir;
				}
				else if (dir == 1 && x == width - 1) {
					++y;
					dir = -dir;
				}
				else if (dir == 1 && y == 0) {
					++x;
					dir = -dir;
				}
				else {
					x += dir;
					y -= dir;
				}
			}

			image.setRGB(0, 0, width, height, output, 0, width);
			ImageIO.write(image, "png", new File("clue2_output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
