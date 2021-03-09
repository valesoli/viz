package ar.edu.itba.grammar;

import ar.edu.itba.grammar.struct.*;
import ar.edu.itba.grammar.struct.params.FunctionParams;
import ar.edu.itba.grammar.struct.params.TemporalFilterParams;
import ar.edu.itba.util.Pair;
import ar.edu.itba.util.Time;
import ar.edu.itba.util.Utils;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GrammarListener extends TempoGraphBaseListener {

    /**
     * Stores the variables expanded used in where or select
     */
    private final Map<String, Variable> variableAttributes = new HashMap<>();

    private final Set<String> functionVariables = new HashSet<>();

    /**
     * Stores the conditions defined in WHERE
     */
    private List<BaseWhereCondition> whereConditions = new LinkedList<>();

    private BaseWhereCondition currentWhereCondition;

    /**
     * Used to get the variable referenced by an alias
     * Example: p1.Name as p1_name => Key: p1_name, Value: p1
     */
    private Map<String, String> aliasesVariable = new HashMap<>();

    /**
     * Used in where conditions to replace aliases accessors with the corresponding .value
     * Example: p1_name = 'John' => p1_name.value = 'John'
     */
    private Set<Token> aliasesInWhere;

    private List<ReturnEntity> returnEntities = new LinkedList<>();

    private final Set<String> objectVariables = new HashSet<>();

    private final Set<String> edgesVariables = new HashSet<>();

    /**
     * Variables defined implicitly by the parser
     * Used to generate a variable for an object definition
     * Used for SELECT *
     */
    private final Set<String> parserVariables = new HashSet<>();

    /**
     * Stores edges variables defined in WHEN MATCH
     */
    private final Set<String> whenEdgesVariables = new HashSet<>();

    /**
     * Stores object variables defined in WHEN MATCH
     */
    private final Set<String> whenObjectsVariables = new HashSet<>();

    /**
     * Stores objects variables references (to outer query) used in WHEN MATCH
     */
    private Set<String> whenObjectsReferences = new HashSet<>();

    private Optional<TemporalFilterParams> snapshotParams = Optional.empty();

    private Optional<TemporalFilterParams> betweenParams = Optional.empty();

    private Optional<TemporalFilterParams> whenParams = Optional.empty();

    private FunctionParams.Builder functionParamsBuilder;

    private List<FunctionParams> functionParams = new LinkedList<>();

    private ParserVariables parserVariablesGenerator = new ParserVariables();

    private final CypherOutputGenerator cyOut = new CypherOutputGenerator(parserVariablesGenerator, variableAttributes);

    private String cypherQuery;

    private boolean selectAll = false;

    /**
     * Return expressions that will be appended to return
     * User index of selection
     */
    private Map<Integer, String> returnExpr = new HashMap<>();

    /**
     * Used to order return attributes according to user defined order in select
     * AtomicInteger class so it can be modify by helper
     */
    private final AtomicInteger selectIndex = new AtomicInteger(1);

    private String functionVariableName;

    private StringBuilder path = new StringBuilder();

    private TempoGraphParser.Where_connectorContext connectorContext;

    private boolean removeNextConnector = false;

    private GrammarListenerHelper helper = new GrammarListenerHelper(variableAttributes, cyOut, selectIndex);

    @Override
    public void exitSelect_clause(final TempoGraphParser.Select_clauseContext ctx) {
        if (ctx.TIMES() != null)
            selectAll = true;
    }

    @Override
    public void exitSelect_exp(final TempoGraphParser.Select_expContext ctx) {
        if (ctx.attr() != null) {
            final String variable = ctx.attr().WORD(0).getText();
            final String attribute = ctx.attr().WORD(1).getText();

            if (ctx.alias() != null) {
                final String alias = ctx.alias().WORD().getText();
                if (aliasesVariable.containsKey(alias))
                    throw new ParseCancellationException(Utils.generateErrorMessage("alias " + alias + " already defined", ctx.alias().WORD().getSymbol()));
                variableAttributes.get(variable).getAttributes().get(attribute).getValue().setAlias(alias);
                aliasesVariable.put(alias, variable);
            }
        }
        else if (ctx.WORD() != null){
            final String variable = ctx.WORD().getText();
            final Variable v = variableAttributes.getOrDefault(variable, new Variable(variable));
            if (v.isSelected())
                throw new ParseCancellationException(Utils.generateErrorMessage("variable " + variable + " already selected", ctx.WORD().getSymbol()));
            variableAttributes.put(variable, v);
            variableAttributes.get(variable).setSelectIndex(selectIndex.getAndIncrement());
        }
        else
            returnExpr.put(selectIndex.getAndIncrement(), ctx.getText());
    }

    /**
     * Maps the attribute to the corresponding value node
     */
    @Override
    public void enterAttr(final TempoGraphParser.AttrContext ctx) {
        if (ctx.getParent() instanceof TempoGraphParser.SumContext || ctx.getParent() instanceof TempoGraphParser.SizeContext)
            return;

        final CommonToken variableToken = (CommonToken) ctx.WORD(0).getSymbol();
        final CommonToken attributeToken = (CommonToken) ctx.WORD(1).getSymbol();

        if (cyOut.getState() == QueryState.WHEN && edgesVariables.contains(variableToken.getText()))
            throw new ParseCancellationException(Utils.generateErrorMessage("when clause cant have references to an outter query edge", variableToken));

        final boolean selectAttribute = !(ctx.getParent() instanceof TempoGraphParser.AtomContext);
        Optional<TempoGraphParser.AliasContext> aliasCtx = Optional.empty();
        if (selectAttribute) {
            final TempoGraphParser.Select_expContext select_expContext = ((TempoGraphParser.Select_expContext) (ctx.getParent()));
            final boolean aliasPresent = select_expContext.alias() != null;
            if (aliasPresent)
                aliasCtx = Optional.of(select_expContext.alias());
        }
        final String valueVariable = helper.attributeFound(variableToken.getText(), attributeToken.getText(), selectAttribute, aliasCtx, whenEdgesVariables);

        if (!selectAttribute && helper.notContained(variableToken.getText(), functionVariables, edgesVariables, whenEdgesVariables)) {
            variableToken.setText(valueVariable);
            attributeToken.setText(CypherOutputGenerator.VALUE.toLowerCase());
        }
    }

    @Override
    public void enterPath(final TempoGraphParser.PathContext ctx) {
        cyOut.appendCommaForPaths();
    }

    @Override
    public void enterAtom(final TempoGraphParser.AtomContext ctx) {
        if (ctx.WORD() != null && !functionVariables.contains(ctx.WORD().getText())) {
            final String valueVariable = ctx.WORD().getText();
            String objectVariable = aliasesVariable.get(valueVariable);
            Variable v = variableAttributes.get(objectVariable);
            final Value value = v.getAttributes().values().stream().map(Attribute::getValue).filter(val -> val.getName().equals(valueVariable))
                    .findFirst().get();
            value.setFoundInSelect(false);
        }
    }

    @Override
    public void exitPath(final TempoGraphParser.PathContext ctx) {
        if (cyOut.getState() == QueryState.MATCH) {
            final Set<String> objectVariables = helper.getObjectVariablesFromPath(ctx);
            cyOut.addPath(objectVariables, path.toString());
        }
        path.setLength(0);
    }

    @Override
    public void enterObject_variable(final TempoGraphParser.Object_variableContext ctx) {
        cyOut.appendPartOfPathToMatch(ctx.getText());
        if (cyOut.getState() == QueryState.WHEN) {
            if (!objectVariables.contains(ctx.WORD().getSymbol().getText()))
                whenObjectsVariables.add(ctx.WORD().getSymbol().getText());
            else
                whenObjectsReferences.add(ctx.WORD().getText());
        }
        else
            objectVariables.add(ctx.WORD().getSymbol().getText());
        final String variable = ctx.WORD().getText();
        if (selectAll && cyOut.getState() != QueryState.CALL && !variableAttributes.containsKey(variable)) {
            parserVariables.add(variable);
            final Variable v = new Variable(variable);
            variableAttributes.put(variable, v);
            v.setSelectIndex(selectIndex.getAndIncrement());
        }
        path.append(ctx.getText());
    }

    @Override
    public void enterObject_def(final TempoGraphParser.Object_defContext ctx) {
        String variable = null;
        final String objectType;
        final boolean variablePresent = helper.variableDefinition(ctx.WORD());
        if (variablePresent) {
            variable = ctx.WORD(0).getText();
            objectType = ctx.WORD(1).getText();
            helper.validateDefinition("variable", "already defined", variable, true,
                    Optional.of(ctx.WORD(0).getSymbol()), whenObjectsVariables, functionVariables, aliasesVariable.keySet());
            if (cyOut.getState() == QueryState.WHEN)
                whenObjectsVariables.add(ctx.WORD(0).getSymbol().getText());
            else
                objectVariables.add(ctx.WORD(0).getSymbol().getText());
        }
        else
            objectType = ctx.WORD(0).getSymbol().getText();
        if (cyOut.getState() != QueryState.CALL) {
            if (selectAll && !variableAttributes.containsKey(variable))
                variable = helper.nodeSelectAll(ctx.WORD(), variable, parserVariables, ctx.COLON());
            else if (!variablePresent)
                variable = helper.generateVariableForDefinition(ctx.WORD(0).getText(), ctx.COLON());
        }
        path.append(cyOut.matchAppendObjectDef(Optional.ofNullable(variable), objectType));
    }

    @Override
    public void exitDirect_edge(final TempoGraphParser.Direct_edgeContext ctx) {
        cyOut.appendPartOfPathToMatch(ctx.getText());
        path.append(ctx.getText());
    }

    @Override
    public void exitRelation_edge(final TempoGraphParser.Relation_edgeContext ctx) {
        cyOut.appendPartOfPathToMatch(ctx.getText());
        path.append(ctx.getText());
    }


    /**
     * When with only one edge
     */
    @Override
    public void enterEdge_def(final TempoGraphParser.Edge_defContext ctx) {
        final boolean variablePresent = helper.variableDefinition(ctx.WORD());
        String edgeVariable = null;
        if (variablePresent) {
            edgeVariable = ctx.WORD(0).getText();
            if (cyOut.getState() == QueryState.WHEN)
                whenEdgesVariables.add(ctx.WORD(0).getText());
            else
                edgesVariables.add(ctx.WORD(0).getText());
        }
        if (cyOut.getState() != QueryState.CALL) {
            if (selectAll)
                edgeVariable = helper.nodeSelectAll(ctx.WORD(), ctx.WORD(0).getText(), parserVariables, ctx.COLON());
            else if (!variablePresent)
                edgeVariable = helper.generateVariableForDefinition(ctx.WORD(0).getText(), ctx.COLON());
        }
        if (cyOut.getState() == QueryState.WHEN)
            cyOut.setWhenEdge(edgeVariable);
    }

    @Override
    public void enterCondition(TempoGraphParser.ConditionContext ctx) {
        if (ctx.where_fcall_bool() == null) {
            Set<String> conditionVariables = new HashSet<>();
            aliasesInWhere = new HashSet();
            helper.addConditionVariables(ctx.value(0), conditionVariables, aliasesVariable, aliasesInWhere);
            helper.addConditionVariables(ctx.value(1), conditionVariables, aliasesVariable, aliasesInWhere);

            currentWhereCondition = cyOut.getState() == QueryState.WHEN ? new WhenWhereCondition() : new WhereCondition();
            currentWhereCondition.setConditionVariables(conditionVariables);
        }
    }

    @Override
    public void exitCondition(TempoGraphParser.ConditionContext ctx) {
        if (ctx.where_fcall_bool() == null) {
            for (Token token : aliasesInWhere)
                ((CommonToken) token).setText(token.getText() + CharConstants.POINT + CypherOutputGenerator.VALUE.toLowerCase());
            currentWhereCondition.setCondition(ctx.getText());
            whereConditions.add(currentWhereCondition);
        }

        if (cyOut.getState() != QueryState.WHEN && ctx.where_fcall_bool() != null) {

            if (!cyOut.functionWhereIsEmpty())
                cyOut.appendFunctionWhere(connectorContext.getText());
            //append condition to function where
            cyOut.appendFunctionWhere(functionVariableName);

            //remove function call from where
            ctx.children.clear();

            //remove connector
            if (connectorContext != null)
                connectorContext.children.clear();
            else
                removeNextConnector = true;
        }
    }

    @Override public void enterWhere_connector(TempoGraphParser.Where_connectorContext ctx) {
        connectorContext = ctx;
        if (removeNextConnector) {
            ctx.children.clear();
            removeNextConnector = false;
        }
    }

    @Override
    public void exitWhere_clause(final TempoGraphParser.Where_clauseContext ctx) {
        if (!(ctx.getParent() instanceof TempoGraphParser.When_clauseContext)) {
            cyOut.whereAppend(ctx.where_conditions().getText());
        } else {
            cyOut.whenWhereAppend(ctx.where_conditions().getText());
        }
    }

    @Override
    public void exitMatch_clause(TempoGraphParser.Match_clauseContext ctx) {
        if (cyOut.getState() == QueryState.WHEN)
            cyOut.whenMatchAppend(CharConstants.LINE_SEPARATOR.toString());
        else
            cyOut.matchAppend(CharConstants.LINE_SEPARATOR.toString());
    }

    @Override
    public void enterSkip_clause(TempoGraphParser.Skip_clauseContext ctx) {
        cyOut.skipAppend(ctx.getText());
    }

    @Override
    public void enterLimit_clause(TempoGraphParser.Limit_clauseContext ctx) {
        cyOut.limitAppend(ctx.getText());
    }

    @Override
    public void enterEndpoints_args(final TempoGraphParser.Endpoints_argsContext ctx) {
        helper.validateEndpoints(objectVariables, ctx);
    }

    @Override public void exitProperty_access(TempoGraphParser.Property_accessContext ctx) {
        final TerminalNode propertyNode = ctx.ID() != null ? ctx.ID() : ctx.WORD(1);

        final String node = ctx.WORD(0).getText();
        final String property = propertyNode.getText();

        final String propertyAccess = node + CharConstants.POINT + property;
        ((CommonToken) ctx.WORD(0).getSymbol()).setText(propertyAccess);
        ((CommonToken) ctx.LBRACKET().getSymbol()).setText("");
        ((CommonToken) ctx.RBRACKET().getSymbol()).setText("");
        ((CommonToken) propertyNode.getSymbol()).setText("");
    }

    @Override
    public void enterF_call(final TempoGraphParser.F_callContext ctx) {
        cyOut.setState(QueryState.CALL);
        functionVariableName = ctx.WORD().getText();
        helper.validateDefinition("variable", "already defined", functionVariableName, true,
                Optional.of(ctx.WORD().getSymbol()), objectVariables, whenObjectsVariables, functionVariables);
        functionVariables.add(functionVariableName);
        if (selectAll) {
            final Variable v = new Variable(functionVariableName);
            variableAttributes.put(functionVariableName, v);
            v.setSelectIndex(selectIndex.getAndIncrement());
        }

    }

    @Override
    public void enterWhere_fcall_bool(TempoGraphParser.Where_fcall_boolContext ctx) {
        cyOut.setState(QueryState.CALL);
        functionVariableName = parserVariablesGenerator.createVariable();
        functionVariables.add(functionVariableName);
    }

    @Override
    public void exitWhere_fcall_bool(TempoGraphParser.Where_fcall_boolContext ctx) {
        functionParams.add(functionParamsBuilder.build());
        cyOut.setState(QueryState.MATCH);
    }

    @Override
    public void exitCpath(TempoGraphParser.CpathContext ctx) {
        String functionName = ctx.CPATH() != null ? ctx.CPATH().getText() : ctx.PAIRCPATH().getText();
        if (!(ctx.parent instanceof TempoGraphParser.F_callContext))
            functionName += CypherOutputGenerator.BOOL;
        helper.validatePathArg(functionName, objectVariables, ctx.endpoints_args(), true);
        functionParamsBuilder = helper.handleFunction(functionName,functionVariableName, ctx.endpoints_args());
        if (ctx.interval_arg() ==  null)
            return;
        final String from = Utils.getQuotedString(ctx.interval_arg().time_value(0).getText());
        final String to = Utils.getQuotedString(ctx.interval_arg().time_value(1).getText());
        final String invalidIntervalMessage = Utils.generateErrorMessage("invalid time range in " + functionName + " call", ctx.interval_arg().COMMA().getSymbol());
        final Pair<String, String> interval = helper.getTimeInterval(from, to, invalidIntervalMessage);
        functionParamsBuilder.withTemporalInterval(cyOut.generateInterval(interval.getLeft(), interval.getRight()));
    }

    @Override
    public void exitLatest(TempoGraphParser.LatestContext ctx) {
        final String functionName = ctx.LATESTARRIVAL() != null ? ctx.LATESTARRIVAL().getText() : ctx.LATESTDEPARTURE().getText();
        helper.validatePathArg(functionName, objectVariables, ctx.endpoints_args(), false);
        functionParamsBuilder = helper.handleFunction(functionName,functionVariableName, ctx.endpoints_args());
        if (ctx.time_value() != null) {
            final String to = Utils.getQuotedString(ctx.time_value().getText());
            functionParamsBuilder.withTemporalInterval(cyOut.generateInterval(Granularity.getMinValue(to), to));
        }
    }

    @Override
    public void exitFastest(TempoGraphParser.FastestContext ctx) {
        helper.validatePathArg(ctx.FASTEST().getText(), objectVariables, ctx.endpoints_args(), false);
        functionParamsBuilder = helper.handleFunction(ctx.FASTEST().getText(),functionVariableName, ctx.endpoints_args());
    }

    @Override
    public void exitEarliest(TempoGraphParser.EarliestContext ctx) {
        helper.validatePathArg(ctx.EARLIEST().getText(), objectVariables, ctx.endpoints_args(), false);
        functionParamsBuilder = helper.handleFunction(ctx.EARLIEST().getText(),functionVariableName, ctx.endpoints_args());
    }

    @Override
    public void exitShortest(TempoGraphParser.ShortestContext ctx) {
        helper.validatePathArg(ctx.SHORTEST().getText(), objectVariables, ctx.endpoints_args(), false);
        functionParamsBuilder = helper.handleFunction(ctx.SHORTEST().getText(),functionVariableName, ctx.endpoints_args());
    }

    @Override
    public void exitF_call(final TempoGraphParser.F_callContext ctx) {
        functionParams.add(functionParamsBuilder.build());
        cyOut.setState(QueryState.MATCH);
    }

    @Override
    public void exitQuery_body(final TempoGraphParser.Query_bodyContext ctx) {
        final Map<Integer, ReturnEntity> returnEntities = new HashMap<>();
        for (final Map.Entry<String, Variable> variable : variableAttributes.entrySet()) {
            helper.validateDefinition("Variable", "undefined", variable.getKey(), false,
                    Optional.empty(), objectVariables, edgesVariables, parserVariables, functionVariables);

            if (variable.getValue().isSelected()) {
                if (!functionVariables.contains(variable.getKey()) && !functionVariables.isEmpty())
                    FunctionParams.addSelectedVariable(variable.getKey());
                returnExpr.put(variable.getValue().getSelectIndex(), variable.getKey());
                returnEntities.put(variable.getValue().getSelectIndex(), new ReturnEntity(variable.getKey(), ""));
            }
            helper.addAttributesForVariable(variable.getKey(), variable.getValue().getAttributes().values(),
                    functionVariables, edgesVariables, returnEntities, returnExpr);
        }
        this.returnEntities = helper.orderReturnEntities(returnEntities);
        helper.appendAndOrderReturnExpr(returnExpr);
    }

    @Override
    public void exitSnapshot_clause(final TempoGraphParser.Snapshot_clauseContext ctx) {
        final String value = Utils.getQuotedString(ctx.time_value().getText());
        snapshotParams = Optional.of(new TemporalFilterParams(cyOut.generateTemporalInnerQuery(), value, returnEntities));
    }

    @Override
    public void exitBetween_clause(final TempoGraphParser.Between_clauseContext ctx) {
        final String from = Utils.getQuotedString(ctx.time_value(0).getText());
        final String to = Utils.getQuotedString(ctx.time_value(1).getText());
        final String invalidIntervalMessage = Utils.generateErrorMessage("invalid time range in between clause", ctx.AND().getSymbol());
        final Pair<String, String> interval = helper.getTimeInterval(from, to, invalidIntervalMessage);
        betweenParams = Optional.of(new TemporalFilterParams(cyOut.generateTemporalInnerQuery(), cyOut.generateInterval(interval.getLeft(), interval.getRight()), returnEntities));
    }

    @Override
    public void enterWhen_clause(final TempoGraphParser.When_clauseContext ctx) {
        cyOut.setState(QueryState.WHEN);
        if (ctx.match_clause().path().size() > 1)
            throw new ParseCancellationException("In this version of TempoGraph when clause cant support more than one path in match");
        if (ctx.match_clause().path(0).direct_edge().size() != 1 && ctx.match_clause().path(0).relation_edge().size() != 1)
            throw new ParseCancellationException("In this version of TempoGraph When clause supports exactly one edge");
    }

    @Override
    public void exitWhen_clause(final TempoGraphParser.When_clauseContext ctx) {
        helper.validateDefinitions("Variable", "undefined", Optional.empty(), false,
                variableAttributes.values().stream().map(Variable::getName).collect(Collectors.toList()), objectVariables, edgesVariables, whenEdgesVariables, parserVariables, whenObjectsVariables, functionVariables);

        cyOut.setState(QueryState.MATCH);
        final Set<String> whenVariables = new HashSet<>();
        helper.addObjectVariablesToWhen(whenObjectsReferences, whenVariables);
        whenVariables.addAll(whenObjectsVariables);
        whenVariables.addAll(whenEdgesVariables);

        Set<String> whereConditionsToAdd = helper.handleConditionsForWhen(whereConditions, whenVariables);
        if (!whereConditionsToAdd.isEmpty()) {
            String joinConditions = String.join(CypherOutputGenerator.AND, whereConditionsToAdd);
            cyOut.whenWhereAppend(joinConditions);
        }

        whenParams = Optional.of(new TemporalFilterParams(cyOut.generateTemporalInnerQuery(), "interval", returnEntities));
    }

    @Override
    public void exitQuery(final TempoGraphParser.QueryContext ctx) {
        if ((snapshotParams.isPresent() || betweenParams.isPresent() || whenParams.isPresent()) && !functionVariables.isEmpty())
            throw new ParseCancellationException("In this version of TempoGraph temporal operators and function calls are not supported in the same query");

        if (snapshotParams.isPresent())
            cypherQuery = cyOut.generateSnapshotQuery(snapshotParams.get());
        else if (betweenParams.isPresent())
            cypherQuery = cyOut.generateBetweenQuery(betweenParams.get());
        else if (whenParams.isPresent())
            cypherQuery = cyOut.generateWhenQuery(whenParams.get());
        else
            cypherQuery = cyOut.generateNonTemporalFilterQuery(functionParams);
        FunctionParams.clear();
    }

    @Override
    public void exitTime_value(TempoGraphParser.Time_valueContext ctx) {
        if (!Time.validateTime(Utils.getQuotedString(ctx.getText())))
            throw new ParseCancellationException(Utils.generateErrorMessage("invalid time format", helper.getTimeToken(ctx)));
    }

    public String getCypherQuery() {
        return cypherQuery;
    }
}
