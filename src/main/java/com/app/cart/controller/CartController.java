package com.app.cart.controller;

import com.app.cart.dto.CartDTO;
import com.app.cart.entity.Cart;
import com.app.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/addItem", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public Cart addProductItem(@RequestBody CartDTO cartDTO) {
        try {
            return cartService.saveProducts(cartDTO);
        }catch (Exception e){

        }
        return cartService.saveProducts(cartDTO);
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Cart> getAll(){
        return cartService.findAll();
    }

    @RequestMapping(value = "/updateItem/{id}",method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public Cart updateProductItem(@RequestBody CartDTO cartDTO, @PathVariable("id") Long id) {
        return cartService.updateProduct(cartDTO, id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
    public void deleteProductItem(@PathVariable("id") Long ids) {
        cartService.deleteProduct(ids);
    }

//    @RequestMapping(method = RequestMethod.DELETE)
//    public void ClearCart(Object object) {
//        cartService.clearCart(object);
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkout/{id}")
    public void purchaseProducts(@PathVariable("id") Long id) {
        cartService.checkoutProducts(id);
    }
}
