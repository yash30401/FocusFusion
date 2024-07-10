package com.yash.focusfusion.feature_pomodoro.presentation.timer_adding_updating_session.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yash.focusfusion.R
import com.yash.focusfusion.ui.theme.fontFamily

@Composable
fun TaskTagEditDialog(modifier: Modifier = Modifier, setShowDialog: (Boolean) -> Unit,
                      tagSelectedName:(String)->Unit) {

    var tagSelected by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFFDFC),
            modifier = modifier
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Task Tag",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        IconButton(onClick = { setShowDialog(false) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Close Dialog",
                                tint = Color.Gray
                            )
                        }

                    }

                    Text(text = "Study",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Study"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Study"
                                    tagSelectedName("Study")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Study"
                                tagSelectedName("Study")
                            }
                        })

                    Text(text = "Work",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Work"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Work"
                                    tagSelectedName("Work")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Work"
                                tagSelectedName("Work")

                            }
                        })

                    Text(text = "Exercise",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Exercise"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Exercise"
                                    tagSelectedName("Exercise")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Exercise"
                                tagSelectedName("Exercise")

                            }
                        })


                    Text(text = "Sport",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Sport"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Sport"
                                    tagSelectedName("Sport")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Sport"
                                tagSelectedName("Sport")

                            }
                        })


                    Text(text = "Relax",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Relax"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Relax"
                                    tagSelectedName("Relax")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Relax"
                                tagSelectedName("Relax")

                            }
                        })


                    Text(text = "Entertainment",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Entertainment"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Entertainment"
                                    tagSelectedName("Entertainment")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Entertainment"
                                tagSelectedName("Entertainment")

                            }
                        })


                    Text(text = "Social",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Social"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Social"
                                    tagSelectedName("Social")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Social"
                                tagSelectedName("Social")

                            }
                        })


                    Text(text = "Other",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = if(tagSelected=="Other"){
                            Modifier
                                .border(
                                    2.dp,
                                    color = Color(0xFFFF8D61), shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    tagSelected = "Other"
                                    tagSelectedName("Other")
                                }
                        }else{
                            Modifier.clickable {
                                tagSelected = "Other"
                                tagSelectedName("Other")

                            }
                        })
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TaskTagEditDialogPreview() {
    TaskTagEditDialog(setShowDialog = {}) {

    }
}