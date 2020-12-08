import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import assertk.fail
import com.freiheit.assertions.*
import org.junit.Test

class TestAssertK {
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
        assertThat(matthias).isEqualTo(Person("Matthias", "Bender"))
        assertThat(matthias.name, "matthias.name").startsWith("Matt")
        assertThat(matthias.name, "matthias.name").endsWith("ias")
        assertThat(matthias.name, "matthias.name").contains("tth")
        assertThat(matthias, "matthias").all {
            prop(Person::name).isEqualTo("Matthias1")
            prop(Person::lastName).isEqualTo("Bender2")
            prop(Person::address).isNull()
        }
    }

    @Test
    fun `asserting failures`() {
        assertThat {
            matthias.kill()
        }.isFailure().all {
            isInstanceOf(CannotDieException::class)
            cause().isNotNull().isInstanceOf(IllegalArgumentException::class)
        }
    }

    @Test
    fun `test extensions`() {
        val s = listOf(1, 2, 3, 4)

        assertThat(somebody, "somebody").all {

            isACoolPerson()
            address().prop(Address::city).isEqualTo(City.Berlin)
        }
    }

    fun Assert<Person>.isACoolPerson() {
        given {
            when(it.name) {
                "Matthias" -> return
                else -> fail("$it is not cool at all!")
            }
        }
    }

    fun Assert<Person>.address(): Assert<Address> {
        val realName = name?.plus(".") ?: ""
        return transform("${realName}address") { it.address }.isNotNull()
    }
}