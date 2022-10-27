using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Hello
{
    public class Utility
    {
        private static int countsOfObjects = 0;

        public Utility() {
            countsOfObjects += 1;
        }

        public int getCounts() {
            return Utility.countsOfObjects;
        }
    }
}