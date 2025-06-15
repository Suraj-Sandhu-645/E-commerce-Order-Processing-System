package com.example.product_management.util;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
//    @Mapping(source = "id", target = "userId")
    ProductEntity toEntity(ProductDTO dto);
//    @Mapping(source = "userId", target = "id")
    ProductDTO toDTO(ProductEntity product);
}
