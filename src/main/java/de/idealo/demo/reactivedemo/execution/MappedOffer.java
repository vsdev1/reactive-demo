package de.idealo.demo.reactivedemo.execution;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MappedOffer {
    private final String productTitle;
    private final String merchantName;
    private final BigDecimal price;
    private final String metaData;
}
