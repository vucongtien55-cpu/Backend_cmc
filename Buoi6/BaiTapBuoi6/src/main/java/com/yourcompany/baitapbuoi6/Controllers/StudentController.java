package com.yourcompany.baitapbuoi6.Controllers;

import com.yourcompany.baitapbuoi6.Entities.Student;
import com.yourcompany.baitapbuoi6.Services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    // Xem tất cả
    @GetMapping
    public List<Student> getAll() {
        return studentService.getAll();
    }

    //Thêm sinh viên mới
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    //Sua sinh vien
    @PutMapping("{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student){
        return studentService.updateStudent(id, student);
    }

    //xoa sinh vien
    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }
}
