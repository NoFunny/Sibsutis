package compiler.parser.AST.Else;

import compiler.parser.AST.statement.NodeStatement;

import java.util.ArrayList;
import java.util.List;

public class NodeJustElse extends NodeElse{
    private List<NodeStatement> statementList = new ArrayList<>();

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    public List<NodeStatement> getStatementList() {
        return statementList;
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

        if (statementList.size() > 0) {
            String nameStatementNode = "STATEMENTS";
            String labelNameStatementNode = String.format("\"%s%d\"", nameStatementNode, index++);

            sb.append(String.format("%s [label=\"%s\"];\n",
                    labelNameStatementNode,
                    nameStatementNode));

            for (NodeStatement statement : statementList) {
                if (statement != null) {
                    index = statement.visit(labelNameStatementNode, index++, sb);
                }
            }

            sb.append(String.format("%s -> %s;\n", labelNameNode, labelNameStatementNode));
        }

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
