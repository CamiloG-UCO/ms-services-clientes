# language: es
Característica: Consultar clientes registrados
  Como usuario con permisos de recepción
  Quiero ver los clientes registrados en el sistema
  Para poder consultar y exportar su información

  Antecedentes:
    Dado el usuario "catalina.lopez" con permisos de recepción

  Escenario: Ver lista completa de clientes registrados
    Dado que existen clientes registrados en el sistema
    Cuando seleccione la opción "Ver clientes registrados" en el sistema
    Entonces el sistema debe mostrar una lista con los datos de cada cliente
    Y la lista debe incluir nombres, cédulas, correos, teléfonos y fecha de registro

  Escenario: Buscar cliente por número de cédula
    Dado que existe un cliente con cédula "10254879"
    Cuando busque el cliente por cédula "10254879"
    Entonces el sistema debe mostrar el cliente con cédula "10254879"
    Y debe mostrar su nombre, correo, teléfono y fecha de registro

  Escenario: Buscar cliente por nombre
    Dado que existe un cliente con nombre "Mauricio"
    Cuando busque el cliente por nombre "Mauricio"
    Entonces el sistema debe mostrar los clientes que coinciden con "Mauricio"

  Escenario: Buscar cliente por correo electrónico
    Dado que existe un cliente con correo "andres@gmail.com"
    Cuando busque el cliente por correo "andres@gmail.com"
    Entonces el sistema debe mostrar el cliente con ese correo
    Y debe mostrar su nombre completo y otros datos relevantes

  Escenario: Buscar cliente por teléfono
    Dado que existe un cliente con teléfono "3214567890"
    Cuando busque el cliente por teléfono "3214567890"
    Entonces el sistema debe mostrar el cliente con ese teléfono

  Escenario: Buscar cliente que no existe
    Dado que no existe un cliente con cédula "999999999"
    Cuando busque el cliente por cédula "999999999"
    Entonces el sistema debe mostrar un mensaje indicando que no se encontró el cliente

  Escenario: Exportar lista de clientes en formato PDF
    Dado que existen clientes registrados en el sistema
    Cuando seleccione la opción "Exportar en PDF"
    Entonces el sistema debe generar un archivo PDF con la información de todos los clientes

  Escenario: Exportar lista de clientes en formato Excel
    Dado que existen clientes registrados en el sistema
    Cuando seleccione la opción "Exportar en Excel"
    Entonces el sistema debe generar un archivo Excel con la información de todos los clientes