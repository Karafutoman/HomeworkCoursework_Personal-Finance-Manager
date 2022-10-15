import com.google.gson.Gson;
import jsonData.JsonData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.Tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductTrackerTests {
    public static Tracker tracker;

    @BeforeEach
    public void InitBeforeTest(){
        tracker = new Tracker();
    }

    @Test
    public void testgetCategoryHighSum(){
        String currentDate = getCurrentDate();

        Gson gson = new Gson();
        JsonData jsonData = gson.fromJson("{\"title\": \"шапка\", \"date\": \"" + currentDate + "\", \"sum\": 100}", JsonData.class);
        tracker.addNewProduct(jsonData);

        jsonData = gson.fromJson("{\"title\": \"шапка\", \"date\": \"" + currentDate + "\", \"sum\": 200}", JsonData.class);
        tracker.addNewProduct(jsonData);

        jsonData = gson.fromJson("{\"title\": \"булка\", \"date\": \"" + currentDate + "\", \"sum\": 50}", JsonData.class);
        tracker.addNewProduct(jsonData);

        Assertions.assertEquals(300, tracker.getCategoryHighSum().getSum());
    }

    @Test
    public void testGetJsonSumForCategoryByProductName(){
        String currentDate = getCurrentDate();

        Gson gson = new Gson();
        JsonData jsonData = gson.fromJson("{\"title\": \"шапка\", \"date\": \"" + currentDate + "\", \"sum\": 100}", JsonData.class);
        tracker.addNewProduct(jsonData);

        jsonData = gson.fromJson("{\"title\": \"шапка\", \"date\": \"" + currentDate + "\", \"sum\": 200}", JsonData.class);
        tracker.addNewProduct(jsonData);

        jsonData = gson.fromJson("{\"title\": \"булка\", \"date\": \"" + currentDate + "\", \"sum\": 50}", JsonData.class);
        tracker.addNewProduct(jsonData);

        Assertions.assertEquals("{" +
                "  \"maxCategory\": {" +
                "    \"category\": \"одежда\"," +
                "    \"sum\": \"300\"" +
                "  }" +
                "}", tracker.getMaxSumCategory());
    }

    private static String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
