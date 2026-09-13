# Arrax

> **Gestión de pedidos, inventario y ventas por encargo de carne de cerdo.**

**Arrax** es una aplicación móvil Android diseñada para digitalizar el control de pedidos de carne de cerdo por encargo, sustituyendo procesos tradicionalmente realizados mediante libretas y registros manuales.

El sistema permite administrar **lotes de producción, cortes, pedidos, clientes, inventario, gastos y resultados económicos**, manteniendo la trazabilidad desde el registro del pedido hasta su entrega y pago.

> 🚧 **Estado:** En desarrollo — MVP

---

## 🎯 Problema

La gestión de una venta por encargo de un cerdo completo puede involucrar múltiples pedidos, diferentes cortes, cantidades expresadas en kilogramos o dinero, inventario variable y gastos asociados al procesamiento.

Cuando esta información se administra manualmente, pueden aparecer problemas como:

* Pérdida o duplicación de pedidos.
* Dificultad para conocer el inventario disponible por corte.
* Errores al convertir dinero ↔ peso.
* Falta de seguimiento del estado de cada pedido.
* Dificultad para conocer cuánto se ha vendido realmente.
* Falta de visibilidad sobre gastos y utilidad final.
* Mayor complejidad durante el pesado y entrega de múltiples pedidos.

**Arrax** busca centralizar este proceso en una única aplicación móvil.

---

## 💡 Propuesta

La aplicación organiza el proceso completo:

```text
🐖 Lote
   ↓
🥩 Cortes e inventario
   ↓
🛒 Pedido
   ↓
⚖️ Pesado
   ↓
📦 Entregado
   ↓
💰 Pagado
   ↓
📊 Cierre y utilidad
```

El flujo principal de los pedidos contempla:

**Apartado → Sacado/Pesado → Entregado → Pagado**

El pago y la entrega se manejan de manera independiente, permitiendo escenarios como un pedido entregado pero pendiente de pago, o pagado por adelantado pero aún no entregado.

---

## ✨ Funcionalidades del MVP

### 🐖 Gestión de lotes

Cada cerdo vendido se representa como un **lote independiente**.

Permite registrar:

* Fecha de inicio.
* Peso total.
* Peso real o estimado.
* Estado del lote.
* Fecha de cierre.
* Cortes obtenidos durante el despiece.

Los lotes pueden permanecer **abiertos** mientras exista producto disponible y posteriormente cerrarse para generar el resumen económico.

---

### 🥩 Gestión de cortes e inventario

Cada lote cuenta con un catálogo configurable de productos.

Ejemplos:

* Carne
* Costilla
* Codillo
* Espinazo
* Chicharrón
* Manteca
* Morcilla
* Higadilla

Cada producto mantiene:

```text
Nombre
Precio por kg
Kg disponibles
Kg vendidos
```

El catálogo no está limitado a una lista fija, permitiendo agregar productos cuando sea necesario.

---

### 🛒 Gestión de pedidos

Un pedido pertenece a un cliente y a un lote.

Cada pedido puede contener múltiples líneas de producto:

```text
Producto
Cantidad
Precio
Subtotal
```

La aplicación permite introducir una cantidad de dos maneras:

```text
$ → kg
kg → $
```

Por ejemplo:

```text
Precio: $180/kg
Cliente solicita: $360

360 / 180 = 2 kg
```

También contempla múltiples pedidos realizados durante una misma visita, manteniéndolos como entidades independientes cuando corresponda.

---

### ⚖️ Cola de pesado por corte

Una de las funcionalidades centrales de Arrax es que el proceso de pesado está organizado **por tipo de corte y no por pedido**.

En lugar de:

```text
Pedido Juan
 ├── Costilla
 ├── Carne
 └── Codillo

Pedido María
 ├── Costilla
 ├── Carne
 └── Codillo
```

La aplicación puede consolidar el trabajo:

```text
COSTILLA
 ├── Juan → 2.0 kg
 ├── María → 1.5 kg
 ├── Vecino → 1.0 kg
 └── Nuera → 2.0 kg

TOTAL → 6.5 kg
```

