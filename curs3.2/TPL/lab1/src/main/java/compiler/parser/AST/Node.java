package compiler.parser.AST;

public interface Node {
    int visit(String rootNode, int index, StringBuilder sb);
}
