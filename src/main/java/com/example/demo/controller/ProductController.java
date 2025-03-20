package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Optional<Product> productData = productRepository.findById(id);

        return productData.map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product productDetails) throws IntrospectionException {
        if (isAdmin()){
            Optional<Product> productData = productRepository.findById(id);

            if (productData.isPresent()) {
                Product product = productData.get();
                BeanUtils.copyProperties(productDetails, product, getIgnoredProperties(productDetails));
                product.setUpdatedAt(Instant.now());
                return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") int id) {
        if (isAdmin()) {
            try {
                productRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (isAdmin()) {
            try {
                // Set createdAt and updatedAt timestamps
                Instant now = Instant.now();
                product.setCreatedAt(now);
                product.setUpdatedAt(now);

                // Save the product to the database
                Product savedProduct = productRepository.save(product);

                return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
            } catch (Exception e) {
                // Handle exceptions (e.g., database errors)
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private String[] getIgnoredProperties(Object source) throws IntrospectionException {
        return Arrays.stream(Introspector.getBeanInfo(source.getClass(), Object.class)
                        .getPropertyDescriptors())
                .map(PropertyDescriptor::getName)
                .filter(name -> name.equals("id") || name.equals("createdAt") || isNull(source, name))
                .toArray(String[]::new);
    }

    private boolean isNull(Object source, String propertyName) {
        try {
            return Objects.isNull(new PropertyDescriptor(propertyName, source.getClass()).getReadMethod().invoke(source));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAdmin(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email.equals("admin@admin.com")){
            System.out.println("email" +email);
            return true;
        }
        else{
            return false;
        }
    }
}
