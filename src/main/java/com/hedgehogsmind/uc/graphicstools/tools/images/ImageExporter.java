package com.hedgehogsmind.uc.graphicstools.tools.images;

import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;

import java.awt.image.BufferedImage;

public interface ImageExporter {

    public String exportImageAsUTF8(final String imageName, final BufferedImage inputImage, final PixelDirection pixelDirection);

}
