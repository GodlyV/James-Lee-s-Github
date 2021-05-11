using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using BikeStore.Models;
using System.Security.Claims;

namespace BikeStore.Controllers
{
    public class ProductsController : Controller
    {
        private readonly AdventureWorksLT2017gr15Context db;
        private readonly String ADMIN = "Admin";
        private readonly String NAME = "Name";
        private readonly String EMAIL = "Email";
        private readonly String ROLE = "Role";

        public ProductsController(AdventureWorksLT2017gr15Context context)
        {
            db = context;
        }

        public void Index()
        {
            Categories();
        }

        enum Category_Name
        {
            Bikes = 1,
            Components,
            Clothing,
            Accessories,
            Mountain_Bikes,
            Road_Bikes,
            Touring_Bikes,
            Handlebars,
            Bottom_Brackets,
            Brakes,
            Chains,
            Cranksets,
            Derailleurs,
            Forks,
            Headsets,
            Mountain_Frames,
            Pedals,
            Road_Frames,
            Saddles,
            Touring_Frames,
            Wheels,
            Bib_Shorts,
            Caps,
            Gloves,
            Jerseys,
            Shorts,
            Socks,
            Tights,
            Vests,
            Bike_Racks,
            Bike_Stands,
            Bottles_and_Cages,
            Cleaners,
            Fenders,
            Helmets,
            Hydration_Packs,
            Lights,
            Locks,
            Panniers,
            Pumps,
            Tires_and_Tubes
        }

        const int DEFAULT_DISPLAY = 12;

        #region Search
        public IActionResult Search(string searchString, int page = 1, int displayNum = DEFAULT_DISPLAY)
        {
            var categories = db.ProductCategory
                .Where(c => c.ParentProductCategoryId != null)
                .Select(c => new CategoryListModel
                {
                    productCategoryId = c.ProductCategoryId,
                    Name = c.Name
                }).ToList();

            var ret = new SearchResultModel
            {
                categories = categories,
            };

            List<BikeListModel> searchedBikes;

            if(searchString != null)
            {
                searchedBikes = db.VProductAndDescription
                    .Where(b => b.Culture == "en" && b.SellEndDate == null && (b.Name.Contains(searchString) || b.Description.Contains(searchString)))
                    .Select(b => new BikeListModel
                    {
                        ProductModel = b.Name,
                        Description = b.Description,
                        ProductModelID = b.ProductModelId
                    }).Distinct().ToList();
            }
            else
            {
                searchedBikes = db.VProductAndDescription
                    .Where(b => b.Culture == "en" && b.SellEndDate == null)
                    .Select(b => new BikeListModel
                    {
                        ProductModel = b.Name,
                        Description = b.Description,
                        ProductModelID = b.ProductModelId
                    }).Distinct().ToList();
            }

            decimal val = (decimal)searchedBikes.Count / displayNum;
            int maxPage = (int)Math.Ceiling(val);

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.CategoryId = null;
            ViewBag.SearchString = searchString;

            if (searchedBikes.Count() > 0)
            {
                int startDisplay = displayNum * (page - 1);
                searchedBikes = searchedBikes.Skip(startDisplay).Take(displayNum).ToList();

                ret.bikes = searchedBikes;
                return View(ret);
            }

            ViewBag.Empty = "";
            return View(ret);
        }
        public IActionResult AdvancedSearch(int? productCategoryId, string searchString, int page = 1, int displayNum = DEFAULT_DISPLAY)
        {
            var categories = db.ProductCategory
                .Where(c => c.ParentProductCategoryId != null)
                .Select(c => new CategoryListModel
                {
                    productCategoryId = c.ProductCategoryId,
                    Name = c.Name
                }).ToList();

            var ret = new SearchResultModel
            {
                categories = categories
            };

            List<BikeListModel> searchedBikes = new List<BikeListModel>();

            if (productCategoryId != null)
            {
                searchedBikes = db.VProductAndDescription
                .Where(b => b.Culture == "en" && b.ProductCategoryId == productCategoryId && b.SellEndDate == null)
                .Select(b => new BikeListModel
                {
                    ProductModel = b.Name,
                    Description = b.Description,
                    ProductModelID = b.ProductModelId
                }).ToList();
            }
            else
            {
                searchedBikes = db.VProductAndDescription
                .Where(b => b.Culture == "en" && b.SellEndDate == null)
                .Select(b => new BikeListModel
                {
                    ProductModel = b.ProductModel,
                    Description = b.Description,
                    ProductModelID = b.ProductModelId
                }).ToList();
            }

            if(searchString != null)
            {
                searchedBikes = searchedBikes
                    .Where(b => b.ProductModel.Contains(searchString) || b.Description.Contains(searchString)).ToList();
            }

            decimal val = (decimal)searchedBikes.Count / displayNum;
            int maxPage = (int)Math.Ceiling(val);

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.CategoryId = productCategoryId;
            ViewBag.SearchString = searchString;

            if (searchedBikes.Count() > 0)
            {
                int startDisplay = displayNum * (page - 1);
                searchedBikes = searchedBikes.Skip(startDisplay).Take(displayNum).ToList();

                ret.bikes = searchedBikes;
                return View("Search", ret);
            }
            ViewBag.Empty = "";
            return View("Search", ret);
        }

