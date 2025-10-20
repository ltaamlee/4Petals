(() => {
	// ==============================
	// 1. STATE VÀ BIẾN TOÀN CỤC (TRONG SCOPE)
	// ==============================
	let materialIndex = 1;
	let materialsData = {}; // Cache map maNL -> {donViTinh, ...}
	let STATUS_CACHE = []; // Cache danh sách trạng thái
	const API = '/api/manager/products';

	// ==============================
	// 2. HÀM HELPER CHUNG
	// ==============================

	/**
	 * Khởi tạo materialsData từ biến global
	 */
	function initMaterialsDataFromWindow() {
		try {
			const arr = Array.isArray(window.__MATERIALS__) ? window.__MATERIALS__ : [];
			materialsData = arr.reduce((acc, m) => {
				acc[String(m.maNL)] = m;
				return acc;
			}, {});
		} catch (e) {
			console.error("Lỗi khởi tạo materialsData:", e);
			materialsData = {};
		}
	}

	/**
	 * Thoát các ký tự HTML đặc biệt
	 */
	function escapeHtml(str) {
		if (str == null) return '';
		return String(str)
			.replace(/&/g, '&amp;')
			.replace(/</g, '&lt;')
			.replace(/>/g, '&gt;')
			.replace(/"/g, '&quot;')
			.replace(/'/g, '&#39;');
	}

	/**
	 * Tải danh sách trạng thái (có cache)
	 */
	async function loadStatuses() {
		if (STATUS_CACHE.length) return STATUS_CACHE;
		try {
			const r = await fetch('/api/manager/products/statuses');
			if (!r.ok) throw new Error("Không thể tải trạng thái");
			STATUS_CACHE = await r.json(); // [{value, text}]
			return STATUS_CACHE;
		} catch (e) {
			console.error(e);
			return [];
		}
	}

	/**
	 * Điền các option vào một <select> trạng thái
	 */
	function populateStatusSelect(selectEl, statuses, defaultValue = null) {
		if (!selectEl) return;
		selectEl.innerHTML = '';
		statuses.forEach(s => {
			const opt = document.createElement('option');
			opt.value = s.value;
			opt.textContent = s.text;
			if (defaultValue !== null && String(defaultValue) === String(s.value)) {
				opt.selected = true;
			}
			selectEl.appendChild(opt);
		});
	}

	/**
	 * Hiển thị ảnh preview khi chọn file
	 */
	window.previewProduct = function(event) {
		const file = event.target.files[0];
		if (!file) return;
		const reader = new FileReader();
		reader.onload = () => {
			const preview = document.getElementById('productPreview');
			if (preview) preview.src = reader.result;
		};
		reader.readAsDataURL(file);
	};

	// ==============================
	// 3. QUẢN LÝ MODAL (MỞ/ĐÓNG)
	// ==============================

	/**
	 * Mở modal
	 */
	window.openModal = function(modalId) {
		const modal = document.getElementById(modalId);
		if (!modal) return;
		modal.classList.add('show');
		document.body.classList.add('modal-open');
	};

	/**
	 * Đóng modal và reset form
	 */
	window.closeModal = function(modalId) {
		const modal = document.getElementById(modalId);
		if (!modal) return;

		modal.classList.remove('show');
		document.body.classList.remove('modal-open');
		modal.dataset.editingId = '';

		// Reset form
		const form = modal.querySelector('form');
		form?.reset();

		// Reset ảnh preview
		const preview = document.getElementById('productPreview');
		if (preview) preview.src = ''; // Giả sử có src mặc định hoặc trống

		// Reset bảng nguyên liệu
		const tbody = modal.querySelector('#materialsTable tbody');
		if (tbody) {
			tbody.innerHTML = '';
			materialIndex = 1; // Reset index
			addMaterialRow(); // Giữ lại 1 dòng trống
		}

		// Reset tiêu đề và nút
		const title = modal.querySelector('.modal-header h2');
		if (title) title.textContent = 'Thêm Sản Phẩm';
		const btnSubmit = modal.querySelector('.btn-submit');
		if (btnSubmit) btnSubmit.textContent = 'Thêm';
	};

	// ==============================
	// 4. QUẢN LÝ HÀNG NGUYÊN LIỆU
	// ==============================

	/**
	 * Cập nhật hiển thị đơn vị tính khi chọn nguyên liệu
	 */
	function updateUnitDisplay(selectElement) {
		const row = selectElement.closest("tr");
		if (!row) return;
		const materialId = selectElement.value;
		const unitCell = row.querySelector(".unit-display");
		if (!unitCell) return;

		// Ưu tiên lấy từ attribute data-unit của option
		const opt = selectElement.selectedOptions?.[0];
		const dataUnit = opt?.getAttribute('data-unit');
		
		unitCell.textContent = dataUnit || materialsData[materialId]?.donViTinh || "";
	}

	/**
	 * Clone một hàng nguyên liệu từ <template>
	 */
	function cloneMaterialRow(i) {
		const tpl = document.getElementById('material-row-template');
		if (!tpl) {
			console.error('Missing #material-row-template');
			return null;
		}
		const frag = tpl.content.cloneNode(true);
		const row = frag.querySelector('.material-row');

		// Thay __i__ trong các name
		row.querySelectorAll('[name]').forEach(el => {
			el.name = el.name.replace('__i__', i);
		});

		// Bind events
		const sel = row.querySelector("select[name*='materialId']");
		sel.addEventListener('change', () => updateUnitDisplay(sel));
		
		// Populate options cho select
		// (Template có thể không chứa sẵn options)
		if (sel.options.length <= 1) { // Chỉ có option "Chọn..."
			Object.values(materialsData).forEach(m => {
				const opt = document.createElement('option');
				opt.value = m.maNL;
				opt.textContent = m.tenNL;
				opt.dataset.unit = m.donViTinh || '';
				sel.appendChild(opt);
			});
		}
		
		row.querySelector('.btn-remove').addEventListener('click', () => {
			const rows = document.querySelectorAll("#materialsTable .material-row");
			if (rows.length > 1) {
				row.remove();
			}
		});

		// Set unit lần đầu (nếu option đầu có data-unit)
		updateUnitDisplay(sel);
		return row;
	}

	/**
	 * Thêm một hàng nguyên liệu vào bảng (có thể prefill data)
	 */
	window.addMaterialRow = function(prefill) {
		const tbody = document.querySelector("#materialsTable tbody");
		if (!tbody) return;

		const row = cloneMaterialRow(materialIndex);
		if (!row) return;

		if (prefill) {
			if (prefill.maNL != null) {
				const sel = row.querySelector("select[name*='materialId']");
				sel.value = String(prefill.maNL);
				updateUnitDisplay(sel); // Cập nhật DVT sau khi set value
			}
			if (prefill.soLuongCan != null || prefill.soLuong != null) {
				row.querySelector("input[type='number']").value = prefill.soLuongCan ?? prefill.soLuong ?? 1;
			}
		}

		tbody.appendChild(row);
		materialIndex++;
	};

	/**
	 * Xóa hàng nguyên liệu (hỗ trợ inline onclick cũ nếu có)
	 */
	window.removeMaterialRow = function(btn) {
		const row = btn.closest('tr.material-row');
		const rows = document.querySelectorAll("#materialsTable .material-row");
		if (row && rows.length > 1) {
			row.remove();
		}
	};

	// ==============================
	// 5. API CRUD SẢN PHẨM
	// ==============================

	/**
	 * Tải và hiển thị danh sách sản phẩm
	 */
	async function loadProducts() {
		const form = document.getElementById('searchFilterForm');
		const keyword = form?.querySelector('[name="keyword"]')?.value ?? '';
		const status = form?.querySelector('#statusFilter')?.value || '';
		const categoryId = form?.querySelector('#categoryFilter')?.value || '';

		const q = new URLSearchParams({
			keyword: keyword.trim(),
			status: status,
			categoryId: categoryId
		});

		const tbody = document.getElementById('productTableBody');
		if (!tbody) return;

		try {
			const r = await fetch(`${API}/no-paging?` + q.toString());
			if (!r.ok) throw new Error('Không tải được danh sách sản phẩm');
			const data = await r.json(); // List<ProductRowVM>

			tbody.innerHTML = ''; // Xóa nội dung cũ

			if (!data.length) {
				tbody.innerHTML = `<tr><td colspan="7" style="text-align:center">Không có dữ liệu</td></tr>`;
				return;
			}

			data.forEach(p => {
				const tr = document.createElement('tr');
				tr.dataset.id = p.maSP; // Thêm id vào tr để delegate
				tr.innerHTML = `
					<td>${p.maSP}</td>
					<td>${escapeHtml(p.tenSP ?? '')}</td>
					<td>${p.hinhAnh ? `<img src="${p.hinhAnh}" alt="${escapeHtml(p.tenSP)}" style="height:128px;border-radius:6px">` : ''}</td>
					<td class="text-right">${p.giaGoc ?? p.gia ?? ''}</td>
					<td>${escapeHtml(p.trangThaiText ?? '')}</td>
					<td class="text-right">
						<a href="javascript:void(0)" class="btn-edit" data-id="${p.maSP}">
							<i class="fas fa-edit"></i>
						</a>
						<button type="button" class="btn-del" data-id="${p.maSP}">
							<i class="fas fa-trash"></i>
						</button>
					</td>`;
				tbody.appendChild(tr);
			});
			// KHÔNG cần bind event ở đây, đã dùng event delegation ở init()
		} catch (e) {
			tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;color:red">${escapeHtml(e.message)}</td></tr>`;
		}
	}

	/**
	 * Mở modal Sửa và fill dữ liệu
	 */
	async function openEdit(id) {
		try {
			const [detailRes, statuses] = await Promise.all([
				fetch(`${API}/${id}`),
				loadStatuses()
			]);
			
			if (!detailRes.ok) throw new Error(`Không tải được chi tiết sản phẩm #${id}`);
			const p = await detailRes.json();

			openModal('addProductModal');
			const modal = document.getElementById('addProductModal');
			modal.dataset.editingId = id;

			// Đổi tiêu đề + nút
			modal.querySelector('.modal-header h2').textContent = 'Sửa Sản Phẩm';
			modal.querySelector('.btn-submit').textContent = 'Cập nhật';

			// Fill form
			document.getElementById('tenSP').value = p.tenSP || '';
			document.getElementById('donViTinh').value = p.donViTinh || '';
			document.getElementById('gia').value = p.gia || 0;
			document.getElementById('moTa').value = p.moTa || '';
			document.getElementById('danhMuc').value = p.maDM || '';
			
			// Ảnh preview
			const preview = document.getElementById('productPreview');
			if (preview) preview.src = p.hinhAnh || '';

			// Trạng thái
			const selStatus = document.getElementById('trangThai');
			populateStatusSelect(selStatus, statuses, p.trangThai);

			// Materials
			const tbody = document.querySelector('#materialsTable tbody');
			tbody.innerHTML = ''; // Xóa các dòng trống
			materialIndex = 1; // Reset index
			
			const materials = p.materials || [];
			if (materials.length > 0) {
				materials.forEach(line => {
					addMaterialRow({ 
						maNL: line.maNL, 
						soLuongCan: line.soLuongCan || line.soLuong || 1 
					});
				});
			} else {
				addMaterialRow(); // Thêm 1 dòng trống nếu không có nguyên liệu
			}
		} catch (e) {
			alert(e.message || e);
		}
	}

	/**
	 * Xóa sản phẩm
	 */
	function del(id) {
		if (!confirm(`Bạn có chắc muốn xóa sản phẩm #${id} không?`)) return;
		
		fetch(`${API}/${id}`, { method: 'DELETE' })
			.then(r => {
				if (!r.ok) throw new Error('Xóa thất bại');
				loadProducts(); // Tải lại danh sách sau khi xóa
			})
			.catch(err => alert(err.message || err));
	}

	/**
	 * Gắn sự kiện submit cho form (Thêm/Sửa)
	 */
	function bindFormSubmit() {
		const modal = document.getElementById('addProductModal');
		const form = modal?.querySelector('form');
		if (!form) return;

		form.addEventListener('submit', async (e) => {
			e.preventDefault();

			// 1. Lấy dữ liệu JSON
			const payload = {
				tenSP: form.querySelector('#tenSP').value.trim(),
				donViTinh: form.querySelector('#donViTinh').value.trim(),
				gia: Number(form.querySelector('#gia').value || 0),
				moTa: form.querySelector('#moTa').value || '',
				danhMucId: Number(form.querySelector('#danhMuc').value),
				trangThai: Number(form.querySelector('#trangThai').value),
				materials: Array.from(form.querySelectorAll('#materialsTable tbody tr.material-row'))
					.map(tr => ({
						maNL: Number(tr.querySelector("select[name*='materialId']").value),
						soLuongCan: Number(tr.querySelector("input[type='number']").value || 1)
					}))
					.filter(x => x.maNL > 0 && x.soLuongCan > 0)
			};

			const editingId = modal.dataset.editingId;
			const method = editingId ? 'PUT' : 'POST';
			const url = editingId ? `${API}/${editingId}` : API;

			try {
				// 2. Gửi JSON
				const r = await fetch(url, {
					method,
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify(payload)
				});

				if (!r.ok) {
					const errText = await r.text();
					throw new Error(errText || 'Lưu thất bại');
				}
				
				// 3. Xử lý kết quả để lấy ID (cho upload ảnh)
				let productId = editingId;
				if (!productId) {
					// Nếu là POST (tạo mới), cố gắng lấy ID từ response
					try {
						const resBody = await r.json();
						if (resBody && resBody.maSP) {
							productId = resBody.maSP;
						}
					} catch (jsonError) {
						// Có thể BE trả 201 Created với Location header
						const locationHeader = r.headers.get('Location');
						if (locationHeader) {
							const m = locationHeader.match(/\/api\/manager\/products\/(\d+)/);
							if (m) productId = m[1];
						}
					}
				}

				// 4. Upload ảnh (nếu có)
				const fileInput = document.querySelector('#file');
				const file = fileInput?.files?.[0];
				
				if (productId && file) {
					const fd = new FormData();
					fd.append('file', file);
					const up = await fetch(`${API}/${productId}/image`, {
						method: 'POST',
						body: fd
					});
					if (!up.ok) {
						// Vẫn coi là thành công nhưng cảnh báo
						alert('Lưu sản phẩm thành công, nhưng upload ảnh thất bại.');
					}
				}

				// 5. Đóng modal và tải lại danh sách
				closeModal('addProductModal');
				loadProducts();
				
			} catch (err) {
				alert(err.message || 'Đã xảy ra lỗi');
			}
		});
	}

	// ==============================
	// 6. KHỞI TẠO (INIT)
	// ==============================
	function init() {
		// 1. Tải cache nguyên liệu
		initMaterialsDataFromWindow();

		// 2. Bind sự kiện đóng modal
		document.addEventListener('click', (e) => {
			if (e.target.classList.contains('modal')) {
				closeModal(e.target.id);
			}
		});
		document.addEventListener('keydown', (e) => {
			if (e.key === 'Escape') {
				document.querySelectorAll('.modal.show').forEach(m => closeModal(m.id));
			}
		});

		// 3. Bind sự kiện click Sửa/Xóa (Event Delegation)
		document.addEventListener('click', (e) => {
			const editBtn = e.target.closest('.btn-edit');
			if (editBtn) {
				e.preventDefault();
				const id = editBtn.dataset.id;
				if (id) openEdit(id);
				return;
			}

			const delBtn = e.target.closest('.btn-del');
			if (delBtn) {
				e.preventDefault();
				const id = delBtn.dataset.id;
				if (id) del(id);
				return;
			}
		});

		// 4. Thêm 1 hàng nguyên liệu rỗng vào modal (đang ẩn)
		addMaterialRow();
		
		// 5. Tải trước cache trạng thái cho modal
		loadStatuses().then(statuses => {
			const selStatus = document.getElementById('trangThai');
			if (selStatus) populateStatusSelect(selStatus, statuses, 1); // Default: Đang bán (1)
		});

		// 6. Bind sự kiện cho form filter
		const formFilter = document.getElementById('searchFilterForm');
		formFilter?.addEventListener('submit', (e) => {
			e.preventDefault();
			loadProducts();
		});

		// 7. Bind sự kiện submit cho form modal
		bindFormSubmit();

		// 8. Tải danh sách sản phẩm ban đầu
		loadProducts();
	}

	// Chạy hàm init khi DOM đã sẵn sàng
	document.addEventListener('DOMContentLoaded', init);

})();