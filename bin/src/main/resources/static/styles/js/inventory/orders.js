let currentOrderId = null;

	function showOrderDetail(maDH) {
		currentOrderId = maDH;

		// Gọi AJAX để lấy chi tiết đơn hàng
		fetch('/inventory/orders/' + maDH + '/details') // endpoint trả về JSON
			.then(response => response.json())
			.then(data => {
				const tbody = document.getElementById('order-detail-body');
				tbody.innerHTML = ''; // xóa cũ
				data.forEach((item, index) => {
					const row = document.createElement('tr');
					row.innerHTML = `
						<td>${index + 1}</td>
						<td>${item.tenSP}</td>
						<td>${item.soLuong}</td>
						<td>${item.gia}</td>
						<td>${item.soLuong * item.gia}</td>
					`;
					tbody.appendChild(row);
				});

				// Hiển thị modal
				const modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
				modal.show();
			});
	}

	function confirmOrder() {
		if (currentOrderId !== null) {
			// Tích ô trạng thái
			const checkbox = document.getElementById('status-' + currentOrderId);
			checkbox.checked = true;

			// Gửi AJAX để cập nhật trạng thái Order trên server
			fetch('/inventory/orders/' + currentOrderId + '/confirm', { method: 'POST' });

			// Ẩn modal
			const modalEl = document.getElementById('orderDetailModal');
			const modal = bootstrap.Modal.getInstance(modalEl);
			modal.hide();
		}
	}