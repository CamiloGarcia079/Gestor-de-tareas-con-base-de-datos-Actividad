package vista;

import dao.EquipoDAO;
import dao.PersonaDAO;
import dao.TipoPersonaDAO;
import modelo.Equipo;
import modelo.Persona;
import modelo.TipoPersona;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class PanelPersonas extends JPanel {

    private final PersonaDAO personaDAO = new PersonaDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final TipoPersonaDAO tipoPersonaDAO = new TipoPersonaDAO();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Email", "Tipo", "Equipo"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    private final JTextField txtNombre = new JTextField(12);
    private final JTextField txtEmail = new JTextField(15);
    private final JComboBox<TipoPersona> comboTipo = new JComboBox<>();
    private final JComboBox<Equipo> comboEquipo = new JComboBox<>();

    public PanelPersonas() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Email:"));
        formulario.add(txtEmail);
        formulario.add(new JLabel("Tipo:"));
        formulario.add(comboTipo);
        formulario.add(new JLabel("Equipo:"));
        formulario.add(comboEquipo);
        JButton btnAgregar = new JButton("Agregar persona");
        formulario.add(btnAgregar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarPersona());

        cargarCombos();
        cargarTabla();
    }

    private void cargarCombos() {
        try {
            comboTipo.removeAllItems();
            for (TipoPersona tp : tipoPersonaDAO.listar()) comboTipo.addItem(tp);

            comboEquipo.removeAllItems();
            for (Equipo eq : equipoDAO.listar()) comboEquipo.addItem(eq);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar catalogos:\n" + ex.getMessage());
        }
    }

    private void agregarPersona() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        TipoPersona tipo = (TipoPersona) comboTipo.getSelectedItem();
        Equipo equipo = (Equipo) comboEquipo.getSelectedItem();

        if (nombre.isEmpty() || email.isEmpty() || tipo == null) {
            JOptionPane.showMessageDialog(this, "Nombre, email y tipo de persona son obligatorios.");
            return;
        }
        try {
            personaDAO.insertar(new Persona(0, nombre, email, tipo, equipo));
            txtNombre.setText("");
            txtEmail.setText("");
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos:\n" + ex.getMessage());
        }
    }

    // se llama desde afuera cuando se crea un equipo nuevo en otra pestaña
    public void refrescarCombos() {
        cargarCombos();
    }

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            for (Persona p : personaDAO.listar()) {
                String equipoNombre = (p.getEquipo() != null) ? p.getEquipo().getNombre() : "Sin equipo";
                modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getEmail(), p.getTipo().getNombre(), equipoNombre});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar personas:\n" + ex.getMessage());
        }
    }
}
