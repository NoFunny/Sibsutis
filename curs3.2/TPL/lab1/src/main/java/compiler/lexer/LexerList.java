package compiler.lexer;

import compiler.parser.ParseException.CriticalParseException;
import jdk.nashorn.internal.parser.Lexer;

public class LexerList {
    private Tokenizer tokenizer;
    private Token lookahead;

    public LexerList(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        consume();
    }

    public Token getLookahead() {
        return lookahead;
    }

    public Tokenizer getToken() {
        return tokenizer;
    }

    private void consume() {
        lookahead = tokenizer.getNextToken();
    }

    public void match(String type) throws CriticalParseException {
        if (lookahead.getType().equals(type)) {
            consume();
        } else {
            throw new CriticalParseException("expecting <" + type
                    + ">, but found is <"+ lookahead.getType() +
                    ":" + lookahead.getValue()
                    + "> in " + lookahead.getLocation());
        }
    }

    public void matchTypeAndCheckValue(String type, String value) throws CriticalParseException {
        if (lookahead.getType().equals(type) && lookahead.getValue().equals(value)) {
            consume();
        } else {
            throw new CriticalParseException("expecting <" + type
                    + ">, but found is <"+ lookahead.getType() +
                    ":" + lookahead.getValue()
                    + "> in " + lookahead.getLocation());
        }
    }

    public boolean matchLite(String x) {
        if (lookahead.getType().equals(x)) {
            return true;
        }

        return false;
    }

    public void matchOneOf(String... tokens) throws CriticalParseException {
        boolean matchIs = false;
        for (String token : tokens) {
            if (lookahead.getType().equals(token)) {
                consume();
                matchIs = true;
            }
        }

        if (!matchIs) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tokens.length; i++) {
                sb.append(tokens[i]);
                if (i != tokens.length - 1) {
                    sb.append(" or ");
                }
            }

            throw new CriticalParseException("expecting <" + sb.toString()
                    + ">, but found is <"+ lookahead.getType() +
                    ":" + lookahead.getValue()
                    + "> in " + lookahead.getLocation());
        }
    }

}
