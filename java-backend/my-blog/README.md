# Простейший блог на Spring и h2

Результат семинара https://vk.com/video-111905078_456243212.

Прикольно, h2 я до этого не знал. Было интересно, хоть и просто.

## Отзыв

Самая интересная секция, потому что семинар довольно простой.

- Поговорили про dependency injection и т.п.
- Мне пришлось поотлаживать DB и поработать с Java Stream.
- Добавили стилей и получился прикольный сайт.
- Воспользовались Spring Data JPA.
- Я конечно, немного всё усложнил и сделал так, что у меня не 2 разных класса `Post` а один наследуется от другого и это наследование используется в определении методов `PostService` интерфейса.

## Что можно улучшить?

- Добавить нормальную БД (Postgres).
- Добавить теги к постам и сделать их нормальное отображение (например, как статьи на habr, заголовок и начало текста).
- Сделать так, чтобы не все посты сразу показывались, а можно было пролистать (как в гугл результаты внизу имеют страницы).
- Добавить фильтр по тэгам. Тэгов всего должно быть 3: `log` (что сегодня произошло), `info` (урок/тутор/шпоргалка), `opinion` (мнение по теме).
- Добавить главную страницу, где написать, кто автор, в одном абзаце рассказать о себе, показать свою фотку и сослаться на другие соц-сети.
- Добавить тг-бота, чтобы из телеги можно было бы отправлять посты с картинкой (пишешь тг-боту, он отправляет пост на сайт). Желательно, конечно, сделать картинку опциональной.
- Добавить возможность комментирования под постами, чтобы люди могли реагировать. Перед публикацией на сайте комментарий отправляется по тг-боту автору, где автор может принять или не принять комментарий. Было бы вообще каширно, если бы можно было бы добавить модераторов или machine learning, чтобы не весь спам пришлось бы разгребать.
- Ну и защитить от Ddos, естественно.
- Защититься от сканирования портов: https://vk.com/@greyteam-kak-zaschititsya-ot-skanirovaniya-portov-i-shodan
- Выполнить деплой: https://habr.com/ru/post/687210/
