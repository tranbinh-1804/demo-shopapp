package com.tranbinh.demo_shopapp.controllers;

import com.tranbinh.demo_shopapp.dtos.OrderDTO;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.responses.ApiResponse;
import com.tranbinh.demo_shopapp.responses.order.OrderResponse;
import com.tranbinh.demo_shopapp.services.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        ApiResponse.error("Validation failed", HttpStatus.BAD_REQUEST, errorMessages)
                );
            }

            OrderDTO createOrderDTO = orderService.createOrder(orderDTO);
            OrderResponse orderResponse = OrderResponse.fromDTO(createOrderDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(orderResponse, "Order created successfully"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: " +
                            e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable("id") Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid order ID", HttpStatus.BAD_REQUEST));
            }
            OrderDTO orderDTO = orderService.getOrderById(id);
            OrderResponse orderResponse = OrderResponse.fromDTO(orderDTO);
            return ResponseEntity.ok(ApiResponse.success(orderResponse, "Order found successfully"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: " +
                            e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUserId(
            @PathVariable("user_id") Long user_id
    ) {
        try {
            if (user_id == null || user_id <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid user ID", HttpStatus.BAD_REQUEST));
            }
            List<OrderDTO> orderDTOS = orderService.getOrdersByUserId(user_id);
            List<OrderResponse> orderResponses = orderDTOS.stream()
                    .map(OrderResponse::fromDTO)
                    .collect(Collectors.toList());
            String message = orderResponses.isEmpty()
                    ? "No orders found for user ID" + user_id
                    : "Orders found";
            return ResponseEntity.ok(ApiResponse.success(orderResponses, message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: "
                            + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid order ID", HttpStatus.BAD_REQUEST));
            }

            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        ApiResponse.error("Validation failed", HttpStatus.BAD_REQUEST, errorMessages)
                );
            }
            OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
            OrderResponse orderResponse = OrderResponse.fromDTO(updatedOrder);

            return ResponseEntity.ok(ApiResponse.success(
                    orderResponse,
                    "Order updated successfully with id = " + id));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Internal server error: "
                            + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid order ID", HttpStatus.BAD_REQUEST));
            }
            orderService.deleteOrder(id);
            return ResponseEntity.ok(ApiResponse.success(
                    "Order with id " + id + "has been soft deleted.",
                    "Order deleted successfully"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }
}