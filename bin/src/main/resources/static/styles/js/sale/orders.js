let currentPage = 0;
const pageSize = 10;

// ================= THỐNG KÊ ĐƠN HÀNG =================
async function loadOrderStats() {
    try {
        const res = await fetch('/api/sale/orders/stats');
        if (!res.ok) throw new Error('Không thể tải thống kê');

        const data = await res.json();
        document.getElementById('totalOrdersStat').textContent = data.totalOrders || 0;
        document.getElementById('pendingOrdersStat').textContent = data.pendingOrders || 0;
        document.getElementById('completedOrdersStat').textContent = data.completedOrders || 0;
        document.getElementById('cancelledOrdersStat').textContent = data.cancelledOrders || 0;

    } catch (err) {
        console.error('Lỗi tải thống kê:', err);
        showNotification('❌ Lỗi tải thống kê', true);
    }
}

// ================= LOAD ĐƠN HÀNG (AJAX + FILTER + SEARCH + PAGE) =================
async function loadOrders(page = 0) {
    currentPage = page;

    const status = document.querySelector('select[name="status"]')?.value || '';
    const keyword = document.querySelector('input[name="keyword"]')?.value.trim() || '';

    const url = new URL('/api/sale/orders', window.location.origin);
    url.searchParams.append('page', currentPage);
    url.searchParams.append('size', pageSize);
    if (status) url.searchParams.append('trangThai', status); // nếu rỗng -> tất cả
    if (keyword) url.searchParams.append('keyword', keyword);

    try {
        const res = await fetch(url);
        if (!res.ok) throw new Error('Không thể tải danh sách đơn hàng');

        const data = await res.json();
        renderOrderTable(data.content);
        renderPagination(data.totalPages);

    } catch (err) {
        console.error('❌ Lỗi tải đơn hàng:', err);
        const tableBody = document.getElementById('orderTableBody');
        tableBody.innerHTML = `<tr><td colspan="8" style="text-align:center; color:red;">Không thể tải dữ liệu</td></tr>`;
    }
}

