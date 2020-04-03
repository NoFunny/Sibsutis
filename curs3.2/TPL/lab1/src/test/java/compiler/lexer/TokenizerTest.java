package compiler.lexer;

import junit.framework.TestCase;

import org.junit.Assert;

public class TokenizerTest extends TestCase {

    public void testProcessComments() {
        String input = new String(
                "//Это комментарий\n" +
                        "\n" +
                        "/*Это комментарий*/\n" +
                        "\n" +
                        "publics class Main {");
        String input_nd = new String("//Это комментарий\n" +
                "\n" +
                "/*Это комментарий\n" +
                "\n" +
                "publics class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = 54.3;\n" +
                "\n" +
                "}");

        Assert.assertEquals("\n" +
                "\n" +
                "\n" +
                "\n" +
                "publics class Main {", Tokenizer.processComments(input));
        Assert.assertEquals(null, Tokenizer.processComments(input_nd));
    }

    public void testGetNextToken() {
        Tokenizer token = new Tokenizer("public static void main(String[] \"args) {");
        Token tokenn = new Token("program", "program", new Location(0,0));
        Assert.assertEquals(tokenn.toString(), token.getNextToken().toString());
        Token tokenn1 = new Token("PUBLIC", "public", new Location(1,7));
        Assert.assertEquals(tokenn1.toString(), token.getNextToken().toString());
        Token tokenn2 = new Token("STATIC", "static", new Location(1,14));
        Assert.assertEquals(tokenn2.toString(), token.getNextToken().toString());
        Token tokenn3 = new Token("VOID", "void", new Location(1,19));
        Assert.assertEquals(tokenn3.toString(), token.getNextToken().toString());
        Token tokenn4 = new Token("ID", "main", new Location(1,24));
        Assert.assertEquals(tokenn4.toString(), token.getNextToken().toString());
        Token tokenn5 = new Token("OPEN_BRACKET", "(", new Location(1,25));
        Assert.assertEquals(tokenn5.toString(), token.getNextToken().toString());
        Token tokenn6 = new Token("ID", "String", new Location(1,31));
        Assert.assertEquals(tokenn6.toString(), token.getNextToken().toString());
        Token tokenn7 = new Token("OPEN_SQUARE_BRACKET", "[", new Location(1,32));
        Assert.assertEquals(tokenn7.toString(), token.getNextToken().toString());
        Token tokenn8 = new Token("CLOSE_SQUARE_BRACKET", "]", new Location(1,33));
        Assert.assertEquals(tokenn8.toString(), token.getNextToken().toString());
        Token tokenn9 = new Token("UNKNOWN", "\"args) {", new Location(1,42));
        Assert.assertEquals(tokenn9.toString(), token.getNextToken().toString());
    }
}