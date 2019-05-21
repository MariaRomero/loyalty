package com.flux.test;
import com.flux.test.model.Item;
import java.util.Comparator;
import java.util.List;

public class Reward {

    public Long awardReward(List<Item> itemsFromReceiptOnScheme) {
        long price = itemsFromReceiptOnScheme.stream()
                .sorted(Comparator.comparing(Item::getPrice))
                .findFirst()
                .get()
                .getPrice();

        return price;
    }
}
