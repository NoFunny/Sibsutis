%---------------------1-------------------------
even(X) :- X mod 2 =:= 0.

print_uneven(X, Y) :-
	not(even(Y)),
	write(Y), tab(1), fail.

print_uneven(X, Y) :-
	Y \== X, Y1 is Y - 1, print_uneven(X, Y1).

uneven() :- read(X), read(Y), print_uneven(X, Y).


%--------------------2-----------------------
calc_sum(X, Y, Sum) :-
	S is X + Sum,
	X \== Y,
	X1 is X + 1,
	calc_sum(X1, Y, S).

calc_sum(X, X, Sum) :- write(sum = Sum).

sum() :-
		read(X),
		read(Y),
		Y1 is Y + 1,
		calc_sum(X, Y1, 0).

%------------------------3--------------------------
find_min_digit(0, Min) :- write(Min).

find_min_digit(X, Min) :-
	D is X mod 10,
	X1 is X div 10,
	D < Min,
	find_min_digit(X1, D).

find_min_digit(X, Min) :-
	X1 is X div 10,
	find_min_digit(X1, Min).

min_digit() :-
	read(X),
	X \= 0,
	X1 is abs(X),
	find_min_digit(X1, 9).

min_digit() :- write(0).

%--------------------------4-------------------------
calc_fibonacci(0, Y) :- Y is 1, !.
calc_fibonacci(1, Y) :- Y is 1, !.

calc_fibonacci(X, Y) :-
	X1 is X - 2,
	X2 is X - 1,
	calc_fibonacci(X1, Y1),
	calc_fibonacci(X2, Y2),
	Y is Y1 + Y2.

fibonacci() :-
	repeat,
	read(X),
	(X < 0, !; calc_fibonacci(X, Y), write(Y), nl, fail).
