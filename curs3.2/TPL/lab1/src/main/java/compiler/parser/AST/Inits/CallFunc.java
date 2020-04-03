package compiler.parser.AST.Inits;

import compiler.parser.AST.TypeInit;
import compiler.parser.AST.statement.NodeStatement;
import compiler.parser.AST.NodeArgsInit;

import java.util.ArrayList;
import java.util.List;

public class CallFunc extends ForkInit {
    private List<NodeArgsInit> nodeArgsInitList = new ArrayList<>(); // optional
    private List<NodeStatement> statementList = new ArrayList<>(); // too

    public void addArgInit(NodeArgsInit node) {
        nodeArgsInitList.add(node);
    }

    public void addStatement(NodeStatement node) {
        statementList.add(node);
    }

    public List<NodeStatement> getStatementList() {
        return statementList;
    }

    public List<NodeArgsInit> getNodeArgsInitList() {
        return nodeArgsInitList;
    }

    @Override
    public void chooseType() {
        type = TypeInit.FUNC;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        if (nodeArgsInitList.size() > 0) {
            String nameArgsNode = "ARGS";
            String labelArgsNode = String.format("\"%s%d\"", nameArgsNode, index++);

            sb.append(String.format("%s [label=\"%s\"];\n",
                    labelArgsNode,
                    nameArgsNode));

            for (NodeArgsInit nodeArgsInit : nodeArgsInitList) {
                index = nodeArgsInit.visit(labelArgsNode, index++, sb);
            }

            sb.append(String.format("%s -> %s;\n", rootNode, labelArgsNode));
        }

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

            sb.append(String.format("%s -> %s;\n", rootNode, labelNameStatementNode));
        }

        return index;
    }
}
