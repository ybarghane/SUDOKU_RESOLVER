package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class Image {

	private static List<List<String>> setImageInTable(String fileName) throws IOException {
		List<List<String>> output = new ArrayList<List<String>>();
		File file = new File(fileName);
		BufferedImage image = ImageIO.read(file);
		// Getting pixel color by position x and y
		for (int j = 0; j < image.getHeight(); j++) {
			output.add(new ArrayList<String>());
			for (int i = 0; i < image.getWidth(); i++) {
				int clr = image.getRGB(i, j);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				if ((red + green + blue) < 30) {
					output.get(j).add("*");
				} else {
					output.get(j).add(" ");
				}
			}
			System.out.println("");
		}
		return output;
	}
	
	private static void printImage(List<List<String>> imageTable){
		for (int i = 0; i < imageTable.size(); i++) {
			for (int j = 0; j < imageTable.get(i).size(); j++) {
				System.out.print(imageTable.get(i).get(j));
			}
			System.out.println("");
		}
	}
	
	public static List<Map<String, Integer>> tableXYofAsterix(List<List<String>> imageTable){
		List<Map<String, Integer>> tableOut = new ArrayList<Map<String,Integer>>();
		Map<String, Integer> position = null;
		for (int i = 0; i < imageTable.size(); i++) {
			for (int j = 0; j < imageTable.get(i).size(); j++) {
				position = new HashMap<String, Integer>();
				position.put("x", i);
				position.put("y", j);
				tableOut.add(position);
			}
		}
		return tableOut;
	}
	
	

	public static void main(String args[]) throws IOException {
		String fileName = "in/1.png";
//		String fileName = "in/sudoku.png";
		
		printImage(setImageInTable(fileName));
//		System.out.println(tableXYofAsterix(setImageInTable(fileName)));

	}
}