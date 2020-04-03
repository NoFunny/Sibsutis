package compiler.parser.AST.ArrayMember;

import compiler.lexer.Token;

public class ArrayMemberId extends ArrayMember{
    private Token id;

    public ArrayMemberId(Token id) {
        this.id = id;
    }

    public Token getId() {
        return id;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "AR_MEM_ID";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\\n%s\"];\n",
                labelNameNode,
                nameNode,
                id.getValue()));

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
