package compiler.parser;

import compiler.lexer.LexerList;
import compiler.lexer.Tokenizer;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParserTest extends TestCase {


    public void testGo() {
        String input = readFile("/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/syntaxTest.java");
        Tokenizer lexer = new Tokenizer(input);
        Parser parser = new Parser(new LexerList(lexer));
        Assert.assertEquals(null,parser.go());

        String input1 = readFile("/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/grammarTest.java");
        Tokenizer lexer1 = new Tokenizer(input1);
        Parser parser1 = new Parser(new LexerList(lexer1));
        Assert.assertEquals(null, parser1.go());

        String input2 = readFile("/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/funcTest.java");
        Tokenizer lexer2 = new Tokenizer(input2);
        Parser parser2 = new Parser(new LexerList(lexer2));
        Assert.assertEquals("loc<23:16> ID 'min'", parser2.go().getMainMethod().getStatementList().get(7).toString());

    }

    public static String readFile(String fileDir) {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileDir))) {
            String result = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                result = result.concat(line) + "\n";
            }
            return result;
        } catch (IOException e) {
            System.out.println("Error was encountered during loading of the file: " + fileDir);
            e.printStackTrace();
        }
        return null;
    }
}