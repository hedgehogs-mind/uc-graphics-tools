package com.hedgehogsmind.uc.graphicstools.graphics;

public class AvrCFontExporter implements FontExporter {

    @Override
    public String exportFontAsUTF8(Font font) {
        final StringBuilder sb = new StringBuilder();

        int bytes_per_char = (int)Math.ceil(font.getWidth()*font.getHeight()/8.0 + 1);
        int bytes = 2 + (bytes_per_char * 256);
        final byte fontData[] = new byte[bytes];
        fontData[0] = (byte) font.getWidth();
        fontData[1] = (byte) font.getHeight();

        int index = 2;
        for ( int i = 0; i < 256; i++ ) {
            byte charData[] = font.getCharacters().get(i).reformat(PixelDirection.TOP_TO_BOTTOM_LEFT_TO_RIGHT, true, false);
            for ( byte b : charData ) {
                fontData[index++] = b;
            }
        }


        sb.append("/*"+System.lineSeparator()+" * AvrCFontExporter - ");
        sb.append(font.getName());
        sb.append(System.lineSeparator()+" *"+System.lineSeparator());
        sb.append(" * uint8_t width_index    = 0;"+System.lineSeparator());
        sb.append(" * uint8_t height_index   = 1;"+System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(" * uint8_t bytes_per_char = ((width * height) / 8) + (((width * height) % 8) > 0 ? 1 : 0);"+System.lineSeparator());
        sb.append(" * uint8_t char_index     = 2 + (char_code * bytes_per_char);"+System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(" * Calculated for this font:"+System.lineSeparator());
        sb.append(" * uint8_t char_index     = 2 + (char_code * ");
        sb.append(bytes_per_char);
        sb.append(");"+System.lineSeparator());
        sb.append(" *"+System.lineSeparator()+" */"+System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("#include <avr/pgmspace.h>");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("const uint8_t ");
        sb.append(font.getName());
        sb.append("_bc[");
        sb.append(bytes);
        sb.append("] PROGMEM = {");

        for ( int i = 0; i < fontData.length; i++ ) {
            if ( i > 0 ) sb.append(", ");
            sb.append("0x");
            sb.append(String.format("%02x", fontData[i]));
        }

        sb.append("};");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

}

