# Bookers

Software para la gestión de clientes, administradores, libros, inventarios, transacciones y más.
El código fuente contiene el software para ser compilador con **Amazon Java Corretto v17** (Recomendado) en back-end y el front-end.

## Font-end

El front-end de la aplicación está construida con JavaFX para mejorar la experiencia de usuario y se está utilizando la librería `bootstrapfx-core` para mejorar la experiencia del usuario.

## Back-end
El back-end está construido con Spring Boot Framework v3.2.3 con persistencia de datos PostgreSQL. El back-end se puede inicializar automáticamente desde cero con el archivo [docker-compose.yml]() que está en la carpeta [Bookers_Backend]()

#### Crear un contenedor Docker del back-end
``` bash
docker-compose up
```