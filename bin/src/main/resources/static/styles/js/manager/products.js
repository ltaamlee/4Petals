// ==============================
// 📦 1. PHẦN XỬ LÝ GIAO DIỆN & MODAL
// ==============================
(() => {
	let materialIndex = 1;
	let materialsData = {};

	// --- Khởi tạo dữ liệu nguyên liệu (server gửi xuống) ---
	window.initMaterialsData = function(data) {
		materialsData = data || {};
	};

	// --- Mở & Đóng modal ---
	window.openModal = function(modalId) {
		const modal = document.getElementById(modalId);
		if (!modal) return;
		modal.classList.add('show');
		document.body.classList.add('modal-open');
	};

	window.closeModal = function(modalId) {
		const modal = document.getElementById(modalId);
		if (!modal) return;
		modal.classList.remove('show');
		document.body.classList.remove('modal-open');
		modal.dataset.editingId = '';
	};

	// --- Click ngoài modal để đóng ---
	document.addEventListener('click', (e) => {
		if (e.target.classList.contains('modal')) closeModal(e.target.id);
	});

	// --- ESC để đóng ---
	document.addEventListener('keydown', (e) => {
		if (e.key === 'Escape') {
			document.querySelectorAll('.modal.show').forEach(m => closeModal(m.id));
		}
	});

	// --- Review ảnh sản phẩm ---
	window.previewProduct = function(event) {
		const file = event.target.files[0];
		if (!file) return;
		const reader = new FileReader();
		reader.onload = () => (document.getElementById('productPreview').src = reader.result);
		reader.readAsDataURL(file);
	};

	// --- Cập nhật đơn vị tính ---
	function updateUnitDisplay(selectElement) {
		const row = selectElement.closest("tr");
		const materialId = selectElement.value;
		const unitCell = row.querySelector(".unit-display");
		unitCell.textContent = materialsData[materialId]?.donViTinh || "";
	}

	// --- Thêm dòng nguyên liệu ---
	window.addMaterialRow = function() {
		const tableBody = document.querySelector("#materialsTable tbody");
		if (!tableBody) return;

		const templateRow = document.querySelector(".material-row");
		const newRow = templateRow.cloneNode(true);

		newRow.querySelectorAll("select, input").forEach(el => {
			if (el.name.includes("materials[0]")) {
				el.name = el.name.replace("materials[0]", `materials[${materialIndex}]`);
			}
			if (el.tagName === "INPUT") el.value = 1;
		});

		const select = newRow.querySelector("select[name*='materialId']");
		if (select) {
			select.addEventListener('change', () => updateUnitDisplay(select));
		}

		newRow.querySelector('.btn-remove')?.addEventListener('click', () => {
			const rows = document.querySelectorAll("#materialsTable .material-row");
			if (rows.length > 1) newRow.remove();
		});

		tableBody.appendChild(newRow);
		materialIndex++;
	};

	// --- Gán sự kiện khi load ---
	document.addEventListener('DOMContentLoaded', () => {
		document.querySelectorAll("#materialsTable select[name*='materialId']").forEach(sel => {
			sel.addEventListener('change', () => updateUnitDisplay(sel));
		});
	});
})();


