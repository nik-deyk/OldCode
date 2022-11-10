namespace Hello
{
    public class Calculator
    {
        private readonly int sum;

        public Calculator(string input) {
            int plusSignPosition = input.IndexOf('+'); // "12+12" ---> 2 ;;;; "152+3" --->> 3 ;;;;; "abc" ---> -1

            if (plusSignPosition != -1) { // "12+14"
                string number_a = input.Substring(0, plusSignPosition); // "12"
                string number_b = input.Substring(plusSignPosition + 1); // "14"

                int a = Convert.ToInt32(number_a);
                int b = Convert.ToInt32(number_b);
                sum = a + b;
            } else {
                throw new ArgumentException("У вас строка должна содержать +, а там его нет");
            }
        }

        public int get() {
            return sum;
        }
    }
}