package vista;

import dao.EquipoDAO;
import dao.EstadoTareaDAO;
import dao.TareaDAO;
import modelo.Equipo;
import modelo.EstadoTarea;
import modelo.Tarea;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class PanelTareas extends JPanel {

    private final TareaDAO tareaDAO = new TareaDAO();
    private final EstadoTareaDAO estadoDAO = new EstadoTareaDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Titulo", "Prioridad", "Estado", "Equipo", "Fecha limite"}, 0);
    private final JTable tabla = new JTable(modeloTabla);

    private final JTextField txtTitulo = new JTextField(12);
    private final JTextField txtDescripcion = new JTextField(15);
    private final JComboBox<String> comboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
    private final JComboBox<Equipo> comboEquipo = new JComboBox<>();
    private final JComboBox<EstadoTarea> comboEstadoNueva = new JComboBox<>();

    private final JComboBox<EstadoTarea> comboEstadoCambiar = new JComboBox<>();

    public PanelTareas() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(2, 1));

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila1.add(new JLabel("Titulo:"));
        fila1.add(txtTitulo);
        fila1.add(new JLabel("Descripcion:"));
        fila1.add(txtDescripcion);
        fila1.add(new JLabel("Prioridad:"));
        fila1.add(comboPrioridad);
        fila1.add(new JLabel("Equipo:"));
        fila1.add(comboEquipo);
        JButton btnAgregar = new JButton("Crear tarea");
        fila1.add(btnAgregar);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila2.add(new JLabel("Cambiar estado de la fila seleccionada a:"));
        fila2.add(comboEstadoCambiar);
        JButton btnCambiarEstado = new JButton("Actualizar estado");
        fila2.add(btnCambiarEstado);

        formulario.add(fila1);
        formulario.add(fila2);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarTarea());
        btnCambiarEstado.addActionListener(e -> cambiarEstado());

        cargarCombos();
        cargarTabla();
    }

    private void cargarCombos() {
        try {
            comboEquipo.removeAllItems();
            for (Equipo eq : equipoDAO.listar()) comboEquipo.addItem(eq);

            comboEstadoNueva.removeAllItems();
            comboEstadoCambiar.removeAllItems();
            for (EstadoTarea est : estadoDAO.listar()) {
                comboEstadoNueva.addItem(est);
                comboEstadoCambiar.addItem(est);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar catalogos:\n" + ex.getMessage());
        }
    }

    private void agregarTarea() {
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String prioridad = (String) comboPrioridad.getSelectedItem();
        Equipo equipo = (Equipo) comboEquipo.getSelectedItem();

        if (titulo.isEmpty() || equipo == null) {
            JOptionPane.showMessageDialog(this, "El titulo y el equipo son obligatorios.");
            return;
        }
        try {
            // toda tarea nueva nace en el primer estado del catalogo (normalmente "Por hacer")
            EstadoTarea estadoInicial = estadoDAO.listar().get(0);
            Tarea t = new Tarea(0, titulo, descripcion, prioridad, estadoInicial, LocalDate.now(), null, equipo);
            tareaDAO.insertar(t);
            txtTitulo.setText("");
            txtDescripcion.setText("");
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos:\n" + ex.getMessage());
        }
    }

    private void cambiarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una tarea en la tabla.");
            return;
        }
        int idTarea = (int) modeloTabla.getValueAt(fila, 0);
        EstadoTarea nuevoEstado = (EstadoTarea) comboEstadoCambiar.getSelectedItem();
        if (nuevoEstado == null) return;

        try {
            tareaDAO.actualizarEstado(idTarea, nuevoEstado.getId());
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado:\n" + ex.getMessage());
        }
    }

    public void refrescarCombos() {
        cargarCombos();
    }

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            for (Tarea t : tareaDAO.listar()) {
                String limite = (t.getFechaLimite() != null) ? t.getFechaLimite().toString() : "-";
                modeloTabla.addRow(new Object[]{t.getId(), t.getTitulo(), t.getPrioridad(),
                        t.getEstado().getNombre(), t.getEquipo().getNombre(), limite});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar tareas:\n" + ex.getMessage());
        }
    }
}
