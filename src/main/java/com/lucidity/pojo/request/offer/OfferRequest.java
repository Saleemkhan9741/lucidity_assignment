package com.lucidity.pojo.request.offer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferRequest {
    @JsonProperty("restaurant_id")
    private int restaurantId;
    @JsonProperty("offer_type")
    private String offerType;
    @JsonProperty("offer_value")
    private int offerValue;
    @JsonProperty("customer_segment")
    private List<String> customerSegment;
}
