package compiler.parser.AST.statement;

import compiler.parser.AST.Node;

public abstract class NodeStatement implements Node {
    abstract public int visit(String rootNode, int index, StringBuilder sb);
}
