using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BikeStore.Models
{
    public partial class Customer
    {
        public enum PrivilegeType
        {
            Admin, Moderator, User
        }

        public Customer()
        {
            CustomerAddress = new HashSet<CustomerAddress>();
            SalesOrderHeader = new HashSet<SalesOrderHeader>();
        }

        

        [Key]
        public int CustomerId { get; set; }

        public bool NameStyle { get; set; }

        public string Title { get; set; }

        [Required(ErrorMessage = "First Name must be specified.")]
        [RegularExpression(@"^[a-zA-Z]+$", ErrorMessage = "First Name can only contain letters.")]
        public string FirstName { get; set; }

        public string MiddleName { get; set; }

        [Required(ErrorMessage = "Last Name must be specified.")]
        [RegularExpression(@"^[a-zA-Z]+$", ErrorMessage = "Last Name can only contain letters.")]
        public string LastName { get; set; }

        public string Suffix { get; set; }

        public string CompanyName { get; set; }

        public string SalesPerson { get; set; }

        [Required(ErrorMessage = "Email must be specified.")]
        [RegularExpression(@"^\w+[\w-\.]*\@\w+((-\w+)|(\w*))\.[a-z]{2,3}$", ErrorMessage = "Invalid Email Format (ex.: john@gmail.com)")]
        public string EmailAddress { get; set; }

        [RegularExpression(@"^\s*(?:\+?(\d{1,3}))?[-. (]*(\d{3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*$", ErrorMessage = "Invalid phone number format (ex.: 123-456-7890)")]
        public string Phone { get; set; }

        public string PasswordHash { get; set; }

        public string PasswordSalt { get; set; }

        public Guid Rowguid { get; set; }

        public DateTime ModifiedDate { get; set; }

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
        [DataType(DataType.Password)]
        public string ConfirmPassword { get; set; }
        public string Privileges { get; set; }


        public virtual ICollection<CustomerAddress> CustomerAddress { get; set; }
        public virtual ICollection<SalesOrderHeader> SalesOrderHeader { get; set; }
    }
}