        #endregion

        #region Main Categories
        public IActionResult Bikes()
        {
            var bikeList = db.ProductCategory.Where(b => b.ParentProductCategoryId == (int)Category_Name.Bikes);
            ViewBag.Title = Category_Name.Bikes.ToString();
            return View("Category", bikeList);
        }

        public IActionResult Components()
        {
            var componentList = db.ProductCategory.Where(b => b.ParentProductCategoryId == (int)Category_Name.Components);
            ViewBag.Title = Category_Name.Components.ToString();
            return View("Category", componentList);
        }

        public IActionResult Clothing()
        {
            var clothingList = db.ProductCategory.Where(b => b.ParentProductCategoryId == (int)Category_Name.Clothing);
            ViewBag.Title = Category_Name.Clothing.ToString();
            return View("Category", clothingList);
        }

        public IActionResult Accessories()
        {
            var accessoryList = db.ProductCategory.Where(b => b.ParentProductCategoryId == (int)Category_Name.Accessories);
            ViewBag.Title = Category_Name.Accessories.ToString();
            return View("Category", accessoryList);
        }
        #endregion

        #region Categories

        public IActionResult Mountain_Bikes(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Mountain_Bikes, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Mountain Bikes";

            return View("SubCategory", Category((int)Category_Name.Mountain_Bikes, displayNum, page));
        }

        public IActionResult Road_Bikes(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Road_Bikes, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Road Bikes";
            return View("SubCategory", Category((int)Category_Name.Road_Bikes, displayNum, page));
        }

        public IActionResult Touring_Bikes(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Touring_Bikes, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Touring Bikes";
            return View("SubCategory", Category((int)Category_Name.Touring_Bikes, displayNum, page));
        }

        public IActionResult Handlebars(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Handlebars, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Handlebars";
            return View("SubCategory", Category((int)Category_Name.Handlebars, displayNum, page));
        }

        public IActionResult Bottom_Brackets(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Bottom_Brackets, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Bottom Brackets";
            return View("SubCategory", Category((int)Category_Name.Bottom_Brackets, displayNum, page));
        }

        public IActionResult Brakes(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Brakes, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Brakes";
            return View("SubCategory", Category((int)Category_Name.Brakes, displayNum, page));
        }

        public IActionResult Chains(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Chains, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Chains";
            return View("SubCategory", Category((int)Category_Name.Chains, displayNum, page));
        }

        public IActionResult Cranksets(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Cranksets, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Cranksets";
            return View("SubCategory", Category((int)Category_Name.Cranksets, displayNum, page));
        }

        public IActionResult Derailleurs(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Derailleurs, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Derailleurs";
            return View("SubCategory", Category((int)Category_Name.Derailleurs, displayNum, page));
        }

        public IActionResult Forks(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Forks, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Forks";
            return View("SubCategory", Category((int)Category_Name.Forks, displayNum, page));
        }

