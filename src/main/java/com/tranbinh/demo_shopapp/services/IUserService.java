package com.tranbinh.demo_shopapp.services;

import com.tranbinh.demo_shopapp.dtos.UserDTO;
import com.tranbinh.demo_shopapp.entities.User;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    /**
     * Đăng ký người dùng mới.
     *
     * @param userDTO Dữ liệu người dùng từ request.
     * @return Người dùng đã được tạo.
     * @throws DataNotFoundException Nếu không tìm thấy vai trò (Role).
     * @throws DataIntegrityViolationException Nếu username hoặc số điện thoại đã tồn tại.
     * @throws Exception Các lỗi khác.
     */
    User createUser(UserDTO userDTO) throws DataNotFoundException, DataIntegrityViolationException, Exception;

    /**
     * Đăng nhập người dùng.
     *
     * @param username Tên đăng nhập.
     * @param password Mật khẩu.
     * @return Thông tin đăng nhập thành công (ví dụ: token).
     * @throws Exception Nếu đăng nhập thất bại (sai username/password, tài khoản bị khóa,...).
     */
    String login(String username, String password) throws Exception;
}
