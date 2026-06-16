# Backlog del Proyecto: AutoCon

Este backlog detalla las tareas necesarias para alcanzar el MVP (Producto Mínimo Viable) de AutoCon.

## 🟢 Fase 1: Cimientos y Configuración (Semana 1-2)
- [x] Configurar estructura del proyecto Android (Settings, Build Gradle).
- [x] Configurar dependencias de Jetpack Compose y Material3.
- [ ] Implementar persistencia básica con DataStore (Toggle de activación).
- [ ] Configurar Foreground Service para la detección de llamadas en segundo plano.

## 🔵 Fase 2: Gestión de Saludos y Audio (Semana 3-4)
- [ ] Implementar UI de Selección de Saludos Predefinidos.
- [x] Implementar UI de Grabación de Saludo Personalizado (`RecordGreetingScreen`).
- [ ] Lógica de grabación de audio para saludos personalizados.
- [ ] Lógica de almacenamiento interno de archivos de audio (.m4a).

## 🟡 Fase 3: Pipeline de Contestación (Semana 5-6)
- [ ] Implementar BroadcastReceiver para detectar llamadas entrantes.
- [ ] Implementar lógica de "Auto-Answer" (requiere permisos específicos/APIs de accesibilidad o telecom).
- [ ] Flujo de Audio: Silenciar micrófono -> Reproducir saludo -> Grabar respuesta.
- [ ] Control de tiempo máximo de grabación (90s) y colgado automático.

## 🟠 Fase 4: Interfaz de Usuario y Reproducción (Semana 7)
- [ ] Pantalla Principal: Lista de mensajes recibidos (LazyColumn).
- [ ] Integración con Contactos de Android (Phonebook Lookup).
- [ ] Pantalla de Detalle/Reproductor: Controles de Play/Pause y Seekbar.
- [ ] Funcionalidad de Swipe-to-delete para mensajes.

## 🔴 Fase 5: Pulido y Despliegue (Semana 8)
- [ ] Manejo de permisos en tiempo de ejecución (Audio, Llamadas, Contactos).
- [ ] Pruebas de integración y estabilidad del servicio.
- [ ] Preparación de APK para distribución directa.
- [ ] Configuración de Google Play Console para publicación.

---
**Nota:** El progreso se actualiza conforme se completan los hitos del desarrollo.
