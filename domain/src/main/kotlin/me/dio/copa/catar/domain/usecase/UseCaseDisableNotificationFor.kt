package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

class UseCaseDisableNotificationFor @Inject constructor(val repository: MatchesRepository) {
    suspend operator fun invoke(id: String) = repository.disableNotificationFor(id)
}