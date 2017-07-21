package com.example.lelik.rp5;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Forecast {
    String RefreshDate;
    ForecastData[] Data;
    String YarTemp;
    String YarTempDiff;
}

class ForecastData {
    String Hour;
    String Temp;
    String Clouds;
    ForecastRain Rain;
    ForecastWind Wind;
    String DayOfWeek;
    boolean IsDayLight;
}

class ForecastRain {
    String FirstHalf;
    String FirstHalfNum;
    String SecondHalf;
    String SecondHalfNum;
}

enum WindDirection {
    N, NE, E, SE, S, SW, W, NW
}

class ForecastWind {
    WindDirection Direction;
    String Strength;
    String Gusts;
}

final class ForecastParser {
    private static Pattern rainPattern = Pattern.compile(".*\\((\\d+\\.*\\d*) (м|с)м.*");
    private static Pattern yartempPattern = Pattern.compile(".*<b>(-?\\d+\\.\\d+)</b>.*>(-?\\+?\\d+\\.\\d+)</font>.*");

    static Forecast parseHtml(String html, String yartemp, ForecastPeriod forecastPeriod) {
        Stopwatch sw = Stopwatch.startNew();
        int start = html.indexOf("<table id=\"" + getTableName(forecastPeriod) + "\"");
        int finish = html.indexOf("</table", start);
        Document doc = Jsoup.parseBodyFragment(html.substring(start, finish + 8));

        Log.i("doc", sw.getElapsedAndRestart());
        Element table = doc.select("#" + getTableName(forecastPeriod)).first();
        Log.i("table", sw.getElapsedAndRestart());

        Elements hours = table.select("tr:eq(1) > td:gt(0)");
        Log.i("hours", sw.getElapsedAndRestart());
        Elements clouds = table.select("tr:eq(2) > td:gt(0) > div:eq(0) > div");
        Log.i("clouds", sw.getElapsedAndRestart());
        Elements rains = table.select("tr:eq(3) > td:gt(0) > div:eq(0)");
        Log.i("rains", sw.getElapsedAndRestart());
        String[] temps = getTemps(table);
        Log.i("temps", sw.getElapsedAndRestart());
        String[] windDirs = getWindDirs(table);
        Log.i("windDirs", sw.getElapsedAndRestart());
        String[] windStrength = getWindStrength(table);
        Log.i("windStrength", sw.getElapsedAndRestart());
        String[] windGusts = getWindGusts(table);
        Log.i("windGusts", sw.getElapsedAndRestart());

        int len = hours.size();
        ForecastData[] data = new ForecastData[len];

        for (int i = 0; i < len; i++) {
            ForecastData current = data[i] = new ForecastData();

            current.Hour = hours.get(i).text();
            current.IsDayLight = hours.get(i).className().startsWith("d");
            current.Clouds = clouds.get(i).className();

            ForecastRain rain = new ForecastRain();
            rain.FirstHalf = rains.get(i * 2).child(0).className();
            rain.FirstHalfNum = parseRain(rains.get(i * 2).attr("onmouseover"));
            rain.SecondHalf = rains.get(i * 2 + 1).child(0).className();
            rain.SecondHalfNum = parseRain(rains.get(i * 2 + 1).attr("onmouseover"));
            current.Rain = rain;

            current.Temp = temps[i];

            current.Wind = new ForecastWind();
            current.Wind.Direction = parseWindDirection(windDirs[i]);
            current.Wind.Strength = windStrength[i];
            if (windGusts != null && windGusts.length > 0) {
                current.Wind.Gusts = windGusts[i];
            }
        }
        Log.i("for", sw.getElapsedAndRestart());

        setDays(table, data);
        Log.i("setDays", sw.getElapsedAndRestart());

        Forecast forecast = new Forecast();
        forecast.Data = data;

        forecast.RefreshDate = getRefreshDate(html);
        Log.i("setRefreshDate", sw.getElapsedAndRestart());
        if (yartemp != null) {
            parseYartemp(yartemp, forecast);
            Log.i("parseYartemp", sw.getElapsedAndRestart());
        }

        return forecast;
    }

    private static String[] getWindGusts(Element table) {
        return getValues(table, new Predicate<Element>() {
            @Override
            public boolean test(Element e) {
                return e.text().contains("порывы");
            }
        }, new Function<Element, String>() {
            @Override
            public String apply(Element e) {
                return getWind(e);
            }
        });
    }

