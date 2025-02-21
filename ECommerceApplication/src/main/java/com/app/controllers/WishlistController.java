package com.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Product;
import com.app.entites.User;
import com.app.entites.Wishlist;
import com.app.services.WishlistService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.app.repositories.UserRepo;
import com.app.repositories.ProductRepo;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
class WishlistController {
    @Autowired
    private WishlistService wishlistService;
        
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/public/wishlist/{userId}")
    public List<Wishlist> getWishlist(@PathVariable Long userId) {
        return wishlistService.getWishlistByUserId(userId);
    }

    @PostMapping("/public/wishlist/add")
    public void addToWishlist(
        @RequestParam Long userId,
        @RequestParam Long productId) {

        Optional<User> user = userRepo.findById(userId);
        Optional<Product> product = productRepo.findById(productId);
        
        if (user.isPresent() && product.isPresent()) {
            wishlistService.addToWishlist(user.get(), product.get());
        } else {
            throw new RuntimeException("User or Product not found");
        }
    }

    @DeleteMapping("/public/wishlist/remove")
    public void removeFromWishlist(
        @RequestParam Long userId,
        @RequestParam Long productId) {

        wishlistService.removeFromWishlist(userId, productId);
    }
}