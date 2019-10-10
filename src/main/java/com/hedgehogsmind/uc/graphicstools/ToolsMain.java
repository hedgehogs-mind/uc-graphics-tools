package com.hedgehogsmind.uc.graphicstools;

import com.hedgehogsmind.uc.graphicstools.exceptions.ArgumentParsingException;
import com.hedgehogsmind.uc.graphicstools.exceptions.ArgumentsMissingException;
import com.hedgehogsmind.uc.graphicstools.target.TargetPlatform;
import com.hedgehogsmind.uc.graphicstools.tools.FontEncoder;
import com.hedgehogsmind.uc.graphicstools.tools.Tool;
import com.hedgehogsmind.uc.graphicstools.tools.exceptions.ToolExecutionException;

import java.util.*;

public class ToolsMain {

    public final static String appHeader = "\n\nhedgehogs-mind.com - µC-Graphics-Tools\n" +
            "--------------------------------------\n";

    public final static String horizontalLine = "--------------------------------------";

    public final static String usage =
                        "Usage of the graphics tools is:\n\n\n" +
                        "app.jar -T[tool] [....]\n\n" +
                        "\t- [tool]: tool to use\n" +
                        "\t- [....]: arguments for tool\n\n" +
                        "Place argument values with spaces in quotes, e.g.: \"file here.test\"\n";

    private static final Set<Tool> tools;

    static {
        tools = new HashSet<>();
        tools.add(new FontEncoder());
    }

    public static void main(final String args[]) throws Exception {
        printIntro();

        if (args.length == 0) {
            printGeneralUsage(Optional.empty());
        }

        try {
            final Map<String, String> arguments = parseArguments(args);
            executeByArguments(arguments);
            System.out.println("\n"+horizontalLine+"\n");
            System.out.println("END - µC-Graphics-Tools");

        } catch ( ArgumentParsingException e ) {
            printGeneralUsage(Optional.of(e.getMessage()));
        } catch ( ArgumentsMissingException e ) {
            printGeneralUsage(Optional.of(e.getMessage()));
        } catch ( ToolExecutionException e ) {
            printGeneralUsage(Optional.of(e.getMessage()));
        }
    }

    private static void executeByArguments(final Map<String, String> arguments) throws ArgumentsMissingException, ToolExecutionException {
        final String toolKey = arguments.get("T");
        if (  toolKey == null ) throw new ArgumentsMissingException("Tool key not specified by '-T'!");

        Tool requestedTool = null;
        for ( final Tool availableTool : tools ) {
            if ( availableTool.getToolKey().toLowerCase().equals(toolKey.toLowerCase()) ) {
                requestedTool = availableTool;
                break;
            }
        }

        try {
            final Map<String, String> requiredArguments = selectArguments(requestedTool.getRequiredArguments().keySet(), arguments, true);
            final Map<String, String> optionalArguments = selectArguments(requestedTool.getOptionalArguments().keySet(), arguments, false);

            System.out.println("Executing tool '"+requestedTool.getName()+"'");
            System.out.println(horizontalLine+"\n");
            final String message = requestedTool.execute(requiredArguments, optionalArguments);
            System.out.println("\n"+horizontalLine+"\n");
            System.out.println("Execution of '"+requestedTool.getName()+"' finished with:\n\n"+message);
        } catch ( ArgumentsMissingException e ) {
            throw new ArgumentsMissingException("Tool '"+requestedTool.getName()+"' misses argument: "+e.getMessage());
        } catch ( ToolExecutionException e ) {
            System.out.println("\n"+horizontalLine+"\n");
            System.out.println("Execution of '"+requestedTool.getName()+"' crashed with:\n\n"+e.getMessage());
        }
    }

    private static Map<String, String> selectArguments(final Set<String> keysToSelect,
                                                       final Map<String, String> availableArguments,
                                                       final boolean argumentsMustBePresent) throws ArgumentsMissingException {

        if ( keysToSelect == null ) throw new NullPointerException("keysToSelect must not be null.");
        if ( availableArguments == null ) throw new NullPointerException("availableArguments must not be null.");

        final Map<String, String> selectedArguments = new HashMap<>();
        for (final Map.Entry<String, String> availableArgument : availableArguments.entrySet() ) {
            if ( keysToSelect.contains(availableArgument.getKey()) ) {
                selectedArguments.put(availableArgument.getKey(), availableArgument.getValue());
            }
        }

        if ( argumentsMustBePresent ) {
            for ( final String requestedArgument : keysToSelect ) {
                if ( selectedArguments.get(requestedArgument) == null ) {
                    throw new ArgumentsMissingException("Argument '"+requestedArgument+"' missing!");
                }
            }
        }

        return selectedArguments;
    }

    private static Map<String, String> parseArguments(final String args[]) throws ArgumentParsingException {
        if ( args == null ) throw new NullPointerException("args must not be null.");
        if ( args.length == 0 ) throw new IllegalArgumentException("args must not be empty.");

        final Map<String, String> arguments = new HashMap<>();

        for ( final String arg : args ) {
            if ( arg.length() < 3 ) {
                throw new ArgumentParsingException("Argument '"+arg+"' invalid. Pattern is: [-][arg_identifier][arg_value].");
            }

            if ( !arg.startsWith("-") ) {
                throw new ArgumentParsingException("Argument '"+arg+"' invalid. Must start with hyphen!");
            }

            final String key = arg.substring(1, 2);
            if ( key.equals("\"") ) {
                throw new ArgumentParsingException("Quote '\"' not allowed as argument identifier.");
            }

            arguments.put(key.toUpperCase(), arg.substring(2));
        }

        return arguments;
    }

    private static void printIntro() {
        System.out.println(appHeader);
    }

    private static void printGeneralUsage(final Optional<String> message) {
        if ( message.isPresent() ) {
            System.out.println("Message:\n");
            System.out.println("\t--> "+message.get());

            System.out.println("\n"+horizontalLine+"\n");
        }

        System.out.println(usage);
        System.out.println(horizontalLine+"\n");
        System.out.println("Supported tools:\n");
        printSupportedTools();
    }

    private static void printSupportedTools() {
        for ( final Tool tool : tools ) {
            printSupportedTool(tool);
        }
    }

    private static void printSupportedTool(final Tool tool) {
        if ( tool == null ) throw new NullPointerException("tool must not be null.");

        System.out.println("# "+tool.getName()+":");
        System.out.println("\tkey:           "+tool.getToolKey());
        System.out.println("\tdescription:   "+tool.getDescription());
        System.out.println("\trequired args: ");
        for ( final Map.Entry<String, String> requiredArg : tool.getRequiredArguments().entrySet() ) {
            System.out.println("\t\t- '"+requiredArg.getKey()+"': "+requiredArg.getValue());
        }
        System.out.println("\toptional args: ");
        for ( final Map.Entry<String, String> optionalArg : tool.getOptionalArguments().entrySet() ) {
            System.out.println("\t\t- '"+optionalArg.getKey()+"': "+optionalArg.getValue());
        }
        System.out.println("\tsupported platforms: ");
        for (final TargetPlatform targetPlatform : tool.getSupportedTargetPlatforms() ) {
            System.out.println("\t\t- ["+targetPlatform.getPlatformKey()+"] '"+targetPlatform.getPlatformName()+"': "+targetPlatform.getDescription());
        }
    }

}
