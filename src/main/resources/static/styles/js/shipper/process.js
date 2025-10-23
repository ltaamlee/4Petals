document.addEventListener("DOMContentLoaded", function() {

	window.kiemTraGhiChu = function(selectElement) {
		const maDH = selectElement.id.replace("status-", "");
		// Giữ nguyên tên biến ghiChuInput/note vì ID trong HTML là 'note-'
		const ghiChuInput = document.getElementById("note-" + maDH);
		const giaTri = selectElement.value; // Giá trị là DANG_GIAO, HOAN_TAT, HUY

		if (giaTri === "HUY") {
			ghiChuInput.required = true;
			// Cập nhật Placeholder để hiển thị rõ là Lý do thất bại
			ghiChuInput.placeholder = "❗ Bắt buộc nhập lý do thất bại"; 
			ghiChuInput.classList.add("border-danger");
		} else {
			ghiChuInput.required = false;
			// Đặt lại placeholder cho trường hợp khác
			ghiChuInput.placeholder = "Nhập ghi chú/lý do (nếu có)";
			ghiChuInput.classList.remove("border-danger");
		}
	}
	const orderForm = document.getElementById("orderForm");
		if (orderForm) {
			orderForm.addEventListener("submit", function(e) {
				console.log("--- Bắt đầu kiểm tra form submit ---"); // <-- DEBUG 1
				let hopLe = true;
				document.querySelectorAll("select.form-select").forEach(select => {
					const maDH = select.id.replace("status-", "");
					const note = document.getElementById("note-" + maDH);
					
					console.log(`Kiểm tra đơn hàng ${maDH}. Trạng thái: ${select.value}. Ghi chú: ${note.value.trim().length}`); // <-- DEBUG 2
					
					if (select.value === "HUY" && note.value.trim() === "") {
						note.classList.add("border-danger");
						hopLe = false;
					} else {
						note.classList.remove("border-danger");
					}
				});

				console.log(`Kết quả kiểm tra: hopLe = ${hopLe}`); // <-- DEBUG 3

				if (!hopLe) {
					e.preventDefault();
					// Thêm return false để đảm bảo ngăn chặn hành động mặc định của form trong mọi trường hợp
					alert("Nếu chọn 'Giao hàng thất bại' thì lý do không được để trống!");
					return false; // <-- THÊM DÒNG NÀY ĐỂ TĂNG CƯỜNG NGĂN CHẶN
				}
				
				console.log("Form hợp lệ, đang gửi lên server..."); // <-- DEBUG 4
			});
		}
	const btnCancel = document.getElementById("btnCancel");
	if (btnCancel) {
		btnCancel.addEventListener("click", function() {
			window.location.reload();
		});
	}
});