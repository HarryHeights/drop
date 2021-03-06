package mcneil.peter.drop.util

import mcneil.peter.drop.model.Either

class Validate {
    companion object {
        private val emailRegex = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        private val passwordRegex = Regex(".{8,}")

        fun email(email: String): Either<String, Boolean> {
            val sanitisedEmail = email.trim()
            var valid = true
            var message = ""

            if (sanitisedEmail.isEmpty()) {
                valid = false
                message = "Email should not be empty."
            }

            if (!emailRegex.matches(sanitisedEmail)) {
                valid = false
                message = "Email is not valid."
            }

            return if (valid) {
                Either.Right(valid)
            } else {
                Either.Left(message)
            }
        }

        fun password(password: String): Either<String, Boolean> {
            var valid = true
            var message = ""

            if (!passwordRegex.matches(password)) {
                valid = false
                message = "Password must be 8 characters or longer."
            }

            return if (valid) {
                Either.Right(valid)
            } else {
                Either.Left(message)
            }
        }

        fun emailPasswordForm(email: String, password: String): Boolean {
            var valid = true

            val validEmail = email(email)
            val validPassword = password(password)

            if (validEmail is Either.Left) {
                valid = false
            }

            if (validPassword is Either.Left) {
                valid = false
            }

            return valid
        }
    }
}