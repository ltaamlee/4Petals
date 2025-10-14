let materialIndex = 1; // index cho các row thêm mới

function addMaterialRow() {
    const tableBody = document.querySelector('#materialsTable tbody');
    const newRow = document.createElement('tr');
    newRow.classList.add('material-row');

    // Clone options từ select mặc định
    const firstSelect = document.querySelector('.material-row.default-row select');
    const optionsHtml = Array.from(firstSelect.options)
        .map(opt => `<option value="${opt.value}">${opt.textContent}</option>`)
        .join('');

    newRow.innerHTML = `
        <td>
            <select name="materials[${materialIndex}].materialId" required>
                ${optionsHtml}
            </select>
        </td>
        <td>
            <button type="button" class="btn-remove" onclick="removeMaterialRow(this)">Xóa</button>
        </td>
    `;

    tableBody.appendChild(newRow);
    materialIndex++;
}

function removeMaterialRow(button) {
    const row = button.closest('tr');
    if (!row.classList.contains('default-row')) { // chỉ xóa row không phải mặc định
        row.remove();
        updateMaterialIndexes();
    }
}

// Cập nhật lại index sau khi xóa
function updateMaterialIndexes() {
    const rows = document.querySelectorAll('#materialsTable tbody tr.material-row');
    rows.forEach((row, idx) => {
        const select = row.querySelector('select');
        select.name = `materials[${idx}].materialId`;
    });
}





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