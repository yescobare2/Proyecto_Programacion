package com.campuslite.persistence;

import com.campuslite.domain.Enrollment;
import com.campuslite.domain.Evaluation;
import com.campuslite.domain.Laboratory;
import com.campuslite.domain.ProjectEvaluation;
import com.campuslite.domain.WrittenExam;

import java.util.List;
import javax.swing.*;
import java.io.*;

/**
 * Persistencia CSV de evaluaciones.
 */
public class EvaluationCSVRepository {

    /**
     * Guarda evaluaciones.
     */
	public void saveEvaluations(List<Enrollment> enrollments) {

	    try (PrintWriter writer =
	                 new PrintWriter(
	                         new FileWriter(
	                                 FilePaths.EVALUATIONS_FILE
	                         ))) {

	        writer.println(
	                "studentCode,courseCode,type,name,score,percentage"
	        );

	        for (Enrollment enrollment : enrollments) {

	            for (Evaluation evaluation :
	                    enrollment.getEvaluations()) {

	                writer.println(
	                        enrollment.getStudent()
	                                .getStudentCode() + "," +

	                        enrollment.getCourse()
	                                .getCourseCode() + "," +

	                        evaluation.getTypeName() + "," +

	                        evaluation.getEvaluationName() + "," +

	                        evaluation.getScore() + "," +

	                        evaluation.getPercentage()
	                );
	            }
	        }

	    } catch (IOException e) {

	        JOptionPane.showMessageDialog(
	                null,
	                "Error al guardar evaluaciones.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	        );

	        e.printStackTrace();
	    }
	}

    /**
     * Carga evaluaciones.
     */
	public void loadEvaluations(
	        List<Enrollment> enrollments
	) {

	    File file =
	            new File(
	                    FilePaths.EVALUATIONS_FILE
	            );

	    if (!file.exists()) {
	        return;
	    }

	    try (BufferedReader br =
	                 new BufferedReader(
	                         new FileReader(file))) {

	        br.readLine();

	        String line;

	        while ((line = br.readLine()) != null) {

	            String[] data = line.split(",");

	            String studentCode = data[0];

	            String courseCode = data[1];

	            String type = data[2];

	            String name = data[3];

	            double score =
	                    Double.parseDouble(data[4]);

	            double percentage =
	                    Double.parseDouble(data[5]);

	            for (Enrollment enrollment :
	                    enrollments) {

	                boolean sameStudent =
	                        enrollment.getStudent()
	                                .getStudentCode()
	                                .equals(studentCode);

	                boolean sameCourse =
	                        enrollment.getCourse()
	                                .getCourseCode()
	                                .equals(courseCode);

	                if (sameStudent && sameCourse) {

	                    Evaluation evaluation =
	                            createEvaluation(
	                                    type,
	                                    name,
	                                    score,
	                                    percentage
	                            );

	                    enrollment.addEvaluation(
	                            evaluation
	                    );

	                    break;
	                }
	            }
	        }

	    } catch (IOException ex) {

	        JOptionPane.showMessageDialog(
	                null,
	                "Error cargando evaluaciones.",
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	        );

	        ex.printStackTrace();
	    }
	}
    /**
     * Crea evaluación según tipo.
     */
    private Evaluation createEvaluation(
            String type,
            String name,
            double score,
            double percentage) {

        switch (type) {

            case "Examen":

                return new WrittenExam(
                        name,
                        score,
                        percentage
                );

            case "Laboratorio":

                return new Laboratory(
                        name,
                        score,
                        percentage
                );

            case "Proyecto":

                return new ProjectEvaluation(
                        name,
                        score,
                        percentage
                );

            default:
                return null;
        }
    }
}