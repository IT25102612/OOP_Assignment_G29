/* ================================================================
   floor-plan.js  —  Hotel Tranquility room & floor plan system
   Used by: admin-add-room.js, admin-view.js
   ================================================================

   BUILDING STRUCTURE (per city location):
   Each hotel has 3 buildings: A, B, C
     Building A: 80 floors  — upper floors = premium suites
     Building B: 60 floors  — mid-range suites + executive
     Building C: 40 floors  — regular rooms

   ROOM NUMBERING FORMAT:
     [Country Code][City Code][Building][Floor][Room]
     Example: FPA.80.01 = France > Paris > Building A > Floor 80 > Room 01

   COUNTRY CODES: FR, SP, US, TR, IT, EN, CN, JP, TH, MX, BR, PE, SL, MV

   CITY CODES (2 letters):
     France: PA(Paris), NI(Nice), LY(Lyon)
     Spain:  BA(Barcelona), MA(Madrid), SE(Seville), VA(Valencia)
     USA:    NY(New York), LV(Las Vegas), OR(Orlando), LA(Los Angeles)
     Turkey: IS(Istanbul), AN(Antalya)
     Italy:  RO(Rome), VE(Venice), MI(Milan)
     England:LO(London), MC(Manchester)
     China:  BJ(Beijing), SH(Shanghai)
     Japan:  TK(Tokyo), OS(Osaka), KY(Kyoto)
     Thailand:BK(Bangkok), PH(Phuket), CM(Chiang Mai)
     Mexico: CA(Cancun), MC(Mexico City) → MC conflicts, use MX for Mexico City
     Brazil: RJ(Rio de Janeiro), SP(Sao Paulo)
     Peru:   LI(Lima), CU(Cusco)
     Sri Lanka: CO(Colombo), KA(Kandy), GA(Galle)
     Maldives:  NM(North Male Atoll), BA(Baa Atoll)

   FLOOR PLAN — Building A (80 floors):
     Floors  1-2:   Lobby, Reception, Concierge, Restaurants (no rooms)
     Floor   3:     Spa & Wellness Centre (no rooms)
     Floor   4:     Indoor Swimming Pool (no rooms)
     Floor   5:     Indoor Tennis Court (no rooms)
     Floor   6:     Indoor Basketball Court (no rooms)
     Floor   7:     Games Area & Entertainment (no rooms)
     Floor   8:     Supermarket & Shopping (no rooms)
     Floors  9-10:  Conference & Meeting Rooms (no rooms)
     Floors 11-20:  Regular rooms (Single Regular) — 12 rooms per floor
     Floors 21-35:  Double Regular — 10 rooms per floor
     Floors 36-45:  Family Regular — 8 rooms per floor
     Floors 46-55:  Junior Suites — 6 rooms per floor
     Floors 56-65:  Executive Suites — 4 rooms per floor
     Floors 66-72:  Family Suites — 3 rooms per floor
     Floors 73-77:  Penthouse Suites — 2 rooms per floor (each entire floor split)
     Floors 78-80:  Presidential Suites — 1 suite per TWO floors (1.5 suites)
                    Floor 78-79: Presidential Suite 01
                    Floor 80:   Presidential Suite 02 (top floor)

   FLOOR PLAN — Building B (60 floors):
     Floors  1-2:   Lobby, Dining, Bar (no rooms)
     Floor   3:     Spa (no rooms)
     Floor   4:     Indoor Pool (no rooms)
     Floors  5-15:  Single Regular — 14 rooms per floor
     Floors 16-30:  Double Regular — 12 rooms per floor
     Floors 31-42:  Family Regular — 8 rooms per floor
     Floors 43-50:  Junior Suites — 6 rooms per floor
     Floors 51-56:  Executive Suites — 4 rooms per floor
     Floors 57-60:  Family Suites — 3 rooms per floor

   FLOOR PLAN — Building C (40 floors):
     Floors  1-2:   Lobby, Café, Lounge (no rooms)
     Floors  3-40:  Single Regular — 16 rooms per floor
                    (floors 20-40 also have some Double Regular mixed in:
                     8 Single + 8 Double per floor)

   ================================================================ */

