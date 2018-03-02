package de.idealo.demo.reactivedemo.data;

import java.util.Collection;
import java.util.HashSet;

import lombok.Data;
import lombok.Getter;

@Data
public class Items {
    private final String productTitle;
    @Getter
    private Collection<Item> items = new HashSet<>();

    public void addItem(Item item) {
        items.add(item);
    }
}
