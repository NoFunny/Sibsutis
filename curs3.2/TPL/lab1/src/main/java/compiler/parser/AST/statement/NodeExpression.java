package compiler.parser.AST.statement;

import compiler.parser.AST.operator.Operator;
import compiler.parser.AST.value.GenericValue;

public class NodeExpression extends NodeStatement {
    private GenericValue lValue;
    private NodeExpression lExpression;
    private Operator operator;
    private NodeExpression rExpression;
    private GenericValue rValue;

    public GenericValue getlValue() {
        return lValue;
    }

    public void setlValue(GenericValue lValue) {
        this.lValue = lValue;
    }

    public NodeExpression getlExpression() {
        return lExpression;
    }

    public void setlExpression(NodeExpression lExpression) {
        this.lExpression = lExpression;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public NodeExpression getrExpression() {
        return rExpression;
    }

    public void setrExpression(NodeExpression rExpression) {
        this.rExpression = rExpression;
    }

    public GenericValue getrValue() {
        return rValue;
    }

    public void setrValue(GenericValue rValue) {
        this.rValue = rValue;
    }

    @Override
    public String toString() {
        String str = "";

        if (lValue != null) {
            str += lValue.getValue().getValue();
        }
        if (operator != null) {
            str += operator.getValue().getValue();
        }
        if (rValue != null) {
            str += " " + rValue.getValue().getValue();
        }
        if (lExpression != null) {
            str += lExpression.toString();
        }
        if (rExpression != null) {
            str += rExpression.toString();
        }

        return str;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String pickUpNode;
        String pickUpLabel;

        if (operator != null) {
            pickUpNode = operator.getValue().getValue();
        } else {
            pickUpNode = "EXPRESSION";
        }

        pickUpLabel = String.format("\"%s%d\"",
                pickUpNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                pickUpLabel,
                pickUpNode));

        if (lValue != null) {
            index = lValue.visit(pickUpLabel, index, sb);
        }
        if (lExpression != null) {
            index = lExpression.visit(pickUpLabel, index, sb);
        }
        if (rValue != null) {
            index = rValue.visit(pickUpLabel, index, sb);
        }
        if (rExpression != null) {
            index = rExpression.visit(pickUpLabel, index, sb);
        }

        sb.append(String.format("%s -> %s;\n", rootNode, pickUpLabel));

        return index;

    }
}
