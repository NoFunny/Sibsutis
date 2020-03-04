;;2.4

(defun add (w n &aux (a ( car w)))
  (print(cond ((null w) nil)
        ((numberp a) (cons (+ a n) (add (cdr w) n)))
        (t (cons a (add (cdr w) n))))))
 (add '(a -1 6 v 3) 3)
(print `-------------------)
 ;;2.14

(defun convert(x)
	(print(cond ((null x) nil)
		((member (car x) (cdr x)) (convert(cdr x)))
		(t (cons (car x) (convert (cdr x)))))))

(convert '(a b a a c c))
(print `-------------------)   

;;2.24

(defun sort (x)
  (print (cond
        ((null x) nil)
        (t (append (remove-if-not #'(lambda (q) (equal (car x) q)) x)
            (sort (remove (car x) x)))))))
 
(sort '(1 5 2 1 4 3 1 2 4 5 4))
(print `-------------------)   


