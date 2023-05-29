package com.example.application.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.vaadin.flow.component.html.Image;

@Service
public class Utils {

	public Image byteToImage(byte[] image) {
	     String base64Image = Base64.getEncoder().encodeToString(image);
	     return new Image("data:image/png;base64," + base64Image, "Image");
	}
	
	public byte[] setDefaultImage() {
		byte[] defaultImageData = null;
		try (InputStream is = new FileInputStream("frontend/styles/images/default-profile-pic.png")) {
		defaultImageData = is.readAllBytes();
		} catch (IOException e) {
		System.out.println("setDefaultImage error"); // TODO: LOG ERROR TO LOG TABLE
		}
		return defaultImageData;
		}
	
	public String byteToDataUrl(byte[] bytes, String fileName) {
	    String encoded = Base64.getEncoder().encodeToString(bytes);
	    String mimeType;
	    if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
	        mimeType = "image/jpeg";
	    } else if (fileName.toLowerCase().endsWith(".png")) {
	        mimeType = "image/png";
	    } else {
	        throw new IllegalArgumentException("Unsupported file type: " + fileName);
	    }
	    return "data:" + mimeType + ";base64," + encoded;
	}
	
}
