package clue4;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("clue4.png");
			BufferedImage image = ImageIO.read(stream);
			stream.close();
			int width  = image.getWidth();
			int height = image.getHeight();
			int[] raw  = image.getRGB(0, 0, width, height, null, 0, width);
			int[] output = new int[width * height];

			rotateSections(315, 0xFF0000FF, width, height, raw, output);
			rotateSections(126, 0xFF00FF00, width, height, output, raw);
			rotateSections(63,  0xFFFF0000, width, height, raw, output);
			rotateSections(45,  0xFF00FFFF, width, height, output, raw);
			rotateSections(21,  0xFFFFFF00, width, height, raw, output);
			rotateSections(7,   0xFFFF00FF, width, height, output, raw);

			image.setRGB(0, 0, width, height, raw , 0, width);
			ImageIO.write(image, "png", new File("clue4_output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void rotateSections(int dim, int targetColor, int width, int height, int[] read, int[] write) {
		for (int i = 0; i < width; i += dim) {
			for (int j = 0; j < height; j += dim) {
				Rotate rotate = Main::rotate0;
				if (read[(i + dim - 1) + j * width] == targetColor)
					rotate = Main::rotate90;
				else if (read[i + (j + dim - 1) * width] == targetColor)
					rotate = Main::rotate270;
				else if (read[(i + dim - 1) + (j + dim - 1) * width] == targetColor)
					rotate = Main::rotate180;
				for (int k = 0; k < dim * dim; ++k) {
					int rotated = rotate.rotate(dim, k);
					write[(i + k % dim) + (j + k / dim) * width] = read[(i + rotated % dim) + (j + rotated / dim) * width];
				}
			}
		}
	}

	interface Rotate {
		int rotate(int width, int index);
	}

	private static int rotate0(int dim, int index) {
		return index;
	}

	private static int rotate90(int dim, int index) {
		int x = -index / dim;
		int y =  index % dim;
		return (x + dim - 1) + y * dim;
	}

	private static int rotate180(int dim, int index) {
		int x = -index % dim;
		int y = -index / dim;
		return (x + dim - 1) + (y + dim - 1) * dim;
	}

	private static int rotate270(int dim, int index) {
		int x =  index / dim;
		int y = -index % dim;
		return x + (y + dim - 1) * dim;
	}
}
