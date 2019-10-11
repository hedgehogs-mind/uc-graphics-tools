package com.hedgehogsmind.uc.graphicstools.tools.fonts;

import com.hedgehogsmind.uc.graphicstools.target.TargetPlatform;
import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;
import com.hedgehogsmind.uc.graphicstools.tools.Tool;
import com.hedgehogsmind.uc.graphicstools.tools.exceptions.ToolExecutionException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FontEncoder extends Tool {

    private final static String TOOL_KEY = "fenc";

    private final static String TOOL_NAME = "FontEncoder";

    private final static String TOOL_DESCRIPTION = "Encodes bw bitmap fonts with 256 characters (16x16) to byte array format.";

    private final static Map<String, String> TOOL_REQUIRED_ARGUMENTS;

    private final static Map<String, String> TOOL_OPTIONAL_ARGUMENTS;

    private final static Set<TargetPlatform> TOOL_SUPPORTED_PLATFORMS;

    static {
        TOOL_REQUIRED_ARGUMENTS = new HashMap<>();
        TOOL_REQUIRED_ARGUMENTS.put("I", "Input file: must be an image, most likely a PNG.");
        TOOL_REQUIRED_ARGUMENTS.put("F", "Format: either 'bc' or 'bcs'.");
        TOOL_REQUIRED_ARGUMENTS.put("P", "Platform: platform to export for. See usage page for supported tools.");
        TOOL_REQUIRED_ARGUMENTS.put("D", "Order/direction of pixels: hv (left to right then to bottom) or vh (top to bottom, left to right).");

        TOOL_OPTIONAL_ARGUMENTS = new HashMap<>();
        TOOL_OPTIONAL_ARGUMENTS.put("O", "Output file: file to export header content to. Default export location is 'IMAGE_NAME.h'.");

        TOOL_SUPPORTED_PLATFORMS = new HashSet<>();
        TOOL_SUPPORTED_PLATFORMS.add(TargetPlatform.AVR_GCC);
    }

    public FontEncoder() {
        super(TOOL_KEY, TOOL_NAME, TOOL_DESCRIPTION, TOOL_REQUIRED_ARGUMENTS, TOOL_OPTIONAL_ARGUMENTS, TOOL_SUPPORTED_PLATFORMS);
    }

    @Override
    public String execute(Map<String, String> requiredArguments, Map<String, String> optionalArguments) throws ToolExecutionException {
        final String inputFileName = requiredArguments.get("I");
        final File inputFile = new File(inputFileName);
        if ( !inputFile.exists() ) throw new ToolExecutionException("Input file '"+inputFileName+"' does not exist.");

        final String format = requiredArguments.get("F");
        if ( !format.equals("bc") && !format.equals("bcs") ) throw new ToolExecutionException("Format '"+format+"' unknown.");

        final String platformKey = requiredArguments.get("P");
        final Optional<TargetPlatform> potentialPlatform = TargetPlatform.getByKey(platformKey);
        if ( !potentialPlatform.isPresent() ) throw new ToolExecutionException("Target platform '"+platformKey+"' unknown.");
        final TargetPlatform platform = potentialPlatform.get();

        final String directionKey = requiredArguments.get("D");
        final Optional<PixelDirection> potentialDirection = PixelDirection.getByKey(directionKey);
        if ( !potentialDirection.isPresent() ) throw new ToolExecutionException("Direction '"+directionKey+"' unknown.");
        final PixelDirection direction = potentialDirection.get();

        String fontName = inputFile.getName();
        fontName = fontName.substring(0, fontName.lastIndexOf("."));
        try {
            final Font font = FontReader.getInstance().readFont(ImageIO.read(inputFile), fontName);

            if ( platform == TargetPlatform.AVR_GCC ) {
                final AvrCFontExporter fontExporter = new AvrCFontExporter();
                final String headerFont = fontExporter.exportFontAsUTF8(font, direction, format);

                final Optional<String> preciseOutputFileName = Optional.ofNullable(optionalArguments.get("O"));
                final File outputFile = new File(preciseOutputFileName.isPresent() ? preciseOutputFileName.get() : fontName+"_"+format+".h");
                if ( !outputFile.exists() ) outputFile.createNewFile();

                final FileWriter writer = new FileWriter(outputFile);
                writer.write(headerFont);
                writer.close();

                return "Saved font as '"+outputFile.getName()+"'.";
            } else {
                throw new RuntimeException("Unsupported platform '"+platform+"'.");
            }

        } catch ( IOException e ) {
            throw new ToolExecutionException("Could not read input file '"+inputFileName+"': "+e.getMessage(), e);
        }
    }
}
