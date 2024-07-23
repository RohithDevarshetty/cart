package com.app.cart.controller;

import com.app.cart.dto.CartDTO;
import com.app.cart.dto.CartRequestDTO;
import com.app.cart.dto.CartResponseDTO;
import com.app.cart.entity.Cart;
import com.app.cart.exception.CartException;
import com.app.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Cart management APIs")
@Slf4j
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add a new product item to the cart.
     *
     * @param cartRequestDTO the DTO containing details of the product to add to the cart
     * @return the updated Cart object
     * @throws CartException if there is an issue adding the product to the cart
     */
    @Operation(
            summary = "Add a new product item to the cart",
            description = "Add a new product item to the cart using the provided CartRequestDTO"
    )
    @RequestMapping(value = "/add-item", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public CartDTO addProductItem(
            @Parameter(description = "DTO containing product details to be added to the cart", required = true)
            @RequestBody CartRequestDTO cartRequestDTO) {
        try {
            return cartService.addProducts(cartRequestDTO);
        } catch (CartException e) {
            log.error("Error while adding item from cart for : {}", cartRequestDTO, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error while adding item to cart for: {}", cartRequestDTO, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add product to cart");
        }
    }


    @RequestMapping(value = "/refresh/{userId}", method = RequestMethod.GET, produces = "application/json")
    public CartResponseDTO refreshCart(@Parameter(description = "ID of the user whose cart needs to be fetched", required = true) @PathVariable("userId") Long userId) {
        try {
            return cartService.refreshCart(userId);
        } catch (CartException e) {
            log.error("Error while refresh cart ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error fetching cart for userId: {}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch cart");
        }
    }

    /**
     * @param cartRequestDTO the DTO containing the details of the product to be updated int the cart
     * @return updated cart items
     */
    @Operation(summary = "Update product item in cart", description = "Update specific product item in the cart")
    @RequestMapping(value = "/update-item", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public CartDTO updateProductItem(
            @Parameter(description = "Cart DTO with updated details", required = true) @RequestBody CartRequestDTO cartRequestDTO) {
        try {
            return cartService.updateProduct(cartRequestDTO);
        } catch (CartException e) {
            log.error("Error while updating item from cart for : {}", cartRequestDTO, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error while updating cart for : {}", cartRequestDTO, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update cart");
        }

    }

    /**
     * Remove an item from the cart.
     *
     * @param cartRequestDTO the DTO containing the details of the product to be removed from the cart
     */
    @Operation(
            summary = "Remove an item from the cart",
            description = "Remove a specific item from the cart using the details provided in the CartRequestDTO"
    )
    @RequestMapping(method = RequestMethod.DELETE, value = "/remove-item", consumes = "application/json")
    public void removeItem(
            @Parameter(description = "Cart DTO with details", required = true) @RequestBody CartRequestDTO cartRequestDTO) {
        try {
            cartService.deleteProduct(cartRequestDTO);
        } catch (CartException e) {
            log.error("Error while removing item from cart for : {}", cartRequestDTO, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error while removing item from cart for : {}", cartRequestDTO, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete item from cart");
        }
    }

    /**
     * Clear all items from the cart for a specific user.
     *
     * @param userId the ID of the user whose cart is to be cleared
     */
    @Operation(
            summary = "Clear cart items for a user",
            description = "Removes all items from the cart of the user identified by the given user ID",
            parameters = @Parameter(name = "userId", description = "ID of the user whose cart is to be cleared", required = true)
    )
    @RequestMapping(value = "clear/{userId}", method = RequestMethod.DELETE)
    public void clearCart(
            @Parameter(description = "ID of the user whose cart is to be cleared", required = true) @PathVariable("userId") Long userId) {
        try {
            cartService.clearCart(userId);
        } catch (CartException e) {
            log.error("Error while clearing cart", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error while clearing cart for user : {}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to clear cart");
        }

    }

    /**
     * Checkout products in the cart.
     *
     * @param userId the id of the user whose cart items are to be checked out
     */
    @Operation(
            summary = "Checkout products in the cart",
            description = "Checkout all products in the cart for a specific user by the user id"
    )
    @RequestMapping(method = RequestMethod.POST, value = "/checkout/{userId}")
    public void purchaseProducts(
            @Parameter(description = "ID of the user whose cart items are to be checked out", required = true) @PathVariable("userId") Long userId) {
        try {
            cartService.checkoutProducts(userId);
        } catch (CartException e) {
            log.error("Error while checkout from cart", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error while checkout cart for user : {}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed while cart checkout");
        }
    }


}
