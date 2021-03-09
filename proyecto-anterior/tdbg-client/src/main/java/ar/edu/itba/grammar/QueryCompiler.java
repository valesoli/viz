package ar.edu.itba.grammar;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class QueryCompiler {

    public String compile(String query) {
        TempoGraphLexer lexer = new TempoGraphLexer(CharStreams.fromString(query));
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.getInstance());
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        TempoGraphParser parser = new TempoGraphParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.getInstance());

        GrammarListener listener = new GrammarListener();
        ParseTreeWalker.DEFAULT.walk(listener, parser.query());
        return listener.getCypherQuery();
    }

}
