package fourpetals.com.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import fourpetals.com.dto.request.products.ProductRequest;
import fourpetals.com.dto.response.products.ProductDetailResponse;
import fourpetals.com.service.ProductService;

@RestController
@RequestMapping("/api/manager/products")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerProductsController {

    @Autowired private ProductService productService;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public Page<ProductDetailResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer categoryId
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("maSP").descending());
        return productService.search(keyword, status, categoryId, pageable);
    }

    @GetMapping("/{maSP}")
    public ProductDetailResponse detail(@PathVariable Integer maSP){
        return productService.findById(maSP);
    }

    // ★ Create with multipart (payload + file)
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDetailResponse> create(
            @RequestPart("payload") String payload,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        ProductRequest req = mapper.readValue(payload, ProductRequest.class);
        return ResponseEntity.ok(productService.create(req, file));
    }

    // ★ Update with multipart (payload + file)
    @PutMapping(value="/{maSP}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDetailResponse> update(
            @PathVariable Integer maSP,
            @RequestPart("payload") String payload,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        ProductRequest req = mapper.readValue(payload, ProductRequest.class);
        return ResponseEntity.ok(productService.update(maSP, req, file));
    }

    @DeleteMapping("/{maSP}")
    public ResponseEntity<?> delete(@PathVariable Integer maSP){
        productService.delete(maSP);
        return ResponseEntity.ok().build();
    }
}
