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
	const status = form.elements['status'].value;

	currentPage = page;

	const url = `/api/admin/suppliers?page=${page}&size=${pageSize}` +
		`&keyword=${encodeURIComponent(keyword)}` +
		`&materialId=${materialId}` +
		`&status=${status}`;

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

		// =================== PHẦN SỬA ĐỔI ===================
		const status = supplier.trangThai;

		const isActive = (status === 'ACTIVE');

		const isBlocked = (status === 'SUSPENDED');


		const row = document.createElement('tr');
		row.innerHTML = `
			<td>${supplier.maNCC}</td>
			<td>${supplier.tenNCC}</td>
			<td>${supplier.email ?? '—'}</td>
			<td>${supplier.sdt ?? '—'}</td>
			<td>${supplier.diaChi ?? '—'}</td>
			<td>${formattedDate}</td>
			<td class="toggle-cell">
			<label class="switch">
                <input type="checkbox" ${isActive ? 'checked' : ''} 
			           data-id="${supplier.maNCC}" 
			           onchange="toggleSupplierStatus(this)"
			           ${isBlocked ? 'disabled' : ''}> 
                <span class="slider round"></span>
			</label>

            <button class="btn-block" onclick="toggleSupplierBlock(this, ${supplier.maNCC})" 
                    data-blocked="${isBlocked}">
		        <i class="fas ${isBlocked ? 'fa-lock' : 'fa-lock-open'}"></i>
		    </button>
			</td>

			<td>
				<div class="action-buttons">
				<button class="btn-view" onclick="openSupplierDetailModal(${supplier.maNCC})">
				                        <i class="fas fa-eye"></i>
				                    </button>
				<button class="btn-edit" onclick="openEditSupplierModal(${supplier.maNCC})">
									    <i class="fas fa-edit"></i>
									</button>

					<button class="btn-delete" onclick="deleteSupplier(${supplier.maNCC})">
					                   <i class="fas fa-trash"></i>
					               </button>
				</div>
			</td>
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

// --- TOGGLE TRẠNG THÁI NHÀ CUNG CẤP ---


async function toggleSupplierStatus(checkbox) {
	const supplierId = checkbox.dataset.id;
	const newStatus = checkbox.checked ? 'ACTIVE' : 'INACTIVE';

	try {
		const res = await fetch(`/api/admin/suppliers/${supplierId}/status`, {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ status: newStatus })
		});

		if (!res.ok) {
			const errorText = await res.text();
			throw new Error(errorText || 'Cập nhật trạng thái thất bại');
		}

		await res.json();
		loadSuppliers(currentPage);

	} catch (err) {
		console.error('❌ Lỗi toggle status:', err);
		alert(`Cập nhật trạng thái thất bại: ${err.message}`);
		checkbox.checked = !checkbox.checked;
	}
}

async function toggleSupplierBlock(button, supplierId) {
	const currentlyBlocked = button.getAttribute('data-blocked') === 'true';
	const newStatus = currentlyBlocked ? 'ACTIVE' : 'SUSPENDED';

	if (!confirm(`Bạn có chắc muốn ${currentlyBlocked ? 'mở khóa' : 'khóa'} nhà cung cấp này?`)) {
		return;
	}

	try {
		const res = await fetch(`/api/admin/suppliers/${supplierId}/status`, {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ status: newStatus })
		});

		if (!res.ok) {
			const errorText = await res.text();
			throw new Error(errorText || 'Cập nhật block thất bại');
		}

		await res.json();
		alert(`${currentlyBlocked ? 'Mở khóa' : 'Khóa'} nhà cung cấp thành công!`);
		loadSuppliers(currentPage);

	} catch (err) {
		console.error('❌ Lỗi toggle block:', err);
		alert(`Cập nhật trạng thái block thất bại: ${err.message}`);
	}
}



// -------------------- XỬ LÝ LỖI VALIDATION --------------------
function clearErrors(isEdit = false) {
	// Xóa tất cả lỗi từng field
	document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
	// Xóa lỗi chung (nếu có)
	const generalErrorId = isEdit ? 'edit-form-error' : 'form-error';
	const generalError = document.getElementById(generalErrorId);
	if (generalError) generalError.textContent = '';
}

function displayErrors(errors, isEdit = false) {
	clearErrors(isEdit);
	if (!errors) return;

	if (typeof errors === 'string') {
		// Nếu là string, hiển thị vào div chung
		const generalErrorId = isEdit ? 'edit-form-error' : 'form-error';
		const generalError = document.getElementById(generalErrorId);
		if (generalError) {
			generalError.textContent = errors;
		} else {
			console.error('Lỗi: ', errors);
		}
		return;
	}

	for (const field in errors) {
		// Lấy tên field gốc (bỏ prefix kiểu user.tenNCC)
		let cleanField = field.replace(/^.*\./, "");

		// Nếu form edit, thêm prefix 'edit' vào đầu (tương ứng với div id trong edit form)
		const elementId = isEdit
			? 'edit' + cleanField.charAt(0).toUpperCase() + cleanField.slice(1) + '-error'
			: cleanField + '-error';

		const el = document.getElementById(elementId);
		if (el) {
			el.textContent = errors[field];
		} else {
			// Nếu không tìm thấy div riêng, đưa vào div chung
			const generalErrorId = isEdit ? 'edit-form-error' : 'form-error';
			const generalError = document.getElementById(generalErrorId);
			if (generalError) {
				generalError.textContent += errors[field] + '\n';
			} else {
				console.warn(`Không tìm thấy element hiển thị lỗi cho: #${elementId}`);
			}
		}
	}
}


