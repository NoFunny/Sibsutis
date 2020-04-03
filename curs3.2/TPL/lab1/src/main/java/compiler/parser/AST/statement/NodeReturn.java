package compiler.parser.AST.statement;

import compiler.lexer.Token;

public class NodeReturn extends NodeStatement {
    private Token returnToken;
    private NodeExpression expression;

    public void setReturnToken(Token returnToken) {
        this.returnToken = returnToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeExpression getExpression() {
        return expression;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "RETURN";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        index = expression.visit(labelNameNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
