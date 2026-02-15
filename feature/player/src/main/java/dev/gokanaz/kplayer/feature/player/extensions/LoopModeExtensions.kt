package dev.gokanaz.kplayer.feature.player.extensions

import dev.gokanaz.kplayer.core.model.player.LoopMode
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon

fun LoopMode.toIcon(): NextIcon {
    return when (this) {
        LoopMode.NONE -> NextIcon.Repeat
        LoopMode.ONE -> NextIcon.RepeatOne
        LoopMode.ALL -> NextIcon.Repeat
    }
}

fun LoopMode.next(): LoopMode {
    return when (this) {
        LoopMode.NONE -> LoopMode.ALL
        LoopMode.ALL -> LoopMode.ONE
        LoopMode.ONE -> LoopMode.NONE
    }
}
