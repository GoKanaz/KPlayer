package dev.gokanaz.kplayer.core.domain.usecase

import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.Video
import javax.inject.Inject

class GetRecentVideosUseCase @Inject constructor() {
    suspend operator fun invoke(
        limit: Int = 50,
        sortOrder: SortOrder = SortOrder.DESCENDING
    ): List<Video> {
        return emptyList()
    }
}