    private static String[] getWindStrength(Element table) {
        return getValues(table, new Predicate<Element>() {
            @Override
            public boolean test(Element e) {
                return e.child(0).id().equals("t_wind_velocity");
            }
        }, new Function<Element, String>() {
            @Override
            public String apply(Element e) {
                return getWind(e);
            }
        });
    }

    private static String[] getWindDirs(Element table) {
        return getValues(table, new Predicate<Element>() {
            @Override
            public boolean test(Element e) {
                return e.text().contains("направление");
            }
        }, new Function<Element, String>() {
            @Override
            public String apply(Element e) {
                return e.text();
            }
        });
    }

    private static String[] getTemps(Element table) {
        return getValues(table, new Predicate<Element>() {
            @Override
            public boolean test(Element e) {
                return e.child(0).id().equals("t_temperature");
            }
        }, new Function<Element, String>() {
            @Override
            public String apply(Element e) {
                return e.child(0).text();
            }
        });
    }

    private static String[] getValues(Element table, Predicate<Element> predicate, Function<Element, String> selector) {
        String[] result = null;

        for (Element tr : table.child(0).children()) {
            if (predicate.test(tr.child(0))) {
                result = new String[tr.children().size() - 1];
                for (int i = 0; i < result.length; i++) {
                    result[i] = selector.apply(tr.child(i + 1));
                }
                break;
            }
        }
        return result;
    }

    private static void parseYartemp(String html, Forecast forecast) {
        Matcher matcher = yartempPattern.matcher(html);
        if (matcher.matches()) {
            forecast.YarTemp = matcher.group(1);
            forecast.YarTempDiff = matcher.group(2);
        }
    }

    private static String getRefreshDate(String html) {
        Pattern pattern = Pattern.compile(".*Последнее обновление прогноза погоды было\\s*(\\d+)&amp;nbsp;час(|а|ов) (\\d+)&amp;nbsp;минут(|у|ы)\\s*назад.*");
        Matcher matcher = pattern.matcher(html);
        if (matcher.matches()) {
            return matcher.group(1) + ":" + String.format("%02d", Integer.parseInt(matcher.group(3)));
        }
        else {
            pattern = Pattern.compile(".*Последнее обновление прогноза погоды было\\s*(\\d+)&amp;nbsp;минут(|у|ы)\\s*назад.*");
            matcher = pattern.matcher(html);
            if (matcher.matches()) {
                return "0:" + String.format("%02d", Integer.parseInt(matcher.group(1)));
            }
            else {
                pattern = Pattern.compile(".*Последнее обновление прогноза погоды было\\s*(\\d+)&amp;nbsp;час(|а|ов)\\s*назад.*");
                matcher = pattern.matcher(html);
                if (matcher.matches()) {
                    return matcher.group(1) + ":00";
                }
                else {
                    return "???";
                }
            }
        }
    }

    private static String getWind(Element element) {
        if (element.children().size() == 0) {
            return element.text();
        }
        else {
            return element.children().first().text();
        }
    }

    private static void setDays(Element table, ForecastData[] data) {
        int current = -1;
        Elements days = table.select("tbody > tr.forecastDate > td:gt(0)");

        for (Element day : days) {
            int colspan = Integer.parseInt(day.attr("colspan")) / 2;

            if (current == -1) {
                current = colspan;
            }
            else {
                data[current].DayOfWeek = day.child(0).child(0).child(0).text();
                current += colspan;
            }
        }
    }

    private static WindDirection parseWindDirection(String text) {
        switch (text) {
            case "С":
                return WindDirection.N;
            case "С-З":
                return WindDirection.NW;
            case "З":
                return WindDirection.W;
            case "Ю-З":
                return WindDirection.SW;
            case "Ю":
                return WindDirection.S;
            case "Ю-В":
                return WindDirection.SE;
            case "В":
                return WindDirection.E;
            case "С-В":
                return WindDirection.NE;
            default:
                return null;
        }
    }

    private static String parseRain(String text) {
        Matcher mc = rainPattern.matcher(text);
        if (!mc.matches()) {
            return "";
        }
        return mc.group(1);
    }

    private static String getTableName(ForecastPeriod forecastPeriod) {
        switch (forecastPeriod) {
            case OneDay:
                return "forecastTable_1";
            case ThreeDays:
                return "forecastTable_3";
            case SixDays:
                return "forecastTable";
        }
        return null;
    }
}
