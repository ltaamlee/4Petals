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
				<button class="btn-approve" onclick="approveOrder(${order.maDH}, '${order.trangThai}')">
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


function showNotification(message, isError = false, duration = 3000) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.classList.toggle('error', isError);
    notification.style.display = 'block';
    
    setTimeout(() => {
        notification.style.display = 'none';
    }, duration);
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

		// Gắn orderId để lưu khi cập nhật ghi chú
		document.getElementById('viewOrderModal').dataset.orderId = orderId;

		// Điền thông tin vào modal
		document.getElementById('detailOrderId').innerText = order.maDH ?? '';
		document.getElementById('detailCustomer').innerText = order.hoTenKH ?? '';
		document.getElementById('detailOrderDate').innerText = order.ngayDat
			? new Date(order.ngayDat).toLocaleString('vi-VN') : '';
		document.getElementById('detailPayment').innerText = order.phuongThucThanhToan ?? '';
		document.getElementById('detailStatus').innerText = order.trangThai ?? '';
		document.getElementById('detailAddress').innerText = order.diaChiGiao ?? '';
		document.getElementById('detailPhone').innerText = order.sdtNguoiNhan ?? '';
		document.getElementById('detailEmployee').innerText = order.nhanVienXuLy ?? '';
		document.getElementById('detailUpdatedAt').innerText = order.ngayCapNhat
			? new Date(order.ngayCapNhat).toLocaleString('vi-VN') : '';

		// Điền ghi chú (textarea)
		document.getElementById('detailNotes').value = order.ghiChu ?? '';

		// Hiển thị danh sách sản phẩm
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

			const trTotal = document.createElement('tr');
			trTotal.innerHTML = `
				<td colspan="4" style="text-align:right; font-weight:bold;">Tổng tiền hàng</td>
				<td style="font-weight:bold;">${totalItemsAmount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}</td>
			`;
			tbody.appendChild(trTotal);
		} else {
			tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Không có sản phẩm</td></tr>';
		}

		// Phí vận chuyển + Tổng tiền
		const shippingFee = order.phiVanChuyen ?? 0;
		document.getElementById('detailShippingFee').innerText =
			Number(shippingFee).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
		const grandTotal = totalItemsAmount + Number(shippingFee);
		document.getElementById('detailTotal').innerText =
			grandTotal.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

		openModal('viewOrderModal');
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}


// -------------------- LƯU GHI CHÚ ĐƠN HÀNG --------------------
async function saveOrderNote() {
	const modal = document.getElementById('viewOrderModal');
	const orderId = modal.dataset.orderId;
	const note = document.getElementById('detailNotes').value.trim();

	if (!orderId) {
		alert('Không xác định được đơn hàng!');
		return;
	}

	try {
		const res = await fetch(`/api/sale/orders/edit/${orderId}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ ghiChu: note })
		});

		if (!res.ok) {
			const msg = await res.text();
			throw new Error(msg || 'Lỗi khi lưu ghi chú');
		}

		alert('✅ Đã lưu ghi chú thành công!');
		closeModal('viewOrderModal');
	} catch (err) {
		console.error(err);
		alert('❌ ' + err.message);
	}
}


// -------------------- DUYỆT ĐƠN HÀNG --------------------
let currentApproveOrderId = null;
let currentApproveOrderStatus = null;

function approveOrder(orderId, orderStatus) {
    currentApproveOrderId = orderId;
    currentApproveOrderStatus = orderStatus?.trim().toLowerCase();

    const modal = document.getElementById('confirmApproveModal');
    const body = modal.querySelector('.modal-body');
    const btnSubmit = modal.querySelector('.btn-submit');

    if (currentApproveOrderStatus !== 'chờ xử lý') {
        body.innerHTML = `<p>Đơn hàng đã được duyệt!</p>`;
        btnSubmit.style.display = 'none';
    } else {
        body.innerHTML = `<p>Bạn có chắc chắn muốn duyệt đơn hàng này không?</p>`;
        btnSubmit.style.display = 'inline-block';
    }

    openModal('confirmApproveModal');
}


// Xác nhận duyệt đơn hàng
async function confirmApproveOrder() {
    if (!currentApproveOrderId) return;

    console.log('Sending approve request for orderId:', currentApproveOrderId);

    try {
        const res = await fetch(`/api/sale/orders/approve/${currentApproveOrderId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        });

        console.log('Response status:', res.status);
        const data = await res.text();
        console.log('Response body:', data);

        if (!res.ok) {
            throw new Error(data || 'Không thể duyệt đơn hàng');
        }

        showNotification('✅ Đơn hàng đã được duyệt!');
        closeModal('confirmApproveModal');
        currentApproveOrderId = null;
        loadOrderStats();
        loadOrders();

    } catch (err) {
        console.error(err);
        showNotification('❌ ' + err.message, true);
    }
}



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
