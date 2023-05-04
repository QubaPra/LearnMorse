package com.example.learnmorse

fun getMorseCode(char: Char): String {
    val morseAlphabet = mapOf(
        'A' to "•—", 'B' to "—•••", 'C' to "—•—•", 'D' to "—••", 'E' to "•", 'F' to "••—•",
        'G' to "——•", 'H' to "••••", 'I' to "••", 'J' to "•———", 'K' to "—•—", 'L' to "•—••",
        'M' to "——", 'N' to "—•", 'O' to "———", 'P' to "•——•", 'R' to "•—•",
        'S' to "•••", 'T' to "—", 'U' to "••—", 'W' to "•——", 'Y' to "—•——", 'Z' to "——••"
    )
    return morseAlphabet[char] ?: ""
}

fun getNormalLetter(morseCode: String): Char {
    val morseAlphabet = mapOf(
        "•—" to 'A', "—•••" to 'B', "—•—•" to 'C', "—••" to 'D', "•" to 'E', "••—•" to 'F',
        "——•" to 'G', "••••" to 'H', "••" to 'I', "•———" to 'J', "—•—" to 'K', "•—••" to 'L',
        "——" to 'M', "—•" to 'N', "———" to 'O', "•——•" to 'P', "•—•" to 'R',
        "•••" to 'S', "—" to 'T', "••—" to 'U', "•——" to 'W', "—•——" to 'Y', "——••" to 'Z'
    )
    return morseAlphabet[morseCode] ?: ' '
}
