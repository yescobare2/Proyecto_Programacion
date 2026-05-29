package com.campuslite.persistence;

import com.campuslite.domain.Course;
import com.campuslite.domain.Enrollment;
import com.campuslite.domain.Student;
import com.campuslite.logic.EnrollmentManager;

import javax.swing.*;
import java.io.*;
import java.util.List;

/**
 * Persistencia CSV de inscripciones.
 */
public class EnrollmentCSVRepository {

    /**
     * Guarda inscripciones.
     */
    public void saveEnrollments(
            List<Enrollment> enrollments) {

        try (PrintWriter writer =
                     new PrintWriter(
                             new FileWriter(
                                     FilePaths.ENROLLMENTS_FILE
                             ))) {

            writer.println(
                    "studentCode,courseCode"
            );

            for (Enrollment enrollment :
                    enrollments) {

                writer.println(
                        enrollment.getStudent()
                                .getStudentCode()
                                + ","
                                +
                                enrollment.getCourse()
                                        .getCourseCode()
                );
            }

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error guardando inscripciones."
            );

            ex.printStackTrace();
        }
    }

    /**
     * Carga inscripciones.
     */
    public void loadEnrollments(
            EnrollmentManager enrollmentManager,
            List<Student> students,
            List<Course> courses) {

        File file =
                new File(
                        FilePaths.ENROLLMENTS_FILE
                );

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(file))) {

            String line;

            /**
             * Saltar encabezado.
             */
            br.readLine();

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data =
                        line.split(",");

                if (data.length < 2) {
                    continue;
                }

                String studentCode =
                        data[0].trim();

                String courseCode =
                        data[1].trim();

                Student student =
                        findStudent(
                                students,
                                studentCode
                        );

                Course course =
                        findCourse(
                                courses,
                                courseCode
                        );

                if (student != null
                        && course != null) {

                    /**
                     * Evita duplicados.
                     */
                    if (!enrollmentManager
                            .isStudentEnrolled(
                                    student,
                                    course
                            )) {

                        enrollmentManager
                                .enrollStudent(
                                        student,
                                        course
                                );
                    }
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error cargando inscripciones."
            );

            ex.printStackTrace();
        }
    }

    /**
     * Busca estudiante.
     */
    private Student findStudent(
            List<Student> students,
            String code) {

        for (Student student :
                students) {

            if (student.getStudentCode()
                    .equals(code)) {

                return student;
            }
        }

        return null;
    }

    /**
     * Busca curso.
     */
    private Course findCourse(
            List<Course> courses,
            String code) {

        for (Course course :
                courses) {

            if (course.getCourseCode()
                    .equals(code)) {

                return course;
            }
        }

        return null;
    }
}