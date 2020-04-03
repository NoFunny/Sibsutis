package compiler.parser.AST.Inits;

import compiler.parser.AST.TypeInit;
import compiler.parser.AST.statement.NodeExpression;

public class ForkInitVar extends ForkInit {
    private NodeExpression expression;

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeExpression getExpression() {
        return expression;
    }

    @Override
    public void chooseType() {
        type = TypeInit.VAR;
    }

    @Override
    public int visit(String rootName, int index, StringBuilder sb) {
        String nameNode = "VAR";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        index = expression.visit(labelNameNode, index, sb);

        sb.append(String.format("%s -> %s;\n",
                rootName,
                labelNameNode));

        return index;
    }
}
