using Microsoft.AspNetCore.Mvc.Rendering;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class CategoryListModel
    {
        public int productCategoryId { get; set; }
        public string Name { get; set;}

    }
}
