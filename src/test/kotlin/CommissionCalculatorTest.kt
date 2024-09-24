import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CommissionCalculatorTest {

    @Test
    fun testMastercardCommission() {
        val transfer = Transfer(10_000.0, "Mastercard")
        assertEquals(0.0, calculateCommission(transfer, 50_000.0, 0.0), 0.01)
    }

    @Test
    fun testVisaCommission() {
        val transfer = Transfer(10_000.0, "Visa")
        assertEquals(75.0, calculateCommission(transfer, 0.0, 0.0), 0.01)
    }

    @Test
    fun testVkPayCommission() {
        val transfer = Transfer(10_000.0, "Mastercard", true)
        assertEquals(0.0, calculateCommission(transfer, 0.0, 0.0), 0.01)
    }

    @Test
    fun testMastercardCommissionAboveLimit() {
        val transfer = Transfer(80_000.0, "Mastercard")
        assertThrows(IllegalArgumentException::class.java) {
            calculateCommission(transfer, 600_000.0, 0.0)
        }
    }

    @Test
    fun testMonthlyLimitExceeded() {
        // Первая транзакция, которая не превышает лимит на перевод
        val transfer1 = Transfer(100_000.0, "Mastercard") // Сумма перевода в пределах лимита
        calculateCommission(transfer1, 100_000.0, 0.0)

        // Вторая транзакция, которая также не превышает лимит на перевод, но в сумме превышает месячный лимит
        val transfer2 = Transfer(550_000.0, "Mastercard") // Сумма перевода, которая в сумме превышает месячный лимит
        assertThrows(IllegalArgumentException::class.java) {
            calculateCommission(transfer2, 100_000.0, 0.0)
        }
    }

    @Test
    fun testVkPayDailyLimit() {
        val transfer = Transfer(16_000.0, "Mastercard", true)
        assertThrows(IllegalArgumentException::class.java) {
            calculateCommission(transfer, 0.0, 0.0)
        }
    }

    @Test
    fun testVkPayMonthlyLimit() {
        val transfer1 = Transfer(10_000.0, "Mastercard", true)
        calculateCommission(transfer1, 0.0, 10_000.0)

        val transfer2 = Transfer(35_000.0, "Mastercard", true)
        assertThrows(IllegalArgumentException::class.java) {
            calculateCommission(transfer2, 0.0, 10_000.0)
        }
    }
}
