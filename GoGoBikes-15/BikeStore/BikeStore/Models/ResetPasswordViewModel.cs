using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Views.Home
{
    public class ResetPasswordViewModel
    {
        [Required(ErrorMessage = "Email must be specified.")]
        [RegularExpression(@"^\w+[\w-\.]*\@\w+((-\w+)|(\w*))\.[a-z]{2,3}$", ErrorMessage = "Invalid Email Format (ex.: john@gmail.com)")]
        public string Email { get; set; }
        [NotMapped]
        [Display(Name = "Password")]
        [Required(ErrorMessage = "This is required")]
        [StringLength(60, ErrorMessage = "Must be between 5 and 60 characters", MinimumLength = 5)]
        [DataType(DataType.Password)]
        public string Password { get; set; }
        [Display(Name = "Confirm Password")]
        [Required(ErrorMessage = "This is required")]
        [NotMapped]
        [StringLength(60, ErrorMessage = "Must be between 5 and 60 characters", MinimumLength = 5)]
        [Compare("Password", ErrorMessage = "The fields Password and Confirm Password should be equal")]
        public string ConfirmPassword { get; set; }
    }
}
