# 🗂️ Gestor de Tareas Scrum

Sistema de gestión de tareas estilo Scrum con equipos, personas y persistencia en **MySQL**.

**Autores:** Filip Sanabria · Camilo García  
**Campus:** CampusLands  

---

## 📐 Diagramas

### Diagrama Entidad-Relación (E-R)

```
┌─────────────┐        ┌──────────────┐        ┌─────────────┐
│  TYPE_PERSON│        │    PERSON    │        │    TEAM     │
│─────────────│        │──────────────│        │─────────────│
│ PK id       │        │ PK id        │        │ PK id       │
│ nombre      │        │ nombre       │   1    │ nombre      │
│ descripcion │        │ email        │◄───────│ descripcion │
└─────────────┘        │ tipo_persona │  N     └─────────────┘
        │              │ FK id_equipo │              │
        │ 1            └──────────────┘              │
        │ N                  │ N              N      │
        └────────────────────┘               │      │
                             │          ┌────▼──────┐│
                             │          │TEAM_PERSON ││
                             │          │───────────-││
                             │          │ PK id      ││
                             │          │ FK id_team ─┘
                             │          │ FK id_person│
                             │          │ fecha_alta  │
                             │          └────────────-┘
                             │
              N              │        1
     ┌────────────────┐     │   ┌────────────┐
     │  ASSEMENT_TASK │     │   │ STATUS_TASK│
     │────────────────│     │   │────────────│
     │ PK id          │     │   │ PK id      │
     │ FK id_task     │     │   │ nombre     │
     │ FK id_person ──┘     │   │ descripcion│
     │ nota           │     │   └────────────┘
     └───────┬────────┘     │         │ 1
             │ N            │         │
             │    N         ▼         │ N
         ┌───▼──────────────────────┐
         │          TASK            │
         │──────────────────────────│
         │ PK id                    │
         │ titulo                   │
         │ descripcion              │
         │ prioridad (ALTA/MED/BAJA)│
         │ estado (POR_HACER/...)   │
         │ fecha_creacion           │
         │ fecha_limite             │
         │ FK id_equipo             │
         └──────────────────────────┘
```

---

### Diagrama UML de Clases

