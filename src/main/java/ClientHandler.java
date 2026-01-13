import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable{

    //conexão expecifica com o cliente
    private Socket socket;

    //referencia da lista de saida dee todos clientes
    private List<PrintWriter> writers;

    //saida deste cliente
    private PrintWriter out;

    //entrada para esse cliente
    private BufferedReader in;

    //recebe o socket e a lista
    public ClientHandler(Socket socket, List<PrintWriter> writers){
        this.socket = socket;
        this.writers = writers;
    }

    @Override
    public void run() {
        try {
            //inputstreamreader permite converter bytes em caracteres do fluxo de dados do cliente
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //facilita escrever strings e sair no printlb, true é autoflush manda na hora
            out = new PrintWriter(socket.getOutputStream(),true);

            //adiciona a saida do cliente na lista global
            synchronized (writers) {
                writers.add(out);
            }
            out.println("Bem-Vindo ao Chat! Digite sua mensagem:");
            String msg;

            //looping para ficar lendo as mensagens
            while((msg = in.readLine()) != null){
                System.out.println("Recebido de " + socket.getInetAddress() + ": " + msg);
                //qnd recebe disapara para todos clientes
                broadcast(msg);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //se cliente cair ou sair desconectamos ele
            try {
                if(out != null){
                    synchronized (writers){
                        writers.remove(out);
                    }
                }
                if (socket != null && !socket.isClosed()){
                    socket.close();
                }
            } catch (IOException e){
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    //envia a mensagem para todos os clientes conectados
    private void broadcast(String mensagem) {
        synchronized (writers) {
            for (PrintWriter writer : writers) {
                writer.println(mensagem);
            }
        }
    }
}
