using Microsoft.AspNetCore.Mvc.Rendering;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class SearchResultModel
    {
        public IEnumerable<BikeListModel> bikes { get; set; }
        public int ProductCategoryId { get; set; }
        public List<CategoryListModel> categories { get; set; }
    }
}