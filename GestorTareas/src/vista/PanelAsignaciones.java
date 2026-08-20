package vista;

import dao.AsignacionDAO;
import dao.PersonaDAO;
import dao.TareaDAO;
import modelo.Asignacion;
import modelo.Persona;
import modelo.Tarea;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class PanelAsignaciones extends JPanel {

    private final AsignacionDAO asignacionDAO = new AsignacionDAO();
    private final TareaDAO tareaDAO = new TareaDAO();
    private final PersonaDAO personaDAO = new PersonaDAO();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Tarea", "Persona asignada", "Nota"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    private final JComboBox<Tarea> comboTarea = new JComboBox<>();
    private final JComboBox<Persona> comboPersona = new JComboBox<>();
    private final JTextField txtNota = new JTextField(20);

    public PanelAsignaciones() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Tarea:"));
        formulario.add(comboTarea);
        formulario.add(new JLabel("Persona:"));
        formulario.add(comboPersona);
        formulario.add(new JLabel("Nota:"));
        formulario.add(txtNota);
        JButton btnAsignar = new JButton("Asignar tarea");
        formulario.add(btnAsignar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnAsignar.addActionListener(e -> asignar());

        cargarCombos();
        cargarTabla();
    }

    private void cargarCombos() {
        try {
            comboTarea.removeAllItems();
            for (Tarea t : tareaDAO.listar()) comboTarea.addItem(t);

            comboPersona.removeAllItems();
            for (Persona p : personaDAO.listar()) comboPersona.addItem(p);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar tareas/personas:\n" + ex.getMessage());
        }
    }

    private void asignar() {
        Tarea tarea = (Tarea) comboTarea.getSelectedItem();
        Persona persona = (Persona) comboPersona.getSelectedItem();
        if (tarea == null || persona == null) {
            JOptionPane.showMessageDialog(this, "Debes elegir una tarea y una persona.");
            return;
        }
        try {
            asignacionDAO.insertar(new Asignacion(0, tarea, persona, txtNota.getText().trim()));
            txtNota.setText("");
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la asignacion:\n" + ex.getMessage());
        }
    }

    public void refrescarCombos() {
        cargarCombos();
    }

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            for (Asignacion a : asignacionDAO.listar()) {
                modeloTabla.addRow(new Object[]{a.getId(), a.getTarea().getTitulo(), a.getPersona().getNombre(), a.getNota()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar asignaciones:\n" + ex.getMessage());
        }
    }
}
