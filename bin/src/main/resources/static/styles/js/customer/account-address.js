document.addEventListener("DOMContentLoaded", () => {
  const modal = document.getElementById("addressModal");
  const form = document.getElementById("addressForm");
  const title = document.getElementById("modalTitle");
  const cancelBtn = document.getElementById("cancelBtn");

  const maDC = document.getElementById("maDC");
  const hoTen = document.getElementById("hoTen");
  const sdt = document.getElementById("sdt");
  const diaChiChiTiet = document.getElementById("diaChiChiTiet");

  // 👉 Nút thêm mới
  const addBtn = document.querySelector(".btn-add-address");
  if (addBtn) {
    addBtn.addEventListener("click", e => {
      e.preventDefault();
      title.textContent = "Thêm địa chỉ mới";
      form.reset();
      maDC.value = "";
      modal.classList.remove("hidden");
    });
  }

  // 👉 Nút cập nhật
  document.querySelectorAll(".edit-address").forEach(btn => {
    btn.addEventListener("click", e => {
      e.preventDefault();
      title.textContent = "Cập nhật địa chỉ";

      // Lấy dữ liệu từ data-attribute
      maDC.value = btn.dataset.id;
      hoTen.value = btn.dataset.name;
      sdt.value = btn.dataset.phone;
      diaChiChiTiet.value = btn.dataset.detail;

      modal.classList.remove("hidden");
    });
  });

  // 👉 Đóng popup
  cancelBtn.addEventListener("click", () => modal.classList.add("hidden"));

  // 👉 Click ngoài modal để đóng
  modal.addEventListener("click", e => {
    if (e.target === modal) modal.classList.add("hidden");
  });
});
