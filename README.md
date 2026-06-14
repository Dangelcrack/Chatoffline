# 💬 ChatOffline

Aplicación de escritorio de chat que funciona **sin conexión a internet**, desarrollada en **Java 21 con JavaFX**. Los usuarios y mensajes se almacenan localmente en archivos XML mediante JAXB, lo que permite una comunicación persistente sin depender de servidores externos.

---

## 🚀 Características

- 💾 **Persistencia local** — usuarios y mensajes guardados en `users.xml` y `messages.xml`
- 🖥️ **Interfaz gráfica** con JavaFX y FXML
- 👤 **Gestión de usuarios** — registro e inicio de sesión sin servidor
- 📨 **Historial de mensajes** — los chats se conservan entre sesiones
- 📦 **Sin dependencias externas** — funciona completamente offline

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| JavaFX | 20.0.2 | Interfaz gráfica |
| JAXB | 2.3.1 | Serialización XML |
| Maven | — | Gestión de dependencias |

---

## ▶️ Instalación y ejecución

### Requisitos previos
- Java 21 o superior
- Maven 3.x

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/Dangelcrack/Chatoffline.git
cd Chatoffline

# 2. Compilar y ejecutar con Maven
mvn javafx:run
```

---

## 📁 Estructura del proyecto

```
Chatoffline/
├── src/
│   └── main/
│       ├── java/com/github/dangelcrack/   # Lógica de la aplicación
│       └── resources/                      # FXML y recursos visuales
├── users.xml                               # Base de datos local de usuarios
├── messages.xml                            # Historial de mensajes
├── pom.xml                                 # Configuración Maven
└── README.md
```

---

## 👤 Autor

**Ángel Guerrero** — [@Dangelcrack](https://github.com/Dangelcrack)

📧 angelguerrero540@gmail.com

---
