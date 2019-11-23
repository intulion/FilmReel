package com.akat.filmreel.data.local;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class IntListConverter {
    private static final String SEPARATOR = ",";

    @TypeConverter
    public static List<Integer> toList(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        List<Integer> list = new ArrayList<>();
        for (String s : data.split(SEPARATOR)) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    @TypeConverter
    public static String fromList(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));

            if (i < list.size() - 1) {
                builder.append(SEPARATOR);
            }
        }
        return builder.toString();
    }
}