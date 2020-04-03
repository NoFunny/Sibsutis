package compiler.parser.AST.value;

import compiler.parser.AST.ArrayMember.ArrayMember;

public class CallArrayMember extends GenericValue{
    // value is id
    private ArrayMember arrayMember;

    public void setArrayMember(ArrayMember arrayMember) {
        this.arrayMember = arrayMember;
    }

    public ArrayMember getArrayMember() {
        return arrayMember;
    }

    @Override
    public int visit(String rootNode, int index, StringBuilder sb) {
        String nameNode = "CALL_AR_MEM";
        String lableNameNode = String.format("\"%s%d\"",
                nameNode,
                index++);

        sb.append(String.format("%s [label=\"%s\\nid=%s\"];\n",
                lableNameNode,
                nameNode,
                value.getValue()));

        index = arrayMember.visit(lableNameNode, index, sb);

        sb.append(String.format("%s -> %s;\n", rootNode, lableNameNode));

        return index;
    }

}
