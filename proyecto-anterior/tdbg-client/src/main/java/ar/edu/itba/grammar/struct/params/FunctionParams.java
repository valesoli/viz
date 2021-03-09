package ar.edu.itba.grammar.struct.params;

import ar.edu.itba.grammar.struct.ProcedureConfiguration;

import java.util.LinkedList;
import java.util.List;

public class FunctionParams {

    private String functionName;
    private String variableName;
    private String initialNode;
    private String endingNode;
    private String minLength;
    private String maxLength;
    private String offset;
    private ProcedureConfiguration procedureConfiguration;

    /**
     * Variables that are selected by the user
     * Used to append the selected variables to WITH
     */
    private static List<String> selectedVariables = new LinkedList<>();

    private FunctionParams() {
    }

    public static class Builder {
        private String functionName;
        private String variableName;
        private String initialNode;
        private String endingNode;
        private String minLength;
        private String maxLength;
        private String offset;
        private ProcedureConfiguration procedureConfiguration;

        public Builder(final String functionName, final String variableName, final String initialNode, final String endingNode, final ProcedureConfiguration procedureConfiguration) {
            this.functionName = functionName;
            this.variableName = variableName;
            this.initialNode = initialNode;
            this.endingNode = endingNode;
            this.procedureConfiguration = procedureConfiguration;
        }

        public void withMinLength(final String minLength) {
            this.minLength = minLength;
        }

        public void withMaxLength(final String maxLength) {
            this.maxLength = maxLength;
        }

        public void withOffset(final String offset) {
            this.offset = offset;
        }

        public void withTemporalInterval(final String interval) {
            procedureConfiguration.addTemporalInterval(interval);
        }

        public FunctionParams build() {
            FunctionParams functionParams = new FunctionParams();
            functionParams.functionName = this.functionName;
            functionParams.variableName = this.variableName;
            functionParams.initialNode = this.initialNode;
            functionParams.endingNode = this.endingNode;
            functionParams.minLength = this.minLength;
            functionParams.maxLength = this.maxLength;
            functionParams.offset = this.offset;
            functionParams.procedureConfiguration = this.procedureConfiguration;
            return functionParams;
        }
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getVariableName() {
        return variableName;
    }

    public String[] getArgs() {
        List<String> args = new LinkedList<>();
        args.add(initialNode);
        args.add(endingNode);
        if (minLength != null && maxLength != null) {
            args.add(minLength);
            args.add(maxLength);
        }
        else
            args.add(offset);
        args.add(procedureConfiguration.getConfigurationString());
        return args.toArray(new String[0]);
    }

    public static List<String> getSelectedVariables() {
        return selectedVariables;
    }

    public static void addSelectedVariable(final String variableName) {
        selectedVariables.add(variableName);
    }

    public static void clear() {
        selectedVariables.clear();
    }
}