        public IActionResult Headsets(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Headsets, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Headsets";
            return View("SubCategory", Category((int)Category_Name.Headsets, displayNum, page));
        }

        public IActionResult Mountain_Frames(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Mountain_Frames, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Mountain Frames";
            return View("SubCategory", Category((int)Category_Name.Mountain_Frames, displayNum, page));
        }

        public IActionResult Pedals(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Pedals, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Pedals";
            return View("SubCategory", Category((int)Category_Name.Pedals, displayNum, page));
        }

        public IActionResult Road_Frames(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Road_Frames, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Road Frames";
            return View("SubCategory", Category((int)Category_Name.Road_Frames, displayNum, page));
        }

        public IActionResult Saddles(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Saddles, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Saddles";
            return View("SubCategory", Category((int)Category_Name.Saddles, displayNum, page));
        }

        public IActionResult Touring_Frames(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Touring_Frames, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Touring Frames";
            return View("SubCategory", Category((int)Category_Name.Touring_Frames, displayNum, page));
        }

        public IActionResult Wheels(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Wheels, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Wheels";
            return View("SubCategory", Category((int)Category_Name.Wheels, displayNum, page));
        }

        public IActionResult Bib_Shorts(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Bib_Shorts, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Bib-Shorts";
            return View("SubCategory", Category((int)Category_Name.Bib_Shorts, displayNum, page));
        }

        public IActionResult Caps(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Caps, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Caps";
            return View("SubCategory", Category((int)Category_Name.Caps, displayNum, page));
        }

        public IActionResult Gloves(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Gloves, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Gloves";
            return View("SubCategory", Category((int)Category_Name.Gloves, displayNum, page));
        }

        public IActionResult Jerseys(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Jerseys, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Jerseys";
            return View("SubCategory", Category((int)Category_Name.Jerseys, displayNum, page));
        }

        public IActionResult Shorts(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Shorts, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Shorts";
            return View("SubCategory", Category((int)Category_Name.Shorts, displayNum, page));
        }

        public IActionResult Socks(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Socks, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Socks";
            return View("SubCategory", Category((int)Category_Name.Socks, displayNum, page));
        }

        public IActionResult Tights(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Tights, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Tights";
            return View("SubCategory", Category((int)Category_Name.Tights, displayNum, page));
        }

        public IActionResult Vests(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Vests, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Vests";
            return View("SubCategory", Category((int)Category_Name.Vests, displayNum, page));
        }

        public IActionResult Bike_Racks(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Bike_Racks, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Bike Racks";
            return View("SubCategory", Category((int)Category_Name.Bike_Racks, displayNum, page));
        }

        public IActionResult Bike_Stands(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Bike_Stands, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Bikes Stands";
            return View("SubCategory", Category((int)Category_Name.Bike_Stands, displayNum, page));
        }

        public IActionResult Bottles_and_Cages(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Bottles_and_Cages, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Bottles and Cages";
            return View("SubCategory", Category((int)Category_Name.Bottles_and_Cages, displayNum, page));
        }

        public IActionResult Cleaners(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Cleaners, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Cleaners";
            return View("SubCategory", Category((int)Category_Name.Cleaners, displayNum, page));
        }

        public IActionResult Fenders(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Fenders, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Fenders";
            return View("SubCategory", Category((int)Category_Name.Fenders, displayNum, page));
        }

        public IActionResult Helmets(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Helmets, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Helmets";
            return View("SubCategory", Category((int)Category_Name.Helmets, displayNum, page));
        }

        public IActionResult Hydration_Packs(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Hydration_Packs, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Hydration Packs";
            return View("SubCategory", Category((int)Category_Name.Hydration_Packs, displayNum, page));
        }

        public IActionResult Lights(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Lights, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Lights";
            return View("SubCategory", Category((int)Category_Name.Lights, displayNum, page));
        }

        public IActionResult Locks(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Locks, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Locks";
            return View("SubCategory", Category((int)Category_Name.Locks, displayNum, page));
        }

        public IActionResult Panniers(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Panniers, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Panniers";
            return View("SubCategory", Category((int)Category_Name.Panniers, displayNum, page));
        }

