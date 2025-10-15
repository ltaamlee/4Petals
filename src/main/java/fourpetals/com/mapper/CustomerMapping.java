//package fourpetals.com.mapper;
//
//import fourpetals.com.dto.response.customer.*;
//import fourpetals.com.entity.Customer;
//import fourpetals.com.enums.CustomerRank;
//import fourpetals.com.enums.Gender;
//
//public final class CustomerMapping {
//
//  public static CustomerItemResponse toItem(Customer c) {
//    String email = c.getUser()!=null ? c.getUser().getEmail() : null;
//    String hang  = c.getHangThanhVien()!=null ? c.getHangThanhVien().displayName : null;
//    return new CustomerItemResponse(
//      c.getMaKH(), c.getHoTen(), c.getSdt(), email, c.getDiaChi(), hang, c.getNgaySinh()
//    );
//  }
//
//  public static CustomerDetailResponse toDetail(Customer c) {
//    String email = c.getUser()!=null ? c.getUser().getEmail() : null;
//    String hang  = c.getHangThanhVien()!=null ? c.getHangThanhVien().displayName : null;
//    String gt    = c.getGioiTinh()!=null ? c.getGioiTinh().name() : null;
//    return new CustomerDetailResponse(
//      c.getMaKH(), c.getHoTen(), c.getNgaySinh(), gt, c.getSdt(), c.getDiaChi(), hang, email
//    );
//  }
//
//  public static void applyUpdate(Customer c, String hoTen, java.time.LocalDate ngaySinh,
//                                 String gioiTinh, String sdt, String diaChi, String hang) {
//    if (hoTen != null) c.setHoTen(hoTen);
//    if (ngaySinh != null) c.setNgaySinh(ngaySinh);
//    if (gioiTinh != null && !gioiTinh.isBlank()) c.setGioiTinh(Gender.valueOf(gioiTinh));
//    if (sdt != null) c.setSdt(sdt);
//    if (diaChi != null) c.setDiaChi(diaChi);
//    if (hang != null && !hang.isBlank()) c.setHangThanhVien(CustomerRank.valueOf(hang));
//  }
//}
