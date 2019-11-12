package clue6;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("clue6_data.png");
			BufferedImage image = ImageIO.read(stream);
			stream.close();
			int width  = image.getWidth();
			int height = image.getHeight();
			int[] raw  = image.getRGB(0, 0, width, height, null, 0, width);
			int[] output = new int[width * height];

			for (int i = 0; i < width * height; ++i)
				output[i] = 0xFF000000;

			for (int i = 0; i < width * height; ++i) {
				if (raw[i] != 0xFFFFFFFF) {
					int other = findMatch(i, raw);
					raw[i]     = 0xFFFFFFFF;
					raw[other] = 0xFFFFFFFF;
					if (i == 7559)
						System.out.println(other % width + ", " + other / width);
					connect(
						i % width, i / width,
						other % width, other / width,
						width, output
					);
				}
			}

			image.setRGB(0, 0, width, height, output, 0, width);
			ImageIO.write(image, "png", new File("clue6_output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int findMatch(int index, int[] data) {
		int i = index;
		while (++i < data.length && data[index] != data[i]);
		return i;
	}

	private static void connect(int x0, int y0, int x1, int y1, int width, int[] write) {
		boolean flip = (y0 > y1) != (x0 > x1);
		int temp = x0;
		x0 = Math.min(temp, x1);
		x1 = Math.max(temp, x1);
		temp = y0;
		y0 = Math.min(temp, y1);
		y1 = Math.max(temp, y1);
		if (x0 == x1) {
			for (int i = y0; i <= y1; ++i)
				emit(x0, i, width, write);
		}
		else if (y0 == y1) {
			for (int i = x0; i <= x1; ++i)
				emit(i, y0, width, write);
		}
		else {
			float xDiff = x1 - x0 + 1;
			float yDiff = y1 - y0 + 1;
			for (int i = 0; i < xDiff - 1; ++i) {
				float begin = i / xDiff;
				float end = (i + 1) / xDiff;
				float avg = (begin + end) / 2;
				for (int j = (int) (begin * yDiff); j <= end * yDiff; ++j) {
					if (flip) {
						if (j < avg * yDiff)
							emit(x0 + i, y1 - j, width, write);
						else
							emit(x0 + i + 1, y1 - j, width, write);
					}
					else {
						if (j < avg * yDiff)
							emit(x0 + i, y0 + j, width, write);
						else
							emit(x0 + i + 1, y0 + j, width, write);
					}
				}
			}
		}
	}

	private static void emit(int x, int y, int width, int[] write) {
		write[x + y * width] = 0xFFFF2277;
	}
}