Esto busca representar de manera más fiel el flujo físico de trabajo durante el despiece y pesado.

---

### 📦 Seguimiento del pedido

Cada línea de producto puede marcarse individualmente como pesada.

Esto permite representar pedidos parcialmente procesados:

```text
Juan
 ├── ✓ Costilla
 ├── ✓ Codillo
 └── ○ Carne
```

El pedido completo solamente pasa a **Sacado/Pesado** cuando todas sus líneas han sido confirmadas.

---

### 🎙️ Operación manos libres

La pantalla de pesado está diseñada considerando que el operador puede tener las manos ocupadas o sucias y que las condiciones de iluminación pueden dificultar la lectura.

El sistema contempla:

* Activación mediante botón físico externo.
* Comandos de voz.
* Lectura mediante texto a voz.
* Repetición de instrucciones.
* Confirmación de cantidades.
* Cambio de corte.
* Cancelación y corrección.

Ejemplo conceptual:

```text
"Costilla para Juan Pérez, dos kilos."

> Confirmar

"Pedido confirmado."

"Siguiente: María López, kilo y medio."
```

Esta funcionalidad forma parte de una fase posterior del desarrollo.

---

### 💰 Gastos

Los gastos pueden asociarse directamente con un lote.

Ejemplos:

* Carnicero.
* Insumos.
* Especias.
* Otros gastos de procesamiento.

Cada gasto registra:

```text
Concepto
Categoría
Monto
Fecha
```

---

### 📊 Cierre de lote

Al cerrar un lote, Arrax puede generar un resumen económico:

```text
Peso total
        ↓
Ventas
        ↓
Gastos
        ↓
Utilidad neta
```

La utilidad se calcula mediante:

```text
Utilidad neta = Venta total - Gastos totales
```

También se contemplan pedidos que hayan quedado pendientes de entrega o pago.

---

# 🏗️ Arquitectura

El proyecto está planteado como una aplicación Android utilizando una arquitectura orientada a separar responsabilidades entre presentación, lógica de negocio y acceso a datos.

```text
┌──────────────────────────────┐
│        Presentation          │
│                              │
│   UI / Screens / ViewModel   │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│          Domain              │
│                              │
│  Models / Business Logic     │
│  Use Cases                   │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│           Data               │
│                              │
│ Repository / Firebase        │
│ Firestore / Authentication   │
└──────────────────────────────┘
```

La separación permite mantener la lógica de negocio independiente de la interfaz y facilita la evolución del proyecto.

---

# 🧰 Tecnologías

| Tecnología                  | Uso                      |
| --------------------------- | ------------------------ |
| **Kotlin**                  | Lenguaje principal       |
| **Android**                 | Plataforma móvil         |
| **Jetpack Compose**         | Construcción de interfaz |
| **Firebase Authentication** | Autenticación            |
| **Cloud Firestore**         | Persistencia de datos    |
| **Android Studio**          | Entorno de desarrollo    |

---

# 🗄️ Modelo de datos

La persistencia utiliza **Cloud Firestore** siguiendo un modelo orientado a documentos.

Estructura conceptual:

```text
/lotes/{loteId}
    ├── productos/{productoId}
    └── gastos/{gastoId}

/clientes/{clienteId}

/pedidos/{pedidoId}
    └── lineas/{lineaId}
```

Las líneas de pedido conservan información desnormalizada del producto para facilitar las lecturas, mientras que las operaciones críticas de inventario están planteadas mediante **transacciones de Firestore** para evitar inconsistencias durante el pesado simultáneo de pedidos.

La cola consolidada por corte requiere consultas mediante `collectionGroup` sobre las líneas de pedido.

---

# 🔄 Flujo de negocio

```text
                    ┌──────────────┐
                    │     Lote     │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    Pedido    │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │   Apartado   │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    Pesado    │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │   Entregado  │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    Pagado    │
                    └──────────────┘
```

> **Nota:** `Entregado` y `Pagado` son estados independientes dentro del proceso.

---

# 📱 Pantallas principales

