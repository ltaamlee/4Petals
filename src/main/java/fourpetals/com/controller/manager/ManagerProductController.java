package fourpetals.com.controller.manager;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import fourpetals.com.entity.Product;
import fourpetals.com.enums.ProductStatus;
import fourpetals.com.repository.CategoryRepository;
import fourpetals.com.repository.SupplierRepository;
import fourpetals.com.service.ProductService;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/manager/products")
public class ManagerProductController {
  private final ProductService productService;
  private final CategoryRepository categoryRepo;
  private final SupplierRepository supplierRepo;

  public ManagerProductController(ProductService productService,
                                  CategoryRepository categoryRepo,
                                  SupplierRepository supplierRepo) {
    this.productService = productService;
    this.categoryRepo = categoryRepo;
    this.supplierRepo = supplierRepo;
  }


  @GetMapping
  public String index(@RequestParam(required=false) String q,
                      @RequestParam(required=false) Integer catId,
                      @RequestParam(required=false) Integer supId,
                      Model model) {
    model.addAttribute("products", productService.search(q, catId, supId));
    model.addAttribute("total", productService.total());
    model.addAttribute("q", q);
    model.addAttribute("catId", catId);
    model.addAttribute("supId", supId);
    model.addAttribute("cats", categoryRepo.findAll());
    model.addAttribute("sups", supplierRepo.findAll());
    return "manager/product/index";
  }

  @GetMapping("/add")
  public String add(Model model){
    model.addAttribute("product", new Product());
    bindLists(model);
    return "manager/product/form";
  }

  @GetMapping("/{id}/edit")
  public String edit(@PathVariable Integer id, Model model){
    var p = productService.findById(id);
    if (p == null) return "redirect:/manager/products";
    model.addAttribute("product", p);
    bindLists(model);
    return "manager/product/form";
  }

  @PostMapping({"/add","/{id}/edit"})
  public String save(@ModelAttribute("product") Product form,
                     @RequestParam(name = "file", required = false) MultipartFile file) throws IOException {

    // Upload ảnh nếu có
    if (file != null && !file.isEmpty()){
      String fileName = LocalDateTime.now().toString().replace(":","-") + "_" +
                        StringUtils.cleanPath(file.getOriginalFilename());
      Path folder = Path.of("uploads", "product");
      Files.createDirectories(folder);
      Files.copy(file.getInputStream(), folder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
      form.setHinhAnh("/uploads/product/" + fileName);
    }

    form.updateStatusBasedOnStock();
    productService.save(form);
    return "redirect:/manager/products";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Integer id){
    productService.delete(id);
    return "redirect:/manager/products";
  }

  // đổi trạng thái nhanh (tuỳ chọn)
  @PostMapping("/{id}/status")
  @ResponseBody
  public String changeStatus(@PathVariable Integer id, @RequestParam ProductStatus status){
    var p = productService.findById(id);
    if (p == null) return "NOT_FOUND";
    p.setProductStatus(status);
    productService.save(p);
    return "OK";
  }

  private void bindLists(Model model){
    model.addAttribute("cats", categoryRepo.findAll());
    model.addAttribute("sups", supplierRepo.findAll());
    model.addAttribute("statuses", ProductStatus.values());
  }
}