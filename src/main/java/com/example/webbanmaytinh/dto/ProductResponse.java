package com.example.webbanmaytinh.dto;

import com.example.webbanmaytinh.entity.Product;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductResponse {
    String id;
    String name;
    BigDecimal price;
    Boolean isDiscounted;
    Integer discountPercent;
    BigDecimal finalPrice;
    String description;
    String imageUrl;
    Integer quantity;
    String categoryId;
    String categoryName;

    public static ProductResponse from(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .isDiscounted(product.getIsDiscounted())
                .discountPercent(product.getDiscountPercent())
                .finalPrice(product.getFinalPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }
}

