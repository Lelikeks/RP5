package com.example.lelik.rp5;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by lelik on 02.06.2017.
 */

class Place {
    private static final Place defaultPlace = new Place("https://rp5.ru/%D0%9F%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0_%D0%B2_%D0%AF%D1%80%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D0%BB%D0%B5,_%D0%AF%D1%80%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D1%81%D0%BA%D0%B0%D1%8F_%D0%BE%D0%B1%D0%BB%D0%B0%D1%81%D1%82%D1%8C");

    private String url;
    private String name;

    Place(String url) {
        this.url = url;

        try {
            name = URLDecoder.decode(url, "UTF-8").replace("https://rp5.ru/", "").replace('_', ' ');

            int comma = name.indexOf(',');
            if (comma > -1) {
                name = name.substring(0, comma);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static Place getDefaultPlace() {
        return defaultPlace;
    }

    @Override
    public String toString() {
        return name;
    }

    String getUrl() {
        return url;
    }

    boolean isDefault() {
        return defaultPlace.url.equals(url);
    }
}
