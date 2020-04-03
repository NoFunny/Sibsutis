package compiler.parser.AST.Else;

import compiler.parser.AST.statement.NodeConditional;

public class NodeIfElse extends NodeElse{
    private NodeConditional conditional;

    public void setConditional(NodeConditional conditional) {
        this.conditional = conditional;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "ELSE";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        index = conditional.visit(labelNameNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
