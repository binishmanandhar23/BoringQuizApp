package com.treeleaf.quiz.screens

sealed class Screen(val route: String){
    object OnBoarding{
        object WelcomeScreen: Screen(route = "welcome_screen")
        object NameScreen: Screen(route = "name_screen")
    }
    object QuizSection {
        object QuestionScreen: Screen(route = "question_screen")
        object ResultScreen: Screen(route = "result_screen")
    }
}
