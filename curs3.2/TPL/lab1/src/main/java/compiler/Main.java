package compiler;

import compiler.identifierTable.SemanticExceptioin.SemanticException;
import compiler.identifierTable.Table;
import compiler.lexer.LexerList;
import compiler.lexer.Token;
import compiler.lexer.Tokenizer;
import compiler.parser.AST.NodeClass;
import compiler.parser.Parser;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static StringBuilder tokens = new StringBuilder();
    private static LinkedList<Token> tokenss = new LinkedList<>();
    private static Tokenizer tokenizer = null;

    static lex Lexer;
    static parse Parser;

    public static void main(String[] args) throws IOException {
        String input = readFile("/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/alg.java");
        String outputDir = "/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/out.txt";
        boolean flag = true;

        while (flag) {
            System.out.println("\n1 - <lexical analysis>" + '\n' + "2 - <parse>" + '\n' + "3 - <compile>" + '\n' + "4 - <exit>");
            System.out.println("Choose part:");

            Scanner in = new Scanner(System.in);
            int s = Integer.parseInt(in.nextLine());
            switch (s) {
                case 1:
                    tokenizer = new Tokenizer(input);
                    System.out.println(tokenizer.getTokenss());
                    writeTokensToFile(outputDir);
                    break;
                case 2:
                    Parser parser = new Parser(new LexerList(tokenizer));
                    NodeClass root = parser.go();
//                    parser.showTree();
                    parser.printTreeToFile();
                    Table identifierTable = new Table();
                    try {
                        identifierTable.go(root);
                    } catch (SemanticException e) {
                        System.out.println(String.format("\nERROR [semantic]: %s\n",
                                e.getMessage()));
                        return;
                    }
                    identifierTable.printTable();
                    break;
                case 3:
                    Lexer = new lex();
                    Thread threadLex = new Thread(Lexer);
                    threadLex.start();
                    System.out.println("Потоки закончили свою работу!");
                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    flag = false;
                    System.out.println("Error command!");
                    break;
            }
        }
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
}
