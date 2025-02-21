package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Wishlist;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserUserId(Long userId);
    void deleteByUserUserIdAndProductProductId(Long userId, Long productId);
}
