package com.campuslite.ui;

import com.campuslite.domain.*;
import com.campuslite.logic.EnrollmentManager;
import com.campuslite.logic.ValidationUtils;
import com.campuslite.persistence.EvaluationCSVRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de evaluaciones.
 */
public class EvaluationsPanel extends JPanel {

    private final EnrollmentManager enrollmentManager;
    private final EvaluationCSVRepository repository;

    // selección separada
    private JComboBox<Student> cmbStudents;
    private JComboBox<Course> cmbCourses;

    private JComboBox<String> cmbType;

    private JTextField txtName;
    private JTextField txtScore;
    private JTextField txtPercentage;

    private ModernTable table;
    private DefaultTableModel model;

    public EvaluationsPanel(EnrollmentManager enrollmentManager) {

        this.enrollmentManager = enrollmentManager;
        this.repository = new EvaluationCSVRepository();

        initialize();
        loadData();
        refreshTable();
    }

    // =========================
    // INIT UI
    // =========================
    private void initialize() {

        setLayout(new BorderLayout());
        setBackground(UIStyles.BACKGROUND_COLOR);
        setBorder(UIStyles.createPadding());

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(UIStyles.BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // =========================
        // COMPONENTES
        // =========================
        cmbStudents = new JComboBox<>();
        cmbCourses = new JComboBox<>();

        cmbType = new JComboBox<>();
        cmbType.addItem("Examen");
        cmbType.addItem("Laboratorio");
        cmbType.addItem("Proyecto");

        txtName = new JTextField(15);
        txtScore = new JTextField(15);
        txtPercentage = new JTextField(15);

        // =========================
        // LABELS
        // =========================
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Estudiante"), gbc);

        gbc.gridx = 1;
        topPanel.add(new JLabel("Curso"), gbc);

        gbc.gridx = 2;
        topPanel.add(new JLabel("Tipo"), gbc);

        gbc.gridx = 3;
        topPanel.add(new JLabel("Nombre"), gbc);

        gbc.gridx = 4;
        topPanel.add(new JLabel("Nota"), gbc);

        gbc.gridx = 5;
        topPanel.add(new JLabel("Porcentaje"), gbc);

        // =========================
        // CAMPOS
        // =========================
        gbc.gridy = 1;

        gbc.gridx = 0;
        topPanel.add(cmbStudents, gbc);

        gbc.gridx = 1;
        topPanel.add(cmbCourses, gbc);

        gbc.gridx = 2;
        topPanel.add(cmbType, gbc);

        gbc.gridx = 3;
        topPanel.add(txtName, gbc);

        gbc.gridx = 4;
        topPanel.add(txtScore, gbc);

        gbc.gridx = 5;
        topPanel.add(txtPercentage, gbc);

        // =========================
        // BOTONES
        // =========================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UIStyles.BACKGROUND_COLOR);

        ModernButton btnAdd = new ModernButton("➕ Agregar");
        ModernButton btnUpdate = new ModernButton("🔄 Actualizar");
        ModernButton btnDelete = new ModernButton("❌ Eliminar");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.CENTER;

        topPanel.add(buttonPanel, gbc);

        // =========================
        // TABLA
        // =========================
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "Inscripción",
                "Tipo",
                "Nombre",
                "Nota",
                "Porcentaje"
        });

        table = new ModernTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // =========================
        // EVENTS
        // =========================
        btnAdd.addActionListener(e -> addEvaluation());
        btnUpdate.addActionListener(e -> updateEvaluation());
        btnDelete.addActionListener(e -> deleteEvaluation());

        cmbStudents.addActionListener(e -> refreshTable());
        cmbCourses.addActionListener(e -> refreshTable());

