package com.hedgehogsmind.uc.graphicstools.tools;

import java.util.Optional;

public enum PixelDirection {
    LEFT_TO_RIGHT_TOP_TO_BOTTOM("hv"),
    TOP_TO_BOTTOM_LEFT_TO_RIGHT("vh");

    private final String key;

    PixelDirection(String key) {
        this.key = key;
    }

    public static Optional<PixelDirection> getByKey(final String key) {
        if ( key == null ) throw new NullPointerException("key must not be null.");
        if ( key.isEmpty() ) throw new IllegalArgumentException("key must not be empty.");

        for ( final PixelDirection direction : PixelDirection.values() ) {
            if ( direction.key.toLowerCase().equals(key.toLowerCase()) ) {
                return Optional.of(direction);
            }
        }

        return Optional.empty();
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "PixelDirection{" +
                "key='" + key + '\'' +
                '}';
    }


}
