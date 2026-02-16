package dev.gokanaz.kplayer.core.domain.usecase

import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.Video
import javax.inject.Inject

class GetFavoriteVideosUseCase @Inject constructor() {
    suspend operator fun invoke(
        sortType: SortType = SortType.DATE,
        sortOrder: SortOrder = SortOrder.DESCENDING
    ): List<Video> {
        return emptyList()
    }
}
