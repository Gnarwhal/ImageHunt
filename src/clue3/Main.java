package clue3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("clue3.png");
			BufferedImage image = ImageIO.read(stream);
			stream.close();
			int width  = image.getWidth();
			int height = image.getHeight();
			int[] raw  = image.getRGB(0, 0, width, height, null, 0, width);
			int[] output = new int[width * height];

			int xmin = 0, xmax = width  - 1;
			int ymin = 0, ymax = height - 1;

			int x = 0;
			int y = 0;
			int dir = 3;
			for (int i = 0; i < width * height; ++i) {
				output[i] = raw[x + y * width];
				switch (dir) {
					case 0:
						if (x == xmax) {
							--xmax;
							++y;
							++dir;
						}
						else
							++x;
						break;
					case 1:
						if (y == ymax) {
							--ymax;
							--x;
							++dir;
						}
						else
							++y;
						break;
					case 2:
						if (x == xmin) {
							++xmin;
							--y;
							++dir;
						}
						else
							--x;
						break;
					case 3:
						if (y == ymin) {
							++ymin;
							++x;
							dir = 0;
						}
						else
							--y;
						break;
				}
			}

			image.setRGB(0, 0, width, height, output, 0, width);
			ImageIO.write(image, "png", new File("clue3_output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
