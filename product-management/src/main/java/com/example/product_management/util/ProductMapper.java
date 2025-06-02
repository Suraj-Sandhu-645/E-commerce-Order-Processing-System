package com.example.product_management.util;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity toEntity(ProductDTO dto);
    ProductDTO toDTO(ProductEntity product);
}
