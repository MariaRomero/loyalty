package com.flux.test;

import com.flux.test.model.Receipt;
import com.flux.test.model.Scheme;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AvailableSchemesReader {

    final List<Scheme> schemeAvail = new ArrayList<>();

    public List<Scheme> getSchemeListFromFile() {
        final Gson gson = new Gson();
        JSONArray jsonSchemes = new JSONArray();

        try {
            jsonSchemes = (JSONArray) readFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        jsonSchemes.stream().forEach( scheme -> schemeAvail.add(gson.fromJson(scheme.toString(), Scheme.class)));

        return schemeAvail;
    }

    private Object readFile() throws IOException, ParseException {
        final JSONParser parser = new JSONParser();
        return parser.parse(new FileReader("src/data/schemesAvailable.json"));
    }

    public boolean schemeAvailForMerchant(Receipt receipt) {
        for (int i = 0; i < schemeAvail.size(); i++ ) {
            UUID schemeMerchantId = schemeAvail.get(i).getMerchantId();
            UUID receiptMerchantId = receipt.getMerchantId();
            if (schemeMerchantId.equals(receiptMerchantId)) {
                return true;
            }
        }
        return false;
    }
}
