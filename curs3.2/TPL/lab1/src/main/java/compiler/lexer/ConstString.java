package compiler.lexer;

import net.sf.oval.constraint.NotNull;


public class ConstString extends Container {

    private String token;
    private String expr;

    public ConstString(@NotNull String token, @NotNull String expr) {
        this.token = token;
        this.expr = expr;
    }


    @Override
    public boolean isIn(String str) {
        return expr.equals(str);
    }


    @Override
    public String getToken() {
        return token;
    }
}
