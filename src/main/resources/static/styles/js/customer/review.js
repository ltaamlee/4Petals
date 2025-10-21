function openReviewPopup(button) {
  const orderId = button.getAttribute('data-order-id');
  document.getElementById('orderId').value = orderId;
  document.getElementById('reviewModal').classList.remove('hidden');
}

function closeReviewPopup() {
  document.getElementById('reviewModal').classList.add('hidden');
}

window.addEventListener('click', function (e) {
  const modal = document.getElementById('reviewModal');
  if (e.target === modal) closeReviewPopup();
});

document.getElementById('reviewForm').addEventListener('submit', function (e) {
  e.preventDefault();

  const orderId = document.getElementById('orderId').value;
  const rating = document.querySelector('input[name="rating"]:checked')?.value;
  const comment = this.comment.value.trim();

  if (!rating) {
    alert('Vui lòng chọn số sao đánh giá.');
    return;
  }

  // TODO: Gửi request tới API đánh giá (ví dụ)
  // fetch(`/customer/orders/${orderId}/review`, { ... })

  alert(`Đã gửi đánh giá cho đơn #${orderId} (${rating}⭐): ${comment}`);
  closeReviewPopup();
});
