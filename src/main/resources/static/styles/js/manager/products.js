// styles/js/manager/products.js
(() => {
	const API = '/api/manager/products'; // API phía Manager

	// ================== Modal ==================
	window.openModal = function(id) {
		const el = document.getElementById(id);
		if (!el) return;

		// reset form + nguyên liệu
		const form = el.querySelector('form');
		if (form && form.reset) form.reset();

		const tbody = el.querySelector('#materialsTable tbody');
		if (tbody) {
			tbody.innerHTML = '';
			addMaterialRow();
		}

		// bật modal (CSS của layout sẽ căn giữa)
		el.classList.add('show');
		document.body.classList.add('modal-open');

		// ensure không có inline margin khiến lệch tâm
		const box = el.querySelector('.modal-content');
		if (box) box.style.margin = '';
	};

	window.closeModal = function(id) {
		const el = document.getElementById(id);
		if (!el) return;
		el.classList.remove('show');
		document.body.classList.remove('modal-open');
		if (el.dataset) el.dataset.editingId = '';
	};

	// ================== Preview ảnh ==================
	window.previewProduct = (e) => {
		const file = e.target.files && e.target.files[0];
		const img = document.getElementById('productPreview');
		if (!file || !img) return;
		const fr = new FileReader();
		fr.onload = (ev) => (img.src = ev.target.result);
		fr.readAsDataURL(file);
	};

	// ================== Nguyên liệu ==================
	function selectHtmlTemplate() {
		// Ưu tiên danh sách từ server nếu đã đính vào window.__MATERIALS__
		if (Array.isArray(window.__MATERIALS__) && window.__MATERIALS__.length) {
			return [
				`<option value="">Chọn nguyên liệu</option>`,
				...window.__MATERIALS__.map(
					(m) =>
						`<option value="${m.maNL}" data-unit="${m.donViTinh || ''}">${m.tenNL}</option>`
				),
			].join('');
		}
		// Fallback: clone select đầu tiên đã render bằng Thymeleaf
		const firstSel = document.querySelector('#materialsTable tbody tr select');
		return firstSel ? firstSel.innerHTML : `<option value="">Chọn nguyên liệu</option>`;
	}

	function updateUnitDisplay(selectEl, unitSpan) {
		const opt = selectEl.options[selectEl.selectedIndex];
		let unit = (opt && (opt.dataset.unit || opt.getAttribute('data-unit'))) || '';

		if (!unit && Array.isArray(window.__MATERIALS__)) {
			const m = window.__MATERIALS__.find((x) => String(x.maNL) === selectEl.value);
			unit = m ? m.donViTinh || '' : '';
		}
		unitSpan.textContent = unit;
	}

	function reindexMaterialNames() {
		document
			.querySelectorAll('#materialsTable tbody tr.material-row')
			.forEach((tr, i) => {
				const sel = tr.querySelector('select[name^="materials["]');
				const qty = tr.querySelector('input[name^="materials["]');
				if (sel) sel.name = `materials[${i}].materialId`;
				if (qty) qty.name = `materials[${i}].soLuong`;
			});
	}

	window.addMaterialRow = function() {
		const tbody = document.querySelector('#materialsTable tbody');
		if (!tbody) return;

		const tr = document.createElement('tr');
		tr.className = 'material-row';
		tr.innerHTML = `
      <td>
        <select name="materials[0].materialId" required>${selectHtmlTemplate()}</select>
      </td>
      <td><input type="number" name="materials[0].soLuong" min="1" value="1" required></td>
      <td><span class="unit-display"></span></td>
      <td><button type="button" class="btn-remove">Xóa</button></td>
    `;
		tbody.appendChild(tr);

		const sel = tr.querySelector('select');
		const unitSpan = tr.querySelector('.unit-display');
		sel.addEventListener('change', () => updateUnitDisplay(sel, unitSpan));
		updateUnitDisplay(sel, unitSpan); // set ngay lần đầu

		tr.querySelector('.btn-remove').addEventListener('click', () => {
			tr.remove();
			reindexMaterialNames();
		});

		reindexMaterialNames();
	};

	// ================== Load danh sách sản phẩm ==================
	let page = 0,
		size = 10,
		keyword = '';
	function loadProducts() {
		const params = new URLSearchParams({ page, size, keyword });
		fetch(`${API}?${params}`)
			.then((r) => (r.ok ? r.json() : Promise.reject(new Error('Không tải được danh sách sản phẩm'))))
			.then((data) => {
				const tbody = document.getElementById('productTableBody');
				if (!tbody) return;
				tbody.innerHTML = '';
				(data.content || []).forEach((p) => {
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

				tbody.querySelectorAll('.btn-del').forEach((btn) => {
					btn.addEventListener('click', () => del(btn.dataset.id));
				});
				tbody.querySelectorAll('.btn-edit').forEach((btn) => {
					btn.addEventListener('click', () => openEdit(btn.dataset.id));
				});
			})
			.catch((err) => {
				const tbody = document.getElementById('productTableBody');
				const m = err?.message || 'Lỗi không xác định';
				if (tbody)
					tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;color:red">${m}</td></tr>`;
				console.error(err);
			});
	}

	// ================== Xem chi tiết để sửa ==================
	function openEdit(id) {
		fetch(`${API}/${id}`)
			.then((r) => (r.ok ? r.json() : Promise.reject(new Error(`Không tải được sản phẩm #${id}`))))
			.then((p) => {
				openModal('addProductModal');

				document.getElementById('tenSP').value = p.tenSP || '';
				document.getElementById('donViTinh').value = p.donViTinh || '';
				document.getElementById('gia').value = p.gia || 0;
				document.getElementById('moTa').value = p.moTa || '';
				document.getElementById('danhMuc').value = p.maDM || '';

				const tbody = document.querySelector('#materialsTable tbody');
				tbody.innerHTML = '';
				(p.materials || []).forEach((line) => {
					addMaterialRow();
					const last = tbody.querySelector('tr.material-row:last-child');
					const sel = last.querySelector('select');
					const qty = last.querySelector('input[type="number"]');
					sel.value = String(line.maNL);
					qty.value = line.soLuongCan || 1;
					updateUnitDisplay(sel, last.querySelector('.unit-display'));
				});

				document.getElementById('addProductModal').dataset.editingId = String(id);
			})
			.catch((err) => alert(err?.message || err));
	}

	// ================== Delete ==================
	function del(id) {
		if (!confirm('Xóa sản phẩm?')) return;
		fetch(`${API}/${id}`, { method: 'DELETE' })
			.then((r) => {
				if (r.ok) return;
				throw new Error('Xóa thất bại');
			})
			.then(() => loadProducts())
			.catch((err) => alert(err?.message || err));
	}
	window.del = del;

	// ================== Submit form (multipart) ==================
	function bindFormSubmit() {
		const modal = document.getElementById('addProductModal');
		const form = modal ? modal.querySelector('form') : null;
		if (!form) return;

		form.addEventListener('submit', (e) => {
			e.preventDefault();

			const tenSP = document.getElementById('tenSP').value.trim();
			const donViTinh = document.getElementById('donViTinh').value.trim();
			const gia = Number(document.getElementById('gia').value || 0);
			const moTa = document.getElementById('moTa').value || '';
			const danhMucId = Number(document.getElementById('danhMuc').value);

			const rows = document.querySelectorAll('#materialsTable tbody tr.material-row');
			const materials = Array.from(rows)
				.map((tr) => {
					const sel = tr.querySelector('select');
					const qty = tr.querySelector('input[type="number"]');
					return { maNL: Number(sel.value), soLuongCan: Number(qty.value || 1) };
				})
				.filter((x) => x.maNL);

			const payload = JSON.stringify({
				tenSP,
				donViTinh,
				gia,
				soLuongTon: 0,
				moTa,
				danhMucId,
				materials,
			});

			const fd = new FormData();
			fd.append('payload', payload);
			const file = document.getElementById('file')?.files?.[0];
			if (file) fd.append('file', file);

			const editingId = modal.dataset.editingId;
			const method = editingId ? 'PUT' : 'POST';
			const url = editingId ? `${API}/${editingId}` : API;

			fetch(url, { method, body: fd })
				.then(async (r) => {
					if (r.ok) return r.json();
					const ct = r.headers.get('content-type') || '';
					const msg = ct.includes('application/json')
						? JSON.stringify(await r.json())
						: await r.text();
					throw new Error(msg || `HTTP ${r.status}`);
				})
				.then(() => {
					modal.dataset.editingId = '';
					closeModal('addProductModal');
					loadProducts();
				})
				.catch((err) => alert(err?.message || err));
		});
	}

	// ================== Init ==================
	document.addEventListener('DOMContentLoaded', () => {
		// đảm bảo modal không bị che
		const css = document.createElement('style');
		css.textContent = `.modal{z-index:9999}`;
		document.head.appendChild(css);

		bindFormSubmit();
		loadProducts();
	});
})();
