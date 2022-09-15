from flask import Flask, render_template, request, url_for
import sqlite3
import random

app = Flask(__name__)

def random_image():
    str_list = ['img/lego/spiderman_lego.png', 
    'img/lego/halkman_lego.png',
    'img/lego/batman_lego.png']
    return url_for('static', filename=random.choice(str_list))

def get_password_by_id(con, id):
    users_table = con.execute("SELECT * FROM passwords WHERE id = '" + str(id) + "'").fetchall()
    if (len(users_table) == 0 or len(users_table) > 1):
        return ""
    return users_table[0]['password']

@app.route('/users')
def index():
    con = sqlite3.connect('Users.db')
    con.row_factory = sqlite3.Row

    users_table = con.execute("SELECT id, login FROM users WHERE status = 'active' order by cast(id as unsigned)").fetchall()

    con.close()

    return render_template('index.html', users=users_table)

@app.route('/by-login')
def index_by_login():
    if (len(request.args) == 0 or len(request.args) > 1):
        return render_template('error_page.html', error_message=("Please, enter login"))
        
    user = request.args.get('login')
    if user is None or "'" in user or '"' in user or '<' in user or '>' in user or '-' in user:
        return render_template('error_page.html', error_message="No login specified")

    else:
        con = sqlite3.connect('Users.db')
        con.row_factory = sqlite3.Row

        users_table = con.execute("SELECT * FROM users WHERE login = '" + user + "'").fetchall()


        if (len(users_table) == 0 or len(users_table) > 1):
            con.close()
            return render_template('error_page.html', error_message="Wrong number of users with such login")
        else:
            pswd=get_password_by_id(con, users_table[0]['id'])
            con.close()

        return render_template('visit_card.html', user_info=users_table[0], user_image=random_image(), user_password=pswd)

@app.route('/by-id')
def index_by_id():
    if (len(request.args) == 0 or len(request.args) > 1):
        return render_template('error_page.html', error_message=("Please, enter id"))
        
    user = request.args.get('id')
    if user is None or not user.isnumeric():
        return render_template('error_page.html', error_message="No id specified")

    else:
        con = sqlite3.connect('Users.db')
        con.row_factory = sqlite3.Row

        users_table = con.execute("SELECT * FROM users WHERE id = '" + user + "'").fetchall()

        if (len(users_table) == 0 or len(users_table) > 1):
            con.close()
            return render_template('error_page.html', error_message="Wrong number of users with such id")
        else:
            pswd=get_password_by_id(con, users_table[0]['id'])
            con.close()

        return render_template('visit_card.html', user_info=users_table[0], user_image=random_image(), user_password=pswd)

@app.errorhandler(404)
def page_not_found(error):
   return render_template('error_page.html'), 404