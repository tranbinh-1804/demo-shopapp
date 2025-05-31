package com.tranbinh.demo_shopapp.services.order;

import com.tranbinh.demo_shopapp.dtos.OrderDTO;
import com.tranbinh.demo_shopapp.entities.Order;
import com.tranbinh.demo_shopapp.entities.User;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.repositories.OrderRepository;
import com.tranbinh.demo_shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found with id " + orderDTO.getUserId()));
        Order order = orderDTO.toEntity(user);
        orderRepository.save(order);
        return OrderDTO.fromEntity(order);
    }

    @Override
    public OrderDTO getOrderById(Long id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id " + id));
        return OrderDTO.fromEntity(order);
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id " + id));
        if (orderDTO.getUserId() != null && !orderDTO.getUserId().equals(existingOrder.getUser().getId())) {
            User newUser = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new DataNotFoundException("User not found with id " + orderDTO.getUserId()));
            existingOrder.setUser(newUser);
        }

        if (orderDTO.getFullName() != null) {
            existingOrder.setFullName(orderDTO.getFullName().trim());
        }
        if (orderDTO.getEmail() != null) {
            existingOrder.setEmail(orderDTO.getEmail().trim());
        }

        if (orderDTO.getAddress() != null) {
            existingOrder.setAddress(orderDTO.getAddress().trim());
        }

        if (orderDTO.getPhoneNumber() != null) {
            existingOrder.setPhoneNumber(orderDTO.getPhoneNumber().trim());
        }

        if (orderDTO.getNote() != null) {
            existingOrder.setNote(orderDTO.getNote().trim());
        }

        if (orderDTO.getTotalMoney() != null) {
            existingOrder.setTotalMoney(orderDTO.getTotalMoney());
        }

        if (orderDTO.getShippingMethod() != null) {
            existingOrder.setShippingMethod(orderDTO.getShippingMethod().trim());
        }

        if (orderDTO.getShippingAddress() != null) {
            existingOrder.setShippingAddress(orderDTO.getShippingAddress().trim());
        }

        if (orderDTO.getShippingDate() != null) {
            existingOrder.setShippingDate(orderDTO.getShippingDate());
        }

        if (orderDTO.getStatus() != null) {
            existingOrder.setStatus(orderDTO.getStatus());
        }

        if (orderDTO.getPaymentMethod() != null) {
            existingOrder.setPaymentMethod(orderDTO.getPaymentMethod().trim());
        }

        if (orderDTO.getTrackingNumber() != null) {
            existingOrder.setTrackingNumber(orderDTO.getTrackingNumber().trim());
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return OrderDTO.fromEntity(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with id " + id));
        existingOrder.setActive(false);
        orderRepository.save(existingOrder);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) throws DataNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with id " + userId));
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByKeyword(String keyword) {
        return List.of();
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) {
        return List.of();
    }
}
