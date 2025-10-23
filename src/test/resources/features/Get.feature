# Created by juanjosearenasquintero at 23/10/25
Feature: Gestión de clientes registrados

  Scenario: Consultar, buscar y exportar la lista de clientes
    Given el usuario "catalina.lopez" con permisos de recepción ha iniciado sesión en el sistema
    When el usuario selecciona la opción "Ver clientes registrados"
    Then el sistema debe mostrar una lista con los siguientes datos de cada cliente:
      | Campo            | Descripción                       |
      | Nombre           | Nombre completo del cliente       |
      | Cédula           | Documento de identificación        |
      | Correo           | Dirección de correo electrónico   |
      | Teléfono         | Número de contacto                |
      | Fecha de registro | Fecha en que se registró el cliente |
    And el sistema debe permitir buscar clientes por cédula, nombre u otro dato relevante
    And el sistema debe permitir exportar la información en formato PDF o Excel