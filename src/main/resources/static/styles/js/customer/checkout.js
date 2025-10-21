document.addEventListener("DOMContentLoaded", () => {
	// ---------------------------
	// PHẦN 1: XỬ LÝ CHUNG
	// ---------------------------
	const form = document.getElementById('checkoutForm');
	
	// ---------------------------
	// PHẦN 2: XỬ LÝ CHỌN PHƯƠNG THỨC THANH TOÁN (Giữ lại)
	// ---------------------------
	const radios = document.querySelectorAll('input[name="paymentMethod"]');
	const paymentInput = document.getElementById('paymentMethodInput'); // Input ẩn trong form
	const qrBox = document.getElementById('qrBox'); // Div chứa QR

	// Vì radio nằm ngoài form, ta phải dùng JS để cập nhật input ẩn
	radios.forEach(radio => {
		radio.addEventListener('change', (e) => {
			const selectedValue = e.target.value;
			
			// 1. Cập nhật giá trị vào input ẩn
			paymentInput.value = selectedValue; 

			// 2. Đổi tab active (cho đẹp)
			document.querySelectorAll('.payment-tab').forEach(tab => tab.classList.remove('active'));
			e.target.closest('.payment-tab').classList.add('active');
			
			// 3. Ẩn QR box đi (VÌ LUỒNG NÀY KHÔNG DÙNG NỮA)
			qrBox.classList.add('hidden');
		});
	});

	// ---------------------------
	// PHẦN 3: XỬ LÝ SUBMIT FORM
	// ---------------------------
	
    // !!! ĐÃ XÓA BỎ HOÀN TOÀN logic "form.addEventListener('submit', ...)"
    // Form sẽ submit một cách bình thường đến "/checkout/confirm"
    // Backend (Java) sẽ xử lý việc chuyển hướng.

	// ---------------------------
	// PHẦN 4: CHỌN / THÊM ĐỊA CHỈ (Giữ nguyên code cũ của bạn)
	// ---------------------------
	const changeBtn = document.querySelector('.change-btn');
	const addressModal = document.getElementById('addressModal');
	const cancelBtn = document.getElementById('cancelAddressBtn');
	const confirmBtn = document.getElementById('confirmAddressBtn');
	const openAddFormBtn = document.getElementById('openAddFormBtn');
	const addAddressForm = document.getElementById('addAddressForm');

	if (changeBtn) {
		changeBtn.addEventListener('click', e => {
			e.preventDefault();
			addressModal.classList.remove('hidden');
		});
	}

	if (cancelBtn) {
		cancelBtn.addEventListener('click', () => addressModal.classList.add('hidden'));
	}

	if (confirmBtn) {
		confirmBtn.addEventListener('click', () => {
			const selected = document.querySelector('input[name="selectedAddress"]:checked');
			if (!selected) {
				alert("Vui lòng chọn một địa chỉ giao hàng!");
				return;
			}

			const name = selected.dataset.name;
			const phone = selected.dataset.phone;
			const address = selected.dataset.address;

			// Cập nhật text hiển thị
			document.getElementById('selectedName').textContent = name;
			document.getElementById('selectedPhone').textContent = phone;
			document.getElementById('selectedAddress').textContent = address;

			// Cập nhật các input ẩn trong form để gửi đi
			form.querySelector('input[name="tenNguoiNhan"]').value = name;
			form.querySelector('input[name="sdt"]').value = phone;
			form.querySelector('input[name="diaChi"]').value = address;

			addressModal.classList.add('hidden');
		});
	}

	if (openAddFormBtn) {
		openAddFormBtn.addEventListener('click', () => {
			addAddressForm.classList.toggle('hidden');
		});
	}
});