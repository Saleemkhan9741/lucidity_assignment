package com.lucidity.data.cart;

import com.lucidity.enums.OfferType;
import org.testng.annotations.DataProvider;

public class CartDataProvider {

    @DataProvider(name = "offerTestData")
    public Object[][] offerTestData() {
        return new Object[][]{
                {"TC01", "1", "p1", OfferType.FLATX.name(), 10, 200, 190},
                {"TC02", "1", "p2", OfferType.FLATX_PERCENT.name(), 10, 200, 180},
                {"TC03", "2", "p1", OfferType.FLATX.name(), 0, 200, 200},
                {"TC04", "2", "p2", OfferType.FLATX_PERCENT.name(), 0, 200, 200},
                {"TC05", "3", "p1", OfferType.FLATX.name(), 10, 0, 0},
                {"TC06", "3", "p2", OfferType.FLATX_PERCENT.name(), 10, 0, 0}
        };
    }
}
