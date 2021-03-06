# testTask
Задание:

В архиве в папке in находится набор файлов in1.txt, in2.txt ... in10.txt.
Каждый файл имеет одинаковую структуру:
первая строка - количество строк, которые нужно считать из файла.
вторая строка - количество чисел, которые нужно и обработать из каждой строки.
третья и все последующие строки - целые числа, разделенные пробелом.

Нужно для каждого файла in.txt создать файл out.txt с таким же номером, cодержащий сумму считанных по каждой строке чисел, наибольшее и наименьшее значение, 
в конце файла добавить общую сумму всех чисел по всему файлу (в том числе те, что не учитывались при поиски min и max).
Кроме того создать файл с названием result.txt, содержащий названия файлов out, отсортированных по убыванию поля total в них.


Формат выходного файла out должен быть таким:

Для каждой обработанной строки
<номер строки> : sum=<сумма считанных элементов> : max=<наибольшее значение> : min=<наименьшее значение>
В последней строке total=<общая сумма чисел в файле>

Важно:
1) При вычислении общей суммы чисел, первые две строки не учитываются
2) Если в файле in.txt меньше строк, чем указано в первой строке (первая и вторая строка не считаются), то в выходном файле out.txt должна содержаться 2 строки:
 Error, найдено 3 строки вместо 5
 total=0
 где 3 и 5 это общее количество строк и количество обрабатываемых строк
3) Если в какой либо строке файла in.txt чисел меньше чем указано во второй строке, то в файле out.txt для это строки должно быть
 <номер строки> : Error найдено 6 чисел вместо 11
 где 6 и 11 общее количество чисел в строке и количество обрабатываемых чисел для каждой строки.
4) Результатом выполнения задания является код на Java. Код долежен компилироваться, содержать комментарии при необходимости
5) Использовать сторонние библиотеки разрешно, но тогда обязательно использовать систему сборки Maven/Gradle
6) Плюсом будет код, выложенный в публичный репозиторий на Githab, и ссылка на него

Пример:

Входной файл in1.txt:
1
1
105550

Выходной файл out1.txt
1 : sum=105550 : max=105550 : min=105550
total=105550



Входной файл in2.txt:
2
3
12 44 66 2 0 -12 88
-2 44 8 2 5
2 3 4
23 55 66 233 23 56
21 545 66 12 34

Выходной файл out2.txt
1 : sum=122 : max=66 : min=12
2 : sum=50 : max=44 : min=-2
total=1400



Входной файл in3.txt:
4
4
1 2 3 4
2 4 5
88

Выходной файл out3.txt
Error, найдено 3 строки вместо 4
total=0

Входной файл in17.txt
5
2
88 1234 2 55 -123 55 123 22 55 66
12 88 99 2222 55 66
12 21 55 897 241
455
-1 -2 -6 -7 -88 -129
1 2 55 22 66 88

Выходной файл out17.txt
1 : sum=1322 : max=1234 : min=88
2 : sum=100 : max=88 : min=12
3 : sum=33 : max=21 : min=12
4 : Error найдено 1 чисел вместо 2
5 : sum=-3 : max=-1 : min=-2
total=5801

Выходной файл result.txt отсортирован по total
out1.txt
out17.txt
out2.txt
out3.txt
