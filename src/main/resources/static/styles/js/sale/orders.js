let currentPage = 0;
const pageSize = 10;

// -------------------- THỐNG KÊ ĐƠN HÀNG --------------------
async function loadOrderStats() {
	try {
		const res = await fetch('/api/sale/orders/stats');
		if (!res.ok) throw new Error('Không thể tải thống kê');

		const data = await res.json();

		// Cập nhật số liệu trên dashboard
		document.getElementById('totalOrdersStat').textContent = data.totalOrders || 0;
		document.getElementById('pendingOrdersStat').textContent = data.pendingOrders || 0;
		document.getElementById('completedOrdersStat').textContent = data.completedOrders || 0;
		document.getElementById('cancelledOrdersStat').textContent = data.cancelledOrders || 0;

	} catch (err) {
		console.error('Lỗi tải thống kê:', err);
	}
}

async function loadOrders() {
	try {
		const res = await fetch('/api/sale/orders');
		if (!res.ok) throw new Error('Không thể tải danh sách đơn hàng');

		const orders = await res.json();
		renderOrderTable(orders);
	} catch (err) {
		console.error('❌ Lỗi tải đơn hàng:', err);
		const tableBody = document.getElementById('orderTableBody');
		tableBody.innerHTML = `
			<tr><td colspan="8" style="text-align:center; color:red;">Không thể tải dữ liệu đơn hàng</td></tr>
		`;
	}
}

// -------------------- HIỂN THỊ DỮ LIỆU TRÊN BẢNG --------------------
function renderOrderTable(orders) {
	const tableBody = document.getElementById('orderTableBody');
	tableBody.innerHTML = '';

	if (!orders || orders.length === 0) {
		tableBody.innerHTML = `
			<tr><td colspan="8" style="text-align:center;">Không có đơn hàng nào được tìm thấy!</td></tr>
		`;
		return;
	}

	orders.forEach((order, index) => {
		const row = `
			<tr>
				<td>${index + 1}</td>
				<td>${order.hoTenKH || 'Mã KH: ' + order.maKH}</td>
				<td>${formatDate(order.ngayDat)}</td>
				<td>${formatCurrency(order.tongTien)}</td>
				<td>${order.phuongThucThanhToan}</td>
				<td>${order.ngayDat ? formatDate(order.ngayDat) : '—'}</td>
				<td>${order.trangThai}</td>
				<td>
				<button class="btn-view" onclick="viewOrderModal(${order.maDH})">
				  <i class="fas fa-eye"></i>
				</button>

					<button class="btn-edit" onclick="editOrderModal(${order.maDH})">
					<i class="fas fa-edit"></i>
					</button>
					
					<button class="btn-approve" onclick="approveOrder(${order.maDH})">
					<i class="fas fa-check"></i>
				    </button>
					
				</td>
			</tr>
		`;
		tableBody.insertAdjacentHTML('beforeend', row);
	});
}

// -------------------- HÀM HỖ TRỢ --------------------
function formatDate(dateStr) {
	if (!dateStr) return '';
	const date = new Date(dateStr);
	return date.toLocaleString('vi-VN', {
		day: '2-digit',
		month: '2-digit',
		year: 'numeric',
		hour: '2-digit',
		minute: '2-digit'
	});
}

