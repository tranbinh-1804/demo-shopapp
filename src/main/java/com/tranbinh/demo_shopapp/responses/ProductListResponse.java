package com.tranbinh.demo_shopapp.responses;

import com.tranbinh.demo_shopapp.dtos.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductDTO> products;
    private int totalPages;
}
