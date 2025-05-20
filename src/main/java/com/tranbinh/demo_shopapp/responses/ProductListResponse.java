package com.tranbinh.demo_shopapp.responses;

import com.tranbinh.demo_shopapp.dtos.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response object for paginated product listings.
 * Used to encapsulate both the list of products and pagination information
 * when returning product search or listing results.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    /**
     * List of products matching the search criteria or page request.
     * Contains product details in DTO format.
     */
    private List<ProductResponse> productResponses;

    /**
     * Total number of pages available for the current search criteria.
     * Used for pagination controls.
     */
    private int totalPages;
}
