package com.hedgehogsmind.uc.graphicstools.tools.fonts;

import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;

import java.util.Arrays;
import java.util.Objects;

public class Character {

    private static final int BIT_NOT_EMPTY = 0;

    private final int charCode;

    private final int width;

    private final int height;

    private final byte pixels[][];

    private final boolean empty;

    public Character(int charCode, int width, int height, byte[][] pixels, boolean empty) {
        this.charCode = charCode;
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.empty = empty;
    }

    public int getCharCode() {
        return charCode;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[][] getPixels() {
        return pixels;
    }

    public boolean isEmpty() {
        return empty;
    }

    public byte[] reformat(final PixelDirection direction,
                           final boolean addLeadingNotEmptyFlagByte,
                           final boolean removePixelsIfEmpty) {
        final byte[] bits = new byte[width*height+1];

        if ( direction == PixelDirection.LEFT_TO_RIGHT_TOP_TO_BOTTOM ) {
            int counter = 0;
            for ( int y = 0; y < height; y++ ) {
                for ( int x = 0; x < width; x++ ) {
                    bits[counter++] = pixels[x][y];
                }
            }
        } else if ( direction == PixelDirection.TOP_TO_BOTTOM_LEFT_TO_RIGHT ) {
            int counter = 0;
            for ( int x = 0; x < width; x++ ) {
                for ( int y = 0; y < height; y++ ) {
                    bits[counter++] = pixels[x][y];
                }
            }
        } else {
            throw new IllegalArgumentException("Direction '"+direction+"' not implemented yet.");
        }


        byte formatedPixels[] = new byte[ ( (this.empty && removePixelsIfEmpty) ? 0 : (int)Math.ceil(width*height/8.0)) + (addLeadingNotEmptyFlagByte ? 1 : 0)];
        if ( addLeadingNotEmptyFlagByte ) formatedPixels[0] = (byte)(0 | ((this.empty ? 0 : 1) << BIT_NOT_EMPTY));
        if ( !this.empty || !removePixelsIfEmpty) {
            for ( int i = 0; i < width*height; i++ ) {
                formatedPixels[i/8+(addLeadingNotEmptyFlagByte ? 1 : 0)] |= (bits[i] << (i%8));
            }
        }
        return formatedPixels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return getCharCode() == character.getCharCode() &&
                getWidth() == character.getWidth() &&
                getHeight() == character.getHeight() &&
                isEmpty() == character.isEmpty() &&
                Arrays.equals(getPixels(), character.getPixels());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getCharCode(), getWidth(), getHeight(), isEmpty());
        result = 31 * result + Arrays.hashCode(getPixels());
        return result;
    }

    @Override
    public String toString() {
        return "Character{" +
                "charCode=" + charCode +
                ", width=" + width +
                ", height=" + height +
                ", pixels=" + Arrays.toString(pixels) +
                ", empty=" + empty +
                '}';
    }
}