        public IActionResult Pumps(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Pumps, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Pumps";
            return View("SubCategory", Category((int)Category_Name.Pumps, displayNum, page));
        }

        public IActionResult Tires_and_Tubes(int displayNum = DEFAULT_DISPLAY, int page = 1)
        {
            int maxPage = GetPageNum((int)Category_Name.Tires_and_Tubes, displayNum);

            if (maxPage < page)
                page = maxPage;
            else if (page < 1)
                page = 1;

            ViewBag.Page = page;
            ViewBag.MaxPage = maxPage;
            ViewBag.Title = "Tires and Tubes";
            return View("SubCategory", Category((int)Category_Name.Tires_and_Tubes, displayNum, page));
        }
        #endregion

        public IActionResult Categories()
        {
            var categories = db.ProductCategory
                .Where(c => c.ParentProductCategoryId == null)
                .Select(c => new ProductCategoryModel
                {
                    ProductCategoryId = c.ProductCategoryId,
                    Name = c.Name,
                    Description = c.Description
                })
                .Distinct().ToList();

            ViewBag.Img = ".JPG";

            return View("MainCategories", categories);
        }

        public List<BikeListModel> Category(int id, int displayNum, int page)
        {
            var validProducts = db.VProductAndDescription
                .Where(b => b.Culture == "en" && b.ProductCategoryId == id && b.SellEndDate == null)
                .Select(b => new BikeListModel
                {
                    ProductModel = b.ProductModel,
                    Description = b.Description,
                    ProductModelID = b.ProductModelId
                })
                .Distinct().ToList();

            int startDisplay = displayNum * (page - 1);

            validProducts = validProducts.Skip(startDisplay).Take(displayNum).ToList();

            return validProducts;
        }

        public int GetPageNum(int id, int displayNum)
        {
            int numPages = db.VProductAndDescription
                .Where(b => b.Culture == "en" && b.ProductCategoryId == id && b.SellEndDate == null)
                .Select(b => new BikeListModel
                {
                    ProductModel = b.ProductModel,
                    Description = b.Description,
                    ProductModelID = b.ProductModelId
                })
                .Distinct().ToList().Count();

            decimal val = (decimal)numPages / displayNum;
            int retVal = (int)Math.Ceiling(val);
            return retVal;
        }

        public IActionResult Details(int? id, string sortOrder)
        {
            if (id == null)
            {
                return NotFound();
            }

            ViewData["NameSortParm"] = sortOrder == "name" ? "name_desc" : "name";
            ViewData["NumberSortParm"] = sortOrder == "number" ? "number_desc" : "number";
            ViewData["ColorSortParm"] = sortOrder == "color" ? "color_desc" : "color";
            ViewData["PriceSortParm"] = sortOrder == "price" ? "price_desc" : "price";
            ViewData["SizeSortParm"] = sortOrder == "size" ? "size_desc" : "size";
            ViewData["WeightSortParm"] = sortOrder == "weight" ? "weight_desc" : "weight";

            var productModelList = db.Product.Where(b => b.ProductModelId == id && (b.SellEndDate == null || b.SellEndDate > DateTime.Now));

            switch (sortOrder)
            {
                case "name":
                    productModelList = productModelList.OrderBy(s => s.Name);
                    break;
                case "number":
                    productModelList = productModelList.OrderBy(s => s.ProductNumber);
                    break;
                case "color":
                    productModelList = productModelList.OrderBy(s => s.Color);
                    break;
                case "price":
                    productModelList = productModelList.OrderBy(s => s.ListPrice - s.Discount);
                    break;
                case "size":
                    productModelList = productModelList.OrderBy(s => s.Size);
                    break;
                case "weight":
                    productModelList = productModelList.OrderBy(s => s.Weight);
                    break;
                case "name_desc":
                    productModelList = productModelList.OrderByDescending(s => s.Name);
                    break;
                case "number_desc":
                    productModelList = productModelList.OrderByDescending(s => s.ProductNumber);
                    break;
                case "color_desc":
                    productModelList = productModelList.OrderByDescending(s => s.Color);
                    break;
                case "price_desc":
                    productModelList = productModelList.OrderByDescending(s => s.ListPrice - s.Discount);
                    break;
                case "size_desc":
                    productModelList = productModelList.OrderByDescending(s => s.Size);
                    break;
                case "weight_desc":
                    productModelList = productModelList.OrderByDescending(s => s.Weight);
                    break;
            }

            ViewBag.SortOrder = sortOrder;

            if (productModelList == null)
            {
                return NotFound();
            }

            return View(productModelList.ToList());
        }

