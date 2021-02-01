package se.dendromeda.receipts.util;
/** Filename : ImageOperations.java **/
/**
 * 
 */
//package mts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import se.dendromeda.receipts.model.ReceiptLine;

/**
 * @author Asee Shrestha
 *
 */

/** 
 * Thresholds the image at the given ThresholdValue 
 * @params requiredThresholdValue The threshold value by which the image is to be threshold
 */
public class ImageOperations {
	
	static String outputPathFolder = "C:\\Users\\46706\\Pictures\\thresholdImages\\";
	static String tesseractDataPath = "C:\\Users\\46706\\Documents\\Tess4J\\tessdata";
	
	static int ratioTarget = 8;
	
	public static String readFromImage(BufferedImage ipimage) throws IOException, TesseractException {
    	
	    BufferedImage opimage = Threshold(ipimage, 120);
	    
	    String outputPath = outputPathFolder += "threshold" + (int)(Math.random()*1000) + ".png";
	    File f = new File(outputPath);
	    
	    ImageIO 
	    .write(opimage, 
	    		"png", 
	    		f);
	    
	    Tesseract tesseract = new Tesseract(); 

  
	    tesseract.setDatapath(tesseractDataPath);
	    tesseract.setLanguage("swe");
	    
	    // the path of your tess data folder 
	    // inside the extracted file 
	    String text 
	    = tesseract.doOCR(new File(outputPath)); 
	    
	    f.delete();
	    
	    
	    return text;
	    

 
	    
	}
	
	static int lastRatio;
	
	public static BufferedImage Threshold(BufferedImage img,int thresholdvalue) {
		
		System.out.println("Threshold value: " + thresholdvalue);
		
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage finalThresholdImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB) ;
		
		int red = 0;
		int green = 0;
		int blue = 0;
		
		int nWhite = 0;
		int nBlack = 0;
		
		for (int x = 0; x < width; x++) {
//			System.out.println("Row: " + x);
			try {

				for (int y = 0; y < height; y++) {
					int color = img.getRGB(x, y);
					
					red = ImageOperations.getRed(color);
					green = ImageOperations.getGreen(color);
					blue = ImageOperations.getBlue(color);
				
//					System.out.println("Threshold : " + requiredThresholdValue);
						if((red+green+blue)/3 < (int) (thresholdvalue)) {
							finalThresholdImage.setRGB(x,y,ImageOperations.mixColor(0, 0,0));
							nBlack++;
						}
						else {
							finalThresholdImage.setRGB(x,y,ImageOperations.mixColor(255, 255,255));
							nWhite++;
						}
					
				}
			} catch (Exception e) {
				 e.getMessage();
			}
		}
		System.out.println("White: " + nWhite + " / Black: " + nBlack + " = " + nWhite/nBlack);
		int ratio = nWhite/nBlack;
		if (ratio == ratioTarget || (lastRatio + ratio)/2 == ratioTarget || 
				(Math.abs(lastRatio-ratio) > 2 && ratioTarget > Math.min(lastRatio, ratio) && ratioTarget < Math.max(lastRatio, ratio))) {
			return finalThresholdImage;
		} else if (nWhite/nBlack > ratioTarget) {
			lastRatio = ratio;
			return Threshold(img, thresholdvalue+1);
		} else {
			lastRatio = ratio;
			return Threshold(img, thresholdvalue-1);
		}
		
		
		
	}

	private static int mixColor(int red, int green, int blue) {
		return red<<16|green<<8|blue;
	}

	public static int getRed(int color) {
		return (color & 0x00ff0000)  >> 16;
	}
	
	public static int getGreen(int color) {
		return	(color & 0x0000ff00)  >> 8;
	}
	
	public static int getBlue(int color) {
		return (color & 0x000000ff)  >> 0;
		
	}

}