package fourpetals.com.dto;

import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;

import java.util.Optional;

public class UserMapping {

    public static UserDetailResponse toUserResponse(User user) {
        if (user == null) return null;

        UserDetailResponse dto = new UserDetailResponse();

        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setImageUrl(user.getImageUrl());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        // Mapping Role
        dto.setRoleName(
            Optional.ofNullable(user.getRole())
                    .map(role -> role.getRoleName())
                    .map(roleName -> roleName.getDisplayName())
                    .orElse("Chưa gán")
        );

        // Mapping FullName và Phone từ KhachHang hoặc NhanVien
        if (user.getKhachHang() != null) {
            dto.setFullName(user.getKhachHang().getHoTen());
            dto.setPhone(user.getKhachHang().getSdt());
        } else if (user.getNhanVien() != null) {
            dto.setFullName(user.getNhanVien().getHoTen());
            dto.setPhone(user.getNhanVien().getSdt());
        } else {
            dto.setFullName("N/A");
            dto.setPhone("Chưa cập nhật");
        }

        // Mapping Status bằng enum
        dto.setStatus(user.getUserStatus() != null ? user.getUserStatus() : UserStatus.INACTIVE);

        return dto;
    }
}
