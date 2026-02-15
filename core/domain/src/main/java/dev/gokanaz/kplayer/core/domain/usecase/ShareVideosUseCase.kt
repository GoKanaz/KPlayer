package dev.gokanaz.kplayer.core.domain.usecase

import android.content.Context
import javax.inject.Inject

class ShareVideosUseCase @Inject constructor() {
    suspend operator fun invoke(videos: List<Any>, context: Context) {
    }
}
