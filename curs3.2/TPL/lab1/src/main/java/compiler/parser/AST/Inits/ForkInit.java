package compiler.parser.AST.Inits;

import compiler.parser.AST.Node;
import compiler.parser.AST.TypeInit;

public abstract class ForkInit implements Node {
    TypeInit type;
    public abstract void chooseType();
    public abstract int visit(String rootName, int index, StringBuilder sb);


    public TypeInit getType() {
        return type;
    }
}
