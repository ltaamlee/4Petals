let materialIndex = 1;
// Lưu trữ thông tin nguyên liệu khi trang tải
let materialsData = {};

// Khởi tạo dữ liệu nguyên liệu (nên được populate từ server)
function initMaterialsData(data) {
	materialsData = data;
}

function addMaterialRow() {
	const tableBody = document.querySelector("#materialsTable tbody");
	const newRow = document.querySelector(".material-row").cloneNode(true);

	newRow.querySelectorAll("select, input").forEach(el => {
		if (el.name.includes("materials[0]")) {
			el.name = el.name.replace("materials[0]", `materials[${materialIndex}]`);
		}
		if (el.tagName === "INPUT") el.value = 1;
	});

	// Gán sự kiện change cho select nguyên liệu mới
	const materialSelect = newRow.querySelector("select[name*='materialId']");
	if (materialSelect) {
		materialSelect.addEventListener('change', function() {
			updateUnitDisplay(this);
		});
	}

	tableBody.appendChild(newRow);
	materialIndex++;
}


// REVIEW ẢNH
function previewProduct(event) {
	const reader = new FileReader();
	reader.onload = function() {
		document.getElementById('productPreview').src = reader.result;
	}
	reader.readAsDataURL(event.target.files[0]);
}



function removeMaterialRow(button) {
	const rows = document.querySelectorAll("#materialsTable .material-row");
	if (rows.length > 1) button.closest("tr").remove();
}

// Cập nhật đơn vị tính khi chọn nguyên liệu
function updateUnitDisplay(selectElement) {
	const row = selectElement.closest("tr");
	const materialId = selectElement.value;
	const unitCell = row.querySelector("td:nth-child(3)");

	if (materialId && materialsData[materialId]) {
		const unit = materialsData[materialId].donViTinh;
		unitCell.textContent = unit || "N/A";
	} else {
		unitCell.textContent = "";
	}
}

// Gán sự kiện cho các select nguyên liệu đã tồn tại khi trang tải
document.addEventListener('DOMContentLoaded', function() {
	document.querySelectorAll("#materialsTable select[name*='materialId']").forEach(select => {
		select.addEventListener('change', function() {
			updateUnitDisplay(this);
		});
	});
});

// --- MỞ MODAL ---
function openModal(modalId) {
	const modal = document.getElementById(modalId);
	if (!modal) {
		console.error('❌ Không tìm thấy modal: ' + modalId);
		return;
	}
	modal.classList.add('show');
	document.body.style.overflow = 'hidden';
}

// --- ĐÓNG MODAL ---
function closeModal(modalId) {
	const modal = document.getElementById(modalId);
	if (!modal) return;
	modal.classList.remove('show');
	document.body.style.overflow = '';
}

// --- CLICK NGOÀI MODAL-CONTENT ĐỂ ĐÓNG (FIX: Xóa điều kiện sai) ---
document.addEventListener('click', function(event) {
	// Kiểm tra nếu click vào .modal (phần overlay, không phải modal-content)
	if (event.target.classList.contains('modal')) {
		const modalId = event.target.id;
		if (modalId) {
			closeModal(modalId);
		}
	}
});

// --- ESC ĐỂ ĐÓNG MODAL ---
document.addEventListener('keydown', function(event) {
	if (event.key === 'Escape') {
		const modals = document.querySelectorAll('.modal.show');
		modals.forEach(modal => closeModal(modal.id));
	}
});