const FLOOR_PLAN = {

  buildingA: {
    totalFloors: 80,
    amenityFloors: {
      1: 'Lobby & Reception',
      2: 'Restaurants & Dining',
      3: 'Spa & Wellness Centre',
      4: 'Indoor Swimming Pool',
      5: 'Indoor Tennis Court',
      6: 'Indoor Basketball Court',
      7: 'Games Area & Entertainment',
      8: 'Supermarket & Shopping',
      9: 'Conference Rooms',
      10: 'Meeting & Executive Lounge',
    },
    roomFloors: {
      // floors 11-20: Single Regular, 12 rooms/floor
      ...Object.fromEntries(
        Array.from({length:10}, (_,i) => [i+11, { suite:'single-regular', roomsPerFloor:12 }])
      ),
      // floors 21-35: Double Regular, 10 rooms/floor
      ...Object.fromEntries(
        Array.from({length:15}, (_,i) => [i+21, { suite:'double-regular', roomsPerFloor:10 }])
      ),
      // floors 36-45: Family Regular, 8 rooms/floor
      ...Object.fromEntries(
        Array.from({length:10}, (_,i) => [i+36, { suite:'family-regular', roomsPerFloor:8 }])
      ),
      // floors 46-55: Junior Suite, 6 rooms/floor
      ...Object.fromEntries(
        Array.from({length:10}, (_,i) => [i+46, { suite:'junior', roomsPerFloor:6 }])
      ),
      // floors 56-65: Executive Suite, 4 rooms/floor
      ...Object.fromEntries(
        Array.from({length:10}, (_,i) => [i+56, { suite:'executive', roomsPerFloor:4 }])
      ),
      // floors 66-72: Family Suite, 3 rooms/floor
      ...Object.fromEntries(
        Array.from({length:7}, (_,i) => [i+66, { suite:'family', roomsPerFloor:3 }])
      ),
      // floors 73-77: Penthouse Suite, 2 rooms/floor
      ...Object.fromEntries(
        Array.from({length:5}, (_,i) => [i+73, { suite:'penthouse', roomsPerFloor:2 }])
      ),
      // floors 78-80: Presidential Suite
      78: { suite:'presidential', roomsPerFloor:1, note:'Spans floors 78-79' },
      79: { suite:'presidential', roomsPerFloor:0, note:'Upper floor of 78-79 Presidential' },
      80: { suite:'presidential', roomsPerFloor:1, note:'Top floor Presidential' },
    }
  },

  buildingB: {
    totalFloors: 60,
    amenityFloors: {
      1: 'Lobby & Dining',
      2: 'Bar & Lounge',
      3: 'Spa',
      4: 'Indoor Pool',
    },
    roomFloors: {
      ...Object.fromEntries(
        Array.from({length:11}, (_,i) => [i+5,  { suite:'single-regular', roomsPerFloor:14 }])
      ),
      ...Object.fromEntries(
        Array.from({length:15}, (_,i) => [i+16, { suite:'double-regular', roomsPerFloor:12 }])
      ),
      ...Object.fromEntries(
        Array.from({length:12}, (_,i) => [i+31, { suite:'family-regular', roomsPerFloor:8 }])
      ),
      ...Object.fromEntries(
        Array.from({length:8}, (_,i) => [i+43, { suite:'junior', roomsPerFloor:6 }])
      ),
      ...Object.fromEntries(
        Array.from({length:6}, (_,i) => [i+51, { suite:'executive', roomsPerFloor:4 }])
      ),
      ...Object.fromEntries(
        Array.from({length:4}, (_,i) => [i+57, { suite:'family', roomsPerFloor:3 }])
      ),
    }
  },

  buildingC: {
    totalFloors: 40,
    amenityFloors: {
      1: 'Lobby & Café',
      2: 'Lounge & Co-working',
    },
    roomFloors: {
      // floors 3-19: Single Regular only, 16 rooms/floor
      ...Object.fromEntries(
        Array.from({length:17}, (_,i) => [i+3, { suite:'single-regular', roomsPerFloor:16 }])
      ),
      // floors 20-40: 8 Single Regular + 8 Double Regular per floor
      ...Object.fromEntries(
        Array.from({length:21}, (_,i) => [i+20, { suite:'mixed', roomsPerFloor:16,
          breakdown:{ 'single-regular':8, 'double-regular':8 } }])
      ),
    }
  },

};

/* ---------------------------------------------------------------
   CITY CODE MAP
--------------------------------------------------------------- */
const CITY_CODES = {
  'Paris':'PA','Nice':'NI','Lyon':'LY',
  'Barcelona':'BA','Madrid':'MA','Seville':'SE','Valencia':'VA',
  'New York':'NY','Las Vegas':'LV','Orlando':'OR','Los Angeles':'LA',
  'Istanbul':'IS','Antalya':'AN',
  'Rome':'RO','Venice':'VE','Milan':'MI',
  'London':'LO','Manchester':'MC',
  'Beijing':'BJ','Shanghai':'SH',
  'Tokyo':'TK','Osaka':'OS','Kyoto':'KY',
  'Bangkok':'BK','Phuket':'PH','Chiang Mai':'CM',
  'Cancun':'CA','Mexico City':'MX',
  'Rio de Janeiro':'RJ','São Paulo':'SP',
  'Lima':'LI','Cusco':'CU',
  'Colombo':'CO','Kandy':'KA','Galle':'GA',
  'North Malé Atoll':'NM','Baa Atoll':'BA',
};

const COUNTRY_CODES = {
  'France':'FR','Spain':'SP','United States':'US','Turkey':'TR',
  'Italy':'IT','England':'EN','China':'CN','Japan':'JP',
  'Thailand':'TH','Mexico':'MX','Brazil':'BR','Peru':'PE',
  'Sri Lanka':'SL','Maldives':'MV',
};

/* ---------------------------------------------------------------
   GENERATE ROOM NUMBER
   buildRoom('Paris', 'France', 'A', 56, 3)
   → "FRPA.A56.03"
--------------------------------------------------------------- */
function buildRoomNumber(city, country, building, floor, roomNum) {
  const cc = COUNTRY_CODES[country.trim()] || 'XX';
  const ci = CITY_CODES[city.trim()]       || 'XX';
  const fl = String(floor).padStart(2, '0');
  const rn = String(roomNum).padStart(2, '0');
  return `${cc}${ci}.${building}${fl}.${rn}`;
}

/* ---------------------------------------------------------------
   GET SUITE TYPE FOR A GIVEN BUILDING + FLOOR
--------------------------------------------------------------- */
function getSuiteForFloor(building, floor) {
  const plan = FLOOR_PLAN[`building${building}`];
  if (!plan) return null;
  if (plan.amenityFloors[floor]) return { amenity: plan.amenityFloors[floor] };
  return plan.roomFloors[floor] || null;
}