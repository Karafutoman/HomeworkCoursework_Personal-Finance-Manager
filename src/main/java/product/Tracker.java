package product;

import com.google.gson.Gson;
import jsonData.JsonData;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Tracker {
    private Map<String, Category> trackedProducts = new HashMap();

    private Map<String, String> hashProducts = new HashMap();

    private static String template = "булка\tеда\n" +
            "колбаса\tеда\n" +
            "сухарики\tеда\n" +
            "курица\tеда\n" +
            "тапки\tодежда\n" +
            "шапка\tодежда\n" +
            "мыло\tбыт\n" +
            "акции\tфинансы";

    public Tracker() {
        loadCategories();
    }

    private void loadCategories() {
        try {
            File file = new File("categories.tsv");

            if (file.exists() == false) {
                file.createNewFile();

                Files.writeString(Path.of(file.getPath()), template);
                System.out.println("Создан файл с категориями, загрузка данных");
            } else {
                System.out.println("Файл с категориями найден, загрузка данных");
            }

            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                String[] splitedData = data.split("\t");

                if (splitedData.length == 2) {
                    Category currentCategory;
                    if (trackedProducts.containsKey(splitedData[1])) {
                        currentCategory = (Category) trackedProducts.get(splitedData[1]);
                    } else {
                        currentCategory = new Category(splitedData[1]);
                    }
                    currentCategory.addProduct(splitedData[0]);
                    hashProducts.put(splitedData[0], splitedData[1]);
                    trackedProducts.put(splitedData[1], currentCategory);
                }
                System.out.println(data);
            }
            myReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Category getCategoryHighSum() {
        int maxSum = -1;
        Category result = null;

        for (Map.Entry<String, Category> entry : trackedProducts.entrySet()) {
            if (entry.getValue().getSum() > maxSum) {
                maxSum = entry.getValue().getSum();
                result = entry.getValue();
            }
        }

        return result;
    }

    public void addNewProduct(JsonData product) {
        if (hashProducts.containsKey(product.title)) {
            Category prCat = (Category) trackedProducts.get(hashProducts.get(product.title));
            prCat.trackerSum(product);
        } else {
            Category currentCategory;
            if (trackedProducts.containsKey("другое")) {
                currentCategory = (Category) trackedProducts.get("другое");
            } else {
                currentCategory = new Category("другое");
            }
            currentCategory.addProduct(product.title);
            currentCategory.trackerSum(product);
            hashProducts.put(product.title, "другое");
            trackedProducts.put("другое", currentCategory);
        }

        System.out.println("Товар " + product.title + " занесён в категорию " + hashProducts.get(product.title)
                + " на сумму: " + product.sum);
    }

    public String getMaxSumCategory() {
        Category category = getCategoryHighSum();

        return "{" +
                "  \"maxCategory\": {" +
                "    \"category\": \"" + category.getCategoryName() + "\"," +
                "    \"sum\": \"" + category.getSum() + "\"" +
                "  }" +
                "}";

    }
    public void saveBin() throws Exception {
        try (FileWriter fileWriter = new FileWriter("data.bin")) {
            Gson gson = new Gson();
            gson.toJson(this, fileWriter);
        }
    }
    public static Tracker loadFromBinFile() throws Exception {
        try (FileReader fileReader = new FileReader("data.bin")) {
            Gson gson = new Gson();
            return gson.fromJson(fileReader, Tracker.class);
        }
    }
}