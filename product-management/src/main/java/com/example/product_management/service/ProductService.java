package com.example.product_management.service;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.entity.ProductEntity;
import com.example.product_management.exception.MissingProductDataException;
import com.example.product_management.exception.ProductIdIsNotPresentException;
import com.example.product_management.incoming.InventoryUpdated;
import com.example.product_management.repository.ProductRepository;
import com.example.product_management.util.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    public ProductDTO createAndSaveProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.toEntity(productDTO);
        productEntity.setProductId(UUID.randomUUID().toString());
        productEntity = productRepository.save(productEntity);

        return productMapper.toDTO(productEntity);
    }


    public ProductDTO getProductDetailsByProductId(String id) {
        if(id != null) {
            Optional<ProductEntity> productEntity = productRepository.findByProductId(id);
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


    public ProductDTO updateProductDetailsByProductId(String id, ProductDTO productDTO) {
        if(id != null) {
            Optional<ProductEntity> productEntityOptional = productRepository.findByProductId(id);
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
        throw new IllegalArgumentException("Product id should not be null or empty");
    }

    public Boolean deleteProductDetailsByProductId(String id){
        if(id != null) {
            Optional<ProductEntity> productEntityOptional = productRepository.findByProductId(id);
            if(productEntityOptional.isPresent()){
                productRepository.deleteByProductId(id);
                return true;
            } else throw new ProductIdIsNotPresentException("Product id is not present");
        }
        throw new IllegalArgumentException("Product id should not be null or empty");
    }

    public void processInventoryUpdatedEvent(InventoryUpdated inventoryUpdated) {
        Optional<ProductEntity> productEntityOptional = productRepository.findByProductId(inventoryUpdated.getProductId());
        int stock = 0;
        if(productEntityOptional.isPresent()) {
            ProductEntity productEntity = productEntityOptional.get();
            if(productEntity.getStock() > inventoryUpdated.getQuantityChanged()){
                stock = productEntity.getStock() - inventoryUpdated.getQuantityChanged();
                productEntity.setStock(stock);
            } else{
                stock = inventoryUpdated.getQuantityChanged() - productEntity.getStock();
                productEntity.setStock(0);
            }

            productRepository.save(productEntity);
            log.info("Product stock is updated");
//            return stock;
        } else throw new ProductIdIsNotPresentException("Product id is not present");
    }
}
