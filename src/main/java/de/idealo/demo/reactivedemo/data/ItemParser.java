package de.idealo.demo.reactivedemo.data;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemParser {

    public static void main(String[] args) {
        final ItemParser itemParser = new ItemParser();

        itemParser.parseItems()
                .subscribe(item -> log.info("parsed item: {}", item));
    }

    public Flowable<Item> parseItems() {
        ClassLoader classLoader = ItemParser.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("items.csv");

        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
        return Flowable.fromIterable(reader)
                .map(csvRow -> new Item(csvRow[0], csvRow[1]));
    }
}
