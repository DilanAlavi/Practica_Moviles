package com.ucb.ucbtest.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ucb.domain.Movie
import com.ucb.ucbtest.R

@Composable
fun MoviesUI(movieViewModel: MovieViewModel = hiltViewModel(), onSuccess : (Movie) -> Unit ) {

    LaunchedEffect(Unit) {
        movieViewModel.loadMovies()
    }

    val uiState by movieViewModel.state.collectAsState()

    when ( val ui = uiState) {
        is MovieViewModel.MovieUIState.Loading -> {
            CircularProgressIndicator()
        }
        is MovieViewModel.MovieUIState.Loaded -> {
            Column(
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(id = R.string.movie_list) ,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        textAlign = TextAlign.Center)
                }
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                        //.padding(top = 100.dp),
                    columns = GridCells.Fixed(2) // GridCells.Adaptive(minSize =  128.dp),


                ) {
                    items(ui.list.size) {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 8.dp
                                )
                                .clip(RoundedCornerShape(20.dp))
                                .background(color = Color.Cyan)
                                .clickable {
                                    onSuccess(ui.list[it])
                                }
                        ) {
                            Box( modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                )  {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w185/"+ui.list[it].posterPath,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = ui.list[it].title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }

                            }

                        }
                    }

                }
            }

        }
        is MovieViewModel.MovieUIState.Error -> {
            Text(ui.message)
        }
    }

}