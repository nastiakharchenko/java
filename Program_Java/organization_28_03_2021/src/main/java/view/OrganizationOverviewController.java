package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import model.Organization;
import model.OrganizationWithId;
import repository.OrganizationRepository;
import repository.OrganizationRepositoryImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrganizationOverviewController {
    @FXML
    private TableView<OrganizationWithId> organizationTable;
    @FXML
    private TableColumn<OrganizationWithId, Long> idColumn;
    @FXML
    private TableColumn<OrganizationWithId, String> nameColumn;

    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label employeesCountLabel;
    @FXML
    private Label creationDateLabel;
    @FXML
    private Label mfoLabel;
    @FXML
    private Label siteLabel;
    @FXML
    private Label commerciallyLabel;
    @FXML
    private Label idLabel;

    //private static final List<Organization> organizations = new ArrayList<>();
    //private final OrganizationRepository organization = new OrganizationRepositoryImpl();
    private OrganizationRepositoryImpl organization = new OrganizationRepositoryImpl();
    ObservableList<OrganizationWithId> organizationData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        organization.importFromCsv("1.csv");
        organizationData.addAll(organization.getOrganizationData());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        organizationTable.setItems(organizationData);   //(ObservableList<OrganizationRepositoryImpl>)

        // Show details by selected item
        showOrganizationDetails(null);
        organizationTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showOrganizationDetails(newValue)
        );
    }

    private void showOrganizationDetails(OrganizationWithId organization) {
        if (organization != null) {
            // Заполняем метки информацией из объекта person.
            nameLabel.setText(organization.getName());
            addressLabel.setText(organization.getAddress());
            employeesCountLabel.setText(Integer.toString(organization.getEmployeesCount()));
            creationDateLabel.setText(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(organization.getCreationDate()));
            mfoLabel.setText(Integer.toString(organization.getMfo()));
            siteLabel.setText(organization.getSite());
            commerciallyLabel.setText(Boolean.toString(organization.getCommercially()));
            idLabel.setText(Long.toString(organization.getId()));

        } else {
            // Если organization = null, то убираем весь текст.
            nameLabel.setText("");
            addressLabel.setText("");
            employeesCountLabel.setText("");
            creationDateLabel.setText("");
            mfoLabel.setText("");
            siteLabel.setText("");
            commerciallyLabel.setText("");
            idLabel.setText("");
        }
    }


}
