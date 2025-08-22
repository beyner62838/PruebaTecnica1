# API de Gestión Bancaria - Documentación

## Requisitos previos
- Java 21
- Maven
- PostgreSQL

## Configuración
1. Clona el repositorio:

### git clone https://github.com/beyner62838/PruebaTecnica1.git
### cd PruebaTecnica1
### Configura la base de datos PostgreSQL en application.properties
### Es importante Crear la base de Datos en pgAdmin 
### Compila el proyecto:
## mvn clean install
## mvn spring-boot:run
## Una vez que la aplicación esté en ejecución, puedes acceder a la documentación de la API en:

### http://localhost:8080 

### Endpoints principales
## Clientes
### GET /api/clientes: Obtener todos los clientes
### GET /api/clientes/{id}: Obtener un cliente por ID
### POST /api/clientes: Crear un nuevo cliente
### PUT /api/clientes/{id}: Actualizar un cliente existente
### DELETE /api/clientes/{id}: Eliminar un cliente
## Cuentas
### GET /api/cuentas: Obtener todas las cuentas
### GET /api/cuentas/{id}: Obtener una cuenta por ID
### POST /api/cuentas: Crear una nueva cuenta
### PUT /api/cuentas/{id}: Actualizar una cuenta existente
### DELETE /api/cuentas/{id}: Eliminar una cuenta
## Transacciones
### POST /api/transacciones/consignacion: Realizar una consignación
### POST /api/transacciones/retiro: Realizar un retiro
### POST /api/transacciones/transferencia: Realizar una transferencia
### GET /api/transacciones: Obtener todas las transacciones
### GET /api/transacciones/{id}: Obtener una transacción por ID
## Notas importantes
Todos los endpoints requieren que los datos se envíen en formato JSON
Las operaciones monetarias utilizan BigDecimal para precisión en los cálculos
Los clientes deben ser mayores de edad para registrarse
Cada transacción se realiza en una operación atómica (transaccional)
