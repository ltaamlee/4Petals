function loadPermissions(userId) {
    fetch('/admin/users/' + userId + '/permissions')
        .then(res => res.json())
        .then(data => {
            const table = document.getElementById('permissionTable');
            document.getElementById('permUserId').value = userId;

            // Xóa bảng cũ
            table.querySelector('tbody')?.remove();

            // Tạo tbody mới
            const tbody = document.createElement('tbody');

            data.forEach(perm => {
                const tr = document.createElement('tr');

                const tdCheck = document.createElement('td');
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.name = 'permissionIds';
                checkbox.value = perm.permissionId;
                if(perm.assigned) checkbox.checked = true;
                tdCheck.appendChild(checkbox);

                const tdName = document.createElement('td');
                tdName.textContent = perm.name;

                tr.appendChild(tdCheck);
                tr.appendChild(tdName);
                tbody.appendChild(tr);
            });

            table.appendChild(tbody);
        })
        .catch(err => console.error(err));
};
