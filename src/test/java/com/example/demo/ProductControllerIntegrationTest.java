package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.enumeration.InventoryStatus;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateProduct() throws Exception {
        Product product = getProduct();

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = getProduct();

        Product savedProduct = productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + savedProduct.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = getProduct();

        Product savedProduct = productRepository.save(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/" + savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Product"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = getProduct();

        Product savedProduct = productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + savedProduct.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + savedProduct.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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