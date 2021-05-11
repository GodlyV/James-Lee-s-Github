using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class Account
    {
        [Key]
        public int Id { get; set; }

        [Display(Name = "First Name")]
        [StringLength(100, ErrorMessage = "Must be between 2 and 100 characters", MinimumLength = 2)]
        [Required(ErrorMessage = "This is required")]
        public string firstName { get; set; }

        [Display(Name = "Last Name")]
        [Required(ErrorMessage = "This is required")]
        [StringLength(100, ErrorMessage = "Must be between 2 and 100 characters", MinimumLength = 2)]

        public string lastName { get; set; }

        [Display(Name = "Email")]
        [EmailAddress]
        [Required(ErrorMessage = "This is required")]
        public string email { get; set; }

        [Display(Name = "Password")]
        [Required(ErrorMessage = "This is required")]
        [StringLength(255, ErrorMessage = "Must be between 5 and 255 characters", MinimumLength = 5)]
        [DataType(DataType.Password)]
        public string password { get; set; }

        [Display(Name = "Confirm Password")]
        [Required(ErrorMessage = "This is required")]
        [NotMapped]
        [StringLength(255, ErrorMessage = "Must be between 5 and 255 characters", MinimumLength = 5)]
        [Compare("password", ErrorMessage = "The fields Password and Confirm Password should be equal")]
        public string confirmPassword { get; set; }

        [DataType(DataType.Date)]
        public DateTime TimeStamp { get; set; }
    }
}
