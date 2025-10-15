package fourpetals.com.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import fourpetals.com.entity.Order;
import fourpetals.com.enums.OrderStatus;

public interface OrderService {
  List<Order> search(String q, OrderStatus status, LocalDate from, LocalDate to);
  Order findById(Integer id);
  Order save(Order o);          // tự tính lại tổng tiền
  void delete(Integer id);
  long countToday();
  long countByStatus(OrderStatus s);
  Map<LocalDate, Long> countByDate(int recentDays);
  Order changeStatus(Integer id, OrderStatus next);
  
  
  
  
  
}
