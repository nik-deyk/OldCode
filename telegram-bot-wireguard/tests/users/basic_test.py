"""Unit tests for users module."""
import unittest
import os
from ipaddress import IPv4Address
from tokens import TokenDatabase, TokenState
from users import UserDatabase, UserState

# pylint: disable=C0116,C0103
class TestUserStorageMethods(unittest.TestCase):
    """Test that users database works properly."""

    test_tokens_db = "test_token"
    test_users_db = "test_user"

    def setUp(self): # noqa: D102
        self.tk = TokenDatabase(database_name=self.test_tokens_db)
        self.usrs = UserDatabase(self.tk, database_name=self.test_users_db)

    def tearDown(self): # noqa: D102
        for db_name in (self.test_tokens_db, self.test_users_db):
            if os.path.exists(db_name + ".db"):
                os.remove(db_name + ".db")

    def test_created_user_has_default_state(self): # noqa: D102
        # pylint: disable=E1101
        u = self.usrs.add(123)
        self.assertEqual(self.usrs.get(u.chat_id).state, u.DEFAULT_STATE)

    def test_created_user_state_can_be_changed(self): # noqa: D102
        u = self.usrs.add(123)
        u.state = UserState.GONNA_READ_TOKEN
        self.assertEqual(self.usrs.get(u.chat_id).state, UserState.GONNA_READ_TOKEN)

    def test_add_token_to_created_user(self): # noqa: D102
        t = self.tk.create()
        u = self.usrs.add(123)
        u.token = t
        t.state = TokenState.ACTIVATED
        self.assertEqual(self.usrs.get(u.chat_id).token.state, TokenState.ACTIVATED)

    def test_used_ips_are_returned_after_some_users_creations(self): # noqa: D102
        data: dict = {
            12: '10.0.0.23',
            23: '10.0.0.183',
            34: '10.0.0.56'
        }
        for user_id, ip in data.items():
            u = self.usrs.add(user_id)
            u.address = IPv4Address(ip)
        self.assertSetEqual(self.usrs.used_ips(), set(IPv4Address(ip) for ip in data.values()))
