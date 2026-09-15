using Microsoft.AspNetCore.Mvc.RazorPages;
using Buoi1.Models;

namespace Buoi1.Pages
{
    public class IndexModel : PageModel
    {
        public Article FeaturedArticle { get; set; } = new();
        public List<Article> SubArticles { get; set; } = new();
        public Article OpinionArticle { get; set; } = new();

        public void OnGet()
        {
            FeaturedArticle = new Article
            {
                Title = "'Ưu tiên tái định cư trước khi triển khai Trục cảnh quan sông Hồng'",
                Summary = "Hà Nội sẽ ưu tiên xây dựng các khu tái định cư trước khi triển khai Trục Đại lộ cảnh quan sông Hồng - dự án hơn 11.400 ha, liên quan khoảng 247.000 dân ven sông, thực hiện giai đoạn 2026-2038.",
                ImageUrl = "https://picsum.photos/700/400?random=1",
                TimeAgo = "1h trước"
            };

            SubArticles = new List<Article>
            {
                new Article
                {
                    Title = "Hà Nội giải phóng mặt bằng cho hơn 1.400 dự án",
                    ImageUrl = "https://picsum.photos/400/250?random=2"
                },
                new Article
                {
                    Title = "TP HCM siết kiểm soát người nhập cảnh phòng Ebola",
                    ImageUrl = "https://picsum.photos/400/250?random=3"
                }
            };

            OpinionArticle = new Article
            {
                Title = "'Bóc lột' trên YouTube",
                Summary = "Khi cỗ máy Content ID của YouTube bị trục lợi, nhiều nghệ sĩ...",
                Author = "Hoàng Hà",
                CommentCount = 24
            };
        }
    }
}