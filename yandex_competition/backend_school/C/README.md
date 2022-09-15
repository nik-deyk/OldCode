# Задача C

Выделил отдельный проект под неё, потому что там исполоьзовалась зависимость от `json-simple`.

# Пример 

```
[{"id": 1, "name": "Asus notebook","price": 1564,"date": "23.09.2021"},{"price": 2500, "id": 3, "date": "05.06.2020", "name": "Keyboardpods" }, {"date": "23.09.2021", "name": "Airpods","id": 5, "price": 2300}, {"name": "EaRPoDs", "id": 2, "date": "01.01.2022", "price": 2200}, { "id": 4, "date": "23.09.2021", "name": "Dell notebook",  "price": 2300}]
PRICE_LESS_THAN 2400
DATE_AFTER 23.09.2021
NAME_CONTAINS pods
PRICE_GREATER_THAN 2200
DATE_BEFORE 02.01.2022
```

Задача: отсортировать всё это счастье по id, при этом отфильтровать по указанным фильтрам

Результат: все тесты пройдены.

```
[{"name": "EaRPoDs", "id": 2, "date": "01.01.2022", "price": 2200}, {"date": "23.09.2021", "name": "Airpods", "id": 5, "price": 2300}]
```