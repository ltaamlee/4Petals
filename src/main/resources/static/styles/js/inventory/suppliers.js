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

let currentPage = 0;
const pageSize = 10;

// --- LOAD DANH SÁCH NHÀ CUNG CẤP ---
function loadSuppliers(page = 0) {
	const form = document.getElementById('searchFilterForm');
	const keyword = form.elements['keyword'].value;
	const materialId = form.elements['materialId'].value;

	currentPage = page;

	const url = `/api/inventory/suppliers?page=${page}&size=${pageSize}&keyword=${encodeURIComponent(keyword)}&materialId=${materialId}`;

	document.getElementById('supplierTableBody').innerHTML = '<tr><td colspan="8" style="text-align:center;">Đang tải dữ liệu...</td></tr>';
	document.getElementById('supplierPagination').innerHTML = '';

	fetch(url)
		.then(response => {
			if (!response.ok) throw new Error('Lỗi kết nối!');
			return response.json();
		})
		.then(data => {
			renderSupplierTable(data.content);
			renderPagination(data.number, data.totalPages);
		})
		.catch(error => {
			console.error('Lỗi khi tải nhà cung cấp:', error);
			document.getElementById('supplierTableBody').innerHTML = '<tr><td colspan="8" style="text-align:center;color:red;">Không thể tải dữ liệu nhà cung cấp. Vui lòng thử lại.</td></tr>';
		});
}

// --- RENDER BẢNG NHÀ CUNG CẤP ---
function renderSupplierTable(suppliers) {
	const tableBody = document.getElementById('supplierTableBody');
	tableBody.innerHTML = '';

	if (!suppliers || suppliers.length === 0) {
		tableBody.innerHTML = '<tr><td colspan="8" style="text-align:center;">Không có nhà cung cấp nào được tìm thấy</td></tr>';
		return;
	}

	suppliers.forEach(supplier => {
		const formattedDate = supplier.createdAt
			? new Date(supplier.createdAt).toLocaleDateString('vi-VN', {
				year: 'numeric', month: '2-digit', day: '2-digit',
				hour: '2-digit', minute: '2-digit'
			}).replace(',', '')
			: '—';

		const row = document.createElement('tr');
		row.innerHTML = `
			<td>${supplier.maNCC}</td>
			<td>${supplier.tenNCC}</td>
			<td>${supplier.email ?? '—'}</td>
			<td>${supplier.sdt ?? '—'}</td>
			<td>${supplier.diaChi ?? '—'}</td>
			<td>${formattedDate}</td>
			
		`;
	
		tableBody.appendChild(row);
	});
}

// --- PHÂN TRANG ---
function renderPagination(currentPage, totalPages) {
	const paginationDiv = document.getElementById('supplierPagination');
	paginationDiv.innerHTML = '';
	if (totalPages <= 1) return;

	let html = '';
	html += currentPage > 0
		? `<a href="#" data-page="${currentPage - 1}"> <i class="fas fa-chevron-left"></i> Trước </a>`
		: `<span class="disabled"> <i class="fas fa-chevron-left"></i> Trước </span>`;

	for (let i = 0; i < totalPages; i++) {
		const activeClass = i === currentPage ? 'active' : '';
		html += `<a href="#" data-page="${i}" class="${activeClass}">${i + 1}</a>`;
	}

	html += currentPage < totalPages - 1
		? `<a href="#" data-page="${currentPage + 1}"> Sau <i class="fas fa-chevron-right"></i> </a>`
		: `<span class="disabled"> Sau <i class="fas fa-chevron-right"></i> </span>`;

	paginationDiv.innerHTML = html;
}



// --- SỰ KIỆN KHI TRANG ĐƯỢC TẢI ---
document.addEventListener('DOMContentLoaded', () => {
	loadSuppliers(0);
	const form = document.getElementById('searchFilterForm');
	form.addEventListener('submit', e => {
		e.preventDefault();
		loadSuppliers(0);
	});

	document.getElementById('materialFilter').addEventListener('change', () => loadSuppliers(0));

	document.getElementById('supplierPagination').addEventListener('click', e => {
		e.preventDefault();
		const target = e.target.closest('a');
		if (target && target.dataset.page) {
			loadSuppliers(parseInt(target.dataset.page, 10));
		}
	});
});


// --- MỞ / ĐÓNG MODAL 
function openModal(modalId) {
	const modal = document.getElementById(modalId);
	if (!modal) {
		console.error('❌ Không tìm thấy modal: ' + modalId);
		return;
	}
	modal.classList.add('show');
	document.body.style.overflow = 'hidden';
}

function closeModal(modalId) {
	const modal = document.getElementById(modalId);
	if (!modal) return;
	modal.classList.remove('show');
	document.body.style.overflow = '';
}

document.addEventListener('click', function(event) {
	if (event.target.classList.contains('modal')) {
		const modalId = event.target.id;
		if (modalId) closeModal(modalId);
	}
});

document.addEventListener('keydown', function(event) {
	if (event.key === 'Escape') {
		const modals = document.querySelectorAll('.modal.show');
		modals.forEach(modal => closeModal(modal.id));
	}
});
