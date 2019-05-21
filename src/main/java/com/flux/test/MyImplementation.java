package com.flux.test;

import com.flux.test.model.*;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MyImplementation implements ImplementMe {

    List<Long> rewardsGiven = new ArrayList<>();
    List<Item> itemsFromReceiptOnScheme = new ArrayList<>();
    Integer stampsCount = 0;
    UUID currentSchemeId;


    private Object readFile() throws IOException, ParseException {
        final JSONParser parser = new JSONParser();
        return parser.parse(new FileReader("src/data/schemesAvailable.json"));
    }

    @NotNull
    @Override
    public List<Scheme> getSchemes() {
        final Gson gson = new Gson();
        final List<Scheme> schemeAvail = new ArrayList<>();
        JSONArray schemes = new JSONArray();
        try {
            schemes = (JSONArray) readFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        schemes.stream().forEach( scheme -> schemeAvail.add(gson.fromJson(scheme.toString(), Scheme.class)));

        return schemeAvail;
    }

    private List<List<String>> getSkuFromSchemeList(List<Scheme> schemeAvail) {

        List<List<String>> schemeSkus = new ArrayList<>();

        for (int i = 0; i < schemeAvail.size(); i++ ) {
            schemeSkus.add(schemeAvail.get(i).getSkus());
        }
        return schemeSkus;
    }

    private List<Item> getItemsListFromReceipt(Receipt receipt) {

        List<Item> receiptItemsList = new ArrayList<>();

        for (int i = 0; i < receipt.getItems().size(); i++ ) {
            receiptItemsList.add(receipt.getItems().get(i));
        }

        return receiptItemsList;
    }

    private boolean schemesAvailForMerchant(List<Scheme> schemes, Receipt receipt) {
        for (int i = 0; i < schemes.size(); i++ ) {
            UUID schemeMerchantId = schemes.get(i).getMerchantId();
            UUID receiptMerchantId = receipt.getMerchantId();
            if (schemeMerchantId.equals(receiptMerchantId)) {
               return true;
            }
        }
        return false;
    }

    public Integer calculateStamps(List<Scheme> schemeAvail, Receipt receipt) {
        Integer stamps = 0;
        List skusFromScheme = getSkuFromSchemeList(schemeAvail);
        List itemsFromReceipt = getItemsListFromReceipt(receipt);

        for(int i = 0; i < skusFromScheme.size(); i++ ) {
            int maxStamps = schemeAvail.get(i).getMaxStamps();
            currentSchemeId = schemeAvail.get(i).getId();
            List skuFromScheme = ((ArrayList) skusFromScheme.get(i));
            for(int x = 0; x < itemsFromReceipt.size(); x++) {
                if (skuFromScheme.contains(((Item) itemsFromReceipt.get(x)).getSku())) {
                    itemsFromReceiptOnScheme.add((Item) itemsFromReceipt.get(x));
                    stamps++;
                }
            }
            if (stamps > maxStamps) {
                awardReward();
                return capStamps(maxStamps, stamps);
            }
        }
        stampsCount = stamps;
        return stamps;
    }

    private void awardReward() {
        long price = itemsFromReceiptOnScheme.stream()
                .sorted(Comparator.comparing(Item::getPrice))
                .findFirst()
                .get()
                .getPrice();

        rewardsGiven.add(price);
    }

    private Integer capStamps(int maxStamps, Integer stamps) {
        stampsCount = 0;
        return stamps - (stamps % maxStamps);
    }


    @NotNull
    @Override
    public List<ApplyResponse> apply(@NotNull Receipt receipt) {
        Integer stampsGiven = 0;

        List<ApplyResponse> applyResponses = new ArrayList<>();

        List<Scheme> schemesAvail = getSchemes();

        if (schemesAvailForMerchant(schemesAvail, receipt)) {
            stampsGiven = calculateStamps(schemesAvail, receipt);
            ApplyResponse applyResponse = new ApplyResponse(receipt.getMerchantId(), stampsCount, stampsGiven,rewardsGiven);
            applyResponses.add(applyResponse);
        }
        return applyResponses;
    }

    @NotNull
    @Override
    public List<StateResponse> state(@NotNull UUID accountId) {
        List<StateResponse> stateResponses = new ArrayList<>();

        StateResponse stateResponse = new StateResponse(currentSchemeId, stampsCount, rewardsGiven);
        stateResponses.add(stateResponse);
        return  stateResponses;
    }

    @Override
    public void setSchemes(@NotNull List<Scheme> list) {

    }

}
