/* ================================================================
   home.js  —  JavaScript for index.html (the home page)
   Load AFTER main.js
   ================================================================ */

/* ---------------------------------------------------------------
   1. SCROLL-IN ANIMATION FOR LOCATION ROWS
   Adds .visible class when a row enters the viewport
--------------------------------------------------------------- */
(function () {
  const rows = document.querySelectorAll('.location-row');
  if (!rows.length) return;
  rows.forEach((row) => row.classList.add('will-animate'));

  // Use IntersectionObserver for smooth scroll-reveal
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');
          observer.unobserve(entry.target); // only trigger once
        }
      });
    },
    { threshold: 0.1} 
  );

  rows.forEach((row) => observer.observe(row));
})();


/* ---------------------------------------------------------------
   2. REVIEWS CAROUSEL
   Handles sliding, dots, and arrow buttons
--------------------------------------------------------------- */
(function () {
  const track    = document.getElementById('reviewsTrack');
  const dotsWrap = document.getElementById('reviewDots');
  const prevBtn  = document.querySelector('.rev-prev');
  const nextBtn  = document.querySelector('.rev-next');

  if (!track) return;

  // ------- Load reviews from backend -------
  // When backend is ready, uncomment this block and remove placeholder cards from HTML:
  /*
  async function loadReviews() {
    try {
      const res  = await fetch('ReviewServlet');        // your servlet URL
      const data = await res.json();                    // expects array of review objects

      track.innerHTML = '';                             // clear placeholders

      data.forEach((review) => {
        const card = document.createElement('div');
        card.classList.add('review-card');
        card.innerHTML = `
          <div class="review-header">
            <span class="review-codename">${escapeHTML(review.codename)}</span>
            <span class="review-location">${escapeHTML(review.location)}</span>
          </div>
          <p class="review-text">"${escapeHTML(review.reviewText)}"</p>
          ${review.adminReply ? `
          <div class="review-reply">
            <span class="reply-label">Hotel Tranquility</span>
            <p>"${escapeHTML(review.adminReply)}"</p>
          </div>` : ''}
        `;
        track.appendChild(card);
      });

      initCarousel();  // build dots after cards are loaded
    } catch (err) {
      console.error('Could not load reviews:', err);
      initCarousel(); // still initialise with placeholder cards
    }
  }
  loadReviews();
  */

  // ------- Carousel logic -------
  initCarousel(); // call directly while using placeholder cards

  function initCarousel() {
    const cards = Array.from(track.querySelectorAll('.review-card'));
    if (!cards.length) return;

    let current = 0;

    // Build dot buttons
    dotsWrap.innerHTML = '';
    cards.forEach((_, idx) => {
      const dot = document.createElement('button');
      dot.classList.add('review-dot');
      dot.setAttribute('aria-label', `Review ${idx + 1}`);
      dot.addEventListener('click', () => goTo(idx));
      dotsWrap.appendChild(dot);
    });

    const dots = Array.from(dotsWrap.querySelectorAll('.review-dot'));

    function goTo(idx) {
      current = (idx + cards.length) % cards.length;
      track.style.transform = `translateX(-${current * 100}%)`;
      dots.forEach((d, k) => d.classList.toggle('active', k === current));
    }

    // Init first
    goTo(0);

    // Arrow buttons
    prevBtn?.addEventListener('click', () => goTo(current - 1));
    nextBtn?.addEventListener('click', () => goTo(current + 1));

    // Auto-advance every 6 seconds
    let autoTimer = setInterval(() => goTo(current + 1), 6000);

    const carousel = document.getElementById('reviewsCarousel');
    carousel?.addEventListener('mouseenter', () => clearInterval(autoTimer));
    carousel?.addEventListener('mouseleave', () => {
      autoTimer = setInterval(() => goTo(current + 1), 6000);
    });
  }
})();


/* ---------------------------------------------------------------
   3. REVIEW SUBMIT FORM
   Sends review data to backend servlet
--------------------------------------------------------------- */
function submitReview(event) {
  event.preventDefault();

  const email    = document.getElementById('reviewEmail').value.trim();
  const codename = document.getElementById('reviewCodename').value.trim();
  const location = document.getElementById('reviewLocation').value.trim();
  const text     = document.getElementById('reviewText').value.trim();
  const feedback = document.getElementById('reviewFeedback');

  // Basic client-side validation
  if (!email || !codename || !location || !text) {
    feedback.textContent = 'Please fill in all fields.';
    feedback.style.color = '#c0392b';
    return;
  }

  // ------- Send to backend -------
  // When backend is ready, uncomment and use this fetch block:
  /*
  const formData = new FormData();
  formData.append('email',      email);
  formData.append('codename',   codename);
  formData.append('location',   location);
  formData.append('reviewText', text);

  fetch('SubmitReviewServlet', {     // your servlet URL
    method: 'POST',
    body: formData,
  })
    .then((res) => {
      if (res.ok) {
        feedback.textContent = 'Thank you! Your review has been submitted for approval.';
        feedback.style.color = 'var(--accent)';
        document.getElementById('reviewForm').reset();
      } else {
        feedback.textContent = 'Something went wrong. Please try again.';
        feedback.style.color = '#c0392b';
      }
    })
    .catch(() => {
      feedback.textContent = 'Network error. Please try again.';
      feedback.style.color = '#c0392b';
    });
  */

  // TEMPORARY (remove when backend is ready):
  feedback.textContent = 'Thank you! Your review has been submitted for approval.';
  feedback.style.color = 'var(--accent)';
  document.getElementById('reviewForm').reset();
}


/* ---------------------------------------------------------------
   HELPER: Escape HTML to prevent XSS when inserting user content
--------------------------------------------------------------- */
function escapeHTML(str) {
  const div = document.createElement('div');
  div.textContent = str;
  return div.innerHTML;
}