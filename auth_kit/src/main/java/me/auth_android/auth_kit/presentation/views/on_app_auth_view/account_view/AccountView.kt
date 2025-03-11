package me.auth_android.auth_kit.presentation.views.on_app_auth_view.account_view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.auth_android.auth_kit.data.auth.AuthProvider
import me.auth_android.auth_kit.R
import me.auth_android.auth_kit.presentation.components.EmailInputField
import me.auth_android.auth_kit.presentation.components.PasswordTextField
import me.auth_android.auth_kit.presentation.components.PopUpView
import me.auth_android.auth_kit.presentation.utils.getEmailValid
import me.auth_android.auth_kit.presentation.utils.getPasswordValid

@Composable
fun AccountView(
    toSignInView: () -> Unit,
    onNavigateBack: () -> Unit,
    toReAuth: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel<AccountViewModel>(),
) {
    AccountViewContent(toSignInView, onNavigateBack, toReAuth, viewModel)
}

@Composable
private fun AccountViewContent(
    toSignInView: () -> Unit,
    onNavigateBack: () -> Unit,
    toReAuth: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel<AccountViewModel>(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(context, viewModel) {
        viewModel.navigateEvent.collect {
            when (it) {
                AccountViewNavigateEvent.BackToAppView -> onNavigateBack()
                AccountViewNavigateEvent.ToReAuthView -> toReAuth()
                AccountViewNavigateEvent.ToSignInView -> toSignInView()
            }
        }
    }

    val backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val itemContainerColor = MaterialTheme.colorScheme.background
    val subTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)

    val nameStyle = MaterialTheme.typography.bodyLarge
    val itemContentTitleStyle = MaterialTheme.typography.bodyMedium.copy(color = subTextColor)
    val itemTitleStyle = MaterialTheme.typography.bodyMedium
    val itemDescriptionStyle = MaterialTheme.typography.bodySmall.copy(color = subTextColor)

    // content list
    // name
    val names =
        AccountViewContent(
            title = stringResource(R.string.name_label),
            style = itemContentTitleStyle,
            // subTitle = TODO(),
            items =
                persistentListOf(
                    AccountViewItem(
                        text = uiState.user?.givenName ?: stringResource(R.string.first_name_label),
                        textStyle =
                            if (uiState.user?.givenName.isNullOrEmpty())
                                nameStyle.copy(color = subTextColor)
                            else nameStyle,
                    ),
                    AccountViewItem(
                        text = uiState.user?.familyName ?: stringResource(R.string.last_name_label),
                        textStyle =
                            if (uiState.user?.familyName.isNullOrEmpty())
                                nameStyle.copy(color = subTextColor)
                            else nameStyle,
                    ),
                ),
        )
    // store
    val store =
        AccountViewContent(
            title = stringResource(R.string.purchases_label),
            style = itemContentTitleStyle,
            subTitle =
                AccountViewContentSubTitle(
                    text = stringResource(R.string.store_label),
                    icon = painterResource(R.drawable.outline_store),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary),
                ),
            items =
                persistentListOf(
                    AccountViewItem(
                        text = "Plan",
                        textStyle = itemTitleStyle,
                        subcription = ItemSubcription("Basic"),
                    ),
                    AccountViewItem(
                        text = "Practice Times",
                        textStyle = itemTitleStyle,
                        description = "10 minutes of practice will be renewed every month",
                        descriptionStyle = itemDescriptionStyle,
                        subcription = ItemSubcription("0 minutes"),
                    ),
                ),
        )
    // sign in methods
    val emailMethod = uiState.user?.authProviderLinks!!.find { it.provider == AuthProvider.Email }
    val ggMethod = uiState.user.authProviderLinks.find { it.provider == AuthProvider.Google }
    val oneProvider = uiState.user.authProviderLinks.count { it.isLinked } == 1
    val signInMethods =
        AccountViewContent(
            title = stringResource(R.string.sign_in_method_label),
            style = itemContentTitleStyle,
            items =
                persistentListOf(
                    AccountViewItem(
                        text = stringResource(R.string.email_label),
                        textStyle = itemTitleStyle,
                        icon = painterResource(R.drawable.baseline_mail_outline),
                        description = emailMethod?.email ?: "",
                        descriptionStyle = itemDescriptionStyle,
                        subcription =
                            if (emailMethod?.isLinked == true && oneProvider) null
                            else
                                ItemSubcription(
                                    text =
                                        if (emailMethod?.isLinked == true) {
                                            stringResource(R.string.un_link_label)
                                        } else stringResource(R.string.link_label),
                                    style = TextStyle(color = MaterialTheme.colorScheme.primary),
                                    onClick = {
                                        if (emailMethod?.isLinked == true) {
                                            viewModel.onUnLinkEmail()
                                        } else {
                                            viewModel.onLinkEmailClick()
                                        }
                                    },
                                ),
                    ),
                    AccountViewItem(
                        text = stringResource(R.string.google_label),
                        textStyle = itemTitleStyle,
                        description = ggMethod?.email ?: "",
                        descriptionStyle = itemDescriptionStyle,
                        icon = painterResource(R.drawable.google_icon),
                        subcription =
                            if (ggMethod?.isLinked == true && oneProvider) null
                            else
                                ItemSubcription(
                                    text =
                                        if (ggMethod?.isLinked == true) {
                                            stringResource(R.string.un_link_label)
                                        } else stringResource(R.string.link_label),
                                    style = TextStyle(color = MaterialTheme.colorScheme.primary),
                                    onClick = {
                                        if (ggMethod?.isLinked == true) {
                                            viewModel.onUnLinkGoogle()
                                        } else {
                                            viewModel.onLinkWithGoogleClick(context)
                                        }
                                    },
                                ),
                    ),
                ),
        )
    // caution zones
    val caution =
        AccountViewContent(
            title = stringResource(R.string.caution_zone_label),
            items =
                persistentListOf(
                    AccountViewItem(
                        text = stringResource(R.string.delete_data_label),
                        textStyle = itemTitleStyle,
                        description = stringResource(R.string.delete_data_description_label),
                        descriptionStyle = itemDescriptionStyle,
                        subcription =
                            ItemSubcription(
                                text = stringResource(R.string.delete_label),
                                style = TextStyle(color = MaterialTheme.colorScheme.error),
                                onClick = { viewModel.onDeleteDataClick() },
                            ),
                    ),
                    AccountViewItem(
                        text = stringResource(R.string.delete_account_label),
                        textStyle = itemTitleStyle,
                        description = stringResource(R.string.delete_account_description_label),
                        descriptionStyle = itemDescriptionStyle,
                        subcription =
                            ItemSubcription(
                                text = stringResource(R.string.delete_label),
                                style = TextStyle(color = MaterialTheme.colorScheme.error),
                                onClick = { viewModel.onDeleteAccountClick() },
                            ),
                    ),
                ),
        )

    val itemContentList = persistentListOf(names, store, signInMethods, caution)
    val scrollState = rememberScrollState(0)
    val titleVisible = scrollState.value < 100
    Column(
        modifier = Modifier.fillMaxSize().background(color = backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(
            visible = !titleVisible,
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
        ) {
            Column {
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .height(36.dp)
                            .background(color = itemContainerColor),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(
                        onClick = viewModel::onCloseViewClick,
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Icon(painter = painterResource(R.drawable.close_icon), "")
                    }
                    Text(
                        stringResource(R.string.account_label),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                HorizontalDivider()
            }
        }
        AnimatedVisibility(
            titleVisible,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        ) {
            Box(
                modifier =
                    Modifier.fillMaxWidth().height(36.dp).background(color = backgroundColor),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = viewModel::onCloseViewClick,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Icon(painter = painterResource(R.drawable.close_icon), "")
                }
            }
        }

        Column(
            modifier =
                Modifier.weight(1f)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.account_label),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            Spacer(Modifier.size(32.dp))
            ProfilePhoto(uiState.user.photoURL, { /*on change user photo*/ })
            Spacer(Modifier.size(12.dp))
            Text(
                text =
                    uiState.user.let {
                        if (it.isAnonymous) stringResource(R.string.anonymous_label)
                        else if (it.displayName.isNullOrEmpty())
                            stringResource(R.string.unknown_label)
                        else it.displayName
                    },
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text =
                    uiState.user.let {
                        if (it.email.isNullOrEmpty()) stringResource(R.string.unknown_label)
                        else it.email
                    },
                style = itemContentTitleStyle,
            )
            Spacer(Modifier.size(32.dp))
            ContentListView(itemContentList, containerColor = itemContainerColor)
            Spacer(Modifier.size(32.dp))
            Button(
                onClick = { viewModel.onSignOutClick(context = context) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors().copy(containerColor = itemContainerColor),
                shape = MaterialTheme.shapes.small,
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_logout),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        stringResource(R.string.sign_out_label),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            Spacer(Modifier.size(32.dp))
        }
    }

    if (uiState.isLinkEmail) {
        EmailPasswordView(
            email = uiState.email,
            password = uiState.password,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmClick = viewModel::linkWithEmail,
            onDismiss = viewModel::onDismissEmailLink,
        )
    }

    PopUpView(uiState.popUpState)
}

