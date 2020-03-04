package lexer;

import java.io.*;

public class Main {
    private static StringBuilder tokens = new StringBuilder();

    public static void main(String[] args) {
        String input = readFile("/home/nofunny/Рабочий стол/study/curs3.2/TPL/lab1/src/main/resources/alg.java");
        Tokenizer tokenizer = new Tokenizer(input);
        try {
            while (!tokenizer.isEmpty()) {
                tokens.append(tokenizer.getNextToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
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

//    private static void writeTokensToFile(String outputDir) {
//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter(outputDir));
//            out.write(tokens.toString());
//            out.close();
//        } catch (IOException e) {
//            System.out.println("An error was encountered during writing to the file: " + outputDir);
//            System.out.println(e.getStackTrace());
//        }
//
//    }
}
