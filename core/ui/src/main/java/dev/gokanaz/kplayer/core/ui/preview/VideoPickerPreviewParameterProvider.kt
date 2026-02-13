package dev.gokanaz.kplayer.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.gokanaz.kplayer.core.model.Video

class VideoPickerPreviewParameterProvider : PreviewParameterProvider<Video> {
    override val values: Sequence<Video> = sequenceOf(
        Video(
            id = "1",
            title = "Sample Video 1",
            fileName = "sample1.mp4",
            filePath = "/storage/emulated/0/Video/sample1.mp4",
            uri = "content://media/external/video/media/1",
            mimeType = "video/mp4",
            size = 1024 * 1024 * 128,
            duration = 125 * 1000,
            width = 1920,
            height = 1080,
            resolution = "1080p",
            thumbnail = "",
            isFavorite = true,
            watchCount = 5
        ),
        Video(
            id = "2",
            title = "Sample Video 2 with Very Long Title That Should Be Truncated",
            fileName = "sample2.mp4",
            filePath = "/storage/emulated/0/Video/sample2.mp4",
            uri = "content://media/external/video/media/2",
            mimeType = "video/mp4",
            size = 1024 * 1024 * 256,
            duration = 3723 * 1000,
            width = 3840,
            height = 2160,
            resolution = "4K",
            thumbnail = "",
            isFavorite = false,
            watchCount = 2
        ),
        Video(
            id = "3",
            title = "Sample Video 3",
            fileName = "sample3.mkv",
            filePath = "/storage/emulated/0/Video/sample3.mkv",
            uri = "content://media/external/video/media/3",
            mimeType = "video/x-matroska",
            size = 1024 * 1024 * 1024,
            duration = 7200 * 1000,
            width = 1280,
            height = 720,
            resolution = "720p",
            thumbnail = "",
            isFavorite = false,
            watchCount = 0
        )
    )
}

class FolderPreviewParameterProvider : PreviewParameterProvider<dev.gokanaz.kplayer.core.model.Folder> {
    override val values: Sequence<dev.gokanaz.kplayer.core.model.Folder> = sequenceOf(
        dev.gokanaz.kplayer.core.model.Folder(
            id = "1",
            name = "Movies",
            path = "/storage/emulated/0/Movies",
            bucketId = "bucket1",
            mediaCount = 15,
            totalSize = 1024 * 1024 * 1024 * 5,
            totalDuration = 3600 * 1000 * 3
        ),
        dev.gokanaz.kplayer.core.model.Folder(
            id = "2",
            name = "TV Shows",
            path = "/storage/emulated/0/TV",
            bucketId = "bucket2",
            mediaCount = 42,
            totalSize = 1024 * 1024 * 1024 * 15,
            totalDuration = 3600 * 1000 * 20
        ),
        dev.gokanaz.kplayer.core.model.Folder(
            id = "3",
            name = "Home Videos",
            path = "/storage/emulated/0/DCIM/Camera",
            bucketId = "bucket3",
            mediaCount = 8,
            totalSize = 1024 * 1024 * 512,
            totalDuration = 3600 * 1000
        )
    )
}
