// ==============================
// 📦 1. PHẦN XỬ LÝ GIAO DIỆN & MODAL
// ==============================
(() => {
  let materialIndex = 1;
  let materialsData = {}; // map maNL -> {donViTinh, ...}

  // === init từ server (window.__MATERIALS__)
  document.addEventListener('DOMContentLoaded', () => {
    const list = Array.isArray(window.__MATERIALS__) ? window.__MATERIALS__ : [];
    materialsData = list.reduce((acc, m) => {
      acc[String(m.maNL)] = m; // để lookup đơn vị tính
      return acc;
    }, {});
    // bind sự kiện change cho dòng đầu tiên nếu có
    document.querySelectorAll("#materialsTable select[name*='materialId']").forEach(sel => {
      sel.addEventListener('change', () => updateUnitDisplay(sel));
      updateUnitDisplay(sel); // set unit ngay lần đầu
    });
  });

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

  // --- Cập nhật đơn vị tính theo option được chọn ---
  function updateUnitDisplay(selectElement) {
    const row = selectElement.closest("tr");
    const materialId = selectElement.value;
    const unitCell = row.querySelector(".unit-display");
    // ưu tiên lấy từ attribute data-unit của option
    const opt = selectElement.selectedOptions && selectElement.selectedOptions[0];
    unitCell.textContent = (opt && opt.getAttribute('data-unit')) || (materialsData[materialId]?.donViTinh || "");
  }

  // === (MỚI) clone từ <template>, không phụ thuộc vào .material-row trong tbody
  function cloneMaterialRow(i) {
    const tpl = document.getElementById('material-row-template');
    if (!tpl) {
      console.error('Missing #material-row-template');
      return null;
    }
    const frag = tpl.content.cloneNode(true);
    const row = frag.querySelector('.material-row');

    // thay __i__ trong các name
    row.querySelectorAll('[name]').forEach(el => {
      el.name = el.name.replace('__i__', i);
    });

    // bind events
    const sel = row.querySelector("select[name*='materialId']");
    sel.addEventListener('change', () => updateUnitDisplay(sel));
    row.querySelector('.btn-remove').addEventListener('click', () => {
      const rows = document.querySelectorAll("#materialsTable .material-row");
      if (rows.length > 1) row.remove();
    });

    // set unit lần đầu (nếu option đầu có data-unit)
    updateUnitDisplay(sel);
    return row;
  }

  // --- Thêm dòng nguyên liệu (có thể prefill) ---
  window.addMaterialRow = function(prefill) {
    const tbody = document.querySelector("#materialsTable tbody");
    if (!tbody) return;

    const row = cloneMaterialRow(materialIndex);
    if (!row) return;

    if (prefill && prefill.maNL != null) {
      const sel = row.querySelector("select[name*='materialId']");
      sel.value = String(prefill.maNL);
      updateUnitDisplay(sel);
    }
    if (prefill && prefill.soLuongCan != null) {
      row.querySelector("input[type='number']").value = prefill.soLuongCan;
    }

    tbody.appendChild(row);
    materialIndex++;
  };

  // tiện ích xoá dùng inline onclick cũ (giữ tương thích)
  window.removeMaterialRow = function(btn) {
    const row = btn.closest('tr.material-row');
    const rows = document.querySelectorAll("#materialsTable .material-row");
    if (row && rows.length > 1) row.remove();
  };

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
	      // reset index về 0 hoặc 1 đều được, miễn đồng bộ name
	      materialIndex = 0;
	      (p.materials || []).forEach(line => {
	        addMaterialRow({ maNL: line.maNL, soLuongCan: line.soLuongCan || line.soLuong || 1 });
	      });
	      // nếu không có dòng nào, thêm 1 dòng rỗng
	      if ((p.materials || []).length === 0) addMaterialRow();

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
