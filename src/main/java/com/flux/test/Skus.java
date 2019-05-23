package com.flux.test;

import com.flux.test.model.Scheme;

import java.util.ArrayList;
import java.util.List;

public class Skus {

    public List<List<String>> getSkusFromScheme(final List<Scheme> schemesAvail) {

        final List<List<String>> schemeSkus = new ArrayList<>();

        schemesAvail.stream().forEach(scheme -> schemeSkus.add(scheme.getSkus()));

        return schemeSkus;
    }
}