// ================= HIỂN THỊ BẢNG =================
function renderOrderTable(orders) {
    const tableBody = document.getElementById('orderTableBody');
    tableBody.innerHTML = '';

    if (!orders || orders.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="8" style="text-align:center;">Không có đơn hàng nào!</td></tr>`;
        return;
    }

    orders.forEach((order, index) => {
        const row = `
            <tr>
                <td>${index + 1 + currentPage * pageSize}</td>
                <td>${order.hoTenKH || 'Mã KH: ' + order.maKH}</td>
                <td>${formatDate(order.ngayDat)}</td>
                <td>${formatCurrency(order.tongTien)}</td>
                <td>${order.phuongThucThanhToan}</td>
                <td>${formatDate(order.ngayDat)}</td>
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

// ================= PHÂN TRANG =================
function renderPagination(totalPages) {
    const paginationDiv = document.getElementById('orderPagination');
    paginationDiv.innerHTML = '';

    if (totalPages <= 1) return;

    for (let i = 0; i < totalPages; i++) {
        const btn = document.createElement('button');
        btn.textContent = i + 1;
        btn.disabled = i === currentPage;
        btn.onclick = () => loadOrders(i);
        paginationDiv.appendChild(btn);
    }
}

// ================= HÀM HỖ TRỢ =================
function formatDate(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toLocaleString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
}

function formatCurrency(amount) {
    if (!amount) return '0 ₫';
    return amount.toLocaleString('vi-VN') + ' ₫';
}

function showNotification(message, isError = false, duration = 3000) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.classList.toggle('error', isError);
    notification.style.display = 'block';
    setTimeout(() => notification.style.display = 'none', duration);
}

// ================= MODAL =================
function openModal(modalId) {
    document.getElementById(modalId)?.classList.add('show');
    document.body.style.overflow = 'hidden';
}
function closeModal(modalId) {
    document.getElementById(modalId)?.classList.remove('show');
    document.body.style.overflow = '';
}

// ================= XEM CHI TIẾT ĐƠN HÀNG =================
async function viewOrderModal(orderId) {
    try {
        const res = await fetch(`/api/sale/orders/view/${orderId}`);
        if (!res.ok) throw new Error('Không thể tải chi tiết đơn hàng');
        const order = await res.json();

        const modal = document.getElementById('viewOrderModal');
        modal.dataset.orderId = orderId;

        document.getElementById('detailOrderId').innerText = order.maDH ?? '';
        document.getElementById('detailCustomer').innerText = order.hoTenKH ?? '';
        document.getElementById('detailOrderDate').innerText = order.ngayDat ? formatDate(order.ngayDat) : '';
        document.getElementById('detailPayment').innerText = order.phuongThucThanhToan ?? '';
        document.getElementById('detailStatus').innerText = order.trangThai ?? '';
        document.getElementById('detailAddress').innerText = order.diaChiGiao ?? '';
        document.getElementById('detailPhone').innerText = order.sdtNguoiNhan ?? '';
        document.getElementById('detailEmployee').innerText = order.nhanVienXuLy ?? '';
        document.getElementById('detailUpdatedAt').innerText = order.ngayCapNhat ? formatDate(order.ngayCapNhat) : '';
        document.getElementById('detailNotes').value = order.ghiChu ?? '';

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

        const shippingFee = order.phiVanChuyen ?? 0;
        document.getElementById('detailShippingFee').innerText = Number(shippingFee).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
        document.getElementById('detailTotal').innerText = (totalItemsAmount + Number(shippingFee)).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

        openModal('viewOrderModal');
    } catch (err) {
        console.error(err);
        showNotification('❌ ' + err.message, true);
    }
}

// ================= LƯU GHI CHÚ =================
async function saveOrderNote() {
    const modal = document.getElementById('viewOrderModal');
    const orderId = modal.dataset.orderId;
    const note = document.getElementById('detailNotes').value.trim();
    if (!orderId) return showNotification('Không xác định được đơn hàng!', true);

    try {
        const res = await fetch(`/api/sale/orders/edit/${orderId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ghiChu: note })
        });
        if (!res.ok) throw new Error(await res.text() || 'Lỗi khi lưu ghi chú');

        showNotification('✅ Đã lưu ghi chú thành công!');
        closeModal('viewOrderModal');
        loadOrders(currentPage);
    } catch (err) {
        console.error(err);
        showNotification('❌ ' + err.message, true);
    }
}

// ================= DUYỆT ĐƠN HÀNG =================
let currentApproveOrderId = null;
let currentApproveOrderStatus = null;

function approveOrder(orderId, orderStatus) {
    currentApproveOrderId = orderId;
    currentApproveOrderStatus = orderStatus?.trim().toLowerCase();

    const modal = document.getElementById('confirmApproveModal');
    const body = modal.querySelector('.modal-body');
    const btnSubmit = modal.querySelector('.btn-submit');
    const btnCancel = modal.querySelector('.btn-cancel');

    if (currentApproveOrderStatus !== 'chờ xử lý') {
        body.innerHTML = `<p>Đơn hàng đã được duyệt!</p>`;
        btnSubmit.style.display = 'none';  // ẩn nút "Có"
        btnCancel.textContent = 'Đóng';     // đổi nút "Hủy" thành "Đóng"
        btnCancel.style.display = 'inline-block';
    } else {
        body.innerHTML = `<p>Bạn có chắc chắn muốn duyệt đơn hàng này không?</p>`;
        btnSubmit.style.display = 'inline-block';
        btnCancel.textContent = 'Hủy';      // giữ lại chữ Hủy
        btnCancel.style.display = 'inline-block';
    }

    openModal('confirmApproveModal');
}


async function confirmApproveOrder() {
    if (!currentApproveOrderId) return;

    try {
        const res = await fetch(`/api/sale/orders/approve/${currentApproveOrderId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        });
        if (!res.ok) throw new Error(await res.text() || 'Không thể duyệt đơn hàng');

        showNotification('✅ Đơn hàng đã được duyệt!');
        closeModal('confirmApproveModal');
        currentApproveOrderId = null;
        loadOrderStats();
        loadOrders(currentPage);

    } catch (err) {
        console.error(err);
        showNotification('❌ ' + err.message, true);
    }
}

// ================= INIT =================
document.addEventListener('DOMContentLoaded', () => {
    loadOrderStats();
    loadOrders();

    // Search form
    document.getElementById('searchFilterForm')?.addEventListener('submit', e => {
        e.preventDefault();
        loadOrders(0);
    });

    // Filter trạng thái
    document.querySelector('select[name="status"]')?.addEventListener('change', () => loadOrders(0));

    // Đóng modal khi click ra ngoài hoặc nhấn ESC
    document.addEventListener('click', e => { if (e.target.classList.contains('modal')) closeModal(e.target.id); });
    document.addEventListener('keydown', e => { if (e.key === 'Escape') document.querySelectorAll('.modal.show').forEach(m => closeModal(m.id)); });
});
