// mapNL được Thymeleaf truyền vào trang
const mapNL = window.mapNguyenLieu || {}; // map mã phiếu nhập -> danh sách nguyên liệu

// Cập nhật dropdown Nguyên Liệu khi chọn Phiếu Nhập
function updateNguyenLieu() {
	const phieuSelect = document.getElementById("phieuNhap");
	if (!phieuSelect) return;

	const maPN = phieuSelect.value;
	const selectNL = document.getElementById("nguyenLieu");
	selectNL.innerHTML = '<option value=""> </option>';

	if (mapNL[maPN]) {
		mapNL[maPN].forEach(nl => {
			const opt = document.createElement("option");
			opt.value = nl.maNL;
			opt.textContent = nl.tenNL;
			selectNL.appendChild(opt);
		});
	}
}

// Gắn sự kiện onchange cho dropdown Phiếu Nhập
document.addEventListener("DOMContentLoaded", () => {
	const phieuSelect = document.getElementById("phieuNhap");
	if (phieuSelect) {
		phieuSelect.addEventListener("change", updateNguyenLieu);
		updateNguyenLieu(); // populate khi load trang
	}
});

// Hiển thị chi tiết Phiếu Nhập
function viewPhieuNhapDetail(maPN) {
	const tbody = document.getElementById("phieuNhap-detail-body");
	if (!tbody) return;

	// 🔥 Cập nhật tiêu đề modal (nếu cần, để hiển thị Mã PN)
	const modalTitle = document.getElementById("phieuNhapDetailLabel");
	if (modalTitle) {
		modalTitle.textContent = `Chi tiết phiếu nhập của phiếu nhập có mã: ${maPN}`;
	}


	tbody.innerHTML = ""; // Xóa nội dung cũ

	fetch(`/inventory/detail/${maPN}`)
		.then(response => response.json())
		.then(data => {
			if (data.length === 0) {
				tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">Không có chi tiết phiếu nhập</td></tr>`;
				return;
			}

			data.forEach((ct, index) => {
				const soLuong = Number(ct.soLuong);
				const giaNhap = Number(ct.giaNhap);
				const thanhTien = Number(ct.thanhTien ?? soLuong * giaNhap);

				// 🔥 Định dạng tiền tệ bằng toLocaleString('vi-VN')
				const giaNhapFormatted = giaNhap.toLocaleString('vi-VN');
				const thanhTienFormatted = thanhTien.toLocaleString('vi-VN');


				const tr = document.createElement("tr");
				tr.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${ct.nguyenLieu.tenNL}</td>
                    <td>${soLuong}</td>
                    <td>${giaNhapFormatted}</td>
                    <td>${thanhTienFormatted}</td>
                `;
				tbody.appendChild(tr);
			});
		})
		.catch(error => {
			console.error("Lỗi khi tải chi tiết phiếu nhập:", error);
			tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">Lỗi tải dữ liệu</td></tr>`;
		});

	const modalEl = document.getElementById('phieuNhapDetailModal');
	if (modalEl) {
		const modal = new bootstrap.Modal(modalEl);
		modal.show();
	}
}