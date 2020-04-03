package compiler.parser.AST.Else;

import compiler.lexer.Token;
import compiler.parser.AST.Node;

public abstract class NodeElse implements Node {
    private Token elseToken;

    public void setElseToken(Token elseToken) {
        this.elseToken = elseToken;
    }
}