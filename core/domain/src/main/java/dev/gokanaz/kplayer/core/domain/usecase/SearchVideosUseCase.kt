package dev.gokanaz.kplayer.core.domain.usecase

import dev.gokanaz.kplayer.core.model.Video
import javax.inject.Inject

class SearchVideosUseCase @Inject constructor() {
    suspend operator fun invoke(query: String): List<Video> {
        return emptyList()
    }
}
