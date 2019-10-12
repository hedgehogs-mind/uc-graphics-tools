package com.hedgehogsmind.uc.graphicstools.tools.images;

import com.hedgehogsmind.uc.graphicstools.target.TargetPlatform;
import com.hedgehogsmind.uc.graphicstools.tools.Tool;
import com.hedgehogsmind.uc.graphicstools.tools.exceptions.ToolExecutionException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImageEncoder extends Tool {

    private final static String TOOL_KEY = "inc";

    private final static String TOOL_NAME = "ImageEncoder";

    private final static String TOOL_DESCRIPTION = "Encodes bw bitmap image of any size to byte array format.";

    private final static Map<String, String> TOOL_REQUIRED_ARGUMENTS;

    private final static Map<String, String> TOOL_OPTIONAL_ARGUMENTS;

    private final static Set<TargetPlatform> TOOL_SUPPORTED_PLATFORMS;

    static {
        TOOL_REQUIRED_ARGUMENTS = new HashMap<>();
        TOOL_REQUIRED_ARGUMENTS.put("I", "Input file: must be an image, most likely a PNG.");
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
        return null;
    }
}
