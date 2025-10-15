// fourpetals/com/controller/manager/ManagerCategoriesApi.java
package fourpetals.com.controller.manager;

import fourpetals.com.dto.request.categories.CategoryRequest;
import fourpetals.com.model.CategoryRowVM;
import fourpetals.com.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

@RestController
@RequestMapping("/api/manager/categories")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerCategoriesController {

  @Autowired private CategoryService service;

  @GetMapping
  public Page<CategoryRowVM> list(@RequestParam(defaultValue = "") String keyword,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "maDM,asc") String sort){
    Sort s = Sort.by(sort.split(",")[0]).ascending();
    if (sort.toLowerCase().endsWith(",desc")) s = s.descending();
    return service.search(keyword, page, size, s);
  }

  @GetMapping("/{id}")
  public CategoryRowVM get(@PathVariable Integer id){ return service.get(id).orElseThrow(); }

  @PostMapping
  public CategoryRowVM create(@RequestBody CategoryRequest req){ return service.create(req); }

  @PutMapping("/{id}")
  public CategoryRowVM update(@PathVariable Integer id, @RequestBody CategoryRequest req){
    return service.update(id, req);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id){ service.delete(id); }

  @GetMapping("/export")
  public void export(HttpServletResponse resp,
                     @RequestParam(defaultValue = "") String keyword) throws IOException {
    var page = service.search(keyword, 0, Integer.MAX_VALUE, Sort.by("maDM"));
    resp.setContentType("text/csv; charset=UTF-8");
    resp.setHeader("Content-Disposition", "attachment; filename=\"categories.csv\"");
    try (PrintWriter w = resp.getWriter()){
      w.println("MaDM,TenDM,MoTa,UpdatedAt");
      for (var r : page.getContent()){
        w.printf("%d,%s,%s,%s%n",
          r.maDM(), safe(r.tenDM()), safe(r.moTa()),
          r.updatedAt()==null? "" : r.updatedAt().toString());
      }
    }
  }
  private String safe(String s){ return s==null? "" : s.replace(',', ' '); }
}
