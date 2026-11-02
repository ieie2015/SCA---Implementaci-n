# Datos de prueba del CU02

Estos archivos están preparados para importarse desde DBeaver CE usando
UTF-8, separador `,` y la primera fila como encabezado.

Importar en este orden:

1. `01_tipo_sala.csv`
2. `02_sala.csv`
3. `03_tipo_audiencia.csv`
4. `05_solicitud_agendamiento.csv`
5. `04_estado_audiencia.csv`
6. `05_participante.csv`
7. `06_disponibilidad_sala.csv`
8. `07_disponibilidad_participante.csv`
9. `08_audiencia.csv`
10. `09_audiencia_participante.csv`

Los identificadores desde 101 fueron reservados para estos datos de prueba.
No importar `05_tipo_participante.csv`: los tipos `JUEZ`, `FISCAL`,
`DEFENSOR`, `IMPUTADO`, `VICTIMA` y `QUERELLANTE` ya existen en la base.
Ese archivo queda como referencia de los códigos esperados.
La audiencia `9101` utiliza la solicitud de agendamiento existente `1`.
Verificar que esa solicitud exista antes de importar `08_audiencia.csv`.
Las solicitudes `101` a `104` quedan pendientes para procesarse desde CU01.
La solicitud `105` se incluye como escenario ya procesado y no debe volver
a generar una audiencia.

Escenarios incluidos:

- Sala 101 disponible durante toda la jornada.
- Sala 102 disponible solo durante la mañana.
- Sala 103 fuera de servicio.
- Sala 104 con mantenimiento en una franja.
- Participantes con disponibilidad completa, parcial y sin disponibilidad.
- Audiencias pendiente, programada, confirmada y cancelada.
- Un intervalo que permite validar conflictos con una audiencia programada.
