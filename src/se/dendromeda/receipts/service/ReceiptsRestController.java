package se.dendromeda.receipts.service;
 
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;

/**
 * @author Crunchify.com
 * * Description: This program converts unit from Centigrade to Fahrenheit.
 * Last updated: 12/28/2018
 */
 
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import net.sourceforge.tess4j.TesseractException;
import se.dendromeda.receipts.ReceiptController;
 
@Path("/receipts")
public class ReceiptsRestController {
	
	@Path("/hello")
	@GET
	@Produces("application/json")
	public String hello() {
 
		return "Hello!";
	}
	

	
	
	
	@POST
	@Path("/enterreceipt")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("*/*")
	public static String saveImage(@FormParam("image") InputStream stream) throws IOException, TesseractException, Base64DecodingException {
		if (stream != null) {
			byte[] base64bytes = IOUtils.toByteArray(stream);
			byte[] bytes = Base64.decode(base64bytes);
			System.out.println("Image string: " + new String(bytes));
			System.out.println("Entering receipt");
			InputStream in = new ByteArrayInputStream(bytes);
			BufferedImage buffImage = ImageIO.read(in);
			//ReceiptController.readReceiptImage(buffImage);
			in.close();
			return "Image saved";			
		} else {
			return "AAAAAAAAAAAH";
		}
	}
	
	

 
}