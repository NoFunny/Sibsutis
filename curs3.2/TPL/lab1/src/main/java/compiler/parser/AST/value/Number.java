package compiler.parser.AST.value;

public class Number extends GenericValue {
    private boolean isNegative;

    public void setNegative(boolean isNegative) {
        this.isNegative = isNegative;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "NUMBER";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        String negative = isNegative ? "-" : "";

        sb.append(String.format("%s [label=\"%s\\n%s%s\"];\n",
                labelNameNode,
                nameNode,
                negative,
                getValue().getValue()));

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
