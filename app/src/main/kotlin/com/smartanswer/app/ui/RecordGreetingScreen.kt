package com.smartanswer.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartanswer.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordGreetingScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onDiscardClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0F171B),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color(0xFF21A1E1)
                ),
                title = {
                    Text(
                        text = stringResource(R.string.record_greeting),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Close"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onHelpClick) {
                        Text(
                            text = stringResource(R.string.help),
                            color = Color(0xFF21A1E1),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.record_your_greeting),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.record_description),
                color = Color(0xFF707D82),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TimerBox(value = "00", label = stringResource(R.string.minutes))
                Text(
                    text = ":",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                TimerBox(value = "12", label = stringResource(R.string.seconds), isActive = true)
            }

            Spacer(modifier = Modifier.height(64.dp))

            WaveformView()

            Spacer(modifier = Modifier.weight(1f))

            RecordButton()

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDiscardClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF1C262B))
                ) {
                    Text(
                        text = stringResource(R.string.discard),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF21A1E1),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TimerBox(value: String, label: String, isActive: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 64.dp)
                .background(
                    color = if (isActive) Color(0xFF1C262B) else Color(0xFF1C262B),
                    shape = RoundedCornerShape(32.dp)
                )
                .then(
                    if (isActive) Modifier.border(2.dp, Color(0xFF21A1E1), RoundedCornerShape(32.dp))
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                color = if (isActive) Color(0xFF21A1E1) else Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = if (isActive) Color(0xFF21A1E1) else Color(0xFF707D82),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WaveformView() {
    Row(
        modifier = Modifier.height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val heights = listOf(
            12, 16, 24, 32, 48, 40, 56, 72, 64, 80,
            56, 48, 32, 24, 16, 12, 16, 24, 48, 64,
            90, 64, 48, 24, 16, 12
        )
        heights.forEachIndexed { index, height ->
            val color = if (index in 10..20) Color(0xFF21A1E1) else Color(0xFF1C262B)
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(height.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}

@Composable
fun RecordButton() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color(0xFF21A1E1).copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(Color(0xFF21A1E1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_stop),
                contentDescription = "Stop Recording",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun RecordGreetingScreenPreview() {
    RecordGreetingScreen()
}
