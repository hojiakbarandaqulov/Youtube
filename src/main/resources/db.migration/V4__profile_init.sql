INSERT INTO profile ( name, surname, email, password, status, visible, created_date, role)
VALUES ('Adminjon', 'Adminov', 'hojiakbarandaulov5@gmail.com', 'JRSIpuUOBrlNMJrx2URi', 'ACTIVE', true, now(),
        'ROLE_ADMIN') ON CONFLICT (id) DO NOTHING;
SELECT setval('profile_id_seq', max(id))
FROM profile;
