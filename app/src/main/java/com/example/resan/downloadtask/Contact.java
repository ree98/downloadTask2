package com.example.resan.downloadtask;

import org.json.JSONException;
import org.json.JSONObject;

public class Contact {
    public String mName;
    public String mStock;
    public String mPrice;
    public String image;

    public Contact(String mName, String mStock, String mPrice, String image){
        this.mName = mName;
        this.mStock = mStock;
        this.mPrice = mPrice;
        this.image = image;
    }

    public String getName(){
        return mName;
    }

    public String getStock(){
        return mStock;
    }
    public String getPrice(){
        return mPrice;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Contact fromJson(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString("name");
        String stock = jsonObject.getString("stock");
        String price = jsonObject.getString("price");
        String image = jsonObject.getString("img");

        return new Contact(name, stock, price, image);
    }
}
