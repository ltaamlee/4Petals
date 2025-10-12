package fourpetals.com.controller.manager;

import fourpetals.com.entity.Product;
import fourpetals.com.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager/products")
public class ManagerProductController {

    private final ProductService productService;

    // Constructor injection (bắt buộc để productService != null)
    public ManagerProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @PageableDefault(size = 10, sort = "id") Pageable pageable,
                       Model model) {
        Page<Product> page = productService.list(q, pageable); // <- line 32 của bạn
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "manager/product/list"; // đảm bảo tồn tại file templates/manager/product/list.html
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "manager/product/form";
    }

    @PostMapping
    public String create(@ModelAttribute Product product) {
        productService.create(product);
        return "redirect:/manager/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.get(id));
        return "manager/product/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Product product) {
        productService.update(id, product);
        return "redirect:/manager/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        productService.delete(id);
        return "redirect:/manager/products";
    }
}
