document.addEventListener("DOMContentLoaded", function() {
    let currentOrderId = null;

    window.showOrderDetail = function(maDH) {
        currentOrderId = maDH;
        fetch(`/shipper/${maDH}/details`) 
          .then(res => res.json())
          .then(data => {
            if (data.error) {
                alert(data.error);
                return;
            }
            const sanPhamList = data.sanPham ? data.sanPham.split(', ').map(item => {
                const match = item.match(/(.+) \(x(\d+)\)/);
                if (match) {
                    const tenSP = match[1];
                    const soLuong = match[2];
                    return `- ${tenSP} (SL: ${soLuong})`;
                }
                return item;
            }).join('<br>') : "";
            document.getElementById("maDH").innerText = maDH;
            document.getElementById("tenKhachHang").innerText = data.tenKhachHang || "";
            document.getElementById("diaChi").innerText = data.diaChi || "";
            document.getElementById("soDienThoai").innerText = data.soDienThoai || "";
            
            document.getElementById("phuongThucThanhToan").innerText = data.phuongThucThanhToan || ""; 
            document.getElementById("ghiChu").innerText = data.ghiChu || "Không có";

            const sanPhamElement = document.getElementById("sanPhamDetails");
            if (sanPhamElement) {
                sanPhamElement.innerHTML = sanPhamList;
            }
            
            let thanhTienFormatted = "";
            if (data.thanhTien) {
                 const thanhTienNum = parseFloat(data.thanhTien);
                 thanhTienFormatted = thanhTienNum.toLocaleString('vi-VN') + " VNĐ";
            }
            document.getElementById("thanhTien").innerText = thanhTienFormatted;
            const toastEl = document.getElementById("orderDetailToast");
            const toast = new bootstrap.Toast(toastEl, { autohide: false });
            toast.show();
          })
          .catch((error) => {
            console.error("Lỗi khi tải chi tiết đơn hàng:", error);
            alert("Không thể tải chi tiết đơn hàng.");
          });
    };
    
});