package compiler.identifierTable;

import compiler.parser.AST.Inits.NodeInit;

public class Id implements GenericUnit {
    private NodeInit node;

    Id(NodeInit node) {
        this.node = node;
    }

    public NodeInit getNode() {
        return node;
    }
}
