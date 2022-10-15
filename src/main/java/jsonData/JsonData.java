package jsonData;

import com.google.gson.annotations.SerializedName;

public class JsonData {
    @SerializedName("title")
    public String title;
    @SerializedName("date")
    public String date;
    @SerializedName("sum")
    public Integer sum;
}