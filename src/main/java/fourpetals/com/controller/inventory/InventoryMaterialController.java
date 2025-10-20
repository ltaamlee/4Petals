package fourpetals.com.controller.inventory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fourpetals.com.entity.Material;
import fourpetals.com.entity.Supplier;
import fourpetals.com.repository.MaterialRepository;
import fourpetals.com.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;


//CHUYỂN SANG RESET CONTROLLER - DÙNG AJAX RENDER LÊN
@Controller
@RequestMapping("/api/inventory/materials")
public class InventoryMaterialController {
	
	// DÙNG SERVICE, TẤT CẢ ĐỀU GỌI HÀM TỪ SERVICE LÊN KHÔNG VIẾT BẰNG REPOSITORY - THAM KHẢO SUPPLIER
	@Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SupplierRepository supplierRepository;
    @GetMapping
    public String listMaterials(Model model) {
        List<Material> listMaterials = materialRepository.findAll();
        List<Supplier> listSuppliers = supplierRepository.findAll();

        model.addAttribute("listMaterials", listMaterials);
        model.addAttribute("listSuppliers", listSuppliers);

        return "inventory/dashboard"; 
    }

    //Thêm nguyên liệu mới
    @PostMapping("/add")
    public String addMaterial(@RequestParam("tenNL") String tenNL,
                              @RequestParam("donViTinh") String donViTinh){

        Material material = new Material();
        material.setTenNL(tenNL);
        material.setDonViTinh(donViTinh);

        materialRepository.save(material);
        return "redirect:/inventory/dashboard";
    }

    // 📌 Xóa nguyên liệu
    @PostMapping("/delete")
    public String deleteMaterial(@RequestParam("maNL") Integer maNL) {
        if (materialRepository.existsById(maNL)) {
            materialRepository.deleteById(maNL);
        }
        return "redirect:/inventory/materials";
    }
    // 💾 Cập nhật nguyên liệu
    @PostMapping("/edit")
    public String updateMaterial(@RequestParam("maNL") Integer maNL,
                                 @RequestParam(value = "tenNL", required = false) String tenNL,
                                 @RequestParam(value = "donViTinh", required = false) String donViTinh,
                                 @RequestParam(value = "soLuongGiam", required = false) Integer soLuongGiam) {

        Optional<Material> materialOpt = materialRepository.findById(maNL);

        if (materialOpt.isPresent()) {
            Material material = materialOpt.get();

            if (tenNL != null && !tenNL.trim().isEmpty()) {
                material.setTenNL(tenNL.trim());
            }

            if (donViTinh != null && !donViTinh.trim().isEmpty()) {
                material.setDonViTinh(donViTinh.trim());
            }

            // ✅ Nếu nhập số lượng cần giảm → trừ đi
            if (soLuongGiam != null && soLuongGiam > 0) {
                int soLuongHienTai = material.getSoLuongTon();
                int soLuongMoi = soLuongHienTai - soLuongGiam;

                if (soLuongMoi < 0) {
                    soLuongMoi = 0; // tránh âm
                }

                material.setSoLuongTon(soLuongMoi);
            }

            materialRepository.save(material);
        }

        return "redirect:/inventory/materials";
    }


}
