
using static System.Formats.Asn1.AsnWriter;
namespace NikitaVersion
{





    class Cake
    {
        public static List<Cake> cakes = new List<Cake>();
        private Dictionary<int, double> scales_of_cakes = new Dictionary<int, double>();

        private int dia;
        private double value;
        private double scale;

        private double justGetScale()
        {
            scale = Math.PI * this.dia * this.dia / 4;
            return scale;
        }

        private double GetScale()
        {
            if (scales_of_cakes.TryGetValue(dia, out value) == true) { scale = value; }
            else { justGetScale(); }
            return scale;
        }

        public Cake(int dia)
        {
            this.dia = dia;
            cakes.Add(this);
            if (scales_of_cakes.ContainsKey(dia) == false) { scales_of_cakes.Add(dia, justGetScale()); }
        }
    }
}