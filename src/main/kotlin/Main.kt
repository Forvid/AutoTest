data class Transfer(
    val amount: Double,
    val cardType: String,
    val isVkPay: Boolean = false
)

fun calculateCommission(transfer: Transfer, monthlyTransfer: Double, monthlyVkPayTransfer: Double): Double {
    // Лимиты
    val dailyLimit = 150_000.0
    val monthlyLimit = 600_000.0
    val vkPayDailyLimit = 15_000.0
    val vkPayMonthlyLimit = 40_000.0

    // Проверка на лимиты
    if (transfer.isVkPay) {
        if (transfer.amount > vkPayDailyLimit) throw IllegalArgumentException("Превышен лимит для VK Pay за один раз.")
        if (monthlyVkPayTransfer + transfer.amount > vkPayMonthlyLimit) throw IllegalArgumentException("Превышен лимит для VK Pay за месяц.")
    } else {
        if (transfer.amount > dailyLimit) throw IllegalArgumentException("Превышен лимит для перевода за один раз.")
        if (monthlyTransfer + transfer.amount > monthlyLimit) throw IllegalArgumentException("Превышен лимит для перевода за месяц.")
    }

    // Расчет комиссии
    return when {
        transfer.isVkPay -> 0.0
        transfer.cardType in listOf("Mastercard", "Maestro") -> {
            if (monthlyTransfer < 75_000) 0.0 else (transfer.amount * 0.006 + 20).coerceAtLeast(0.0)
        }
        transfer.cardType in listOf("Visa", "Мир") -> {
            (transfer.amount * 0.0075).coerceAtLeast(35.0)
        }
        else -> throw IllegalArgumentException("Неизвестный тип карты.")
    }
}

fun main() {
    // Пример использования
    val transfer = Transfer(10_000.0, "Mastercard")
    val commission = calculateCommission(transfer, 50_000.0, 0.0)
    println("Комиссия за перевод: $commission")
}