La primera versión contempla las siguientes áreas:

### Inicio / Lote activo

Resumen del lote actual:

* Peso.
* Inventario por corte.
* Pedidos pendientes.
* Estado general.

### Nuevo pedido

* Selección de cliente.
* Creación de cliente.
* Selección de productos.
* Conversión automática `$ ↔ kg`.
* Resumen del pedido.

### Cola de pesado

Pantalla enfocada en el trabajo operativo:

* Pedidos pendientes.
* Agrupación por corte.
* Cantidades a pesar.
* Confirmación de cada línea.
* Operación manos libres.

### Cuenta del cliente

Vista acumulada de:

* Pedidos.
* Productos.
* Cantidades.
* Importes.
* Pagos pendientes.

### Gastos

Registro de gastos asociados al lote.

### Cierre

Resumen de:

* Producción.
* Ventas.
* Gastos.
* Utilidad.
* Pedidos pendientes.

---

# 🗺️ Roadmap

## Fase 1 — Base de datos y CRUD

* [ ] Configuración de Firebase
* [ ] Modelo de datos
* [ ] Gestión de lotes
* [ ] Gestión de productos
* [ ] Gestión de clientes
* [ ] Gestión de pedidos

## Fase 2 — Lógica de negocio

* [ ] Conversión `$ ↔ kg`
* [ ] Estados de pedidos
* [ ] Inventario
* [ ] Transacciones de Firestore
* [ ] Cuenta acumulada por cliente
* [ ] Cola consolidada por corte

## Fase 3 — Finanzas

* [ ] Registro de gastos
* [ ] Cálculo de ventas
* [ ] Cálculo de utilidad
* [ ] Cierre de lote
* [ ] Resumen económico

## Fase 4 — Operación manos libres

* [ ] Botón físico Bluetooth
* [ ] Reconocimiento de voz
* [ ] Comandos operativos
* [ ] Texto a voz
* [ ] Flujo de pesado por voz

## Fase 5 — Pulido

* [ ] Alertas de inventario bajo
* [ ] Manejo de peso estimado
* [ ] Mejoras de accesibilidad
* [ ] Alto contraste
* [ ] Tipografía optimizada para exteriores
* [ ] Optimización de interacción con una sola mano

---

# 🔐 Alcance actual

Arrax está concebido inicialmente para **uso familiar e interno**, con usuarios administrativos y ayudantes.

En esta primera etapa no contempla:

* Sistema avanzado de roles y permisos.
* Reportes comparativos históricos.
* Facturación fiscal.
* Resolución avanzada de conflictos entre dispositivos.

Estas funcionalidades pueden evaluarse en versiones posteriores.

---

# 📌 Estado del proyecto

**Arrax se encuentra actualmente en desarrollo.**

El repositorio representa el proceso de construcción de un producto móvil orientado a resolver un problema operativo específico mediante software.

El objetivo no es únicamente digitalizar una libreta, sino modelar digitalmente un flujo de trabajo físico donde intervienen:

**producto → peso → inventario → pedido → entrega → pago → rentabilidad.**

---

# 👨‍💻 Sobre el proyecto

Arrax forma parte de mi portafolio de desarrollo de software y representa un ejercicio práctico de:

* Desarrollo Android.
* Diseño de interfaces móviles.
* Modelado de datos NoSQL.
* Arquitectura de software.
* Diseño de lógica de negocio.
* Gestión de inventario.
* Procesamiento de pedidos.
* Integración con servicios cloud.
* Diseño de experiencias para entornos de trabajo reales.

El proyecto se encuentra en evolución y algunas funcionalidades descritas corresponden al diseño y roadmap del producto.

---

## 📄 Documentación

La documentación de requerimientos y arquitectura se encuentra dentro del repositorio.

Consulta:

**`Markdown.md`**

para conocer con mayor detalle los requerimientos funcionales, modelo de datos, flujo de negocio y fases de desarrollo.

---

## ⚖️ Licencia

Este proyecto es de uso personal y forma parte de mi portafolio profesional.

La licencia definitiva será definida conforme avance el proyecto.
