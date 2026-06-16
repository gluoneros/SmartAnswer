# PLANTILLA COMPACTA -- MVP

**Proyecto:** AutoCon -- Contestador Automatico Android
**F-Inicio:** 2026-06-15 --- **F-Meta:** 2026-08-31 (~8 semanas)
**Obj:** Contestar automatica llamadas Android; grabar repuestas; reproducirlas orden temporal inverso (mas reciente arriba).

---

Resumen Ejecutivo

**Problema:** En muchos pais latinoamericanos, planes mobile basic
carecen acceso buzon voz estandar operador. Usuarios pierden
llamadas important cuando estan ocupados o durmiendo sin forma
recuperar informacion dejadas personas contacto. El impacto
afecta ambitos laboral familiar urgente medico diariamente.

**Solucion Propuesta:** Aplicacion nativa Android captura evento
llamada entrante reproduce un saludo configurado graba respuesta
del otro lado despues tono. Todo queda registrado lista con
reproductor integr disponible instantaneo offline local.

**Propuesta Valor -- Estructur Standard:**
Para usuar hispanoabl sin serv buzon operad local, AutoCon
entrega solucion autonoma gratuida portatil indpendiente
sin suscrip ni dependencia infraestructur external terceros.

**Public Objetivo Early Adoptrs:**
- Conductores profes entrega rapida campo abierto
- Tecnicos terreno instalaciones remot difusa geografic
- Padre joven descansando cerca cel nocturno urgentia fam
- Independient homeoffice recepcion cli com durante almuerzo
- Vendedor campo visit comercios ruta extensa diaria

---

Funcionalidades Incluye Scope M V P

1. **Toggle Activar Desactiv Contest Auto**
Estado encender apagar persis tras reinicio device via
DataStore Preferences Protobuff key-value. Mientras modo
activo dispo idle repos detect ingreso call activa flujo.
Foreground Service notif visible oblig requer anti-sys kill.

2. **Seleccion Saludo Predefinido**
Menu present ~5 frases saludo esp incorpor fabrica tipo:
"Hola yo no puedo atender deja mensaje tono". Us sel cual
us standart default greet call incoming flow system playback.

3. **Grabar Saludo Personalizado Usuario**
Opcion grab propio salut usa mic device input. Reemplaza
defecto seleccion usuario activa modo custom. Ambas opcions
coexist como alterna selectable pero solo una activa simultanea
moment dado fluj exec. Configur persistence DataStore Proto.

4. **Pipeline Completo Contestaci Auto**
Detec incoming --> pausa conext canal voc --> play greeting
archivo --> start recor response interlo --> halt hangup detect
OR tmax timeout default ninety sec --> save file intern priv path
app Storage. Pipeline coordinado state-mach Idle Wait Play Rec Save
Comp Error. MediaRecorder AAC MP4 output mono channel config.

5. **Panel Grabacion Lista Cronol Reprod Principal**
Home Screen Jetpack Compose lista invers tempo newest first cada
fila muestra fecha completa hora minuto numero telefono asociado
nombre guardar contacts phonebook lookup o etiqueta Desconoc
cuando falta registro direc identific known identity person. Tap row
open detalle full-screen replay module controles Play Paused
seekbar progress time elapsed over remaining total duration display.

6. **Eliminar Entrada Individual Confirma Modal previo borrar definitivo**
Swipe horizontal izquierda o icon trash visible cada item requiere
confirmacion dialog modal antes removal permanent local storage
audio file delete disk space reclaim immediately after action done ok yes.

7. **Horario Basica Operaci Activ Mode**
Opciones simple limitada siemprs activo sin restrici temp;
horario labor standar lunes viernes ocho diecisiete; nocturno
repos fuera labor noche madrugada temprano manana hasta dia
siguiente siguiente semana ciclo semanal recurrent periodic auto.
Pausa temporaria automatico fuera franja horario sin necesidad reiniciar modo contestador. Se mantiene persistencia estado almacenado.
STOP_CASCADE_HERE
