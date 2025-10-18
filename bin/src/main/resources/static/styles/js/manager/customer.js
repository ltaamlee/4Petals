let curPage = 0;
const pageSize = 10;
let chart, chartType = 'bar';

function csrfHeaders() {
	const t = document.querySelector('meta[name="_csrf"]')?.content;
	const h = document.querySelector('meta[name="_csrf_header"]')?.content;
	return (t && h) ? { [h]: t } : {};
}

/* ---------- STATS + CHART ---------- */
function switchBar() { chartType = 'bar'; updateChart(); }
function switchPie() { chartType = 'pie'; updateChart(); }

async function updateChart() {
	try {
		const range = document.getElementById('timeRange').value;
		const res = await fetch(`/api/manager/customers/stats?range=${range}`, { headers: csrfHeaders() });
		if (!res.ok) { console.error('stats failed', res.status); return; }
		const data = await res.json();

		// Nếu muốn đồng bộ card:
		if (document.getElementById('totalCustomersStat')) document.getElementById('totalCustomersStat').innerText = data.cards.total;
		if (document.getElementById('newMonthCustomersStat')) document.getElementById('newMonthCustomersStat').innerText = data.cards.newMonth;
		if (document.getElementById('femaleCustomersStat')) document.getElementById('femaleCustomersStat').innerText = data.cards.female;
		if (document.getElementById('maleCustomersStat')) document.getElementById('maleCustomersStat').innerText = data.cards.male;

		const ctx = document.getElementById('customerChart').getContext('2d');
		if (chart) chart.destroy();

		if (chartType === 'bar') {
			chart = new Chart(ctx, {
				type: 'bar',
				data: { labels: data.labels, datasets: [{ label: 'Khách hàng mới', data: data.values }] },
				options: { responsive: true, scales: { y: { beginAtZero: true } } }
			});
		} else {
			chart = new Chart(ctx, {
				type: 'pie',
				data: { labels: ['Nam', 'Nữ'], datasets: [{ data: [data.maleCount, data.femaleCount] }] },
				options: { responsive: true }
			});
		}
	} catch (e) { console.error(e); }
}

/* ---------- LIST + FILTERS + PAGINATION ---------- */
function buildListUrl(page) {
	const kw = document.getElementById('keyword')?.value || '';
	const g = document.getElementById('genderFilter')?.value || '';
	const r = document.getElementById('rankFilter')?.value || '';
	const q = new URLSearchParams();
	q.set('page', page); q.set('size', pageSize);
	if (kw) q.set('keyword', kw);
	if (g) q.set('gender', g);   // NAM/NU/KHAC
	if (r) q.set('rank', r);     // THUONG/BAC/VANG/KIM_CUONG
	return `/api/manager/customers?${q.toString()}`;
}

async function loadCustomers(page = 0) {
	try {
		curPage = page;
		const body = document.getElementById('customerTableBody');
		body.innerHTML = `<tr><td colspan="9" style="text-align:center;">Đang tải...</td></tr>`;
		const res = await fetch(buildListUrl(page), { headers: csrfHeaders() });
		if (!res.ok) {
			body.innerHTML = `<tr><td colspan="9" style="text-align:center;color:red;">Không tải được dữ liệu (${res.status})</td></tr>`;
			console.error('list failed', res.status, await res.text());
			return;
		}
		const data = await res.json();
		renderTable(data.content);
		renderPagination(data.number, data.totalPages);
	} catch (e) {
		console.error(e);
		document.getElementById('customerTableBody').innerHTML =
			`<tr><td colspan="9" style="text-align:center;color:red;">Lỗi JS khi tải dữ liệu</td></tr>`;
	}
}

