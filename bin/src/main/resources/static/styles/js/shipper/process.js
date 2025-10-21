
document.addEventListener("DOMContentLoaded", function() {

	window.kiemTraGhiChu = function(selectElement) {
		const maDH = selectElement.id.replace("status-", "");
		const ghiChuInput = document.getElementById("note-" + maDH);
		const giaTri = selectElement.value; // Giá trị là DANG_GIAO, HOAN_TAT, HUY

		if (giaTri === "HUY") {
			ghiChuInput.required = true;
			ghiChuInput.placeholder = "❗ Bắt buộc nhập lý do thất bại";
			ghiChuInput.classList.add("border-danger");
		} else {
			ghiChuInput.required = false;
			ghiChuInput.placeholder = "Nhập ghi chú (nếu có)";
			ghiChuInput.classList.remove("border-danger");
		}
	}
	const orderForm = document.getElementById("orderForm");
	if (orderForm) {
		orderForm.addEventListener("submit", function(e) {
			let hopLe = true;
			document.querySelectorAll("select.form-select").forEach(select => {
				const maDH = select.id.replace("status-", "");
				const note = document.getElementById("note-" + maDH);
				if (select.value === "HUY" && note.value.trim() === "") {
					note.classList.add("border-danger");
					hopLe = false;
				} else {
					note.classList.remove("border-danger");
				}
			});

			if (!hopLe) {
				e.preventDefault();
				alert("Nếu chọn 'Giao hàng thất bại' thì ghi chú không được để trống!");
			}
		});
	}

	const btnCancel = document.getElementById("btnCancel");
	if (btnCancel) {
		btnCancel.addEventListener("click", function() {
			window.location.reload();
		});
	}
});