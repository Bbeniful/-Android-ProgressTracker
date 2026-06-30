package com.bbeniful.feature.settings.impl.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbeniful.design.darkTextColor
import com.bbeniful.design.lightButton
import com.bbeniful.design.normalTextColor
import com.bbeniful.domain.model.UserProfile

@Composable
internal fun UserProfileScreen(
    userProfile: UserProfile,
    isProfileSaved: Boolean,
    onEvent: (SettingsIntent) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(isProfileSaved) {
        if (isProfileSaved) {
            Toast.makeText(context, "Profile saved", Toast.LENGTH_SHORT).show()
            onEvent(SettingsIntent.ProfileSavedHandled)
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = normalTextColor
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Edit Profile",
                color = normalTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ProfileTextField(
                value = userProfile.firstName,
                label = "First Name",
                onValueChange = { onEvent(SettingsIntent.UpdateFirstName(it)) }
            )

            ProfileTextField(
                value = userProfile.lastName,
                label = "Last Name",
                onValueChange = { onEvent(SettingsIntent.UpdateLastName(it)) }
            )

            ProfileTextField(
                value = userProfile.nickname,
                label = "Nickname (optional)",
                onValueChange = { onEvent(SettingsIntent.UpdateNickname(it)) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = lightButton),
                onClick = { onEvent(SettingsIntent.SaveUserProfile) }
            ) {
                Text(text = "Save Profile", fontSize = 16.sp, color = darkTextColor)
            }
        }
    }
}
