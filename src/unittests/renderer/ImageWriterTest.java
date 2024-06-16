package unittests.renderer;

import org.junit.jupiter.api.Test;
import renderer.ImageWriter;
import primitives.Color;

/**
 * Testing ImageWriter class.
 */

public class ImageWriterTest {
	/**
	 * Test method for writing an image with a grid pattern.
	 */
	@Test
	public void testWriteImage() {
		// Define the resolution and dimensions
		int nX = 801;
		int nY = 501;
		int numRows = 10;
		int numCols = 16;
		int rowHeight = nY / numRows;
		int colWidth = nX / numCols;

		// Create the ImageWriter
		ImageWriter imageWriter = new ImageWriter("grid_test", nX, nY);

		// Define colors
		Color backgroundColor = new Color(java.awt.Color.WHITE); // White for background
		Color gridColor = new Color(java.awt.Color.RED); // Black for grid

		// Loop through each pixel
		for (int i = 0; i < nY; i++) {
			for (int j = 0; j < nX; j++) {
				// Check if the pixel is on the grid lines
				imageWriter.writePixel(j, i, i % rowHeight == 0 || j % colWidth == 0 ? gridColor : backgroundColor);
			}
		}

		// Write the image to a file
		imageWriter.writeToImage();
	}
}
