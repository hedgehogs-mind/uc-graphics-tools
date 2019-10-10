package com.hedgehogsmind.uc.graphicstools.target;

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
