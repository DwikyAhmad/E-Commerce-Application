package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.entites.Wishlist;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Long> {
    
    // Find a wishlist by the user's ID
    Wishlist findByUserUserId(Long userId);
    
    // Alternatively, you could add a query to fetch a wishlist by user email if needed:
    // @Query("SELECT w FROM Wishlist w WHERE w.user.email = ?1")
    // Wishlist findWishlistByEmail(String email);
}