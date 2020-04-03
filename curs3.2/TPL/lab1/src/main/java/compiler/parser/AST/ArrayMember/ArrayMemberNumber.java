package compiler.parser.AST.ArrayMember;

import compiler.parser.AST.value.Number;

public class ArrayMemberNumber extends ArrayMember{
    private Number numberMember;

    public ArrayMemberNumber(Number parseNumber) {
        this.numberMember = parseNumber;
    }

    public Number getNumberMember() {
        return numberMember;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "AR_MEM_NUM";
        String labelNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\"];\n",
                labelNameNode,
                nameNode));

        index = numberMember.visit(labelNameNode, index, sb);

        sb.append(String.format("%s -> %s;\n",
                rootNode,
                labelNameNode));

        return index;
    }
}
