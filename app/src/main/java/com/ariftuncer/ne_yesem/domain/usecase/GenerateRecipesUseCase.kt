// domain/usecase/GenerateRecipesUseCase.kt
package com.ne_yesem.domain.usecase

import com.ne_yesem.domain.repository.GenerateParams
import com.ne_yesem.domain.repository.RecipeAIRepository
import javax.inject.Inject

class GenerateRecipesUseCase @Inject constructor(
    private val repo: RecipeAIRepository
) {
    suspend operator fun invoke(params: GenerateParams): String =
        repo.generate(params)
}
