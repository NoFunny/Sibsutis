package compiler.identifierTable;

import compiler.parser.AST.NodeArgsInit;

public class ArgId implements GenericUnit {
    private NodeArgsInit node;

    public ArgId(NodeArgsInit node) {
        this.node = node;
    }

    public NodeArgsInit getNode() {
        return node;
    }
}
