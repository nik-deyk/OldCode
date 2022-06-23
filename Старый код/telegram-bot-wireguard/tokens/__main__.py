"""Import token operations."""
import argparse
from tokens import TokenDatabase

def check_positive(value):
    """Check that input argument is positive."""
    try:
        value = int(value)
    except ValueError as exs:
        raise Exception(f"{value} is not an integer") from exs
    if value <= 0:
        raise argparse.ArgumentTypeError(f"{value} is not a positive integer")
    return value


def generate_tokens(num: int):
    """Generete new tokens and show them to the user."""
    tk_base = TokenDatabase()
    for _ in range(0, num):
        print(tk_base.create().id)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Create new tokens and insert them to database.')
    parser.add_argument("-t", "--tokens", type=check_positive,
                        help='Number of new tokens to add to the storage')
    args = parser.parse_args()
    if args.tokens is not None:
        generate_tokens(args.tokens)
