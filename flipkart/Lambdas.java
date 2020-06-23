
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.Color;

@FunctionalInterface // Annotating It as Functional Interface
interface PixelFunction {
    Color apply(int x, int y);
}

class ImageDemo {
    public static BufferedImage createImage(int width, int height, PixelFunction f) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                Color color = f.apply(x, y);
                image.setRGB(x, y, color.getRGB());
            }
        return image;
    } 
    
    public static void main(String[] args) throws IOException {
        
        BufferedImage frenchFlag = ImageDemo.createImage(150, 100,
            (x, y) -> x < 50 ? Color.BLUE : x < 100 ? Color.WHITE : Color.RED);
	
		// START: Generated Code By Compiler For Above Line --
			class GeneratedPixelClass implements PixelFunction {
			    Color apply(int x, int y) {
					return x < 50 ? Color.BLUE : x < 100 ? Color.WHITE : Color.RED    	
			    }
			}

			GeneratedPixelClass tempObject = new GeneratedPixelClass()
			BufferedImage frenchFlag = ImageDemo.createImage(150, 100, tempObject);
		// END: Generated Code By Compiler For Above Line --
	        
        Path path = Paths.get("flag.png");
        ImageIO.write(frenchFlag, "PNG", path.toFile());
        System.out.println("Image saved to " + path.toAbsolutePath());
    }
}

