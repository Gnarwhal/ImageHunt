package clue7;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			RawReader reader = new RawReader("clue7.tri");
			if (!reader.assertByte(187))
				System.out.println("Incorrect identifier byte!");
			else {
				if (!(reader.assertByte('T') && reader.assertByte('R') && reader.assertByte('I')))
					System.out.println("Could not locate 'TRI' signature");
				else {
					if (!reader.assertByte(2))
						System.out.println("Incorrect format version!");
					else {
						int width = reader.readInt();
						int height = reader.readInt();
						int[] output = new int[width * height];

						// Unused color information byte
						reader.readByte();

						int[] patternTable = new int[255 * 8 / 4];
						for (int i = 0; i < patternTable.length; ++i)
							patternTable[i] = reader.readInt();

						for (int j = 0; j < height; j += 8) {
							for (int i = 0; i < width; i += 8) {
								int patternIndex = reader.readByte();
								int color = (0xFF              << 24)
										| (reader.readByte() << 16)
										| (reader.readByte() <<  8)
										| (reader.readByte() <<  0);
								if (patternIndex == 0)
									for (int x = 0; x < 8; ++x)
										for (int y = 0; y < 8; ++y)
											output[i + x + (j + y) * width] = color;
								else {
									int color2 = (0xFF              << 24)
											| (reader.readByte() << 16)
											| (reader.readByte() <<  8)
											| (reader.readByte() <<  0);
									for (int k = 0; k < 64; ++k) {
										int intIndex = k / 32;
										int bitIndex = k % 32;
										boolean colorSelection = ((patternTable[(patternIndex - 1) * 2  + intIndex] >> (32 - bitIndex)) & 1) == 1;

										int x = k % 8;
										int y = k / 8;
										output[i + x + (j + y) * width] = (!colorSelection ? color : color2);
									}
								}
							}
						}

						BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
						image.setRGB(0, 0, width, height, output, 0, width);
						ImageIO.write(image, "png", new File("clue7_output.png"));
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class RawReader {

		private FileInputStream stream;

		public RawReader(String path) throws IOException {
			stream = new FileInputStream(path);
		}

		public boolean assertByte(int b) throws IOException {
			return stream.read() == b;
		}

		public int readByte() throws IOException {
			return stream.read();
		}

		public int readInt() throws IOException {
			return (stream.read() << 32)
					| (stream.read() << 16)
					| (stream.read() <<  8)
					| (stream.read() <<  0);
		}

		public void close() throws IOException {
			stream.close();
		}
	}
}
