# Backlog del MVP — Contestador Automático y Grabador Local (SmartAnswer)

**Proyecto:** SmartAnswer (AutoCon) — Contestador Automático Android
**Objetivo del MVP:** Implementar un contestador automático autónomo de llamadas con grabación y reproducción local de mensajes, selección de saludos predefinidos/personalizados y un programador básico de horarios activos.
**Duración:** 2026-06-15 → 2026-08-10 (~8 semanas)
**Capacidad Planificada:** 80 story points (Factor de enfoque: 75%)
**Story Points Comprometidos:** 69 SP

---

**Leyenda de Estado:**
- ⬜ : Pendiente (No iniciado)
- ⏳ : En Progreso
- ✅ : Completado
- 🚫 : Bloqueado

**Escala de Estimación (Fibonacci Modificado):**
`1` · `2` · `3` · `5` · `8` · `13`

---

## Tickets del MVP

### [MVP-G1] Configuración Base y Persistencia

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T1 | Configurar la estructura del proyecto Android con dependencias de Jetpack Compose y Material3. Ajustar `app/build.gradle` con SDK compilación 34 y mínimo 24. | 1 | Alta | Ninguna | ✅ |
| MVP-T2 | Implementar persistencia de configuración local con `DataStore Preferences` creando `DataStoreManager`. Almacenar claves: contestador activo (Boolean), tipo de saludo (Enum), ID de saludo predefinido (Int), ruta de saludo personalizado (String) y modo horario (Enum). | 2 | Alta | MVP-T1 | ⬜ |
| MVP-T3 | Crear Foreground Service (`SmartAnswerService`) y canal de notificación permanente obligatorio (`foregroundServiceType="phoneCall"`) para evitar que Android destruya la app en reposo. | 3 | Alta | MVP-T2 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [x] Entorno y dependencias de Material3 configurados correctamente
- [ ] Manager de DataStore Preferences funcionando para todas las claves de configuración
- [ ] Foreground Service creado y visible en la barra de estado con notificación persistente

---

### [MVP-G2] Gestión de Saludos y Grabación

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T4 | Crear la interfaz de grabación de saludo personalizado (`RecordGreetingScreen.kt`) en Compose con visualización de temporizador y waveform. | 3 | Alta | MVP-T1 | ✅ |
| MVP-T5 | Integrar `MediaRecorder` de Android en `RecordGreetingScreen` para grabar audio desde el micrófono en formato MPEG_4/AAC mono y guardarlo en un archivo temporal. | 3 | Alta | MVP-T4 | ⬜ |
| MVP-T6 | Implementar almacenamiento permanente en el directorio interno `/greetings/custom_greeting.m4a` al pulsar Guardar, borrando el temporal al pulsar Descartar. | 2 | Alta | MVP-T5 | ⬜ |
| MVP-T7 | Crear la UI y lógica para seleccionar y previsualizar con `MediaPlayer` uno de los 5 saludos predefinidos incorporados de fábrica. | 3 | Media | MVP-T2 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [x] UI de grabación `RecordGreetingScreen` maquetada e integrada en la actividad principal
- [ ] Grabación mediante `MediaRecorder` funcional con cronómetro activo
- [ ] Guardado físico en `/greetings/custom_greeting.m4a` e integración con persistencia
- [ ] Selección de saludos predefinidos y reproducción de prueba funcional

---

### [MVP-G3] Detección de Llamadas y Auto-Answer (Pipeline)

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T8 | Crear `CallReceiver` (BroadcastReceiver) para interceptar `TelephonyManager.ACTION_PHONE_STATE_CHANGED` y detectar transiciones entre `RINGING`, `OFFHOOK` e `IDLE`. | 2 | Alta | MVP-T3 | ⬜ |
| MVP-T9 | Implementar lógica de descolgado automático (Auto-Answer) usando `TelecomManager.acceptRingingCall()` en API 26+ y método de colgado con `endCall()`. | 5 | Alta | MVP-T8 | ⬜ |
| MVP-T10 | Crear la máquina de estados del contestador (Idle, Wait, Play, Record, Save, Error) para sincronizar el flujo de audio de la llamada (reproducción del saludo -> beep -> grabación del interlocutor). | 8 | Alta | MVP-T9, MVP-T6 | ⬜ |
| MVP-T11 | Implementar guardado automático del mensaje del interlocutor en `/messages/msg_{timestamp}.m4a` al colgar o al superar el timeout de 90 segundos. | 3 | Alta | MVP-T10 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [ ] Llamadas entrantes detectadas por el BroadcastReceiver en tiempo real
- [ ] Descolgado y colgado automático programático funcional en API 26+
- [ ] Secuencia completa de audio coordinada por la máquina de estados (Saludo -> Tono -> Grabación)
- [ ] Grabación limitada a 90 segundos máximo y guardada correctamente en almacenamiento privado

---

