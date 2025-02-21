package com.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.User;
import com.app.entites.Wishlist;
import com.app.repositories.WishlistRepo;

@Service
public class WishlistServiceImpl implements WishlistService {
    @Autowired
    private WishlistRepo WishlistRepo;

    @Override
    public List<Wishlist> getWishlistByUserId(Long userId) {
        return WishlistRepo.findByUserUserId(userId);
    }

    @Override
    public void addToWishlist(User user, Product product) {
        Wishlist wishlist = new Wishlist(user, product);
        WishlistRepo.save(wishlist);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        WishlistRepo.deleteByUserUserIdAndProductProductId(userId, productId);
    }
}