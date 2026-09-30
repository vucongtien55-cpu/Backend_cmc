package com.yourcompany.baitapbuoi6.Services;

import com.yourcompany.baitapbuoi6.Entities.Student;
import com.yourcompany.baitapbuoi6.Reposotories.IStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final IStudentRepository studentRepository;


    //Lấy toàn bộ thông tin
    public List<Student> getAll(){
        return studentRepository.findAll();
    }

    //Thêm sinh viên
    public Student createStudent(Student student){
        if(studentRepository.existsByName(student.getName())){
            throw new RuntimeException("Sinh viên " + student.getName() + " đã tồn tại.");
        }
        return studentRepository.save(student);
    }

    //sua sinh vien
    public Student updateStudent(Long id, Student student){
        Student student1 = studentRepository.findById(id).orElseThrow(()-> new RuntimeException("Sinh viên có id: " + id + " không tồn tại."));
        if(student.getName() != null && !student1.getName().equals(student.getName()) && studentRepository.existsByName(student.getName())){
            throw new RuntimeException("Sinh viên "+ student.getName() + " đã tồn tại.");
        }
        student1.setName(student.getName());
        student1.setAge(student.getAge());
        student1.setAddress(student.getAddress());
        return studentRepository.save(student1);
    }

    //Xoa sinh viên
    public void deleteStudent(Long id){
        Student student = studentRepository
                .findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy sinh viên có id: " + id));
        studentRepository.delete(student);
    }
}
