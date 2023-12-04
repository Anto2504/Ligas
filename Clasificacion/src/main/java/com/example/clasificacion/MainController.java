package com.example.clasificacion;

import com.example.clasificacion.model.Equipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private ListView<Equipo> Clasificacion;

    @FXML
    private MenuItem Detalles;

    @FXML
    private ComboBox<String> Liga_Futbol;

    private ObservableList<String> equipos;

    @FXML
    private MenuItem Salir;

    @FXML
    private ComboBox<String> Temporada;

    private ObservableList<String> season;

    @FXML
    private Button Ver;

    @FXML
    private Menu menu_acciones;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instancias();
        cargarLigas();
        cargarTemporadas();

        Ver.setOnAction(this);
        Detalles.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == Ver) {
            if (Liga_Futbol.getValue() != null && Temporada.getValue() != null) {
                cargarTablaClasificacion();
            } else {
                mostrarAlerta("Error", "Por favor, selecciona una liga y una temporada.");
            }
        } else if (actionEvent.getSource() == Detalles) {
            Equipo equipoSeleccionado = Clasificacion.getSelectionModel().getSelectedItem();
            if (equipoSeleccionado != null) {
                mostrarDetallesEquipo(equipoSeleccionado);
            } else {
                mostrarAlerta("Error", "Por favor, selecciona un equipo para ver los detalles.");
            }
        }
    }

    private void cargarLigas() {
        try {
            URL url = new URL("https://www.thesportsdb.com/api/v1/json/3/all_leagues.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject json = new JSONObject(response.toString());
            JSONArray leagues = json.getJSONArray("leagues");

            for (int i = 0; i < leagues.length(); i++) {
                JSONObject equipo = leagues.getJSONObject(i);
                String liga = equipo.getString("strLeague");
                equipos.add(liga);
            }

            Liga_Futbol.setItems(equipos);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar las ligas.");
        }
    }

    private void cargarTemporadas() {
        try {
            URL url = new URL("https://www.thesportsdb.com/api/v1/json/3/search_all_seasons.php?id=4328&authuser=0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject json = new JSONObject(response.toString());
            JSONArray seasons = json.getJSONArray("seasons");

            for (int i = 0; i < seasons.length(); i++) {
                JSONObject temp = seasons.getJSONObject(i);
                String seasonName = temp.getString("strSeason");
                season.add(seasonName);
            }

            Temporada.setItems(season);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar las temporadas.");
        }
    }

    private void cargarTablaClasificacion() {
        String selectedLeague = Liga_Futbol.getValue();
        String selectedSeason = Temporada.getValue();

        try {
            URL url = new URL("https://www.thesportsdb.com/api/v1/json/3/lookuptable.php?l=" + getLeagueId(selectedLeague) + "&s=" + selectedSeason);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject json = new JSONObject(response.toString());
            JSONArray standings = json.getJSONArray("table");

            ObservableList<Equipo> equipos = FXCollections.observableArrayList();
            for (int i = 0; i < standings.length(); i++) {
                JSONObject standing = standings.getJSONObject(i);
                String teamName = standing.getString("strTeam");
                String teamBadge = standing.getString("strTeamBadge");

                Equipo equipo = new Equipo(teamName, teamBadge, "", "");
                equipos.add(equipo);
            }

            Clasificacion.setItems(equipos);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar la tabla de clasificación.");
        }
    }

    private void instancias() {
        equipos = FXCollections.observableArrayList();
        season = FXCollections.observableArrayList();
    }

    private String getLeagueId(String leagueName) {
        try {
            URL url = new URL("https://www.thesportsdb.com/api/v1/json/3/all_leagues.php?authuser=0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject json = new JSONObject(response.toString());
            JSONArray leagues = json.getJSONArray("leagues");

            for (int i = 0; i < leagues.length(); i++) {
                JSONObject league = leagues.getJSONObject(i);
                if (league.getString("strLeague").equals(leagueName)) {
                    return league.getString("idLeague");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al obtener el id de la liga.");
        }

        return "";
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void mostrarDetallesEquipo(Equipo equipo) {
        try {
            URL url = new URL("https://www.thesportsdb.com/api/v1/json/3/lookuptable.php?l=4328&s=2020-2021&authuser=0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject json = new JSONObject(response.toString());
            JSONArray table = json.getJSONArray("table");

            for (int i = 0; i < table.length(); i++) {
                JSONObject teamInfo = table.getJSONObject(i);
                String currentTeamName = teamInfo.getString("strTeam");

                if (currentTeamName.equals(equipo.getNombre())) {
                    String teamBadge = teamInfo.getString("strTeamBadge");
                    String teamName = teamInfo.getString("strTeam");
                    String teamForm = teamInfo.getString("strForm");
                    int teamPoints = teamInfo.getInt("intPoints");

                    mostrarDialogoDetalles(teamBadge, teamName, teamForm, teamPoints);

                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar los detalles del equipo.");
        }
    }

    private void mostrarDialogoDetalles(String teamBadge, String teamName, String teamForm, int teamPoints) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Detalles del Equipo");

        VBox content = new VBox();
        content.getChildren().add(new ImageView(new Image(teamBadge)));
        content.getChildren().add(new Label("Nombre del Equipo: " + teamName));
        content.getChildren().add(new Label("Forma: " + teamForm));
        content.getChildren().add(new Label("Puntos: " + teamPoints));

        dialog.getDialogPane().setContent(content);

        ButtonType cerrarButton = new ButtonType("Cerrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(cerrarButton);

        dialog.showAndWait();
    }
}
