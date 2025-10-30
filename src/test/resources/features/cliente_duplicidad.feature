@DuplicidadCliente
Feature: Validar duplicidad y coincidencias de clientes
  Como recepcionista,
  Quiero que el sistema detecte posibles clientes duplicados al registrar uno nuevo,
  Para evitar inconsistencias en la base de datos.

  Scenario: El sistema detecte posibles clientes duplicados
    Given existe un cliente registrado con los siguientes datos:
      | nombre | apellidos | documentoTipo | documentoNumero | correo | telefono |
      | Andrés | Ramírez   | CC            | 10254879        | andres@hotel.com | 3001234567 |

    When intento registrar un nuevo cliente con el mismo documento "CC" y número "10254879"
    Then el sistema debe mostrar el mensaje "Cliente ya registrado: documento duplicado"
