package com.flux.test;

import com.flux.test.model.Item;
import com.flux.test.model.Receipt;

import java.util.ArrayList;
import java.util.List;

public class ReceiptItems {

    public List<Item> getItemsFromReceipt(final Receipt receipt) {

        final List<Item> receiptItemsList = new ArrayList<>();

        receipt.getItems().stream().forEach(item -> receiptItemsList.add(item));

        return receiptItemsList;
    }
}
