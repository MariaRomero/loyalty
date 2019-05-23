package com.flux.test;
import com.flux.test.model.Item;
import java.util.Comparator;
import java.util.List;

public class ApplyReward {

    public Long apply(final List<Item> earnedStampItems) {
        long price = earnedStampItems.stream()
                .sorted(Comparator.comparing(Item::getPrice))
                .findFirst()
                .get()
                .getPrice();

        return price;
    }
}
