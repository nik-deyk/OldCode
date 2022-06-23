"""Main bot script."""
import logging
import os
import time
import telebot
import flag
from telebot.types import InlineKeyboardButton, InlineKeyboardMarkup
from users import UserDatabase, WireGuardConfig, User, UserState
from tokens import TokenDatabase, TokenState
from trans.i18n_base_middleware import I18N

logging.basicConfig(
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    level=logging.INFO
)
logger = logging.getLogger('MainScript')

try:
    TELEGRAM_TOKEN = os.environ['vpn_bot_token']
except Exception as exc:
    logger.error("Couldn't find VPN BOT token in environment variables. Please, set it!")
    raise ModuleNotFoundError from exc

bot = telebot.TeleBot(TELEGRAM_TOKEN, parse_mode=None, use_class_middlewares=True)

tokens = TokenDatabase()
usrs = UserDatabase(tokens)

i18n = I18N(translations_path='trans', domain_name='messages')
i18n.users = usrs
_ = i18n.gettext

# """--------------------BOT-HANDLERS----------------------"""

def gen_markup(keys: dict, row_width: int) -> InlineKeyboardMarkup:
    """Create inline keyboard of given shape with buttons.."""
    markup = InlineKeyboardMarkup()
    markup.row_width = row_width
    for conf_data, conf_text in keys.items():
        markup.add(InlineKeyboardButton(
            conf_text, callback_data=conf_data))
    return markup

@bot.message_handler(commands=['start', 'help'])
def help_command(message):
    """Return commands and their descriptions."""
    user = usrs.obtain(message.chat.id)
    if user.state == UserState.HAVE_TOKEN:
        bot.send_message(user.chat_id, _("Run /help to ask help;"
                                        "\nRun /config to get config."))
    else:
        bot.send_message(user.chat_id, _("Run /help to ask help;"
                                        "\nRun /token to pass your access token;"
                                        "\nRun /config to get config."))

@bot.message_handler(commands=['settings'])
def settings_command(message):
    """Return settings markup."""
    bot.send_message(chat_id=message.chat.id,
                     text=_("Settings menu"),
                     reply_markup=gen_markup({"change_language": _("Change language")}, 1))

@bot.callback_query_handler(func=lambda call: call.data == 'settings')
def settings_menu_query(call):
    """Handle settings menu."""
    bot.edit_message_text(_("Settings menu"), call.message.chat.id,
                              call.message.message_id, reply_markup=gen_markup({"change_language": _("Change language")}, 1))

@bot.callback_query_handler(func=lambda call: call.data == 'change_language')
def change_language_menu_query(call):
    """Handle change language settings menu."""
    config: dict = {}
    for lang_name, flag_name in {"ru": "ru", "en": "gb"}.items():
        config["change_lang_to_" + lang_name] = f"{flag.flag(flag_name)} {lang_name}"
    config["settings"] = _(" « Back")
    bot.edit_message_text(_("Select your language:"), call.message.chat.id,
                              call.message.message_id, reply_markup=gen_markup(config, 1))


@bot.callback_query_handler(func=lambda call: call.data.startswith("change_lang_to_"))
def change_user_language(call):
    """Change user language."""
    user = usrs.obtain(call.message.chat.id)
    old_lang: str = user.lang
    new_lang: str = call.data.removeprefix("change_lang_to_")
    if old_lang != new_lang:
        old_lang = user.lang
        user.lang = new_lang
        logger.info("User language changed: id %d", user.chat_id)
        bot.answer_callback_query(call.id, _("Language was changed to ", new_lang) + new_lang)
        i18n.switch(new_lang)
        change_language_menu_query(call)
        # Cant just call it: Telegram raise exception when you try to change text to the same one.

@bot.message_handler(commands=['token'])
def token_command(message):
    """Wait for user to pass token."""
    user = usrs.obtain(message.chat.id)
    if standart_ban_workflow(user):
        return
    if user.state == UserState.HAVE_TOKEN:
        bot.send_message(user.chat_id, _("You do not need to pass token since "
                                        "you have already one: %s") % (user.token.id,))
    else:
        user.state = UserState.GONNA_READ_TOKEN
        bot.send_message(user.chat_id, _("Please, give me your token"))

@bot.message_handler(commands=['config'])
def config_command(message):
    """Return config file."""
    user = usrs.obtain(message.chat.id)
    if standart_ban_workflow(user):
        return
    if user.state == UserState.HAVE_TOKEN:
        wg_conf = WireGuardConfig(user.token.id, usrs.used_ips())
        result, error = wg_conf.obtain()
        if result:
            path = wg_conf.get()
            with open(path, 'r', encoding='utf-8') as config:
                bot.send_document(user.chat_id,
                                  document=config)
            if user.address is None:
                user.address = wg_conf.address()
            if user.config_create_time is None:
                user.config_create_time = int(time.time())
        else:
            bot.send_message(user.chat_id, _("Sorry, server error occurred: %s.") % (error,))
            logger.error("User %d got the server error %s", user.chat_id, error)
    else:
        bot.send_message(user.chat_id, _("Please, pass you /token firstly."))


@bot.message_handler(content_types='text')
def text_message_handler(message):
    """Get access token."""
    user = usrs.obtain(message.chat.id)
    if standart_ban_workflow(user):
        return
    if user.state != UserState.GONNA_READ_TOKEN:
        bot.send_message(user.chat_id, _("Sorry, command not recognized :(\nTry /help"))
    else:
        user.token_requests += 1
        if (token := tokens.get(message.text)) is not None:
            if token.state == TokenState.FREE:
                user.state = UserState.HAVE_TOKEN
                token.state = TokenState.ACTIVATED
                user.token = token
                bot.send_message(user.chat_id, _("Activation successful! Go and get your /config!"))
            else:
                bot.send_message(user.chat_id, _("Sorry, this token is already activated :( Try another one?"))
        else:
            user.state = UserState.NO_TOKEN
            bot.send_message(user.chat_id, _("Token not found : '%s'") % (message.text,))

# """-------------------ADDITION-FUNCTIONS--------------------"""

def standart_ban_workflow(user: User) -> bool:
    """Check if the user is banned and send message to him if so.

    Return true if user is banned.
    """
    if user.banned:
        bot.send_message(user.chat_id, _("Sorry, you was banned. Ask @velikiy_prikalel for details."))
        return True
    return False

bot.setup_middleware(i18n)
bot.infinity_polling()
