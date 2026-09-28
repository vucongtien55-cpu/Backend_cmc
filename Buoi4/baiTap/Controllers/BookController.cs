using Microsoft.AspNetCore.Mvc;
using baiTap.Models;

namespace baiTap.Controllers
{
    public class BookController : Controller
    {
        // Khởi tạo danh sách sách giả lập trong bộ nhớ (đúng theo yêu cầu đề bài)
        private static List<Book> _books = new List<Book>
        {
            new Book { Id = 1, Name = "Clean Code", Price = 20 },
            new Book { Id = 2, Name = "ASP.NET MVC", Price = 15 },
            new Book { Id = 3, Name = "Design Pattern", Price = 25 }
        };

        // Chức năng 1: Hiển thị danh sách sách (/Book hoặc /Book/Index)
        [HttpGet]
        public IActionResult Index()
        {
            return View(_books);
        }

        // Chức năng 2: Hiển thị chi tiết sách theo Id (/Book/Detail/1)
        [HttpGet]
        public IActionResult Detail(int id)
        {
            var book = _books.FirstOrDefault(b => b.Id == id);
            if (book == null)
            {
                return NotFound("Không tìm thấy sách có Id này!");
            }
            return View(book);
        }

        // Chức năng 3: Form thêm sách (GET)
        [HttpGet]
        public IActionResult Create()
        {
            return View();
        }

        // Chức năng 3: Xử lý thêm sách (POST)
        [HttpPost]
        public IActionResult Create(Book book)
        {
            // Tự động tăng Id
            book.Id = _books.Max(b => b.Id) + 1;
            _books.Add(book);

            // Thông báo thêm thành công[cite: 4]
            TempData["SuccessMessage"] = "Thêm sách thành công!";
            return RedirectToAction("Index");
        }
    }
}