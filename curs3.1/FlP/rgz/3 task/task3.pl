:-dynamic information/3.
start:-consult('database.txt'),
listing(information/3),
show_menu.

show_menu:-repeat,
writeln('0 – Показать базу данных'),
writeln('1 – Добавить запись в БД'),
writeln('2 – Удалить запись из БД'),
writeln('3 – Найти маршрут'),
writeln('4 – Выход'),nl,
write('Введите Ваш выбор: (1-4) '),
read(X),
nl,
X<5,
process(X),nl,
X=4,!.

process(0):-listing(information/3).

process(1):-writeln('Тип Транспорта:'),
read(Transport),
writeln('Номер маршрута: '),
read(Number),
write('Остановки: '), read(Ost),
assertz(information(Transport,Number,Ost)).

process(2):-
repeat,
writeln('Хочешь удалить маршрут? (yes-да / no - для выхода)'),nl,
read(Choice),nl,
(Choice == yes ->
  write('Тип транспорта'),nl,
  read(Transport),nl,
  writeln('Номер'),nl, read(Number),   retract(information(Transport,Number,N1)),nl;
  write('Отмена'),!).
% answer(n).
% answer(_):-

process(3):-writeln('Введите название остановки №1'),
read(X),
writeln('Введите название остановки №2'),
read(Y),
information(N1,N2,N3),
member(X,N3),
member(Y,N3),
write(N1),
write(' '),
write(N2), nl, fail.
process(4):-!.

insert(X,L,[X|L]).
