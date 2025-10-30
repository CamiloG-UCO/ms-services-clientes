Feature: Registro de clientes

  Scenario: Crear nuevo cliente con todos los campos requeridos
    Given un endpoint de registro de clientes está disponible en "http://localhost:8081/api/clientes"
    When se envía una solicitud POST con el cuerpo:
      | documentType | documentNumber | name    | lastNames | email                | phone        |
      | CC           | 10254879       | Andrés  | Ramírez   | andres.r@email.com   | 3214567890   |
    Then el código de respuesta debe ser 201 (Created)
    And el cliente debe ser guardado con el nombre "Andrés Ramírez"
    And el sistema debe retornar el campo "clientCode" con un valor que inicie con "CLI-"