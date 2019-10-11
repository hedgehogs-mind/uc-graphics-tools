package com.hedgehogsmind.uc.graphicstools.tools.fonts;

import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;

public interface FontExporter {

    public String exportFontAsUTF8(Font font, final PixelDirection pixelDirection, final String format);

}
