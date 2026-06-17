# Especificación de Arquitectura y Guía de MVP: SmartAnswer

Este documento describe el stack tecnológico, la arquitectura del software, la estructura lógica de la aplicación, una historia de usuario de referencia y la secuencia de pasos atómicos para implementar el MVP (Producto Mínimo Viable) de SmartAnswer.

---

## 🛠️ 1. Stack Tecnológico

La aplicación se construirá utilizando tecnologías nativas de Android modernas, optimizadas para rendimiento y mantenibilidad local sin dependencias en la nube:

*   **Lenguaje:** Kotlin (v1.9.x o compatible).
*   **Interfaz de Usuario (UI):** Jetpack Compose con Material3 para un diseño dinámico, responsivo y adaptado al tema oscuro (`Color(0xFF0F171B)`).
*   **Persistencia de Configuración:** DataStore Preferences (de Jetpack) para almacenamiento clave-valor ligero del estado del contestador y horarios de operación.
*   **Manejo de Concurrencia:** Kotlin Coroutines y Flow para programación asíncrona no bloqueante (lecturas de DataStore, flujos de audio).
*   **Servicio en Segundo Plano:** Foreground Service (`SmartAnswerService`) asociado con el tipo `phoneCall` para garantizar que Android mantenga activa la detección de llamadas incluso cuando el dispositivo está bloqueado.
*   **Detección y Control de Llamadas:**
    *   `BroadcastReceiver` para escuchar `TelephonyManager.ACTION_PHONE_STATE_CHANGED`.
    *   `TelecomManager` para descolgar (`acceptRingingCall()`) y colgar (`endCall()`) llamadas programáticamente en Android 8.0+ (API 26+).
*   **Procesamiento de Audio:**
    *   `MediaPlayer` de Android para reproducir el saludo de salida al interlocutor.
    *   `MediaRecorder` de Android para grabar saludos y mensajes entrantes en formato MPEG-4 (`.m4a`) con codificación AAC a una frecuencia adecuada para voz.
*   **Acceso a Datos del Sistema:** `ContentResolver` para realizar consultas seguras a `ContactsContract.PhoneLookup` y resolver contactos locales.

---

## 🏗️ 2. Arquitectura de Software

La aplicación sigue el patrón de diseño **MVVM (Model-View-ViewModel)** combinado con los principios de **Clean Architecture** estructurados en tres capas principales. Esto aísla la lógica de negocio de los componentes de Android y la interfaz de usuario:

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           Presentación (UI)                             │
│       [Compose Screens] ◄──► [ViewModels] ◄──► [UiState/Events]         │
└────────────────────────────────────┬────────────────────────────────────┘
                                     ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                 Domain                                  │
│             [Use Cases] ◄──► [Models] ◄──► [Repositories Interfaces]    │
└────────────────────────────────────┬────────────────────────────────────┘
                                     ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                  Data                                   │
