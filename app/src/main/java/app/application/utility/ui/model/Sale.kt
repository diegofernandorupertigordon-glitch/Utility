package app.application.utility.ui.model

data class Sale(
    val id: String = "",
    val cliente: String = "",
    val total: Double = 0.0,
    val fecha: Long = 0L,
    val items: List<Map<String, Any>> = emptyList()
)