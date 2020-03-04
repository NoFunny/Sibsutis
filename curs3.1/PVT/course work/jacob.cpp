CREATE TABLE Directors (
"id_режиссера" INTEGER NOT NULL,
"Фамилия" VARCHAR(25) NOT NULL,
"Имя" VARCHAR(25) NOT NULL,
"Отчество" VARCHAR(25) ,
"Дата рождения" DATE,

CONSTRAINT "К2" PRIMARY KEY ("id_режиссера")
);

INSERT INTO Directors ("id_режиссера","Фамилия","Имя","Дата рождения") VALUES (1,'Соммерс','Стивен','1962-02-20');
INSERT INTO Directors ("id_режиссера","Фамилия","Имя","Дата рождения") VALUES (2,'Стромберг','Роберт','1965-06-30');
INSERT INTO Directors ("id_режиссера","Фамилия","Имя","Дата рождения") VALUES (3,'Бёртон','Тим','1958-03-25');
INSERT INTO Directors ("id_режиссера","Фамилия","Имя","Дата рождения") VALUES (4,'Дарабонт','Фрэнк','1959-01-28');
INSERT INTO Directors ("id_режиссера","Фамилия","Имя","Дата рождения") VALUES (5,'Финчер','Дэвид','1629-08-28');

CREATE TABLE Awards (
"id_премии" INTEGER NOT NULL,
"название премии" VARCHAR(25) NOT NULL,

CONSTRAINT "K8" PRIMARY KEY ("id_премии")
);

INSERT INTO Awards ("id_премии", "название премии") VALUES (1,'Оскар');
INSERT INTO Awards ("id_премии", "название премии") VALUES (2,'Золотой глобус');
INSERT INTO Awards ("id_премии", "название премии") VALUES (3,'Премия канала MTV');


CREATE TABLE Age_Limit (
"id_возраста" INTEGER NOT NULL,
"Возраст" INTEGER NOT NULL,

CONSTRAINT "К7" PRIMARY KEY ("id_возраста")
);

INSERT INTO Age_Limit ("id_возраста", "Возраст") VALUES (1,0);
INSERT INTO Age_Limit ("id_возраста", "Возраст") VALUES (2,6);
INSERT INTO Age_Limit ("id_возраста", "Возраст") VALUES (3,12);
INSERT INTO Age_Limit ("id_возраста", "Возраст") VALUES (4,16);
INSERT INTO Age_Limit ("id_возраста", "Возраст") VALUES (5,18);
INSERT INTO Age_Limit ("id_возраста", "Возраст") VALUES (6,21);


CREATE TABLE Films (
"id_фильма" INTEGER NOT NULL,
"Название" VARCHAR(100) NOT NULL,
"Дата выхода" DATE ,
"Бюджет" INTEGER NOT NULL,
"id_режиссера" INTEGER NOT NULL,
"id_возраста" INTEGER NOT NULL,

CONSTRAINT "К1" PRIMARY KEY ("id_фильма"),
CONSTRAINT "C1" FOREIGN KEY ("id_режиссера") REFERENCES Directors ("id_режиссера"),
CONSTRAINT "C10" FOREIGN KEY ("id_возраста") REFERENCES Age_Limit ("id_возраста")
);

INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (1,'Мумия','1999-03-16',80000000,1,3);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (2,'МалиFnSента','2014-04-24',180000000,2,2);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (3,'Зеленая миля','1999-12-6',60000000,4,4);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (4,'Побег из Шоушенка','1994-09-10',25000000,4,4);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (5,'Алиса в Стране чудес','2010-02-25',200000000,3,3);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (6,'Чарли и Шоколадная фабрика','2005-07-10',150000000,3,2);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (7,'Бойцовский клуб','1999-09-10',63000000,5,5);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (8,'Загадочная история Бенджамина Баттона','2008-12-10',150000000,5,3);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (9,'Эдвард руки-ножницы','1990-12-6',20000000,3,3);
INSERT INTO Films ("id_фильма","Название","Дата выхода","Бюджет","id_режиссера","id_возраста") VALUES (10,'Мрачные тени','2012-09-15',150000000,3,3);

CREATE TABLE FnAw (
"id_фильма" INTEGER NOT NULL,
"id_премии" INTEGER NOT NULL,

CONSTRAINT "K10" UNIQUE ("id_фильма", "id_премии"),
CONSTRAINT "C13" FOREIGN KEY ("id_премии")
 
REFERENCES Awards ("id_премии")
);

INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (1,1);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (2,2);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (3,1);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (4,3);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (5,1);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (6,2);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (7,2);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (7,1);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (7,3);
INSERT INTO FnAw ("id_фильма", "id_премии") VALUES (8,1);


