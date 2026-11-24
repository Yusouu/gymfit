import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.format.DateTimeFormatter;

public class GymFitController {

    @FXML private TextField txtNombre, txtApellido1, txtApellido2, txtIban;
    @FXML private DatePicker dpFechaNac;
    @FXML private RadioButton rbMujer, rbHombre, rbOtro;
    @FXML private RadioButton rbBasica, rbPlus, rbPremium, rbJoven, rbSenior;
    @FXML private ComboBox<String> cbProvincia, cbClase;
    @FXML private TableView<String[]> tablaSocios;
    @FXML private TableColumn<String[], String> colNombre, colApellidos, colFecha, colProvincia, colCuota, colClase;
    @FXML private VBox panelMensaje;
    @FXML private Label lblTituloError, lblDetalleError, lblSolucionError;

    private ToggleGroup grupoSexo;
    private ToggleGroup grupoCuota;
    private final ObservableList<String[]> listaSocios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        grupoSexo = new ToggleGroup();
        rbMujer.setToggleGroup(grupoSexo);
        rbHombre.setToggleGroup(grupoSexo);
        rbOtro.setToggleGroup(grupoSexo);

        grupoCuota = new ToggleGroup();
        rbBasica.setToggleGroup(grupoCuota);
        rbPlus.setToggleGroup(grupoCuota);
        rbPremium.setToggleGroup(grupoCuota);
        rbJoven.setToggleGroup(grupoCuota);
        rbSenior.setToggleGroup(grupoCuota);

        cbProvincia.setItems(FXCollections.observableArrayList(
                "Sevilla", "Madrid", "Málaga", "Cádiz", "Córdoba", "Granada", "Huelva", "Jaén", "Valencia", "Barcelona"));
        cbClase.setItems(FXCollections.observableArrayList(
                "Spinning", "Pilates", "Yoga", "Body Pump", "Zumba", "Cross Training", "Natación", "GAP"));

        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        colApellidos.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        colProvincia.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
        colCuota.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[4]));
        colClase.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[5]));

        listaSocios.addAll(
                new String[]{"Ana", "Pérez López", "12/03/1999", "Sevilla", "Premium", "Pilates"},
                new String[]{"Luis", "Martín Gil", "21/07/2001", "Sevilla", "Básica", "Cross Training"},
                new String[]{"Marta", "Sánchez Díaz", "30/11/1988", "Cádiz", "Senior", "Yoga"}
        );
        tablaSocios.setItems(listaSocios);
    }

    @FXML
    private void onGuardar() {
        if (txtNombre.getText().isBlank() || txtApellido1.getText().isBlank() || dpFechaNac.getValue() == null
                || cbProvincia.getValue() == null || grupoCuota.getSelectedToggle() == null || cbClase.getValue() == null) {
            panelMensaje.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #F87171; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 12;");
            lblTituloError.setText("Faltan datos obligatorios por completar");
            lblDetalleError.setText("No se ha podido guardar el alta porque hay campos obligatorios vacíos.");
            lblSolucionError.setText("Cómo solucionarlo: completa nombre, apellidos, fecha (dd/mm/aaaa), provincia, cuota y clase, y pulsa «Guardar socio».");
            return;
        }

        String fecha = dpFechaNac.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String cuota = ((RadioButton) grupoCuota.getSelectedToggle()).getText();
        listaSocios.add(new String[]{
                txtNombre.getText().trim(),
                (txtApellido1.getText() + " " + txtApellido2.getText()).trim(),
                fecha,
                cbProvincia.getValue(),
                cuota,
                cbClase.getValue()
        });

        panelMensaje.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #34D399; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 12;");
        lblTituloError.setStyle("-fx-font-weight: bold; -fx-text-fill: #065F46; -fx-font-size: 14px;");
        lblTituloError.setText("Socio guardado correctamente");
        lblDetalleError.setStyle("-fx-text-fill: #065F46;");
        lblDetalleError.setText("Se ha registrado el alta de " + txtNombre.getText() + " " + txtApellido1.getText() + ".");
        lblSolucionError.setStyle("-fx-text-fill: #065F46; -fx-font-weight: bold;");
        lblSolucionError.setText("Ya aparece en la tabla de socios registrados.");
    }

    @FXML
    private void onLimpiar() {
        txtNombre.clear();
        txtApellido1.clear();
        txtApellido2.clear();
        txtIban.clear();
        dpFechaNac.setValue(null);
        cbProvincia.setValue(null);
        cbClase.setValue(null);
        if (grupoSexo.getSelectedToggle() != null) grupoSexo.getSelectedToggle().setSelected(false);
        if (grupoCuota.getSelectedToggle() != null) grupoCuota.getSelectedToggle().setSelected(false);
    }

    @FXML
    private void onNuevo() {
        onLimpiar();
        txtNombre.requestFocus();
    }

    @FXML
    private void onDarDeBaja() {
        String[] seleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar baja de socio");
        alerta.setHeaderText("¿Dar de baja a " + seleccionado[0] + " " + seleccionado[1] + "?");
        alerta.setContentText("Esta acción quitará su ficha de la lista.");
        alerta.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) listaSocios.remove(seleccionado);
        });
    }

    @FXML
    private void onSalir() {
        System.exit(0);
    }
}