package com.hedgehogsmind.uc.graphicstools.tools.fonts;

import java.util.List;
import java.util.Objects;

public class Font {

    private final String name;

    private final List<Character> characters;

    private final int width;

    private final int height;

    public Font(String name, List<Character> characters, int width, int height) {
        this.name = name;
        this.characters = characters;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Font font = (Font) o;
        return getWidth() == font.getWidth() &&
                getHeight() == font.getHeight() &&
                Objects.equals(getName(), font.getName()) &&
                Objects.equals(getCharacters(), font.getCharacters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCharacters(), getWidth(), getHeight());
    }

    @Override
    public String toString() {
        return "Font{" +
                "name='" + name + '\'' +
                ", characters=" + characters +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
