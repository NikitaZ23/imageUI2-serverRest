package com.example.serverrest.common;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParseJson {

    List<String> list;

    public ParseJson(String text) {

        JsonObject jsonObject = new JsonParser().parse(text).getAsJsonObject();

        JsonObject jsonObject2 = jsonObject.getAsJsonObject("result");

        JsonArray tags = jsonObject2.getAsJsonArray("tags");

        list = new ArrayList<>();

        for (int i = 0; i < tags.size(); i++) {
            JsonObject jsonObject3 = tags.get(i).getAsJsonObject();
            JsonObject jsonObject4 = jsonObject3.get("tag").getAsJsonObject();

            list.add(jsonObject4.get("en").toString().replace("\"", ""));
        }
    }
}
