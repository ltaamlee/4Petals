package fourpetals.com.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Lớp mô phỏng sản phẩm trong kho (test giao diện)
class Product {
    private String maSP;
    private String tenSP;
    private String donViTinh;
    private double donGia;
    private int soLuong;

    public Product(String maSP, String tenSP, String donViTinh, double donGia, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public String getMaSP() { return maSP; }
    public String getTenSP() { return tenSP; }
    public String getDonViTinh() { return donViTinh; }
    public double getDonGia() { return donGia; }
    public int getSoLuong() { return soLuong; }
}

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    // Trang dashboard chính
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Dữ liệu giả lập để test giao diện
        List<Product> listProducts = new ArrayList<>();
        listProducts.add(new Product("SP001", "Hoa hồng đỏ", "Bó", 120000, 30));
        listProducts.add(new Product("SP002", "Hoa tulip vàng", "Bó", 150000, 20));
        listProducts.add(new Product("SP003", "Hoa cúc trắng", "Bó", 80000, 50));
        listProducts.add(new Product("SP004", "Hoa ly hồng", "Cành", 90000, 25));

        model.addAttribute("listProducts", listProducts);
        return "inventory/dashboard"; // trỏ tới templates/inventory/dashboard.html
    }

    // Trang nhập hoa vào kho
    @GetMapping("/stores")
    public String addProductPage() {
        return "inventory/add"; // trỏ tới templates/inventory/add.html
    }

    // Trang danh sách đơn hàng
    @GetMapping("/orders")
    public String orderList() {
        return "inventory/orders"; // trỏ tới templates/inventory/orders.html
    }
}