package compiler;

import compiler.lexer.Token;
import compiler.lexer.Tokenizer;

import java.io.*;
import java.util.LinkedList;

public class lex implements Runnable {
    private static StringBuilder tokens = new StringBuilder();
    private static LinkedList<Token> tokenss = new LinkedList<>();
    private static Tokenizer tokenizer = null;
    String input = readFile("/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/alg.java");
    String outputDir = "/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/out.txt";
    static parse Parser;

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

    private static void writeTokensToFile(String outputDir) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(outputDir));
            out.write(tokenss.toString());
            out.close();
        } catch (IOException e) {
            System.out.println("An error was encountered during writing to the file: " + outputDir);
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void run() {
        tokenizer = new Tokenizer(input);
        System.out.println(tokenizer.getTokenss());
        writeTokensToFile(outputDir);
        Parser = new parse(tokenizer);
        Thread threadParse = new Thread(Parser);
        threadParse.start();
    }
}
