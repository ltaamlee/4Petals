// /styles/js/admin/users.js

let currentPage = 0;
const pageSize = 10;

// -------------------- THỐNG KÊ NGƯỜI DÙNG --------------------
async function loadUserStats() {
	try {
		const res = await fetch('/api/admin/users/stats');
		if (!res.ok) throw new Error('Không thể tải thống kê');
		const data = await res.json();
		document.getElementById('totalUsersStat').textContent = data.totalUsers || 0;
		document.getElementById('activeUsersStat').textContent = data.activeUsers || 0;
		document.getElementById('inactiveUsersStat').textContent = data.inactiveUsers || 0;
		document.getElementById('blockedUsersStat').textContent = data.blockedUsers || 0;
	} catch (err) {
		console.error('Lỗi tải thống kê:', err);
	}
}

// -------------------- TẢI DANH SÁCH NGƯỜI DÙNG --------------------
async function loadUsers(page = 0) {
	const form = document.getElementById('searchFilterForm');
	const keyword = form?.elements['keyword']?.value || '';
	const status = form?.elements['status']?.value || '';
	const roleId = form?.elements['roleId']?.value || '';

	currentPage = page;
	const url = `/api/admin/users?page=${page}&size=${pageSize}&keyword=${encodeURIComponent(keyword)}&status=${status}&roleId=${roleId}`;

	const tableBody = document.getElementById('userTableBody');
	const paginationDiv = document.getElementById('userPagination');

	tableBody.innerHTML = '<tr><td colspan="9" style="text-align:center;">Đang tải dữ liệu...</td></tr>';
	paginationDiv.innerHTML = '';

	try {
		const res = await fetch(url);
		if (!res.ok) throw new Error('Không thể tải dữ liệu người dùng');
		const data = await res.json();
		renderUserTable(data.content);
		renderPagination(data.number, data.totalPages);
	} catch (err) {
		console.error('Lỗi tải danh sách người dùng:', err);
		tableBody.innerHTML = '<tr><td colspan="9" style="text-align:center;color:red;">Không thể tải dữ liệu. Vui lòng thử lại.</td></tr>';
	}
}

// -------------------- HIỂN THỊ BẢNG NGƯỜI DÙNG --------------------
function renderUserTable(users) {
	const tableBody = document.getElementById('userTableBody');
	tableBody.innerHTML = '';

	if (!users || users.length === 0) {
		tableBody.innerHTML = '<tr><td colspan="9" style="text-align:center;">Không có người dùng nào được tìm thấy</td></tr>';
		return;
	}

	users.forEach(user => {
		const fullName = user.fullName ?? 'N/A';
		const phone = user.phone ?? 'Chưa cập nhật';
		const roleName = user.roleName ?? 'N/A';
		const createdAt = user.createdAt ? new Date(user.createdAt).toLocaleString('vi-VN') : 'N/A';

		const row = document.createElement('tr');
		row.innerHTML = `
            <td>${user.userId}</td>
            <td>${user.username}</td>
            <td>${fullName}</td>
            <td>${user.email}</td>
            <td>${phone}</td>
            <td>${roleName}</td>
            <td>${createdAt}</td>
            <td>
                <label class="switch">
                    <input type="checkbox" ${user.statusValue === 1 ? 'checked' : ''} onchange="toggleStatus(this, ${user.userId})">
                    <span class="slider round"></span>
                </label>
                <button class="btn-block" onclick="toggleBlock(this, ${user.userId})" data-blocked="${user.statusValue === -1}">
                    <i class="fas ${user.statusValue === -1 ? 'fa-lock' : 'fa-lock-open'}"></i>
                </button>
            </td>
            <td>
                <div class="action-buttons">
                    <a href="javascript:void(0)" class="btn-view" onclick="openViewUserModal(${user.userId})">
                        <i class="fas fa-eye"></i>
                    </a>
                    <a href="javascript:void(0)" class="btn-edit" onclick="openEditUserModal(${user.userId})">
                        <i class="fas fa-edit"></i>
                    </a>
                    <button class="btn-delete" onclick="deleteUser(${user.userId})">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        `;
		tableBody.appendChild(row);
	});
}

// -------------------- PHÂN TRANG --------------------
function renderPagination(current, total) {
	const paginationDiv = document.getElementById('userPagination');
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

// -------------------- THAY ĐỔI TRẠNG THÁI (ACTIVE/INACTIVE) --------------------
async function toggleStatus(checkbox, userId) {
	const newStatus = checkbox.checked ? 1 : 0;
	try {
		const res = await fetch(`/api/admin/users/${userId}`, {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ status: newStatus })
		});
		if (!res.ok) throw new Error('Cập nhật thất bại');
		loadUserStats();
	} catch (err) {
		console.error(err);
		alert('Cập nhật trạng thái thất bại!');
		checkbox.checked = !checkbox.checked;
	}
}