        table.getSelectionModel().addListSelectionListener(e -> loadSelectedEvaluation());

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // =========================
    // ADD
    // =========================
    private void addEvaluation() {

        try {

            Enrollment enrollment = getSelectedEnrollment();

            if (enrollment == null) {
                JOptionPane.showMessageDialog(this, "Seleccione estudiante y curso.");
                return;
            }

            String name = txtName.getText();
            String scoreText = txtScore.getText();
            String percentageText = txtPercentage.getText();

            validateFields(name, scoreText, percentageText);

            Evaluation evaluation = createEvaluation(
                    cmbType.getSelectedItem().toString(),
                    name,
                    Double.parseDouble(scoreText),
                    Double.parseDouble(percentageText)
            );

            enrollment.validatePercentage(evaluation);
            enrollment.addEvaluation(evaluation);
            repository.saveEvaluations(
                    enrollmentManager.getEnrollments()
            );

            refreshTable();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // =========================
    // UPDATE
    // =========================
    private void updateEvaluation() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una evaluación.");
            return;
        }

        try {

            Enrollment enrollment = getSelectedEnrollment();

            Evaluation old = enrollment.getEvaluations().get(row);

            Evaluation updated = createEvaluation(
                    cmbType.getSelectedItem().toString(),
                    txtName.getText(),
                    Double.parseDouble(txtScore.getText()),
                    Double.parseDouble(txtPercentage.getText())
            );

            enrollment.removeEvaluation(old);
            enrollment.validatePercentage(updated);
            enrollment.addEvaluation(updated);
            repository.saveEvaluations(
                    enrollmentManager.getEnrollments()
            );

            refreshTable();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // =========================
    // DELETE
    // =========================
    private void deleteEvaluation() {

        int row = table.getSelectedRow();

        if (row == -1) return;

        Enrollment enrollment = getSelectedEnrollment();

        Evaluation eval = enrollment.getEvaluations().get(row);

        enrollment.removeEvaluation(eval);
        repository.saveEvaluations(
                enrollmentManager.getEnrollments()
        );

        refreshTable();
    }

    // =========================
    // TABLE
    // =========================
    private void refreshTable() {

        model.setRowCount(0);

        Enrollment enrollment = getSelectedEnrollment();

        if (enrollment == null) return;

        for (Evaluation e : enrollment.getEvaluations()) {

            model.addRow(new Object[]{
                    enrollment.getEnrollmentCode(),
                    e.getTypeName(),
                    e.getEvaluationName(),
                    e.getScore(),
                    e.getPercentage()
            });
        }

        model.fireTableDataChanged();
    }

    // =========================
    // HELPERS
    // =========================
    private Enrollment getSelectedEnrollment() {

        Student s = (Student) cmbStudents.getSelectedItem();
        Course c = (Course) cmbCourses.getSelectedItem();

        if (s == null || c == null) return null;

        return enrollmentManager.findEnrollment(s, c);
    }

    private void loadData() {

        cmbStudents.removeAllItems();
        cmbCourses.removeAllItems();

        for (Enrollment enrollment :
                enrollmentManager.getEnrollments()) {

            Student student =
                    enrollment.getStudent();

            Course course =
                    enrollment.getCourse();

            boolean studentExists = false;

            for (int i = 0;
                 i < cmbStudents.getItemCount();
                 i++) {

                if (cmbStudents.getItemAt(i)
                        .getStudentCode()
                        .equals(student.getStudentCode())) {

                    studentExists = true;
                    break;
                }
            }

            if (!studentExists) {
                cmbStudents.addItem(student);
            }

            boolean courseExists = false;

            for (int i = 0;
                 i < cmbCourses.getItemCount();
                 i++) {

                if (cmbCourses.getItemAt(i)
                        .getCourseCode()
                        .equals(course.getCourseCode())) {

                    courseExists = true;
                    break;
                }
            }

            if (!courseExists) {
                cmbCourses.addItem(course);
            }
        }
    }
    
    public void reloadData() {

        loadData();

        refreshTable();

        revalidate();

        repaint();
    }

    private Evaluation createEvaluation(String type, String name, double score, double percentage) {

        switch (type) {
            case "Examen":
                return new WrittenExam(name, score, percentage);
            case "Laboratorio":
                return new Laboratory(name, score, percentage);
            case "Proyecto":
                return new ProjectEvaluation(name, score, percentage);
        }

        return null;
    }

    private void validateFields(String name, String score, String percentage) {

        if (ValidationUtils.isEmpty(name)
                || ValidationUtils.isEmpty(score)
                || ValidationUtils.isEmpty(percentage)) {
            throw new IllegalArgumentException("Campos obligatorios.");
        }

        if (!ValidationUtils.isDouble(score)
                || !ValidationUtils.isDouble(percentage)) {
            throw new IllegalArgumentException("Valores inválidos.");
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtScore.setText("");
        txtPercentage.setText("");
    }

    private void loadSelectedEvaluation() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        txtName.setText(model.getValueAt(row, 2).toString());
        txtScore.setText(model.getValueAt(row, 3).toString());
        txtPercentage.setText(model.getValueAt(row, 4).toString());
        cmbType.setSelectedItem(model.getValueAt(row, 1).toString());
    }
}