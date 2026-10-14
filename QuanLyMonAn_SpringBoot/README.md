# Quản lý món ăn - Spring Boot + PostgreSQL

## Trước khi chạy
1. Thay `MSSV_CUA_BAN` bằng MSSV thật của bạn trong:
   - `src/main/resources/application.properties`
   - Tất cả `@Table`, `@Column`, `@JoinColumn` trong 3 Entity.
   - Tên 3 lớp Entity và các chỗ import/sử dụng chúng trong code.
2. Tạo database PostgreSQL tên `MSSV_CUA_BAN_MID`.
3. Sửa `spring.datasource.password` trong `application.properties`.
4. Cài JDK 21 và Maven, sau đó chạy `mvn spring-boot:run`.
5. Mở http://localhost:8080

## Lưu ý quan trọng
- Đây là bộ khung chạy ban đầu, cần thay MSSV trước khi chạy.
- Tạo tối thiểu 3 loại món qua API `POST /api/categories`, rồi tạo 5 món qua `POST /api/dishes`.
- API danh sách: `GET /api/dishes?name=pho&categoryId=1&available=true&minPrice=10000&maxPrice=100000&sortBy=price&direction=asc`
- Tạo loại món JSON: `{"name":"Main course","description":"Main dishes"}`
- Tạo món JSON: `{"name":"Pho bo","price":45000,"preparationTime":10,"isAvailable":true,"description":"Beef noodle soup","categoryId":1}`
- Thêm ảnh: `POST /api/dishes/1/images` với JSON `{"imageUrl":"https://example.com/dish.jpg","isThumbnail":true}`

## Ghi chú
Đề bài yêu cầu tiền tố MSSV ở tên database, cả 3 bảng và mọi cột vật lý. Hãy tìm toàn bộ `MSSV_CUA_BAN` và thay nhất quán bằng MSSV thật. Không chỉ thay tên file/lớp vì phải sửa cả các tham chiếu trong code.
