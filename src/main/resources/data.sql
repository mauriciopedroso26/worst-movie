INSERT INTO MOVIES (release_year, title, studios, producers, winner)
SELECT"YEAR",TITLE,STUDIOS,PRODUCERS,
    CASE
        WHEN WINNER = 'yes'
            THEN TRUE
        ELSE FALSE
        END
FROM CSVREAD('classpath:/Movielist.csv', NULL, 'charset=UTF-8 fieldSeparator=;');