// -------------------- KHÓA / MỞ KHÓA NGƯỜI DÙNG --------------------
async function toggleBlock(button, userId) {
	const isBlocked = button.dataset.blocked === 'true';
	const newStatus = isBlocked ? 1 : -1;

	try {
		const res = await fetch(`/api/admin/users/${userId}`, {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ status: newStatus })
		});
		if (!res.ok) throw new Error('Cập nhật thất bại');

		loadUsers(currentPage);
		loadUserStats();
	} catch (err) {
		console.error(err);
		alert('Cập nhật trạng thái thất bại!');
	}
}

// -------------------- XÓA NGƯỜI DÙNG --------------------
async function deleteUser(userId) {
	if (!confirm(`⚠️ Bạn có chắc muốn xóa người dùng có ID ${userId} không?`)) return;

	try {
		const res = await fetch(`/api/admin/users/delete/${userId}`, {
			method: 'DELETE'
		});

		if (!res.ok) {
			const errorText = await res.text();
			throw new Error(errorText || 'Xóa người dùng thất bại!');
		}

		alert('✅ Xóa người dùng thành công!');
		loadUsers(currentPage);
		loadUserStats();
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}

// -------------------- QUẢN LÝ MODAL --------------------
function openModal(modalId) {
	document.getElementById(modalId)?.classList.add('show');
	document.body.style.overflow = 'hidden';
}
function closeModal(modalId) {
	document.getElementById(modalId)?.classList.remove('show');
	document.body.style.overflow = '';
}

// -------------------- XỬ LÝ LỖI VALIDATION --------------------
function clearErrors() {
	document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
}
function displayErrors(errors) {
	clearErrors();
	if (!errors) return;
	if (typeof errors === 'string') {
		alert(errors);
		return;
	}
	if (errors.error) {
		alert(errors.error); // hiển thị popup thay vì trên form
		return;
	}

	for (const field in errors) {
		const elementId = field.replace("user.", "") + "-error";
		const el = document.getElementById(elementId);
		if (el) {
			el.textContent = errors[field];
		} else {
			console.warn(`Không tìm thấy element hiển thị lỗi cho: #${elementId}`);
		}
	}
}


// -------------------- XEM CHI TIẾT NGƯỜI DÙNG --------------------
async function openViewUserModal(userId) {
	try {
		const res = await fetch(`/api/admin/users/view/${userId}`);
		if (!res.ok) throw new Error('Không thể tải chi tiết người dùng');
		const user = await res.json();

		document.getElementById('detailUserId').innerText = user.userId ?? '';
		document.getElementById('detailUsername').innerText = user.username ?? '';
		document.getElementById('detailFullName').innerText = user.fullName ?? '';
		document.getElementById('detailGender').innerText = user.gender ?? '—';
		document.getElementById('detailBirthDate').innerText = user.birthDate
			? new Date(user.birthDate).toLocaleDateString('vi-VN') : '—';
		document.getElementById('detailEmail').innerText = user.email ?? '';
		document.getElementById('detailPhone').innerText = user.phone ?? '';
		document.getElementById('detailRole').innerText = user.roleName ?? '';
		document.getElementById('detailStatus').innerText = user.statusDisplay ?? '';
		document.getElementById('detailCreatedAt').innerText = user.createdAt ? new Date(user.createdAt).toLocaleString('vi-VN') : '';
		document.getElementById('detailUpdatedAt').innerText = user.updatedAt ? new Date(user.updatedAt).toLocaleString('vi-VN') : '';

		openModal('viewUserModal');
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}

// -------------------- MỞ FORM CHỈNH SỬA NGƯỜI DÙNG --------------------
async function openEditUserModal(userId) {
	const form = document.getElementById('editUserForm');
	form?.reset();
	clearErrors();

	try {
		const res = await fetch(`/api/admin/users/view/${userId}`);
		if (!res.ok) throw new Error('Không thể tải thông tin người dùng');
		const user = await res.json();

		console.log("Dữ liệu người dùng từ backend:", user);

		document.getElementById('editUserId').value = user.userId;
		document.getElementById('editUsername').value = user.username || '';
		document.getElementById('editFullName').value = user.fullName || '';
		document.getElementById('editEmail').value = user.email || '';
		document.getElementById('editPhone').value = user.phone || '';
		document.getElementById('editBirthDate').value = user.birthDate || '';
		document.getElementById('editGender').value = user.gender || '';

		const roleSelect = document.getElementById('editRole');
		const roleContainer = document.getElementById('roleContainer');
		console.log("Dữ liệu user từ backend:", user);
		console.log("Role name:", user.roleName);

		if (user.roleName?.trim() === 'Khách hàng') {
			console.log("Role là CUSTOMER -> ẩn select");
			roleContainer.style.display = 'none';
		} else {
			console.log("Role khác CUSTOMER -> hiện select");
			roleContainer.style.display = 'block';
			const roleSelect = document.getElementById('editRole');
			const foundOpt = Array.from(roleSelect.options).find(opt =>
				opt.textContent.trim() === user.roleName.trim()
			);
			if (foundOpt) {
				roleSelect.value = foundOpt.value;
				console.log("Set role select:", roleSelect.value);
			}
		}

		openModal('editUserModal');
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}


// -------------------- SỰ KIỆN KHI DOM ĐÃ TẢI --------------------
document.addEventListener('DOMContentLoaded', () => {
	loadUsers(0);
	loadUserStats();

	// Gắn sự kiện cho form tìm kiếm và bộ lọc
	document.getElementById('searchFilterForm')?.addEventListener('submit', e => { e.preventDefault(); loadUsers(0); });
	document.getElementById('statusFilter')?.addEventListener('change', () => loadUsers(0));
	document.getElementById('roleFilter')?.addEventListener('change', () => loadUsers(0));

	// Đóng modal khi click ra ngoài hoặc nhấn ESC
	document.addEventListener('click', e => {
		if (e.target.classList.contains('modal')) closeModal(e.target.id);
	});
	document.addEventListener('keydown', e => {
		if (e.key === 'Escape') document.querySelectorAll('.modal.show').forEach(m => closeModal(m.id));
	});

	// --- FORM THÊM NHÂN VIÊN ---
	const addForm = document.getElementById('addEmployeeForm');
	addForm?.addEventListener('submit', async e => {
		e.preventDefault();
		clearErrors();

		const data = {
			hoTen: document.getElementById('fullName').value,
			ngaySinh: document.getElementById('birthDate').value || null,
			gioiTinh: document.getElementById('gender').value || null,
			sdt: document.getElementById('phone').value,
			roleId: parseInt(document.getElementById('role').value),
			user: { // <-- Giữ đối tượng user lồng nhau
				username: document.getElementById('username').value,
				password: document.getElementById('password').value,
				email: document.getElementById('email').value,
			},
		};

		console.log(" Dữ liệu chuẩn bị gửi lên server:", JSON.stringify(data, null, 2));


		try {
			// **FIX**: Giữ nguyên endpoint gốc của bạn
			const res = await fetch('/api/admin/users/add', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(data)
			});

			if (!res.ok) {
				if (res.status === 400) { // Lỗi validation
					const errors = await res.json();
					displayErrors(errors); // Hiển thị lỗi ngay trên form
					return;
				}
				throw new Error(await res.text() || 'Thêm nhân viên thất bại');
			}

			const result = await res.json();
			alert(`✅ Thêm nhân viên thành công: ${result.username}`);
			addForm.reset();
			closeModal('addEmployeeModal');
			loadUsers(0);
			loadUserStats();
		} catch (err) {
			console.error(err);
			alert('❌ ' + err.message);
		}
	});

	// --- FORM SỬA NGƯỜI DÙNG ---
	const editForm = document.getElementById('editUserForm');
	editForm?.addEventListener('submit', async e => {
		e.preventDefault();
		clearErrors();
		const userId = document.getElementById('editUserId').value;
		const data = {
			userId: parseInt(userId),
			username: document.getElementById('editUsername').value.trim(),
			fullName: document.getElementById('editFullName').value.trim(),
			email: document.getElementById('editEmail').value.trim(),
			phone: document.getElementById('editPhone').value.trim(),
			birthDate: document.getElementById('editBirthDate').value || null,
			gender: document.getElementById('editGender').value || null,
			roleName: document.getElementById('editRole').value
		};

		console.log(data);
		try {
			const res = await fetch(`/api/admin/users/edit/${userId}`, {
				method: 'PUT',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(data)
			});
			if (!res.ok) {
				if (res.status === 400) {
					displayErrors(await res.json());
					return;
				}
				throw new Error(await res.text() || 'Cập nhật thất bại');
			}
			const updated = await res.json();
			alert(`✅ Cập nhật thành công: ${updated.username}`);
			closeModal('editUserModal');
			loadUsers(currentPage);
			loadUserStats();
		} catch (err) {
			console.error(err);
			alert('❌ ' + err.message);
		}
	});
});


async function downloadUsersCSV() {
	try {
		const response = await fetch('/api/admin/users/export', { method: 'GET' });

		const contentType = response.headers.get('content-type');

		// Nếu server trả JSON (thường là lỗi hoặc không có dữ liệu)
		if (contentType && contentType.includes('application/json')) {
			const data = await response.json();
			alert(data.message || "Không có dữ liệu để xuất.");
			return;
		}

		if (response.ok) {
			const blob = await response.blob();
			const url = window.URL.createObjectURL(blob);

			const a = document.createElement('a');
			a.href = url;
			a.download = 'danh_sach_nguoi_dung.csv'; // tên file download
			document.body.appendChild(a);
			a.click();
			a.remove();

			// Giải phóng URL object
			window.URL.revokeObjectURL(url);
		} else {
			alert("Đã xảy ra lỗi khi xuất dữ liệu.");
		}
	} catch (error) {
		console.error("❌ Lỗi:", error);
		alert("Không thể kết nối đến server.");
	}
}
