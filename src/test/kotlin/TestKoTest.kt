import com.freiheit.assertions.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveCauseInstanceOf
import org.junit.Test

/**
 * Pros:
 * * Infix notation
 * * Impressive error messages (detection of broken fields)
 * * vast amount of assertions
 * * modules for ktor and others
 * * multiplatform support
 */
class TestKoTest {
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
        somebody shouldBe somebody.copy(address = somebody.address!!.copy(street = "bla"))
        matthias shouldBe Person("Matthias", "Bende123r")
        matthias.shouldBeEqualToUsingFields(
            Person("Matthias", "Bender", address = somebody.address),
            Person::name, Person::lastName
        )
        matthias.shouldBeEqualToIgnoringFields(
            Person("Matthias", "Bender", address = somebody.address),
            Person::address
        )
    }

    @Test
    fun `asserting failures`() {
        shouldThrow<CannotDieException> {
            matthias.kill()
        }.shouldHaveCauseInstanceOf<IllegalArgumentException>()
    }

    @Test
    fun `test extensions`() {
        matthias.shouldNotBeCool()
        somebody.shouldBeCool()
    }

    private fun Person.shouldBeCool(): Person {
        should {
            it.name == "Matthias"
        }
        should(CoolnessMatcher())
        return this
    }

    private fun Person.shouldNotBeCool(): Person {
        should(CoolnessMatcher().invert())
        return this
    }

    class CoolnessMatcher : Matcher<Person> {
        override fun test(value: Person): MatcherResult {
            return object : MatcherResult {
                override fun failureMessage(): String =
                    "${value.name} ${value.lastName} is not cool!"

                override fun negatedFailureMessage(): String =
                    "${value.name} ${value.lastName} is too cool!"

                override fun passed(): Boolean = value.name == "Matthias"
            }
        }
    }
}