
const DEMO = {
	revenueByMonth: [
		{ month: '01/2025', value: 120 }, { month: '02/2025', value: 135 }, { month: '03/2025', value: 98 },
		{ month: '04/2025', value: 150 }, { month: '05/2025', value: 175 }, { month: '06/2025', value: 162 },
		{ month: '07/2025', value: 190 }, { month: '08/2025', value: 210 }, { month: '09/2025', value: 198 },
		{ month: '10/2025', value: 223 }, { month: '11/2025', value: 0 }, { month: '12/2025', value: 0 }
	],
	employeesPie: [
		{ label: 'Bán hàng', value: 5 }, { label: 'Kho', value: 3 }, { label: 'Shipper', value: 2 },
		{ label: 'Quản lý', value: 1 }, { label: 'Khác', value: 1 }
	],
	customersByMonth: [
		{ month: '01/2025', value: 20 }, { month: '02/2025', value: 23 }, { month: '03/2025', value: 18 },
		{ month: '04/2025', value: 26 }, { month: '05/2025', value: 29 }, { month: '06/2025', value: 31 },
		{ month: '07/2025', value: 34 }, { month: '08/2025', value: 39 }, { month: '09/2025', value: 41 },
		{ month: '10/2025', value: 44 }, { month: '11/2025', value: 0 }, { month: '12/2025', value: 0 }
	],
	ordersByDayOct2025: Array.from({ length: 31 }, (_, i) => {
		const d = i + 1;
		const val = Math.max(2, Math.min(18, Math.round(8 + 6 * Math.sin(i / 3) + (i % 5) - (i % 7) / 2)));
		return { day: `${String(d).padStart(2, '0')}/10/2025`, value: val };
	}),
	topProducts: [
		{ name: 'Hoa Hồng Đà Lạt', sold: 128, price: 350000, img: 'https://picsum.photos/seed/rose/320/200' },
		{ name: 'Tulip Hà Lan', sold: 97, price: 420000, img: 'https://picsum.photos/seed/tulip/320/200' },
		{ name: 'Hoa Ly Trắng', sold: 76, price: 280000, img: 'https://picsum.photos/seed/lily/320/200' },
		{ name: 'Cẩm Chướng', sold: 64, price: 220000, img: 'https://picsum.photos/seed/carnation/320/200' },
		{ name: 'Lan Hồ Điệp', sold: 55, price: 520000, img: 'https://picsum.photos/seed/orchid/320/200' },
		{ name: 'Baby Breath', sold: 49, price: 180000, img: 'https://picsum.photos/seed/baby/320/200' }
	],
	recentOrders: [
		{ code: 'DH10245', customer: 'Nguyễn Văn A', date: '20/10/2025 14:22', total: 1250000, status: 'Đã đóng đơn' },
		{ code: 'DH10246', customer: 'Trần Thị B', date: '20/10/2025 15:05', total: 820000, status: 'Đang xử lý' },
		{ code: 'DH10247', customer: 'Lê Hoàng C', date: '21/10/2025 09:18', total: 1570000, status: 'Đã thanh toán' },
		{ code: 'DH10248', customer: 'Phạm Minh D', date: '21/10/2025 10:02', total: 640000, status: 'Đang giao' },
		{ code: 'DH10249', customer: 'Võ Gia E', date: '22/10/2025 08:41', total: 910000, status: 'Đã đóng đơn' }
	]
};

// =========================
// SLIDER TOP PRODUCTS
// =========================
function renderTopProducts() {
	const wrap = document.getElementById('topProductsSlider');
	if (!wrap) return;
	wrap.innerHTML = '';
	DEMO.topProducts.forEach(p => {
		const card = document.createElement('div');
		card.style.minWidth = '240px'; card.style.flex = '0 0 auto';
		card.style.border = '1px solid #eee'; card.style.borderRadius = '12px';
		card.style.overflow = 'hidden'; card.style.boxShadow = '0 2px 8px rgba(0,0,0,0.06)';
		card.innerHTML = `
      <img src="${p.img}" alt="${p.name}" style="width:100%;height:140px;object-fit:cover;">
      <div style="padding:12px;">
        <div style="font-weight:600;font-size:14px;margin-bottom:6px;">${p.name}</div>
        <div style="display:flex;justify-content:space-between;font-size:13px;">
          <span>Đã bán: <b>${p.sold}</b></span>
          <span>${p.price.toLocaleString('vi-VN')}₫</span>
        </div>
      </div>`;
		wrap.appendChild(card);
	});
	const slider = wrap;
	document.getElementById('btnPrev')?.addEventListener('click', () => slider.scrollBy({ left: -260, behavior: 'smooth' }));
	document.getElementById('btnNext')?.addEventListener('click', () => slider.scrollBy({ left: 260, behavior: 'smooth' }));
}