@Composable
private fun ProfilePhoto(url: String?, onClick: () -> Unit) {
    Box(modifier = Modifier.size(75.dp)) {
        val painter = rememberAsyncImagePainter(model = url)
        val state = painter.state.collectAsStateWithLifecycle().value
        Image(
            painter =
                if (state is AsyncImagePainter.State.Success) painter
                else painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier.fillMaxSize()
                    .clip(CircleShape)
                    .border(
                        BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.onSurface),
                        shape = CircleShape,
                    ),
        )
        Box(
            modifier =
                Modifier.size(30.dp)
                    .align(Alignment.BottomEnd)
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    )
                    .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(R.drawable.outlined_photo_camera),
                    contentDescription = "Take a photo",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun ContentListView(list: ImmutableList<AccountViewContent>, containerColor: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        list.forEachIndexed { _, item -> AccountViewContentUI(item, containerColor) }
    }
}

@Composable
private fun AccountViewContentUI(content: AccountViewContent, containerColor: Color) {
    val subTitle = content.subTitle
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(content.title, style = content.style)
            if (subTitle != null) {
                Row(
                    modifier = Modifier.wrapContentHeight().clickable { subTitle.onClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (subTitle.icon != null) {
                        Icon(painter = subTitle.icon, "", tint = subTitle.style.color)
                    }
                    Text(text = subTitle.text, style = subTitle.style)
                }
            }
        }
        Spacer(Modifier.size(8.dp))
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .background(shape = MaterialTheme.shapes.small, color = containerColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            content.items.forEachIndexed { index, item ->
                if (index != content.items.lastIndex) {
                    AccountViewItemUI(item, true)
                } else {
                    AccountViewItemUI(item)
                }
            }
        }
    }
}

