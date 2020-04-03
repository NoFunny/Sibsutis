package compiler.parser.AST.value;

public class StrLiteral extends GenericValue {
    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "STR_LITERAL";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\\nid=%s\"];\n",
                labelNameNode,
                nameNode,
                value.getValue()));

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
