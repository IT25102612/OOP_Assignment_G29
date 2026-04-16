/* ================================================================
   rooms.js  —  JavaScript for available-rooms.html
   Load AFTER main.js
   ================================================================ */

/* ---------------------------------------------------------------
   CITY DATA (keyed by country name matching the <select> options)
--------------------------------------------------------------- */
const CITY_MAP = {
  'France':        ['Paris','Nice','Lyon'],
  'Spain':         ['Barcelona','Madrid','Seville','Valencia'],
  'United States': ['New York','Las Vegas','Orlando','Los Angeles'],
  'Turkey':        ['Istanbul','Antalya'],
  'Italy':         ['Rome','Venice','Milan'],
  'England':       ['London','Manchester'],
  'China':         ['Beijing','Shanghai'],
  'Japan':         ['Tokyo','Osaka','Kyoto'],
  'Thailand':      ['Bangkok','Phuket','Chiang Mai'],
  'Mexico':        ['Cancun','Mexico City'],
  'Brazil':        ['Rio de Janeiro','São Paulo'],
  'Peru':          ['Lima','Cusco'],
  'Sri Lanka':     ['Colombo','Kandy','Galle'],
  'Maldives':      ['North Malé Atoll','Baa Atoll'],
};

/* ---------------------------------------------------------------
   SUITE DATA
   Each suite has:
     id           → used in URL to select-room page
     name         → displayed as card title
     pricePerNight→ USD
     beds         → number of bedrooms
     baths        → number of bathrooms
     balcony      → 'yes' | 'no'
     jointRooms   → 'yes' | 'no'
     privatePool  → 'yes' | 'no'
     tags         → short pill labels shown on card
     desc         → brief text shown on card (not repeating filter info)
     images       → array of 3 image paths (main, small1, small2)

   NOTE FOR BACKEND TEAM:
     In production, suite availability per location + date comes from
     the database. The static SUITES array below is for frontend display
     only. Replace the searchRooms() fetch call with a real servlet call.
--------------------------------------------------------------- */
const SUITES = [
  {
    id:            'presidential',
    name:          'Presidential Suite',
    pricePerNight: 63600,
    beds:          3,
    baths:         3,
    balcony:       'yes',
    jointRooms:    'no',
    privatePool:   'yes',
    tags:          ['Two Full Floors','Rooftop Access','Executive Lounge','Formal Dining','Private Kitchen','King · Queen · Queen'],
    desc:          'The pinnacle of Tranquility. Spanning two entire floors at the highest point of the property, the Presidential Suite offers a formal living room, full dining area, private kitchen, and premium amenities curated to the highest standard. Rooftop access and executive lounge privileges included.',
    images: [
      'assets/images/suites/presidential-1.jpg',
      'assets/images/suites/presidential-2.jpg',
      'assets/images/suites/presidential-3.jpg',
    ],
  },
  {
    id:            'penthouse',
    name:          'Penthouse Suite',
    pricePerNight: 44200,
    beds:          3,
    baths:         2,
    balcony:       'yes',
    jointRooms:    'no',
    privatePool:   'no',
    tags:          ['Entire Floor','Floor-to-Ceiling Glass','Private Terrace','Rooftop Access','Executive Lounge','King · Twin · Twin'],
    desc:          'An open-plan sanctuary wrapped in floor-to-ceiling glass. The Penthouse occupies an entire floor with sweeping panoramic views, a private terrace, open living and dining, and a full kitchen. Three bedrooms — one master and two guest — sleep up to six in refined comfort.',
    images: [
      'assets/images/suites/penthouse-1.jpg',
      'assets/images/suites/penthouse-2.jpg',
      'assets/images/suites/penthouse-3.jpg',
    ],
  },
  {
    id:            'executive',
    name:          'Executive Suite',
    pricePerNight: 351,
    beds:          2,
    baths:         1,
    balcony:       'yes',
    jointRooms:    'no',
    privatePool:   'no',
    tags:          ['Executive Lounge','Meeting Spaces','Complimentary Breakfast','Evening Cocktails','Ergonomic Work Area','King · King'],
    desc:          'Crafted for the discerning business traveller. Features a large ergonomic work desk, advanced in-room technology, balcony, complimentary breakfast and evening cocktails, and full access to meeting facilities and the executive lounge.',
    images: [
      'assets/images/suites/executive-1.jpg',
      'assets/images/suites/executive-2.jpg',
      'assets/images/suites/executive-3.jpg',
    ],
  },
  {
    id:            'junior',
    name:          'Junior Suite',
    pricePerNight: 420,
    beds:          1,
    baths:         1,
    balcony:       'no',
    jointRooms:    'no',
    privatePool:   'no',
    tags:          ['Open-Plan Layout','Sofa Bed','Lounge Area','Basic Amenities','King'],
    desc:          'A spacious, light-filled studio concept. The sleeping and sitting areas flow seamlessly into one open space — no full-wall separation, just calm and comfort. Includes a sofa bed for an extra guest and all essential amenities.',
    images: [
      'assets/images/suites/junior-1.jpg',
      'assets/images/suites/junior-2.jpg',
      'assets/images/suites/junior-3.jpg',
    ],
  },
  {
    id:            'family',
    name:          'Family Suite',
    pricePerNight: 560,
    beds:          2,
    baths:         2,
    balcony:       'no',
    jointRooms:    'yes',
    privatePool:   'no',
    tags:          ['Joint Rooms','Children\'s Room','Bunk Beds','Board Games','Shared Living Area','King · Bunk × 2'],
    desc:          'Two interconnected rooms designed for families. The master room offers a king bed, while the children\'s room features two sets of bunk beds with child-friendly amenities and a curated selection of board games. A generous shared living area connects both rooms.',
    images: [
      'assets/images/suites/family-1.jpg',
      'assets/images/suites/family-2.jpg',
      'assets/images/suites/family-3.jpg',
    ],
  },
  {
    id:            'single-regular',
    name:          'Single Regular',
    pricePerNight: 60,
    beds:          1,
    baths:         1,
    balcony:       'no',
    jointRooms:    'no',
    privatePool:   'no',
    tags:          ['Queen Bed','Essential Amenities','City View','Cosy & Comfortable'],
    desc:          'Everything you need, nothing you don\'t. A well-appointed room with a queen bed, private bathroom, and all essential amenities. Perfect for solo travellers or a short getaway.',
    images: [
      'assets/images/suites/single-regular-1.jpg',
      'assets/images/suites/single-regular-2.jpg',
      'assets/images/suites/single-regular-3.jpg',
    ],
  },
  {
    id:            'double-regular',
    name:          'Double Regular',
    pricePerNight: 100,
    beds:          2,
    baths:         1,
    balcony:       'no',
    jointRooms:    'no',
    privatePool:   'no',
    tags:          ['Two Queen Beds','Shared Bathroom','Essential Amenities','Great Value'],
    desc:          'A comfortable, well-sized room with two queen beds and a private bathroom. Ideal for couples, friends, or anyone who enjoys a little extra space without the luxury price tag.',
    images: [
      'assets/images/suites/double-regular-1.jpg',
      'assets/images/suites/double-regular-2.jpg',
      'assets/images/suites/double-regular-3.jpg',
    ],
  },
  {
    id:            'family-regular',
    name:          'Family Regular',
    pricePerNight: 120,
    beds:          2,
    baths:         1,
    balcony:       'no',
    jointRooms:    'yes',
    privatePool:   'no',
    tags:          ['Joint Rooms','Bunk Bed Room','Queen Bed','Shared Bathroom','Child Friendly'],
    desc:          'Two connected rooms at a price that makes sense. One room features a queen bed, the other a bunk bed — both sharing a bathroom. A practical and affordable choice for families travelling together.',
    images: [
      'assets/images/suites/family-regular-1.jpg',
      'assets/images/suites/family-regular-2.jpg',
      'assets/images/suites/family-regular-3.jpg',
    ],
  },
];

