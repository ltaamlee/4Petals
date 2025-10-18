
// REVIEW ẢNH
function previewAvatar(event) {
	const reader = new FileReader();
	reader.onload = function() {
		document.getElementById('avatarPreview').src = reader.result;
	}
	reader.readAsDataURL(event.target.files[0]);
}

// HIỂN THỊ THÔNG BÁO
function showAlert(success, message) {
	const alertBox = document.getElementById('alertBox');
	const icon = document.getElementById('alertIcon');
	const msg = document.getElementById('alertMessage');

	alertBox.style.display = 'block';
	msg.textContent = message;

	if (success) {
		alertBox.className = 'alert alert-success';
		icon.className = 'fa-solid fa-circle-check';
	} else {
		alertBox.className = 'alert alert-error';
		icon.className = 'fa-solid fa-circle-exclamation';
	}

	setTimeout(() => alertBox.style.display = 'none', 4000);
}

// SUBMIT FORM AJAX
document.getElementById('profileForm').addEventListener('submit', function(e) {
	e.preventDefault();

	const formData = new FormData(this);
	const url = '/admin/profile/update';
	showSpinner();

	fetch(url, { method: 'POST', body: formData })
		.then(res => {
			if (!res.ok) throw new Error('Lỗi kết nối đến máy chủ!');
			return res.json();
		})
		.then(result => {
			const avatar = document.getElementById('avatarPreview');

			if (result.success && result.data && result.data.imageUrl) {
				// Cập nhật ảnh mới
				avatar.src = '/images/' + result.data.imageUrl;

				// Chờ ảnh load xong mới show alert + hide spinner
				avatar.onload = () => {
					showAlert(result.success, result.message);
					hideSpinner();
				};

				// Load ảnh lỗi
				avatar.onerror = () => {
					showAlert(false, 'Upload thành công nhưng không thể load ảnh!');
					hideSpinner();
				};
			} else {
				// Trường hợp không có imageUrl
				showAlert(result.success, result.message);
				hideSpinner();
			}
		})
		.catch(err => {
			console.error('Lỗi khi gửi dữ liệu:', err);
			showAlert(false, 'Không thể gửi yêu cầu: ' + err.message);
			hideSpinner();
		});
});
