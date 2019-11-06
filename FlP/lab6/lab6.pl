%------------1--------------
read_list_def:-
  write('Введи список List:'),nl,
  read(List),
  write('Введи границы X,Y:'),nl,
  read(X),read(Y),
  list_making(X,Y,Min,Max),
  split_list(List,Min,Max,List1,List2,List3),
  write(List1),write(List2),writeln(List3).

list_making(X,Y,Min,Max):-
  X>Y, !, Max = X, Min = Y; Max = Y, Min = X.

split_list([], _X, _Y, [], [], []).

split_list([Head|Tail], X, Y, [Head|List1], List2, List3):-
    Head < X, !, split_list(Tail, X, Y, List1, List2, List3).
split_list([Head|Tail], X, Y, List1, [Head|List2], List3):-
    Head =< Y, !, split_list(Tail, X, Y, List1, List2, List3).
split_list([Head|Tail], X, Y, List1, List2, [Head|List3]):-
    !, split_list(Tail, X, Y, List1, List2, List3).

%------------------2---------------------
maximum:-
    write('Введи список A:'),nl,
    read(A),
    max_list(A, Max),
    list_max(A,B, Max, 1),
    writeln(B).

    list_max([],[], _, _).

    list_max([Head|Tail], [N|B], Max, N):-
        Head =:= Max, !, N1 is N + 1, list_max(Tail, B, Max, N1).
    list_max([Head|Tail], B, Max, N):-
        Head =\= Max, !, N1 is N + 1, list_max(Tail, B, Max, N1).

%------------------3---------------------
frequency:-
  write('Введи список List:'),nl,
  read(List),
  bubble_sort(List, Sort_List),
  seek(Sort_List, _, ResList),
  write(ResList).

	seek([], 0, []).
	seek([H|B], K, [H]) :-
		helper([H|B], H, K, List), seek(List, Max, _), K > Max, !.
	seek([H|B], Max, List1) :-
		helper([H|B], H, K, List), seek(List, Max, List1), K < Max, !.
	seek([H|B], Max, [H|List1]) :-
		helper([H|B], H, K, List), seek(List, Max, List1), K =:= Max, !.

	helper([], _, 0, []) :- !.
	helper([H|B], X, 0, [H|B]) :- H =\= X, !.
	helper([H|B], X, K, B1) :-  H =:= X,  helper(B, X, K1, B1), K is K1 + 1.

  move_max_to_end([], []):-!.
  move_max_to_end([Head], [Head]):-!.
  move_max_to_end([First, Second|Tail], [Second|ListWithMaxEnd]):-
    First > Second, !,
  move_max_to_end([First|Tail], ListWithMaxEnd).
  move_max_to_end([First, Second|Tail], [First|ListWithMaxEnd]):-
  move_max_to_end([Second|Tail], ListWithMaxEnd).

  bubble_sort(SortedList, SortedList):-
    move_max_to_end(SortedList, DoubleSortedList),
    SortedList = DoubleSortedList, !.
  bubble_sort(List, SortedList):-
    move_max_to_end(List, SortedPart),
    bubble_sort(SortedPart, SortedList).
