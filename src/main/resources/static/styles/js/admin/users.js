let currentPage = 0;
const pageSize = 10;

// --- THỐNG KÊ NGƯỜI DÙNG ---
function loadUserStats() {
	fetch('/api/admin/users/stats')
		.then(response => response.json())
		.then(data => {
			document.getElementById('totalUsersStat').textContent = data.totalUsers;
			document.getElementById('activeUsersStat').textContent = data.activeUsers;
			document.getElementById('inactiveUsersStat').textContent = data.inactiveUsers;
			document.getElementById('blockedUsersStat').textContent = data.blockedUsers;
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

	const url = `/api/admin/users?page=${page}&size=${pageSize}&keyword=${encodeURIComponent(keyword)}&status=${status}&roleId=${roleId}`;

	document.getElementById('userTableBody').innerHTML = '<tr><td colspan="9" style="text-align:center;">Đang tải dữ liệu...</td></tr>';
	document.getElementById('userPagination').innerHTML = '';

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
			document.getElementById('userTableBody').innerHTML = '<tr><td colspan="9" style="text-align:center;color:red;">Không thể tải dữ liệu người dùng. Vui lòng thử lại.</td></tr>';
		});
}

// --- RENDER BẢNG NGƯỜI DÙNG ---
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
		const formattedDate = new Date(user.createdAt).toLocaleDateString('vi-VN', {
			year: 'numeric', month: '2-digit', day: '2-digit',
			hour: '2-digit', minute: '2-digit'
		}).replace(',', '');

		const row = document.createElement('tr');
		row.innerHTML = `
            <td>${user.userId}</td>
            <td>${user.username}</td>
            <td>${fullName}</td>
            <td>${user.email}</td>
            <td>${phone}</td>
            <td>${roleName}</td>
            <td>${formattedDate}</td>
            <td class="toggle-cell">
                <label class="switch">
                    <input type="checkbox" ${user.statusValue === 1 ? 'checked' : ''} data-id="${user.userId}">
                    <span class="slider round"></span>
                </label>
                <button class="btn-block" data-id="${user.userId}" data-blocked="${user.statusValue === -1 ? 'true' : 'false'}" style="margin-left:5px;">
                    <i class="fas ${user.statusValue === -1 ? 'fa-lock' : 'fa-lock-open'}"></i>
                </button>
            </td>
            <td>
                <div class="action-buttons">
                    <a href="/admin/users/view/${user.userId}" class="btn-view"> 
                        <i class="fas fa-eye"></i>
                    </a> 
                    <a href="/admin/users/edit/${user.userId}" class="btn-edit"> 
                        <i class="fas fa-edit"></i>
                    </a> 
                    <button class="btn-delete" data-id="${user.userId}">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        `;

		// --- SỰ KIỆN ---
		row.querySelector('input[type="checkbox"]').addEventListener('change', e => toggleStatus(e.target));
		row.querySelector('.btn-block').addEventListener('click', e => toggleBlock(e.currentTarget));
		row.querySelector('.btn-delete').addEventListener('click', e => deleteUser(e.currentTarget));

		tableBody.appendChild(row);
	});
}

// --- PHÂN TRANG ---
function renderPagination(currentPage, totalPages) {
	const paginationDiv = document.getElementById('userPagination');
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

// --- TOGGLE ACTIVE / INACTIVE ---
function toggleStatus(checkbox) {
	const userId = checkbox.getAttribute('data-id');
	const newStatus = checkbox.checked ? 1 : 0;

	fetch(`/api/admin/users/${userId}`, {
		method: 'PUT',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ status: newStatus })
	})
		.then(response => {
			if (!response.ok) throw new Error('Cập nhật thất bại');
			loadUserStats();
		})
		.catch(err => {
			console.error(err);
			alert('Cập nhật trạng thái thất bại!');
			checkbox.checked = !checkbox.checked;
		});
}

// --- TOGGLE BLOCK / UNBLOCK (KHÔNG RELOAD BẢNG) ---
function toggleBlock(button) {
	const userId = button.getAttribute('data-id');
	const isBlocked = button.getAttribute('data-blocked') === 'true';
	const newStatus = isBlocked ? 1 : -1;

	fetch(`/api/admin/users/${userId}`, {
		method: 'PUT',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ status: newStatus })
	})
		.then(response => {
			if (!response.ok) throw new Error('Cập nhật thất bại');

			// Update button và checkbox cục bộ
			const icon = button.querySelector('i');
			icon.classList.toggle('fa-lock', !isBlocked);
			icon.classList.toggle('fa-lock-open', isBlocked);
			button.setAttribute('data-blocked', (!isBlocked).toString());

			// Update checkbox nếu muốn
			const checkbox = button.closest('td').querySelector('input[type="checkbox"]');
			if (checkbox) checkbox.checked = newStatus === 1;

			loadUserStats(); // cập nhật số liệu
		})
		.catch(err => {
			console.error(err);
			alert('Cập nhật trạng thái thất bại!');
		});
}

// --- XÓA NGƯỜI DÙNG ---
function deleteUser(button) {
	const userId = button.getAttribute('data-id');
	if (confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
		fetch(`/api/admin/users/${userId}`, { method: 'DELETE' })
			.then(response => {
				if (response.ok) {
					alert('Xóa người dùng thành công!');
					loadUsers(currentPage);
					loadUserStats();
				} else throw new Error('Xóa thất bại');
			})
			.catch(err => {
				console.error(err);
				alert('Đã xảy ra lỗi khi xóa người dùng.');
			});
	}
}

// --- KHI TRANG ĐƯỢC TẢI ---
document.addEventListener('DOMContentLoaded', () => {
	loadUsers(0);
	loadUserStats();

	const form = document.getElementById('searchFilterForm');
	form.addEventListener('submit', e => {
		e.preventDefault();
		loadUsers(0);
	});

	document.getElementById('statusFilter').addEventListener('change', () => loadUsers(0));
	document.getElementById('roleFilter').addEventListener('change', () => loadUsers(0));

	document.getElementById('userPagination').addEventListener('click', e => {
		e.preventDefault();
		const target = e.target.closest('a');
		if (target && target.dataset.page) {
			loadUsers(parseInt(target.dataset.page, 10));
		}
	});
});

// --- MỞ MODAL ---
function openModal(modalId) {
	const modal = document.getElementById(modalId);
	if (!modal) {
		console.error('❌ Không tìm thấy modal: ' + modalId);
		return;
	}
	modal.classList.add('show');
	document.body.style.overflow = 'hidden';
}

// --- ĐÓNG MODAL ---
function closeModal(modalId) {
	const modal = document.getElementById(modalId);
	if (!modal) return;
	modal.classList.remove('show');
	document.body.style.overflow = '';
}

// --- CLICK NGOÀI MODAL-CONTENT ĐỂ ĐÓNG (FIX: Xóa điều kiện sai) ---
document.addEventListener('click', function(event) {
	// Kiểm tra nếu click vào .modal (phần overlay, không phải modal-content)
	if (event.target.classList.contains('modal')) {
		const modalId = event.target.id;
		if (modalId) {
			closeModal(modalId);
		}
	}
});

// --- ESC ĐỂ ĐÓNG MODAL ---
document.addEventListener('keydown', function(event) {
	if (event.key === 'Escape') {
		const modals = document.querySelectorAll('.modal.show');
		modals.forEach(modal => closeModal(modal.id));
	}
});