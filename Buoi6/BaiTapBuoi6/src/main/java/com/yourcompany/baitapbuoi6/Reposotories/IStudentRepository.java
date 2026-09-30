package com.yourcompany.baitapbuoi6.Reposotories;

import com.yourcompany.baitapbuoi6.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Boolean existsByName(String name);
}
