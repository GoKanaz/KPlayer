package dev.gokanaz.kplayer.core.ui.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.ArtTrack
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.FullscreenExit
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PictureInPicture
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material.icons.outlined.Hd
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.RepeatOne
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.material.icons.outlined.VolumeDown
import androidx.compose.material.icons.outlined.VolumeMute
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter

sealed class NextIcon(
    val filled: ImageVector,
    val outlined: ImageVector
) {
    object Home : NextIcon(Icons.Filled.Home, Icons.Outlined.Home)
    object Search : NextIcon(Icons.Filled.Search, Icons.Outlined.Search)
    object Library : NextIcon(Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic)
    object Settings : NextIcon(Icons.Filled.Settings, Icons.Outlined.Settings)
    object Play : NextIcon(Icons.Filled.PlayArrow, Icons.Outlined.PlayArrow)
    object Pause : NextIcon(Icons.Filled.Pause, Icons.Outlined.Pause)
    object SkipNext : NextIcon(Icons.Filled.SkipNext, Icons.Outlined.SkipNext)
    object SkipPrevious : NextIcon(Icons.Filled.SkipPrevious, Icons.Outlined.SkipPrevious)
    object Shuffle : NextIcon(Icons.Filled.Shuffle, Icons.Outlined.Shuffle)
    object Repeat : NextIcon(Icons.Filled.Repeat, Icons.Outlined.Repeat)
    object RepeatOne : NextIcon(Icons.Filled.RepeatOne, Icons.Outlined.RepeatOne)
    object Add : NextIcon(Icons.Filled.Add, Icons.Outlined.Add)
    object Delete : NextIcon(Icons.Filled.Delete, Icons.Outlined.Delete)
    object Edit : NextIcon(Icons.Filled.Edit, Icons.Outlined.Edit)
    object Share : NextIcon(Icons.Filled.Share, Icons.Outlined.Share)
    object Download : NextIcon(Icons.Filled.Download, Icons.Outlined.Download)
    object Favorite : NextIcon(Icons.Filled.Favorite, Icons.Outlined.Favorite)
    object FavoriteBorder : NextIcon(Icons.Filled.FavoriteBorder, Icons.Outlined.FavoriteBorder)
    object Sort : NextIcon(Icons.Filled.Sort, Icons.Outlined.Sort)
    object Filter : NextIcon(Icons.Filled.FilterList, Icons.Outlined.FilterList)
    object Grid : NextIcon(Icons.Filled.GridView, Icons.Outlined.GridView)
    object List : NextIcon(Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List)
    object VolumeUp : NextIcon(Icons.Filled.VolumeUp, Icons.Outlined.VolumeUp)
    object VolumeDown : NextIcon(Icons.Filled.VolumeDown, Icons.Outlined.VolumeDown)
    object VolumeMute : NextIcon(Icons.Filled.VolumeMute, Icons.Outlined.VolumeMute)
    object VolumeOff : NextIcon(Icons.Filled.VolumeOff, Icons.Outlined.VolumeOff)
    object Subtitles : NextIcon(Icons.Filled.Subtitles, Icons.Outlined.Subtitles)
    object Quality : NextIcon(Icons.Filled.Hd, Icons.Outlined.Hd)
    object Speed : NextIcon(Icons.Filled.Speed, Icons.Outlined.Speed)
    object Fullscreen : NextIcon(Icons.Filled.Fullscreen, Icons.Outlined.Fullscreen)
    object FullscreenExit : NextIcon(Icons.Filled.FullscreenExit, Icons.Outlined.FullscreenExit)
    object PictureInPicture : NextIcon(Icons.Filled.PictureInPicture, Icons.Outlined.PictureInPicture)
    object Info : NextIcon(Icons.Filled.Info, Icons.Outlined.Info)
    object Warning : NextIcon(Icons.Filled.Warning, Icons.Outlined.Warning)
    object Error : NextIcon(Icons.Filled.Error, Icons.Outlined.Error)
    object Success : NextIcon(Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle)
    object Menu : NextIcon(Icons.Filled.Menu, Icons.Outlined.Menu)
    object MoreVert : NextIcon(Icons.Filled.MoreVert, Icons.Outlined.MoreVert)
    object Close : NextIcon(Icons.Filled.Close, Icons.Outlined.Close)
    object Check : NextIcon(Icons.Filled.Check, Icons.Outlined.Check)
    object Done : NextIcon(Icons.Filled.Done, Icons.Outlined.Done)
    object ArrowBack : NextIcon(Icons.AutoMirrored.Filled.ArrowBack, Icons.AutoMirrored.Outlined.ArrowBack)
    object DarkMode : NextIcon(Icons.Filled.DarkMode, Icons.Outlined.DarkMode)
    object LightMode : NextIcon(Icons.Filled.LightMode, Icons.Outlined.LightMode)
    object Language : NextIcon(Icons.Filled.Language, Icons.Outlined.Language)
    object Login : NextIcon(Icons.AutoMirrored.Filled.Login, Icons.AutoMirrored.Outlined.Login)
    object Logout : NextIcon(Icons.AutoMirrored.Filled.ExitToApp, Icons.AutoMirrored.Outlined.ExitToApp)
    object PlaylistAdd : NextIcon(Icons.Filled.PlaylistAdd, Icons.Outlined.PlaylistAdd)
    object PlaylistPlay : NextIcon(Icons.Filled.PlaylistPlay, Icons.Outlined.PlaylistPlay)
    object Folder : NextIcon(Icons.Filled.Folder, Icons.Outlined.Folder)
    object MusicNote : NextIcon(Icons.Filled.MusicNote, Icons.Outlined.MusicNote)
    object Audiotrack : NextIcon(Icons.Filled.Audiotrack, Icons.Outlined.Audiotrack)
    object Album : NextIcon(Icons.Filled.Album, Icons.Outlined.Album)
    object ArtTrack : NextIcon(Icons.Filled.ArtTrack, Icons.Outlined.ArtTrack)
    object Brightness4 : NextIcon(Icons.Filled.Brightness4, Icons.Outlined.Brightness4)
    object Brightness7 : NextIcon(Icons.Filled.Brightness7, Icons.Outlined.Brightness7)
}

@Composable
fun NextIconPainter(icon: NextIcon, filled: Boolean = false) = rememberVectorPainter(
    image = if (filled) icon.filled else icon.outlined
)