        public IActionResult Product(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var product = db.Product.Where(b => b.ProductId == id).ToList().First();

            if (product == null)
            {
                return NotFound();
            }

            if (product.Weight != null)
            {
                ViewBag.Weight = product.Weight;
            }
            else
            {
                ViewBag.Weight = 0;
            }

            if (double.TryParse(product.Size, out double x)){
                ViewBag.isNumSize = product.Size;
            }


            return View(product);
        }

        public IActionResult Compare()
        {
            List<string> checkboxes = Request.Form.Keys.ToList();
            checkboxes.RemoveAt(checkboxes.Count() - 1);

            List<int> productIds = new List<int>();
            foreach (string s in checkboxes)
            {
                productIds.Add(int.Parse(s));
            }
            var products = db.Product.Where(p => productIds.Contains(p.ProductId));


            return View(products);
        }

        // GET: Bikes/Create
        public IActionResult Create()
        {
            ViewData["ParentProductCategoryId"] = new SelectList(db.ProductCategory, "ProductCategoryId", "Name");
            return View();
        }

        // POST: Bikes/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("ProductCategoryId,ParentProductCategoryId,Name")] ProductCategory productCategory)
        {
            if (ModelState.IsValid)
            {
                productCategory.ModifiedDate = DateTime.Now;
                db.Add(productCategory);
                await db.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ParentProductCategoryId"] = new SelectList(db.ProductCategory, "ProductCategoryId", "Name", productCategory.ParentProductCategoryId);
            return View(productCategory);
        }

        // GET: Bikes/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var productCategory = await db.ProductCategory.FindAsync(id);
            if (productCategory == null)
            {
                return NotFound();
            }
            ViewData["ParentProductCategoryId"] = new SelectList(db.ProductCategory, "ProductCategoryId", "Name", productCategory.ParentProductCategoryId);
            return View(productCategory);
        }

