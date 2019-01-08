package com.ibkglobal.api.bean.custom;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ASCIIArtService {

	public static void main(String[] args) throws IOException{
		int width = 150;
		int height = 10;
		
//		BufferedImage image1 = ImageIO.read(new File("C:\\work\\dev\\workspaces\\20181202\\ibkglobal-api\\src\\main\\resources\\logo\\ibk.png"));
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        graphics2d.drawString("IBK INTEGRATOR >>", 5, 10);
        graphics2d.drawString("KOLUMBUS", 5, 10);
        
		//save this image
        //ImageIO.write(image, "png", new File("imsi.png"));
        //☆ ★ ○ ● ◎ ◇ ◆ □ ■ △ ▲ ▽ ▼ ◁ ◀ ▷ ▶⊙ ◈ ▣ ◐ ◑        ♤ ♠ ♡ ♥ ♧ ♣ 

        for(int y=0;y<height;y++) {
        	StringBuffer sb = new StringBuffer();
        	for(int x=0;x<width;x++) {
        		sb.append(image.getRGB(x, y) == -16777216 ? " " : "~");
        	}
        	if(sb.toString().trim().isEmpty()) continue;
        	System.out.println(sb);
        }
       
	}
}