### [MVP-G4] Bandeja de Entrada y Contactos (UI)

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T12 | Crear la UI de la bandeja de entrada principal con `LazyColumn` para listar cronológicamente en orden inverso (más reciente arriba) los archivos en `/messages/`. | 3 | Alta | MVP-T11 | ⬜ |
| MVP-T13 | Integrar `ContactsContract.PhoneLookup` mediante `ContentResolver` para traducir números de teléfono en nombres de contactos del dispositivo (mostrar "Desconocido" si no se encuentra). | 3 | Media | MVP-T12 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [ ] Lista de mensajes cargada en tiempo real mostrando remitente, fecha, hora y duración
- [ ] Resolución de nombres de contactos de Android funcional y fluida (Phonebook Lookup)

---

### [MVP-G5] Reproductor de Mensajes y Swipe-to-Delete

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T14 | Implementar un reproductor de audio integrado para los mensajes con botones de Play/Pause y una SeekBar/Slider funcional para adelantar/retroceder. | 5 | Alta | MVP-T12 | ⬜ |
| MVP-T15 | Implementar gesto de Swipe-to-delete en los ítems de la lista y un Diálogo Modal de Confirmación previo a la eliminación física del archivo del disco. | 3 | Alta | MVP-T12 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [ ] Reproductor de audio funcional y sincronizado con la barra de progreso
- [ ] Swipe-to-delete interactivo con diálogo modal de confirmación antes de borrar físicamente

---

### [MVP-G6] Planificador de Horarios

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T16 | Implementar lógica de validación horaria (`SchedulerValidator`) para verificar si la hora y día actual corresponden a los modos: Siempre, Laboral (L-V 8-17), o Nocturno. | 3 | Media | MVP-T2 | ⬜ |
| MVP-T17 | Integrar el validador de horario en el flujo de contestación para ignorar la llamada y dejarla sonar si se encuentra fuera de la franja operativa configurada. | 2 | Alta | MVP-T16, MVP-T8 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [ ] Algoritmo de validación horaria funcionando correctamente
- [ ] El contestador se auto-pausa fuera de la franja horaria sin desactivar el servicio persistente

---

### [MVP-G7] Android — Tests Automatizados

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T18 | [Unit] Crear tests unitarios en JUnit para la clase `SchedulerValidator` comprobando límites de horas y días de la semana para los tres modos horarios. | 2 | Media | MVP-T16 | ⬜ |
| MVP-T19 | [Integration] Crear tests para la máquina de estados `CallStateMachine` simulando eventos de entrada, finalización del saludo y colgado de llamadas en segundo plano. | 5 | Alta | MVP-T10 | ⬜ |
| MVP-T20 | [Unit] Implementar tests unitarios para verificar la resolución correcta de nombres de contactos y manejo de números desconocidos. | 2 | Media | MVP-T13 | ⬜ |
| MVP-T21 | [Instrumented] Implementar pruebas instrumentadas UI con `ComposeTestRule` para validar los flujos de grabación (`RecordGreetingScreen`) y los controles de reproducción de mensajes. | 5 | Media | MVP-T14, MVP-T6 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [ ] Tests de horarios ejecutados y pasando exitosamente
- [ ] Máquina de estados validada ante fallos o interrupciones
- [ ] Cobertura de tests unitarios de lógica > 80%
- [ ] Tests instrumentados validando las pantallas clave de la aplicación

---

### [MVP-G8] Permisos y Distribución

| ID | Ticket | SP | Prioridad | Dependencias | Estado |
|----|--------|----|-----------|--------------|--------|
| MVP-T22 | Implementar flujo dinámico e interactivo de solicitud de permisos requeridos en tiempo de ejecución (`RECORD_AUDIO`, `READ_PHONE_STATE`, `ANSWER_PHONE_CALLS`, `READ_CONTACTS`, `POST_NOTIFICATIONS`) antes de encender el contestador. | 3 | Alta | MVP-T12 | ⬜ |
| MVP-T23 | Configurar firmas de release y compilar el archivo APK de producción listo para distribución directa (sideloading). | 2 | Media | MVP-T12 | ⬜ |

**Criterios de Aceptación del Grupo:**
- [ ] Diálogo explicativo inicial y petición de permisos correcta en tiempo de ejecución
- [ ] APK compilada de forma optimizada y firmada correctamente para producción

---

## Resumen del Sprint (Plan de Trabajo MVP)

| Métrica | Valor |
|---------|-------|
| Total de Tickets | 23 |
| Total Story Points | 69 SP |
| Tickets Alta Prioridad | 15 |
| Tickets Media Prioridad | 8 |
| Tickets Baja Prioridad | 0 |

---

## Definition of Done (DoD)
- [ ] Código implementado en Kotlin y Compose respetando las guías de estilo del proyecto
- [ ] Pruebas unitarias e instrumentadas pasando sin errores en el emulador/dispositivo
- [ ] Verificación manual del flujo completo del contestador en llamadas reales o simuladas
- [ ] Limpieza de recursos y memoria en MediaRecorder y MediaPlayer implementada (`release()`)
- [ ] Revisión del cumplimiento de las políticas de permisos de Google Play
- [ ] Documentación actualizada y código comentado en las secciones de arquitectura y pipeline
