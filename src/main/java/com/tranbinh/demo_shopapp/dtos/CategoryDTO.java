package com.tranbinh.demo_shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO  {

    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    private String name;
}
