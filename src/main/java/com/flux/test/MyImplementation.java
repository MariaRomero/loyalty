package com.flux.test;

import com.flux.test.model.*;
//import org.json.simple.JSONArray;
import org.json.simple.ItemList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import org.jetbrains.annotations.NotNull;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyImplementation implements ImplementMe {
    @NotNull
    @Override
    public List<Scheme> getSchemes() {
        return null;
    }

    @Override
    public void setSchemes(@NotNull List<Scheme> list) {

    }

    public List jsonPar() {

        JSONParser parser = new JSONParser();
        List schemeAvail = new ArrayList<>();
        try {
            Object object = parser.parse(new FileReader("src/data/schemesAvailable.json"));
            JSONArray schemes = (JSONArray)object;
            schemes.stream().forEach( scheme -> schemeAvail.add(scheme));

            return schemeAvail;

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schemeAvail;

    }

    /**
     * Retrieve and return the current state for an account for all the active schemes.  If they have never used a
     * scheme before then a zero state should be returned (ie 0 stamps given, 0 payments, current stamp as 0.
     *
     * Should return one `StateResponse` instance for each scheme
     */
//    Receipt(id=e6b1da05-050e-4463-b981-72793d4977ac,
//            accountId=33f862b4-3f02-47e0-b9b7-d7e44cf5716e,
//            merchantId=c8ed317f-c27d-4415-a80b-188b23047294,
//            items=[Item(sku=1, price=100, quantity=1),
//            Item(sku=1, price=100, quantity=1),
//            Item(sku=1, price=100, quantity=1),
//            Item(sku=1, price=100, quantity=1),
//            Item(sku=1, price=100, quantity=1)])

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
    public String calculateStamps(List<Item> schemeAvail) {

//        Scheme scheme = new Scheme()
//        schemeAvail.stream().filter(s-> itemList.get(0)).forEach(System.out::println);
        return schemeAvail.forEach(System.out::println);

    }

    @NotNull
    @Override
    public List<ApplyResponse> apply(@NotNull Receipt receipt) {
        List<Item> schemeAvail = jsonPar();
        calculateStamps(schemeAvail);
//        private final Scheme schemeAvail = jsonPar();
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
}
