# Основы работы с Hibernate

[Вебинар от Артёма Полозова.](https://vk.com/video-111905078_456246643)

На вебинаре:
1. Основные понятия. Что такое Hibernate?
2. Создание и настройка БД в выбранной СУБД.
3. Реализация простого подключения к БД через `java.sql`.
4. Подключение через Hibernate.

## Чем отличается мой код от того, что был написан на семинаре

1. `Gradle` вместо `maven`.
2. `PostgreSQL` вместо `MySQL`.
3. Ключ `owner` в таблице `tasks_table` ссылается на _не-первичный_ ключ в таблице `owners_table`.
4. Классы `Task` и `TaskClass` объединены в один.

## Проверить у себя

1. Убедитесь, что у вас установлен `PostgreSQL` и `java` версии >=17.
2. Создаёте пользователя `alex` в БД с запоминающимся паролем. Выполните эти строки, чтобы записать пароль в файлы конфигурации:

```shell
SQL_USER_PASSWORD=YourCoolPassword # Заменить нужным!
sed -i "s/\\\$\\\$\\\$/${SQL_USER_PASSWORD}/g" src/main/resources/hibernate.xml src/main/resources/config.properties
```

3. Импортируйте `taskmanager_db.sql`, например, так:

```shell
psql -U alex -f taskmanager_db.sql
```

4. Наслаждайтесь магией Gradle:

```shell
./gradlew run
```

_Заметка: можете зайти в DemoApplication.java и поменять способ подключения к БД._

5. Если у вас работает БД и данные были скопированы из `taskmanager_db.sql` успешно, то можете протестировать оба подхода одновременно:

```shell
./gradlew test
```