CREATE TABLE Film_crew (
"id_участника-группы" INTEGER NOT NULL,
"Фамилия" VARCHAR(25) NOT NULL,
"Имя" VARCHAR(25) NOT NULL,
"Отчество" VARCHAR(25),

CONSTRAINT "K5" PRIMARY KEY ("id_участника-группы")
);

INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (1,'Жар','Кевин');
INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (2,'Стромберг','Роберт');
INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (3,'Тэттерсолл','Дэвид');
INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (4,'Фрэнсис','Ричард');
INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (5,'Рот','Джо');
INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (6,'Крессвелл','Крис');
INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (7,'Яканджело','Питер');
INSERT INTO Film_crew ("id_участника-группы", "Фамилия", "Имя") VALUES (8,'Файт','Марк');


CREATE TABLE FnS (
"id_участника-группы" INTEGER NOT NULL,
"id_фильма" INTEGER NOT NULL,

CONSTRAINT "K11" UNIQUE ("id_фильма", "id_участника-группы"),
CONSTRAINT "C11" FOREIGN KEY ("id_участника-группы") REFERENCES Film_crew ("id_участника-группы"),
CONSTRAINT "C12" FOREIGN KEY ("id_фильма") REFERENCES Films ("id_фильма")
);

INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (1,1);
INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (2,2);
INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (3,3);
INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (4,4);
INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (5,5);
INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (6,6);
INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (7,7);
INSERT INTO FnS ("id_участника-группы", "id_фильма") VALUES (8,8);


CREATE TABLE Genre (
"id_жанра" INTEGER NOT NULL,
"жанр" VARCHAR(25) NOT NULL,

CONSTRAINT "K6" PRIMARY KEY ("id_жанра")
);

INSERT INTO Genre ("id_жанра", "жанр") VALUES (1,'Приключения');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (2,'Фэнтези');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (3,'Драма');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (4,'Криминал');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (5,'Триллер');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (6,'Комедия');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (7,'Боевик');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (8,'Биография');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (9,'История');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (10,'Военный');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (11,'Фантастика');
INSERT INTO Genre ("id_жанра", "жанр") VALUES (12,'Мелодрама');

CREATE TABLE FnG (
"id_фильма" INTEGER NOT NULL,
"id_жанра" INTEGER NOT NULL,

CONSTRAINT "K9" UNIQUE ("id_фильма", "id_жанра"),
CONSTRAINT "C8" FOREIGN KEY ("id_жанра") REFERENCES Genre ("id_жанра"),
CONSTRAINT "C9" FOREIGN KEY ("id_фильма") REFERENCES Films ("id_фильма")
);

INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (1,1);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (2,2);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (3,3);
INSERT INTO FnG ("id_фильма", "id_жанра")
 
VALUES (4,3);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (5,2);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (6,6);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (7,4);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (8,12);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (9,2);
INSERT INTO FnG ("id_фильма", "id_жанра") VALUES (10,2);


CREATE TABLE Cast (
"id_актёра" INTEGER NOT NULL,
"Фамилия" VARCHAR(25) NOT NULL,
"Имя" VARCHAR(25) NOT NULL,
"Отчество" VARCHAR(25),
"Дата рождения" DATE,

CONSTRAINT "K3" PRIMARY KEY ("id_актёра")
);

INSERT INTO Cast ("id_актёра", "Фамилия", "Имя", "Дата рождения") VALUES (1,'Брендан','Джеймс','1968-02-03');
INSERT INTO Cast ("id_актёра", "Фамилия", "Имя", "Дата рождения") VALUES (2,'Джоли','Анджелина','1975-06-04');
INSERT INTO Cast ("id_актёра", "Фамилия", "Имя", "Дата рождения") VALUES (3,'Брендан','Джеймс','1968-02-03');
INSERT INTO Cast ("id_актёра", "Фамилия", "Имя", "Дата рождения") VALUES (4,'Фриман','Морган','1975-06-04');
INSERT INTO Cast ("id_актёра", "Фамилия", "Имя", "Дата рождения") VALUES (5,'Нортон','Эдвард','1968-02-03');
INSERT INTO Cast ("id_актёра", "Фамилия", "Имя", "Дата рождения") VALUES (6,'Питт','Брэд','1968-02-03');
INSERT INTO Cast ("id_актёра", "Фамилия", "Имя", "Дата рождения") VALUES (7,'Депп','Джонни','1975-06-04');


