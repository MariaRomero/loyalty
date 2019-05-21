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

    List<Long> paymentsGiven = new ArrayList<>();
    List<Item> itemsFromReceiptOnScheme = new ArrayList<>();
    Integer stamps = 0;
    Integer stampsCount = 0;
    UUID currentSchemeId;

    Reward reward = new Reward();

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

        schemeAvail.stream().forEach(scheme -> schemeSkus.add(scheme.getSkus()));

        return schemeSkus;
    }

    private List<Item> getItemsListFromReceipt(Receipt receipt) {

        List<Item> receiptItemsList = new ArrayList<>();

        receipt.getItems().stream().forEach(item -> receiptItemsList.add(item));

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
        List skusFromScheme = getSkuFromSchemeList(schemeAvail);
        List itemsFromReceipt = getItemsListFromReceipt(receipt);

        for(int i = 0; i < skusFromScheme.size(); i++ ) {
            int maxStamps = schemeAvail.get(i).getMaxStamps();
            currentSchemeId = schemeAvail.get(i).getId();
            List skuFromScheme = ((ArrayList) skusFromScheme.get(i));

            applyStamps(itemsFromReceipt, skuFromScheme);

            if (stamps > maxStamps) {
                paymentsGiven.add(reward.awardReward(itemsFromReceiptOnScheme));
                return capStamps(maxStamps, stamps);
            }
        }
        stampsCount = stamps;
        return stamps;
    }

    private void applyStamps(List itemsFromReceipt, List skuFromScheme) {
        for(int x = 0; x < itemsFromReceipt.size(); x++) {
            if (skuFromScheme.contains(((Item) itemsFromReceipt.get(x)).getSku())) {
                itemsFromReceiptOnScheme.add((Item) itemsFromReceipt.get(x));
                stamps++;
            }
        }
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
            ApplyResponse applyResponse = new ApplyResponse(receipt.getMerchantId(), stampsCount, stampsGiven, paymentsGiven);
            applyResponses.add(applyResponse);
        }
        return applyResponses;
    }

    @NotNull
    @Override
    public List<StateResponse> state(@NotNull UUID accountId) {
        List<StateResponse> stateResponses = new ArrayList<>();

        StateResponse stateResponse = new StateResponse(currentSchemeId, stampsCount, paymentsGiven);
        stateResponses.add(stateResponse);
        return  stateResponses;
    }

    @Override
    public void setSchemes(@NotNull List<Scheme> list) {

    }

}
