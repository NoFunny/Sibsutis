female(mary).
female(liz).
female(ann).
male(john).
male(bob).
male(paul).
male(sam).
male(pat).
parent(mary,bob).
parent(john,bob).
parent(mary,ann).
parent(bob,liz).
parent(bob,sam).
parent(bob,paul).
parent(paul,pat).

mother(X,Y):-
  parent(X,Y),female(X).

father(X,Y):-
  parent(X,Y),male(X).

son(X,Y):-
  parent(Y,X),male(X).

douther(X,Y):-
  parent(Y,X),female(X).

brother(X,Y):-
  parent(Z,X),parent(Z,Y),male(X),male(Y),X\=Y.

sister(X,Y):-
  parent(Z,X),parent(Z,Y),female(X),male(Y),X\=Y.

grandson(X,Y):-
  parent(Z,X),parent(Y,Z),male(X).

aunt(X,Y):-
  sister(X,Z),parent(Z,Y).

hTwoChild(X):-
  son(Y,X),douther(Z,X),female(X).

successor(X):-
  son(Y,X),male(X), X\=Y.
