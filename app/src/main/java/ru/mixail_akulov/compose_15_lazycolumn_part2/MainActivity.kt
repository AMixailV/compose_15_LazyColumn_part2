package ru.mixail_akulov.compose_15_lazycolumn_part2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.javafaker.Faker
import java.util.Random

data class User(
    val id: Long,
    val photoUrl: String,
    val name: String,
    val status: String
)

fun createUserList(): List<User> {
    val faker = Faker.instance(Random(0))
    val images = mutableListOf(
        "https://i-fotok.ru/girls/foto-lica-devushki-dlya-makiyazha_1/foto-lica-devushki-dlya-makiyazha_2.jpg",
        "https://interesnoznat.com/wp-content/uploads/gesichtermix_11925868_1671921689718597_1702935533_n.jpg",
        "https://www.syl.ru/misc/i/ni/9/4/0/8/9/1/i/940891.jpg",
        "https://i-fotok.ru/stil/lico-devushki-dlya-makiyazha_4/19.jpg",
        "https://style.pibig.info/uploads/posts/2023-03/1679688581_style-pibig-info-p-krasivie-zhenskie-litsa-bez-makiyazha-zhen-1.jpg",
        "https://amiel.club/uploads/posts/2022-03/1647762836_1-amiel-club-p-kartinki-litsa-cheloveka-1.jpg",
        "https://mykaleidoscope.ru/x/uploads/posts/2022-09/1663668908_23-mykaleidoscope-ru-p-interes-na-litse-krasivo-24.jpg",
        "https://img.goodfon.ru/original/1600x1200/1/8f/dzhud-lou-akter-muzhchina-lico.jpg",
        "https://i.pinimg.com/originals/f4/03/77/f40377cbb982eb779f75d5b050ef63a8.jpg"
    )

    val list = List(100) { index ->
        val id = index + 1L
        User(
            id = id,
            photoUrl = images[index % images.size],
            name = faker.name().fullName(),
            status = faker.shakespeare().hamletQuote()
        )
    }
    return list
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppScreen() {
    var userList by remember {
        mutableStateOf(createUserList())
    }
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        items(
            items = userList,
            key = { user -> user.id }
        ) { user ->
            UserCard(
                user = user,
                onUserClicked = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.clicked_on, user.name),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onUserDelete = {
                    userList = userList - user
                },
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onUserClicked: () -> Unit,
    onUserDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(size = 6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) {
                onUserClicked()
            }
    ) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            UserImage(url = user.photoUrl)
            Spacer(modifier = Modifier.width(16.dp))
            UserInfo(user = user)
            DeleteUserButton(onClick = onUserDelete)
        }
    }
}

@Composable
private fun UserImage(url: String) {
    val placeHolder = rememberVectorPainter(
        image = Icons.Rounded.AccountCircle
    )
    AsyncImage(
        model = url,
        placeholder = placeHolder,
        contentDescription = stringResource(id = R.string.user_photo),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(64.dp)
            .aspectRatio(1f / 1f)
            .clip(CircleShape)
    )
}

@Composable
private fun RowScope.UserInfo(user: User) {
    Column (
        modifier = Modifier.weight(1f)
    ){
        Text(
            text = user.name,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = user.status,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun RowScope.DeleteUserButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.align(Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = stringResource(R.string.delete_user)
        )
    }
}
