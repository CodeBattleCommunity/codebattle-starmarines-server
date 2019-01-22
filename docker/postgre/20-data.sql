INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot', 'bot', 'd2tz2f5iwp4s4ntfzrisiuykhd666', 'bot', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd666', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot1', 'bot1', 'd2tz2f5iwp4s4ntfzrisiuykhd6u1', 'bot1', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u1', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot1'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot2', 'bot2', 'd2tz2f5iwp4s4ntfzrisiuykhd6u2', 'bot2', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u2', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot2'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot3', 'bot3', 'd2tz2f5iwp4s4ntfzrisiuykhd6u3', 'bot3', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u3', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot3'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot4', 'bot4', 'd2tz2f5iwp4s4ntfzrisiuykhd6u4', 'bot4', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u4', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot4'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot5', 'bot5', 'd2tz2f5iwp4s4ntfzrisiuykhd6u5', 'bot5', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u5', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot5'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot6', 'bot6', 'd2tz2f5iwp4s4ntfzrisiuykhd6u6', 'bot6', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u6', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot6'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot7', 'bot7', 'd2tz2f5iwp4s4ntfzrisiuykhd6u7', 'bot7', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u7', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot7'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot8', 'bot8', 'd2tz2f5iwp4s4ntfzrisiuykhd6u8', 'bot8', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u8', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot8'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot9', 'bot9', 'd2tz2f5iwp4s4ntfzrisiuykhd6u9', 'bot9', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd6u9', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot9'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot10', 'bot10', 'd2tz2f5iwp4s4ntfzrisiuykhd610', 'bot10', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd610', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot10'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('bot11', 'bot11', 'd2tz2f5iwp4s4ntfzrisiuykhd611', 'bot11', '999999999', 
            'j5cd2tz2f5iwp4s4ntfzrisiuykhd611', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='bot11'), 'ROLE_USER');
INSERT INTO "USERS"( "USER_NAME", "LOGIN", "PASSWORD", "BOT_NAME", "PHONE", 
            "TOKEN", "EMAIL")
    VALUES ('admin', 'admin', 'Hardcoded2014', 'admin', '999999999', 
            'nyannyannyannyannyannyannyannyan', 'asdf@asdf.com');
INSERT INTO "AUTHORITIES"("USER_ID", "AUTHORITY")
    VALUES ((SELECT u."ID" FROM "USERS" u WHERE u."LOGIN"='admin'), 'ROLE_ADMIN');