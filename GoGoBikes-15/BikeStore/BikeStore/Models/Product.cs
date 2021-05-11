using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace BikeStore.Models
{
    public partial class Product
    {
        public Product()
        {
            SalesOrderDetail = new HashSet<SalesOrderDetail>();
        }

        public decimal DiscountPrice
        {
            get
            {
                return ListPrice - Discount;
            }
            
        }


        public int ProductId { get; set; }
        [Display(Name = "Name")]
        public string Name { get; set; }
        [Display(Name = "Product Number")]
        public string ProductNumber { get; set; }
        [Display(Name = "Color")]
        public string Color { get; set; }
        [Range(0.0, Double.MaxValue, ErrorMessage = "Cannot enter negative values.")]
        public decimal StandardCost { get; set; }
        [Display(Name = "List Price")]
        [Range(0.0, Double.MaxValue, ErrorMessage = "Cannot enter negative values.")]
        public decimal ListPrice { get; set; }
        [Display(Name = "Size")]
        public string Size { get; set; }
        [Display(Name = "Weight")]
        [Range(0.0, Double.MaxValue, ErrorMessage = "Cannot enter negative values.")]
        public decimal? Weight { get; set; }
        public int? ProductCategoryId { get; set; }
        public int? ProductModelId { get; set; }
        public DateTime SellStartDate { get; set; }
        public DateTime? SellEndDate { get; set; }
        public DateTime? DiscontinuedDate { get; set; }
        public byte[] ThumbNailPhoto { get; set; }
        public string ThumbnailPhotoFileName { get; set; }
        public Guid Rowguid { get; set; }
        public DateTime ModifiedDate { get; set; }
        public string Description { get; set; }
        [Display(Name="On Sale")]
        public bool OnSale { get; set; }
        [Range(0.0, Double.MaxValue, ErrorMessage ="Cannot enter negative values.")]
        public decimal Discount { get; set; }
        [Range(0, Int32.MaxValue, ErrorMessage = "Cannot enter negative values.")]
        public int Quantity { get; set; }

        public virtual ProductCategory ProductCategory { get; set; }
        public virtual ProductModel ProductModel { get; set; }
        public virtual ICollection<SalesOrderDetail> SalesOrderDetail { get; set; }
    }
}
