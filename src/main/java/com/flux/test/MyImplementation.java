package com.flux.test;

import com.flux.test.model.*;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyImplementation implements ImplementMe {

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

    private Object readFile() throws IOException, ParseException {
        final JSONParser parser = new JSONParser();
        return parser.parse(new FileReader("src/data/schemesAvailable.json"));
    }

    /**
     * Retrieve and return the current state for an account for all the active schemes.  If they have never used a
     * scheme before then a zero state should be returned (ie 0 stamps given, 0 payments, current stamp as 0.
     *
     * Should return one `StateResponse` instance for each scheme
     */


//    Response[ApplyResponse(schemeId=dbf5f024-ea1e-4e11-a620-9589623237ed,
//                           currentStampCount=1,
//                           stampsGiven=1,
//                           paymentsGiven=[])]
//
//   response shouldHaveSize (1)
//            response.first().currentStampCount shouldBe 0
//            response.first().stampsGiven shouldBe 4
//            response.first().paymentsGiven shouldHaveSize 1
//            response.first().paymentsGiven.first() shouldBe 100

    private List<List<String>> getSkuFromSchemeList(List<Scheme> schemeAvail) {

        List<List<String>> schemeSkus = new ArrayList<>();

        for (int i = 0; i < schemeAvail.size(); i++ ) {
            schemeSkus.add(schemeAvail.get(i).getSkus());
        }

//        System.out.println(schemeSkus);  [[1, 2, 3, 4], [5, 6, 7, 8]]
        return schemeSkus;
    }

    private List<Item> getItemsListFromReceipt(Receipt receipt) {

        List<Item> receiptItemsList = new ArrayList<>();

        for (int i = 0; i < receipt.getItems().size(); i++ ) {
            receiptItemsList.add(receipt.getItems().get(i));
        }
//        System.out.println(receiptItemsList);  [Item(sku=1, price=100, quantity=1),
//                                                Item(sku=1, price=100, quantity=1),
//                                                Item(sku=1, price=100, quantity=1),
//                                                Item(sku=1, price=100, quantity=1),
//                                                Item(sku=1, price=100, quantity=1)]

        //        System.out.println(schemeSkus);  [[1, 2, 3, 4], [5, 6, 7, 8]]

        return receiptItemsList;
    }
//
//    private Integer numberOfStamps(List<Item> receiptItemsList) {
//
//    }


    public List calculateStamps(List<Scheme> schemeAvail, Receipt receipt) {

        List skusFromScheme = getSkuFromSchemeList(schemeAvail);
        List itemsFromReceipt = getItemsListFromReceipt(receipt);

        for(int i = 0; i < itemsFromReceipt.size(); i++ ){
            String skuOnReceipt = ((Item) itemsFromReceipt.get(i)).getSku();
            for (int x = 0; x < ((ArrayList)skusFromScheme.get(i)).size(); x++ ){
                if (((ArrayList) skusFromScheme.get(i)).get(x).equals(skuOnReceipt) {

                }
            }
        }

        // compare items
//        itemFromReceipt.forEach((n) -> System.out.println(n));

        return skusFromScheme;
    }

    //    Receipt(id=e6b1da05-050e-4463-b981-72793d4977ac,
//            accountId=33f862b4-3f02-47e0-b9b7-d7e44cf5716e,
//            merchantId=c8ed317f-c27d-4415-a80b-188b23047294,
//            items=[
//            Item(sku=1, price=100, quantity=1),
//            Item(sku=2, price=200, quantity=1),
//            Item(sku=1, price=100, quantity=1),
//            Item(sku=1, price=100, quantity=1),
//            Item(sku=1, price=100, quantity=1)])

    private boolean schemesAvailForMerchant(List<Scheme> schemes, Receipt receipt) {
        for (int i = 0; i < schemes.size(); i++ ) {
            UUID schemeMerchantId = schemes.get(i).getMerchantId();
            UUID receiptMerchantId = receipt.getMerchantId();
            if (schemeMerchantId == receiptMerchantId) {
               true;
            } else {
                false;
            }
        }
    }

    @NotNull
    @Override
    public List<ApplyResponse> apply(@NotNull Receipt receipt) {
        List<Scheme> schemesAvail = getSchemes();
//        System.out.println(schemeAvail);  [{"skus":["1","2","3","4"],"SchemeId":"10000000000","merchantId":"010101010101","maxStamps":"4"},
//                                          {"skus":["5","6","7","8"],"SchemeId":"20000000000","merchantId":"010101010101","maxStamps":"4"}]
        boolean lucky = schemesAvailForMerchant(schemesAvail, receipt);

        calculateStamps(schemesAvail, receipt);
        ApplyResponse response = new ApplyResponse(receipt.getMerchantId(), 1, 1, new ArrayList<>());
        List<ApplyResponse> responses = new ArrayList<>();
        responses.add(response);

        return responses;
    }

    @NotNull
    @Override
    public List<StateResponse> state(@NotNull UUID accountId) {
        return null;
    }

    @Override
    public void setSchemes(@NotNull List<Scheme> list) {

    }

}
