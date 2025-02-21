package com.app.services;

import java.util.List;
import com.app.payloads.WishlistDTO;

public interface WishlistService {
    
    WishlistDTO addProductToWishlist(Long wishlistId, Long productId);
    
    WishlistDTO getWishlist(Long wishlistId);
    
    List<WishlistDTO> getAllWishlists();
    
    String deleteProductFromWishlist(Long wishlistId, Long productId);
}