// --- THÊM NHÀ CUNG CẤP ---
function createSupplier() {
	const form = document.getElementById('supplierForm');
	const errorDiv = document.getElementById('form-error'); // div chung hiển thị lỗi
	if (errorDiv) errorDiv.textContent = ''; // reset lỗi chung

	const materialSelects = document.querySelectorAll('#materialsTable tbody select');
	const materialIds = Array.from(materialSelects)
		.map(select => parseInt(select.value))
		.filter(id => !isNaN(id));

	const formData = {
		tenNCC: document.getElementById('tenNCC').value.trim(),
		diaChi: document.getElementById('diaChi').value.trim(),
		sdt: document.getElementById('sdt').value.trim(),
		email: document.getElementById('email').value.trim(),
		mota: document.getElementById('moTa').value.trim(),
		nhaCungCapNguyenLieu: materialIds
	};

	fetch('/api/admin/suppliers/add', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(formData)
	})
		.then(async response => {
			if (!response.ok) {
				// Lấy lỗi từ backend (text hoặc JSON)
				let text = await response.text();
				try {
					const json = JSON.parse(text);
					displayErrors(json); // hiển thị lỗi từng field
				} catch {
					if (errorDiv) errorDiv.textContent = text || 'Lỗi khi thêm nhà cung cấp';
				}
				throw new Error('Validation lỗi');
			}
			return response.json();
		})
		.then(data => {
			clearErrors(); // xóa lỗi nếu thành công
			alert("Thêm nhà cung cấp thành công!");
			closeModal('addSupplierModal');
			loadSuppliers();
			form.reset();
		})
		.catch(err => {
			console.error("❌ Lỗi khi thêm:", err);
		});
}



// XEM THÔNG TIN CHI TIẾT NHÀ CUNG CẤP
function openSupplierDetailModal(maNCC) {
	fetch(`/api/admin/suppliers/view/${maNCC}`)
		.then(response => response.json())
		.then(data => {
			document.getElementById('detailMaNCC').innerText = data.maNCC;
			document.getElementById('detailTenNCC').innerText = data.tenNCC;
			document.getElementById('detailEmail').innerText = data.email;
			document.getElementById('detailSdt').innerText = data.sdt;
			document.getElementById('detailDiaChi').innerText = data.diaChi;
			document.getElementById('detailCreatedAt').innerText = data.createdAt;
			document.getElementById('detailUpdatedAt').innerText = data.updatedAt;
			document.getElementById('detailMaterials').innerText =
				(data.nhaCungCapNguyenLieuNames || []).join(', ');

			openModal('viewSupplierModal');
		})
		.catch(error => console.error(error));
}


// CHỈNH SỬA NHÀ CUNG CẤP
function openEditSupplierModal(maNCC) {
	fetch(`/api/admin/suppliers/view/${maNCC}`)  //Lấy data từ view xem chi tiết
		.then(response => response.json())
		.then(data => {
			document.getElementById('editMaNCC').value = data.maNCC;
			document.getElementById('editTenNCC').value = data.tenNCC;
			document.getElementById('editDiaChi').value = data.diaChi;
			document.getElementById('editSdt').value = data.sdt;
			document.getElementById('editEmail').value = data.email;

			const tableBody = document.querySelector('#editMaterialsTable tbody');
			tableBody.innerHTML = '';

			// Nếu có nguyên liệu
			if (data.nhaCungCapNguyenLieu && data.nhaCungCapNguyenLieu.length > 0) {
				data.nhaCungCapNguyenLieu.forEach(matId => {
					const row = createEditMaterialRow(matId);
					tableBody.appendChild(row);
				});
			} else {
				const defaultRow = createEditMaterialRow();
				tableBody.appendChild(defaultRow);
			}

			openModal('editSupplierModal');

		})
		.catch(err => console.error("❌ Lỗi khi tải NCC:", err));
}


