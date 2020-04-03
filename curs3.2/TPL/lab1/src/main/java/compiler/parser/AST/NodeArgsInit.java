package compiler.parser.AST;

import compiler.lexer.Token;
import compiler.parser.AST.Node;
import compiler.parser.AST.TypeInit;

public class NodeArgsInit implements Node {
    private Token dataType;
    private Token id;
    private TypeInit typeInit; // var or array

    public void setDataType(Token dataType) {
        this.dataType = dataType;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public void setTypeInit(TypeInit typeInit) {
        this.typeInit = typeInit;
    }

    public Token getId() {
        return id;
    }

    public Token getDataType() {
        return dataType;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String thisNode = String.format("\"args%d\" ", index++);

        sb.append(String.format("%s [label=\"DataType=%s\nid=%s\nType=%s\"];\n",
                thisNode,
                dataType.getValue(),
                id.getValue(),
                typeInit));

        sb.append(String.format("%s -> %s;\n", rootNode, thisNode));

        return index;
    }
}