function formatCurrency(amount) {
	if (!amount) return '0 ₫';
	return amount.toLocaleString('vi-VN') + ' ₫';
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

// -------------------- HỖ TRỢ VALIDATION --------------------
function clearErrors() {
	document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
}
function displayErrors(errors) {
	clearErrors();
	if (!errors) return;
	for (const field in errors) {
		const el = document.getElementById('error' + field.charAt(0).toUpperCase() + field.slice(1));
		if (el) el.textContent = errors[field];
	}
}

// ----------------------------------------------------

// -------------------- XEM CHI TIẾT ĐƠN HÀNG --------------------
async function viewOrderModal(orderId) {
	console.log('Fetch orderId:', orderId);

	try {
		const res = await fetch(`/api/sale/orders/view/${orderId}`);
		if (!res.ok) throw new Error('Không thể tải chi tiết đơn hàng');

		const order = await res.json();
		console.log('Order data:', order);

		// Điền thông tin vào modal
		document.getElementById('detailOrderId').innerText = order.maDH ?? '';
		document.getElementById('detailCustomer').innerText = order.hoTenKH ?? '';
		document.getElementById('detailOrderDate').innerText = order.ngayDat
			? new Date(order.ngayDat).toLocaleString('vi-VN') : '';
		document.getElementById('detailPayment').innerText = order.phuongThucThanhToan ?? '';
		document.getElementById('detailStatus').innerText = order.trangThai ?? '';
		document.getElementById('detailAddress').innerText = order.diaChiGiao ?? '';
		document.getElementById('detailPhone').innerText = order.sdtNguoiNhan ?? '';
		document.getElementById('detailNotes').innerText = order.ghiChu ?? '';
		document.getElementById('detailEmployee').innerText = order.nhanVienXuLy ?? '';
		document.getElementById('detailUpdatedAt').innerText = order.ngayCapNhat
			? new Date(order.ngayCapNhat).toLocaleString('vi-VN') : '';

		// Hiển thị chi tiết sản phẩm và tính tổng tiền hàng
		const tbody = document.getElementById('detailProductsBody');
		tbody.innerHTML = '';
		let totalItemsAmount = 0;

		if (order.chiTietDonHang && order.chiTietDonHang.length > 0) {
			order.chiTietDonHang.forEach((item, index) => {
				const amount = (item.soLuong && item.giaBan) ? item.soLuong * item.giaBan : 0;
				totalItemsAmount += amount;

				const tr = document.createElement('tr');
				tr.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${item.tenSP ?? ''}</td>
                    <td>${item.soLuong ?? ''}</td>
                    <td>${item.giaBan ? Number(item.giaBan).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) : ''}</td>
                    <td>${amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</td>
                `;
				tbody.appendChild(tr);
			});

			// Thêm dòng tổng tiền hàng
			const trTotal = document.createElement('tr');
			trTotal.innerHTML = `
                <td colspan="4" style="text-align:right; font-weight:bold;">Tổng tiền hàng</td>
                <td style="font-weight:bold;">${totalItemsAmount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</td>
            `;
			tbody.appendChild(trTotal);
		} else {
			tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Không có sản phẩm</td></tr>';
		}

		// Hiển thị phí vận chuyển và tổng tiền đơn hàng
		const shippingFee = order.phiVanChuyen ?? 0;
		document.getElementById('detailShippingFee').innerText = Number(shippingFee).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
		const grandTotal = totalItemsAmount + Number(shippingFee);
		document.getElementById('detailTotal').innerText = grandTotal.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

		openModal('viewOrderModal');
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}

// -------------------- CHỈNH SỬA ĐƠN HÀNG --------------------
// -------------------- CHỈNH SỬA ĐƠN HÀNG --------------------
async function editOrderModal(orderId) {
	const form = document.getElementById('editOrderForm');
	if (!form) return console.error('Form editOrderForm không tồn tại');
	form.reset();
	clearErrors();

	try {
		const res = await fetch(`/api/sale/orders/view/${orderId}`);
		if (!res.ok) throw new Error('Không thể tải thông tin đơn hàng');
		const order = await res.json();

		// Map đúng key từ API
		document.getElementById('editOrderId').value = order.maDH ?? '';
		document.getElementById('editEmployee').value = order.nhanVienXuLy ?? '';
		document.getElementById('editEmployeeId').value = order.maNV ?? '';
		document.getElementById('editStatus').value = order.trangThai ?? '';
		document.getElementById('editNgayGiao').value = order.ngayGiao ?? '';
		document.getElementById('editDiaChiGiao').value = order.diaChiGiao ?? '';
		document.getElementById('editSdtNguoiNhan').value = order.sdtNguoiNhan ?? '';
		document.getElementById('editNotes').value = order.ghiChu ?? '';

		openModal('editOrderModal');
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}

// -------------------- FORM CHỈNH SỬA ĐƠN HÀNG (AJAX) --------------------
const editOrderForm = document.getElementById('editOrderForm');
editOrderForm?.addEventListener('submit', async e => {
	e.preventDefault();
	clearErrors();

	const payload = {
		orderId: document.getElementById('editOrderId').value,
		employeeId: document.getElementById('editEmployeeId').value,
		status: document.getElementById('editStatus').value,
		deliveryDate: document.getElementById('editNgayGiao').value,
		deliveryAddress: document.getElementById('editDiaChiGiao').value,
		receiverPhone: document.getElementById('editSdtNguoiNhan').value,
		notes: document.getElementById('editNotes').value
	};

	try {
		const res = await fetch(`/api/sale/orders/edit`, {
			method: 'PUT',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(payload)
		});

		if (!res.ok) {
			if (res.status === 400) {
				displayErrors(await res.json());
				return;
			}
			throw new Error(await res.text() || 'Cập nhật thất bại');
		}

		await res.json();
		alert('✅ Cập nhật thành công!');
		closeModal('editOrderModal');
		loadOrders(currentPage);
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
});


document.addEventListener('DOMContentLoaded', () => {
	loadOrderStats();
	loadOrders();

	// Đóng modal khi click ra ngoài hoặc nhấn ESC
	document.addEventListener('click', e => {
		if (e.target.classList.contains('modal')) closeModal(e.target.id);
	});
	document.addEventListener('keydown', e => {
		if (e.key === 'Escape') document.querySelectorAll('.modal.show').forEach(m => closeModal(m.id));
	});
});
