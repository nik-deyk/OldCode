package json; //remove!!!!

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {
    private static class JSONComparatorBasedOnId implements Comparator<JSONObject> {

        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            return Long.compare((Long) o1.get("id"), (Long) o2.get("id"));
        }
    }

    private static enum FilterType {
        NAME_CONTAINS,
        PRICE_GREATER_THAN,
        PRICE_LESS_THAN,
        DATE_BEFORE,
        DATE_AFTER
    }

    private static class Filter {
        private static final int TYPES_COUNT = FilterType.values().length;
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        public EnumSet<FilterType> filters = EnumSet.noneOf(FilterType.class);;

        private String subName;
        private long minPrice;
        private long maxPrice;
        private LocalDate maxDate;
        private LocalDate minDate;

        protected static class Item {
            public final long price;
            public final String name;
            public final LocalDate date;

            public Item(JSONObject representation) {
                this.price = (Long) representation.get("price");
                this.name = (String) representation.get("name");
                this.date = LocalDate.parse((String) representation.get("date"), formatter);
            }
        }

        public void addFilter(String filterName, String argument) {
            FilterType type = FilterType.valueOf(filterName);
            if (!filters.contains(type)) {
                switch (type) {
                    case NAME_CONTAINS:
                        subName = argument.toLowerCase();
                        break;
                    case PRICE_GREATER_THAN:
                        minPrice = Long.valueOf(argument);
                        break;
                    case PRICE_LESS_THAN:
                        maxPrice = Long.valueOf(argument);
                        break;
                    case DATE_BEFORE:
                        maxDate = LocalDate.parse(argument, formatter);
                        break;
                    case DATE_AFTER:
                        minDate = LocalDate.parse(argument, formatter);
                        break;
                }
                filters.add(type);
            }
        }

        public boolean check(JSONObject obj) {
            Item item = new Item(obj);
            for (FilterType type : this.filters) {
                switch (type) {
                    case NAME_CONTAINS:
                        if (!new String(item.name).toLowerCase().contains(this.subName)) {
                            return false;
                        }
                        break;
                    case PRICE_GREATER_THAN:
                        if (item.price < this.minPrice) {
                            return false;
                        }
                        break;
                    case PRICE_LESS_THAN:
                        if (item.price > this.maxPrice) {
                            return false;
                        }
                        break;
                    case DATE_BEFORE:
                        if (item.date.isAfter(maxDate)) {
                            return false;
                        }
                        break;
                    case DATE_AFTER:
                        if (item.date.isBefore(minDate)) {
                            return false;
                        }
                        break;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        JSONParser parser = new JSONParser();
        Filter filter = new Filter();
        try {
            JSONArray array = (JSONArray) parser.parse(bufferedReader.readLine());
            for (int i = 0; i < Filter.TYPES_COUNT; i++) {
                String[] filterData = bufferedReader.readLine().split(" ");
                filter.addFilter(filterData[0], filterData[1]);
            }

            bufferedReader.close();

            ArrayList<JSONObject> resultList = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject representation = (JSONObject) array.get(i);
                if (filter.check(representation)) {
                    resultList.add(representation);
                }
            }

            Collections.sort(resultList, new JSONComparatorBasedOnId());

            final BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(System.out));
            writter.write("[");
            for (JSONObject obj : resultList) {
                writter.write(obj.toString());
                writter.write(",");
            }
            writter.write("]\n");
            writter.flush();
            writter.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