// =========================
// BẢNG ĐƠN HÀNG GẦN ĐÂY
// =========================
function renderRecentOrders() {
	const tbody = document.getElementById('recentOrdersBody');
	if (!tbody) return;
	tbody.innerHTML = DEMO.recentOrders.map(o => `
    <tr>
      <td style="padding:8px;border-bottom:1px solid #f4f4f4;">${o.code}</td>
      <td style="padding:8px;border-bottom:1px solid #f4f4f4;">${o.customer}</td>
      <td style="padding:8px;border-bottom:1px solid #f4f4f4;">${o.date}</td>
      <td style="padding:8px;border-bottom:1px solid #f4f4f4;text-align:right;">${o.total.toLocaleString('vi-VN')}₫</td>
      <td style="padding:8px;border-bottom:1px solid #f4f4f4;text-align:center;">
        <span style="padding:4px 8px;border-radius:999px;background:#f3f6ff;border:1px solid #dfe7ff;">${o.status}</span>
      </td>
    </tr>
  `).join('');
}

// =========================
// CHART.JS (revenue/employee/customer/orders)
// =========================
let currentChartType = 'revenue';
let chartInstance = null;

function makeLineConfig(labels, data, label) {
	return {
		type: 'line',
		data: { labels, datasets: [{ label, data, tension: 0.3, fill: false, pointRadius: 3, borderWidth: 2 }] },
		options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: true } }, scales: { x: { grid: { display: false } }, y: { beginAtZero: true } } }
	};
}
function makePieConfig(labels, data, label) {
	return {
		type: 'pie',
		data: { labels, datasets: [{ label, data }] },
		options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'right' } } }
	};
}
function destroyIfAny() { if (chartInstance) { chartInstance.destroy(); chartInstance = null; } }
function setChartTitle(t) { const el = document.getElementById('chartTitle'); if (el) el.textContent = t; }

function updateChart() {
	const ctx = document.getElementById('dashboardChart');
	if (!ctx) return;
	if (currentChartType === 'revenue') {
		setChartTitle('Biểu đồ Doanh thu');
		destroyIfAny();
		chartInstance = new Chart(ctx, makeLineConfig(DEMO.revenueByMonth.map(x => x.month), DEMO.revenueByMonth.map(x => x.value), 'Doanh thu (triệu)'));
	} else if (currentChartType === 'employees') {
		setChartTitle('Cơ cấu Nhân viên (giả lập)');
		destroyIfAny();
		chartInstance = new Chart(ctx, makePieConfig(DEMO.employeesPie.map(x => x.label), DEMO.employeesPie.map(x => x.value), 'Nhân viên'));
	} else if (currentChartType === 'customers') {
		setChartTitle('Khách hàng mới theo tháng ');
		destroyIfAny();
		chartInstance = new Chart(ctx, makeLineConfig(DEMO.customersByMonth.map(x => x.month), DEMO.customersByMonth.map(x => x.value), 'Khách hàng mới'));
	} else if (currentChartType === 'orders') {
		setChartTitle('Số lượng đơn hàng theo ngày (10/2025)');
		destroyIfAny();
		chartInstance = new Chart(ctx, makeLineConfig(DEMO.ordersByDayOct2025.map(x => x.day), DEMO.ordersByDayOct2025.map(x => x.value), 'Đơn hàng/ngày'));
	}
}

// Click vào các thẻ số liệu để đổi chart
function showChart(type) { currentChartType = type; updateChart(); }
window.showChart = showChart; // để HTML gọi được

// =========================
// BOOT
// =========================
document.addEventListener('DOMContentLoaded', () => {
	renderTopProducts();
	renderRecentOrders();
	showChart('revenue'); // chart mặc định
});