│        [DataStoreManager] ◄──► [FileStorage] ◄──► [Repository Impls]    │
└─────────────────────────────────────────────────────────────────────────┘
```

### Capas del Proyecto:
1.  **Presentación (Presentation):**
    *   Contiene las pantallas de Compose (`RecordGreetingScreen`, `MainScreen`) y los `ViewModels` correspondientes.
    *   Los `ViewModels` exponen flujos de estado (`StateFlow`) para ser consumidos por la UI y reaccionan a los eventos del usuario llamando a los casos de uso.
2.  **Dominio (Domain):**
    *   Es la capa más interna y pura (no depende del framework de Android).
    *   Define las reglas de negocio en los **Casos de Uso** (ej: `ValidateScheduleUseCase`, `ProcessIncomingCallUseCase`).
    *   Define interfaces de Repositorios e interfaces de controladores de audio genéricos.
3.  **Datos (Data):**
    *   Implementa las interfaces de los repositorios de la capa de Dominio.
    *   Maneja la lectura/escritura de configuraciones locales (`DataStoreManager`) y los archivos de audio en el almacenamiento interno (`Context.filesDir`).
4.  **Integraciones del Sistema (System Integrations):**
    *   Contiene el `Foreground Service`, el `BroadcastReceiver` y las utilidades del sistema de telefonía. Estas actúan como desencadenadores externos que interactúan con la capa de datos y dominio.

---

## 📂 3. Estructura del Proyecto

Físicamente, el código Kotlin se organizará dentro del paquete `com.smartanswer.app` estructurado por capas:

```
app/src/main/kotlin/com/smartanswer/app/
│
├── MainActivity.kt                 # Punto de entrada de la app, inicializa la UI
│
├── data/                           # Capa de Datos
│   ├── local/
│   │   ├── DataStoreManager.kt     # Manejo de persistencia de configuraciones
│   │   └── FileHelper.kt           # Utilidades para almacenar y recuperar .m4a
│   └── repository/
│       ├── SettingsRepositoryImpl.kt
│       └── MessageRepositoryImpl.kt
│
├── domain/                         # Capa de Dominio (Pura)
│   ├── model/
│   │   ├── VoiceMessage.kt         # Entidad que representa un mensaje grabado
│   │   └── SchedulerMode.kt        # Enum (ALWAYS, BUSINESS, NIGHT)
│   ├── repository/
│   │   ├── SettingsRepository.kt
│   │   └── MessageRepository.kt
│   └── usecase/
│       ├── GetMessagesUseCase.kt
│       ├── DeleteMessageUseCase.kt
│       ├── SaveCustomGreetingUseCase.kt
│       └── ValidateScheduleUseCase.kt # Valida si la hora actual entra en el scheduler
│
├── ui/                             # Capa de Presentación
│   ├── main/
│   │   ├── MainScreen.kt           # Bandeja de entrada y reproductor
│   │   └── MainViewModel.kt        # Gestión del estado de grabaciones y reproducción
│   ├── record/
│   │   ├── RecordGreetingScreen.kt # Pantalla de grabación de saludo personalizado
│   │   └── RecordGreetingViewModel.kt
│   ├── theme/                      # Definiciones de colores, tipografías y estilos
│   │   ├── Theme.kt
│   │   ├── Color.kt
│   │   └── Type.kt
│   └── components/                 # Componentes Compose reutilizables (timers, etc.)
│
└── service/                        # Integraciones de Sistema Android
    ├── SmartAnswerService.kt       # Foreground Service para detección en segundo plano
    ├── CallReceiver.kt             # BroadcastReceiver para ACTION_PHONE_STATE_CHANGED
    └── CallStateMachine.kt         # Orquestador del flujo del contestador (State Machine)
```

---

## ⚙️ 4. Arquitectura Lógica y Pipeline de Contestación

El contestador automático opera como una máquina de estados finitos que se ejecuta dentro del `SmartAnswerService` para asegurar que el sistema operativo no mate la tarea.

### Diagrama del Ciclo de Vida del Contestador (Máquina de Estados)

```
       ┌───────────────┐
       │     IDLE      │◄─────────────────────────────────────────────┐
       └───────┬───────┘                                              │
               │ (Evento RINGING)                                     │
               ▼                                                      │
       ┌───────────────┐                                              │
       │     WAIT      │──(Llamada rechazada/cortada)────────────────►│
       └───────┬───────┘                                              │
               │ (Llamada auto-descolgada)                            │
               ▼                                                      │
       ┌───────────────┐                                              │
       │     PLAY      │──(Error / Corte abrupto)────────────────────►│
       └───────┬───────┘                                              │
               │ (Fin de saludo + Tono bip)                           │
               ▼                                                      │
       ┌───────────────┐                                              │
       │    RECORD     │──(Timeout 90s o colgado interlocutor)───────►│
       └───────┬───────┘                                              │
               │ (Captura de fin de llamada)                          │
               ▼                                                      │
       ┌───────────────┐                                              │
       │     SAVE      │──────────────────────────────────────────────┘
       └───────────────┘
