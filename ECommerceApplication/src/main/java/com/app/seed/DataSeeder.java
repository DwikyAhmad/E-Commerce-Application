package com.app.seed;

import java.util.List;
import java.util.Random;

import com.app.repositories.RoleRepo;
import com.app.services.CategoryService;
import com.app.services.CouponService;
import com.app.services.OrderService;
import com.app.services.ProductService;
import com.app.services.UserService;
import com.app.services.WishlistService;
import com.github.javafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.config.AppConstants;
import com.app.entites.Brand;
import com.app.entites.Category;
import com.app.entites.Coupon;
import com.app.entites.Role;
import com.app.payloads.AddressDTO;
import com.app.payloads.UserDTO;
import com.app.entites.Product;
import com.app.repositories.CategoryRepo;
import com.app.repositories.UserRepo;
import com.app.repositories.WishlistRepo;
import com.app.entites.User;
import com.app.entites.Wishlist;
import com.app.repositories.ProductRepo;
import com.app.services.BrandService;
import com.app.services.CartService;
import org.springframework.stereotype.Service;

@Service
public class DataSeeder {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;
    
    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private WishlistService wishlistService;

    private final Faker faker = new Faker();

    public void seedAll() {
        seedRole();
        seedUser();
        seedCategory();
        seedCoupon();
        seedBrand();
        seedProduct();
        seedCart();
        seedOrder();
        seedWishlist();
    }

    public void seedRole() {
        Role adminRole = new Role();
        Role userRole = new Role();

        try {
            adminRole.setRoleId(AppConstants.ADMIN_ID);
            adminRole.setRoleName("ADMIN");

            userRole.setRoleId(AppConstants.USER_ID);
            userRole.setRoleName("USER");

            List<Role> roles = List.of(adminRole, userRole);

            List<Role> savedRoles = roleRepo.saveAll(roles);

            savedRoles.forEach(System.out::println);

        } catch (Exception e) {
            // do nothing
        }
    }

    public void seedUser() {
        for (int i = 0; i <= 20; i++) {
            try {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setCity(faker.address().city());
                addressDTO.setCountry(faker.address().country());
                addressDTO.setState(faker.address().state());
                addressDTO.setStreet(faker.address().streetName());
                addressDTO.setPincode(faker.random().nextInt(100000, 1000000).toString());

                String buildingName;
                do {
                    buildingName = faker.address().buildingNumber() + " " + faker.address().streetName();
                } while (buildingName.length() < 5);
                addressDTO.setBuildingName(buildingName);

                UserDTO userDTO = new UserDTO();
                int desiredLength = 6;
                String firstName;
                String lastName;

                do {
                    firstName = faker.name().firstName();
                } while (firstName.length() < desiredLength);

                do {
                    lastName = faker.name().lastName();
                } while (lastName.length() < desiredLength);

                userDTO.setFirstName(firstName);
                userDTO.setLastName(lastName);
                userDTO.setMobileNumber(faker.random().nextInt(1000000000, 2000000000).toString());
                userDTO.setEmail(firstName.toLowerCase() + '.' + lastName.toLowerCase() + "@gmail.com");
                userDTO.setPassword(passwordEncoder.encode(faker.internet().password()));
                userDTO.setAddress(addressDTO);

                userService.registerUser(userDTO);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public void seedCategory() {
        for (int i = 0; i <= 20; i++) {
            try {
                Category category = new Category();
                category.setCategoryName(faker.commerce().department());
                categoryService.createCategory(category);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public void seedCoupon() {
        for (int i = 0; i < 20; i++) {
            try {
                String code = faker.code().ean8();
                Double discountPercentage = faker.number().randomDouble(2, 0, 100);
                
                couponService.createCoupon(code, discountPercentage);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public void seedBrand() {
        for (int i = 0; i <= 10; i++) {
            try {
                brandService.createBrand(faker.company().name());
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public void seedProduct() {
        List<Category> categories = categoryRepo.findAll();
        List<Brand> brands = brandService.getAllBrands();
        List<Coupon> coupons = couponService.getAllCoupons();
        for (int i = 0; i <= 30; i++) {
            try {
                Product product = new Product();
                product.setProductName(faker.commerce().productName());
                product.setDescription(faker.commerce().material());
                product.setImage("image.png");
                product.setPrice(faker.number().randomDouble(2, 100, 1000));
                product.setDiscount(faker.number().randomDouble(2, 0, 100));
                product.setSpecialPrice(product.getPrice() - product.getDiscount());
                product.setQuantity(faker.number().numberBetween(1, 100));

                int randomCategoryIndex = faker.random().nextInt(0, categories.size());
                Category randomCategory = categories.get(randomCategoryIndex);
                long index = randomCategory.getCategoryId();

                int randomBrandIndex = faker.random().nextInt(0, brands.size());
                Brand randomBrand = brands.get(randomBrandIndex);
                product.setBrand(randomBrand);

                int randomCouponIndex = faker.random().nextInt(0, coupons.size());
                Coupon randomCoupon = coupons.get(randomCouponIndex);
                product.setCoupon(randomCoupon);

                productService.addProduct(index, product);
            } catch (Exception e) {
                // do nothing
            }

        }
    }

    public void seedCart() {
        List<User> users = userRepo.findAll();
        List<Product> products = productRepo.findAll();

        for (User user : users) {
            for (int i = 0; i <= 5; i++) {
                try {
                    cartService.addProductToCart(user.getCart().getCartId(), products.get(i).getProductId(),
                            faker.number().numberBetween(1, 5));
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }

    public void seedOrder() {
        List<User> users = userRepo.findAll();
        Random random = new Random();
        
        for (User user : users) {
            if (random.nextBoolean()) {
                try {
                    String email = user.getEmail();
                    long cartId = user.getCart().getCartId();
                    String paymentMethod = faker.business().creditCardType();
    
                    orderService.placeOrder(email, cartId, paymentMethod);
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
        
    }

    public void seedWishlist() {
        List<User> users = userRepo.findAll();
        List<Product> products = productRepo.findAll();

        for (User user : users) {
            // Retrieve the wishlist for the user using the repository method
            Wishlist wishlist = wishlistRepo.findByUserUserId(user.getUserId());
            for (int i = 0; i <= 5; i++) {
                try {
                    wishlistService.addProductToWishlist(
                        wishlist.getWishlistId(),
                        products.get(i).getProductId()
                    );
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }   
}