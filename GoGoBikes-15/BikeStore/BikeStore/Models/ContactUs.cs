using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class ContactUs
    {
        [Key]
        public int ContactID { get; set; }

        [Required(ErrorMessage = "First Name must be specified.")]
        [RegularExpression(@"^[a-zA-Z]+$", ErrorMessage = "First Name can only contain letters.")]
        public string FirstName { get; set; }

        [Required(ErrorMessage = "Last Name must be specified.")]
        [RegularExpression(@"^[a-zA-Z]+$", ErrorMessage = "Last Name can only contain letters.")]
        public string LastName { get; set; }

        [Required(ErrorMessage = "Postal Code must be specified.")]
        [RegularExpression(@"^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$", ErrorMessage = "Invalid postal code format.")]
        public string PostalCode { get; set; }

        [Required(ErrorMessage = "Email must be specified.")]
        [EmailAddress]
        public string Email { get; set; }

        [Required(ErrorMessage = "Subject must be specified.")]
        public string Subject { get; set; }

        [RegularExpression(@"^\s*(?:\+?(\d{1,3}))?[-. (]*(\d{3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*$", ErrorMessage = "Invalid phone number format.")]
        public string Phone { get; set; }

        [Required(ErrorMessage = "Questions and Comments Section must be filled.")]
        public string QuestionsAndComments { get; set; }

        public DateTime SubmitionTime { get; set; }
    }
}