package de.idealo.demo.reactivedemo.data;

import java.util.Collection;
import java.util.HashSet;

import lombok.Data;
import lombok.Getter;

@Data
public class Product {
    private final String productTitle;
    @Getter
    private Collection<Offer> offers = new HashSet<>();

    public void addOffer(Offer offer) {
        offers.add(offer);
    }
}
