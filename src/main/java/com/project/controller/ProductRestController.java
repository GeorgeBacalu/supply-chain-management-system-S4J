package com.project.controller;

import com.project.entity.Product;
import com.project.exception.InvalidProductException;
import com.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.project.constant.ClientConstants.INVALID_PRODUCT_FORMAT;

@RestController
@RequestMapping(value = "/api/client/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductRestController {

   private final ProductService productService;

   @GetMapping
   public ResponseEntity<List<Product>> getAllProducts() {
      return ResponseEntity.ok(productService.getAllProducts());
   }

   @GetMapping(value = "/{id}")
   public ResponseEntity<Product> getProductById(@PathVariable Long id) {
      return ResponseEntity.ok(productService.getProductById(id));
   }

   @Validated
   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
      if(bindingResult.hasErrors()) {
         throw new InvalidProductException(INVALID_PRODUCT_FORMAT, product);
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
   }
}
