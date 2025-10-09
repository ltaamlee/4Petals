document.addEventListener("DOMContentLoaded", function () {
  const input = document.getElementById("avatar-input");
  const preview = document.getElementById("avatar-preview");
  const saveBtn = document.getElementById("save-btn");
  const cancelBtn = document.getElementById("cancel-btn");
  const form = document.querySelector(".avatar-card form");

  let originalSrc = preview.src;

  // Khi chọn ảnh mới
  input.addEventListener("change", function (e) {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function () {
        preview.src = reader.result;
        saveBtn.style.display = "inline-block";
        cancelBtn.style.display = "inline-block";
      };
      reader.readAsDataURL(file);
    }
  });

  // Hủy upload → quay lại ảnh cũ
  cancelBtn.addEventListener("click", function (e) {
    e.preventDefault();
    preview.src = originalSrc;
    input.value = "";
    saveBtn.style.display = "none";
    cancelBtn.style.display = "none";
  });

  // Lưu ảnh
  form.addEventListener("submit", function (e) {
    if (!input.files[0]) {
      e.preventDefault();
      alert("Vui lòng chọn ảnh trước khi lưu!");
    }
  });
});
