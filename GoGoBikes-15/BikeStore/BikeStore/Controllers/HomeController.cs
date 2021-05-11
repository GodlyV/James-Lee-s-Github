using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using BikeStore.Models;
using System.Security.Cryptography;
using System.Net.Mail;
using System.Net.Http;
using System.Net;
using Newtonsoft.Json.Linq;
using System.Text;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication;
using System.Text.RegularExpressions;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;
using BikeStore.Views.Home;
using Microsoft.AspNet.Identity;
//https://www.codeproject.com/Tips/1081578/How-to-Implement-Contact-Us-Page-in-ASP-NET-MVC-AS contact us?

namespace BikeStore.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;
        private readonly string COMPANY_EMAIL = "gogobikesstore@gmail.com";
        private readonly string COMPANY_PASSWORD = "2MV$WX&+yt&K&y#X";
        private readonly string TIME_FORMAT = "MMMM dd, yyyy hh:mm:ss tt";
        private readonly string WEBSITE_NAME = "gogobikesstore.azurewebsites.net";
        private readonly AdventureWorksLT2017gr15Context db;
        private readonly int MAX = 4;   // max that will be shown on the product carousel for onsale items in Home -> Index

        public HomeController(ILogger<HomeController> logger, AdventureWorksLT2017gr15Context context = null)
        {
            _logger = logger;
            db = context;
        }

        public IActionResult Index()
        {
            

            var onSaleList = db.Product.Where(b => b.OnSale == true && (b.SellEndDate == null || b.SellEndDate > DateTime.Now)).Take(MAX);

            if (onSaleList == null)
            {
                return View();
            }
            return View(onSaleList);            
        }

        public IActionResult RecoverPassword()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> RecoverPassword([Bind("EmailAddress")] Customer customer)
        {
            if (new Regex(@"^\w+[\w-\.]*\@\w+((-\w+)|(\w*))\.[a-z]{2,3}$").Match(customer.EmailAddress).Success)
            {
                if (!ReCaptchaPassed(Request.Form["endForm"]))
                {
                    ModelState.AddModelError(string.Empty, "You failed the CAPTCHA.");
                    ViewBag.ErrorMessage = "You failed the CAPTCHA. Please try again later.";
                    return View();
                }
                
                var validEmail = db.Customer.Where(c => c.EmailAddress == customer.EmailAddress).FirstOrDefault();

                // Valid Email? Send email
                if (validEmail != null)
                {
                    // Generate Password Recovery Token
                    Guid g = Guid.NewGuid();
                    string gString = g.ToString();

                    // Set Session Token
                    HttpContext.Session.Set("PasswordRecoveryToken", g.ToByteArray());

                    // Code = Token

                    string callbackUrl = "/Home/ResetPassword?" + nameof(gString) + "=" + gString;

                    try
                    {
                        MailMessage msz = new MailMessage();

                        // Person Sending the Email (still us)
                        msz.From = new MailAddress(COMPANY_EMAIL);

                        // Where we are sending the email
                        msz.To.Add(customer.EmailAddress);
                        msz.Subject = "GoGoBikes Account Password Recovery!";
                        msz.Body = String.Format("Hello {0} {1}\n\nWe've just received a request to change passwords for your account: {2}\n\nPlease click on the following link to reset your password: "+ WEBSITE_NAME + callbackUrl, validEmail.FirstName, validEmail.LastName, validEmail.EmailAddress);
                       
                        SmtpClient smtp = new SmtpClient();
                        smtp.Host = "smtp.gmail.com";
                        smtp.Port = 587;
                        smtp.Credentials = new System.Net.NetworkCredential
                        (COMPANY_EMAIL, COMPANY_PASSWORD);
                        smtp.EnableSsl = true;
                        smtp.Send(msz);
                    }
                    catch (Exception ex)
                    {
                        ModelState.Clear();
                        ViewBag.ErrorMessage = $" Sorry we are facing Problem here {ex.Message}";
                    }
                }

                // Always send this for better security
                TempData["RecoverMessage"] = "For your security, we need to authenticate your request. You will receive an email if the specified email is linked to an account.";
                return RedirectToAction("RecoverPassword", "Home");
            }
            return View();
        }

        public ActionResult ResetPassword()
        {
            return View();
        }

        [HttpGet]
        public ActionResult ResetPassword(String gString)
        {
            // Get Session Token
            byte[] guid = new byte[100];
            if (HttpContext.Session.TryGetValue("PasswordRecoveryToken", out guid))
            {
                // Compare to provided token // TODO not matching strings or byte arrays
                if (guid.Equals(Encoding.ASCII.GetBytes(gString)))
                {
                    return RedirectToAction("Index", "Home");
                    // Success
                    // Return new ui -> set db
                }
            }

            // Fail
            return NotFound();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> ResetPassword([Bind("Email, Password, ConfirmPassword")] ResetPasswordViewModel info)
        {
            if (User.Claims.Where(c => c.Type == "Role").Select(c => c.Value).First() != "Admin")
            {
                return NotFound();
            }
            return View();
        }


        public IActionResult Login()
        {

            
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Login([Bind("Id,Email,Password")] Login login)
        {
            if (ModelState.IsValid)
            {
                if (!ReCaptchaPassed(Request.Form["endForm"]))
                {
                    ModelState.AddModelError(string.Empty, "You failed the CAPTCHA.");
                    ViewBag.Error = "You failed the CAPTCHA. Please try again later.";
                    return View();
                }

                // Get Account with Same email
                var account = db.Customer.Where(s => s.EmailAddress == login.Email).FirstOrDefault();

                // Valid Email
                if (account != null)
                {
                    string salt = account.PasswordSalt;

                    // Compare Hashed Passwords
                    byte[] passwordAndSaltBytes = System.Text.Encoding.UTF8.GetBytes(login.Password + salt);
                    byte[] hashBytes = new SHA256Managed().ComputeHash(passwordAndSaltBytes);
                    string hashString = Convert.ToBase64String(hashBytes);

                    // Correct Password
                    if (hashString == account.PasswordHash)
                    {
                        TempData["Login"] = "yes";
                        db.Login.Add(login);
                        await db.SaveChangesAsync();

                        // Sessions
                        //https://docs.microsoft.com/en-us/aspnet/core/security/authentication/cookie?view=aspnetcore-3.1

                        var claims = new List<Claim>
                        {
                            new Claim("Email", login.Email),
                            new Claim("Name", account.FirstName + " " + account.LastName),
                            new Claim("Role", account.Privileges),
                        };

                        var claimsIdentity = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);

                        var authProperties = new AuthenticationProperties { };

                        await HttpContext.SignInAsync(
                            CookieAuthenticationDefaults.AuthenticationScheme,
                            new ClaimsPrincipal(claimsIdentity),
                            authProperties);

                        return RedirectToAction("Index");
                    }
                }
            }
            

            TempData["Error"] = "Invalid login or password";
            return RedirectToAction("SignIn", "Authentication");
        }

        public async Task<IActionResult> LogoutAsync()
        {
            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);

            return RedirectToAction("Index");
        }
        public async Task<IActionResult> AddLoginEntry()
        {
            var email = User.FindFirstValue(ClaimTypes.Email);
            TempData["Login"] = "yes";
            Login x = new Login();
            x.Email = email;
            db.Login.Add(x);
            await db.SaveChangesAsync();


            return RedirectToAction("Index", "Home");
        }

        public IActionResult Register()
        {
            return View();
        }

        // POST: Accounts/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Register([Bind("CustomerId,FirstName,LastName,EmailAddress,Password,ConfirmPassword,Phone,ModifiedDate")] Customer account)
        {
            if (ModelState.IsValid)
            {
                if (!ReCaptchaPassed(Request.Form["endForm"]))
                {
                    ModelState.AddModelError(string.Empty, "You failed the CAPTCHA.");
                    ViewBag.ErrorMessage = "You failed the CAPTCHA. Please try again later.";
                    return View();
                }

                // Account Taken?
                var data = db.Customer.Where(x => x.EmailAddress == account.EmailAddress);
                if (data.Count() > 0)
                {
                    ViewBag.ErrorMessage = "Email already linked to an account!";
                    ViewBag.ErrorHint = "Please try to create an account with a different email or login with the tried email.";
                    return View();
                }
                RNGCryptoServiceProvider rng = new RNGCryptoServiceProvider();
                byte[] saltBytes = new byte[6];
                rng.GetBytes(saltBytes);
                string salt = Convert.ToBase64String(saltBytes);

                byte[] passwordAndSaltBytes = System.Text.Encoding.UTF8.GetBytes(account.Password + salt);
                byte[] hashBytes = new SHA256Managed().ComputeHash(passwordAndSaltBytes);
                string hashString = Convert.ToBase64String(hashBytes);

                account.Rowguid = Guid.NewGuid();
                account.PasswordHash = hashString;
                account.PasswordSalt = salt;
                account.ModifiedDate = DateTime.Now;
                account.Privileges = Customer.PrivilegeType.User.ToString();
                db.Customer.Add(account);
                await db.SaveChangesAsync();
                TempData["Registered"] = "yes";
                return RedirectToAction(nameof(Index));

            }

            return View();
        }
        public IActionResult Success()
        {
            return View();
        }

        public IActionResult Privacy()
        {
            return View();
        }
        public IActionResult Returns()
        {
            return View();
        }

        public IActionResult Safety()
        {
            return View();
        }

        [HttpGet]
        public IActionResult ContactUs()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> ContactUs(ContactUs vm)
        {
            if (ModelState.IsValid)
            {
                if (!ReCaptchaPassed(Request.Form["endForm"]))
                {
                    ModelState.AddModelError(string.Empty, "You failed the CAPTCHA.");
                    ViewBag.Message = "You failed the CAPTCHA. Please try again later.";
                    return View();
                }

                try
                {
                    vm.SubmitionTime = DateTime.Now;
                    MailMessage msz = new MailMessage();

                    // Person Sending the Email (still us)
                    msz.From = new MailAddress(COMPANY_EMAIL);

                    // Where we are sending the email
                    msz.To.Add(COMPANY_EMAIL);
                    msz.Subject = vm.Subject + " on " + vm.SubmitionTime.ToString(TIME_FORMAT);
                    msz.CC.Add(vm.Email);
                    string body = String.Format("First name: {0}\nLast name:  {1}\nPostal Code:  {2}\nEmail: {3}\n", vm.FirstName, vm.LastName, vm.PostalCode, vm.Email);
                    if (vm.Phone != "" && vm.Phone != null)
                    {
                        body += "Phone Number:  " + vm.Phone + "\n";
                    }
                    body += "Comments:  " + vm.QuestionsAndComments;
                    msz.Body = body;

                    SmtpClient smtp = new SmtpClient();

                    smtp.Host = "smtp.gmail.com";

                    smtp.Port = 587;

                    smtp.Credentials = new System.Net.NetworkCredential
                    (COMPANY_EMAIL, COMPANY_PASSWORD);

                    smtp.EnableSsl = true;

                    smtp.Send(msz);

                    ModelState.Clear();
                    ViewBag.Message = "Thank you for Contacting us ";

                    db.ContactUs.Add(vm);
                    await db.SaveChangesAsync();
                }
                catch (Exception ex)
                {
                    ModelState.Clear();
                    ViewBag.Message = $" Sorry we are facing Problem here {ex.Message}";
                }
            }
            return View();
        }

        public static bool ReCaptchaPassed(string gRecaptchaResponse)
        {
            HttpClient httpClient = new HttpClient();
            var res = httpClient.GetAsync($"https://www.google.com/recaptcha/api/siteverify?secret=6LcLqMEUAAAAAHK69HYHpuM9L6rb5-AumYLGwTw8&response={gRecaptchaResponse}").Result;
            if (res.StatusCode != HttpStatusCode.OK)
                return false;

            string JSONres = res.Content.ReadAsStringAsync().Result;
            dynamic JSONdata = JObject.Parse(JSONres);
            if (JSONdata.success != "true")
                return false;

            return true;
        }

        public IActionResult AboutUs()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
