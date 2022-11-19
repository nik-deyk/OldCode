Текст задания:

# Торт #1.

*Во-первых:* прога вводит последовательность int чисел 

```
12
34
6546544
0
```

Последовательность кончается `0`. 

Последовательность - это высота свечки на торте. Не известно, сколько на торте свечек, но точно не больше `100`. Задача: на выводе дать сумму всех длин всех свечей.

Вывести в консоль результат строкой: `"Длина свечи будет 100"`.

Класс `Cake`. В конструкторе получает массив целых чисел. Имеет метод `.getSum()`, который выводит целое число - сумму массива. В классе должно быть как минимум одно `private` поле.

*Еще одна задача:* сделать в `Cake` функцию (**не static**), которая принимает аргументом другой объект класса `Cake` и возвращает длину свечи, если склеить свечу этого торта (от которого вызывается данная функция) и другого (который передаётся в аргументе).

*Ещё одна задача:* сделать внутри класса была private переменная (**static**), которая хранит сумму всех свечей всех когда-либо созданных тортов. К классу `Cake` добавить публичную функцию, которая возвращает это значение `getSumCandleOfAllCakes()`.

Чтобы проверить, что данная задача выполнена, надо немного доработать основной функционал главной функции `Main`:
После вывода `"Длина свечи будет 100".` в `Main` надо создать новый объект класса `new Cake([11, 12, 232])`. Потом вывести еще и `getSumCandleOfAllCakes()`.