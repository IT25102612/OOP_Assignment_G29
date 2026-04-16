-- ================================================================
-- Hotel Tranquility — Database Schema
-- File: sql/schema.sql
-- Run this once to set up the entire database.
-- ================================================================

-- Create and select the database
CREATE DATABASE IF NOT EXISTS tranquility_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE tranquility_db;

-- ----------------------------------------------------------------
-- TABLE: rooms
-- Stores every physical room in the hotel system.
-- Populated by the Add Room admin function.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rooms (
    room_id           INT AUTO_INCREMENT PRIMARY KEY,
    room_number       VARCHAR(20)    NOT NULL UNIQUE,
    country           VARCHAR(50)    NOT NULL,
    city              VARCHAR(50)    NOT NULL,
    building          CHAR(1)        NOT NULL,
    floor             INT            NOT NULL,
    room_num_on_floor INT            NOT NULL,
    suite_type        VARCHAR(30)    NOT NULL,
    price_per_night   DECIMAL(10,2)  NOT NULL,
    beds              INT            NOT NULL,
    baths             INT            NOT NULL,
    balcony           ENUM('yes','no') NOT NULL DEFAULT 'no',
    private_pool      ENUM('yes','no') NOT NULL DEFAULT 'no',
    joint_rooms       ENUM('yes','no') NOT NULL DEFAULT 'no',
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------
-- TABLE: reservations
-- One row per room booked per stay.
-- A guest booking 3 rooms = 3 rows, all sharing the same
-- confirmation_id.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS reservations (
    reservation_id    VARCHAR(20)    NOT NULL PRIMARY KEY,
    confirmation_id   VARCHAR(20)    NOT NULL,
    room_number       VARCHAR(20)    NOT NULL,
    guest_name        VARCHAR(100),
    guest_email       VARCHAR(150),
    guest_phone       VARCHAR(30),
    verify_method     ENUM('email','otp') NOT NULL DEFAULT 'email',
    checkin_date      DATE           NOT NULL,
    checkout_date     DATE           NOT NULL,
    nights            INT            NOT NULL,
    adults            INT            NOT NULL DEFAULT 1,
    children          INT            NOT NULL DEFAULT 0,
    meal_plan         ENUM(
                        'none',
                        'breakfast',
                        'half-board',
                        'full-board',
                        'all-inclusive'
                      ) NOT NULL DEFAULT 'none',
    meal_note         TEXT,
    room_price        DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    meal_price        DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    total_price       DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    payment_method    ENUM('paypal','applepay','koko'),
    status            ENUM(
                        'pending',
                        'confirmed',
                        'checked-in',
                        'cancelled'
                      ) NOT NULL DEFAULT 'pending',
    cancel_reason     VARCHAR(50),
    cancel_comment    TEXT,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservation_room
        FOREIGN KEY (room_number)
        REFERENCES rooms(room_number)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ----------------------------------------------------------------
-- TABLE: verification_codes
-- Temporary 6-digit codes sent by email or SMS.
-- Used for: payment verification, update, cancel, admin auth.
-- Codes expire after 10 minutes.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS verification_codes (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    contact     VARCHAR(150)  NOT NULL,
    code        VARCHAR(10)   NOT NULL,
    purpose     ENUM(
                  'payment',
                  'update',
                  'cancel',
                  'admin'
                ) NOT NULL,
    expires_at  TIMESTAMP     NOT NULL,
    used        BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------
-- TABLE: reviews
-- Guest reviews submitted from the home page footer form.
-- Admin must approve them before they appear publicly.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS reviews (
    review_id    INT AUTO_INCREMENT PRIMARY KEY,
    codename     VARCHAR(50)   NOT NULL,
    location     VARCHAR(100),
    email        VARCHAR(150),
    review_text  TEXT          NOT NULL,
    admin_reply  TEXT,
    approved     BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------
-- TABLE: admin_users
-- Stores admin login credentials.
-- Passwords must be stored as bcrypt hashes — never plain text.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin_users (
    admin_id      VARCHAR(50)   NOT NULL PRIMARY KEY,
    password_hash VARCHAR(255)  NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------
-- SEED DATA: Insert the default admin account.
-- Password is 'tranquility123' hashed with bcrypt.
-- Replace this hash if you change the password.
-- ----------------------------------------------------------------
INSERT IGNORE INTO admin_users (admin_id, password_hash)
VALUES (
    'admin',
    '$2a$12$eImiTXuWVxfM37uY4JANjQ==.placeholder_replace_with_real_bcrypt_hash'
);

-- ----------------------------------------------------------------
-- INDEXES: Speed up the most common queries
-- ----------------------------------------------------------------

-- Searching reservations by room + date range (availability check)
CREATE INDEX IF NOT EXISTS idx_res_room_dates
    ON reservations(room_number, checkin_date, checkout_date);

-- Searching reservations by email (update/cancel verify flow)
CREATE INDEX IF NOT EXISTS idx_res_email
    ON reservations(guest_email);

-- Searching reservations by phone
CREATE INDEX IF NOT EXISTS idx_res_phone
    ON reservations(guest_phone);

-- Searching reservations by confirmation ID
CREATE INDEX IF NOT EXISTS idx_res_confirmation
    ON reservations(confirmation_id);

-- Searching rooms by city + suite type (available rooms filter)
CREATE INDEX IF NOT EXISTS idx_rooms_city_suite
    ON rooms(city, suite_type);

-- Cleaning up expired verification codes
CREATE INDEX IF NOT EXISTS idx_codes_expires
    ON verification_codes(expires_at);