// TẠO DÒNG NGUYÊN LIỆU TRONG FORM EDIT
function createEditMaterialRow(selectedId = "") {
	const row = document.createElement("tr");
	row.classList.add("material-row");

	// Clone select từ bảng gốc
	const selectHTML = document.querySelector("#materialsTable select").outerHTML;
	const td1 = document.createElement("td");
	td1.innerHTML = selectHTML;

	const select = td1.querySelector("select");
	if (selectedId) select.value = selectedId;

	const td2 = document.createElement("td");
	const btnRemove = document.createElement("button");
	btnRemove.type = "button";
	btnRemove.classList.add("btn-remove");
	btnRemove.textContent = "Xóa";
	btnRemove.onclick = () => row.remove();
	td2.appendChild(btnRemove);

	row.appendChild(td1);
	row.appendChild(td2);
	return row;
}


// THÊM DÒNG NGUYÊN LIỆU TRONG EDIT FORM
function addEditMaterialRow() {
	const tableBody = document.querySelector("#editMaterialsTable tbody");
	tableBody.appendChild(createEditMaterialRow());
}


// -------------------- SUBMIT FORM EDIT NHÀ CUNG CẤP --------------------
document.getElementById('editSupplierForm').addEventListener('submit', async function(e) {
	e.preventDefault();

	// --- Lấy dữ liệu form ---
	const id = document.getElementById('editMaNCC').value;
	const tenNCC = document.getElementById('editTenNCC').value.trim();
	const diaChi = document.getElementById('editDiaChi').value.trim();
	const sdt = document.getElementById('editSdt').value.trim();
	const email = document.getElementById('editEmail').value.trim();
	const motaEl = document.getElementById('editMoTa');
	const mota = motaEl ? motaEl.value.trim() : '';

	const materialIds = Array.from(document.querySelectorAll('#editMaterialsTable tbody select'))
		.map(select => parseInt(select.value))
		.filter(n => !isNaN(n));

	const data = {
		maNCC: parseInt(id),
		tenNCC,
		diaChi,
		sdt,
		email,
		mota,
		nhaCungCapNguyenLieu: materialIds
	};

	console.log("Payload gửi lên backend:", JSON.stringify(data));

	// --- Xóa lỗi cũ ---
	clearErrors(true); // true = form edit

	try {
		const response = await fetch(`/api/admin/suppliers/edit/${id}`, {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(data)
		});

		if (!response.ok) {
			const text = await response.text();

			try {
				// nếu backend trả JSON lỗi validation
				const json = JSON.parse(text);
				displayErrors(json, true); // hiển thị lỗi form edit
			} catch {
				// nếu backend trả text, hiển thị lỗi chung
				const generalError = document.getElementById('edit-form-error');
				if (generalError) generalError.textContent = text || 'Cập nhật thất bại';
			}

			throw new Error('Validation lỗi');
		}

		const updated = await response.json();
		clearErrors(true); // xóa lỗi nếu thành công
		alert(`✅ Cập nhật thành công: ${updated.tenNCC}`);
		closeModal('editSupplierModal');
		loadSuppliers(currentPage);

	} catch (err) {
		console.error(err);
		// Lỗi đã hiển thị ra form, không cần alert
	}
});



// --- XÓA NHÀ CUNG CẤP ---
function deleteSupplier(maNCC) {
	if (!confirm("Bạn có chắc muốn xóa nhà cung cấp này không?")) {
		return;
	}

	fetch(`/api/admin/suppliers/${maNCC}`, {
		method: 'DELETE'
	})
		.then(response => {
			if (response.ok) {
				alert("Xóa nhà cung cấp thành công!");
				loadSuppliers(currentPage);
				const row = document.querySelector(`#supplierTable tr[data-id='${maNCC}']`);
				if (row) row.remove();
			} else {
				return response.text().then(text => { throw new Error(text); });
			}
		})
		.catch(error => {
			console.error(error);
			alert("Đã có lỗi xảy ra: " + error.message);
		});
}



// --- SỰ KIỆN KHI TRANG ĐƯỢC TẢI ---
document.addEventListener('DOMContentLoaded', () => {
	loadSuppliers(0);
	const form = document.getElementById('searchFilterForm');
	form.addEventListener('submit', e => {
		e.preventDefault();
		loadSuppliers(0);
	});

	['materialFilter', 'statusFilter'].forEach(id => {
		document.getElementById(id).addEventListener('change', () => loadSuppliers(0));
	});

	document.getElementById('supplierPagination').addEventListener('click', e => {
		e.preventDefault();
		const target = e.target.closest('a');
		if (target && target.dataset.page) {
			loadSuppliers(parseInt(target.dataset.page, 10));
		}
	});

	const addForm = document.getElementById('supplierForm');
	addForm.addEventListener('submit', e => {
		e.preventDefault();
		createSupplier();
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
