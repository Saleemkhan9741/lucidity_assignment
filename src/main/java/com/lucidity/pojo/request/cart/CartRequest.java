package com.lucidity.pojo.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    @JsonProperty("cart_value")
    private double cartValue;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("restaurant_id")
    private int restaurantId;
}
