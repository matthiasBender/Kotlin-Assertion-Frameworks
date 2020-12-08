import com.freiheit.assertions.*
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.*
import org.junit.Test

/**
 * Pros:
 * * Infix notation
 * * assertions mostly nice to read
 * * supports coroutines
 * Cons:
 * * error messages limited
 * * no multiple assertions
 */
class TestKluent {
    private val matthias = Person(
        name = "Matthias",
        lastName = "Bender"
    )
    private val somebody = Person(
        name = "Hans",
        lastName = "Wurst",
        address = Address(
            "Some Street 42",
            "123456",
            City.Hamburg
        )
    )

    @Test
    fun `test matthias has names`() {
        matthias shouldBeEqualTo Person("Matthias", "Bender")
        matthias shouldNotBeEqualTo somebody
        matthias.address.shouldBeNull()
    }

    @Test
    fun `test failures`() {
        invoking {
            matthias.kill()
        } shouldThrow AnyException
        invoking {
            matthias.kill()
        } shouldThrow CannotDieException::class

        matthias::kill shouldThrow CannotDieException::class withCause IllegalArgumentException::class

        runBlocking {
            coInvoking { myCoroutine() } shouldThrow IllegalStateException::class
        }
    }

    @Test
    fun `test extensions`(){
        somebody.shouldBeCool()
    }

    fun Person.shouldBeCool() {
        this.should("$this is not cool!") {
            name == "Matthias"
        }
    }

    suspend fun myCoroutine(): Nothing = throw IllegalStateException()
}