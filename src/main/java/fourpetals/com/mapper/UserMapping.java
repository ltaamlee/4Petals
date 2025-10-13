package fourpetals.com.mapper;

import fourpetals.com.dto.response.users.UserDetailResponse;
import fourpetals.com.entity.User;
import fourpetals.com.enums.UserStatus;
import java.util.Optional;

public final class UserMapping {
	private static final String DEFAULT_ROLE = "Chưa gán";
	private static final String DEFAULT_FULL_NAME = "N/A";
	private static final String DEFAULT_PHONE = "Chưa cập nhật";

	private UserMapping() {
	}

	public static UserDetailResponse toUserResponse(User user) {
		if (user == null) {
			return null;
		}

		UserDetailResponse dto = new UserDetailResponse();

		// Mapping các trường cơ bản
		dto.setUserId(user.getUserId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setImageUrl(user.getImageUrl());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setUpdatedAt(user.getUpdatedAt());

		// Mapping RoleName với xử lý null safety
		String roleName = Optional.ofNullable(user.getRole()).map(role -> role.getRoleName())
				.map(nameEnum -> nameEnum.getDisplayName()).orElse(DEFAULT_ROLE);
		dto.setRoleName(roleName);

		// Mapping FullName và Phone từ KhachHang hoặc NhanVien
		mapUserPersonalInfo(user, dto);

		// Mapping Status
		dto.setStatus(Optional.ofNullable(user.getUserStatus()).orElse(UserStatus.INACTIVE));

		return dto;
	}

	private static void mapUserPersonalInfo(User user, UserDetailResponse dto) {
		if (user.getKhachHang() != null) {
			dto.setFullName(extractFullName(user.getKhachHang().getHoTen()));
			dto.setPhone(extractPhone(user.getKhachHang().getSdt()));
		} else if (user.getNhanVien() != null) {
			dto.setFullName(extractFullName(user.getNhanVien().getHoTen()));
			dto.setPhone(extractPhone(user.getNhanVien().getSdt()));
		} else {
			dto.setFullName(DEFAULT_FULL_NAME);
			dto.setPhone(DEFAULT_PHONE);
		}
	}

	private static String extractFullName(String hoTen) {
		return (hoTen != null && !hoTen.trim().isEmpty()) ? hoTen.trim() : DEFAULT_FULL_NAME;
	}

	private static String extractPhone(String sdt) {
		return (sdt != null && !sdt.trim().isEmpty()) ? sdt.trim() : DEFAULT_PHONE;
	}
}