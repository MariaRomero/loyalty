package com.flux.test;

import com.flux.test.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoyaltyScheme implements ImplementMe {

    Stamps stamps = new Stamps();
    final AvailableSchemesReader schemesReader = new AvailableSchemesReader();

    List<Long> paymentsGiven;
    List<Scheme> schemeAvailList = getSchemes();
    UUID currentSchemeId;
    Integer stampsGiven;
    Integer stampsCount;

    @NotNull
    private ApplyResponse generateApplyResponse(@NotNull Receipt receipt) {
        stampsGiven = stamps.calculateStamps(schemeAvailList, receipt);
        stampsCount = stamps.getStampsCount();
        paymentsGiven = stamps.paymentsGiven;
        return new ApplyResponse(receipt.getMerchantId(), stampsCount, stampsGiven, paymentsGiven);
    }

    @NotNull
    @Override
    public List<ApplyResponse> apply(@NotNull Receipt receipt) {
        List<ApplyResponse> applyResponses = new ArrayList<>();

        if (schemesReader.schemeAvailForMerchant(receipt)) {
            ApplyResponse applyResponse = generateApplyResponse(receipt);
            applyResponses.add(applyResponse);
        }
        return applyResponses;
    }

    @NotNull
    @Override
    public List<StateResponse> state(@NotNull UUID accountId) {
        List<StateResponse> stateResponses = new ArrayList<>();
        currentSchemeId = stamps.currentSchemeId;
        stampsCount = stamps.getStampsCount();
        paymentsGiven = stamps.paymentsGiven;

        StateResponse stateResponse = new StateResponse(currentSchemeId, stampsCount, paymentsGiven);
        stateResponses.add(stateResponse);
        return  stateResponses;
    }

    @NotNull
    @Override
    public List<Scheme> getSchemes() {

        return schemesReader.getSchemeListFromFile();
    }

    @Override
    public void setSchemes(@NotNull List<Scheme> list) {
        throw new UnsupportedOperationException("Not called from tests, so not implemented");
    }
}
