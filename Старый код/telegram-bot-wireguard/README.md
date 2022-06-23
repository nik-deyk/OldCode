# GLADIATOR

_welcome to the club, body_

```
# Install python on your machine:
sudo apt install python3.10 python3.10-venv python3.10-dev wireguard
# Compile translations:
msgfmt -o trans/ru/LC_MESSAGES/messages.mo trans/ru/LC_MESSAGES/messages.po
# Install python modules:
pip install -r requirements.txt
# Select telegram token:
export vpn_bot_token="TOKEN-FROM-BOT-FATHER"
# Optionally configure wireguard server (if you have not so):
chmod +x setup-server.sh
sudo ./setup-server.sh
# Run bot:
sudo python bot.py
```

**NOTE:** Without `sudo` your bot will not be able to manage wireguard. **But!** it will stop after some time when running with `sudo`. **That means** that in current implementation you can not run the bot and leave the server. However, you can fix this by creating a new user
with permissions for wireguard and run the bot from that user (not from `sudo`). That is the one possible solution.

## TODO:

- Write documentation.
