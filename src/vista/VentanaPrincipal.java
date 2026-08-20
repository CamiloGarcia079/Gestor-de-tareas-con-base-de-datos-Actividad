package vista;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Gestor de Tareas - Equipos Scrum (MySQL)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);

        PanelEquipos panelEquipos = new PanelEquipos();
        PanelPersonas panelPersonas = new PanelPersonas();
        PanelTareas panelTareas = new PanelTareas();
        PanelAsignaciones panelAsignaciones = new PanelAsignaciones();
        PanelDashboard panelDashboard = new PanelDashboard();

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Equipos", panelEquipos);
        pestanas.addTab("Personas", panelPersonas);
        pestanas.addTab("Tareas", panelTareas);
        pestanas.addTab("Asignaciones", panelAsignaciones);
        pestanas.addTab("Dashboard", panelDashboard);

        // cada vez que se cambia de pestaña, refrescamos combos/tabla para reflejar
        // lo que se haya creado en las otras pestanas (equipos nuevos, tareas nuevas, etc.)
        pestanas.addChangeListener(e -> {
            int idx = pestanas.getSelectedIndex();
            String titulo = pestanas.getTitleAt(idx);
            switch (titulo) {
                case "Personas":
                    panelPersonas.refrescarCombos();
                    break;
                case "Tareas":
                    panelTareas.refrescarCombos();
                    break;
                case "Asignaciones":
                    panelAsignaciones.refrescarCombos();
                    break;
                case "Dashboard":
                    panelDashboard.actualizar();
                    break;
            }
        });

        add(pestanas);
    }
}
