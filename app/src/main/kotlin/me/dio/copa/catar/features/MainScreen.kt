package me.dio.copa.catar.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.Team
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

typealias OnclickNotification = (MatchDomain) -> Unit

@Composable
fun MainScreen(matches: List<MatchDomain>, onclick: OnclickNotification){

    Box(
        Modifier
            .padding(all = 8.dp)
            .fillMaxSize()
            ) {
            LazyColumn(){
                items(matches){  matche ->
                   DetailMatches(matche = matche, onclick)
                }
            }
    }
}

@Composable
fun DetailMatches(matche: MatchDomain, onclick: OnclickNotification){
            Card(Modifier.fillMaxSize()) {
                AsyncImage(model = matche.stadium.image, contentDescription = null, Modifier.size( 130.dp), contentScale = ContentScale.Crop)
                Column(modifier = Modifier.padding(16.dp)) {
                    Notification(matche = matche, onclick)
                    Title(matche = matche)
                    Teams(matche= matche)
                }
                
            }

    Spacer(modifier = Modifier.padding(8.dp))
}

@Composable
fun Notification(matche: MatchDomain, onclick: OnclickNotification){
      Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End) {
         val drawable =  if(matche.notificationEnabled)
             me.dio.copa.catar.R.drawable.ic_notifications_active
         else
             me.dio.copa.catar.R.drawable.ic_notifications

         AsyncImage(
             model = drawable,
             contentDescription = null,
             Modifier.height(24.dp).clickable {
                    onclick(matche)
            }
         )
      }  
}

@Composable
fun Title(matche: MatchDomain){
     Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
         Text(text = "${matche.date.toLocalDate()}  ${matche.stadium.name}", color = Color.White)
     }
}

@Composable
fun Teams(matche: MatchDomain){
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
          Team(matche.team1)
          Text(text = "X", Modifier.padding(end = 16.dp).padding(start = 16.dp), color = Color.White)
          Team(matche.team2)
    }

}

@Composable
fun Team(team: Team){
    Row(horizontalArrangement = Arrangement.Center) {
        Text(text = team.flag,  style = MaterialTheme.typography.h6.copy(color = Color.White))
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = team.displayName, color = Color.White, style = MaterialTheme.typography.h6.copy(color = Color.White))
    }
    
}

