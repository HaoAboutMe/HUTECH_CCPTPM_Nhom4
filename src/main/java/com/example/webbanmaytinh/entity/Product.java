package com.example.webbanmaytinh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "is_discounted", nullable = false)
    @Builder.Default
    private Boolean isDiscounted = false;

    @Min(0)
    @Max(100)
    @Column(name = "discount_percent", nullable = false)
    @Builder.Default
    private Integer discountPercent = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Transient
    public BigDecimal getFinalPrice() {
        if (price == null) {
            return null;
        }
        Integer percent = discountPercent;
        Boolean discounted = isDiscounted;
        if (discounted == null || !discounted || percent == null || percent <= 0) {
            return price;
        }
        BigDecimal multiplier = BigDecimal.valueOf(100L - percent).divide(BigDecimal.valueOf(100L), 4, RoundingMode.HALF_UP);
        return price.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}
