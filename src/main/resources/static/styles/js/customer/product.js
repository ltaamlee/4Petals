
document.addEventListener("DOMContentLoaded", function() {
    const minus = document.getElementById("minus");
    const plus = document.getElementById("plus");
    const qty = document.getElementById("qty");
    const addCart = document.getElementById("addCart");
    const buyNow = document.getElementById("buyNow");
    const cartIconCount = document.getElementById("cart-count"); // phần hiển thị số trên giỏ hàng

    // ====== Tăng / Giảm số lượng ======
    minus.addEventListener("click", () => {
        let value = parseInt(qty.value) || 1;
        if (value > 1) qty.value = value - 1;
    });

    plus.addEventListener("click", () => {
        let value = parseInt(qty.value) || 1;
        qty.value = value + 1;
    });

    // ====== Thêm vào giỏ hàng ======
    addCart.addEventListener("click", () => {
        const productId = addCart.dataset.id;
        const quantity = qty.value;

        fetch("/product/add-to-cart", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: `productId=${productId}&quantity=${quantity}`
        })
        .then(res => res.text())
        .then(msg => {
            alert(msg);
            updateCartCount();
        })
        .catch(() => alert("Lỗi khi thêm vào giỏ hàng"));
    });

    // ====== Mua ngay ======
    buyNow.addEventListener("click", () => {
        const productId = buyNow.dataset.id;
        window.location.href = `/product/buy-now/${productId}`;
    });

    // ====== Cập nhật số lượng hiển thị trên icon giỏ hàng ======
    function updateCartCount() {
        fetch("/cart/count")
        .then(res => res.json())
        .then(data => {
            if (cartIconCount) {
                cartIconCount.textContent = data.count > 99 ? "99+" : data.count;
                cartIconCount.style.display = data.count > 0 ? "inline-block" : "none";
            }
        });
    }

    // Load khi vào trang
    updateCartCount();
});

document.getElementById('buyNow').onclick = () => {
    window.location.href = `/product/buy-now/${id}`;
};

