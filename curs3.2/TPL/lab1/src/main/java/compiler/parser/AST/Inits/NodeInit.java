package compiler.parser.AST.Inits;

import compiler.lexer.Token;
import compiler.parser.AST.statement.NodeStatement;

public class NodeInit extends NodeStatement {
    private Token dataType;
    private Token id;
    private ForkInit forkInit;

    public Token getDataType() {
        return dataType;
    }

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public Token getId() {
        return id;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public ForkInit getForkInit() {
        return forkInit;
    }

    public void setForkInit(ForkInit forkInit) {
        this.forkInit = forkInit;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"init%d\"", index++);

        sb.append(String.format("%s [label=\"INIT\\nDataType=%s\\nid=%s\\nType=%s\"];\n",
                thisNode,
                dataType.getValue(),
                id.getValue(),
                forkInit.getType()));

        index = forkInit.visit(thisNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
