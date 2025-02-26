package me.auth_kit_aos.auth_kit_ui.presentation.views.on_app_auth_view.account_view

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import kotlinx.collections.immutable.ImmutableList

data class AccountViewContent(
    val title: String,
    val style: TextStyle = TextStyle(),
    val subTitle: AccountViewContentSubTitle? = null,
    val items: ImmutableList<AccountViewItem>,
)

data class AccountViewContentSubTitle(
    val text: String = "Subtitle",
    val icon: Painter? = null,
    val style: TextStyle = TextStyle(),
    val onClick: () -> Unit = {},
)

data class AccountViewItem(
    val text: String = "Item 1",
    val textStyle: TextStyle = TextStyle(),
    val icon: Painter? = null,
    val description: String = "",
    val descriptionStyle: TextStyle = TextStyle(),
    val subcription: ItemSubcription? = null,
)

data class ItemSubcription(
    val text: String = "Basic",
    val style: TextStyle = TextStyle(),
    val onClick: () -> Unit = {},
)
