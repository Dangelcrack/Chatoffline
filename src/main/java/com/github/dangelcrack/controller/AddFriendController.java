package com.github.dangelcrack.controller;

import com.github.dangelcrack.model.dao.UserDAO;
import com.github.dangelcrack.model.entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class AddFriendController extends Controller {

    @FXML
    private TextField searchTextField; // Campo de texto para búsqueda

    @FXML
    private ListView<String> usersListView; // ListView para mostrar los usuarios

    private UserDAO userDAO;

    @FXML
    public void initialize() {
        userDAO = new UserDAO(); // Inicializamos el DAO para cargar los usuarios
        loadUsersIntoListView(userDAO.findAll()); // Cargar todos los usuarios inicialmente
    }

    // Método para cargar los usuarios en el ListView
    private void loadUsersIntoListView(List<User> users) {
        usersListView.getItems().clear(); // Limpiar la lista antes de cargar nuevos elementos
        for (User user : users) {
            usersListView.getItems().add(user.getUsername());
        }
    }

    // Manejar la acción del botón de búsqueda
    @FXML
    public void handleSearchFriend() {
        String searchQuery = searchTextField.getText().trim().toLowerCase(); // Obtener la búsqueda y convertirla en minúsculas

        if (!searchQuery.isEmpty()) {
            // Filtrar los usuarios que coinciden con la búsqueda
            List<User> filteredUsers = userDAO.findAll().stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());

            // Cargar los usuarios filtrados en el ListView
            loadUsersIntoListView(filteredUsers);
        } else {
            // Si el campo de búsqueda está vacío, mostrar todos los usuarios
            loadUsersIntoListView(userDAO.findAll());
        }
    }

    @Override
    public void onOpen(Object input) {
        // Manejo de apertura de la vista si es necesario
    }

    @Override
    public void onClose(Object output) {
        // Manejo de cierre de la vista si es necesario
    }
}
