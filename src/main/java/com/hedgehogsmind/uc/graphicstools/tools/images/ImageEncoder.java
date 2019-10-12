package com.hedgehogsmind.uc.graphicstools.tools.images;

import com.hedgehogsmind.uc.graphicstools.target.TargetPlatform;
import com.hedgehogsmind.uc.graphicstools.tools.PixelDirection;
import com.hedgehogsmind.uc.graphicstools.tools.Tool;
import com.hedgehogsmind.uc.graphicstools.tools.exceptions.ToolExecutionException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ImageEncoder extends Tool {

    private final static String TOOL_KEY = "inc";

    private final static String TOOL_NAME = "ImageEncoder";

    private final static String TOOL_DESCRIPTION = "Encodes bw bitmap image of any size to byte array format.";

    private final static Map<String, String> TOOL_REQUIRED_ARGUMENTS;

    private final static Map<String, String> TOOL_OPTIONAL_ARGUMENTS;

    private final static Set<TargetPlatform> TOOL_SUPPORTED_PLATFORMS;

    static {
        TOOL_REQUIRED_ARGUMENTS = new HashMap<>();
        TOOL_REQUIRED_ARGUMENTS.put("I", "Input file: must be an image, most likely a PNG. Max size is 255x255.");
        TOOL_REQUIRED_ARGUMENTS.put("P", "Platform: platform to export for. See usage page for supported tools.");
        TOOL_REQUIRED_ARGUMENTS.put("D", "Order/direction of pixels: hv (left to right then to bottom) or vh (top to bottom, left to right).");

        TOOL_OPTIONAL_ARGUMENTS = new HashMap<>();
        TOOL_OPTIONAL_ARGUMENTS.put("O", "Output file/directory: file to export header content to. Default export location is 'IMAGE_NAME.h'. If value is only a directory, file will be saved to the directory with the default name.");

        TOOL_SUPPORTED_PLATFORMS = new HashSet<>();
        TOOL_SUPPORTED_PLATFORMS.add(TargetPlatform.AVR_GCC);
    }

    public ImageEncoder() {
        super(TOOL_KEY, TOOL_NAME, TOOL_DESCRIPTION, TOOL_REQUIRED_ARGUMENTS, TOOL_OPTIONAL_ARGUMENTS, TOOL_SUPPORTED_PLATFORMS);
    }

    @Override
    public String execute(Map<String, String> requiredArguments, Map<String, String> optionalArguments) throws ToolExecutionException {
        final String inputFileName = requiredArguments.get("I");
        final String platformKey = requiredArguments.get("P");
        final String directionKey = requiredArguments.get("D");

        final File inputFile = new File(inputFileName);
        if ( !inputFile.exists() ) throw new ToolExecutionException("Input file '"+inputFileName+"' does not exist.");

        final Optional<TargetPlatform> potentialTargetPlatform = TargetPlatform.getByKey(platformKey);
        if ( !potentialTargetPlatform.isPresent() ) throw new ToolExecutionException("Platform key '"+platformKey+"' unknown.");
        final TargetPlatform targetPlatform = potentialTargetPlatform.get();
        if ( !this.supportsPlatform(targetPlatform) ) throw new ToolExecutionException("ImageEncoder does not support target platform '"+potentialTargetPlatform.get()+"'");

        final Optional<PixelDirection> potentialDirection = PixelDirection.getByKey(directionKey);
        if ( !potentialDirection.isPresent() ) throw new ToolExecutionException("Unknown pixel direction key '"+directionKey+"'.");
        final PixelDirection pixelDirection = potentialDirection.get();

        try {
            final BufferedImage inputImage = ImageIO.read(inputFile);

            try {

                final String inputFileNameWithoutPath = inputFile.getName();
                final String inputFileNameWithoutPathAndSuffix = inputFileNameWithoutPath.substring(0, inputFileNameWithoutPath.lastIndexOf("."));

                String headerImgUTF8;
                if ( targetPlatform == TargetPlatform.AVR_GCC ) {
                    headerImgUTF8 = AvrCImageExporter.getInstance().exportImageAsUTF8(inputFileNameWithoutPathAndSuffix, inputImage, pixelDirection);
                } else {
                    throw new RuntimeException("Unknown target platform '"+targetPlatform+"'.");
                }

                final Optional<String> outputOverride = Optional.ofNullable(optionalArguments.get("O"));
                final String outputFileNameExtension = "_img_" + directionKey + ".h";
                String outputFileName = inputFileNameWithoutPathAndSuffix + outputFileNameExtension;
                if (outputOverride.isPresent()) {
                    String directoriesToCreateIfNecessary;
                    if (outputOverride.get().endsWith(File.separator)) {
                        directoriesToCreateIfNecessary = outputOverride.get();
                        outputFileName = directoriesToCreateIfNecessary + inputFileNameWithoutPathAndSuffix + outputFileNameExtension;
                    } else {
                        directoriesToCreateIfNecessary = outputOverride.get().substring(0, outputOverride.get().lastIndexOf(File.separator));
                        outputFileName = outputOverride.get();
                    }
                    if (!directoriesToCreateIfNecessary.isEmpty()) new File(directoriesToCreateIfNecessary).mkdirs();
                }

                final File outputFile = new File(outputFileName);

                final FileWriter writer = new FileWriter(outputFile);
                writer.write(headerImgUTF8);
                writer.close();

                return "Saved image in/as '" + outputFileName + "'.";
            } catch ( IOException e ) {
                throw new ToolExecutionException("Error while saving encoded image: "+e.getMessage(), e);
            }

        } catch ( IOException e ) {
            throw new ToolExecutionException("Error while reading input image: "+e.getMessage(), e);
        }
    }
}