```
┌──────────────────────────────────────────────────────────────┐
│                       <<enumeration>>                        │
│                         TipoPersona                          │
│──────────────────────────────────────────────────────────────│
│  SCRUM_MASTER                                                │
│  PRODUCT_OWNER                                               │
│  DEVELOPER                                                   │
│  + getDescripcion(): String                                  │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│                       <<enumeration>>                        │
│                          Prioridad                           │
│──────────────────────────────────────────────────────────────│
│  ALTA  │  MEDIA  │  BAJA                                     │
│  + getDescripcion(): String                                  │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│                       <<enumeration>>                        │
│                         EstadoTarea                          │
│──────────────────────────────────────────────────────────────│
│  POR_HACER │ EN_PROCESO │ EN_REVISION │ FINALIZADO           │
│  + getDescripcion(): String                                  │
└──────────────────────────────────────────────────────────────┘

┌─────────────────────┐   1      N  ┌─────────────────────────┐
│        Team         │◄────────────│         Person          │
│─────────────────────│             │─────────────────────────│
│ - id: int           │             │ - id: int               │
│ - nombre: String    │             │ - nombre: String        │
│ - descripcion:String│             │ - email: String         │
│─────────────────────│             │ - tipo: TipoPersona     │
│ + getters/setters   │             │ - idEquipo: int         │
│ + toString()        │             │─────────────────────────│
└─────────────────────┘             │ + getters/setters       │
                                    │ + toString()            │
                                    └─────────────────────────┘
                                              │ N
                                              │
                                    ┌─────────▼───────────────┐
                                    │     AssessmentTask      │
                                    │─────────────────────────│
                                    │ - id: int               │
                                    │ - idTask: int           │
                                    │ - idPerson: int         │
                                    │ - nota: String          │
                                    │─────────────────────────│
                                    │ + getters/setters       │
                                    └─────────────────────────┘
                                              │ N
                                              │
┌────────────────────────────────────────┐   │
│                  Task                  │◄──┘
│────────────────────────────────────────│
│ - id: int                              │
│ - titulo: String                       │
│ - descripcion: String                  │
│ - prioridad: Prioridad                 │
│ - estado: EstadoTarea                  │
│ - fechaCreacion: LocalDate             │
│ - fechaLimite: LocalDate               │
│ - idEquipo: int                        │
│────────────────────────────────────────│
│ + getters/setters                      │
│ + toString()                           │
└────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    <<Singleton>>                            │
│                    ConexionMySQL                            │
│─────────────────────────────────────────────────────────────│
│ - instancia: ConexionMySQL  {volatile}                      │
│ - conexion: Connection                                      │
│ - URL, USER, PASSWORD: String  {static final}              │
│─────────────────────────────────────────────────────────────│
│ - ConexionMySQL()                                           │
│ + getInstancia(): ConexionMySQL  {static}                   │
│ + getConexion(): Connection                                 │
│ + cerrar(): void                                            │
└─────────────────────────────────────────────────────────────┘

┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌────────────────┐
│  TeamCRUD   │  │ PersonCRUD  │  │  TaskCRUD   │  │AssessmentTask  │
│─────────────│  │─────────────│  │─────────────│  │     CRUD       │
│+crear()     │  │+crear()     │  │+crear()     │  │─────────────── │
│+listarTodos │  │+listarTodos │  │+listarTodas │  │+asignar()      │
│+buscarPorId │  │+buscarPorId │  │+buscarPorId │  │+listarPorTarea │
│+actualizar()│  │+actualizar()│  │+actualizar()│  │+listarPorPerson│
│+eliminar()  │  │+eliminar()  │  │+cambiarEstdo│  │+eliminar()     │
└─────────────┘  └─────────────┘  │+eliminar()  │  └────────────────┘
                                  │+dashboard() │
                                  └─────────────┘
```

---

## 🧮 Normalización hasta 4FN

### Datos sin normalizar (0FN)
```
tarea | prioridad | estado | equipo | miembros            | roles
------|-----------|--------|--------|---------------------|------
T1    | Alta      | Activo | Alpha  | Carlos, Ana, Luis   | SM, PO, Dev
T2    | Media     | Hecho  | Beta   | Felipe, Camilo      | SM, Dev
```

### 1FN — Eliminar grupos repetitivos
- Cada celda debe tener un solo valor atómico.
- `miembros` y `roles` son multivaluados → separarlos en filas individuales.

```
id_tarea | titulo | prioridad | estado    | nombre_equipo | nombre_persona | rol_persona
---------|--------|-----------|-----------|---------------|----------------|-------------
1        | T1     | ALTA      | EN_PROCESO| Team Alpha    | Carlos García  | SCRUM_MASTER
1        | T1     | ALTA      | EN_PROCESO| Team Alpha    | Ana Martínez   | PRODUCT_OWNER
1        | T1     | ALTA      | EN_PROCESO| Team Alpha    | Luis Rodríguez | DEVELOPER
```

### 2FN — Eliminar dependencias parciales
La 1FN tiene dependencias parciales (nombre_equipo depende solo de id_equipo, no del compuesto PK).

**Tablas resultantes:**
```
TEAM(id, nombre, descripcion)
PERSON(id, nombre, email, tipo_persona, id_equipo)
TASK(id, titulo, descripcion, prioridad, estado, fecha_creacion, fecha_limite, id_equipo)
```

### 3FN — Eliminar dependencias transitivas
- `tipo_persona` en PERSON podría parecer una dependencia transitiva, pero al ser un ENUM controlado, no introduce dependencia transitiva real.
- `estado` en TASK: se mantiene como ENUM. Si se amplía la lógica, se separa en STATUS_TASK.
- Se crea `STATUS_TASK` como catálogo independiente para cumplir el modelo E-R.

