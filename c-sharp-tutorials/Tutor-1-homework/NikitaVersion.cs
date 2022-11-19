namespace Nikita
{
    public class NikitaVersion
    {
        public static void Main()
        {
            int[] m = new int[100];

            for (int i = 0; i < m.Length; i++)
            {
                m[i] = Convert.ToInt32(Console.ReadLine());
                if (m[i] == 0)
                    break;
            }

            Cake first_cake = new Cake(m);
            Console.WriteLine($"Длина свечи первого торта {first_cake.getSum()}"); // Под-задача #1.

            Cake second_cake = new Cake(new int[] { 300 });
            int two_candles_length = first_cake.glueCandle(second_cake).getSum();
            Console.WriteLine($"Длина склеенных свечей 1го и 2го тортов {two_candles_length}"); // Под-задача #2.

            Cake third_cake = new Cake(new int[] { 1, 2, 3, 4, 5 }); // 15
            Console.WriteLine($"Длина всех свечей всех когда-либо созданных тортов {Cake.getSumAllOfCandlesOfAllCakes()}"); // Под-задача #3.
        }
    }

    class Cake
    {
        private static List<Cake> cakes = new List<Cake>();

        private int sum;

        public static int getSumAllOfCandlesOfAllCakes()
        {
            int all_sum = 0;
            for (int i = 0; i < cakes.Count; i++)
            {
                all_sum += cakes[i].getSum();
            }
            return all_sum;
        }

        public Cake(int[] a)
        {
            this.sum = 0;
            for (int i = 0; i < a.Length; i++)
            {
                this.sum += a[i];
            }
            cakes.Add(this);
        }

        private Cake(int sum)
        {
            this.sum = sum;
        }

        public Cake glueCandle(Cake other)
        {
            return new Cake(this.sum + other.sum);
        }

        public int getSum()
        {
            return sum;
        }
    }
}