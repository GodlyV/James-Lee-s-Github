using Microsoft.AspNetCore.Authentication;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BikeStore.Models
{
    public class SignInModel
    {
        public AuthenticationScheme[] auth { get; set; }
        public Login login { get; set; }
    }
}
