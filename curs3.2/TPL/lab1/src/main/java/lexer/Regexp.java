package lexer;


import java.util.regex.Pattern;

public class Regexp extends Container {

    private String token;
    private Pattern regexp;

    public Regexp(String token, String regexp) {
        this.token = token;
        this.regexp = Pattern.compile(regexp);
    }


    @Override
    public boolean isIn(String str) {
        return regexp.matcher(str).matches();
    }


    @Override
    public String getToken() {
        return token;
    }
}
