using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class Review
    {
        [Key]
        public int ReviewId { get; set; }

        //[ForeignKey("Customer")]
        public int CustomerId { get; set; }

        [Required]
        public string Comment { get; set; }

        [Required]
        [Range(1,5,ErrorMessage="Rating must be between 1 and 5 stars")]
        public int Rating { get; set; }

    }
}
