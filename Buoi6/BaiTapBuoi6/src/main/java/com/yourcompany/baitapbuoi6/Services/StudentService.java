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

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên có id: " + id));
    }

    public Student createStudent(Student student) {
        if (student.getName() != null && studentRepository.existsByName(student.getName())) {
            throw new RuntimeException("Sinh viên " + student.getName() + " đã tồn tại.");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sinh viên có id: " + id + " không tồn tại."));

        if (student.getName() != null && !student.getName().isBlank()) {
            if (!student.getName().equals(existing.getName())
                    && studentRepository.existsByName(student.getName())) {
                throw new RuntimeException("Sinh viên " + student.getName() + " đã tồn tại.");
            }
            existing.setName(student.getName());
        }

        if (student.getAge() != null) {
            existing.setAge(student.getAge());
        }

        if (student.getEmail() != null && !student.getEmail().isBlank()) {
            existing.setEmail(student.getEmail());
        }

        if (student.getAddress() != null && !student.getAddress().isBlank()) {
            existing.setAddress(student.getAddress());
        }

        if (student.getPhone() != null) {
            existing.setPhone(student.getPhone());
        }

        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên có id: " + id));
        studentRepository.delete(student);
    }
}