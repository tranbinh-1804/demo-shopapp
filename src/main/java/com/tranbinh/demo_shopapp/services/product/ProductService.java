package com.tranbinh.demo_shopapp.services.product;

import com.tranbinh.demo_shopapp.dtos.CategoryDTO;
import com.tranbinh.demo_shopapp.dtos.ProductDTO;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.entities.Product;
import com.tranbinh.demo_shopapp.entities.ProductImage;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.repositories.CategoryRepository;
import com.tranbinh.demo_shopapp.repositories.ProductImageRepository;
import com.tranbinh.demo_shopapp.repositories.ProductRepository;
import com.tranbinh.demo_shopapp.responses.product.ProductResponse;
import com.tranbinh.demo_shopapp.services.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final FileStorageService fileStorageService;

    /**
     * Creates a new product with the provided details.
     * This method handles the creation of a product including its images and thumbnail.
     *
     * @param productDTO DTO containing product information and optional image files
     * @return ProductDTO representing the created product with saved image URLs
     * @throws DataIntegrityViolationException if product name already exists
     * @throws DataNotFoundException           if specified category is not found
     * @throws IllegalArgumentException        if uploaded files are invalid (size > 10MB or non-image type)
     * @throws Exception                       for other unexpected errors during creation
     */
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws Exception {
        // Kiểm tra xem tên sản phẩm đã tồn tại chưa
        if (productRepository.existsByName(productDTO.getName())) {
            throw new DataIntegrityViolationException("Product name already exists");
        }

        if (productDTO.getCategory() == null || productDTO.getCategory().getId() == null) {
            throw new IllegalArgumentException("Category ID must be provided within the category object.");
        }

        // Tìm category theo categoryId từ DTO
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategory().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find category with id: " + productDTO.getCategory().getId()));

        // Tạo đối tượng Product từ DTO và Category đã tìm thấy
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();

        newProduct.setProductImages(new ArrayList<>());
        // Product savedProduct = productRepository.save(newProduct);

        List<MultipartFile> files = productDTO.getFiles();

        if (files != null && !files.isEmpty()) {
            if (files.size() > ProductImage.MAX_IMAGES_PER_PRODUCT) {
                throw new IllegalArgumentException("Maximum number of images allowed is 5");
            }
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file != null && !file.isEmpty()) {
                    validateImageFiles(file);
                    String fileName = fileStorageService.storeFile(file);
                    if (i == 0 && (newProduct.getThumbnail() == null || newProduct.getThumbnail().isEmpty())) {
                        // Chỉ đặt thumbnail nếu nó chưa được set từ productDTO ban đầu hoặc đây là ảnh đầu tiên
                        newProduct.setThumbnail(fileName);
                        // Lưu lại product để cập nhật thumbnail nếu nó được set từ file đầu tiên
                        // Cân nhắc: Nếu productDTO.getThumbnail() đã có giá trị, có thể không muốn ghi đè ở đây.
                        // Hoặc chỉ cập nhật thumbnail nếu productDTO.getThumbnail() là rỗng.
                        // Hiện tại, nếu productDTO.getThumbnail() đã có, nó sẽ bị ghi đè bởi ảnh đầu tiên trong list files.
                    }
                    ProductImage productImage = ProductImage.builder()
                            .product(newProduct)
                            .imageUrl(fileName)
                            .build();
                    newProduct.getProductImages().add(productImage);
                }
            }
        }
        return productRepository.save(newProduct);
    }

    /**
     * Retrieves a paginated list of all products.
     *
     * @param pageRequest Pagination and sorting parameters
     * @return Page of ProductDTO containing product information
     */
    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    /**
     * Updates an existing product with new information.
     * This method allows updating product details including name, price, description,
     * category, thumbnail and product images.
     *
     * @param id         The ID of the product to update
     * @param productDTO The DTO containing updated product information
     * @return Updated product converted to DTO
     * @throws DataNotFoundException    If product with given ID or category is not found
     * @throws IllegalArgumentException If uploaded files are invalid (size > 10MB or non-image type)
     * @throws Exception                For other unexpected errors during update
     */
    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + id));

        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }

        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }

        // Xử lý thumbnail từ DTO (nếu có)
        // Nếu productDTO.getThumbnail() được cung cấp, nó sẽ được ưu tiên.
        // Nếu không, thumbnail hiện tại sẽ được giữ nguyên.
        // Logic xử lý file ảnh thumbnail mới (nếu có) cần được thêm ở đây nếu bạn muốn
        // cho phép cập nhật thumbnail qua file upload khi update.
        // Hiện tại, nó chỉ cập nhật thumbnail nếu productDTO.getThumbnail() là một chuỗi tên file.
        if (productDTO.getThumbnail() != null && !productDTO.getThumbnail().isEmpty()) {
            existingProduct.setThumbnail(productDTO.getThumbnail());
        }

        if (productDTO.getCategory() != null && productDTO.getCategory().getId() != null) {
            Long newCategoryId = productDTO.getCategory().getId();
            Category newCategory = categoryRepository.findById(newCategoryId)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + newCategoryId));
            existingProduct.setCategory(newCategory);
        }


        // Xử lý danh sách file ảnh mới (nếu có)
        List<MultipartFile> files = productDTO.getFiles();

        if (files != null && !files.isEmpty()) {
            if (files.size() > ProductImage.MAX_IMAGES_PER_PRODUCT) {
                throw new IllegalArgumentException("Maximum number of images allowed is 5");
            }
            for(MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    validateImageFiles(file);
                    String fileName = fileStorageService.storeFile(file);
                    ProductImage productImage = ProductImage.builder()
                            .product(existingProduct)
                            .imageUrl(fileName)
                            .build();
                    existingProduct.getProductImages().add(productImage);
                }
            }
        }

        return productRepository.save(existingProduct);
    }

    /**
     * Deletes a product and its associated images.
     * This method removes the product from database and deletes associated image files from storage.
     *
     * @param id The ID of the product to delete
     * @throws DataNotFoundException if product with given ID is not found
     * @throws Exception             if there's an error during deletion
     */
    @Override
    public void deleteProduct(Long id) throws Exception {
        if (!productRepository.existsById(id)) {
            throw new DataNotFoundException("Cannot find product with id: " + id);
        }
        List<ProductImage> productImages = productImageRepository.findAllByProductId(id);
        for (ProductImage productImage : productImages) {
            try {
                fileStorageService.deleteFile(productImage.getImageUrl());
            } catch (IOException e) {
                System.err.println("Error while deleting file: " + productImage.getImageUrl()
                        + ". Reason: " + e.getMessage());
            }
        }
        productImageRepository.deleteAll(productImages);
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            // Log lỗi chi tiết hơn
            throw new Exception("Error while deleting product with id: " + id + ". Reason: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return ProductDTO containing product information
     * @throws DataNotFoundException if product with given ID is not found
     */
    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
    }

    /**
     * Checks if a product with the given name exists.
     *
     * @param name The product name to check
     * @return true if product exists, false otherwise
     */
    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }


    private void validateImageFiles(MultipartFile file) throws IllegalArgumentException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No files uploaded");
        }
        if (file.getSize() > ProductImage.MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException(
                    "File size exceeds the limit of " + (ProductImage.MAX_IMAGE_SIZE / (1024 * 1024))
                            + "MB for file: " + file.getOriginalFilename());
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(
                    "Invalid file type." + "Only images are allowed: " + file.getOriginalFilename());
        }
    }
}