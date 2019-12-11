package com.akat.filmreel.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NetworkUtils {

    public static Gson getGson() {
        JsonDeserializer<Date> deserializer = new JsonDeserializer<Date>() {
            SimpleDateFormat format = new SimpleDateFormat(Constants.HTTP.DATE_FORMAT, Locale.getDefault());

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == null || json.getAsString().isEmpty()) {
                    return null;
                }

                try {
                    return format.parse(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        };

        return new GsonBuilder()
                .registerTypeAdapter(Date.class, deserializer)
                .create();
    }
}
