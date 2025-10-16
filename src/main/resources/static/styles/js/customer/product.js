
const minus = document.getElementById('minus');
const plus = document.getElementById('plus');
const qty = document.getElementById('qty');
minus.onclick = () => qty.value = Math.max(1, qty.value - 1);
plus.onclick = () => qty.value++;

const id = /*[[${product.maSP}]]*/ 0;   

document.getElementById('addCart').onclick = () => {
    fetch('/product/add-to-cart', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `productId=${id}&quantity=${qty.value}`
    }).then(res => res.text()).then(alert);
};

document.getElementById('buyNow').onclick = () => {
    window.location.href = `/product/buy-now/${id}`;
};

