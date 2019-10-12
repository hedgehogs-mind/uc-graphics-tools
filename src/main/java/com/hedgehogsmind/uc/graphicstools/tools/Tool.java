package com.hedgehogsmind.uc.graphicstools.tools;

import com.hedgehogsmind.uc.graphicstools.target.TargetPlatform;
import com.hedgehogsmind.uc.graphicstools.tools.exceptions.ToolExecutionException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class Tool {

    /**
     * Key of the tool: passed as argument to main method to select tool.
     */
    private final String toolKey;

    /**
     * Name of the tool.
     */
    private final String name;

    /**
     * Description of the tool: what does it do?
     */
    private final String description;

    /**
     * List of required (command line) arguments. Argument names (keys) are mapped to argument descriptions (values).
     */
    private final Map<String, String> requiredArguments;

    /**
     * List of optional (command line) arguments. Argument names (keys) are mapped to argument descriptions (values).
     */
    private final Map<String, String> optionalArguments;

    /**
     * Set of target platforms supported by the tool.
     */
    private final Set<TargetPlatform> supportedTargetPlatforms;

    protected Tool(String toolKey, String name, String description, Map<String, String> requiredArguments, Map<String, String> optionalArguments, Set<TargetPlatform> supportedTargetPlatforms) {
        this.toolKey = toolKey;
        this.name = name;
        this.description = description;
        this.requiredArguments = requiredArguments;
        this.optionalArguments = optionalArguments;
        this.supportedTargetPlatforms = supportedTargetPlatforms;
    }

    public abstract String execute(Map<String, String> requiredArguments, Map<String, String> optionalArguments) throws ToolExecutionException;

    public boolean supportsPlatform(final TargetPlatform targetPlatform) {
        return this.supportedTargetPlatforms.stream()
                    .anyMatch(tp -> tp.equals(targetPlatform));
    }

    public String getToolKey() {
        return toolKey;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getRequiredArguments() {
        return requiredArguments;
    }

    public Map<String, String> getOptionalArguments() {
        return optionalArguments;
    }

    public Set<TargetPlatform> getSupportedTargetPlatforms() {
        return supportedTargetPlatforms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tool tool = (Tool) o;
        return Objects.equals(getToolKey(), tool.getToolKey()) &&
                Objects.equals(getName(), tool.getName()) &&
                Objects.equals(getDescription(), tool.getDescription()) &&
                Objects.equals(getRequiredArguments(), tool.getRequiredArguments()) &&
                Objects.equals(getOptionalArguments(), tool.getOptionalArguments()) &&
                Objects.equals(getSupportedTargetPlatforms(), tool.getSupportedTargetPlatforms());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToolKey(), getName(), getDescription(), getRequiredArguments(), getOptionalArguments(), getSupportedTargetPlatforms());
    }

    @Override
    public String toString() {
        return "Tool{" +
                "toolKey='" + toolKey + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", requiredArguments=" + requiredArguments +
                ", optionalArguments=" + optionalArguments +
                ", supportedTargetPlatforms=" + supportedTargetPlatforms +
                '}';
    }

}
