using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace BikeStore.Migrations
{
    public partial class ContactUs : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.EnsureSchema(
                name: "SalesLT");

            migrationBuilder.CreateTable(
                name: "BuildVersion",
                columns: table => new
                {
                    SystemInformationID = table.Column<byte>(nullable: false, comment: "Current version number of the AdventureWorksLT 2012 sample database. ")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    DatabaseVersion = table.Column<string>(name: "Database Version", maxLength: 25, nullable: false, comment: "Current version number of the AdventureWorksLT 2012 sample database. "),
                    VersionDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Current version number of the AdventureWorksLT 2012 sample database. "),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Current version number of the AdventureWorksLT 2012 sample database. ")
                },
                constraints: table =>
                {
                },
                comment: "Current version number of the AdventureWorksLT 2012 sample database. ");

            migrationBuilder.CreateTable(
                name: "ErrorLog",
                columns: table => new
                {
                    ErrorLogID = table.Column<int>(nullable: false, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    ErrorTime = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct."),
                    UserName = table.Column<string>(maxLength: 128, nullable: false, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct."),
                    ErrorNumber = table.Column<int>(nullable: false, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct."),
                    ErrorSeverity = table.Column<int>(nullable: true, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct."),
                    ErrorState = table.Column<int>(nullable: true, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct."),
                    ErrorProcedure = table.Column<string>(maxLength: 126, nullable: true, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct."),
                    ErrorLine = table.Column<int>(nullable: true, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct."),
                    ErrorMessage = table.Column<string>(maxLength: 4000, nullable: false, comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct.")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ErrorLog", x => x.ErrorLogID);
                },
                comment: "Audit table tracking errors in the the AdventureWorks database that are caught by the CATCH block of a TRY...CATCH construct. Data is inserted by stored procedure dbo.uspLogError when it is executed from inside the CATCH block of a TRY...CATCH construct.");

            migrationBuilder.CreateTable(
                name: "Review",
                columns: table => new
                {
                    ReviewId = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    CustomerId = table.Column<int>(nullable: false),
                    Comment = table.Column<string>(nullable: false),
                    Rating = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Review", x => x.ReviewId);
                });

            migrationBuilder.CreateTable(
                name: "Address",
                schema: "SalesLT",
                columns: table => new
                {
                    AddressID = table.Column<int>(nullable: false, comment: "Street address information for customers.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    AddressLine1 = table.Column<string>(maxLength: 60, nullable: false, comment: "Street address information for customers."),
                    AddressLine2 = table.Column<string>(maxLength: 60, nullable: true, comment: "Street address information for customers."),
                    City = table.Column<string>(maxLength: 30, nullable: false, comment: "Street address information for customers."),
                    StateProvince = table.Column<string>(maxLength: 50, nullable: false, comment: "Street address information for customers."),
                    CountryRegion = table.Column<string>(maxLength: 50, nullable: false),
                    PostalCode = table.Column<string>(maxLength: 15, nullable: false, comment: "Street address information for customers."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "Street address information for customers."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Street address information for customers.")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Address", x => x.AddressID);
                },
                comment: "Street address information for customers.");

            migrationBuilder.CreateTable(
                name: "Customer",
                schema: "SalesLT",
                columns: table => new
                {
                    CustomerID = table.Column<int>(nullable: false, comment: "Customer information.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    NameStyle = table.Column<bool>(nullable: false, comment: "Customer information."),
                    Title = table.Column<string>(maxLength: 8, nullable: true, comment: "Customer information."),
                    FirstName = table.Column<string>(maxLength: 50, nullable: false, comment: "Customer information."),
                    MiddleName = table.Column<string>(maxLength: 50, nullable: true, comment: "Customer information."),
                    LastName = table.Column<string>(maxLength: 50, nullable: false, comment: "Customer information."),
                    Suffix = table.Column<string>(maxLength: 10, nullable: true, comment: "Customer information."),
                    CompanyName = table.Column<string>(maxLength: 128, nullable: true, comment: "Customer information."),
                    SalesPerson = table.Column<string>(maxLength: 256, nullable: true, comment: "Customer information."),
                    EmailAddress = table.Column<string>(maxLength: 50, nullable: false, comment: "Customer information."),
                    Phone = table.Column<string>(maxLength: 25, nullable: true, comment: "Customer information."),
                    PasswordHash = table.Column<string>(unicode: false, maxLength: 128, nullable: false, comment: "Customer information."),
                    PasswordSalt = table.Column<string>(unicode: false, maxLength: 10, nullable: false, comment: "Customer information."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "Customer information."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Customer information."),
                    Privileges = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Customer", x => x.CustomerID);
                },
                comment: "Customer information.");

            migrationBuilder.CreateTable(
                name: "ProductCategory",
                schema: "SalesLT",
                columns: table => new
                {
                    ProductCategoryID = table.Column<int>(nullable: false, comment: "High-level product categorization.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    ParentProductCategoryID = table.Column<int>(nullable: true, comment: "High-level product categorization."),
                    Name = table.Column<string>(maxLength: 50, nullable: false, comment: "High-level product categorization."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "High-level product categorization."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "High-level product categorization."),
                    Description = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ProductCategory", x => x.ProductCategoryID);
                    table.ForeignKey(
                        name: "FK_ProductCategory_ProductCategory_ParentProductCategoryID_ProductCategoryID",
                        column: x => x.ParentProductCategoryID,
                        principalSchema: "SalesLT",
                        principalTable: "ProductCategory",
                        principalColumn: "ProductCategoryID",
                        onDelete: ReferentialAction.Restrict);
                },
                comment: "High-level product categorization.");

            migrationBuilder.CreateTable(
                name: "ProductDescription",
                schema: "SalesLT",
                columns: table => new
                {
                    ProductDescriptionID = table.Column<int>(nullable: false, comment: "Product descriptions in several languages.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Description = table.Column<string>(maxLength: 400, nullable: false, comment: "Product descriptions in several languages."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "Product descriptions in several languages."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Product descriptions in several languages.")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ProductDescription", x => x.ProductDescriptionID);
                },
                comment: "Product descriptions in several languages.");

            migrationBuilder.CreateTable(
                name: "ProductModel",
                schema: "SalesLT",
                columns: table => new
                {
                    ProductModelID = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(maxLength: 50, nullable: false),
                    CatalogDescription = table.Column<string>(type: "xml", nullable: true),
                    rowguid = table.Column<Guid>(nullable: false),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ProductModel", x => x.ProductModelID);
                });

            migrationBuilder.CreateTable(
                name: "CustomerAddress",
                schema: "SalesLT",
                columns: table => new
                {
                    CustomerID = table.Column<int>(nullable: false, comment: "Cross-reference table mapping customers to their address(es)."),
                    AddressID = table.Column<int>(nullable: false, comment: "Cross-reference table mapping customers to their address(es)."),
                    AddressType = table.Column<string>(maxLength: 50, nullable: false, comment: "Cross-reference table mapping customers to their address(es)."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "Cross-reference table mapping customers to their address(es)."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Cross-reference table mapping customers to their address(es).")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_CustomerAddress_CustomerID_AddressID", x => new { x.CustomerID, x.AddressID });
                    table.ForeignKey(
                        name: "FK_CustomerAddress_Address_AddressID",
                        column: x => x.AddressID,
                        principalSchema: "SalesLT",
                        principalTable: "Address",
                        principalColumn: "AddressID",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_CustomerAddress_Customer_CustomerID",
                        column: x => x.CustomerID,
                        principalSchema: "SalesLT",
                        principalTable: "Customer",
                        principalColumn: "CustomerID",
                        onDelete: ReferentialAction.Restrict);
                },
                comment: "Cross-reference table mapping customers to their address(es).");

            migrationBuilder.CreateTable(
                name: "SalesOrderHeader",
                schema: "SalesLT",
                columns: table => new
                {
                    SalesOrderID = table.Column<int>(nullable: false, comment: "General sales order information.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    RevisionNumber = table.Column<byte>(nullable: false, comment: "General sales order information."),
                    OrderDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "General sales order information."),
                    DueDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "General sales order information."),
                    ShipDate = table.Column<DateTime>(type: "datetime", nullable: true, comment: "General sales order information."),
                    Status = table.Column<byte>(nullable: false, comment: "General sales order information."),
                    OnlineOrderFlag = table.Column<bool>(nullable: false, comment: "General sales order information."),
                    SalesOrderNumber = table.Column<string>(maxLength: 25, nullable: false, comment: "General sales order information."),
                    PurchaseOrderNumber = table.Column<string>(maxLength: 25, nullable: true, comment: "General sales order information."),
                    AccountNumber = table.Column<string>(maxLength: 15, nullable: true, comment: "General sales order information."),
                    CustomerID = table.Column<int>(nullable: false, comment: "General sales order information."),
                    ShipToAddressID = table.Column<int>(nullable: true, comment: "General sales order information."),
                    BillToAddressID = table.Column<int>(nullable: true, comment: "General sales order information."),
                    ShipMethod = table.Column<string>(maxLength: 50, nullable: false, comment: "General sales order information."),
                    CreditCardApprovalCode = table.Column<string>(unicode: false, maxLength: 15, nullable: true, comment: "General sales order information."),
                    SubTotal = table.Column<decimal>(type: "money", nullable: false, comment: "General sales order information."),
                    TaxAmt = table.Column<decimal>(type: "money", nullable: false, comment: "General sales order information."),
                    Freight = table.Column<decimal>(type: "money", nullable: false, comment: "General sales order information."),
                    TotalDue = table.Column<decimal>(type: "money", nullable: false, comment: "General sales order information."),
                    Comment = table.Column<string>(nullable: true, comment: "General sales order information."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "General sales order information."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "General sales order information.")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_SalesOrderHeader_SalesOrderID", x => x.SalesOrderID);
                    table.ForeignKey(
                        name: "FK_SalesOrderHeader_Address_BillTo_AddressID",
                        column: x => x.BillToAddressID,
                        principalSchema: "SalesLT",
                        principalTable: "Address",
                        principalColumn: "AddressID",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_SalesOrderHeader_Customer_CustomerID",
                        column: x => x.CustomerID,
                        principalSchema: "SalesLT",
                        principalTable: "Customer",
                        principalColumn: "CustomerID",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_SalesOrderHeader_Address_ShipTo_AddressID",
                        column: x => x.ShipToAddressID,
                        principalSchema: "SalesLT",
                        principalTable: "Address",
                        principalColumn: "AddressID",
                        onDelete: ReferentialAction.Restrict);
                },
                comment: "General sales order information.");

            migrationBuilder.CreateTable(
                name: "Product",
                schema: "SalesLT",
                columns: table => new
                {
                    ProductID = table.Column<int>(nullable: false, comment: "Products sold or used in the manfacturing of sold products.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(maxLength: 50, nullable: false, comment: "Products sold or used in the manfacturing of sold products."),
                    ProductNumber = table.Column<string>(maxLength: 25, nullable: false, comment: "Products sold or used in the manfacturing of sold products."),
                    Color = table.Column<string>(maxLength: 15, nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    StandardCost = table.Column<decimal>(type: "money", nullable: false, comment: "Products sold or used in the manfacturing of sold products."),
                    ListPrice = table.Column<decimal>(type: "money", nullable: false, comment: "Products sold or used in the manfacturing of sold products."),
                    Size = table.Column<string>(maxLength: 5, nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    Weight = table.Column<decimal>(type: "decimal(8, 2)", nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    ProductCategoryID = table.Column<int>(nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    ProductModelID = table.Column<int>(nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    SellStartDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Products sold or used in the manfacturing of sold products."),
                    SellEndDate = table.Column<DateTime>(type: "datetime", nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    DiscontinuedDate = table.Column<DateTime>(type: "datetime", nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    ThumbNailPhoto = table.Column<byte[]>(nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    ThumbnailPhotoFileName = table.Column<string>(maxLength: 50, nullable: true, comment: "Products sold or used in the manfacturing of sold products."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "Products sold or used in the manfacturing of sold products."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Products sold or used in the manfacturing of sold products."),
                    Description = table.Column<string>(nullable: true),
                    OnSale = table.Column<bool>(nullable: false),
                    Discount = table.Column<decimal>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Product", x => x.ProductID);
                    table.ForeignKey(
                        name: "FK_Product_ProductCategory_ProductCategoryID",
                        column: x => x.ProductCategoryID,
                        principalSchema: "SalesLT",
                        principalTable: "ProductCategory",
                        principalColumn: "ProductCategoryID",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_Product_ProductModel_ProductModelID",
                        column: x => x.ProductModelID,
                        principalSchema: "SalesLT",
                        principalTable: "ProductModel",
                        principalColumn: "ProductModelID",
                        onDelete: ReferentialAction.Restrict);
                },
                comment: "Products sold or used in the manfacturing of sold products.");

            migrationBuilder.CreateTable(
                name: "ProductModelProductDescription",
                schema: "SalesLT",
                columns: table => new
                {
                    ProductModelID = table.Column<int>(nullable: false, comment: "Cross-reference table mapping product descriptions and the language the description is written in."),
                    ProductDescriptionID = table.Column<int>(nullable: false, comment: "Cross-reference table mapping product descriptions and the language the description is written in."),
                    Culture = table.Column<string>(fixedLength: true, maxLength: 6, nullable: false, comment: "Cross-reference table mapping product descriptions and the language the description is written in."),
                    rowguid = table.Column<Guid>(nullable: false),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Cross-reference table mapping product descriptions and the language the description is written in.")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ProductModelProductDescription_ProductModelID_ProductDescriptionID_Culture", x => new { x.ProductModelID, x.ProductDescriptionID, x.Culture });
                    table.ForeignKey(
                        name: "FK_ProductModelProductDescription_ProductDescription_ProductDescriptionID",
                        column: x => x.ProductDescriptionID,
                        principalSchema: "SalesLT",
                        principalTable: "ProductDescription",
                        principalColumn: "ProductDescriptionID",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_ProductModelProductDescription_ProductModel_ProductModelID",
                        column: x => x.ProductModelID,
                        principalSchema: "SalesLT",
                        principalTable: "ProductModel",
                        principalColumn: "ProductModelID",
                        onDelete: ReferentialAction.Restrict);
                },
                comment: "Cross-reference table mapping product descriptions and the language the description is written in.");

            migrationBuilder.CreateTable(
                name: "SalesOrderDetail",
                schema: "SalesLT",
                columns: table => new
                {
                    SalesOrderID = table.Column<int>(nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader."),
                    SalesOrderDetailID = table.Column<int>(nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader.")
                        .Annotation("SqlServer:Identity", "1, 1"),
                    OrderQty = table.Column<short>(nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader."),
                    ProductID = table.Column<int>(nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader."),
                    UnitPrice = table.Column<decimal>(type: "money", nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader."),
                    UnitPriceDiscount = table.Column<decimal>(type: "money", nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader."),
                    LineTotal = table.Column<decimal>(type: "numeric(38, 6)", nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader."),
                    rowguid = table.Column<Guid>(nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader."),
                    ModifiedDate = table.Column<DateTime>(type: "datetime", nullable: false, comment: "Individual products associated with a specific sales order. See SalesOrderHeader.")
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_SalesOrderDetail_SalesOrderID_SalesOrderDetailID", x => new { x.SalesOrderID, x.SalesOrderDetailID });
                    table.ForeignKey(
                        name: "FK_SalesOrderDetail_Product_ProductID",
                        column: x => x.ProductID,
                        principalSchema: "SalesLT",
                        principalTable: "Product",
                        principalColumn: "ProductID",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_SalesOrderDetail_SalesOrderHeader_SalesOrderID",
                        column: x => x.SalesOrderID,
                        principalSchema: "SalesLT",
                        principalTable: "SalesOrderHeader",
                        principalColumn: "SalesOrderID",
                        onDelete: ReferentialAction.Cascade);
                },
                comment: "Individual products associated with a specific sales order. See SalesOrderHeader.");

            migrationBuilder.CreateIndex(
                name: "AK_Address_rowguid",
                schema: "SalesLT",
                table: "Address",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Address_StateProvince",
                schema: "SalesLT",
                table: "Address",
                column: "StateProvince");

            migrationBuilder.CreateIndex(
                name: "IX_Address_AddressLine1_AddressLine2_City_StateProvince_PostalCode_CountryRegion",
                schema: "SalesLT",
                table: "Address",
                columns: new[] { "AddressLine1", "AddressLine2", "City", "StateProvince", "PostalCode", "CountryRegion" });

            migrationBuilder.CreateIndex(
                name: "IX_Customer_EmailAddress",
                schema: "SalesLT",
                table: "Customer",
                column: "EmailAddress");

            migrationBuilder.CreateIndex(
                name: "AK_Customer_rowguid",
                schema: "SalesLT",
                table: "Customer",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_CustomerAddress_AddressID",
                schema: "SalesLT",
                table: "CustomerAddress",
                column: "AddressID");

            migrationBuilder.CreateIndex(
                name: "AK_CustomerAddress_rowguid",
                schema: "SalesLT",
                table: "CustomerAddress",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "AK_Product_Name",
                schema: "SalesLT",
                table: "Product",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Product_ProductCategoryID",
                schema: "SalesLT",
                table: "Product",
                column: "ProductCategoryID");

            migrationBuilder.CreateIndex(
                name: "IX_Product_ProductModelID",
                schema: "SalesLT",
                table: "Product",
                column: "ProductModelID");

            migrationBuilder.CreateIndex(
                name: "AK_Product_ProductNumber",
                schema: "SalesLT",
                table: "Product",
                column: "ProductNumber",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "AK_Product_rowguid",
                schema: "SalesLT",
                table: "Product",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "AK_ProductCategory_Name",
                schema: "SalesLT",
                table: "ProductCategory",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_ProductCategory_ParentProductCategoryID",
                schema: "SalesLT",
                table: "ProductCategory",
                column: "ParentProductCategoryID");

            migrationBuilder.CreateIndex(
                name: "AK_ProductCategory_rowguid",
                schema: "SalesLT",
                table: "ProductCategory",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "AK_ProductDescription_rowguid",
                schema: "SalesLT",
                table: "ProductDescription",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "PXML_ProductModel_CatalogDescription",
                schema: "SalesLT",
                table: "ProductModel",
                column: "CatalogDescription");

            migrationBuilder.CreateIndex(
                name: "AK_ProductModel_Name",
                schema: "SalesLT",
                table: "ProductModel",
                column: "Name",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "AK_ProductModel_rowguid",
                schema: "SalesLT",
                table: "ProductModel",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_ProductModelProductDescription_ProductDescriptionID",
                schema: "SalesLT",
                table: "ProductModelProductDescription",
                column: "ProductDescriptionID");

            migrationBuilder.CreateIndex(
                name: "AK_ProductModelProductDescription_rowguid",
                schema: "SalesLT",
                table: "ProductModelProductDescription",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_SalesOrderDetail_ProductID",
                schema: "SalesLT",
                table: "SalesOrderDetail",
                column: "ProductID");

            migrationBuilder.CreateIndex(
                name: "AK_SalesOrderDetail_rowguid",
                schema: "SalesLT",
                table: "SalesOrderDetail",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_SalesOrderHeader_BillToAddressID",
                schema: "SalesLT",
                table: "SalesOrderHeader",
                column: "BillToAddressID");

            migrationBuilder.CreateIndex(
                name: "IX_SalesOrderHeader_CustomerID",
                schema: "SalesLT",
                table: "SalesOrderHeader",
                column: "CustomerID");

            migrationBuilder.CreateIndex(
                name: "AK_SalesOrderHeader_rowguid",
                schema: "SalesLT",
                table: "SalesOrderHeader",
                column: "rowguid",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "AK_SalesOrderHeader_SalesOrderNumber",
                schema: "SalesLT",
                table: "SalesOrderHeader",
                column: "SalesOrderNumber",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_SalesOrderHeader_ShipToAddressID",
                schema: "SalesLT",
                table: "SalesOrderHeader",
                column: "ShipToAddressID");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "BuildVersion");

            migrationBuilder.DropTable(
                name: "ErrorLog");

            migrationBuilder.DropTable(
                name: "Review");

            migrationBuilder.DropTable(
                name: "CustomerAddress",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "ProductModelProductDescription",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "SalesOrderDetail",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "ProductDescription",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "Product",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "SalesOrderHeader",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "ProductCategory",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "ProductModel",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "Address",
                schema: "SalesLT");

            migrationBuilder.DropTable(
                name: "Customer",
                schema: "SalesLT");
        }
    }
}
