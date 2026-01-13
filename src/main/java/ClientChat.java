import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientChat {

    //endereço do servidor
    private static final String HOST = "localhost";

    //porta do serivdor
    private static final int PORT = 12345;

    public static void main(String[] args) {

        //tenta conectar o socket, que representa a conexão com o servidor
        try(Socket socket = new Socket(HOST,PORT)){
            System.out.println("Conectado ao servidor!");

            //entrada de dados vindos do servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // saida de dados para o servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //thread para ler mensagem do servidor em paralelo
            Thread readerThread = new Thread(() -> {
                String response;
                try {
                    //enquanto o server mandar mensagem printa na tela
                    while((response = in.readLine()) !=  null){
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada pelo servidor.");
                }
            });

            //se a thread main finalizar, essa tambem
            readerThread.setDaemon(true);
            readerThread.start();

            //le o input do usuario
            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite suas mensagem, digite /sair para sair.");

            while(true){
                String msg = scanner.nextLine();

                if (msg.equalsIgnoreCase("/sair")){
                    System.out.println("Encerrando a conexão...");
                    break;
                }
                //envia a mensagem ao servidor
                out.println(msg);
            }


        } catch (IOException e) {
            System.out.println("Erro no cliente: " + e.getMessage());
        }
        System.out.println("Cliente finalizado.");
    }
}
