// ====================
// GLOBAL VARIABLES
// ====================
let currentChart = null;
let currentChartType = 'revenue';
let currentRange = 'month';

// ====================
// FETCH DASHBOARD SUMMARY
// ====================
async function fetchDashboardStats() {
    try {
        const response = await fetch('/api/manager/dashboard/summary');
        const data = await response.json();
        
        // Cập nhật stat cards
        updateStatCards(data);
        
        return data;
    } catch (error) {
        console.error('Error fetching dashboard stats:', error);
        return null;
    }
}

// ====================
// UPDATE STAT CARDS
// ====================
function updateStatCards(data) {
    document.getElementById('totalRevenueStat').textContent = 
        formatCurrency(data.totalRevenue || 0);
    document.getElementById('totalEmployeesStat').textContent = 
        data.totalEmployees || 0;
    document.getElementById('totalCustomersStat').textContent = 
        data.totalCustomers || 0;
    document.getElementById('totalOrdersStat').textContent = 
        data.totalOrders || 0;
}

// ====================
// FORMAT CURRENCY
// ====================
function formatCurrency(value) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(value);
}

// ====================
// SHOW CHART BY TYPE
// ====================
async function showChart(type) {
    currentChartType = type;
    
    // Highlight active card
    $('.stat-card').removeClass('active');
    $(`.stat-card.${type}`).addClass('active');
    
    // Update chart title
    const titles = {
        revenue: 'Biểu đồ Doanh thu',
        employees: 'Biểu đồ Phân bố Nhân viên',
        customers: 'Biểu đồ Khách hàng mới',
        orders: 'Biểu đồ Đơn hàng'
    };
    $('#chartTitle').text(titles[type]);
    
    // Fetch and render chart
    const range = $('#timeRange').val();
    const dateRange = $('#customDateRange').val();
    
    if (range === 'custom' && dateRange) {
        const [start, end] = dateRange.split(' - ').map(d => 
            moment(d, 'DD/MM/YYYY').format('YYYY-MM-DD')
        );
        await updateChart(type, range, start, end);
    } else {
        await updateChart(type, range);
    }
}

// ====================
// UPDATE CHART
// ====================
async function updateChart(type = null, range = null, start = null, end = null) {
    const chartType = type || currentChartType;
    const timeRange = range || currentRange;
    
    let url = `/api/manager/dashboard/chart?type=${chartType}&range=${timeRange}`;
    
    if (start && end) {
        url += `&start=${start}&end=${end}`;
    }
    
    try {
        const response = await fetch(url);
        const chartData = await response.json();
        
        renderChart(chartData, chartType);
    } catch (error) {
        console.error('Error fetching chart data:', error);
    }
}

// ====================
// RENDER CHART
// ====================
function renderChart(data, type) {
    const ctx = document.getElementById('dashboardChart').getContext('2d');
    
    // Destroy existing chart
    if (currentChart) {
        currentChart.destroy();
    }
    
    // Chart configuration based on type
    const configs = {
        revenue: {
            type: 'line',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: data.values || [],
                    borderColor: '#4CAF50',
                    backgroundColor: 'rgba(76, 175, 80, 0.1)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: true },
                    tooltip: {
                        callbacks: {
                            label: (context) => formatCurrency(context.parsed.y)
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: (value) => formatCurrency(value)
                        }
                    }
                }
            }
        },
        
        employees: {
            type: 'pie',
            data: {
                labels: data.labels || ['Nhân viên bán hàng', 'Quản lý', 'Kho'],
                datasets: [{
                    data: data.values || [3, 1, 1],
                    backgroundColor: [
                        '#FF6384',
                        '#36A2EB',
                        '#FFCE56',
                        '#4BC0C0',
                        '#9966FF'
                    ]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'right' }
                }
            }
        },
        
        customers: {
            type: 'line',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: 'Khách hàng mới',
                    data: data.values || [],
                    borderColor: '#2196F3',
                    backgroundColor: 'rgba(33, 150, 243, 0.1)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: true }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }
        },
        
        orders: {
            type: 'bar',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: 'Số lượng đơn hàng',
                    data: data.values || [],
                    backgroundColor: '#FF9800',
                    borderColor: '#F57C00',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: true }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }
        }
    };
    
    currentChart = new Chart(ctx, configs[type]);
}