function renderTable(items) {
	const body = document.getElementById('customerTableBody');
	body.innerHTML = '';
	if (!items || items.length === 0) {
		body.innerHTML = `<tr><td colspan="9" style="text-align:center;">Không có dữ liệu</td></tr>`;
		return;
	}
	items.forEach(r => {
		const tr = document.createElement('tr');
		tr.innerHTML = `
      <td>${r.maKH}</td>
      <td>${r.hoTen ?? ''}</td>
      <td>${r.sdt ?? ''}</td>
      <td>${r.email ?? ''}</td>
      <td>${displayGender(r.gioiTinh)}</td>
      <td>${displayRank(r.hangThanhVien)}</td>
      <td>${r.tongDon ?? 0}</td>
      <td>${fmtDate(r.ngayTao)}</td>
      <td class="center">
        <div class="actions">
          <a class="icon" href="javascript:void(0)" title="Chi tiết" onclick="openDetail(${r.maKH})"><i class="fa fa-eye"></i></a>
        </div>
      </td>`;
		body.appendChild(tr);
	});
}

function renderPagination(page, total) {
	const div = document.getElementById('customerPagination');
	div.innerHTML = '';
	if (!total || total <= 1) return;
	let html = '';
	html += page > 0 ? `<a href="#" data-p="${page - 1}">Trước</a>` : `<span class="disabled">Trước</span>`;
	for (let i = 0; i < total; i++) {
		html += `<a href="#" data-p="${i}" class="${i === page ? 'active' : ''}">${i + 1}</a>`;
	}
	html += page < total - 1 ? `<a href="#" data-p="${page + 1}">Sau</a>` : `<span class="disabled">Sau</span>`;
	div.innerHTML = html;
	div.querySelectorAll('a[data-p]').forEach(a => a.addEventListener('click', e => { e.preventDefault(); loadCustomers(parseInt(a.dataset.p, 10)); }));
}

function resetFilters() {
	document.getElementById('keyword').value = '';
	document.getElementById('genderFilter').value = '';
	document.getElementById('rankFilter').value = '';
	loadCustomers(0);
}

/* ---------- DETAIL MODAL ---------- */
async function openDetail(id) {
	try {
		const r = await fetch(`/api/manager/customers/${id}`, { headers: csrfHeaders() });
		if (!r.ok) return alert('Không tải được chi tiết');
		const d = await r.json();
		setDetail(d);
		document.getElementById('detailModal').style.display = 'flex';
	} catch (e) { console.error(e); }
}
function closeDetail() { document.getElementById('detailModal').style.display = 'none'; }

function setDetail(d) {
	setText('d_maKH', d.maKH);
	setText('d_hoTen', d.hoTen);
	setText('d_sdt', d.sdt);
	setText('d_email', d.email);
	setText('d_gioiTinh', displayGender(d.gioiTinh));
	setText('d_hang', displayRank(d.hangThanhVien));
	setText('d_ngaySinh', d.ngaySinh ?? '');
	setText('d_diaChi', d.diaChi);
	setText('d_tongDon', d.tongDon);
	setText('d_ngayTao', fmtDate(d.ngayTao));
}
function setText(id, v) { const el = document.getElementById(id); if (el) el.innerText = v ?? ''; }

/* ---------- helpers ---------- */
function fmtDate(s) { if (!s) return ''; const d = new Date(s); return isNaN(d) ? s : d.toLocaleDateString('vi-VN'); }
function displayGender(g) { if (!g) return ''; if (g === 'NAM') return 'Nam'; if (g === 'NU') return 'Nữ'; if (g === 'KHAC') return 'Khác'; return g; }
function displayRank(r) { if (!r) return ''; return { THUONG: 'Thường', BAC: 'Bạc', VANG: 'Vàng', KIM_CUONG: 'Kim Cương' }[r] ?? r; }

/* ---------- init ---------- */



window.addEventListener('DOMContentLoaded', function() {
	try {
		
		loadCustomers(0);
		updateChart();
		const form = document.getElementById('searchFilterForm');
		if (form) form.addEventListener('submit', e => { e.preventDefault(); loadCustomers(0); });
		document.getElementById('detailModal')?.addEventListener('click', e => { if (e.target.id === 'detailModal') closeDetail(); });
	} catch (e) { console.error(e); }
});
