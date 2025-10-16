let currentPage = 0;
const pageSize = 10;

// --- THỐNG KÊ NGƯỜI DÙNG ---
function loadUserStats() {
	fetch('/api/manager/employees/stats')
		.then(response => response.json())
		.then(data => {
			document.getElementById('totalEmployeesStat').textContent = data.totalEmployees;
			document.getElementById('activeEmployeesStat').textContent = data.activeEmployees;
			document.getElementById('inactiveEmployeesStat').textContent = data.inactiveEmployees;
			document.getElementById('blockedEmployeesStat').textContent = data.blockedEmployees;
		})
		.catch(error => console.error('Lỗi khi tải thống kê:', error));
}

// --- LOAD NGƯỜI DÙNG ---
function loadUsers(page = 0) {
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
			renderUserTable(data.content);
			renderPagination(data.number, data.totalPages);
		})
		.catch(error => {
			console.error('Lỗi khi tải người dùng:', error);
			document.getElementById('employeeTableBody').innerHTML = '<tr><td colspan="6" style="text-align:center;color:red;">Không thể tải dữ liệu người dùng. Vui lòng thử lại.</td></tr>';
		});
}


// --- RENDER NHÂN VIÊN ---
function renderEmployeeTable(employees) {
    const tableBody = document.getElementById('employeeTableBody');
    tableBody.innerHTML = '';

    if (!employees || employees.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align:center; padding: 16px;">
                    Không có nhân viên nào được tìm thấy
                </td>
            </tr>`;
        return;
    }

    employees.forEach(emp => {
        const fullName = emp.fullName ?? 'N/A';
        const phone = emp.phone ?? 'Chưa cập nhật';
        const email = emp.email ?? 'Chưa cập nhật';
        const roleName = emp.roleName ?? 'N/A';
        const createdAt = emp.createdAt
            ? new Date(emp.createdAt).toLocaleDateString('vi-VN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            }).replace(',', '')
            : 'Chưa có';

        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${emp.employeeId ?? emp.userId ?? '—'}</td>
            <td>${fullName}</td>
            <td>${phone}</td>
            <td>${email}</td>
            <td>${roleName}</td>
            <td>
                <div class="action-buttons">
                    <a href="/manager/employees/view/${emp.employeeId}" class="btn-view" title="Xem chi tiết">
                        <i class="fas fa-eye"></i>
                    </a>
                    <a href="/manager/employees/edit/${emp.employeeId}" class="btn-edit" title="Chỉnh sửa">
                        <i class="fas fa-edit"></i>
                    </a>
                    <button class="btn-block" 
                        data-id="${emp.employeeId}" 
                        data-blocked="${emp.statusValue === -1 ? 'true' : 'false'}" 
                        title="${emp.statusValue === -1 ? 'Mở khóa' : 'Khóa tài khoản'}">
                        <i class="fas ${emp.statusValue === -1 ? 'fa-lock' : 'fa-lock-open'}"></i>
                    </button>
                    <button class="btn-delete" data-id="${emp.employeeId}" title="Xóa nhân viên">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        `;

        // --- GẮN SỰ KIỆN ---
        row.querySelector('.btn-block').addEventListener('click', e => toggleBlock(e.currentTarget));
        row.querySelector('.btn-delete').addEventListener('click', e => deleteEmployee(e.currentTarget));

        tableBody.appendChild(row);
    });
}