/* ---------------------------------------------------------------
   UPDATE CITY DROPDOWN based on selected country
--------------------------------------------------------------- */
function updateCities() {
  const country = document.getElementById('f-country').value;
  const citySelect = document.getElementById('f-city');

  // Reset
  citySelect.innerHTML = '<option value="">Any</option>';

  if (country && CITY_MAP[country]) {
    CITY_MAP[country].forEach((city) => {
      const opt = document.createElement('option');
      opt.value = city;
      opt.textContent = city;
      citySelect.appendChild(opt);
    });
  }
}

/* ---------------------------------------------------------------
   CALCULATE NIGHTS between two date strings
--------------------------------------------------------------- */
function calcNights(checkin, checkout) {
  if (!checkin || !checkout) return null;
  const ms = new Date(checkout) - new Date(checkin);
  if (ms <= 0) return null;
  return Math.round(ms / (1000 * 60 * 60 * 24));
}

/* ---------------------------------------------------------------
   FORMAT PRICE  e.g. 63600 → "$63,600"
--------------------------------------------------------------- */
function fmtPrice(n) {
  return '$' + n.toLocaleString('en-US');
}

/* ---------------------------------------------------------------
   BUILD A SUITE CARD element
--------------------------------------------------------------- */
function buildCard(suite, nights) {
  const totalPrice = nights ? suite.pricePerNight * nights : suite.pricePerNight;
  const priceLabel = nights ? `${fmtPrice(totalPrice)}` : fmtPrice(suite.pricePerNight);
  const nightsLabel = nights ? `${nights} night${nights > 1 ? 's' : ''}` : 'per night';

  const card = document.createElement('a');
  card.classList.add('suite-card');
  const country = document.getElementById('f-country').value;
  const city    = document.getElementById('f-city').value;
  const checkin = document.getElementById('f-checkin').value;
  const checkout= document.getElementById('f-checkout').value;
  card.href = `select-room.html?suite=${suite.id}&country=${encodeURIComponent(country)}&city=${encodeURIComponent(city)}&checkin=${checkin}&checkout=${checkout}`;

  card.innerHTML = `
    <!-- Image collage -->
    <div class="card-images">
      <div class="card-img-main">
        <img src="${suite.images[0]}" alt="${suite.name} main view"
             onerror="this.style.display='none'" />
      </div>
      <div class="card-img-small">
        <img src="${suite.images[1]}" alt="${suite.name} view 2"
             onerror="this.style.display='none'" />
      </div>
      <div class="card-img-small">
        <img src="${suite.images[2]}" alt="${suite.name} view 3"
             onerror="this.style.display='none'" />
      </div>
    </div>

    <!-- Text content -->
    <div class="card-content">
      <div class="card-title-row">
        <h2 class="suite-name">${suite.name}</h2>
        <div class="suite-price">
          <span class="price-label">from</span>
          <span class="price-amount">${priceLabel}</span>
          <span class="price-nights">${nightsLabel}</span>
        </div>
      </div>
      <div class="suite-tags">
        ${suite.tags.map(t => `<span class="suite-tag">${t}</span>`).join('')}
      </div>
      <p class="suite-desc">${suite.desc}</p>
    </div>
  `;

  return card;
}

