using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace BikeStore.Models
{
    public class BikeStoreContext : DbContext
    {
        public BikeStoreContext (DbContextOptions<BikeStoreContext> options)
            : base(options)
        {
        }

        public DbSet<BikeStore.Models.Review> Review { get; set; }
    }
}
