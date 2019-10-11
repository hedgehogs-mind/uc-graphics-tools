package com.hedgehogsmind.uc.graphicstools.tools.fonts;

import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;

import java.util.ArrayList;
import java.util.List;

public class AvrCFontExporter implements FontExporter {

    public static int SETTINGS_BYTE_BC_BIT = 7;
    public static int SETTINGS_BYTE_BCS_BIT = 6;

    @Override
    public String exportFontAsUTF8(Font font, final PixelDirection pixelDirection, final String format) {
        final StringBuilder sb = new StringBuilder();

        if ( !format.equals("bc") && !format.equals("bcs") ) throw new IllegalArgumentException("Format '"+format+"' invalid.");
        final boolean removePixelsIfEmpty = format.equals("bcs");

        final List<Byte> reformattedBytes = new ArrayList<>();
        for ( final Character c : font.getCharacters() ) {
            for ( final byte b : c.reformat(pixelDirection, true, removePixelsIfEmpty) ) {
                reformattedBytes.add(b);
            }
        }

        final byte fontData[] = new byte[reformattedBytes.size()+3];
        fontData[0] = 0;
        if ( format.equals("bc") ) fontData[0] |= (1 << SETTINGS_BYTE_BC_BIT);
        if ( format.equals("bcs") ) fontData[0] |= (1 << SETTINGS_BYTE_BCS_BIT);
        fontData[1] = (byte) font.getWidth();
        fontData[2] = (byte) font.getHeight();

        int bytes = fontData.length;

        for ( int i = 0; i < reformattedBytes.size(); i++ ) {
            fontData[3+i] = reformattedBytes.get(i);
        }

        sb.append("/*"+System.lineSeparator()+" * AvrCFontExporter - ");
        sb.append(font.getName());
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
        sb.append(font.getName());
        sb.append("_"+format+"[");
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

