package nofunny;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket;
        boolean flag = true;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            socket = new DatagramSocket();
            do {
                //Ввод сообщени серверу
                System.out.println("Enter message to server: ");
                String message = in.readLine();
                byte[] b = message.getBytes();

                //Отправка сообщения
                DatagramPacket dataPacket = new DatagramPacket(b, b.length, InetAddress.getByName("localhost"), 8093);
                socket.send(dataPacket);

                //Буффер для получения входящих данных
                byte[] buffer = new byte[65536];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);


                //Получение данных
                socket.receive(reply);
                byte[] data = reply.getData();
                message = new String(data, 0, reply.getLength());
                System.out.println("Server: " + reply.getAddress().getHostAddress() + ", Port: " + reply.getPort() + ", Get: " + message);
                if(message.equals("exit"))
                        flag = false;
            }while (flag);
        }catch (IOException e) {
            System.err.println(e);
        }
    }
}
