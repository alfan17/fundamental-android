data class SearchUser(
    val totalCount: Int? = null,
    val incompleteResults: Boolean? = null,
    val items: List<ItemsItem?>? = null
)

data class ItemsItem(
    val avatarUrl: String? = null,
    val login: String? = null
)

