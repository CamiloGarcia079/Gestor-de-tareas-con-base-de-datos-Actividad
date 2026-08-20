package vista;

import dao.TareaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class PanelDashboard extends JPanel {

    private final TareaDAO tareaDAO = new TareaDAO();

    private final DefaultTableModel modeloEstado = new DefaultTableModel(new Object[]{"Estado", "Total tareas"}, 0);
    private final DefaultTableModel modeloPrioridad = new DefaultTableModel(new Object[]{"Prioridad", "Total tareas"}, 0);

    public PanelDashboard() {
        setLayout(new GridLayout(1, 2, 10, 10));

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.add(new JLabel("Tareas por estado", SwingConstants.CENTER), BorderLayout.NORTH);
        panelEstado.add(new JScrollPane(new JTable(modeloEstado)), BorderLayout.CENTER);

        JPanel panelPrioridad = new JPanel(new BorderLayout());
        panelPrioridad.add(new JLabel("Tareas por prioridad", SwingConstants.CENTER), BorderLayout.NORTH);
        panelPrioridad.add(new JScrollPane(new JTable(modeloPrioridad)), BorderLayout.CENTER);

        add(panelEstado);
        add(panelPrioridad);

        actualizar();
    }

    public void actualizar() {
        try {
            modeloEstado.setRowCount(0);
            for (Object[] fila : tareaDAO.contarPorEstado()) {
                modeloEstado.addRow(fila);
            }
            modeloPrioridad.setRowCount(0);
            for (Object[] fila : tareaDAO.contarPorPrioridad()) {
                modeloPrioridad.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el dashboard:\n" + ex.getMessage());
        }
    }
}
