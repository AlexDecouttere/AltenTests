package com.example.demo.controller;

import com.example.demo.Service.CartService;
import com.example.demo.entity.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestParam String email) {
        return ResponseEntity.ok(cartService.getCartByUser(email));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam String email, @RequestParam Long productId) {
        return ResponseEntity.ok(cartService.addToCart(email, productId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam String email, @RequestParam Long productId) {
        cartService.removeFromCart(email, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam String email) {
        cartService.clearCart(email);
        return ResponseEntity.ok("Cart cleared");
    }
}