**Tablas resultantes:**
```
STATUS_TASK(id, nombre, descripcion)
TYPE_PERSON(id, nombre, descripcion)
TEAM(id, nombre, descripcion)
PERSON(id, nombre, email, tipo_persona FK→type_person, id_equipo FK→team)
TASK(id, titulo, descripcion, prioridad, estado FK→status_task, fecha_creacion, fecha_limite, id_equipo FK→team)
```

### 4FN — Eliminar dependencias multivaluadas
- Un equipo puede tener muchas personas Y una persona puede pertenecer a muchos equipos → dependencia multivaluada → tabla pivote `TEAM_PERSON`.
- Una tarea puede asignarse a muchas personas Y una persona puede tener muchas tareas → `ASSEMENT_TASK`.

**Tablas en 4FN:**
```
TYPE_PERSON    (id, nombre, descripcion)
TEAM           (id, nombre, descripcion)
PERSON         (id, nombre, email, tipo_persona, id_equipo)
TEAM_PERSON    (id, id_team, id_person, fecha_alta)         ← pivote N:M
STATUS_TASK    (id, nombre, descripcion)
TASK           (id, titulo, descripcion, prioridad, estado, fecha_creacion, fecha_limite, id_equipo)
ASSEMENT_TASK  (id, id_task, id_person, nota)               ← pivote N:M
```

---

## 🗃️ Modelo Lógico

```
TYPE_PERSON
  PK id INT
     nombre VARCHAR(50) UNIQUE NOT NULL
     descripcion VARCHAR(200)

TEAM
  PK id INT
     nombre VARCHAR(100) NOT NULL
     descripcion VARCHAR(255)

PERSON
  PK id INT
     nombre VARCHAR(100) NOT NULL
     email VARCHAR(150) UNIQUE NOT NULL
     tipo_persona ENUM(SCRUM_MASTER, PRODUCT_OWNER, DEVELOPER)
  FK id_equipo → TEAM(id)

TEAM_PERSON
  PK id INT
  FK id_team → TEAM(id)
  FK id_person → PERSON(id)
     fecha_alta DATE
  UQ (id_team, id_person)

STATUS_TASK
  PK id INT
     nombre VARCHAR(50) UNIQUE NOT NULL
     descripcion VARCHAR(200)

TASK
  PK id INT
     titulo VARCHAR(150) NOT NULL
     descripcion TEXT
     prioridad ENUM(ALTA, MEDIA, BAJA) DEFAULT MEDIA
     estado ENUM(POR_HACER, EN_PROCESO, EN_REVISION, FINALIZADO)
     fecha_creacion DATE NOT NULL
     fecha_limite DATE
  FK id_equipo → TEAM(id)

ASSEMENT_TASK
  PK id INT
  FK id_task → TASK(id)
  FK id_person → PERSON(id)
     nota VARCHAR(255)
  UQ (id_task, id_person)
```

---

## 🖥️ Modelo Físico

Ver archivo `gestor_tareas.sql` — contiene el DDL completo ejecutable en MySQL.

---

## 🏗️ Estructura del proyecto

