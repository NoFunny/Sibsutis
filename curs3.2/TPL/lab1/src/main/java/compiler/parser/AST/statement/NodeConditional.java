package compiler.parser.AST.statement;

import compiler.lexer.Token;
import compiler.parser.AST.Else.NodeElse;

import java.util.ArrayList;
import java.util.List;

public class NodeConditional extends NodeStatement {
    private Token ifToken;
    private NodeExpression expression;
    private List<NodeStatement> statementList = new ArrayList<>();
    private NodeElse elseNode;

    public void setIfToken(Token ifToken) {
        this.ifToken = ifToken;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public void setElseNode(NodeElse elseNode) {
        this.elseNode = elseNode;
    }

    public NodeExpression getExpression() {
        return expression;
    }

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    public List<NodeStatement> getStatementList() {
        return statementList;
    }

    public NodeElse getElseNode() {
        return elseNode;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "IF";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        index = expression.visit(labelNameNode, index, sb);

        if (statementList.size() > 0) {
            String nameStatementNode = "STATEMENTS";
            String labelNameStatementNode = String.format("\"%s%d\"", nameStatementNode, index++);

            sb.append(String.format("%s [label=\"%s\"];\n",
                    labelNameStatementNode,
                    nameStatementNode));

            for (NodeStatement statement : statementList) {
                index = statement.visit(labelNameStatementNode, index++, sb);
            }

            sb.append(String.format("%s -> %s;\n", labelNameNode, labelNameStatementNode));
        }

        if (elseNode != null) {
            index = elseNode.visit(labelNameNode, index, sb);
        }

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
