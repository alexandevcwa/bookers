# Bookers

Software para la gestion de clientes, administradores, libros, inventarios, transacciones y más.
El código fuente contiene el software para ser compilador con **Amazon Java Corretto v17** (Recomendado) en backend y el frontend.

## Fontend

El frontend de la aplicación esta construida con JavaFX para mejorar la experiencia de usuario y se esta utilizando la libreria `bootstrapfx-core` para mejorar la experiencia del usuario

## Backend
El backend esta construido con Spring Boot Framework v3.2.3 con persistencia de datos PostgreSQL, el backend se puede inicializar automaticamente desde cero con el archivo [docker-compose.yml]() que esta en la carpeta [Bookers_Backend]()

#### Crear un contenedor docker del backend
``` bash
docker-compose up
```