package compiler.parser.AST.operator;

import compiler.lexer.Token;

public class Operator {
    private Token value;

    public Operator(Token value) {
        this.value = value;
    }

    public Token getValue() {
        return value;
    }
}
