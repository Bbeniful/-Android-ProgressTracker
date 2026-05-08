package com.bbeniful.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserProfileSerializer : Serializer<UserProfileProto> {

    override val defaultValue: UserProfileProto = UserProfileProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProfileProto {
        return try {
            UserProfileProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: UserProfileProto, output: OutputStream) {
        t.writeTo(output)
    }
}
