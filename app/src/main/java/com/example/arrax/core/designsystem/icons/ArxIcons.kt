package com.example.arrax.core.designsystem.icons

import com.example.arrax.R

/**
 * Catálogo central de iconos de ARRAX.
 *
 * Los nombres representan la intención del icono dentro de la aplicación,
 * no necesariamente el nombre físico del drawable.
 *
 * Ejemplo:
 * ArraxIcons.Search
 * ArraxIcons.Order
 * ArraxIcons.Inventory
 *
 * Esto permite cambiar el recurso gráfico sin modificar las pantallas.
 */
object ArxIcons {

    // ============================================================
    // NAVEGACIÓN
    // ============================================================

    /** Regresa a la pantalla anterior. */
    val Back = R.drawable.ic_arrow_back

    /** Avanza a la siguiente pantalla o sección. */
    val Forward = R.drawable.ic_arrow_right

    /** Cierra una pantalla, diálogo o elemento temporal. */
    val Close = R.drawable.ic_close

    /** Abandona el flujo o pantalla actual. */
    val Exit = R.drawable.ic_goout

    // ============================================================
    // ACCIONES PRINCIPALES
    // ============================================================

    /** Crea un nuevo registro u operación. */
    val Add = R.drawable.ic_fab_add

    /** Escanea un código QR. */
    val Qr = R.drawable.ic_fab_qr

    /** Confirma una acción u operación. */
    val Check = R.drawable.ic_check

    /** Guarda los cambios realizados. */
    val Save = R.drawable.ic_save

    /** Edita un registro existente. */
    val Edit = R.drawable.ic_edit

    /** Elimina un registro. */
    val Delete = R.drawable.ic_delete

    /** Actualiza la información mostrada. */
    val Refresh = R.drawable.ic_refresh

    // ============================================================
    // NAVEGACIÓN PRINCIPAL
    // ============================================================

    /** Inicio — estado activo. */
    val HomeFilled = R.drawable.ic_nav_home_filled

    /** Inicio — estado inactivo. */
    val HomeOutline = R.drawable.ic_nav_home_outline

    /** Dashboard / resumen operativo — activo. */
    val DashboardFilled = R.drawable.ic_nav_dashboard_filled

    /** Dashboard / resumen operativo — inactivo. */
    val DashboardOutline = R.drawable.ic_nav_dashboard_outline

    /** Finanzas — activo. */
    val FinanceFilled = R.drawable.ic_nav_finance_filled

    /** Finanzas — inactivo. */
    val FinanceOutline = R.drawable.ic_nav_finance_outline

    /** Proyecciones / estimaciones — activo. */
    val EstimationFilled = R.drawable.ic_nav_estimation_filled

    /** Proyecciones / estimaciones — inactivo. */
    val EstimationOutline = R.drawable.ic_nav_estimation_outline

    /** Perfil del usuario. */
    val Profile = R.drawable.ic_nav_profile_outline

    /** Actividades o tareas operativas. */
    val Tasks = R.drawable.ic_nav_tasks_outline

    /** Equipo — estado activo. */
    val TeamFilled = R.drawable.ic_nav_team_filled

    /** Equipo — estado inactivo. */
    val TeamOutline = R.drawable.ic_nav_team_outline

    // ============================================================
    // OPERACIÓN
    // ============================================================

    /** Representa un lote de productos. */
    val Lot = R.drawable.ic_stacks

    /** Representa un saco o suministro de alimento/material. */
    val Sack = R.drawable.ic_sack_outline

    /** Representa crecimiento o incremento de producción. */
    val Growth = R.drawable.ic_growth

    /** Representa disminución o decremento. */
    val Decrease = R.drawable.ic_decrement

    /** Representa peso o cantidad medida. */
    val Weight = R.drawable.ic_weight

    /** Representa una advertencia operativa. */
    val Warning = R.drawable.ic_warning