        // POST: Bikes/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("ProductCategoryId,ParentProductCategoryId,Name,Rowguid,ModifiedDate")] ProductCategory productCategory)
        {
            if (id != productCategory.ProductCategoryId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    db.Update(productCategory);
                    await db.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ProductCategoryExists(productCategory.ProductCategoryId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            ViewData["ParentProductCategoryId"] = new SelectList(db.ProductCategory, "ProductCategoryId", "Name", productCategory.ParentProductCategoryId);
            return View(productCategory);
        }

        // GET: Bikes/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var productCategory = await db.ProductCategory
                .Include(p => p.ParentProductCategory)
                .FirstOrDefaultAsync(m => m.ProductCategoryId == id);
            if (productCategory == null)
            {
                return NotFound();
            }

            return View(productCategory);
        }

        /*
        // POST: Bikes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var productCategory = await db.ProductCategory.FindAsync(id);
            db.ProductCategory.Remove(productCategory);
            await db.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }*/

        private bool ProductCategoryExists(int id)
        {
            return db.ProductCategory.Any(e => e.ProductCategoryId == id);
        }

        public IActionResult SpecialOffers()
        {
            // Get List of bikes for specified model
            /*
             * SELECT *
             * FROM Saleslt.Product
             * WHERE OnSale = 1 &&
             * (SellEndDate IS NULL OR SellEndDate > CURRENT_TIME)
             */

            var onSaleList = db.Product.Where(b => b.OnSale == true && (b.SellEndDate == null || b.SellEndDate > DateTime.Now)).ToList();

            if (onSaleList == null)
            {
                return NotFound();
            }

            return View(onSaleList);
        }

        public IActionResult Admin(string quantitySearch)
        {
            // Get List of all bikes to show admin
            /*
             * SELECT *
             * FROM Saleslt.Product
             */

            if (!User.Identity.IsAuthenticated || User.Claims.Where(r => r.Type == ROLE).Select(c => c.Value).SingleOrDefault() != ADMIN)
            {
                return NotFound();
            }


            if (!String.IsNullOrEmpty(quantitySearch))
            {
                int qS = Int32.Parse(quantitySearch);
                var products = from s in db.Product
                               where s.Quantity < qS
                               orderby s.Quantity
                               select s;
                return View(products);

            }

            var onSaleList = db.Product.ToList();

            if (onSaleList == null)
            {
                return NotFound();
            }

            return View(onSaleList);
        }

        // GET: Bikes/Create
        public IActionResult CreateProduct()
        {
            ViewData["ProductCategoryId"] = new SelectList(db.ProductCategory, "ProductCategoryId", "Name");
            ViewData["ProductModelId"] = new SelectList(db.ProductModel, "ProductModelId", "Name");
            return View();
        }

        // POST: Bikes/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> CreateProduct([Bind("ProductId,Name,ProductNumber,Color,StandardCost,ListPrice,Size,Weight,ProductCategoryId,ProductModelId,SellStartDate,SellEndDate,DiscontinuedDate,ThumbNailPhoto,ThumbnailPhotoFileName,Rowguid,ModifiedDate,Description,OnSale,Discount")] Product product)
        {
            if (ModelState.IsValid)
            {
                db.Add(product);
                await db.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ProductCategoryId"] = new SelectList(db.ProductCategory, "ProductCategoryId", "Name", product.ProductCategoryId);
            ViewData["ProductModelId"] = new SelectList(db.ProductModel, "ProductModelId", "Name", product.ProductModelId);
            return View(product);
        }

        // GET: Products/EditProduct/id
        public async Task<IActionResult> EditProduct(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var product = await db.Product.FindAsync(id);
            if (product == null)
            {
                return NotFound();
            }
            ViewBag.ProductCategoryId = new SelectList(db.ProductCategory, "ProductCategoryId", "Name", product.ProductCategoryId);
            ViewBag.ProductModelId = new SelectList(db.ProductModel, "ProductModelId", "Name", product.ProductModelId);
            ViewBag.ThumbNailPhoto = Convert.ToBase64String(product.ThumbNailPhoto);
            return View(product);
        }

        // POST: Products1/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> EditProduct(int id, [Bind("ProductId,Name,ProductNumber,Color,StandardCost,ListPrice,Size,Weight,ProductCategoryId,ProductModelId,SellStartDate,SellEndDate,DiscontinuedDate,ThumbNailPhoto,ThumbnailPhotoFileName,Rowguid,ModifiedDate,Description,OnSale,Discount,Quantity")] Product product)
        {
            if (id != product.ProductId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    product.ModifiedDate = DateTime.Now;
                    db.Update(product);
                    await db.SaveChangesAsync();

                    TempData["SuccessMessage"] = "Product Successfully Updated.";
                    return RedirectToAction(nameof(Admin));
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ProductExists(product.ProductId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
            }
            ViewBag.ProductCategoryId = new SelectList(db.ProductCategory, "ProductCategoryId", "Name", product.ProductCategoryId);
            ViewBag.ProductModelId = new SelectList(db.ProductModel, "ProductModelId", "Name", product.ProductModelId);
            ViewBag.ThumbNailPhoto = Convert.ToBase64String(product.ThumbNailPhoto);
            return View(product);
        }

        private bool ProductExists(int id)
        {
            return db.Product.Any(e => e.ProductId == id);
        }

        // GET: Products/Delete/5
        public async Task<IActionResult> DeleteProduct(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var product = await db.Product
                .Include(p => p.ProductCategory)
                .Include(p => p.ProductModel)
                .FirstOrDefaultAsync(m => m.ProductId == id);
            if (product == null)
            {
                return NotFound();
            }

            return View(product);
        }

        // POST: Products/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var product = await db.Product.FindAsync(id);
            db.Product.Remove(product);
            await db.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }


    }
}
