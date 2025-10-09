
document.addEventListener("DOMContentLoaded", () => {
  /* ---------------- SLIDER ---------------- */
  const slides = document.querySelectorAll(".slide");
  const dots = document.querySelectorAll(".dot");
  let index = 0;

  function showSlide(i) {
    slides.forEach((s, idx) => {
      s.classList.toggle("active", idx === i);
      dots[idx].classList.toggle("active", idx === i);
    });
  }

  function nextSlide() {
    index = (index + 1) % slides.length;
    showSlide(index);
  }

  function prevSlide() {
    index = (index - 1 + slides.length) % slides.length;
    showSlide(index);
  }

  const nextBtn = document.querySelector(".next");
  const prevBtn = document.querySelector(".prev");

  if (nextBtn && prevBtn) {
    nextBtn.addEventListener("click", nextSlide);
    prevBtn.addEventListener("click", prevSlide);
  }

  dots.forEach((dot, i) => dot.addEventListener("click", () => {
    index = i;
    showSlide(index);
  }));

  if (slides.length > 0) setInterval(nextSlide, 3000);

  
  
  /* ---------------- AVATAR DROPDOWN ---------------- */
  const avatarBtn = document.getElementById("avatar-btn");
  const menu = document.querySelector(".user-menu");

  if (avatarBtn && menu) {
    avatarBtn.addEventListener("click", (e) => {
      e.stopPropagation();
      menu.classList.toggle("active");
    });

    document.addEventListener("click", (e) => {
      if (!menu.contains(e.target)) {
        menu.classList.remove("active");
      }
    });
  }
});

