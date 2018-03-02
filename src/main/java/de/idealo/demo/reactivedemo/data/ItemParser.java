package de.idealo.demo.reactivedemo.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemParser {

    public static void main(String[] args) {
        final ItemParser itemParser = new ItemParser();

        itemParser.parseItems()
                .subscribe(items -> log.info("parsed items: {}", items));
    }

    public Flowable<Items> parseItems() {
        ClassLoader classLoader = ItemParser.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("feed.csv");

        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(new InputStreamReader(inputStream));

            return Flowable.fromIterable(records)
                    .flatMap(this::mapFromCsvRow);
        } catch (IOException e) {
            throw new RuntimeException("error while parsing feed", e);
        }
    }

    private Flowable<Items> mapFromCsvRow(CSVRecord csvRow) {
        return Flowable.just(csvRow)
                .map(row -> {
                    final Items items = new Items(row.get("product title"));
                    items.addItem(createItem(row, 1));
                    items.addItem(createItem(row, 2));
                    items.addItem(createItem(row, 3));
                    log.info("parsed items: {}", items);

                    return items;
                });
    }

    private Item createItem(CSVRecord csvRow, int merchantIndex) throws ParseException {
        return new Item(csvRow.get("item " + merchantIndex + " - Merchant name"),
                parsePrice(csvRow.get("item " + merchantIndex + " - price")));
    }

    private BigDecimal parsePrice(String price) throws ParseException {
        NumberFormat f = NumberFormat.getNumberInstance(Locale.US);
        if (f instanceof DecimalFormat) {
            ((DecimalFormat) f).setParseBigDecimal(true);
            return (BigDecimal) f.parse(price);
        }

        throw new RuntimeException("no number: " + price);
    }
}
