@Clientes
Feature: Registro de clientes
  Como recepcionista
  Quiero registrar un cliente con su información personal y de contacto
  Para vincularlo a futuras reservas

  Background:
    Given el formulario de registro de clientes

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
