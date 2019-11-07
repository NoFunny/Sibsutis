package nofunny;

import com.jcraft.jsch.*;
import javax.swing.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ssh {
    public static void main(String[] args) {
        try {
            JSch jsch = new JSch();
            String remoteFile = "/home/nofunny/Рабочий стол/study/README.md";
            String login = null;
            if (args.length > 0) {
                login = args[0];
            } else {
                login = JOptionPane.showInputDialog("Enter username:password@hostname", System.getProperty("user.name") + ":<pass>" + "@localhost");
            }
            String user = login.substring(0, login.indexOf(':'));
            String pass = login.substring(login.indexOf(':') + 1, login.indexOf('@'));
            String host = login.substring(login.indexOf('@') + 1);
            Session session = jsch.getSession(user, host, 22);
//            session.setHostKeyAlias("127.0.0.1 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC5pSlW02SJYtAn/P3FXf1hX70CX4UBro5s0Pc82vmj2BjaBvxXk12Gz8+5/mc7pEkaCXGABRvOy3WsezWY1QqtTWKh1bE6HfXB8cxUlpRN8yss/vHLnq6y75/83eIBxNtQgWyfLuH5IVk4OO6S25n8JRlnJqvTArSqpFlDUdlReMnkc5hFEpa+2s0sWAXZvi29q3SwRg0lEOE4K5t+m1SklK1nVM3MN7Vx/eMsCPcfW7wwpJizr4fX9FZM54c2z6KTOGzBS2Eycr3TPCzseTKc96mFTT6FU/8px8+tAj/p5R23b8t9gu5weCEamhQTS2acd/H9uCALICqDzxxrTdlh\n");
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();
            System.out.println("Connection created");
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
//            channel.connect();
//            System.out.println("Channel Created");
//            InputStream stream = channel.get(remoteFile);
//            try (Scanner scanner = new Scanner(new InputStreamReader(stream))) {
//                while (!channel.isClosed()) {
//
//                    String line = scanner.nextLine();
//                    System.out.println(line);
//                }
//        } catch (JSchException | SftpException e) {
//            e.printStackTrace();
//            }
        } catch (JSchException e) {
            e.printStackTrace();
//        } catch (SftpException e) {
//            e.printStackTrace();
        }
    }
}
