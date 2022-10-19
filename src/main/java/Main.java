import com.google.gson.Gson;
import jsonData.JsonData;
import product.Tracker;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            System.out.println("Сервер стартовал");

            Gson gson = new Gson();
            File savedData = new File("data.bin");
            Tracker tracker = new Tracker();

            if (!savedData.createNewFile()) {
                tracker = Tracker.loadFromBinFile();
            }

            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
                ) {
                    String input = in.readLine();
                    JsonData clientRequest = gson.fromJson(input, JsonData.class);
                    tracker.addNewProduct(clientRequest);

                    String serverResponse = tracker.getMaxSumCategory();

                    System.out.println("Максимальные траты клиента: ");
                    System.out.println(serverResponse);
                    out.println(serverResponse);
                    tracker.saveBin();
                } catch (Exception e) {
                    throw new RuntimeException("Не могу сохранить статистику :(", e);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер :(");
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException("Не могу загрузить статистику :(", e);
        }
    }
}
