# Examen_Mobile

El proyecto es un login con conexión a firebase, el cual tiene dos metodos de autentificación por email y password y con la api de google.

La aplicación cuenta con:
  - Login
  - Registro
  - Menu
  - Servicio
  - Consumo de api (google)

En el Login esta compuesto por dos campos edittext donde el usuario puede ingresar el correo y la contraseña, cuenta con una opción para registrarse

En el registro esta compuesto por cuatro campos edittext para almacenar el nombre, corre, telefono y contraseña para crear un usuario nuevo y almacenarlo
en una base de datos de Firebase

En el menu cuenta con un apartado donde muestra el nombre y el correo del usuario que sea a logueado sea por correo y contraseña o por google, también
cuenta con un botón que permite encender y apagar la linterna y un boton para cerrar sesión que verifica si la sesión fue iniciada por correo y contraseña o por google

En el servicio implementamos un servicio para saber si el dispositivo tiene conexión a internet, lo usamos para validar el login, si no tiene conexión le
muestra al usuario una ventana donde le explica que por favor revise su conexión.
