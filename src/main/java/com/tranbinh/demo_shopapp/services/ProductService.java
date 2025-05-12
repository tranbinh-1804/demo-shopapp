package com.tranbinh.demo_shopapp.services;

import com.tranbinh.demo_shopapp.dtos.ProductDTO;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.entities.Product;
import com.tranbinh.demo_shopapp.entities.ProductImage;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.repositories.CategoryRepository;
import com.tranbinh.demo_shopapp.repositories.ProductImageRepository;
import com.tranbinh.demo_shopapp.repositories.ProductRepository;
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

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) throws Exception {
        // Kiểm tra xem tên sản phẩm đã tồn tại chưa
        if (productRepository.existsByName(productDTO.getName())) {
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
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();

        // Lưu sản phẩm mới vào database
        Product savedProduct = productRepository.save(newProduct);

        List<String> savedImageUrls = new ArrayList<>();
        List<MultipartFile> files = productDTO.getFiles();

        if (files != null && !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file != null && !file.isEmpty()) {
                    if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                        throw new IllegalArgumentException(
                                "File size exceeds the limit of 10MB for file: " + file.getOriginalFilename());
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        throw new IllegalArgumentException(
                                "Invalid file type. Only image files are allowed for file: "
                                        + file.getOriginalFilename());
                    }
                    String fileName = fileStorageService.storeFile(file);
                    if (i == 0 && (savedProduct.getThumbnail() == null || savedProduct.getThumbnail().isEmpty())) {
                        // Chỉ đặt thumbnail nếu nó chưa được set từ productDTO ban đầu hoặc đây là ảnh đầu tiên
                        savedProduct.setThumbnail(fileName);
                        // Lưu lại product để cập nhật thumbnail nếu nó được set từ file đầu tiên
                        // Cân nhắc: Nếu productDTO.getThumbnail() đã có giá trị, có thể không muốn ghi đè ở đây.
                        // Hoặc chỉ cập nhật thumbnail nếu productDTO.getThumbnail() là rỗng.
                        // Hiện tại, nếu productDTO.getThumbnail() đã có, nó sẽ bị ghi đè bởi ảnh đầu tiên trong list files.
                        savedProduct = productRepository.save(savedProduct);
                    }
                    ProductImage productImage = ProductImage.builder()
                            .product(savedProduct)
                            .imageUrl(fileName)
                            .build();
                    productImageRepository.save(productImage);
                    savedImageUrls.add(fileName);
                }
            }
        }

        // Tạo ProductDTO để trả về cho client
        return mapProductToDTO(savedProduct, savedImageUrls);
    }

    @Override
    public Page<ProductDTO> getAllProducts(PageRequest pageRequest) {
        Page<Product> products = productRepository.findAll(pageRequest);
        return products.map(this::mapProductToDTO); // Sẽ gọi mapProductToDTO(Product product)
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + id));

        // Kiểm tra và cập nhật category
        Category existingCategory = existingProduct.getCategory();
        if (productDTO.getCategoryId() != null && (existingCategory == null
                || !existingCategory.getId().equals(productDTO.getCategoryId()))) {
            existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find category with id: " + productDTO.getCategoryId()));
            existingProduct.setCategory(existingCategory);
        }

        // Cập nhật các trường khác
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());

        // Xử lý thumbnail từ DTO (nếu có)
        // Nếu productDTO.getThumbnail() được cung cấp, nó sẽ được ưu tiên.
        // Nếu không, thumbnail hiện tại sẽ được giữ nguyên.
        // Logic xử lý file ảnh thumbnail mới (nếu có) cần được thêm ở đây nếu bạn muốn
        // cho phép cập nhật thumbnail qua file upload khi update.
        // Hiện tại, nó chỉ cập nhật thumbnail nếu productDTO.getThumbnail() là một chuỗi tên file.
        if (productDTO.getThumbnail() != null && !productDTO.getThumbnail().isEmpty()) {
            existingProduct.setThumbnail(productDTO.getThumbnail());
        }

        // Xử lý danh sách file ảnh mới (nếu có)
        List<MultipartFile> files = productDTO.getFiles();
        if (files != null && !files.isEmpty()) {
            // Cân nhắc: bạn có muốn xóa các ảnh cũ trước khi thêm ảnh mới không?
            // Hoặc chỉ thêm ảnh mới vào danh sách hiện có?
            // Logic này cần được định nghĩa rõ ràng.
            // Giả sử ở đây chúng ta chỉ thêm ảnh mới.
            List<String> newImageUrls = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file != null && !file.isEmpty()) {
                    if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                        throw new IllegalArgumentException(
                                "File size exceeds the limit of 10MB for file: " + file.getOriginalFilename());
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        throw new IllegalArgumentException(
                                "Invalid file type. Only image files are allowed for file: "
                                        + file.getOriginalFilename());
                    }
                    String fileName = fileStorageService.storeFile(file);
                    ProductImage productImage = ProductImage.builder()
                            .product(existingProduct)
                            .imageUrl(fileName)
                            .build();
                    productImageRepository.save(productImage);
                    newImageUrls.add(fileName);

                    // Cập nhật thumbnail nếu đây là ảnh đầu tiên được upload VÀ thumbnail chưa có
                    // hoặc người dùng muốn ưu tiên ảnh upload
                    if (i == 0 && (existingProduct.getThumbnail() == null ||
                            existingProduct.getThumbnail().isEmpty() ||
                            productDTO.getThumbnail() == null ||
                            productDTO.getThumbnail().isEmpty())) {
                        existingProduct.setThumbnail(fileName);
                    }
                }
            }
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return mapProductToDTO(updatedProduct); // Sẽ gọi mapProductToDTO(Product product) để lấy cả ảnh
    }

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

    @Override
    public ProductDTO getProductById(Long id) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
        return mapProductToDTO(existingProduct); // Sẽ gọi mapProductToDTO(Product product) để lấy cả ảnh
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    private ProductDTO mapProductToDTO(Product product) {
        if (product == null) {
            return null;
        }
        List<String> imageUrls = product.getProductImages().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
        return mapProductToDTO(product, imageUrls);
    }

    ProductDTO mapProductToDTO(Product product, List<String> imageUrls) {
        if (product == null) {
            return null;
        }
        ProductDTO.ProductDTOBuilder builder = ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription());

        if (product.getCategory() != null) {
            builder.categoryId(product.getCategory().getId());
        }

        builder.imageUrls(imageUrls == null ? new ArrayList<>() : imageUrls); // Đảm bảo imageUrls không bao giờ là null

        return builder.build();
    }
}