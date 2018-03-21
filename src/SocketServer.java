import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by NicholasNie on 2018/3/20.
 */
public class SocketServer {

    private ServerSocket skServer;
    private ArrayList alClients;

    public static void main(String[] args){
        new SocketServer().receive();
    }

    public void receive() {
        alClients = new ArrayList();
        try {
            skServer = new ServerSocket(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                Socket clientSocket = skServer.accept();
                PrintWriter pwReceive = new PrintWriter(clientSocket.getOutputStream());
                alClients.add(pwReceive);

                Thread serverThread = new Thread(new ServerReceiver(clientSocket));
                serverThread.start();
                System.out.println("Socket Connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToClient(String message){
        Iterator iterator = alClients.iterator();
        while (iterator.hasNext()){
            PrintWriter pwServer = (PrintWriter)iterator.next();
            pwServer.println(message);
            pwServer.flush();
        }
    }

    public class ServerReceiver implements Runnable{
        BufferedReader brReader;
        Socket skReader;

        public ServerReceiver(Socket socket){
            skReader = socket;
            try {
                InputStreamReader isrReader = new InputStreamReader(skReader.getInputStream());
                brReader = new BufferedReader(isrReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while (null != (message = brReader.readLine())){
                    System.out.print(message);
                    sendToClient(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
