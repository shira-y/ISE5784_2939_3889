package unittests.renderer;

import org.junit.jupiter.api.Test;
import renderer.ImageWriter;
import primitives.Color;

public class ImageWriterTest {

    @Test
    public void testWriteImage() {
        // Define the resolution and dimensions
        int nX = 800;
        int nY = 500;
        int numRows = 10;
        int numCols = 16;
        int rowHeight = nY / numRows;
        int colWidth = nX / numCols;
        
        // Create the ImageWriter
        ImageWriter imageWriter = new ImageWriter("grid_test", nX, nY);
        
        // Define colors
        Color backgroundColor = new Color(255, 255, 255); // White for background
        Color gridColor = new Color(0, 0, 0); // Black for grid
        
        // Loop through each pixel
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                // Check if the pixel is on the grid lines
                if (i % rowHeight == 0 || j % colWidth == 0) {
                    imageWriter.writePixel(j, i, gridColor);
                } else {
                    imageWriter.writePixel(j, i, backgroundColor);
                }
            }
        }
        
        // Write the image to a file
        imageWriter.writeToImage();
    }
}
