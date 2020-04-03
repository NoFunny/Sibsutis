package compiler.lexer;

public class Keywords extends Container {
    static final String[] keywords = {
            "abstract", "boolean", "break", "case", "catch", "const", "continue", "default",
            "double", "extends", "final", "finally", "implements", "import", "instanceof",
            "interface", "native", "package", "private", "protected",
            "super", "switch", "this", "throw", "throws", "try"};

    static final String token = "KEYWORD";

    @Override
    public boolean isIn(String input) {
        for (String str : keywords) {
            if (input != null && input.equals(str))
                return true;
        }
        return false;
    }

    @Override
    public String getToken() {
        return token;
    }
}
