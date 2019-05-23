package com.flux.test;

import com.flux.test.model.Item;
import com.flux.test.model.Receipt;
import com.flux.test.model.Scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Stamps {

    private Integer stamps = 0;
    private Integer stampsCount = 0;
    private List<Item> earnedStampItems = new ArrayList<>();
    UUID currentSchemeId;
    List<Long> paymentsGiven = new ArrayList<>();


    public Integer calculateStamps(final List<Scheme> schemeAvail,final Receipt receipt) {
        final Skus skus = new Skus();
        final ReceiptItems receiptItems = new ReceiptItems();
        final ApplyReward applyReward = new ApplyReward();

        final List<List<String>> skusFromScheme = skus.getSkusFromScheme(schemeAvail);
        final List<Item> itemsFromReceipt = receiptItems.getItemsFromReceipt(receipt);

        for(int i = 0; i < skusFromScheme.size(); i++ ) {
            int maxStamps = schemeAvail.get(i).getMaxStamps();
            currentSchemeId = schemeAvail.get(i).getId();
            List<String> currentSkuList = skusFromScheme.get(i);

            applyStamps(itemsFromReceipt, currentSkuList);

            if (stamps > maxStamps) {
                stampsCount = 0;
                paymentsGiven.add(applyReward.apply(earnedStampItems));
                return capStamps(maxStamps, stamps);
            }
        }
        stampsCount = stamps;
        return stamps;
    }

    private void applyStamps(final List itemsFromReceipt, List currentSkuList) {
        for(int i = 0; i < itemsFromReceipt.size(); i++) {
            if (currentSkuList.contains(((Item) itemsFromReceipt.get(i)).getSku())) {
                earnedStampItems.add((Item) itemsFromReceipt.get(i));
                stamps++;
            }
        }
    }

    private Integer capStamps(int maxStamps, Integer stamps) {
        return stamps - (stamps % maxStamps);
    }

    public Integer getStampsCount() {
        return stampsCount;
    }
}
