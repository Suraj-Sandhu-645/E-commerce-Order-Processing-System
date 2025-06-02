package com.example.product_management.controller;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductDTO> createProductInDatabase(@RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.createAndSaveProduct(productDTO), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getProductDetails(){
        return new ResponseEntity<>(productService.getProductDetails(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductDetailsById(@PathVariable("id") int id){
        return new ResponseEntity<>(productService.getProductDetailsByProductId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProductDetailsById(@PathVariable("id") int id, @RequestBody ProductDTO productDto){
        return new ResponseEntity<>(productService.updateProductDetailsByProductId(id,productDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProductById(@PathVariable("id") int id){
        return new ResponseEntity<>(productService.deleteProductDetailsByProductId(id),HttpStatus.OK);
    }
}

//
//Global Exception Handling
//🔧 Use when:
//You want consistent error responses across the entire application.
//
//        You're handling common exceptions like:
//
//MethodArgumentNotValidException
//
//        HttpMessageNotReadableException
//
//DataIntegrityViolationException
//
//        AccessDeniedException
//
//CustomApplicationException used across services
//
//You want to centralize logic (logging, formatting, alerting).
//
//You want to avoid repeating the same logic in multiple places.


//Local Exception Handling
//🔧 Use when:
//You want to handle an exception specific to a controller, service, or method.
//
//You want to return custom messages for a specific API.
//
//You need different behavior in different modules or services.
//
//It’s a one-off case that doesn’t apply globally.
//
//🛠 How:
//Use try-catch blocks or @ExceptionHandler inside a controller.