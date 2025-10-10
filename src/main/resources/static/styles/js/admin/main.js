	document.getElementById('profileForm').addEventListener('submit', function(e) {
		e.preventDefault(); // ngăn reload trang

	const form = e.target;
	const formData = new FormData(form);

	fetch(form.action, {
		method: 'POST',
	body: formData
    })
    .then(res => res.json())
    .then(data => {
        const alertBox = document.querySelector('.alert');
	if (!alertBox) return;

	alertBox.style.display = 'block';
	alertBox.className = 'alert ' + (data.success ? 'alert-success' : 'alert-error');
	alertBox.innerHTML = `
	<i class="fa-solid ${data.success ? 'fa-circle-check' : 'fa-circle-exclamation'}"></i>
	<span>${data.message}</span>
	`;

	// cập nhật avatar nếu có
	if (data.success && data.user && data.user.imageUrl) {
		document.getElementById('avatarPreview').src = '/images/' + data.user.imageUrl;
        }
    })
    .catch(err => console.error(err));
});
