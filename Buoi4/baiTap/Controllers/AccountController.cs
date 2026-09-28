using baiTap.Models;
using Microsoft.AspNetCore.Mvc;

namespace baiTap.Controllers
{
    public class AccountController : Controller
    {
        // GET: /Account/Login (Hiển thị Form)
        [HttpGet]
        public IActionResult Login()
        {
            return View();
        }

        // POST: /Account/Login (Xử lý Submit)
        [HttpPost]
        public IActionResult Login(LoginViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            // Kiểm tra điều kiện đăng nhập
            if (model.Username == "admin" && model.Password == "123")
            {
                ViewBag.Message = "Login success";
                ViewBag.IsSuccess = true;
            }
            else
            {
                ViewBag.Message = "Login failed";
                ViewBag.IsSuccess = false;
            }

            return View(model);
        }
    }
}
