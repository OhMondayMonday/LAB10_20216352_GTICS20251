# Mina Minadura - Juego del Muki Robertico

## Descripción
Este es un juego de buscaminas temático ambientado en la mina "Minadura" de la sierra central del Perú, donde el muki sagrado Robertico desafía a los exploradores a encontrar cobre sin activar demasiadas bombas.

## Características Implementadas

### ✅ Diseño Visual (1 punto)
- Interfaz temática de mina con colores tierra y dorado
- Efectos de explosión con animaciones CSS
- Parcelas ocultas y descubiertas con diferentes estilos
- Efectos de partículas flotantes
- Animaciones de shake y pulse

### ✅ Flujo Correcto del Juego (6 puntos)
- Carga automática de configuración desde base de datos
- Sistema de intentos (máximo 2 fallos)
- Detección de victoria y derrota
- Gestión de estado del juego en sesión

### ✅ Condición 1: Expansión sin Recursión (7 puntos)
- Implementación con algoritmo BFS (Breadth-First Search)
- Uso de cola para expandir celdas vacías
- No utiliza recursión como se requiere

### ✅ Condición 2: Detección de Bombas Vecinas (1 punto)
- Cálculo automático del número de bombas alrededor
- Visualización con colores diferenciados por número

### ✅ Condición 3: Manejo de Bombas (1 punto)
- Detección y visualización de bombas
- Reducción de intentos disponibles
- Efectos visuales de explosión

### **5. Historial de Movimientos**
- **Registro Automático**: Cada click se guarda en la base de datos
- **Información Completa**: Coordenadas, timestamp, resultado, número de bombas vecinas
- **Visualización**: Botón para ver historial completo de la partida
- **Estados Registrados**: SEGURO, BOMBA, VICTORIA, DERROTA

## Estructura del Proyecto

```
src/main/java/com/example/lab10_20216352_gtics20251/
├── entity/
│   ├── Configuracion.java       # Entidad para tabla configuracion
│   └── PosicionBomba.java       # Entidad para tabla posicionbomba
├── repository/
│   ├── ConfiguracionRepository.java
│   └── PosicionBombaRepository.java
├── dto/
│   ├── CeldaDto.java           # DTO para representar una celda
│   └── JuegoDto.java           # DTO para el estado del juego
├── service/
│   └── BuscaminasService.java   # Lógica principal del juego
└── controller/
    └── BuscaminasController.java # Controlador web
```

## Configuración y Ejecución

### Prerrequisitos
- Java 24
- MySQL Server
- Maven

### Pasos de Instalación

1. **Configurar Base de Datos**
   ```sql
   -- Ejecutar el script database_init.sql en MySQL
   source database_init.sql
   ```

2. **Configurar Conexión a Base de Datos**
   - Editar `src/main/resources/application.properties`
   - Ajustar usuario y contraseña de MySQL si es necesario

3. **Compilar y Ejecutar**
   ```bash
   .\mvnw.cmd clean install
   .\mvnw.cmd spring-boot:run
   ```

4. **Acceder al Juego**
   - Abrir navegador en: `http://localhost:8080/buscaminas`

## Configuración del Tablero
- **Dimensiones**: 6x6 (36 celdas totales)
- **Bombas**: 5 bombas distribuidas según la configuración
- **Intentos**: Máximo 2 bombas antes de perder
- **Coordenadas**: Rango válido de 1-6 para filas y columnas

## Estructura de Base de Datos

### Tabla: configuracion
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idMina | INT | ID de la configuración de mina |
| dimMinaX | INT | Dimensión X del tablero |
| dimMinaY | INT | Dimensión Y del tablero |
| cantBombas | INT | Cantidad total de bombas |
| cantIntentos | INT | Intentos máximos permitidos |
| cantIntentosActual | INT | Intentos actuales restantes |

### Tabla: posicionbomba
| Campo | Tipo | Descripción |
|-------|------|-------------|
| idBomba | INT | ID único de la bomba |
| coordenadaX | INT | Coordenada X de la bomba |
| coordenadaY | INT | Coordenada Y de la bomba |
| idMina | INT | ID de la mina a la que pertenece |

### Tabla: movimiento
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id_movimiento | INT | ID único del movimiento |
| id_mina | INT | ID de la mina relacionada |
| coordenada_x | INT | Coordenada X del movimiento (1-based) |
| coordenada_y | INT | Coordenada Y del movimiento (1-based) |
| es_bomba | BOOLEAN | Si la celda explorada era una bomba |
| numero_vecinas | INT | Número de bombas vecinas a la celda |
| fecha_movimiento | TIMESTAMP | Fecha y hora del movimiento |
| numero_movimiento | INT | Número secuencial del movimiento |
| resultado | VARCHAR(20) | Resultado del movimiento (SEGURO, BOMBA, VICTORIA, DERROTA) |

## Tecnologías Utilizadas
- **Backend**: Spring Boot 3.5.3, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Base de Datos**: MySQL
- **Construcción**: Maven
- **Estilo**: CSS3 con animaciones y efectos

## Funcionalidades del Juego
- **Sistema de Coordenadas**: Ingreso de coordenadas tipo "1 5" para explorar celdas
- **Sistema de 2 intentos**: Máximo 2 bombas antes de perder
- **Expansión automática**: Áreas sin bombas se expanden automáticamente (sin recursión)
- **Contador de bombas vecinas**: Números que indican bombas adyacentes
- **Efectos visuales**: Animaciones y cambios de color según el estado
- **Validación de entrada**: Verificación de coordenadas válidas
- **Interfaz intuitiva**: Colores distintivos para cada estado de celda
- **Registro de Movimientos**: Guarda y muestra el historial de movimientos
- **Historial de Movimientos**: Registro completo de cada click con timestamp y resultado
- **Base de Datos Completa**: Tres tablas (configuracion, posicionbomba, movimiento)
- **Trazabilidad**: Seguimiento completo de la partida para análisis posterior

## Estados de las Celdas
- **🟦 Azul claro**: Celdas sin explorar
- **🟢 Verde**: Celdas exploradas sin bombas
- **🔢 Números**: Cantidad de bombas adyacentes (1-8)
- **💣 Rosa**: Bomba encontrada
- **Vacío**: Área segura sin bombas cercanas

## Ejemplos de Coordenadas
- "1 5" - Fila 1, Columna 5
- "3 2" - Fila 3, Columna 2  
- "6 6" - Fila 6, Columna 6
- "4 3" - Fila 4, Columna 3

El juego valida automáticamente que las coordenadas estén dentro del rango válido (1-6 para ambas dimensiones) y que la celda no haya sido previamente explorada.

## Autor
Proyecto desarrollado para el curso GTICS - LAB10
Código: 20216352