```

### Flujo de Interacción Lógica:
1.  **Monitoreo (`IDLE`):** El `SmartAnswerService` corre en segundo plano. `CallReceiver` intercepta una llamada entrante (`RINGING`).
2.  **Verificación (`WAIT`):** El servicio lee los datos de configuración. Si el contestador está activo y `ValidateScheduleUseCase` confirma que se encuentra dentro de la franja horaria configurada, se inicia el descolgado automático.
3.  **Auto-Answer (`PLAY`):**
    *   El servicio utiliza `TelecomManager` para descolgar la llamada.
    *   Se configura la ruta de audio. `MediaPlayer` reproduce el saludo del usuario (de fábrica o personalizado) directamente en la salida de audio de la llamada.
4.  **Grabación (`RECORD`):**
    *   Al terminar el saludo, se emite un tono tipo "beep".
    *   Se activa `MediaRecorder` tomando el audio del flujo telefónico y se guarda en un archivo temporal.
    *   Un temporizador de seguridad de 90 segundos corre de manera paralela.
5.  **Guardado y Finalización (`SAVE`):**
    *   Si el temporizador llega a 90 segundos o si `CallReceiver` detecta que el estado de la telefonía pasa a `IDLE` (el llamante colgó), el servicio detiene la grabación.
    *   Si es por timeout, el servicio ejecuta `TelecomManager.endCall()` para cortar.
    *   El archivo temporal `.m4a` se mueve a la carpeta interna `/messages/` asignándole un nombre único basado en fecha/hora (`msg_yyyyMMdd_HHmmss.m4a`).
    *   El servicio retorna al estado `IDLE`.

---

## 👤 5. Historia de Usuario

### Historia de Usuario de Referencia
**COMO** un usuario hispanohablante que no tiene contratado o configurado el servicio de buzón de voz de su operador telefónico,
**QUIERO** una aplicación móvil local que conteste automáticamente mis llamadas entrantes cuando estoy ocupado, reproduzca un saludo explicativo y grabe el mensaje de voz del remitente,
**PARA** no perder avisos urgentes (familiares, médicos o de trabajo) sin costo adicional y con control total sobre mis horarios de disponibilidad.

### Criterios de Aceptación:
1.  **Configuración del Estado:**
    *   La pantalla principal debe tener un botón (Switch) para activar/desactivar la contestación automática de forma persistente.
    *   Se debe mostrar una notificación persistente y visible en la barra de estado del dispositivo mientras la aplicación esté activa esperando llamadas.
2.  **Personalización de Saludos:**
    *   El usuario debe poder elegir un saludo predefinido de una lista de 5 opciones genéricas.
    *   El usuario debe poder grabar su propio saludo a través del micrófono en la pantalla `RecordGreetingScreen`, con controles de iniciar, detener, escuchar la previsualización y guardar.
3.  **Gestión de Horarios:**
    *   El usuario debe poder configurar un horario operativo:
        *   *Siempre activo:* Responde todas las llamadas.
        *   *Laboral:* Solo contesta llamadas de lunes a viernes entre las 08:00 y las 17:00.
        *   *Reposó:* Contesta llamadas por las noches (17:01 a 07:59) y fines de semana completos.
    *   Si entra una llamada fuera de la franja activa configurada, el contestador debe ignorar la llamada permitiendo que suene de manera regular.
4.  **Bandeja de Entrada:**
    *   La pantalla principal debe listar cronológicamente todos los mensajes grabados (el más reciente arriba).
    *   Cada fila de la lista debe mostrar: nombre del remitente (si se encuentra registrado en los contactos del teléfono), número de teléfono, fecha y hora de la llamada, y duración del audio.
5.  **Reproductor Integrado:**
    *   Al presionar un mensaje de voz de la lista, se debe abrir un reproductor (o módulo integrado) con botones de Play/Pause y un deslizador (SeekBar/Slider) funcional que muestre el progreso de la reproducción offline.
6.  **Eliminación de Grabaciones:**
    *   El usuario debe poder eliminar mensajes deslizando el ítem o pulsando un botón de eliminar.
    *   El sistema debe mostrar un cuadro de confirmación (Modal Dialog) antes de eliminar permanentemente el archivo físico del almacenamiento del dispositivo.

---

## 🎯 6. Pasos Atómicos para la Implementación del MVP

La implementación del MVP debe realizarse de forma secuencial y ordenada para garantizar que la aplicación mantenga su estabilidad funcional en cada etapa:

1.  **Paso 1: Definición de Permisos y Configuración del Proyecto**
    *   Modificar `AndroidManifest.xml` agregando los permisos requeridos:
        *   `android.permission.READ_PHONE_STATE` (para detectar llamadas).
        *   `android.permission.ANSWER_PHONE_CALLS` (para descolgar llamadas).
        *   `android.permission.RECORD_AUDIO` (para micrófono).
        *   `android.permission.READ_CONTACTS` (para buscar nombres).
        *   `android.permission.POST_NOTIFICATIONS` (necesario en Android 13+ para el Foreground Service).
        *   `android.permission.FOREGROUND_SERVICE` y `android.permission.FOREGROUND_SERVICE_PHONE_CALL`.
    *   Crear los directorios del paquete (`data`, `domain`, `ui`, `service`) según la estructura del proyecto.
2.  **Paso 2: Persistencia de Ajustes con DataStore**
    *   Crear la clase `DataStoreManager` en `data/local/` utilizando preferencias clave-valor.
    *   Implementar métodos de lectura y escritura no bloqueantes para: `isContestadorActive`, `selectedGreetingType`, `selectedPredefinedGreetingId`, `customGreetingPath` y `schedulerMode`.
3.  **Paso 3: Esqueleto del Foreground Service y Ciclo de Vida**
    *   Crear `SmartAnswerService` heredando de `Service`.
    *   Implementar la creación de un canal de notificación persistente (`NotificationChannel`) con prioridad por defecto/alta.
    *   Registrar el servicio en `AndroidManifest.xml`.
    *   Vincular el inicio del servicio al cambio de estado de `isContestadorActive` en DataStore, verificando dinámicamente si los permisos ya han sido otorgados.
4.  **Paso 4: Receptor de Eventos Telefónicos y Validador de Horario**
    *   Crear `CallReceiver` heredando de `BroadcastReceiver`.
    *   Registrarlo dinámicamente en `SmartAnswerService` para capturar `ACTION_PHONE_STATE_CHANGED` únicamente mientras el servicio esté activo.
    *   Crear `ValidateScheduleUseCase` en la capa de dominio que tome la hora del sistema y determine si el contestador debe actuar según la regla de horario configurada.
5.  **Paso 5: Lógica de Grabación del Saludo Personalizado**
    *   En `RecordGreetingScreen`, enlazar los botones "Grabar/Detener" con el `MediaRecorder` de Android.
    *   Guardar el archivo de audio grabado en la ruta `/greetings/custom_greeting.m4a` dentro del almacenamiento interno de la app (`Context.filesDir`).
    *   Actualizar en `DataStoreManager` la ruta del archivo y habilitar la opción de reproducción de prueba para el usuario en la UI.
6.  **Paso 6: Orquestación del Pipeline de Auto-Respuesta y Grabación**
    *   Implementar `CallStateMachine` para manejar los estados `IDLE`, `WAIT`, `PLAY`, `RECORD`, `SAVE`.
    *   En el estado `PLAY`: Descolgar llamada usando `TelecomManager.acceptRingingCall()` y reproducir el saludo usando `MediaPlayer` dirigiendo el audio a la llamada.
    *   Al terminar `PLAY`: Emitir un tono beep sintético y transicionar a `RECORD`.
    *   En el estado `RECORD`: Inicializar `MediaRecorder` para capturar el audio de la llamada durante un máximo de 90 segundos.
7.  **Paso 7: Detección de Colgado y Guardado de Mensajes**
    *   Monitorear la transición a `IDLE` en `CallReceiver`.
    *   Al colgar (el remitente) o al cumplirse el timeout (colgar programáticamente llamando a `TelecomManager.endCall()`), detener `MediaRecorder`.
    *   Guardar el archivo final en `/messages/msg_{timestamp}.m4a`.
8.  **Paso 8: Bandeja de Entrada Principal y Consulta de Contactos**
    *   Crear la pantalla `MainScreen` en Jetpack Compose con una lista (`LazyColumn`) ordenada cronológicamente de forma inversa.
    *   Crear un helper que use `ContactsContract` para traducir el número del llamante a un nombre del directorio telefónico local de Android.
9.  **Paso 9: Módulo Reproductor de Mensajes**
    *   Crear una tarjeta o diálogo en la parte inferior de la pantalla principal que funcione como reproductor.
    *   Instanciar `MediaPlayer` para reproducir los archivos de audio seleccionados.
    *   Vincular el SeekBar/Slider de Compose con el progreso actual del `MediaPlayer` de forma interactiva (permitiendo adelantar/retroceder la reproducción).
10. **Paso 10: Eliminación con Confirmación y Solicitud Dinámica de Permisos**
    *   Implementar la acción de swipe o el botón para borrar un mensaje.
    *   Crear un diálogo de confirmación `AlertDialog` en Compose. Si el usuario confirma, borrar físicamente el archivo del disco.
    *   Crear la lógica de solicitud y flujo explicativo de permisos en la pantalla de inicio para garantizar que el usuario acepte todos los permisos necesarios antes de activar el servicio.
