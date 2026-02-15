package dev.gokanaz.kplayer.core.domain.usecase

import javax.inject.Inject

class DeleteVideosUseCase @Inject constructor() {
    suspend operator fun invoke(videoIds: List<String>) {
    }
}
