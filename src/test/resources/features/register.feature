# Archivo: registro_clientes.feature
@Clientes
Feature: Registro de clientes
  Como recepcionista
  Quiero registrar un cliente con su información personal y de contacto
  Para vincularlo a futuras reservas

  # --- Escenario de Éxito ---

  @Exitoso @Creacion
  Scenario: Registro exitoso de un cliente con datos completos
    Given un servicio para el registro de clientes está en funcionamiento y no existe previamente un cliente con CC 10254879
    When el sistema cliente solicita la creación de un nuevo cliente con los siguientes datos:
      | documentType | documentNumber | name    | lastNames | email                | phone        |
      | CC           | 10254879       | Andrés  | Ramírez   | andres.r@email.com   | 3214567890   |
    Then el registro del cliente debe ser exitoso (Código 201 Created)
    And el cliente recién creado debe tener el nombre completo "Andrés Ramírez"
    And la respuesta del sistema debe incluir un código de cliente que empiece por "CLI-"
    And el cliente debe quedar almacenado en la base de datos

  # --- Escenario de Falla por Duplicidad (Datos ya registrados) ---

  @Falla @Duplicidad
  Scenario: Intento de registro con número de documento ya existente
    Given un servicio para el registro de clientes está en funcionamiento
    And ya existe un cliente registrado con tipo y número de documento "CC" y "10254879"
    When el sistema cliente solicita la creación de un nuevo cliente con los siguientes datos:
      | documentType | documentNumber | name    | lastNames | email                | phone        |
      | CC           | 10254879       | Ana     | Pardo     | ana.pardo@email.com  | 3001112233   |
    Then el registro del cliente debe fallar (Código 409 Conflict o 400 Bad Request)
    And la respuesta del sistema debe contener un mensaje de error indicando que el cliente ya existe
    And no se debe crear un nuevo cliente en la base de datos