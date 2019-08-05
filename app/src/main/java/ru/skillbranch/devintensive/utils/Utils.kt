package ru.skillbranch.devintensive.utils

import android.content.Context
import android.util.TypedValue
import ru.skillbranch.devintensive.R

/**
 * Created by Svcat on 2019-07-02.
 */
object Utils {


    fun getThemeAccentColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, value, true)
        return value.data
    }

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if (firstName.isNullOrBlank()) firstName = null
        if (lastName.isNullOrBlank()) lastName = null

        return Pair(firstName, lastName)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val first = getFirstLetter(firstName)
        val last = getFirstLetter(lastName)
        if (first == null && last == null) {
            return null
        }

        return "${if(first!=null) first else ""}${if(last!=null) last else ""}".toUpperCase()
    }

    private fun getFirstLetter(word: String?): String? {
        if (word.isNullOrBlank()) {
            return null
        }

        return "${word[0]}"
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val divided = payload.replace(" ", divider)
        val transliterated = divided.toCharArray().map {
            val caps = Character.isUpperCase(it)
            var trans = transliterateCharacter(it.toLowerCase())
            if (caps) trans = trans.toUpperCase()
            trans
        }
        return transliterated.reduce { sum, char -> sum + char }
    }

    private fun transliterateCharacter(character: Char): String = when (character) {
        'а' -> "a"
        'б' -> "b"
        'в' -> "v"
        'г' -> "g"
        'д' -> "d"
        'е' -> "e"
        'ё' -> "e"
        'ж' -> "zh"
        'з' -> "z"
        'и' -> "i"
        'й' -> "i"
        'к' -> "k"
        'л' -> "l"
        'м' -> "m"
        'н' -> "n"
        'о' -> "o"
        'п' -> "p"
        'р' -> "r"
        'с' -> "s"
        'т' -> "t"
        'у' -> "u"
        'ф' -> "f"
        'х' -> "h"
        'ц' -> "c"
        'ч' -> "ch"
        'ш' -> "sh"
        'щ' -> "sh'"
        'ъ' -> ""
        'ы' -> "i"
        'ь' -> ""
        'э' -> "e"
        'ю' -> "yu"
        'я' -> "ya"
        else -> character.toString()
    }
}