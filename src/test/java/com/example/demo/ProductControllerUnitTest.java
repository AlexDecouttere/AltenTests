package com.example.demo;

import com.example.demo.controller.ProductController;
import com.example.demo.entity.Product;
import com.example.demo.enumeration.InventoryStatus;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ProductControllerUnitTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        Product product = getProduct();
        product.setRating(4.2);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setCode("TEST1");
        product1.setName("Product 1");
        product1.setDescription("Desc 1");
        product1.setImage("img1.jpg");
        product1.setCategory("Cat 1");
        product1.setPrice(10.0);
        product1.setQuantity(1);
        product1.setInternalReference("ref1");
        product1.setShellId(1);
        product1.setInventoryStatus(InventoryStatus.INSTOCk);
        product1.setRating(1.0);
        products.add(product1);

        Product product2 = new Product();
        product2.setCode("TEST2");
        product2.setName("Product 2");
        product2.setDescription("Desc 2");
        product2.setImage("img2.jpg");
        product2.setCategory("Cat 2");
        product2.setPrice(20.0);
        product2.setQuantity(2);
        product2.setInternalReference("ref2");
        product2.setShellId(2);
        product2.setInventoryStatus(InventoryStatus.LOWSTOCK);
        product2.setRating(2.0);
        products.add(product2);

        when(productRepository.findAll()).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetProductByIdFound() {
        Product product = getProduct();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productController.getProductById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateProductFound() throws IntrospectionException {
        Product existingProduct = getProduct();

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ResponseEntity<Product> response = productController.updateProduct(1, updatedProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Product", response.getBody().getName());
    }

    @Test
    public void testUpdateProductNotFound() throws IntrospectionException {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(1, new Product());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1);

        ResponseEntity<HttpStatus> response = productController.deleteProduct(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private static Product getProduct() {
        Product existingProduct = new Product();
        existingProduct.setCode("TEST1234");
        existingProduct.setName("Test Product");
        existingProduct.setDescription("Test Description");
        existingProduct.setImage("test.jpg");
        existingProduct.setCategory("Test Category");
        existingProduct.setPrice(49.99);
        existingProduct.setQuantity(50);
        existingProduct.setInternalReference("TESTREF5678");
        existingProduct.setShellId(42);
        existingProduct.setInventoryStatus(InventoryStatus.OUTOFSTOCK);
        existingProduct.setRating(4.2);
        return existingProduct;
    }
}
