package com.service.marketplace.persistence.entity;

import com.stripe.model.Price;
import com.stripe.model.Product;

import java.math.BigDecimal;

public class SubscriptionDAO {
    static Product product;

    static {
        product = new Product();

        Product sampleProduct = new Product();
        Price samplePrice = new Price();

        sampleProduct.setName("Provider Subscription");
        sampleProduct.setId("provider_subscription");
        samplePrice.setCurrency("usd");
        samplePrice.setUnitAmountDecimal(BigDecimal.valueOf(9.99));
        sampleProduct.setDefaultPriceObject(samplePrice);
        product = sampleProduct;
    }

    public static Product getProduct(String id) {
        if ("provider_subscription".equals(id)) {
            return product;
        } else {
            return new Product();
        }
    }
}
