// public/styles/js/manager/categories.js
let currentPage = 0;
const pageSize = 10;

function fmtDate(s){
  if(!s) return '';
  const d = new Date(s);
  return d.toLocaleDateString('vi-VN', {year:'numeric', month:'2-digit', day:'2-digit',
                                        hour:'2-digit', minute:'2-digit'}).replace(',', '');
}

// LOAD LIST
function loadCategories(page = 0){
  currentPage = page;
  const kw = document.getElementById('keyword')?.value || '';
  const url = `/api/manager/categories?page=${page}&size=${pageSize}&keyword=${encodeURIComponent(kw)}`;

  document.getElementById('catTableBody').innerHTML =
    '<tr><td colspan="5" style="text-align:center;">Đang tải dữ liệu...</td></tr>';
  document.getElementById('catPagination').innerHTML = '';

  fetch(url)
    .then(r => { if(!r.ok) throw new Error('Network'); return r.json(); })
    .then(data => {
      renderCategoryTable(data.content);
      renderPagination(data.number, data.totalPages);
    })
    .catch(() => {
      document.getElementById('catTableBody').innerHTML =
        '<tr><td colspan="5" style="text-align:center;color:red;">Không tải được dữ liệu</td></tr>';
    });
}

// RENDER TABLE
function renderCategoryTable(items){
  const tb = document.getElementById('catTableBody');
  tb.innerHTML = '';
  if (!items || items.length === 0) {
    tb.innerHTML = '<tr><td colspan="5" style="text-align:center;">Không có danh mục</td></tr>';
    return;
  }
  items.forEach(c => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${c.maDM}</td>
      <td>${c.tenDM ?? ''}</td>
      <td>${c.moTa ?? ''}</td>
      <td>${fmtDate(c.updatedAt)}</td>
      <td>
        <div class="action-buttons">
          <a href="javascript:void(0)" class="btn-edit" onclick="editCategory(${c.maDM})">
            <i class="fas fa-edit"></i>
          </a>
          <button class="btn-delete" onclick="deleteCategory(${c.maDM})">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </td>`;
    tb.appendChild(tr);
  });
}

// PAGINATION (giống users.js)
function renderPagination(page, total){
  const div = document.getElementById('catPagination');
  div.innerHTML = '';
  if (total <= 1) return;

  let html = '';
  html += page > 0
    ? `<a href="#" data-page="${page-1}"><i class="fas fa-chevron-left"></i> Trước</a>`
    : `<span class="disabled"><i class="fas fa-chevron-left"></i> Trước</span>`;

  for (let i=0; i<total; i++){
    const active = i === page ? 'active' : '';
    html += `<a href="#" data-page="${i}" class="${active}">${i+1}</a>`;
  }

  html += page < total-1
    ? `<a href="#" data-page="${page+1}">Sau <i class="fas fa-chevron-right"></i></a>`
    : `<span class="disabled">Sau <i class="fas fa-chevron-right"></i></span>`;

  div.innerHTML = html;

  div.querySelectorAll('a[data-page]').forEach(a=>{
    a.addEventListener('click', e => { e.preventDefault(); loadCategories(parseInt(a.dataset.page, 10)); });
  });
}

// MODAL
function openModal(id){ document.getElementById(id).classList.add('show'); document.body.style.overflow='hidden'; }
function closeModal(id){ document.getElementById(id).classList.remove('show'); document.body.style.overflow=''; }

document.addEventListener('click', e=>{
  if (e.target.classList?.contains('modal')) closeModal(e.target.id);
});
document.addEventListener('keydown', e=>{
  if (e.key === 'Escape') document.querySelectorAll('.modal.show').forEach(m=>m.classList.remove('show'));
});

// EDIT / ADD
async function editCategory(id){
  const r = await fetch(`/api/manager/categories/${id}`); if(!r.ok) return alert('Không lấy được chi tiết');
  const c = await r.json();
  document.getElementById('catId').value = c.maDM;
  document.getElementById('catName').value = c.tenDM ?? '';
  document.getElementById('catDesc').value = c.moTa ?? '';
  document.getElementById('modalTitle').innerText = 'Cập nhật danh mục';
  openModal('catModal');
}

function submitCategory(e){
  e.preventDefault();
  const id = document.getElementById('catId').value;
  const payload = {
    tenDM: document.getElementById('catName').value?.trim(),
    moTa:  document.getElementById('catDesc').value?.trim()
  };
  const url = id ? `/api/manager/categories/${id}` : `/api/manager/categories`;
  const method = id ? 'PUT' : 'POST';
  fetch(url, { method, headers:{'Content-Type':'application/json'}, body: JSON.stringify(payload) })
    .then(r => { if(!r.ok) throw new Error(); return r.json(); })
    .then(() => { closeModal('catModal'); loadCategories(currentPage); })
    .catch(() => alert('Lưu thất bại'));
  return false;
}

// DELETE
function deleteCategory(id){
  if (!confirm('Xóa danh mục này?')) return;
  fetch(`/api/manager/categories/${id}`, { method:'DELETE' })
    .then(r => { if(!r.ok) throw new Error(); loadCategories(currentPage); })
    .catch(()=> alert('Xóa thất bại'));
}

// Init
document.addEventListener('DOMContentLoaded', ()=>{
  loadCategories(0);
  document.getElementById('searchFilterForm').addEventListener('submit', e => { e.preventDefault(); loadCategories(0); });
});
