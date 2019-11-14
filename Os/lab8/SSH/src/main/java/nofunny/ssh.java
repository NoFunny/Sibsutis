package nofunny;

import com.jcraft.jsch.*;
import javax.swing.*;

public class ssh {
    public static void main(String[] args) {
        try {
            JSch jsch = new JSch();
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
            session.setPassword(pass);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();
            System.out.println("Connection created");
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
