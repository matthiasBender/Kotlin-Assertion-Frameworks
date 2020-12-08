package com.freiheit.assertions

data class Person(
    val name: String,
    val lastName: String,
    val address: Address? = null
)

data class Address(
    val street: String,
    val zipCode: String,
    val city: City
)

enum class City {
    Frankfurt,
    Hamburg,
    Berlin,
    London,
    Lisbon;
}

infix fun Person.marries(other: Person): Pair<Person, Person> =
    this to other.copy(lastName = this.lastName)

fun Person.kill() {
    throw CannotDieException("$name cannot die!", IllegalArgumentException("Cause of Failure!"))
}

class CannotDieException(msg: String, cause: Throwable? = null) : Exception(msg, cause)