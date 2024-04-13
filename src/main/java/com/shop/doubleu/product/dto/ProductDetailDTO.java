package com.shop.doubleu.product.dto;

import com.shop.doubleu.product.entity.Product;
import com.shop.doubleu.product.entity.ProductDetail;
import lombok.Data;

@Data
public class ProductDetailDTO {
    private Product product;
    private ProductDetail productDetail;

    public ProductDetailDTO(Product product, ProductDetail productDetail) {
        this.product = product;
        this.productDetail = productDetail;
    }
}