CREATE TABLE FnA (
"id_актёра" INTEGER NOT NULL,
"id_фильма" INTEGER NOT NULL,

CONSTRAINT "K14" UNIQUE ("id_актёра", "id_фильма"),
CONSTRAINT "C2" FOREIGN KEY ("id_актёра") REFERENCES Cast ("id_актёра"),
CONSTRAINT "C3" FOREIGN KEY ("id_фильма") REFERENCES Films ("id_фильма")
);

INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (1,1);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (2,2);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (3,3);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (4,4);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (7,5);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (7,6);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (7,9);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (7,10);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (5,7);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (6,7);
INSERT INTO FnA ("id_актёра", "id_фильма") VALUES (6,8);


CREATE TABLE Film_critics (
"id_критика" INTEGER NOT NULL,
"Фамилия" VARCHAR(25) NOT NULL,
"Имя" VARCHAR(25) NOT NULL,
"Отчество" VARCHAR(25),

CONSTRAINT "K4" PRIMARY KEY ("id_критика")
);

INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (1,'Пупкин','Петр');
INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (2,'Мышкина','Ольга');
INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (3,'Хаустов','Дима');
INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (4,'Маркова','Полина');
INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (5,'Чекербаев','Данил');
INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (6,'Тимофеев','Егор');
INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (7,'Кучкина','Даша');
INSERT INTO Film_critics ("id_критика", "Фамилия", "Имя") VALUES (8,'Заверюха','Алексей');


CREATE TABLE Rating (
"оценка" INTEGER NOT NULL,
"id_критика" INTEGER NOT NULL,
"id_фильма" INTEGER NOT NULL,

CONSTRAINT "K12" PRIMARY KEY ("id_критика", "id_фильма"),
CONSTRAINT "C4" FOREIGN KEY ("id_критика") REFERENCES Film_critics ("id_критика"),
CONSTRAINT "C6" FOREIGN KEY ("id_фильма") REFERENCES Films ("id_фильма")
);

INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (4,1,1);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,2,1);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,3,1);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (3,4,2);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (2,5,2);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,6,2);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (4,7,3);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (3,8,3);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (4,1,3);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,2,4);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,3,4);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (3,4,4);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (1,5,5);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,6,5);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,7,5);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (3,8,6);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (4,1,6);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,2,6);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,3,7);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (3,4,7);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (1,5,7);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,6,8);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (5,7,8);
INSERT INTO Rating ("оценка", "id_критика", "id_фильма") VALUES (3,8,8);


CREATE TABLE Feedback (
"отзыв" VARCHAR(100) NOT NULL,
"id_критика" INTEGER NOT NULL,
"id_фильма" INTEGER NOT NULL,

CONSTRAINT "K13" PRIMARY KEY("id_критика", "id_фильма"),
CONSTRAINT "C5" FOREIGN KEY ("id_критика") REFERENCES Film_critics ("id_критика"),
CONSTRAINT "C7" FOREIGN KEY ("id_фильма") REFERENCES Films ("id_фильма")
);

INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Супер',1,1);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Афигенный фантастический фильм',2,1);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Очень веселый',3,1);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Был сложным для зрителя ',4,2);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Ну такое ',5,2);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('НЕ понравилось',6,2);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Класс',7,3);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Присутствуют неприятные сцены',8,3);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Супер',1,3);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Афигенный фантастический фильм',2,4);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Очень веселый',3,4);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Был сложным и не понятным ',4,4);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Ужас',5,5);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Классный',6,5);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Мне не пнравилось, много киноляпов',7,5);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Не советую',8,6);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Очень веселый',1,6);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Супер,Класс!',2,6);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Советую',3,7);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Сойдет',4,7);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Мне не пнравилось',5,7);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('ВАУ',6,8);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Классный ',7,8);
INSERT INTO Feedback ("отзыв", "id_критика", "id_фильма") VALUES ('Нууу без комментариев',8,8);


--Вывод
SELECT*FROM FnA;
SELECT*FROM Films;
SELECT*FROM Directors;
SELECT*FROM Awards;
SELECT*FROM FnAw;
SELECT*FROM FnS;
SELECT*FROM Film_crew;
SELECT*FROM FnG;
SELECT*FROM Age_Limit;
SELECT*FROM Cast;
SELECT*FROM Rating;
SELECT*FROM Feedback;
SELECT*FROM Film_critics;

--Задачи

SELECT id_фильма, AVG(оценка) as Среднее_значение FROM Rating group BY id_фильма ORDER BY Среднее_значение DESC;
SELECT COUNT(*) FROM FnA.id_актёра 
