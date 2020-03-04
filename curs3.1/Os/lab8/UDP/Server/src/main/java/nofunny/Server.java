package nofunny;

//import com.sun.org.apache.xpath.internal.operations.String;

import java.io.*;
import java.net.*;

import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;

public class Server {
    public static void main(String[] args) {
        boolean flag = true;
        try {
            //Создание сокета
            DatagramSocket socket = new DatagramSocket(8093);

            //Буффер для получения сообщений
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            System.out.println("Waiting data...");

            do {
                socket.receive(incoming);
                byte[] data = incoming.getData();
                String message = new String(data, 0, incoming.getLength());
                System.out.println("Server getting: " + message);

                //Отправление сообщени клиенту
                DatagramPacket dp = new DatagramPacket(message.getBytes(), message.getBytes().length, incoming.getAddress(), incoming.getPort());
                socket.send(dp);
                if(message.equals("exit"))
                    flag = false;
            }while(flag);
        }catch (IOException e) {
            System.err.println(e);
        }
    }
}
