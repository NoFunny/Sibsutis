package nofunny;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Socket clientSocket; // Сокет для общения
    private static ServerSocket server; // Серверсокет
    private static BufferedReader in;   // Поток чтения из сокета
    private static BufferedWriter out;  // Поток записи в сокет
    private static boolean flag = true;
    public static void main(String[] args) {
        try {
            try {
                server = new ServerSocket(8093); // Сервер сокет проверяет порт 8093
                System.out.println("Server has been run!"); //
                clientSocket = server.accept(); //Ожидание подключения к серверу
                try { // Работа сервера с клиентом через сокет, создание потоков ввода\вывода
                    do {
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Принятие сообщения
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); // Отправка сообщения

                        String Message = in.readLine(); // Ожидание сообщения от клиента
                        System.out.println(Message); //Ответ сервера

                        out.write("Hello this is Server! You write: " + Message + "\n");
                        out.flush(); // Очистка буффера
                        if(Message.equals("exit"))
                            flag = false;
                        System.out.println("Flaf = " +flag);
                    }while(flag);
                } finally { // В любом случае сервер будет закрыт
                    System.out.println("...");
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } finally {
                System.out.println("Server has been closed...");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
