using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class BikeListModel : AdventureWorksLT2017gr15Context
    {
        public string ProductModel { get; set; }
        public string Description { get; set; }
        public int ProductModelID { get; set; }
    }
}