@Composable
private fun AccountViewItemUI(item: AccountViewItem, divider: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.icon != null) {
            Image(painter = item.icon, "", modifier = Modifier.size(48.dp).padding(8.dp))
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                ) {
                    Text(item.text, style = item.textStyle)
                    if (item.description.isNotBlank()) {
                        Text(text = item.description, style = item.descriptionStyle)
                    }
                }
                if (item.subcription != null) {
                    TextButton(
                        onClick = item.subcription.onClick,
                        modifier = Modifier.wrapContentWidth(),
                    ) {
                        Text(text = item.subcription.text, style = item.subcription.style)
                    }
                }
            }
            if (divider) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun EmailPasswordView(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val dialogProperties =
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    Dialog(onDismissRequest = onDismiss, properties = dialogProperties) {
        Column(
            modifier =
                Modifier.background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.small,
                    )
                    .padding(4.dp)
        ) {
            EmailInputField(email, onValueChange = onEmailChange)
            Spacer(Modifier.size(4.dp))
            PasswordTextField(password, onPasswordChange = onPasswordChange)
            Row(Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = {
                        onConfirmClick()
                        focusManager.clearFocus()
                    },
                    enabled = email.getEmailValid() && password.getPasswordValid(),
                ) {
                    Text(text = stringResource(R.string.link_label))
                }

                Spacer(Modifier.size(8.dp))
                TextButton(
                    onClick = {
                        onDismiss()
                        focusManager.clearFocus()
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountView_Preview() {
    AccountView({}, {}, {})
}
