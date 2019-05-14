package com.flux.test;

import com.flux.test.model.*;
//import org.json.simple.JSONArray;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
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

    @Override
    public void setSchemes(@NotNull final List<Scheme> list) {

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
    public List calculateStamps(List<Scheme> schemeAvail, Receipt receipt) {

        for (int i = 0; i < schemeAvail.size() - 1; i++) {
            if((schemeAvail.get(i).getSkus().get(i) == 1 && receipt.getItems().get(i).getSku() == 1);
//            if (receipt.getItems()) {
//                System.out.println(true);
//            }else {
//                System.out.println(false);
//
//            }
        }

        return schemeAvail;
    }

    @NotNull
    @Override
    public List<ApplyResponse> apply(@NotNull Receipt receipt) {
        List<Scheme> schemesAvail = getSchemes();
//        System.out.println(schemeAvail);  [{"skus":["1","2","3","4"],"SchemeId":"10000000000","merchantId":"010101010101","maxStamps":"4"},
//                                          {"skus":["5","6","7","8"],"SchemeId":"20000000000","merchantId":"010101010101","maxStamps":"4"}]

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
}
