Feature: Actualizar información del cliente
  Como recepcionista
  Quiero actualizar los datos de contacto o documento de un cliente
  Para mantener la información actualizada y evitar errores en futuras reservas.

  Scenario: Actualizar datos de contacto de un cliente existente
    Given el cliente "Andrés Ramírez" con teléfono actual "3214567890" y correo "andres@gmail.com"
    When el usuario "catalina.lopez" modifique el teléfono a "3209876543" y el correo a "aramirez@hotmail.com"
    Then el sistema debe guardar los nuevos datos
    And mostrar el mensaje "Cliente actualizado correctamente"
