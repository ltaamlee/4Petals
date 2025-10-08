package fourpetals.com.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fourpetals.com.entity.Inventory;
import fourpetals.com.entity.InventoryDetail;
import fourpetals.com.repository.InventoryDetailRepository;
import fourpetals.com.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

	@Autowired
    private InventoryRepository inventoryRepository;
    private InventoryDetailRepository inventoryDetailRepository;

    // ==================== CẬP NHẬT TỔNG TIỀN ====================
    @Transactional
    public void updateTotalAmount(Integer maPN) {
        Inventory inventory = inventoryRepository.findById(maPN)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu nhập có mã: " + maPN));

        BigDecimal tongTien = inventory.getChiTietPhieuNhaps().stream()
                .filter(ct -> ct.getThanhTien() != null)
                .map(InventoryDetail::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        inventory.setTongTien(tongTien);
        inventoryRepository.save(inventory);
    }

    // ==================== THÊM CHI TIẾT PHIẾU NHẬP ====================
    @Transactional
    public void addInventoryDetail(InventoryDetail detail) {
        inventoryDetailRepository.save(detail);
        updateTotalAmount(detail.getPhieuNhap().getMaPN());
    }

    // ==================== XÓA CHI TIẾT PHIẾU NHẬP ====================
    @Transactional
    public void deleteInventoryDetail(Integer maCTPN) {
        InventoryDetail detail = inventoryDetailRepository.findById(maCTPN)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết phiếu nhập có mã: " + maCTPN));

        Integer maPN = detail.getPhieuNhap().getMaPN();
        inventoryDetailRepository.delete(detail);
        updateTotalAmount(maPN);
    }

    // ==================== CẬP NHẬT CHI TIẾT PHIẾU NHẬP ====================
    @Transactional
    public void updateInventoryDetail(InventoryDetail detail) {
        inventoryDetailRepository.save(detail);
        updateTotalAmount(detail.getPhieuNhap().getMaPN());
    }
    
}