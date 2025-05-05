package com.tranbinh.demo_shopapp.services;

import com.tranbinh.demo_shopapp.dtos.ProductDTO;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.entities.Product;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.repositories.CategoryRepository;
import com.tranbinh.demo_shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Lombok sẽ tự tạo constructor cho các final fields
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) throws Exception {
        // Kiểm tra xem tên sản phẩm đã tồn tại chưa
        if (productRepository.existsByName(productDTO.getName())){
            throw new DataIntegrityViolationException("Product name already exists");
        }

        // Tìm category theo categoryId từ DTO
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find category with id: " + productDTO.getCategoryId()));

        // Tạo đối tượng Product từ DTO và Category đã tìm thấy
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail()) // Cân nhắc xử lý file ảnh ở đây hoặc trước đó
                .description(productDTO.getDescription())
                .category(existingCategory) // Gán đối tượng Category
                .build();

        // Lưu sản phẩm mới vào database
        Product savedProduct = productRepository.save(newProduct);

        // Tạo ProductDTO để trả về cho client
        return mapProductToDTO(savedProduct);
    }

    @Override
    public Page<ProductDTO> getAllProducts(PageRequest pageRequest) {
        Page<Product> products = productRepository.findAll(pageRequest);
        return products.map(this::mapProductToDTO);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException, Exception {
        // Cần triển khai logic cập nhật
        // 1. Tìm Product theo id
        // 2. Kiểm tra xem tên sản phẩm mới có trùng với sản phẩm khác không
        // Chỉ kiểm tra nếu tên đã thay đổi và tên mới trùng với sản phẩm khác
        // 3. Kiểm tra xem Category mới có tồn tại không (nếu có thay đổi)
        // 4. Cập nhật các trường của Product từ ProductDTO
        // 5. Lưu lại Product đã cập nhật
        // 6. Trả về ProductDTO tương ứng
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + id));
        Category existingCategory = existingProduct.getCategory();
        if(existingCategory != null && !existingCategory.getId().equals(productDTO.getCategoryId())){
            existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find category with id: " + productDTO.getCategoryId()));
        }
        existingProduct.setCategory(existingCategory);
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        if(productDTO.getThumbnail() != null && !productDTO.getThumbnail().isEmpty()){
            existingProduct.setThumbnail(productDTO.getThumbnail());
        }
        Product updateProduct = productRepository.save(existingProduct);
        return mapProductToDTO(updateProduct);
    }

    @Override
    public void deleteProduct(Long id) throws DataNotFoundException, Exception {
        // Cần triển khai logic xóa
        // 1. Kiểm tra xem Product có tồn tại không
        // 2. Xóa Product
        // Có thể cần xem xét xóa mềm (đánh dấu là đã xóa) thay vì xóa cứng
        if(!productRepository.existsById(id)){
            throw new DataNotFoundException("Cannot find product with id: " + id);
        }
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error while deleting product with id: " + id + ".\nReason: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO getProductById(Long id) throws DataNotFoundException {
        // Cần triển khai logic lấy sản phẩm theo ID
        // 1. Tìm Product theo id
        // 2. Nếu không thấy, ném DataNotFoundException
        // 3. Chuyển Product thành ProductDTO và trả về
        Product existsProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        return mapProductToDTO(existsProduct);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    ProductDTO mapProductToDTO(Product product){
        return ProductDTO.builder()
                // .id(savedProduct.getId()) // ProductDTO hiện tại không có trường id, nếu cần thì thêm vào
                .name(product.getName())
                .price(product.getPrice())
                // .discount(product.getDiscount()) // Giữ lại discount từ DTO gốc nếu cần
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId()) // Lấy id từ đối tượng Category
                // .files(productDTO.getFiles()) // Không nên trả về files trong DTO kết quả
                .build();
    }
}