
INSERT INTO user_account (id, credentials_expiry_date, deleted_flag, email_address, first_name, full_name, gender, is_account_enabled, is_account_expired, is_account_locked, is_credentials_expired, last_name, password, phone_number, status, profile_picture_id, rank_id) VALUES
    ('a393a64d-6d73-4459-8ebc-e6f8a8900211', current_timestamp, false, 'admin@qt.rw', 'QT', 'QT Admin', 'MALE', true, false, false, false, 'Admin', '$2a$12$N9hr.Cw4ySeAxcVdTlmzF.nAFq41zST5YJRUhDs/N0Qcc4nxdGwUu', '+250787161515', 'ACTIVE', null, '9c847319-c9a2-4a20-b4b0-a28af9b924ba') ON CONFLICT DO NOTHING;