    // ============================================================
    // PEDIDOS
    // ============================================================

    /**
     * Producto solicitado dentro de un pedido.
     * Si no existe un drawable específico, puede reutilizarse
     * un icono de inventario/producto.
     */
    val Order = R.drawable.ic_invoice

    /** Representa una línea o elemento de pedido. */
    val OrderLine = R.drawable.ic_box

    /** Representa una operación completada. */
    val Done = R.drawable.ic_check

    /** Representa múltiples operaciones completadas. */
    val DoneAll = R.drawable.ic_done_all

    /** Representa una operación pendiente. */
    val Pending = R.drawable.ic_schedule

    /** Representa procesamiento o flujo de trabajo. */
    val Workflow = R.drawable.ic_timeline

    // ============================================================
    // INVENTARIO
    // ============================================================

    /** Inventario disponible. */
    val Inventory = R.drawable.ic_inventory

    /** Existencias o stock actual. */
    val Stock = R.drawable.ic_stock

    /** Existencias por debajo del nivel esperado. */
    val StockAlert = R.drawable.ic_alert_stock

    /** Producto o unidad almacenada. */
    val Box = R.drawable.ic_box

    /** Área física de almacenamiento. */
    val Storage = R.drawable.ic_home_storage

    // ============================================================
    // FINANZAS
    // ============================================================

    /** Dinero recibido o ingreso. */
    val Income = R.drawable.ic_income

    /** Gasto o salida de dinero. */
    val Expense = R.drawable.ic_expense

    /** Pago realizado o registrado. */
    val Paid = R.drawable.ic_paid

    /** Venta realizada. */
    val Sale = R.drawable.ic_sell

    /** Factura o comprobante. */
    val Invoice = R.drawable.ic_invoice

    /** Balance disponible. */
    val Wallet = R.drawable.ic_wallet

    // ============================================================
    // ANALÍTICA
    // ============================================================

    /** Tendencia positiva. */
    val TrendingUp = R.drawable.ic_trending_up

    /** Tendencia negativa. */
    val TrendingDown = R.drawable.ic_trending_down

    /** Reporte operativo o financiero. */
    val Report = R.drawable.ic_report

    /** Gráfica de datos. */
    val ChartLine = R.drawable.ic_chart_line

    /** Información derivada de los datos. */
    val Insight = R.drawable.ic_insight

    /** Predicción o estimación generada por el sistema. */
    val Prediction = R.drawable.ic_prediction

    // ============================================================
    // USUARIOS Y EQUIPO
    // ============================================================

    /** Usuario o cliente. */
    val User = R.drawable.ic_user

    /** Trabajador u operador. */
    val Worker = R.drawable.ic_worker

    /** Administrador del sistema. */
    val Admin = R.drawable.ic_admin

    /** Asignación de una tarea o elemento. */
    val Assign = R.drawable.ic_assign

    /** Cierra la sesión del usuario. */
    val Logout = R.drawable.ic_logout

    // ============================================================
    // BÚSQUEDA Y FILTRADO
    // ============================================================

    /** Busca información. */
    val Search = R.drawable.ic_search

    /** Indica que no existen resultados de búsqueda. */
    val SearchOff = R.drawable.ic_search_off

    /** Filtra información. */
    val Filter = R.drawable.ic_filter

    /** Muestra opciones adicionales. */
    val More = R.drawable.ic_more

    /** Muestra opciones adicionales en orientación vertical. */
    val MoreVertical = R.drawable.ic_more_vert

    // ============================================================
    // ESTADOS Y FEEDBACK
    // ============================================================

    /** Información contextual o ayuda. */
    val Info = R.drawable.ic_info

    /** Error o operación fallida. */
    val Error = R.drawable.ic_error

    /** Acción o estado cancelado. */
    val CircleX = R.drawable.ic_circle_x

    /** Alerta generada para el dispositivo. */
    val MobileAlert = R.drawable.ic_mobile_alert

