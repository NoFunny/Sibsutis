package compiler.parser.AST;

import compiler.lexer.Token;
import compiler.parser.AST.statement.NodeStatement;

import java.util.ArrayList;
import java.util.List;

public class NodeMainMethod implements Node {
    private Token publicToken;
    private Token staticToken;
    private Token voidToken;
    private Token idMainToken;
    private Token gettingDataTypeToken; // String
    private Token idArgsToken;
    private List<NodeStatement> statementList = new ArrayList<>();

    public void setPublicToken(Token publicToken) {
        this.publicToken = publicToken;
    }

    public void setStaticToken(Token staticToken) {
        this.staticToken = staticToken;
    }

    public void setVoidToken(Token voidToken) {
        this.voidToken = voidToken;
    }

    public void setIdMainToken(Token idMainToken) {
        this.idMainToken = idMainToken;
    }

    public void setGettingDataTypeToken(Token gettingDataTypeToken) {
        this.gettingDataTypeToken = gettingDataTypeToken;
    }

    public void setIdArgsToken(Token idArgsToken) {
        this.idArgsToken = idArgsToken;
    }

    public void addStatement(NodeStatement statement) {
        statementList.add(statement);
    }

    public List<NodeStatement> getStatementList() {
        return statementList;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "MAINMETHOD";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        //
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
        //

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
