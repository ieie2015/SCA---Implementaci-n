# Datos de prueba del CU02

Estos archivos están preparados para importarse desde DBeaver CE usando
UTF-8, separador `,` y la primera fila como encabezado.

Importar en este orden:

1. `01_tipo_sala.csv`
2. `02_sala.csv`
3. `03_tipo_audiencia.csv`
4. `04_estado_audiencia.csv`
5. `05_tipo_participante.csv`
6. `05_participante.csv`
7. `05_solicitud_agendamiento.csv`
8. `06_disponibilidad_sala.csv`
9. `07_disponibilidad_participante.csv`
10. `08_audiencia.csv`
11. `09_audiencia_participante.csv`

Los identificadores desde 201 fueron reservados para estos datos de prueba.
Este conjunto es autoconsistente: las claves foráneas de todos los archivos
apuntan a registros definidos dentro del mismo conjunto.
Las audiencias `9201` a `9204` quedan pendientes para probar CU02.
Las audiencias `9210` y `9211` son referencias ya programadas o confirmadas.

Escenarios incluidos:

- Sala 101 disponible durante toda la jornada.
- Sala 102 disponible solo durante la mañana.
- Sala 103 fuera de servicio.
- Sala 104 con mantenimiento en una franja.
- Cuatro roles obligatorios disponibles: JUEZ, FISCAL, DEFENSOR e IMPUTADO.
- Participantes con disponibilidad completa y parcial.
- Audiencias pendiente, programada, confirmada y cancelada.
- Un intervalo que permite validar conflictos con una audiencia programada.