// ====================
// FETCH TOP PRODUCTS
// ====================
async function fetchTopProducts(limit = 5) {
    try {
        const range = $('#timeRange').val();
        const dateRange = $('#customDateRange').val();
        
        let url = `/api/manager/dashboard/top-products?limit=${limit}&range=${range}`;
        
        if (range === 'custom' && dateRange) {
            const [start, end] = dateRange.split(' - ').map(d => 
                moment(d, 'DD/MM/YYYY').format('YYYY-MM-DD')
            );
            url += `&start=${start}&end=${end}`;
        }
        
        const response = await fetch(url);
        const products = await response.json();
        
        renderTopProducts(products);
    } catch (error) {
        console.error('Error fetching top products:', error);
    }
}

// ====================
// RENDER TOP PRODUCTS SLIDER
// ====================
function renderTopProducts(products) {
    const container = $('#topProductsSlider');
    container.empty();
    
    if (!products || products.length === 0) {
        container.html('<p class="no-data">Chưa có dữ liệu</p>');
        return;
    }
    
    products.forEach((product, index) => {
        const card = `
            <div class="product-card">
                <div class="rank">#${index + 1}</div>
                <img src="${product.imageUrl || '/images/no-image.png'}" 
                     alt="${product.productName}"
                     onerror="this.src='/images/no-image.png'">
                <h4>${product.productName}</h4>
                <p class="quantity">Đã bán: ${product.totalQuantity}</p>
                <p class="revenue">${formatCurrency(product.totalRevenue)}</p>
            </div>
        `;
        container.append(card);
    });
}

// ====================
// FETCH RECENT ORDERS
// ====================
async function fetchRecentOrders(limit = 10) {
    try {
        const response = await fetch(`/api/manager/dashboard/recent-orders?limit=${limit}`);
        const orders = await response.json();
        
        renderRecentOrders(orders);
    } catch (error) {
        console.error('Error fetching recent orders:', error);
    }
}

// ====================
// RENDER RECENT ORDERS TABLE
// ====================
function renderRecentOrders(orders) {
    const tbody = $('#recentOrdersTable tbody');
    tbody.empty();
    
    if (!orders || orders.length === 0) {
        tbody.html('<tr><td colspan="6" class="no-data">Chưa có đơn hàng</td></tr>');
        return;
    }
    
    orders.forEach(order => {
        const statusClass = getStatusClass(order.status);
        const statusText = getStatusText(order.status);
        const row = `
            <tr>
                <td>#${order.orderId}</td>
                <td>${order.customerName}</td>
                <td>${formatCurrency(order.totalAmount)}</td>
                <td>${order.paymentMethod || 'COD'}</td>
                <td><span class="status ${statusClass}">${statusText}</span></td>
                <td>${formatDateTime(order.createdAt)}</td>
            </tr>
        `;
        tbody.append(row);
    });
}

// ====================
// HELPER FUNCTIONS
// ====================
function getStatusClass(status) {
    const statusMap = {
        'CHO_XAC_NHAN': 'pending',
        'DA_XAC_NHAN': 'confirmed',
        'DANG_GIAO': 'shipping',
        'HOAN_THANH': 'completed',
        'HUY': 'cancelled'
    };
    return statusMap[status] || 'pending';
}

function getStatusText(status) {
    const textMap = {
        'CHO_XAC_NHAN': 'Chờ xác nhận',
        'DA_XAC_NHAN': 'Đã xác nhận',
        'DANG_GIAO': 'Đang giao',
        'HOAN_THANH': 'Hoàn thành',
        'HUY': 'Đã hủy'
    };
    return textMap[status] || status;
}

function formatDateTime(dateTime) {
    return moment(dateTime).format('DD/MM/YYYY HH:mm');
}

// ====================
// TOGGLE CUSTOM DATE
// ====================
function toggleCustomDate() {
    const val = $('#timeRange').val();
    currentRange = val;
    
    if (val === 'custom') {
        $('#customDateRange').show();
    } else {
        $('#customDateRange').hide();
        // Update chart and products when range changes
        updateChart(currentChartType, val);
        fetchTopProducts(5);
    }
}

// ====================
// INIT ON PAGE LOAD
// ====================
$(document).ready(function() {
    // Load initial data
    fetchDashboardStats();
    showChart('revenue');
    fetchTopProducts(5);
    fetchRecentOrders(10);
    
    // Setup auto-refresh every 5 minutes
    setInterval(() => {
        fetchDashboardStats();
        fetchRecentOrders(10);
    }, 300000);
});