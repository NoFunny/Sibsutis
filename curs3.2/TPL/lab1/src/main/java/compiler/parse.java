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

public class parse implements Runnable {
    private  Tokenizer tokenizer;

    public parse(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
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

    @Override
    public void run() {
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
        System.out.println("Парсер закончил!");
        System.out.println("\n1 - <lexical analysis>" + '\n' + "2 - <parse>" + '\n' + "3 - <compile>" + '\n' + "4 - <exit>");
        System.out.println("Choose part:");
    }
}

