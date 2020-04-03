package compiler.parser.AST.value;

import java.util.ArrayList;
import java.util.List;

public class FuncCall extends GenericValue {
    // value is id
    private List<GenericValue> argsCall = new ArrayList<>();

    public void setArgsCall(List<GenericValue> argsCall) {
        this.argsCall = argsCall;
    }

    public List<GenericValue> getArgsCall() {
        return argsCall;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "FUNCCAL";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\\nid=%s\"];\n",
                labelNameNode,
                nameNode,
                value.getValue()));

        //
        String nameGenerticValue = "VALUES";
        String labelGenerticValue = String.format("\"%s%d\"", nameGenerticValue, index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelGenerticValue,
                nameGenerticValue));

        for (GenericValue argCall : argsCall) {
            index = argCall.visit(labelGenerticValue, index++, sb);
        }

        sb.append(String.format("%s -> %s;\n", labelNameNode, labelGenerticValue));
        //

        sb.append(String.format("%s -> %s;\n", rootNode, labelNameNode));

        return index;
    }
}
