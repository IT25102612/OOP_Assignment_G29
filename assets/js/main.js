// Theme init
(function () {
  const saved = localStorage.getItem("theme");
  const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
  const start = saved || (prefersDark ? "dark" : "light");
  setTheme(start);
})();

function setTheme(mode) {
  const root = document.documentElement;
  const btn = document.getElementById("themeToggle");

  if (mode === "dark") {
    root.classList.add("theme-dark");
    btn.textContent = "☀️";
    localStorage.setItem("theme", "dark");
  } else {
    root.classList.remove("theme-dark");
    btn.textContent = "🌙";
    localStorage.setItem("theme", "light");
  }
}

document.getElementById("themeToggle").addEventListener("click", () => {
  const dark = document.documentElement.classList.contains("theme-dark");
  setTheme(dark ? "light" : "dark");
});

/* ===== Simple Carousel (auto + arrows) ===== */
(function () {
  const container = document.querySelector('.hero-banner');
  if (!container) return;

  const slides = Array.from(container.querySelectorAll('.hero-slide'));
  const prevBtn = container.querySelector('.hero-arrow.prev');
  const nextBtn = container.querySelector('.hero-arrow.next');

  let i = slides.findIndex(s => s.classList.contains('is-active'));
  if (i < 0) i = 0;

  const DURATION = 4500;   // ms between auto changes
  let timer = null;

  function show(idx) {
    slides.forEach((s, k) => s.classList.toggle('is-active', k === idx));
    i = idx;
  }

  function next() { show((i + 1) % slides.length); }
  function prev() { show((i - 1 + slides.length) % slides.length); }

  function start() { stop(); timer = setInterval(next, DURATION); }
  function stop() { if (timer) clearInterval(timer); timer = null; }

  // Init
  show(i);
  start();

  // Click arrows
  nextBtn?.addEventListener('click', () => { next(); start(); });
  prevBtn?.addEventListener('click', () => { prev(); start(); });

  // Pause on hover (optional, feels nicer)
  container.addEventListener('mouseenter', stop);
  container.addEventListener('mouseleave', start);

  // If you later change slides dynamically, you can call show(newIndex)
})();