    // ============================================================
    // CONECTIVIDAD Y SINCRONIZACIÓN
    // ============================================================

    /** Dispositivo conectado a una red Wi-Fi. */
    val Wifi = R.drawable.ic_wifi

    /** Sin conexión Wi-Fi. */
    val WifiOff = R.drawable.ic_wifi_off

    /** Información sincronizada con la nube. */
    val Cloud = R.drawable.ic_cloud

    /** Sin sincronización con la nube. */
    val CloudOff = R.drawable.ic_cloud_off

    /** Sincronización en progreso. */
    val Sync = R.drawable.ic_sync

    // ============================================================
    // INTERACCIÓN Y HARDWARE
    // ============================================================

    /** Cámara del dispositivo. */
    val Camera = R.drawable.ic_camera

    /** Escaneo de códigos o elementos físicos. */
    val Scan = R.drawable.ic_scan

    /** Activación del micrófono para interacción por voz. */
    val Mic = R.drawable.ic_mic

    /** Micrófono desactivado. */
    val MicOff = R.drawable.ic_mic_off

    /** Envía un mensaje, comando o información. */
    val Send = R.drawable.ic_send

    /** Agrega o selecciona una fotografía. */
    val Photo = R.drawable.ic_add_photo_alternate

    // ============================================================
    // TIEMPO Y MEDICIÓN
    // ============================================================

    /** Fecha específica. */
    val Date = R.drawable.ic_date

    /** Horario o programación. */
    val Schedule = R.drawable.ic_schedule

    /** Línea temporal de operaciones. */
    val Timeline = R.drawable.ic_timeline

    /** Medición de peso o cantidad. */
    val Scale = R.drawable.ic_scale

    /** Repite una operación o proceso. */
    val Repeat = R.drawable.ic_repeat

    // ============================================================
    // SEGURIDAD Y SISTEMA
    // ============================================================

    /** Configuración de la aplicación. */
    val Settings = R.drawable.ic_settings

    /** Notificaciones activas. */
    val NotificationFilled = R.drawable.ic_notification_filled

    /** Notificaciones inactivas. */
    val NotificationOutline = R.drawable.ic_notification_outline

    /** NFC activo. */
    val NfcFilled = R.drawable.ic_nfc_filled

    /** NFC inactivo. */
    val NfcOutline = R.drawable.ic_nfc_outline

    /** Seguridad mediante contraseña activa. */
    val PasswordFilled = R.drawable.ic_password_filled

    /** Seguridad mediante contraseña inactiva. */
    val PasswordOutline = R.drawable.ic_password_outline

    /** Bloqueo o contenido protegido. */
    val Lock = R.drawable.ic_lock

    /** Correo electrónico. */
    val Email = R.drawable.ic_email

    /** Correo electrónico verificado. */
    val EmailVerified = R.drawable.ic_mark_email

    /** Gestión de credenciales o contraseña. */
    val PasswordShield = R.drawable.ic_passkey_edit

    /** Ubicación física. */
    val Location = R.drawable.ic_location

    /** Pedido apartado o reservado. */
    val Reserved = R.drawable.ic_lock

    /** Producto en proceso de pesado/procesamiento. */
    val Processing = R.drawable.ic_scale

    /** Pedido listo para entregar. */
    val Ready = R.drawable.ic_check

    /** Pedido entregado al cliente. */
    val Delivered = R.drawable.ic_send

    /** Pago pendiente. */
    val PaymentPending = R.drawable.ic_schedule

    /** Pago completado. */
    val PaymentDone = R.drawable.ic_paid

    // ============================================================
    // APARIENCIA
    // ============================================================

    /** Activa el modo oscuro. */
    val DarkMode = R.drawable.ic_dark_mode

    /** Activa el modo claro. */
    val LightMode = R.drawable.ic_light_mode

    /** Utiliza la configuración visual del sistema. */
    val System = R.drawable.ic_mobile
}