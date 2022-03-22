# Описание

Здесь хранится простой сайт-банк с выборкой людей и каким-никаким фронтендом.

# Установить:
pip3, python3, my_env, flask, sqlite3.
build-essential, libssl-dev, libffi-dev, python-dev.

# Запуск:
https://www.digitalocean.com/community/tutorials/how-to-make-a-web-application-using-flask-in-python-3-ru

# Если коротко:
1)создать виртуальную среду.
```
python3 -m venv my_env
```
2)войти в среду:
```
source my_env/bin/activate
```
3)установить flask:
```
pip3 install flask
```
4)проверить, что flask поставился:
```
python -c "import flask; print(flask.__version__)"
```
```
Output: 2.0.1
```
5)установить переменные окружения:
```
export FLASK_APP=main
export FLASK_ENV=development
```
6)запуск:
```
flask run
```
вывод должен быть таким:
```
 * Serving Flask app 'main' (lazy loading)
 * Environment: development
 * Debug mode: on
 * Running on http://127.0.0.1:5000/ (Press CTRL+C to quit)
 * Restarting with stat
 * Debugger is active!
 * Debugger PIN: 141-800-643
```
8)В браузере зайти на ``` http://127.0.0.1:5000/ ```

9)Если хотите изменить бд:
```
sqlite3 Users.db 
```
Вывод будет такой:
```
SQLite version 3.31.1 2020-01-27 19:55:54
Enter ".help" for usage hints.
```
10)Если не знаете синтаксис sqlite, выполняйте
```
.help
```
11)Выход из субд:
```
.quit
```
12)Выполнение инициального скрипта:
```
.read init-db.sql
```
12)Экстренные вопросы:

https://vk.com/velikiy_prikalel