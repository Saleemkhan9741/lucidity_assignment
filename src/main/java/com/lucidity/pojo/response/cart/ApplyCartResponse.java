package com.lucidity.pojo.response.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCartResponse {

    @JsonProperty("cart_value")
    private int cartValue;
}
