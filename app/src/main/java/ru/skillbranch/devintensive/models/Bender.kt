package ru.skillbranch.devintensive.models

import java.io.Serializable

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) : Serializable {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {

        val (valid, comment) = validateAnswer(answer)
        if (!valid) {
            return "$comment\n${question.question}" to status.color
        }

        val loweredAnswer = answer.toLowerCase()
        return if (question == Question.IDLE || question.answers.contains(loweredAnswer)) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            if (status == Status.CRITICAL) {
                question = Question.NAME
                status = status.nextStatus()
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            } else {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }
        }
    }

    //Question.NAME -> "Имя должно начинаться с заглавной буквы"
    //Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
    //Question.MATERIAL -> "Материал не должен содержать цифр"
    //Question.BDAY -> "Год моего рождения должен содержать только цифры"
    //Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
    //Question.IDLE -> //игнорировать валидацию
    fun validateAnswer(answer: String): Pair<Boolean, String> {
        return when (question) {
            Question.NAME -> if (startFromUpper(answer)) true to "" else false to "Имя должно начинаться с заглавной буквы"
            Question.PROFESSION -> if (!startFromUpper(answer)) true to "" else false to "Профессия должна начинаться со строчной буквы"
            Question.MATERIAL -> if (noNumbers(answer)) true to "" else false to "Материал не должен содержать цифр"
            Question.BDAY -> if (onlyNumbers(answer)) true to "" else false to "Год моего рождения должен содержать только цифры"
            Question.SERIAL -> if (answer.length == 7 && onlyNumbers(answer)) true to "" else false to "Серийный номер содержит только цифры, и их 7"
            Question.IDLE -> true to ""
        }
    }

    private fun onlyNumbers(answer: String): Boolean {
        return answer.toCharArray().filter { char -> char.isDigit().not() }.isEmpty()
    }

    private fun noNumbers(answer: String): Boolean {
        return answer.toCharArray().filter { char -> char.isDigit() }.isEmpty()
    }

    private fun startFromUpper(answer: String) = answer.isNotBlank() && answer[0].isUpperCase()

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status =
            if (ordinal < values().size - 1) values()[ordinal + 1]
            else values()[0]

    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "bender")),
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")),
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")),
        BDAY("Когда меня создали?", listOf("1111")), // STOPSHIP
        SERIAL("Мой серийный номер?", listOf("1111111")), // STOPSHIP
        IDLE("На этом все, вопросов больше нет", listOf());

        fun nextQuestion(): Question =
            if (this == IDLE) {
                IDLE
            } else {
                values()[ordinal + 1]
            }
    }


}
