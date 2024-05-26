package com.example.diploma.domain.models

abstract class User {
    abstract val id: String;
    abstract val name: String;
    abstract val organization: String;
    abstract val email: String;

    class Base(
        override val id: String,
        override val name: String = "",
        override val organization: String="",
        override val email: String,
        ) : User() {
        constructor() : this(id = "",email = "")
    }

    object Empty : User() {
        override val email = "Empty"
        override val id = "Empty"
        override val name="Empty"
        override val organization = "Empty"
    }

}

