package product;

import jsonData.JsonData;

import java.util.*;

public class Category {
    private Set<String> products = new HashSet<String>();
    private String categoryName;
    private int sum;

    private List addedData = new ArrayList<JsonData>();

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public boolean CheckProductExists(String productName){
        return products.contains(productName);
    }

    public void addProduct(String productName){
        products.add(productName);
    }

    public boolean trackerSum(String productName, int cost, String date){
        if(CheckProductExists(productName)){
            sum += cost;

            JsonData jsonData = new JsonData();
            jsonData.title = productName;
            jsonData.sum = sum;
            jsonData.date = date;

            addedData.add(jsonData);
            return true;
        }

        return false;
    }

    public boolean trackerSum(JsonData jsonData){
        if(CheckProductExists(jsonData.title)){
            sum += jsonData.sum;

            addedData.add(jsonData);
            return true;
        }

        return false;
    }

    public Set<String> getProducts() {
        return products;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getSum() {
        return sum;
    }
}
