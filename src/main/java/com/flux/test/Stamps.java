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


    public Integer calculateStamps(List<Scheme> schemeAvail, Receipt receipt) {
        Skus skus = new Skus();
        ReceiptItems receiptItems = new ReceiptItems();
        ApplyReward applyReward = new ApplyReward();

        List skusFromScheme = skus.getSkusFromScheme(schemeAvail);
        List itemsFromReceipt = receiptItems.getItemsFromReceipt(receipt);

        for(int i = 0; i < skusFromScheme.size(); i++ ) {
            int maxStamps = schemeAvail.get(i).getMaxStamps();
            currentSchemeId = schemeAvail.get(i).getId();
            List currentSkuList = ((ArrayList) skusFromScheme.get(i));

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

    private void applyStamps(List itemsFromReceipt, List currentSkuList) {
        for(int x = 0; x < itemsFromReceipt.size(); x++) {
            if (currentSkuList.contains(((Item) itemsFromReceipt.get(x)).getSku())) {
                earnedStampItems.add((Item) itemsFromReceipt.get(x));
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
