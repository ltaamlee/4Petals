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

document.getElementById('reviewForm').addEventListener('submit', async function (e) {
  e.preventDefault();

  const orderId = document.getElementById('orderId').value;
  const rating = document.querySelector('input[name="rating"]:checked')?.value;
  const comment = this.comment.value.trim();

  if (!rating) {
    alert('Vui lòng chọn số sao đánh giá.');
    return;
  }

  try {
    const response = await fetch(`/customer/orders/${orderId}/review`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        rating: rating,
        comment: comment
      })
    });

    if (response.ok) {
      alert('🌸 Cảm ơn bạn đã đánh giá sản phẩm!');
      closeReviewPopup();
    } else {
      const text = await response.text();
      alert('Lỗi khi gửi đánh giá: ' + text);
    }
  } catch (error) {
    console.error('Fetch error:', error);
    alert('Không thể gửi đánh giá. Vui lòng thử lại!');
  }
});
