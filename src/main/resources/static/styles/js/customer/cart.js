
document.addEventListener("DOMContentLoaded", () => {

	// === Cập nhật tổng tiền trong trang ===
	const updateSummary = () => {
		let subtotal = 0;

		document.querySelectorAll(".cart-item").forEach(item => {
			const checkbox = item.querySelector("input[type='checkbox']");
			const priceText = item.querySelector(".info p").innerText.replace(/[^\d]/g, '');
			const quantity = parseInt(item.querySelector(".quantity input").value);

			if (checkbox && checkbox.checked) {
				subtotal += parseFloat(priceText) * quantity;
			}
		});

		const vat = subtotal * 0.1;
		const total = subtotal + vat;

		document.querySelector(".summary span:nth-of-type(1)").innerText = subtotal.toLocaleString("vi-VN") + " ₫";
		document.querySelector(".summary span:nth-of-type(2)").innerText = vat.toLocaleString("vi-VN") + " ₫";
		document.querySelector(".summary .total strong").innerText = total.toLocaleString("vi-VN") + " ₫";
	};


	// === Tăng/giảm số lượng ===
	document.querySelectorAll(".plus, .minus").forEach(btn => {
		btn.addEventListener("click", e => {
			const input = e.target.closest(".quantity").querySelector("input");
			let qty = parseInt(input.value);
			if (e.target.classList.contains("plus")) qty++;
			else if (qty > 1) qty--;

			input.value = qty;
			updateSummary();

			// Gửi AJAX cập nhật database
			fetch("/cart/update", {
				method: "POST",
				headers: { "Content-Type": "application/x-www-form-urlencoded" },
				body: `id=${input.dataset.id}&qty=${qty}`
			});
		});
	});


	// === Khi gõ số lượng trực tiếp ===
	document.querySelectorAll(".quantity input").forEach(inp => {
		inp.addEventListener("change", e => {
			let qty = parseInt(e.target.value);
			if (isNaN(qty) || qty < 1) qty = 1;
			e.target.value = qty;
			updateSummary();

			fetch("/cart/update", {
				method: "POST",
				headers: { "Content-Type": "application/x-www-form-urlencoded" },
				body: `id=${inp.dataset.id}&qty=${qty}`
			});
		});
	});


	// === Xóa sản phẩm ===
	document.querySelectorAll(".remove").forEach(btn => {
		btn.addEventListener("click", e => {
			const id = btn.dataset.id;
			btn.closest(".cart-item").remove();
			updateSummary();

			fetch("/cart/remove", {
				method: "POST",
				headers: { "Content-Type": "application/x-www-form-urlencoded" },
				body: `id=${id}`
			});
		});
	});


	// === Bỏ chọn checkbox ===
	document.querySelectorAll(".cart-item input[type='checkbox']").forEach(cb => {
		cb.addEventListener("change", updateSummary);
	});

	// Cập nhật tổng ban đầu
	updateSummary();
});

