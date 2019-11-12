package clue0;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream stream = new FileInputStream("clue0.png");
			BufferedImage image = ImageIO.read(stream);
			int width  = image.getWidth();
			int height = image.getHeight();
			int[] raw  = image.getRGB(0, 0, width, height, null, 0, width);
			int[] output = new int[width * height];
			int[] shiftVert = new int[width];
			int[] shiftHorz = new int[height];

			for (int i = 0; i < height; ++i) {
				shiftHorz[i] = -1;
				while (raw[i * width + ++shiftHorz[i]] != 0xFF0000FF && raw[i * width + shiftHorz[i]] != 0xFFFF00FF);
			}

			for (int i = 0; i < width; ++i) {
				for (int j = 0; j < height; ++j) {
					output[j * width + i] = raw[j * width + rotate(i, shiftHorz[j], width)];
				}
			}

			int[] temp = raw;
			raw = output;
			output = temp;

			for (int i = 0; i < width; ++i) {
				shiftVert[i] = -1;
				while (raw[i + (++shiftVert[i] * width)] != 0xFFFF0000 && raw[i + (shiftVert[i] * width)] != 0xFFFF00FF);
			}

			for (int i = 0; i < width; ++i) {
				for (int j = 0; j < height; ++j) {
					output[j * width + i] = raw[rotate(j, shiftVert[i], height) * width + i];
				}
			}

			image.setRGB(0, 0, width, height, output, 0, width);
			ImageIO.write(image, "png", new File("clue0_output.png"));

			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int rotate(int index, int shift, int mod) {
		return (((index % mod + shift % mod) % mod) + mod) % mod;
	}
}
