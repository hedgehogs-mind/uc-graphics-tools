package com.hedgehogsmind.uc.graphicstools.tools;

import com.hedgehogsmind.uc.graphicstools.target.TargetPlatform;
import com.hedgehogsmind.uc.graphicstools.tools.exceptions.ToolExecutionException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        System.out.println("Hey FontEncoder here!");
        System.out.println("");
        System.out.println(requiredArguments);
        System.out.println(optionalArguments);

        return "ready";
    }
}
