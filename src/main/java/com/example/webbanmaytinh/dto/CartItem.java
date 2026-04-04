package com.example.webbanmaytinh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    private String productId;
    private String productName;
    private Long price;
    private String imageUrl;
    private Integer quantity;
}
