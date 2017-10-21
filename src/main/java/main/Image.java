package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	public static void main(String args[]) throws IOException {
		File file = new File("in/2.png");
		BufferedImage image = ImageIO.read(file);
		// Getting pixel color by position x and y
		for (int j = 0; j < image.getHeight() ; j++) {
			for (int i = 0; i < image.getWidth() ; i++) {
				int clr = image.getRGB(i, j);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				if((red+green+blue)<30){
					System.out.print("*");
				} else {
					System.out.print(" ");
				}
//				System.out.print(red + "-" + green + "-" + blue + "|");
				
//				System.out.println("Red Color value = " + red);
//				System.out.println("Green Color value = " + green);
//				System.out.println("Blue Color value = " + blue);
			}
			System.out.println("");
		}
	}
}