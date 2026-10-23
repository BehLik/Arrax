package com.example.arrax.core.designsystem.components.inputs

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.arrax.R
import com.example.arrax.core.designsystem.icons.ArxIcons

@Composable
fun ArxPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    enabled: Boolean = true
) {
    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val visibilityDescription = stringResource(
        if (isPasswordVisible) {
            R.string.auth_hide_password
        } else {
            R.string.auth_show_password
        }
    )

    ArxTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        isError = errorMessage != null,
        errorMessage = errorMessage,
        enabled = enabled,
        singleLine = true,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    isPasswordVisible = !isPasswordVisible
                },
                enabled = enabled
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPasswordVisible) {
                            ArxIcons.PasswordOutline
                        } else {
                            ArxIcons.PasswordFilled
                        }
                    ),
                    contentDescription = visibilityDescription
                )
            }
        }
    )
}
