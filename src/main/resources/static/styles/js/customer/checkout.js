document.addEventListener("DOMContentLoaded", () => {
	const tabs = document.querySelectorAll(".payment-tab");
	const qrBox = document.getElementById("qrBox");
	const methodInput = document.getElementById("paymentMethodInput");

	tabs.forEach(tab => {
		tab.addEventListener("click", () => {
			tabs.forEach(t => t.classList.remove("active"));
			tab.classList.add("active");
			const val = tab.querySelector("input").value;
			methodInput.value = val;
			qrBox.style.display = val === "QR" ? "block" : "none";
		});
	});
});

document.addEventListener("DOMContentLoaded", () => {
  const modal = document.getElementById("addressModal");
  const changeBtn = document.querySelector(".change-btn");
  const cancelBtn = document.getElementById("cancelAddressBtn");
  const confirmBtn = document.getElementById("confirmAddressBtn");
  const addForm = document.getElementById("addAddressForm");
  const openAddFormBtn = document.getElementById("openAddFormBtn");

  // mở popup
  changeBtn.addEventListener("click", e => {
    e.preventDefault();
    modal.classList.remove("hidden");
  });

  // đóng popup
  cancelBtn.addEventListener("click", () => modal.classList.add("hidden"));

  // mở form thêm địa chỉ
  openAddFormBtn.addEventListener("click", () => {
    addForm.classList.toggle("hidden");
  });

  // xác nhận chọn địa chỉ
  confirmBtn.addEventListener("click", () => {
    const selected = document.querySelector("input[name='selectedAddress']:checked");
    if (selected) {
      const item = selected.closest(".address-item");
      const name = item.querySelector("strong").textContent;
      const phone = item.querySelector("span").textContent;
      const addr = item.querySelector("p").textContent;

      document.querySelector(".name").textContent = name;
      document.querySelector(".phone").textContent = phone;
      document.querySelector(".address").textContent = addr;

      // update hidden form
      document.querySelector("input[name='tenNguoiNhan']").value = name;
      document.querySelector("input[name='sdt']").value = phone;
      document.querySelector("input[name='diaChi']").value = addr;
    }
    modal.classList.add("hidden");
  });
});
