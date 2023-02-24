package lonliengine;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lonli.Utils;

public class Frames {
	
	/*
	limited version
	doesn't support frameX, frameY, frameWidth and frameHeight
	*/
	public static List<BufferedImage> sparrow(String animName, File xmlFile, File picFile) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.parse(xmlFile);
			doc.getDocumentElement().normalize();
			
			if (!doc.getDocumentElement().getNodeName().equals("TextureAtlas")) {
				throw new Exception("The file '" + xmlFile.getName() + "' is not a valid sparrow sprite sheet.");
			}
			
			
			NodeList list = doc.getElementsByTagName("SubTexture");
			
			if (list == null || list.getLength() <= 0) {
				throw new Exception("The file '" + xmlFile.getName() + "' has no frames.");
			}
			
			List<Element> elements = new ArrayList<>();
			String[] check = new String[] {"x", "y", "width", "height"};
			for (int i = 0; i < list.getLength(); i++) {
				Node node;
				if ((node = list.item(i)).getNodeType() == Node.ELEMENT_NODE) {
					Element subTexture = (Element) node;
					
					for (String c : check) {
						if (!subTexture.hasAttribute(c)) {
							throw new Exception("The animation '" + animName + "' in the file '" + xmlFile.getName() + "' is missing the attribute: " + c);
						}
					}
					
					String originalName = subTexture.getAttribute("name");
					String subTextureName = originalName.substring(0, originalName.length() - 4);
					
					if (subTextureName.equals(animName)) elements.add(subTexture);
				}
			}
			
			
			List<Element> arrangedElements = new ArrayList<>();
			for (int i = 0; i < elements.size(); i++) {
				String framePos = String.format("%04d", i);
				
				for (Element e : elements) {
					String name = e.getAttribute("name");
					
					if (!name.endsWith(framePos)) continue; // Skips this part of the loop
					
					arrangedElements.add(e);
				}
			}
			
			
			
			BufferedImage sheet = ImageIO.read(picFile);
			List<BufferedImage> frames = new ArrayList<>();
			
			for (Element e : arrangedElements) {
				int x = Integer.parseInt(e.getAttribute("x"));
				int y = Integer.parseInt(e.getAttribute("y"));
				int w = Integer.parseInt(e.getAttribute("width"));
				int h = Integer.parseInt(e.getAttribute("height"));
				
				frames.add(sheet.getSubimage(x, y, w, h));
			}
			
			return frames;
		} catch (Exception e) {
			e.printStackTrace();
			Utils.exit(-1);
		}
		
		return null;
	}
	
	public static List<BufferedImage> sparrow(String animName, String xmlFile, String picFile) {
		return sparrow(animName, new File(xmlFile), new File(picFile));
	}
	
}