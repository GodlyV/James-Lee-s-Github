using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class Login
    {
        [Key]
        public int LoginID { get; set; }

        [Display(Name = "Email Address")]
        [Required(ErrorMessage = "Email must be specified.")]
        [RegularExpression(@"^\w+[\w-\.]*\@\w+((-\w+)|(\w*))\.[a-z]{2,3}$", ErrorMessage = "Invalid Email Format (ex.: john@gmail.com)")]
        public string Email { get; set; }

        [NotMapped]
        [Display(Name = "Password")]
        [Required(ErrorMessage = "Password is required")]
        [DataType(DataType.Password)]
        public string Password { get; set; }

        [DataType(DataType.Date)]
        public DateTime LoginTime { get; set; } = DateTime.Now;
    }
}
