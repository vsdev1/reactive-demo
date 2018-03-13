package de.idealo.demo.reactivedemo.data;

import java.io.BufferedReader;
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
public class ProductParser {

    public static void main(String[] args) {
        final ProductParser productParser = new ProductParser();

        productParser.parseProduct()
                .subscribe(product -> log.info("parsed product: {}", product));
    }

    public Flowable<Product> parseProduct() {
        ClassLoader classLoader = ProductParser.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("feed.csv");

        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(new BufferedReader(new InputStreamReader(inputStream)));

            return Flowable.fromIterable(records)
                    .flatMap(this::mapFromCsvRow);
        } catch (IOException e) {
            throw new RuntimeException("error while parsing feed", e);
        }
    }

    private Flowable<Product> mapFromCsvRow(CSVRecord csvRow) {
        return Flowable.just(csvRow)
                .map(row -> {
                    final Product product = new Product(row.get("product title"));
                    product.addOffer(createOffer(row, 1));
                    product.addOffer(createOffer(row, 2));
                    product.addOffer(createOffer(row, 3));
                    log.info("parsed product: {}", product);

                    return product;
                });
    }

    private Offer createOffer(CSVRecord csvRow, int merchantIndex) throws ParseException {
        return new Offer(csvRow.get("item " + merchantIndex + " - Merchant name"),
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
