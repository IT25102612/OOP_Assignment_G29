/* ================================================================
   city.js  —  JavaScript for city.html
   Reads ?city=paris from the URL, looks up CITY_DATA,
   and populates the entire page dynamically.
   Load AFTER main.js and city-data.js
   ================================================================ */

(function () {

  /* ---------------------------------------------------------------
     1. READ CITY KEY FROM URL
     e.g. city.html?city=paris  →  key = 'paris'
  --------------------------------------------------------------- */
  const params = new URLSearchParams(window.location.search);
  const key    = params.get('city');
  const data   = key && CITY_DATA[key];

  // If unknown city, redirect home
  if (!data) {
    window.location.href = 'index.html';
    return;
  }

  /* ---------------------------------------------------------------
     2. SET PAGE TITLE
  --------------------------------------------------------------- */
  document.title = `${data.name}, ${data.country} — Tranquility`;

  /* ---------------------------------------------------------------
     3. POPULATE BANNER
  --------------------------------------------------------------- */
  const slidesWrap = document.getElementById('citySlides');
  data.slides.forEach((src, idx) => {
    const fig = document.createElement('figure');
    fig.classList.add('hero-slide');
    if (idx === 0) fig.classList.add('is-active');
    fig.innerHTML = `<img src="${src}" alt="${data.name} view ${idx + 1}" />`;
    slidesWrap.appendChild(fig);
  });

  document.getElementById('cityBannerName').textContent    = data.name;
  document.getElementById('cityBannerCountry').textContent = data.country;

  /* ---------------------------------------------------------------
     4. POPULATE BODY CONTENT
  --------------------------------------------------------------- */
  document.getElementById('cityIntro').textContent     = data.intro;
  document.getElementById('cityCtaName').textContent   = data.name;

  const ul = document.getElementById('cityThings');
  data.things.forEach((thing) => {
    const li = document.createElement('li');
    li.textContent = thing;
    ul.appendChild(li);
  });

  /* ---------------------------------------------------------------
     5. BACK BUTTON — returns to home page scrolled to the
        exact city button the user clicked.
        The anchor is the country section id, e.g. #loc-france
  --------------------------------------------------------------- */
  const countrySlug = data.country.toLowerCase().replace(/\s+/g, '-');
  const backBtn     = document.getElementById('backBtn');
  backBtn.href      = `index.html#loc-${countrySlug}`;

  /* ---------------------------------------------------------------
     6. CAROUSEL (reuses same logic as main.js hero carousel)
  --------------------------------------------------------------- */
  (function () {
    const container = document.querySelector('.city-banner');
    if (!container) return;

    const slides = Array.from(container.querySelectorAll('.hero-slide'));
    const prevBtn = container.querySelector('.hero-arrow.prev');
    const nextBtn = container.querySelector('.hero-arrow.next');

    let i = 0;
    const DURATION = 4500;
    let timer = null;

    function show(idx) {
      slides.forEach((s, k) => s.classList.toggle('is-active', k === idx));
      i = idx;
    }
    function next() { show((i + 1) % slides.length); }
    function prev() { show((i - 1 + slides.length) % slides.length); }
    function start() { stop(); timer = setInterval(next, DURATION); }
    function stop()  { if (timer) clearInterval(timer); timer = null; }

    show(0);
    start();

    nextBtn?.addEventListener('click', () => { next(); start(); });
    prevBtn?.addEventListener('click', () => { prev(); start(); });
    container.addEventListener('mouseenter', stop);
    container.addEventListener('mouseleave', start);
  })();

})();