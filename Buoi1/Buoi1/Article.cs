namespace Buoi1.Models
{
    public class Article
    {
        public int Id { get; set; }
        public string Title { get; set; } = string.Empty;
        public string Summary { get; set; } = string.Empty;
        public string ImageUrl { get; set; } = string.Empty;
        public string TimeAgo { get; set; } = string.Empty;
        public string? Author { get; set; }
        public int CommentCount { get; set; }
    }
}