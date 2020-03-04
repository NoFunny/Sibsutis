package lexer;

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
                "publics class Main {",Tokenizer.processComments(input));
        Assert.assertEquals(null, Tokenizer.processComments(input_nd));
    }

    public void testGetNextToken() {
        Tokenizer token = new Tokenizer("public static void main(String[] \"args) {");
        Assert.assertEquals("KEYWORD public\t", token.getNextToken());
        Assert.assertEquals("KEYWORD static\t", token.getNextToken());
        Assert.assertEquals("type_Void void\t", token.getNextToken());
        Assert.assertEquals("STRUCTURE_REFERENCE main(\t", token.getNextToken());
        Assert.assertEquals("type_String String\t", token.getNextToken());
        Assert.assertEquals("OPEN_SQUARE_BRACKET [\t", token.getNextToken());
        Assert.assertEquals("CLOSE_SQUARE_BRACKET ]\t", token.getNextToken());
        Assert.assertEquals("UNKNOWN \"args) {\t", token.getNextToken());
    }
}