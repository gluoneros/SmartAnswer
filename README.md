# AutoCon - Contestador Automático Inteligente

AutoCon es una aplicación nativa para Android diseñada para gestionar llamadas perdidas de forma autónoma. Ideal para usuarios en regiones donde los servicios de buzón de voz del operador son limitados o costosos.

## 🚀 Propuesta de Valor
Para usuarios que no cuentan con servicio de buzón de voz local, AutoCon entrega una solución autónoma, gratuita y portátil que funciona sin conexión a internet ni dependencia de terceros.

## ✨ Funcionalidades Principales (MVP)
*   **Contestador Automático:** Activa/desactiva el modo contestador con persistencia tras el reinicio.
*   **Gestión de Saludos:**
    *   Selección de saludos predefinidos de fábrica.
    *   Grabación y uso de saludos personalizados.
*   **Pipeline de Contestación:**
    *   Detección de llamadas entrantes.
    *   Reproducción automática del saludo.
    *   Grabación del mensaje del llamante (hasta 90 segundos).
*   **Panel de Mensajes:**
    *   Lista cronológica (más reciente primero) de grabaciones recibidas.
    *   Identificación de contactos: Muestra el nombre del contacto o "Desconocido".
    *   Reproductor integrado con controles y barra de progreso.
*   **Privacidad y Almacenamiento:** Grabaciones guardadas localmente en el almacenamiento privado de la app.

## 🛠️ Stack Tecnológico
*   **Lenguaje:** Kotlin
*   **UI:** Jetpack Compose (Moderno y Reactivo)
*   **Persistencia:** DataStore (Preferences/Proto) para configuraciones.
*   **Audio:** MediaRecorder (AAC/MP4) y MediaPlayer.
*   **Servicios:** Foreground Service para evitar que el sistema detenga el proceso.

## 📲 Instalación y Distribución
*   **Distribución Directa:** APK disponible en el repositorio.
*   **Tienda:** Próximamente en Google Play Store.

## 🛠️ Desarrollo
La arquitectura sigue principios de Clean Architecture y MVVM para asegurar mantenibilidad y escalabilidad.

---
© 2026 AutoCon Project. Todos los derechos reservados.
