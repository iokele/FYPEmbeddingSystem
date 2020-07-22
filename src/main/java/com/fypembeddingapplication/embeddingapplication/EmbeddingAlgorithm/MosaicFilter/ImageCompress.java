package com.fypembeddingapplication.embeddingapplication.EmbeddingAlgorithm.MosaicFilter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageCompress {

    public ImageCompress() {

    }


    public String compress(String originalBase64){
        int tThumbWidth = 0;
        int tThumbHeight =0;
        byte[] decodedBytes= Base64.getDecoder().decode(originalBase64);
        Image image=null;
        try{
            ByteArrayInputStream inputStream= new ByteArrayInputStream(decodedBytes);
            image = ImageIO.read(inputStream);
            inputStream.close();
        }
        catch (IOException e){
//            throw new IllegalStateException(e.getMessage());
            e.printStackTrace();
        }
        if(((BufferedImage) image).getWidth()>1000||((BufferedImage) image).getHeight()>1500){
            tThumbHeight = ((BufferedImage) image).getHeight() /2;
            tThumbWidth = ((BufferedImage) image).getWidth()/2;
        }
        else{
            tThumbHeight=((BufferedImage) image).getHeight();
            tThumbWidth= ((BufferedImage) image).getWidth();
        }
        BufferedImage tThumbImage = new BufferedImage( tThumbWidth, tThumbHeight, BufferedImage.TYPE_INT_RGB );
        Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
        tGraphics2D.setBackground( Color.WHITE );
        tGraphics2D.setPaint( Color.WHITE );
        tGraphics2D.fillRect( 0, 0, tThumbWidth, tThumbHeight );
        tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        tGraphics2D.drawImage( image, 0, 0, tThumbWidth, tThumbHeight, null ); //draw the image scaled
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String imageString = null;
        try {
            ImageIO.write(tThumbImage, "JPG", bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = new String (Base64.getEncoder().encode(imageBytes) ,"UTF-8") ;
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ImageIO.write( tThumbImage, "JPG", tThumbnailTarget ); //write the image to a file
        return imageString;
    }
}
