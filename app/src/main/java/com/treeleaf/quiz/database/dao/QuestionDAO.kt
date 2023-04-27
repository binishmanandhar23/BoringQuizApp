package com.treeleaf.quiz.database.dao

import androidx.room.*
import com.treeleaf.quiz.database.entities.QuestionEntity
import com.treeleaf.quiz.database.entities.QuestionUpdateEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface QuestionDAO {
    @Query("SELECT * FROM question")
    fun getAllQuestionsInFlow(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM question")
    fun getAllQuestions(): List<QuestionEntity>

    @Query("SELECT * FROM question WHERE usersAnsweredCorrectly = :usersAnsweredCorrectly")
    fun getAllUnansweredQuestions(usersAnsweredCorrectly: String? = null): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM question WHERE id in (:ids)")
    fun getQuestionsByIds(ids: List<Int>): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllQuestions(listOfQuestions: List<QuestionEntity>)

    @Update(entity = QuestionEntity::class)
    fun updateQuestion(obj: QuestionUpdateEntity)

    @Delete
    fun deleteQuestion(questionEntity: QuestionEntity)
}