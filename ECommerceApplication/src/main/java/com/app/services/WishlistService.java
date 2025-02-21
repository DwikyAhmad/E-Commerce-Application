package com.app.services;

import java.util.List;
import com.app.entites.Product;
import com.app.entites.User;
import com.app.entites.Wishlist;

public interface WishlistService {
    List<Wishlist> getWishlistByUserId(Long userId);
    void addToWishlist(User user, Product product);
    void removeFromWishlist(Long userId, Long productId);
}
