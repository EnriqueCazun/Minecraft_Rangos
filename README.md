# Rangos Automáticos - Plugin para Minecraft

Sistema automático de rangos basado en tiempo de juego para servidores Minecraft. Los jugadores ascienden de rango según sus horas jugadas.

---

## Características
- **Ascenso automático** por tiempo jugado.
- **Configuración 100% personalizable** (nombres, colores, requisitos de tiempo).
- **Comando `/tiempo`** para ver progreso hacia el siguiente rango.
- **Persistencia de datos** entre reinicios del servidor.
- **Soporte para colores** y formatos en nombres.
- **Actualización en tiempo real** de rangos en chat y tablist.

---

## Instalación
1. Descarga el archivo `Rangos-1.0.jar` desde la sección [Releases](https://github.com/EnriqueCazun/Minecraft_Rangos/releases/tag/1.0).
2. Coloca el archivo en la carpeta `plugins` de tu servidor.
3. Reinicia el servidor.

---

## Uso
### Comandos
- `/tiempo`  
  Muestra tu tiempo jugado, rango actual y progreso al siguiente rango.

---

## Contribuciones
Los reportes de errores y sugerencias son bienvenidos. Abre un issue para discutir cambios.

---

## Configuración
Edita el archivo `plugins/Rangos/config.yml`:

```yaml
rangos:
  rango1:
    nombre: Novato
    color: RED
    tiempo: 0  # Horas requeridas
  rango2:
    nombre: Miembro
    color: GREEN
    tiempo: 10
  rango3:
    nombre: Veterano
    color: GOLD
    tiempo: 50
