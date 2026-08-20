package vista;

import dao.EquipoDAO;
import modelo.Equipo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class PanelEquipos extends JPanel {

    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripcion"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtDescripcion = new JTextField(20);

    public PanelEquipos() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Descripcion:"));
        formulario.add(txtDescripcion);
        JButton btnAgregar = new JButton("Agregar equipo");
        formulario.add(btnAgregar);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarEquipo());

        cargarTabla();
    }

    private void agregarEquipo() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del equipo es obligatorio.");
            return;
        }
        try {
            equipoDAO.insertar(new Equipo(0, nombre, descripcion));
            txtNombre.setText("");
            txtDescripcion.setText("");
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos:\n" + ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            for (Equipo eq : equipoDAO.listar()) {
                modeloTabla.addRow(new Object[]{eq.getId(), eq.getNombre(), eq.getDescripcion()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar equipos:\n" + ex.getMessage());
        }
    }
}
