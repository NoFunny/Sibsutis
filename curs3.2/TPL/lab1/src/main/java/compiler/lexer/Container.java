package compiler.lexer;

public abstract class Container {

    public abstract boolean isIn(String str);
    public abstract String getToken();
}
