package dev.gokanaz.kplayer.core.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import dev.gokanaz.kplayer.core.datastore.proto.PlayerSettingsProto
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerPreferencesSerializer @Inject constructor() : Serializer<PlayerSettingsProto> {
    
    override val defaultValue: PlayerSettingsProto = PlayerSettingsProto.getDefaultInstance()
    
    override suspend fun readFrom(input: InputStream): PlayerSettingsProto {
        try {
            return withContext(Dispatchers.IO) {
                PlayerSettingsProto.parseFrom(input)
            }
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", exception)
        }
    }
    
    override suspend fun writeTo(t: PlayerSettingsProto, output: OutputStream) {
        withContext(Dispatchers.IO) {
            t.writeTo(output)
        }
    }
}
