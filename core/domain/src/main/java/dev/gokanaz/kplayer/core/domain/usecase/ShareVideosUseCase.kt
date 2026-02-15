package dev.gokanaz.kplayer.core.domain.usecase

import android.content.Context
import dev.gokanaz.kplayer.core.model.Video
import javax.inject.Inject

class ShareVideosUseCase @Inject constructor() {
    suspend operator fun invoke(videos: List<Video>, context: Context) {
    }
}
