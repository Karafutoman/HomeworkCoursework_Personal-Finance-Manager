
import com.google.gson.Gson;
import jsonData.JsonData;
import product.Tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            System.out.println("Запуск сервера");

            Tracker tracker = new Tracker();
            while (true) {
                System.out.println("Сервер ожидает подключение от клиента");
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {

                    String inString = in.readLine();

                    Gson gson = new Gson();
                    JsonData jsonProductData = gson.fromJson(inString, JsonData.class);
                    tracker.addNewProduct(jsonProductData);

                    // Подготовка json для клиента, ответ на запрос
                    String outJsonData = tracker.getMaxSumCategory();

                    System.out.println("Сформированный json для клиента: ");
                    System.out.println(outJsonData);

                    out.println(outJsonData);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: запуск сервера не возможен");
            e.printStackTrace();
        }
    }
}