import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat {
    //porta do servidor
    private static final int PORT = 12345;

    //cada cliente possui um, o servidor percorre a lista e escreve em cada um
    private static List<PrintWriter> writers = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(String.format("Servidor iniciado na porta: %s", PORT));

        //ouvido do servidor que fica escutando a porta
        try(ServerSocket serverSocket = new ServerSocket(PORT)){

            //servidor fica sempre ouvindo novos clientes
            while(true){

                //quando um cliente se conecta retorna um socket
                Socket clientSocket = serverSocket.accept();

                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());

                //cria um handler para o cliente passando o socket e a lista writers pra broadcast
                ClientHandler handler = new ClientHandler(clientSocket, writers);

                //executa o handler em paralelo ao loop principal
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(String.format("Erro no servidor: %s", e.getMessage()));
        }
    }
}
