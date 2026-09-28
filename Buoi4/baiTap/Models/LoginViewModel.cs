using System.ComponentModel.DataAnnotations;

namespace baiTap.Models
{
    public class LoginViewModel
    {
        [Required(ErrorMessage = "Username không được để trống")]
        public string Username { get; set; } = string.Empty;

        [Required(ErrorMessage = "Password không được để trống")]
        [DataType(DataType.Password)]
        public string Password { get; set; } = string.Empty;
    }
}
