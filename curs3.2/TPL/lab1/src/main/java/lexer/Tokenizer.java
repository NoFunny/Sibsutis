package lexer;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private String stack;
    private LinkedList<String> tokens;


    public Tokenizer(String input) {
        stack = "";
        tokens = new LinkedList<>();
        toTokens(processComments(input));
    }


    public static String processComments(String input) {

        System.out.println(input);
        String commentRegex = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";
        String openBlockComment = "(\\/\\*)";
        String closeBlockComment = "(\\*\\/)";
        Pattern pattern = Pattern.compile(commentRegex);
        Matcher mat = pattern.matcher(input);
        Pattern pattern1 = Pattern.compile(openBlockComment);
        Pattern pattern2 = Pattern.compile(closeBlockComment);
        Matcher matcher = pattern1.matcher(input);
        Matcher matcher1 = pattern2.matcher(input);
        if(matcher.find()) {
            if(!matcher1.find()) {
            System.out.println("ERROR: BLOCK COMMENT DON'T CLOSE");
//            System.out.println(String.format("loc <%d> : BLOCK COMMENT DON'T CLOSE! ", matcher.start()));
            return null;
            }
        }
        while (mat.find())
            System.out.println("COMMENT : " + mat.group());

        return input.replaceAll(commentRegex, "");
    }

    public String getNextToken() {
        if (tokens.size() > 0) {
            return tokens.remove(0);
        } else
            return null;
    }


    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    private void toTokens(String input) {
        String tmp;
        int lineCount = 1;
        int index = 1;

        for (Character c : input.toCharArray()) {
            if((c == '\n' && stack.equals("{")) || (c == '\n' && stack.equals(";")) || (c == '\n' && stack.equals("}"))) {
                flushStack(lineCount, index);
                index = 1;
            }
            if (stack.length() != 0 && (stack.charAt(0) == '\"' || stack.charAt(0) == '\'')) {
                if(c == '\n' && (stack.charAt(stack.length() - 1) != '\"' || stack.charAt(stack.length() - 1) != '\'')) {
                    flushStack(lineCount,index);
                }
                if(c == '\n') {
                    lineCount++;
                    index = 1;
                }else {
                    String littmp = parseString(stack + c);
                    if (littmp == null) {
                        index++;
                        stack += c;
                        continue;
                    } else {
                        tokens.add(littmp + " " + stack + c + "\t");
                        index++;
                        System.out.println(String.format("loc <%d:%d>: '%s' %s",lineCount,index, stack + c, littmp));
                        stack = "";
                        continue;
                    }
                }
            }
            if (Character.isWhitespace(c)) {
                flushStack(lineCount, index);
            }else {
                if (((stack.length() == 0 && Character.isDigit(c))) || (stack.length() != 0 && Character.isDigit(stack.charAt(0)))) {
                    stack += c;
                    index++;

                    for (int i = 0; i < stack.length(); i++) {
                        String numtmp;
                        String substring = stack.substring(i);

                        tmp = parseSyntax(stack.substring(i));


                        if (substring.contains("h") || substring.contains("x") ||
                                substring.contains("f") || substring.contains("."))
                            continue;

                        if (tmp != null) {


                            numtmp = parseNumeric(stack.substring(0, i));
                            if (numtmp != null) {

                                System.out.println(String.format("loc = <%d:%d> '%s' (%s)",lineCount,index-1, stack.substring(0, i), numtmp));
                                tokens.add(numtmp + " " + stack.substring(0, i) + "\t");

                                stack = stack.substring(i);
                            }

                            flushStack(lineCount, index);
                        }
                    }

                    continue;
                }

                tmp = parseSyntax(stack + c);


                if (tmp == null) {
                    flushStack(lineCount, index);
                    assert stack.length() == 0;
                    stack = c + "";
                }else
                    stack += c;
                index++;
            }
            if(c == '\n') {
                index = 1;
                lineCount++;
            }
            if(c == ' ') {
                index++;
            }
        }

        if (stack != null && stack.length() > 0)
            flushStack(lineCount, index);
        tokens.add(EOF);
        System.out.println(String.format("loc = <%d:%d> %s", lineCount, index, EOF));
    }


    private void flushStack(int lineCount, int index) {
        if (stack != null && stack.length() > 0) {
            String tmp = parseSyntax(stack);
            if (tmp == null)
                tmp = parseNumeric(stack);
            if(tmp == null) {
                tmp = "UNKNOWN";
                System.out.print("\033[0;31m");
            }
            tokens.add(tmp + " " + stack + "\t");
            System.out.println(String.format("loc = <%d:%d> '%s' (%s)", lineCount, index, stack, tmp) + "\u001B[0m");
            stack = "";
        }
    }

    public String parseSyntax(String input) {
        for (Container c : SYNTAX_PATTERNS) {
            if (c.isIn(input)) {
                return c.getToken();
            }
        }
        return null;
    }

    public String parseNumeric(String input) {
        for (Container c : NUMERIC_PATTERNS) {
            if (c.isIn(input)) {
                return c.getToken();
            }
        }
        return null;
    }

    public String parseString(String input) {
        for (Container c : STRING_PATTERNS) {
            if (c.isIn(input)) {
                return c.getToken();
            }
        }
        return null;
    }

    private final static String EOF = "EOF";

    private final static Container[] NUMERIC_PATTERNS = {
            new Regexp("HEX", "(0x[0-9A-F]+)|([0-9A-F]+h)"),
            new Regexp("INTEGER", "([1-9]+[0-9]*)|(0)"),
            new Regexp("OCTAL", "0[0-7]+"),
            new Regexp("FLOAT", "([0-9]*\\.[0-9]+)"),
    };


    private final static Container[] STRING_PATTERNS = {
            new Regexp("CHAR", "'[a-zA-Z]'"),
            new Regexp("STRING", "\"[^\"]*\"")
    };


    private final static Container[] SYNTAX_PATTERNS = {
            new Keywords(),
            new ConstString("OPEN_CURL_BRACKET", "{"),
            new ConstString("type_int", "int"),
            new ConstString("type_String", "String"),
            new ConstString("type_Void", "void"),
            new ConstString("type_char", "char"),
            new ConstString("CLOSE_CURL_BRACKET", "}"),
            new ConstString("OPEN_BRACKET", "("),
            new ConstString("CLOSE_BRACKET", ")"),
            new Regexp("BOOLEAN_LITERAL", "(TRUE|true|True|FALSE|false|False)"),
            new Regexp("UNARPLUS", "(\\+\\+)"),
            new Regexp("UNARSUB", "(--)"),
            new Regexp("BINARPLUS", "(\\+)"),
            new Regexp("BINARSUB", "(-)"),
            new Regexp("MUL", "(\\*)"),
            new Regexp("DIV", "(/)"),
            new Regexp("MOD", "(%)"),
            new Regexp("OPERATOR", "(>>|<<)"),
            new Regexp("GREATER", "(>).*"),
            new Regexp("LESS", "(<).*"),
            new Regexp("STRUCTURE_REFERENCE", "[a-zA-Z_][a-zA-Z0-9_]*\\("),
            new Regexp("EqEQUAL", "(==)"),
            new Regexp("EQUAL", "(=)"),
            new Regexp("GreatOrEq", "(<=)"),
            new Regexp("LessOrEq", "(>=)"),
            new Regexp("NotEqual", "(!=)"),
            new ConstString("IF_OPERATOR", "if"),
            new ConstString("WHILE_OPERATOR", "while"),
            new ConstString("ELSE_OPERATOR", "else"),
            new Regexp("LOGIC_AND", "[&,\\|]"),
            new Regexp("LOGIC_OR", "[\\|]"),
            new Regexp("QUICK_OPERATOR", "(\\+=|-=|\\*=|/=)"),
            new ConstString("COMMA", ","),
            new ConstString("OPEN_SQUARE_BRACKET", "["),
            new ConstString("CLOSE_SQUARE_BRACKET", "]"),
            new ConstString("SEMICOLON", ";"),
            new ConstString("COLON", ":"),
            new ConstString("DOT", "."),
            new Regexp("NULL_LITERAL", "(Null|NULL|null)"),
            new Regexp("ID", "^([a-zA-Z_$])([a-zA-Z_$0-9])*$"),
    };
}