```
gestor-tareas/
│
├── src/main/java/com/gestortareas/
│   │
│   ├── models/
│   │   ├── TipoPersona.java        ← Enum: SCRUM_MASTER, PRODUCT_OWNER, DEVELOPER
│   │   ├── Prioridad.java          ← Enum: ALTA, MEDIA, BAJA
│   │   ├── EstadoTarea.java        ← Enum: POR_HACER, EN_PROCESO, EN_REVISION, FINALIZADO
│   │   ├── Team.java               ← Modelo equipo
│   │   ├── Person.java             ← Modelo persona
│   │   ├── Task.java               ← Modelo tarea (con prioridad y estado)
│   │   └── AssessmentTask.java     ← Modelo asignación tarea-persona
│   │
│   ├── persistence/
│   │   └── ConexionMySQL.java      ← Singleton + Double-Checked Locking
│   │
│   ├── crud/
│   │   ├── TeamCRUD.java           ← CRUD completo de equipos
│   │   ├── PersonCRUD.java         ← CRUD completo de personas
│   │   ├── TaskCRUD.java           ← CRUD + dashboard de tareas
│   │   └── AssessmentTaskCRUD.java ← Asignaciones tarea ↔ persona
│   │
│   └── ui/
│       └── Main.java               ← Menú de consola principal
│
├── gestor_tareas.sql               ← DDL + datos de prueba (para draw.io / MySQL)
├── pom.xml                         ← Dependencias: mysql-connector + jackson
└── README.md
```

---

## ⚙️ Conceptos del temario aplicados

| Concepto | Dónde se usa |
|---|---|
| **Enum** (5.15.4.7) | `TipoPersona`, `Prioridad`, `EstadoTarea` |
| **Singleton + Double-Checked Locking** (5.15.4.5) | `ConexionMySQL` |
| **JDBC + PreparedStatement** (5.17.5.1) | Todos los CRUD |
| **INSERT / SELECT / UPDATE / DELETE** (5.17.5.1.1-4) | `TaskCRUD`, `PersonCRUD`, etc. |
| **ArrayList + forEach + lambda** (3.8 / 5.19) | Listas retornadas por los CRUD |
| **HashMap** (3.8.4) | Dashboard de conteo por estado |
| **Switch con arrow** (Java 17) | Menú principal `Main.java` |
| **Text blocks** | Menús de consola |
| **Manejo de excepciones** (try-with-resources) | Todos los CRUD con `PreparedStatement` |
| **Interfaces / abstracción** | Cada CRUD encapsula la lógica de acceso a datos |

---

## 🚀 Cómo ejecutar

### 1. Crear la base de datos
```bash
mysql -u root -p < gestor_tareas.sql
```
O pegar el contenido de `gestor_tareas.sql` en MySQL Workbench y ejecutar.

### 2. Ajustar credenciales
En `ConexionMySQL.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/gestor_tareas?useSSL=false&serverTimezone=UTC";
private static final String USER     = "root";
private static final String PASSWORD = "123456";  // ← cambiar si es necesario
```

### 3. Compilar y ejecutar
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.gestortareas.ui.Main"
```

---

## 📊 Diagrama de flujo del sistema

```
INICIO
  │
  ▼
Menú Principal
  ├── 1. Equipos ──────► Crear / Listar / Actualizar / Eliminar
  │
  ├── 2. Personas ─────► Crear / Listar / Filtrar por equipo / Actualizar / Eliminar
  │                      (Roles: Scrum Master, Product Owner, Developer)
  │
  ├── 3. Tareas ──────► Crear / Listar / Filtrar prioridad / Filtrar estado
  │                     / Cambiar estado / Actualizar / Eliminar
  │
  ├── 4. Asignaciones ► Asignar tarea a persona / Ver asignaciones / Eliminar
  │                     (Tabla ASSEMENT_TASK — relación N:M task ↔ person)
  │
  ├── 5. Dashboard ───► Conteo de tareas por estado en tiempo real (MySQL GROUP BY)
  │
  └── 0. Salir ────────► Cierra la conexión MySQL y termina
```

---

## 📝 Notas

- El archivo `gestor_tareas.sql` puede pegarse directamente en **draw.io** (modo SQL) o en **MySQL Workbench** para generar el diagrama E-R visual automáticamente.
- La captura del diagrama generado en draw.io debe añadirse aquí como imagen.
- La conexión usa **Singleton con Double-Checked Locking** tal como se enseñó en la sección 5.15.4.5 del temario.