// ==============================
// ⚙️ 2. PHẦN XỬ LÝ API CRUD SẢN PHẨM
// ==============================
(() => {
	const API = '/api/manager/products';
	let page = 0, size = 10, keyword = '';

	// --- Load danh sách sản phẩm ---
	function loadProducts() {
		const params = new URLSearchParams({ page, size, keyword });
		fetch(`${API}?${params}`)
			.then(r => r.ok ? r.json() : Promise.reject('Không tải được danh sách'))
			.then(data => {
				const tbody = document.getElementById('productTableBody');
				if (!tbody) return;
				tbody.innerHTML = '';

				(data.content || []).forEach(p => {
					const tr = document.createElement('tr');
					tr.innerHTML = `
						<td>${p.maSP}</td>
						<td>${p.tenSP}</td>
						<td>${p.donViTinh || ''}</td>
						<td class="text-right">${p.gia}</td>
						<td class="text-right">${p.soLuongTon || 0}</td>
						<td>${p.trangThai}</td>
						<td class="text-right">
							<button data-id="${p.maSP}" class="btn-edit">Sửa</button>
							<button data-id="${p.maSP}" class="btn-del">Xóa</button>
						</td>`;
					tbody.appendChild(tr);
				});

				tbody.querySelectorAll('.btn-edit').forEach(btn => btn.addEventListener('click', () => openEdit(btn.dataset.id)));
				tbody.querySelectorAll('.btn-del').forEach(btn => btn.addEventListener('click', () => del(btn.dataset.id)));
			})
			.catch(err => {
				const tbody = document.getElementById('productTableBody');
				if (tbody) tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;color:red">${err}</td></tr>`;
			});
	}

	// --- Mở modal để sửa ---
	function openEdit(id) {
		fetch(`${API}/${id}`)
			.then(r => r.ok ? r.json() : Promise.reject(`Không tải được sản phẩm #${id}`))
			.then(p => {
				openModal('addProductModal');
				document.getElementById('tenSP').value = p.tenSP || '';
				document.getElementById('donViTinh').value = p.donViTinh || '';
				document.getElementById('gia').value = p.gia || 0;
				document.getElementById('moTa').value = p.moTa || '';
				document.getElementById('danhMuc').value = p.maDM || '';

				const tbody = document.querySelector('#materialsTable tbody');
				tbody.innerHTML = '';
				(p.materials || []).forEach(line => {
					addMaterialRow();
					const last = tbody.querySelector('tr.material-row:last-child');
					last.querySelector('select').value = String(line.maNL);
					last.querySelector('input[type="number"]').value = line.soLuongCan || 1;
				});

				document.getElementById('addProductModal').dataset.editingId = id;
			})
			.catch(err => alert(err));
	}

	// --- Xóa sản phẩm ---
	function del(id) {
		if (!confirm('Xóa sản phẩm này?')) return;
		fetch(`${API}/${id}`, { method: 'DELETE' })
			.then(r => {
				if (!r.ok) throw new Error('Xóa thất bại');
				loadProducts();
			})
			.catch(err => alert(err));
	}

	// --- Submit form thêm/sửa sản phẩm ---
	function bindFormSubmit() {
		const modal = document.getElementById('addProductModal');
		const form = modal?.querySelector('form');
		if (!form) return;

		form.addEventListener('submit', (e) => {
			e.preventDefault();

			const tenSP = form.querySelector('#tenSP').value.trim();
			const donViTinh = form.querySelector('#donViTinh').value.trim();
			const gia = Number(form.querySelector('#gia').value || 0);
			const moTa = form.querySelector('#moTa').value || '';
			const danhMucId = Number(form.querySelector('#danhMuc').value);

			const rows = form.querySelectorAll('#materialsTable tbody tr.material-row');
			const materials = Array.from(rows).map(tr => {
				const sel = tr.querySelector('select');
				const qty = tr.querySelector('input[type="number"]');
				return { maNL: Number(sel.value), soLuongCan: Number(qty.value || 1) };
			}).filter(x => x.maNL);

			const payload = JSON.stringify({ tenSP, donViTinh, gia, moTa, danhMucId, materials });
			const fd = new FormData();
			fd.append('payload', payload);
			const file = form.querySelector('#file')?.files?.[0];
			if (file) fd.append('file', file);

			const editingId = modal.dataset.editingId;
			const method = editingId ? 'PUT' : 'POST';
			const url = editingId ? `${API}/${editingId}` : API;

			fetch(url, { method, body: fd })
				.then(async r => {
					if (!r.ok) throw new Error(await r.text());
					closeModal('addProductModal');
					loadProducts();
				})
				.catch(err => alert(err));
		});
	}

	// --- Init ---
	document.addEventListener('DOMContentLoaded', () => {
		loadProducts();
		bindFormSubmit();
	});
})();
