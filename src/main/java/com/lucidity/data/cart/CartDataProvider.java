package com.lucidity.data.cart;

import com.lucidity.enums.OfferType;
import org.testng.annotations.DataProvider;

public class CartDataProvider {

    @DataProvider(name = "offerTestData")
    public Object[][] offerTestData() {
        return new Object[][]{
                {"1", "p1", OfferType.FLATX.name(), 10, 200, 190},
                {"1", "p2", OfferType.FLATX_PERCENT.name(), 10, 200, 180},
                {"2", "p1", OfferType.FLATX.name(), 0, 200, 200},
                {"2", "p2", OfferType.FLATX_PERCENT.name(), 0, 200, 200},
                {"3", "p1", OfferType.FLATX.name(), 10, 0, 0},
                {"3", "p2", OfferType.FLATX_PERCENT.name(), 10, 0, 0}
        };
    }
}
