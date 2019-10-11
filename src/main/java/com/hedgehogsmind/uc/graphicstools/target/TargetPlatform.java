package com.hedgehogsmind.uc.graphicstools.target;

import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;

import java.util.Optional;

public enum TargetPlatform {

    AVR_GCC("AVR-GCC", "avrgcc", "Fonts and images will be encoded as C-arrays including the PROGMEM option and exported as header files.");

    private final String platformName;

    private final String platformKey;

    private final String description;

    TargetPlatform(String platformName, String platformKey, String description) {
        this.platformName = platformName;
        this.platformKey = platformKey;
        this.description = description;
    }

    public static Optional<TargetPlatform> getByKey(final String key) {
        if ( key == null ) throw new NullPointerException("key must not be null.");
        if ( key.isEmpty() ) throw new IllegalArgumentException("key must not be empty.");

        for ( final TargetPlatform platform : TargetPlatform.values() ) {
            if ( platform.platformKey.toLowerCase().equals(key.toLowerCase()) ) {
                return Optional.of(platform);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return "TargetPlatform{" +
                "platformName='" + platformName + '\'' +
                ", platformKey='" + platformKey + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformKey() {
        return platformKey;
    }

    public String getDescription() {
        return description;
    }
}
