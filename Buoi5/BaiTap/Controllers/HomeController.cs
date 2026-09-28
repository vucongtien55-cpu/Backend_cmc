using System.Diagnostics;
using BaiTap.Models;
using Microsoft.AspNetCore.Mvc;

namespace YourProjectName.Controllers
{
    public class HomeController : Controller
    {
        // 1. Action cho Câu 1
        public IActionResult CheckWeekend()
        {
            return View(); // Trả về Views/Home/CheckWeekend.cshtml
        }

        // 2. Action cho Câu 2
        public IActionResult ProductList()
        {
            return View(); // Trả về Views/Home/ProductList.cshtml
        }
    }
}
