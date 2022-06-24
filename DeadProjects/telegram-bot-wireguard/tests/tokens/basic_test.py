"""Unit tests for token module."""
import unittest
import os
from tokens import TokenDatabase, TokenState

# pylint: disable=C0116,C0103
class TestStorageMethods(unittest.TestCase):
    """Test that storage works properly."""

    test_db = "test"

    def setUp(self): # noqa: D102
        self.tk = TokenDatabase(database_name=self.test_db)

    def tearDown(self): # noqa: D102
        if os.path.exists(self.test_db + ".db"):
            os.remove(self.test_db + ".db")

    def test_created_token_is_valid(self): # noqa: D102
        t = self.tk.create()
        self.assertEqual(self.tk.get(t.id).state, t.DEFAULT_STATE)

    def test_created_token_is_invalid_after_deactivation(self): # noqa: D102
        t = self.tk.create()
        t.state = TokenState.ACTIVATED
        self.assertEqual(self.tk.get(t.id).state, TokenState.ACTIVATED)

    def test_random_shit_token_is_invalid(self): # noqa: D102
        self.assertFalse(self.tk.sanitize_token("awesome Python enjoyer"))
