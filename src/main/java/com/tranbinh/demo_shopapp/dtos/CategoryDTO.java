package com.tranbinh.demo_shopapp.dtos;

import com.tranbinh.demo_shopapp.validation.OnProductSubmission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @NotNull(message = "Category ID cannot be null", groups = OnProductSubmission.class)
    private Long id;

    @NotBlank(message = "Category name cannot be blank", groups = Default.class)
    private String name;
}
