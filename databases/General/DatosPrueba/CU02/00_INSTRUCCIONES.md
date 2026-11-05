# Datos de prueba del CU02

Estos archivos están preparados para importarse desde DBeaver CE usando UTF-8, separador `,` y la primera fila como encabezado.

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

Los identificadores desde 201 fueron reservados para estos datos de prueba. El conjunto es autoconsistente: las claves foráneas apuntan a registros definidos dentro del mismo conjunto.

Se generaron datos para días hábiles desde el 05/11/2026 hasta el 31/12/2026, con 36 solicitudes y 30 audiencias pendientes de planificación. Las disponibilidades incluyen jornadas completas y franjas parciales para probar la intersección de horarios, además de salas en mantenimiento u ocupadas.

Para reimportar el conjunto completo sobre una base que ya contiene estos identificadores, eliminar primero los registros anteriores de CU02 respetando el orden inverso de las claves foráneas, o importar en una base limpia.
