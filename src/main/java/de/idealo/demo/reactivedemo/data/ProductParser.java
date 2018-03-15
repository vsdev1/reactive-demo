package de.idealo.demo.reactivedemo.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public Flowable<Product> parseProduct() {
        try {
            BufferedReader bufferedFeedReader = Files.newBufferedReader(getFeedPath(), StandardCharsets.UTF_8);

            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(bufferedFeedReader);

            return Flowable.fromIterable(records).flatMap(this::mapFromCsvRow);
        } catch (IOException e) {
            throw new RuntimeException("error while parsing feed", e);
        }
    }

    private Path getFeedPath() {
        try {
            return Paths.get(ClassLoader.getSystemResource("feed.csv").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while getting feed path", e);
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
