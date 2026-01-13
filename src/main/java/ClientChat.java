import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientChat {

    //endereço do servidor
    private static final String HOST = "localhost";

    //porta do serivdor
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try(Socket socket = new Socket(HOST,PORT)){
            ()
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
