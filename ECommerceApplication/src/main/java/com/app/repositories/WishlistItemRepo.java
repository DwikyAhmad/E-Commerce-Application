package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.app.entites.WishlistItem;

public interface WishlistItemRepo extends JpaRepository<WishlistItem, Long> {

    // Find a wishlist item based on wishlist ID and product ID
    @Query("SELECT wi FROM WishlistItem wi WHERE wi.wishlist.wishlistId = ?1 AND wi.product.id = ?2")
    WishlistItem findWishlistItemByWishlistIdAndProductId(Long wishlistId, Long productId);
    
    // Delete a wishlist item based on wishlist ID and product ID
    @Modifying
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist.wishlistId = ?1 AND wi.product.id = ?2")
    void deleteWishlistItemByWishlistIdAndProductId(Long wishlistId, Long productId);
}
