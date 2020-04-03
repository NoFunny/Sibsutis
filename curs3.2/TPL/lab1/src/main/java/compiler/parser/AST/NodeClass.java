package compiler.parser.AST;

import compiler.lexer.Token;
import compiler.parser.AST.Inits.NodeInit;

import java.util.ArrayList;
import java.util.List;

public class NodeClass implements Node {
    private Token modAccessToken;
    private Token idToken;
    private List<NodeInit> initList = new ArrayList<>();
    private NodeMainMethod mainMethod;

    public void setModAccessToken(Token modAccessToken) {
        this.modAccessToken = modAccessToken;
    }

    public void setIdToken(Token idToken) {
        this.idToken = idToken;
    }

    public void setMainMethod(NodeMainMethod mainMethod) {
        this.mainMethod = mainMethod;
    }

    public List<NodeInit> getInitList() {
        return initList;
    }

    public NodeMainMethod getMainMethod() {
        return mainMethod;
    }

    public void addInit(NodeInit nodeInit) {
        initList.add(nodeInit);
    }

    public String wrapperVisit(int startIndex) { // start point for wrapper GraphViz
        StringBuilder sb = new StringBuilder();

        sb.append("digraph AST {\n" +
                "node [shape=\"box\", style=\"filled\", margin=\"0.1\"];\n");
        String rootNode = "class";
        startIndex = this.visit(rootNode, startIndex, sb);
        sb.append("}");

        System.out.println(String.format("Total %d nodes", startIndex));

        return sb.toString();
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String labelRootName = String.format("\"%s%d\"", rootNode, index++);
        sb.append(String.format("%s [label=\"%s\\nModAccess=%s\\nid=%s\"];\n",
                labelRootName,
                rootNode,
                modAccessToken.getValue(),
                idToken.getValue()));

        for (NodeInit nodeInit : initList) {
            index = nodeInit.visit(labelRootName, index++, sb);
        }

        index = mainMethod.visit(labelRootName, index, sb);

        return index;
    }
}
