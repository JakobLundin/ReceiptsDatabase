package se.dendromeda.receipts;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.TesseractException;
import se.dendromeda.receipts.data.ProductDao;
import se.dendromeda.receipts.data.ReceiptDao;
import se.dendromeda.receipts.data.ReceiptLineDao;
import se.dendromeda.receipts.model.Product;
import se.dendromeda.receipts.model.Receipt;
import se.dendromeda.receipts.model.ReceiptLine;
import se.dendromeda.receipts.util.ImageOperations;
import se.dendromeda.receipts.util.StringHandler;

public class ReceiptController {
	
	static final String outputPathFolder = "C:\\Users\\46706\\Pictures\\Receipts\\";
	static final int INTERVAL = 30000;
	
	public static void main(String args[]) throws InterruptedException, IOException, TesseractException {
		
		String inputFolderPath = "C:\\Users\\46706\\Pictures\\ReceiptsInput\\";
		boolean loop = true;
		
		while(loop) {
			
			Stream<Path> walk = Files.walk(Paths.get(inputFolderPath));
			List<String> files = walk.filter(Files::isRegularFile)
					.map(x -> x.toString()).collect(Collectors.toList());
			
			for (String fileName : files) {
				if (fileName.contains("stopreceipts")) {
					loop = false;
				} else {
					System.out.println("Reading file: " + fileName);
					readReceiptImage(fileName);
				}
			}
			walk.close();
			//Thread.sleep(INTERVAL);
			loop = false;
		}
		System.out.println("Stopping...");
		
	}
	
	
	
	
	public static void readReceiptImage(String inputFilePath) throws IOException, TesseractException {
		ProductDao prodDao = new ProductDao();
		ReceiptDao recDao = new ReceiptDao();
		ReceiptLineDao lineDao = new ReceiptLineDao();
		Receipt receipt = new Receipt(0, LocalDate.now().toString());
		recDao.create(receipt);
		
		String outputPath = outputPathFolder + "receipt_" + receipt.getId() + "_" + receipt.getDate() + ".png";
		
		Files.move(Paths.get(inputFilePath), Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);
		
		File f = new File(outputPath);
		BufferedImage ipimage = ImageIO.read(f);
		
		String text = ImageOperations.readFromImage(ipimage);
		List<String> lines = StringHandler.parseLines(receipt, text);		
		
		List<Product> products = prodDao.getAll();
		for (String line : lines) {
			ReceiptLine receiptLine = new ReceiptLine(receipt, line);
			List<ProductDiff> diffs = new ArrayList<>();
			for (Product product : products) {
				ProductDiff diff = new ProductDiff();
				diff.product = product;
				diff.difference = StringHandler.calculateDifference(product.getName(), receiptLine.getText().replaceAll("[0-9]", "X"));
				diffs.add(diff);
			}
			diffs.sort((e1,e2) -> {
				return e1.difference - e2.difference;
			});
			if (diffs.size() > 0 && diffs.get(0).difference < 3) {
				receiptLine.setProduct(diffs.get(0).product);
			} else {
				Product prod = new Product(0, receiptLine.getText().replaceAll("[0-9]", "X"), null);
				prodDao.create(prod);
				receiptLine.setProduct(prod);
			}
			lineDao.create(receiptLine);
		}
	}
	
	static class ProductDiff{
		Product product;
		int difference;
	}
	
}
