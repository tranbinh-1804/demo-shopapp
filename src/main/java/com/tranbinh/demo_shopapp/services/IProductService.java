package com.tranbinh.demo_shopapp.services;

import com.tranbinh.demo_shopapp.dtos.ProductDTO;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

public interface IProductService {
    /**
     * Tạo sản phẩm mới.
     *
     * @param productDTO Dữ liệu sản phẩm từ request.
     * @return DTO của sản phẩm đã được tạo.
     * @throws Exception Các lỗi khác (ví dụ: validation, lỗi lưu trữ).
     */
    ProductDTO createProduct(ProductDTO productDTO) throws Exception;

    /**
     * Cập nhật thông tin sản phẩm.
     *
     * @param id         ID của sản phẩm cần cập nhật.
     * @param productDTO Dữ liệu cập nhật.
     * @return DTO của sản phẩm đã được cập nhật.
     * @throws DataNotFoundException Nếu không tìm thấy sản phẩm với ID đã cho.
     * @throws Exception             Các lỗi khác.
     */
    ProductDTO updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException, Exception;

    /**
     * Xóa sản phẩm.
     *
     * @param id ID của sản phẩm cần xóa.
     * @throws DataNotFoundException Nếu không tìm thấy sản phẩm với ID đã cho.
     * @throws Exception             Các lỗi khác.
     */
    void deleteProduct(Long id) throws DataNotFoundException, Exception;

    /**
     * Lấy thông tin sản phẩm theo ID.
     *
     * @param id ID của sản phẩm.
     * @return DTO của sản phẩm.
     * @throws DataNotFoundException Nếu không tìm thấy sản phẩm với ID đã cho.
     */
    ProductDTO getProductById(Long id) throws DataNotFoundException;

    /**
     * Lấy danh sách tất cả sản phẩm theo phân trang.
     *
     * @param pageRequest Thông tin phân trang (số trang, kích thước trang).
     * @return Danh sách sản phẩm đã được phân trang.
     */
    Page<ProductDTO> getAllProducts(PageRequest pageRequest);

    /**
     * Kiểm tra xem sản phẩm có tồn tại với tên đã cho hay không.
     *
     * @param name Tên sản phẩm cần kiểm tra.
     * @return true nếu sản phẩm tồn tại, false nếu không tồn tại.
     */
    boolean existsByName(String name);
}