package nofunny;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private static Socket clientSocket; // Сокет для обращения к клиенту
    private static BufferedReader reader; // Ридер для чтения с клавиатуры
    private static BufferedReader in; // Поток чтения из сокета
    private static BufferedWriter out; // Поток записи в сокет
    private static boolean flag = true;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 8093); // Запрос у сервера на соединение
                ExecutorService service = Executors.newFixedThreadPool(3);
                service.submit(() -> {});
                do {
                    reader = new BufferedReader(new InputStreamReader(System.in));
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// Чтение сообщений с сервера
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));// Писать сообщение на сервер
                    // Общение с сервером если соединение успешно,иначе выкидываем исключение
                    System.out.println("Enter message...");
                    String Message = reader.readLine(); // Ввод сообщения
                    out.write(Message + "\n"); // Сервер ждёт символа конца строки чтобы обработать сообщение,отправка сообщения на сервер
                    out.flush();
                    String serverMessage = in.readLine(); // Ответ сервера
                    System.out.println(serverMessage);
                    if(Message.equals("exit"))
                        flag = false;
                }while(flag);
                } finally { // В любом случае нужно закрытть сокет и потоки
                System.out.println("Client has been closed...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
