package dev.gokanaz.kplayer.core.domain.usecase

import dev.gokanaz.kplayer.core.model.Folder
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType
import javax.inject.Inject

class GetFoldersUseCase @Inject constructor() {
    suspend operator fun invoke(
        sortType: SortType = SortType.NAME,
        sortOrder: SortOrder = SortOrder.ASCENDING
    ): List<Folder> {
        return emptyList()
    }
}
