package com.app.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Cart;
import com.app.entites.Product;
import com.app.payloads.WishlistDTO;
import com.app.services.WishlistService;
import com.app.services.CartService;
import com.app.payloads.CartDTO;
import com.app.payloads.ProductDTO;
import com.app.payloads.ResponseDTO;
import com.app.entites.Wishlist;
import com.app.entites.User;
import com.app.services.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    // Add a product to a user's wishlist. Note that wishlist items don't require quantity.
    @PostMapping("/public/wishlists/{wishlistId}/products/{productId}")
    public ResponseEntity<WishlistDTO> addProductToWishlist(
            @PathVariable Long wishlistId, 
            @PathVariable Long productId) {
        WishlistDTO wishlistDTO = wishlistService.addProductToWishlist(wishlistId, productId);
        return new ResponseEntity<>(wishlistDTO, HttpStatus.CREATED);
    }

    // Retrieve all wishlists (typically for admin use).
    @GetMapping("/admin/wishlists")
    public ResponseEntity<List<WishlistDTO>> getAllWishlists() {
        List<WishlistDTO> wishlistDTOs = wishlistService.getAllWishlists();
        return new ResponseEntity<>(wishlistDTOs, HttpStatus.OK);
    }

    // Retrieve a specific user's wishlist.
    // Note: Even though the path contains the user's email, the service only needs the wishlist ID,
    // assuming one wishlist per user.
    @GetMapping("/public/users/{email}/wishlists/{wishlistId}")
    public ResponseEntity<WishlistDTO> getWishlistById(
            @PathVariable String email, 
            @PathVariable Long wishlistId) {
        WishlistDTO wishlistDTO = wishlistService.getWishlist(wishlistId);
        return new ResponseEntity<>(wishlistDTO, HttpStatus.OK);
    }

    // Remove a product from the wishlist.
    @DeleteMapping("/public/wishlists/{wishlistId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromWishlist(
            @PathVariable Long wishlistId, 
            @PathVariable Long productId) {
        String status = wishlistService.deleteProductFromWishlist(wishlistId, productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    // Add all items from wishlist to cart and clear the wishlist
    @PostMapping("/public/{email}/wishlists/{wishlistId}/moveToCart")
    public ResponseEntity<?> addWishlistItemsToCart(@PathVariable String email, @PathVariable Long wishlistId) {
        List<ProductDTO> products = wishlistService.getWishlist(wishlistId).getProducts();


        for (ProductDTO product : products) {
            cartService.addProductToCart(wishlistId, product.getProductId(), 1);
            wishlistService.deleteProductFromWishlist(wishlistId, product.getProductId());
        }

        CartDTO cart = cartService.getCart(email, wishlistId);

        String message = "All items from wishlist moved to cart";
        ResponseDTO response = new ResponseDTO(message, cart, 200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
