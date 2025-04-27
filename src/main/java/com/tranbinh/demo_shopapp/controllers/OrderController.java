package com.tranbinh.demo_shopapp.controllers;

import com.tranbinh.demo_shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping( "")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try{
            return ResponseEntity.ok("Create order successfully\n"+ orderDTO);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<String> getOrderById(@Valid @PathVariable("user_id") Long user_id) {
        try{
            return ResponseEntity.ok("Get order with id = " + user_id);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@Valid  @PathVariable Long id,@Valid @RequestBody OrderDTO orderDTO) {
        try{
            return ResponseEntity.ok("Update order successfully with id = " + id);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id) {
        try{
            return ResponseEntity.ok("Delete order successfully with id = " + id);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
