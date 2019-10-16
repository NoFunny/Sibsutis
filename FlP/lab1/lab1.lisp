;1)4
(print(car(car(cdr(car(cdr '(1 (2 ( * 3 ) 4) 5)))))))
;2)4
(print(append '(+ 1 2) '())) 
; СОЕДИНЯЕТ (+ 1 2) И () В ОДИН СПИСОК И В ИТОГЕ ПОЛУЧАЕМ (+ 1 2)
;3)(4)a
(print(cons '(1)(cons '(2 (3)) nil)))
;3)(4)b
(print(list '(1)(list '2 '(3))))
;4)(4)
(defun F (g)
	(print(append(list(car g) (caddr g)) (list(car(cdr g)) (cadddr g)) (cddddr g))))
	(F '(1 2 3 4 5))
	(print '(Completed.))	

