package com.example.arrax.ui.screen.lobby.settings.component

enum class SettingsPreference(val title: String, val subtitle: String) {
    Theme("Tema", "Claro / Oscuro / Automático"),
    Unit("Unidad de medida", "kg / lb"),
    Language("Idioma", "Español"),
    Backup("Copia de seguridad", "Respaldos y sincronización")
}

enum class AlertChannel(val title: String, val subtitle: String) {
    NewOrders("Pedidos nuevos", "Sonido y vibración"),
    UpcomingEvents("Eventos próximos", "Recordatorios"),
    Confirmations("Confirmaciones", "Pesajes y registros"),
    Promotions("Promociones", "Ofertas y novedades"),
    System("Sistema", "Actualizaciones y mantenimiento")
}

enum class AboutLink(val title: String) {
    Terms("Términos y condiciones"),
    Privacy("Política de privacidad"),
    Licenses("Licencias de terceros")
}