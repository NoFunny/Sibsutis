package lexer;

public class Keywords extends Container {
    static final String[] keywords = {
            "abstract", "boolean", "break", "case", "catch", "class", "const", "continue", "default",
            "double", "extends", "final", "finally", "implements", "println", "System", "out",
            "import", "instanceof", "interface", "native", "new", "package", "private", "protected",
            "public", "return", "static", "super", "switch", "this", "throw", "throws",
            "try"};

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
