using System;
using System.Collections.Generic;


namespace Hello
{
    public class Programma
    {
        public static int[] massiv = new int[100];

        public static void Main()
        {
            int i = 0;
            while (i < massiv.Length)
            {
                string consol = Console.ReadLine();
                int intconsol = Convert.ToInt32(consol);

                if (intconsol == 0)
                {
                    break;
                }

                massiv[i] = intconsol;
                i += 1;
            }

            Cake a = new Cake(massiv);
            Cake b = new Cake(new int[] { 1, 3, 3, 3, 3 }); //13
            Cake c = new Cake(new int[] { 2, 2, 2, 2, 2 }); //10
            Console.WriteLine(Cake.getSumAllOfCandlesOfAllCakes());
        }
    }

    public class Cake
    {
        private static List<Cake> cakes = new List<Cake>();
        private int p;

        private static int summList(List<Cake> c)
        {
            int summa = 0;
            for (int i = 0; i < c.Count; i++)
            {
                summa += c[i].getSum();
            }
            return summa;
        }

        private Cake(int p2)
        {
            p = p2;
        }

        public Cake(int[] svechki)
        {
            int summa = 0;
            for (int i = 0; i < svechki.Length; i++)
            {
                summa += svechki[i];
            }
            p = summa;
            cakes.Add(this);
        }

        public Cake merge(Cake other)
        {
            return new Cake(this.p + other.p);
        }

        public int getSum()
        {
            return p;
        }

        public static int getSumAllOfCandlesOfAllCakes()
        {
            return summList(cakes);
        }
    }
}