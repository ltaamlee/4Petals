let currentPage = 0;
const pageSize = 10;
console.log("✅ employees.js loaded!");

// --- THỐNG KÊ NGƯỜI DÙNG ---
function loadUserStats() {
	fetch('/api/manager/employees/stats')
		.then(response => response.json())
		.then(data => {
			document.getElementById('totalEmployeesStat').textContent = data.totalUsers;
			document.getElementById('activeEmployeesStat').textContent = data.activeUsers;
			document.getElementById('inactiveEmployeesStat').textContent = data.inactiveUsers;
			document.getElementById('blockedEmployeesStat').textContent = data.blockedUsers;
		})
		.catch(error => console.error('Lỗi khi tải thống kê:', error));
}

// --- LOAD NGƯỜI DÙNG ---
function loadUsers(page = 0) {
	console.log("🚀 loadEmployees() được gọi, page =", page);
	const form = document.getElementById('searchFilterForm');
	const keyword = form.elements['keyword'].value;
	const status = form.elements['status'].value;
	const roleId = form.elements['roleId'].value;


	currentPage = page;

	const url = `/api/manager/employees?page=${page}&size=${pageSize}&keyword=${encodeURIComponent(keyword)}&status=${status}&roleId=${roleId}`;

	document.getElementById('employeeTableBody').innerHTML = '<tr><td colspan="6" style="text-align:center;">Đang tải dữ liệu...</td></tr>';
	document.getElementById('employeePagination').innerHTML = '';

	fetch(url)
		.then(response => {
			if (!response.ok) throw new Error('Lỗi kết nối!');
			return response.json();
		})
		.then(data => {
			console.log("📦 Dữ liệu nhận từ API /api/manager/employees:", data);

			if (!data || !data.content) {
				console.warn("⚠️ Không có trường 'content' trong dữ liệu!");
				document.getElementById('employeeTableBody').innerHTML =
					'<tr><td colspan="6" style="text-align:center;color:red;">Không có dữ liệu trả về!</td></tr>';
				return;
			}

			renderEmployeeTable(data.content);
			renderPagination(data.number, data.totalPages);
		})

		.catch(error => {
			console.error('Lỗi khi tải người dùng:', error);
			document.getElementById('employeeTableBody').innerHTML = '<tr><td colspan="6" style="text-align:center;color:red;">Không thể tải dữ liệu người dùng. Vui lòng thử lại.</td></tr>';
		});
}

function renderPagination(current, total) {
	const paginationDiv = document.getElementById('employeePagination');
	paginationDiv.innerHTML = '';
	if (total <= 1) return;

	let html = '';
	html += current > 0
		? `<a href="#" onclick="event.preventDefault(); loadUsers(${current - 1})"> <i class="fas fa-chevron-left"></i> Trước </a>`
		: `<span class="disabled"> <i class="fas fa-chevron-left"></i> Trước </span>`;

	for (let i = 0; i < total; i++) {
		html += `<a href="#" onclick="event.preventDefault(); loadUsers(${i})" class="${i === current ? 'active' : ''}">${i + 1}</a>`;
	}

	html += current < total - 1
		? `<a href="#" onclick="event.preventDefault(); loadUsers(${current + 1})"> Sau <i class="fas fa-chevron-right"></i> </a>`
		: `<span class="disabled"> Sau <i class="fas fa-chevron-right"></i> </span>`;

	paginationDiv.innerHTML = html;
}


// --- RENDER NHÂN VIÊN ---
function renderEmployeeTable(employees) {
	const tableBody = document.getElementById('employeeTableBody');
	tableBody.innerHTML = '';

	if (!employees || employees.length === 0) {
		tableBody.innerHTML = `
      <tr>
        <td colspan="6" style="text-align:center; padding: 16px;">
          Không có nhân viên nào được tìm thấy!
        </td>
      </tr>`;
		return;
	}

	employees.forEach(emp => {
		const fullName = emp.fullName ?? 'N/A';
		const phone = emp.phone ?? 'Chưa cập nhật';
		const email = emp.email ?? 'Chưa cập nhật';
		const roleName = emp.roleName ?? 'N/A';
		const id = emp.employeeId ?? emp.userId ?? '—';
		
		console.log(`Nhân viên: ${fullName}, Role: ${roleName}, RoleId: ${emp.roleId}`);
		const row = document.createElement('tr');
		row.innerHTML = `
      <td>${id}</td>
      <td>${fullName}</td>
      <td>${phone}</td>
      <td>${email}</td>
      <td>${roleName}</td>
      <td>
        <div class="action-buttons">
          <a href="javascript:void(0)" class="btn-view" title="Xem chi tiết"
             onclick="openViewEmployeeModal(${id})">
            <i class="fas fa-eye"></i>
          </a>
        </div>
      </td>
    `;
		tableBody.appendChild(row);
	});
}

async function openViewEmployeeModal(employeeId) {
	try {
		const res = await fetch(`/api/manager/employees/view/${employeeId}`);
		if (!res.ok) throw new Error('Không thể tải chi tiết nhân viên');

		const u = await res.json();
		// Fill modal fields
		document.getElementById('empDetailId').innerText = u.employeeId ?? u.userId ?? '';
		document.getElementById('empDetailName').innerText = u.fullName ?? '';
		document.getElementById('empDetailEmail').innerText = u.email ?? '';
		document.getElementById('empDetailPhone').innerText = u.phone ?? '';
		document.getElementById('empDetailRole').innerText = u.roleName ?? '';
		document.getElementById('empDetailGender').innerText = u.gender ?? '—';
		document.getElementById('empDetailBirth').innerText = u.birthDate
			? new Date(u.birthDate).toLocaleDateString('vi-VN') : '—';
		document.getElementById('empDetailStatus').innerText = u.statusDisplay ?? '';
		document.getElementById('empDetailCreated').innerText = u.createdAt
			? new Date(u.createdAt).toLocaleString('vi-VN') : '';
		document.getElementById('empDetailUpdated').innerText = u.updatedAt
			? new Date(u.updatedAt).toLocaleString('vi-VN') : '';

		openModal('viewEmployeeModal');
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}

document.addEventListener('DOMContentLoaded', () => {
	console.log("📢 DOM đã sẵn sàng — Gọi loadUsers()");
	loadUsers(0); // Gọi hàm tải danh sách nhân viên
	loadUserStats();

	document.getElementById('statusFilter').addEventListener('change', () => loadUsers(0));
	document.getElementById('roleFilter').addEventListener('change', () => loadUsers(0));
	document.getElementById('searchFilterForm').addEventListener('submit', e => {
		e.preventDefault();
		loadUsers(0);
	});
});