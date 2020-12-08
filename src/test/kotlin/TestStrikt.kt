import com.freiheit.assertions.*
import org.junit.Test
import strikt.api.Assertion
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.*

/**
 * Pros about Strikt:
 * * nice to read
 * * great error messages
 * * easy to extend
 * * provides nice integration modules
 * * requires jcenter for maven/gradle dependency
 */

class TestStriktmave {
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
        expectThat(matthias).isEqualTo(Person("Matthias", "Bender"))
        expectThat(matthias.name)
            .startsWith("Matt")
            .endsWith("ias")
            .contains("tth")
        expectThat(matthias.name) {
            startsWith("Matt1")
            endsWith("ias2")
            contains("tth")
        }

        expectThat(matthias) {
            get(Person::name).isEqualTo("Matthias1")
            get(Person::lastName).isEqualTo("Bender2")
            get(Person::address).isNotNull()
        }
    }

    @Test
    fun `asserting failures`() {
        expectCatching {
            matthias.kill()
        }.isFailure()
            .isA<CannotDieException>()
            .cause
            .isA<IllegalArgumentException>()
    }

    @Test
    fun `test extensions`() {
        expectThat(somebody) {
            isACoolPerson()
            address().get(Address::city).isEqualTo(City.Berlin)
        }

    }

    fun Assertion.Builder<Person>.address(): Assertion.Builder<Address> {
        return get(Person::address).isNotNull()
    }

    fun Assertion.Builder<Person>.isACoolPerson(): Assertion.Builder<Person> =
        assert("is a cool person") {
            when(it.name) {
                "Matthias" -> pass()
                else -> fail()
            }
        }
}