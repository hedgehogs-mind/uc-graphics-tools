package com.hedgehogsmind.uc.graphicstools.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FontReader {

    private static FontReader instance;

    public static FontReader getInstance() {
        if ( instance == null ) instance = new FontReader();
        return instance;
    }

    private FontReader() {

    }

    public Font readFont(final BufferedImage fontImage, final String name) {
        if (fontImage == null) throw new NullPointerException("fontImage must not be null.");

        final List<Character> characters = readCharacters(fontImage);
        int charWidth = fontImage.getWidth()/16;
        int charHeight = fontImage.getHeight()/16;

        return new Font(name, characters, charWidth, charHeight);
    }

    public List<Character> readCharacters(final BufferedImage fontImage) {
        if ( fontImage == null ) throw new NullPointerException("fontImage must not be null.");

        final List<Character> characters = new ArrayList<>();

        int charWidth = fontImage.getWidth()/16;
        int charHeight = fontImage.getHeight()/16;

        for ( int i = 0; i < 256; i++ ) {
            int startX = (i*charWidth) % (charWidth*16);
            int startY = (i/16)*charHeight;

            byte pixels[][] = new byte[charWidth][charHeight];
            boolean empty = true;

            for ( int x = 0; x < charWidth; x++ ) {
                for ( int y = 0; y < charHeight; y++ ) {
                    if ( (fontImage.getRGB(x+startX, y+startY) & 0x00FFFFFF) ==  0x00000000) {
                        empty = false;
                        pixels[x][y] = 1;
                    } else {
                        pixels[x][y] = 0;
                    }
                }
            }

            characters.add(new Character(i, charWidth, charHeight, pixels, empty));
        }

        return characters;
    }

}
