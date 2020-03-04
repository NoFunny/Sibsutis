(print '(Lab 3.1))

(defun subset(a b)
	(print(cond((null a))
			((member (car a) b) (subset(cdr a)b))
			(nil))))

(subset '(a b c) '(d a b g c))


(print '(Lab 3.2))

(defun dif_sum(a b)
	(print(cond
		((null a)nil)
		((not (member (car a) b)) (cons (car a) (dif_sum(cdr a) b)))
		(t (dif_sum (cdr a) b)))))

(dif_sum '(1 2 3 4 5 6) '(1 2 3 ))

(print '(Lab 3.3))

(defun map_list(a b)
	(print(cond((null a)nil)
		(t(cons(apply 'funcall (cons b a)) (map_list(cdr a) b))))))

(map_list '(1 2 3 4 5 6) '*)