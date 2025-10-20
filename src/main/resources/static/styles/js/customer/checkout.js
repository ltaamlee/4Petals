document.addEventListener("DOMContentLoaded", () => {
	const tabs = document.querySelectorAll(".payment-tab");
	const qrBox = document.getElementById("qrBox");
	const methodInput = document.getElementById("paymentMethodInput");

	tabs.forEach(tab => {
		tab.addEventListener("click", () => {
			tabs.forEach(t => t.classList.remove("active"));
			tab.classList.add("active");
			const val = tab.querySelector("input").value;
			methodInput.value = val;
			qrBox.style.display = val === "QR" ? "block" : "none";
		});
	});
});

document.addEventListener("DOMContentLoaded", () => {
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

			document.getElementById('selectedName').textContent = name;
			document.getElementById('selectedPhone').textContent = phone;
			document.getElementById('selectedAddress').textContent = address;

			document.getElementById('tenNguoiNhan').value = name;
			document.getElementById('sdt').value = phone;
			document.getElementById('diaChi').value = address;

			addressModal.classList.add('hidden');
		});
	}

	if (openAddFormBtn) {
		openAddFormBtn.addEventListener('click', () => {
			addAddressForm.classList.toggle('hidden');
		});
	}
});
