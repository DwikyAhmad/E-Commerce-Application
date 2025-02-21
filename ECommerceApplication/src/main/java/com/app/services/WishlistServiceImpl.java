package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Wishlist;
import com.app.entites.WishlistItem;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.WishlistDTO;
import com.app.payloads.ProductDTO;
import com.app.repositories.WishlistRepo;
import com.app.repositories.WishlistItemRepo;
import com.app.repositories.ProductRepo;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private WishlistItemRepo wishlistItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public WishlistDTO addProductToWishlist(Long wishlistId, Long productId) {
        // Retrieve the wishlist by its ID
        Wishlist wishlist = wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId));
        
        // Retrieve the product by its ID
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Check if the product already exists in the wishlist
        WishlistItem wishlistItem = wishlistItemRepo.findWishlistItemByWishlistIdAndProductId(wishlistId, productId);
        if (wishlistItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the wishlist");
        }
        
        // Create and save the new wishlist item
        WishlistItem newWishlistItem = new WishlistItem();
        newWishlistItem.setWishlist(wishlist);
        newWishlistItem.setProduct(product);
        wishlistItemRepo.save(newWishlistItem);
        
        // Map the updated wishlist to its DTO representation
        WishlistDTO wishlistDTO = modelMapper.map(wishlist, WishlistDTO.class);
        List<ProductDTO> productDTOs = wishlist.getWishlistItems().stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());
        wishlistDTO.setProducts(productDTOs);
        
        return wishlistDTO;
    }

    @Override
    public WishlistDTO getWishlist(Long wishlistId) {
        Wishlist wishlist = wishlistRepo.findById(wishlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "wishlistId", wishlistId));
        
        WishlistDTO wishlistDTO = modelMapper.map(wishlist, WishlistDTO.class);
        List<ProductDTO> productDTOs = wishlist.getWishlistItems().stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());
        wishlistDTO.setProducts(productDTOs);
        return wishlistDTO;
    }

    @Override
    public List<WishlistDTO> getAllWishlists() {
        List<Wishlist> wishlists = wishlistRepo.findAll();
        if (wishlists.isEmpty()) {
            throw new APIException("No wishlist exists");
        }
        
        return wishlists.stream().map(wishlist -> {
            WishlistDTO wishlistDTO = modelMapper.map(wishlist, WishlistDTO.class);
            List<ProductDTO> productDTOs = wishlist.getWishlistItems().stream()
                    .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());
            wishlistDTO.setProducts(productDTOs);
            return wishlistDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public String deleteProductFromWishlist(Long wishlistId, Long productId) {
        WishlistItem wishlistItem = wishlistItemRepo.findWishlistItemByWishlistIdAndProductId(wishlistId, productId);
        if (wishlistItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        wishlistItemRepo.deleteWishlistItemByWishlistIdAndProductId(wishlistId, productId);

        return "Product " + wishlistItem.getProduct().getProductName() + " removed from the wishlist!";
    }
}
