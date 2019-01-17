-- Passwords are BCrypted hashes here

INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot', 'bot', '$2a$10$XsmYz3.r.UaoCUe8yqFtzucoBgMl9oTpWqWlNgfqLQAqwxIDG90Zy', 'bot', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd666', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot1', 'bot1', '$2a$10$9CkJxdKX/0QCLv3W.tCYlets3ic9SRTyWcU.ZoQBovAPpnwF.Z7Z6', 'bot1', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u1', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot1'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot2', 'bot2', '$2a$10$H8oebEbRh6QbCiotnqSJSuOtTpM.0r1tvCEhY3isl9Q565HuHDf5y', 'bot2', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u2', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot2'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot3', 'bot3', '$2a$10$d6jPrigTQEZ.LEma7FoP8uPjL7K1JXgZ8SvENCesuYujD5cOyGjq6', 'bot3', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u3', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot3'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot4', 'bot4', '$2a$10$sQatXETrxRTvZd9owGFnY.gnY012.YR4Ok/0Qh9zBM5UCr6XJ/koy', 'bot4', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u4', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot4'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot5', 'bot5', '$2a$10$PxSzimLo92TI8HYIiwtaZOgRAHOkFtbc9JtNnZWCw5Y/Q7E9oPOh2', 'bot5', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u5', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot5'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot6', 'bot6', '$2a$10$6.xQ9og3ONUf2PZu5ut7BObLXNHRjqIGz7H8X8i5Hy8vv5scpc/qW', 'bot6', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u6', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot6'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot7', 'bot7', '$2a$10$Vzzr7pSzbiBy9wlSa1/mNuTQC6sDT9Fzy2y8ljuyAxcXU59G33ZI2', 'bot7', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u7', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot7'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot8', 'bot8', '$2a$10$6/5xGIvvei.ZY6FNEL4EmevRYWIYc7/8mSwSzljkfp.Uxi2RmGM6K', 'bot8', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u8', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot8'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot9', 'bot9', '$2a$10$nBOjRvAdgxR5JYHIovxBoebXXTKG/kNeGlvWe9B/j8J/ofMeEoKjS', 'bot9', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u9', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot9'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot10', 'bot10', '$2a$10$1/gpA3hqaabh.G7uqixSwuZ3vvJXsDJXLdaove3zBDJ.yo7L5VShO', 'bot10', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd610', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot10'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('bot11', 'bot11', '$2a$10$aEjX.wWW0959XWWaDMsFZOXRckrk/.CUguFUtWgvQn.TT/8OKDwOS', 'bot11', '999999999',
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd611', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot11'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE",
            "TOKEN", "EMAIL")
    VALUES ('admin', 'admin', '$2a$10$GAk2B9I6e.qUQRgdm3CoWe9/nyK3gmF388m2IJI3FJ958NtvbdTVC', 'admin', '999999999',
            'nyannyannyannyannyannyannyannyan', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='admin'), 'ROLE_ADMIN');


INSERT INTO "SETTINGS"("ID", "VAL")
VALUES
  ('GAME_TURN_DELAY', '2000'),
  ('TRAINIG_BOT_LOGINS', 'bot,bot2,bot3,bot4,bot5,bot6,bot7,bot8,bot9,bot10'),
  ('PORT', '10040'),
  ('NEXT_GAME_TIME', '2019-01-14T15:37:23'),
  ('READING_TIMEOUT', '10000'),
  ('ERROR_RESPONSE_DELAY', '500'),
  ('STAT_ROWS_TO_SHOW', '30'),
  ('REGISTRATION_IS_OPEN', 'true'),
  ('MINIMAL_PLAYERS_NUMBER', '2'),
  ('MAXIMAL_PLAYERS_NUMBER', '8'),
  ('GAME_TURNS_LIMIT', '200');

-- admin/Hardcoded2014