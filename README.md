<div align="center">

# 💬 ChatOffline

### Desktop Messaging Engine

Aplicación de escritorio desarrollada en **Java 21** y **JavaFX** que permite la comunicación local mediante almacenamiento persistente en XML. Diseñada bajo el patrón **MVC**, ofrece una experiencia de mensajería completamente **offline**, sin necesidad de servidores externos.

<br>

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-20-0A7EA4?style=for-the-badge)
![JAXB](https://img.shields.io/badge/JAXB-XML_Persistence-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

</div>

---

# 📖 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Instalación](#-instalación)
- [Funcionamiento](#-funcionamiento)
- [Persistencia de Datos](#-persistencia-de-datos)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Retos Técnicos](#-retos-técnicos)
- [Roadmap](#-roadmap)
- [Autor](#-autor)
- [Licencia](#-licencia)

---

# ✨ Características

✅ Aplicación de escritorio desarrollada con JavaFX.

✅ Comunicación completamente offline.

✅ Persistencia local mediante XML.

✅ Interfaz gráfica moderna construida con FXML.

✅ Arquitectura MVC para una clara separación de responsabilidades.

✅ Gestión local de usuarios y mensajes.

✅ Sin dependencia de servidores externos.

---

# 🚀 Tecnologías

| Tecnología | Uso |
|------------|-----|
| Java 21 | Lenguaje principal |
| JavaFX | Interfaz gráfica |
| FXML | Diseño de vistas |
| JAXB | Serialización XML |
| Maven | Gestión del proyecto |

---

# 🏗 Arquitectura

```
Usuario
    │
    ▼
Interfaz JavaFX
    │
    ▼
Controllers
    │
    ▼
Servicios
    │
    ▼
JAXB
    │
    ▼
Archivos XML
```

La aplicación sigue el patrón **MVC (Model-View-Controller)**, facilitando el mantenimiento y la escalabilidad del proyecto.

---

# 📦 Instalación

## Clonar el proyecto

```bash
git clone https://github.com/Dangelcrack/Chatoffline.git

cd Chatoffline
```

## Requisitos

- Java JDK 21 o superior
- Maven 3.x

## Ejecutar

```bash
mvn javafx:run
```

---

# 💻 Funcionamiento

La aplicación almacena toda la información de forma local:

- Usuarios registrados.
- Historial de conversaciones.
- Estado de la sesión.

Todo ello se guarda automáticamente mediante archivos XML utilizando JAXB.

---

# 💾 Persistencia de Datos

La persistencia se basa en serialización XML.

Archivos principales:

```
users.xml
messages.xml
```

Esto permite:

- Mantener conversaciones entre ejecuciones.
- Evitar dependencia de bases de datos.
- Facilitar copias de seguridad.
- Portabilidad del sistema.

---

# 📁 Estructura del Proyecto

```
Chatoffline
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.github.dangelcrack
│   │   │       ├── controller
│   │   │       ├── model
│   │   │       ├── service
│   │   │       └── Main.java
│   │   │
│   │   └── resources
│   │       └── fxml
│   │
├── data
│   ├── users.xml
│   └── messages.xml
│
└── pom.xml
```

---

# 🔧 Retos Técnicos

## 🔄 Gestión de Estado

Sincronización entre la interfaz JavaFX y los archivos XML para garantizar la persistencia de mensajes en tiempo real.

---

## 📄 Serialización JAXB

Optimización de lectura y escritura de grandes historiales de conversación mediante serialización XML.

---

## 🏛 Arquitectura MVC

Separación clara entre:

- Modelo de datos.
- Controladores.
- Interfaz gráfica.
- Servicios de persistencia.

---

# 🚧 Roadmap

- [x] Mensajería local
- [x] Persistencia XML
- [x] Arquitectura MVC
- [x] Interfaz JavaFX
- [ ] Cifrado AES de conversaciones
- [ ] Soporte multihilo
- [ ] Exportación a PDF
- [ ] Exportación a JSON
- [ ] Búsqueda de mensajes
- [ ] Temas claro/oscuro
- [ ] Notificaciones de escritorio
- [ ] Pruebas unitarias (JUnit)

---

# 👨‍💻 Autor

## Ángel Guerrero

**Java Backend & Desktop Developer**

📧 angelguerrero540@gmail.com

🐙 GitHub

https://github.com/Dangelcrack

---

# ⭐ Si este proyecto te resulta útil...

Puedes apoyar el desarrollo dejando una **⭐ Star** en el repositorio.

---

# 📄 Licencia

Este proyecto se distribuye bajo la licencia **MIT**.