/* ---------------------------------------------------------------
   SEARCH ROOMS
   Reads filters, matches against SUITES array,
   renders cards (or no-results message).

   BACKEND NOTE:
   When your teammate's servlet is ready, replace the local
   filter logic below with a fetch() call like:

   const params = new URLSearchParams({ country, city, beds, ... });
   const res = await fetch('SearchRoomsServlet?' + params);
   const suites = await res.json();
   // then loop suites and call buildCard(suite, nights)
--------------------------------------------------------------- */
function searchRooms() {
  const beds    = document.getElementById('f-beds').value;
  const baths   = document.getElementById('f-baths').value;
  const balcony = document.getElementById('f-balcony').value;
  const joint   = document.getElementById('f-joint').value;
  const pool    = document.getElementById('f-pool').value;
  const checkin = document.getElementById('f-checkin').value;
  const checkout= document.getElementById('f-checkout').value;
  const nights  = calcNights(checkin, checkout);

  const emptyState = document.getElementById('emptyState');
  const suiteList  = document.getElementById('suiteList');

  // Hide empty state, clear previous results
  emptyState.style.display = 'none';
  suiteList.innerHTML = '';

  // Filter locally
  const matched = SUITES.filter((s) => {
    if (beds    && s.beds    !== parseInt(beds))    return false;
    if (baths   && s.baths   !== parseInt(baths))   return false;
    if (balcony && s.balcony !== balcony)            return false;
    if (joint   && s.jointRooms !== joint)           return false;
    if (pool    && s.privatePool !== pool)           return false;
    return true;
  });

  if (!matched.length) {
    suiteList.innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">✦</div>
        <p>No suites match your selected filters.<br>Try broadening your search.</p>
      </div>`;
    return;
  }

  matched.forEach((suite) => {
    suiteList.appendChild(buildCard(suite, nights));
  });
}

/* ---------------------------------------------------------------
   DATE VALIDATION — prevent checkout before checkin
--------------------------------------------------------------- */
document.getElementById('f-checkin')?.addEventListener('change', function () {
  const checkout = document.getElementById('f-checkout');
  checkout.min = this.value;
  if (checkout.value && checkout.value < this.value) {
    checkout.value = this.value;
  }
});

// Set min checkin to today
(function () {
  const today = new Date().toISOString().split('T')[0];
  const checkin = document.getElementById('f-checkin');
  if (checkin) checkin.min = today;
})();