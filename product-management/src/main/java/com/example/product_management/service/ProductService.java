package com.example.product_management.service;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.entity.ProductEntity;
import com.example.product_management.exception.MissingProductDataException;
import com.example.product_management.exception.ProductIdIsNotPresentException;
import com.example.product_management.repository.ProductRepository;
import com.example.product_management.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    public ProductDTO createAndSaveProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.toEntity(productDTO);
        productEntity = productRepository.save(productEntity);

        return productMapper.toDTO(productEntity);
    }


    public ProductDTO getProductDetailsByProductId(Integer id) {
        if(id != null) {
            Optional<ProductEntity> productEntity = productRepository.findById(id);
            if(productEntity.isPresent()){
                return productMapper.toDTO(productEntity.get());
            } else throw new ProductIdIsNotPresentException("Product id is not present");
        }
        throw new IllegalArgumentException("Id should not be null or empty");
    }

    public List<ProductDTO> getProductDetails() {
        List<ProductEntity> productEntityList =  productRepository.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();
        if(productEntityList.isEmpty()) {
            throw new MissingProductDataException("Product data is not present in database");
        } else{
            for(ProductEntity productEntity : productEntityList) {
                productDTOList.add(productMapper.toDTO(productEntity));
            }
            return productDTOList;
        }
    }


    public ProductDTO updateProductDetailsByProductId(Integer id, ProductDTO productDTO) {
        if(id != null) {
            Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
            if(productEntityOptional.isPresent()){
                ProductEntity productEntity = productEntityOptional.get();
                productEntity.setDescription(productDTO.getDescription() != null ? productDTO.getDescription() :
                        productEntity.getDescription());
                productEntity.setName(productDTO.getName() != null ? productDTO.getName() : productEntity.getName());
                productEntity.setPrice(productDTO.getPrice());
                productEntity.setStock(productDTO.getStock());
                productRepository.save(productEntity);
                return productMapper.toDTO(productEntity);

            } else throw new ProductIdIsNotPresentException("Product id is not present");
        }
        throw new IllegalArgumentException("Id should not be null or empty");
    }

    public Boolean deleteProductDetailsByProductId(Integer id){
        if(id != null) {
            Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
            if(productEntityOptional.isPresent()){
                productRepository.deleteById(id);
                return true;
            } else throw new ProductIdIsNotPresentException("Product id is not present");
        }
        throw new IllegalArgumentException("Id should not be null or empty");
    }
}
