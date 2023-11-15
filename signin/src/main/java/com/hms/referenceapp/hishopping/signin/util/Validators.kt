// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.signin.util

import java.util.regex.Pattern

fun isEmailValid(email: String): Boolean {
    return if (email.isBlank())
        false
    else
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    val passwordPattern: Pattern = Pattern.compile(
        "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}.*$"
    )
    return password.isNotBlank() && passwordPattern.matcher(password).matches()
}

fun isFullNameValid(fullName: String): Boolean {
    val fullNamePattern: Pattern = Pattern.compile(
        "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}\$"
    )
    return fullName.isNotBlank() && fullNamePattern.matcher(fullName).matches()
}