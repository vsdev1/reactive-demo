package de.idealo.demo.reactivedemo.data;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Item {
    private final String merchantName;
    private final BigDecimal price;
}
