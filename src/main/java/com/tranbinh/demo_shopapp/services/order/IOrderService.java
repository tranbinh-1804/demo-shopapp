package com.tranbinh.demo_shopapp.services.order;


import com.tranbinh.demo_shopapp.dtos.OrderDTO;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.responses.order.OrderResponse;

import java.util.List;

/**
 * Service interface for managing orders in the shop application.
 * Provides operations for creating, retrieving, updating, and deleting orders.
 */
public interface IOrderService {
    /**
     * Creates a new order in the system.
     *
     * @param orderDTO The order data transfer object containing order details
     * @return OrderResponse containing the created order information
     * @throws Exception if there's an error during order creation
     */
    OrderDTO createOrder(OrderDTO orderDTO) throws Exception;

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order to retrieve
     * @return OrderResponse containing the order information
     * @throws DataNotFoundException if the order is not found
     */
    OrderDTO getOrderById(Long id) throws DataNotFoundException;

    /**
     * Updates an existing order.
     *
     * @param id       The ID of the order to update
     * @param orderDTO The order data transfer object containing updated order details
     * @return OrderResponse containing the updated order information
     * @throws DataNotFoundException if the order is not found
     */
    OrderDTO updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    /**
     * Deletes an order from the system.
     *
     * @param id The ID of the order to delete
     * @throws DataNotFoundException if the order is not found
     */
    void deleteOrder(Long id) throws DataNotFoundException;

    /**
     * Retrieves all orders for a specific user.
     *
     * @param userId The ID of the user whose orders to retrieve
     * @return List of OrderDTO containing the user's orders
     */
    List<OrderDTO> getOrdersByUserId(Long userId) throws DataNotFoundException;

    /**
     * Retrieves all orders matching the search keyword.
     *
     * @param keyword The search term to filter orders
     * @return List of OrderResponse containing matching orders
     */
    List<OrderDTO> getOrdersByKeyword(String keyword);

    /**
     * Retrieves all orders with a specific status.
     *
     * @param status The status to filter orders by
     * @return List of OrderResponse containing orders with the specified status
     */
    List<OrderDTO> getOrdersByStatus(String status);
}
