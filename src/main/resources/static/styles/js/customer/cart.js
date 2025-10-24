document.addEventListener("DOMContentLoaded", () => {

  // === Hàm cập nhật tổng tiền ===
  const updateSummary = () => {
    let subtotal = 0;

    document.querySelectorAll(".cart-item").forEach(item => {
      const checkbox = item.querySelector("input[type='checkbox']");
      const priceText = item.querySelector(".info p").innerText.replace(/[^\d]/g, '');
      const quantity = parseInt(item.querySelector(".quantity input").value);

      if (checkbox && checkbox.checked) {
        subtotal += parseFloat(priceText) * quantity;
      }
    });

    // Không còn VAT → chỉ hiển thị tổng
    const total = subtotal;

    const summary = document.querySelector(".summary");
    if (summary) {
      summary.querySelector(".subtotal").innerText = subtotal.toLocaleString("vi-VN") + " ₫";
      summary.querySelector(".total strong").innerText = total.toLocaleString("vi-VN") + " ₫";
    }
  };

  // === Gửi request cập nhật số lượng ===
  const updateQuantityDB = (id, quantity) => {
    fetch(`/cart/update?id=${id}&quantity=${quantity}`, { method: "POST" })
      .catch(err => console.error("Cập nhật giỏ hàng lỗi:", err));
  };

  // === Gửi request xóa sản phẩm ===
  const removeItemDB = (id) => {
    fetch(`/cart/remove?id=${id}`, { method: "POST" })
      .catch(err => console.error("Xóa sản phẩm lỗi:", err));
  };

  // === Tăng/giảm số lượng ===
  document.querySelectorAll(".plus, .minus").forEach(btn => {
    btn.addEventListener("click", e => {
      const input = e.target.closest(".quantity").querySelector("input");
      let qty = parseInt(input.value);
      if (e.target.classList.contains("plus")) qty++;
      else if (qty > 1) qty--;

      input.value = qty;
      updateSummary();
      updateQuantityDB(input.dataset.id, qty);
    });
  });

  // === Khi gõ số lượng trực tiếp ===
  document.querySelectorAll(".quantity input").forEach(inp => {
    inp.addEventListener("change", e => {
      let qty = parseInt(e.target.value);
      if (isNaN(qty) || qty < 1) qty = 1;
      e.target.value = qty;
      updateSummary();
      updateQuantityDB(inp.dataset.id, qty);
    });
  });

  // === Xóa sản phẩm ===
  document.querySelectorAll(".remove").forEach(btn => {
    btn.addEventListener("click", e => {
      const id = btn.dataset.id;
      btn.closest(".cart-item").remove();
      updateSummary();
      removeItemDB(id);
    });
  });

  // === Khi tích / bỏ chọn sản phẩm ===
  document.querySelectorAll(".cart-item input[type='checkbox']").forEach(cb => {
    cb.addEventListener("change", updateSummary);
  });

  // Cập nhật tổng ban đầu khi trang load
  updateSummary();
});

// cart.js
document.querySelector(".checkout").addEventListener("click", function (e) {
  e.preventDefault();

  const selectedIds = [];
  document.querySelectorAll(".cart-item input[type='checkbox']:checked").forEach(cb => {
    const id = cb.closest(".cart-item").querySelector("input[type='number']").dataset.id;
    selectedIds.push(id);
  });

  if (selectedIds.length === 0) {
    alert("Vui lòng chọn ít nhất 1 sản phẩm để thanh toán!");
    return;
  }

  document.getElementById("selectedIds").value = selectedIds.join(",");
  document.getElementById("checkoutForm").submit();
});

document.addEventListener("DOMContentLoaded", () => {
    const items = document.querySelectorAll(".cart-item");
    const subtotalEl = document.querySelector(".subtotal");
    const discountEl = document.querySelector(".discount-total");
    const totalEl = document.querySelector(".total strong");

    let subtotal = 0;
    let discountTotal = 0;

    items.forEach(item => {
        const priceText = item.querySelector(".info p").textContent.replace(/[^\d]/g, '');
        const discountText = item.querySelector(".discount span")?.textContent.replace(/[^\d]/g, '') || "0";
        const qty = parseInt(item.querySelector("input[type='number']").value);

        const price = parseInt(priceText);
        const discount = parseInt(discountText);

        subtotal += price * qty;
        discountTotal += discount * qty;
    });

    const total = subtotal - discountTotal;

    subtotalEl.textContent = subtotal.toLocaleString("vi-VN") + " ₫";
    discountEl.textContent = discountTotal.toLocaleString("vi-VN") + " ₫";
    totalEl.textContent = total.toLocaleString("vi-VN") + " ₫";
});

