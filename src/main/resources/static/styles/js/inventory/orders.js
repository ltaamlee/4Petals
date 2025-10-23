/*let currentOrderId = null;

function showOrderDetail(maDH) {
	currentOrderId = maDH;

	// 🔥 1. Cập nhật tiêu đề modal trước khi gọi AJAX
	const modalTitleSpan = document.getElementById('modal-order-id-display');
	if (modalTitleSpan) {
		modalTitleSpan.textContent = ` ${maDH}`;
	}

	// Gọi AJAX để lấy chi tiết đơn hàng (endpoint trả về JSON)
	fetch('/inventory/orders/' + maDH + '/details')
		.then(response => response.json())
		.then(data => {
			const tbody = document.getElementById('order-detail-body');
			tbody.innerHTML = ''; // xóa cũ

			// Lặp qua từng chi tiết đơn hàng (sản phẩm)
			data.forEach((item, index) => {
			    
				// 🔥 1. Tạo nội dung HTML cho cột Nguyên liệu
				const materialContent = item.chiTietNguyenLieu && item.chiTietNguyenLieu.length > 0
					? `<ul class="list-unstyled text-start m-0 p-0 ps-3 small">` +
					  item.chiTietNguyenLieu.map(material => `
							<li>
								- ${material.tenNguyenLieu}: 
								<span class="fw-bold">${material.tongSoLuongCan}</span> 
								<span class="text-muted">(${material.donViTinh})</span>
							</li>
					  `).join('') + `</ul>`
					: '<span class="text-muted">Không áp dụng</span>';


				// 🔥 2. Hàng chính hiển thị thông tin sản phẩm và cột Nguyên liệu
				const productRow = document.createElement('tr');
				productRow.innerHTML = `
					<td>${index + 1}</td>
					<td>${item.tenSanPham}</td>
					<td>${item.soLuong}</td>
				    
					<td class="text-start">${materialContent}</td>
				`;
			    
				// 🔥 3. Chỉ thêm hàng chính, loại bỏ hàng phụ (materialRow)
				tbody.appendChild(productRow);
			});

			// Hiển thị modal
			const modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
			modal.show();
		})
		.catch(error => {
			// Đặt lại tiêu đề nếu có lỗi
			 if (modalTitleSpan) {
				modalTitleSpan.textContent = "";
			}
			console.error('Error fetching order details:', error);
			alert('Có lỗi xảy ra khi lấy chi tiết đơn hàng.');
		});
}

function confirmOrder() {
	if (currentOrderId !== null) {
		const modalEl = document.getElementById('orderDetailModal');
		const modal = bootstrap.Modal.getInstance(modalEl);
		
		fetch('/inventory/orders/' + currentOrderId + '/confirm', { method: 'POST' })
			.then(response => {
				if (response.ok) {
					// Sử dụng currentOrderId trong thông báo
					alert('Đơn hàng có mã ' + currentOrderId + ' đã đóng gói thành công');
					window.location.reload(); 
					
				} else {
					return response.text().then(errorMessage => {
						throw new Error(errorMessage); 
					});
				}
			})
			.catch(error => {
				console.error('Lỗi xác nhận đơn hàng:', error.message);
				alert(error.message); 
			})
			.finally(() => {
				if (modal) {
					modal.hide();
				}
			    
				// Xóa mã đơn hàng khỏi tiêu đề sau khi modal đóng (optional)
				const modalTitleSpan = document.getElementById('modal-order-id-display');
				if (modalTitleSpan) {
					modalTitleSpan.textContent = "";
				}
			});
	}
}*/

let currentOrderId = null;

function showOrderDetail(maDH) {
	currentOrderId = maDH;

	// 🔥 1. Cập nhật tiêu đề modal trước khi gọi AJAX
	const modalTitleSpan = document.getElementById('modal-order-id-display');
	if (modalTitleSpan) {
		modalTitleSpan.textContent = ` ${maDH}`;
	}

	// Gọi AJAX để lấy chi tiết đơn hàng (endpoint trả về JSON)
	fetch('/inventory/orders/' + maDH + '/details')
		.then(response => response.json())
		.then(data => {
			const tbody = document.getElementById('order-detail-body');
			tbody.innerHTML = ''; // xóa cũ

			// Lặp qua từng chi tiết đơn hàng (sản phẩm)
			data.forEach((item, index) => {

				// 🔥 1. Tạo nội dung HTML cho cột Nguyên liệu
				const materialContent = item.chiTietNguyenLieu && item.chiTietNguyenLieu.length > 0
					? `<ul class="list-unstyled text-start m-0 p-0 ps-3 small">` +
					item.chiTietNguyenLieu.map(material => `
                            <li>
                                - ${material.tenNguyenLieu}: 
                                <span class="fw-bold">${material.tongSoLuongCan}</span> 
                                <span class="text-muted">(${material.donViTinh})</span>
                            </li>
                      `).join('') + `</ul>`
					: '<span class="text-muted">Không áp dụng</span>';


				// 🔥 2. Hàng chính hiển thị thông tin sản phẩm và cột Nguyên liệu
				const productRow = document.createElement('tr');
				productRow.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${item.tenSanPham}</td>
                    <td>${item.soLuong}</td>
                    
                    <td class="text-start">${materialContent}</td>
                `;

				// 🔥 3. Chỉ thêm hàng chính, loại bỏ hàng phụ (materialRow)
				tbody.appendChild(productRow);
			});

			// Hiển thị modal
			const modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
			modal.show();
		})
		.catch(error => {
			// Đặt lại tiêu đề nếu có lỗi
			if (modalTitleSpan) {
				modalTitleSpan.textContent = "";
			}
			console.error('Error fetching order details:', error);
			alert('Có lỗi xảy ra khi lấy chi tiết đơn hàng.');
		});
}

function confirmOrder() {
	if (currentOrderId !== null) {
		const modalEl = document.getElementById('orderDetailModal');
		const modal = bootstrap.Modal.getInstance(modalEl);

		fetch('/inventory/orders/' + currentOrderId + '/confirm', { method: 'POST' })
			.then(response => {
				if (response.ok) {
					// Sử dụng currentOrderId trong thông báo
					alert('Đơn hàng có mã ' + currentOrderId + ' đã đóng gói thành công');

					// START: Thêm logic cập nhật checkbox
					const checkbox = document.getElementById('confirm-check-' + currentOrderId);
					if (checkbox) {
						checkbox.checked = true; // Tick chọn checkbox
					}
					// END: Thêm logic cập nhật checkbox

					window.location.reload();

				} else {
					return response.text().then(errorMessage => {
						throw new Error(errorMessage);
					});
				}
			})
			.catch(error => {
				console.error('Lỗi xác nhận đơn hàng:', error.message);
				alert(error.message);
			})
			.finally(() => {
				if (modal) {
					modal.hide();
				}

				// Xóa mã đơn hàng khỏi tiêu đề sau khi modal đóng (optional)
				const modalTitleSpan = document.getElementById('modal-order-id-display');
				if (modalTitleSpan) {
					modalTitleSpan.textContent = "";
				}
			});
	}
}