using System;
using System.Text;

// модификатор доступа : private/public

// примитивный тип: int (0, -1, 21), float (0.1545), char ('a', 'b'), string ("sfjs"), bool
// примитивный тип не поддерживает значения null

// static: из статичной функции можно обращаться только к статическим переменным

/*
1) Саша: делает Calculator
2) Саша: смотри, Леша, Calculator; как его использовать:
c = new Calculator(text); <<-- создать объект, он сам все сохранит и сделает.
    System.FormatException <<-- выкинет, если что-то не так
c.get() <<-- получить сумму.
3) Леша: ага, все ясно.
*/

// exception, try/catch, debug

namespace Hello {
    class Program {
        public static void Main(string[] args) {
            string text = Console.ReadLine(); // "12+abc";

            try {
                Calculator c = new Calculator(text);
                Console.WriteLine("Sum is : " + c.get()); // 146
            } catch (FormatException e) {
                Console.WriteLine("Your string is wrong");
            } catch (ArgumentException e) {
                Console.WriteLine("String do not contain sign +");
            }
        }
    }
}

/**

*/