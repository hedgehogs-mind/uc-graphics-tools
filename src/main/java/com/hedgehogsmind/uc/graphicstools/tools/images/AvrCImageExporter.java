package com.hedgehogsmind.uc.graphicstools.tools.images;

import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;
import com.hedgehogsmind.uc.graphicstools.tools.fonts.AvrCFontExporter;

import java.awt.image.BufferedImage;
import java.util.List;

public class AvrCImageExporter implements ImageExporter {

    public static int SETTINGS_BYTE_HV_BIT = 5;
    public static int SETTINGS_BYTE_VH_BIT = 4;

    private static AvrCImageExporter instance;

    public static AvrCImageExporter getInstance() {
        if ( instance == null ) instance = new AvrCImageExporter();
        return instance;
    }

    private AvrCImageExporter() {
    }

    @Override
    public String exportImageAsUTF8(final String imageName, final BufferedImage inputImage, final PixelDirection pixelDirection) {
        if ( imageName == null ) throw new NullPointerException("imageName must not be null.");
        if ( imageName.isEmpty() ) throw new IllegalArgumentException("imageName must not be empty.");
        if ( inputImage == null ) throw new NullPointerException("inputImage must not be null.");
        if ( pixelDirection == null ) throw new NullPointerException("pixelDirection must not be null.");

        if ( inputImage.getWidth() > 255 ) throw new IllegalArgumentException("Maximum width of input image is 255, but was "+inputImage.getWidth()+".");
        if ( inputImage.getHeight() > 255 ) throw new IllegalArgumentException("Maximum height of input image is 255, but was "+inputImage.getHeight()+".");

        final int w = inputImage.getWidth();
        final int h = inputImage.getHeight();
        final int wh = w*h;
        final int bytes_for_pixels = (wh/8) + ((wh%8) > 0 ? 1 : 0);
        final byte img_data[] = new byte[bytes_for_pixels+3];

        if ( pixelDirection == PixelDirection.LEFT_TO_RIGHT_TOP_TO_BOTTOM ) img_data[0] |= (1 << SETTINGS_BYTE_HV_BIT);
        if ( pixelDirection == PixelDirection.TOP_TO_BOTTOM_LEFT_TO_RIGHT ) img_data[0] |= (1 << SETTINGS_BYTE_VH_BIT);
        img_data[1] = (byte)inputImage.getWidth();
        img_data[2] = (byte)inputImage.getHeight();

        int byte_index = 3;
        if ( pixelDirection == PixelDirection.LEFT_TO_RIGHT_TOP_TO_BOTTOM ) {
            for ( int i = 0; i < inputImage.getWidth()*inputImage.getHeight(); i++ ) {
                if ( i%8 == 0 ) byte_index++;
                if ( (inputImage.getRGB(i%w, i/w) & 0x00111111) == 0x00000000 ) img_data[byte_index] |= (1 << (i%8));
            }
        } else if ( pixelDirection == PixelDirection.TOP_TO_BOTTOM_LEFT_TO_RIGHT ) {
            for ( int i = 0; i < inputImage.getWidth()*inputImage.getHeight(); i++ ) {
                if ( i%8 == 0 ) byte_index++;
                if ( (inputImage.getRGB(i/h, i%h) & 0x00111111) == 0x00000000 ) img_data[byte_index] |= (1 << (i%8));
            }
        } else {
            throw new RuntimeException("Unknown pixel direction '"+pixelDirection+"'.");
        }

        //Generate header file
        final StringBuilder sb = new StringBuilder();

        sb.append("/*"+System.lineSeparator()+" * µC-Graphics-Tools - AvrCImageExporter - ");
        sb.append(imageName);
        sb.append(System.lineSeparator()+" *"+System.lineSeparator());
        sb.append(" * uint8_t settings_index = 0;"+System.lineSeparator());
        sb.append(" * uint8_t width_index    = 1;"+System.lineSeparator());
        sb.append(" * uint8_t height_index   = 2;"+System.lineSeparator());
        sb.append(" *"+System.lineSeparator()+" */"+System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("#include <avr/pgmspace.h>");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("const uint8_t ");
        sb.append(imageName);
        sb.append("_img_"+pixelDirection.getKey()+"[");
        sb.append(img_data.length);
        sb.append("] PROGMEM = {");

        for ( int i = 0; i < img_data.length; i++ ) {
            if ( i > 0 ) sb.append(", ");
            sb.append("0x");
            sb.append(String.format("%02x", img_data[i]));
        }

        sb.append("};");
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
