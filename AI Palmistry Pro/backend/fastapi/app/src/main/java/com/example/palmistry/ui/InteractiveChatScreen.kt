package com.example.palmistry.ui

import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmistry.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: String = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
)

@Composable
fun InteractiveChatScreen(
    initialReading: String,
    palmMetadataJson: String,
    selectedLanguage: String,
    onSendFollowUpQuestion: (String, (String) -> Unit) -> Unit,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                isUser = false,
                text = initialReading
            )
        )
    }

    var currentInputText by remember { mutableStateOf("") }
    var isAiResponding by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }

    // TextToSpeech Engine
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsEngine?.language = Locale("hi", "IN")
            }
        }
        ttsEngine = tts
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val quickQuestions = listOf(
        "💍 Meri Shadi kab hone ke yog hain?",
        "💼 Meri Job / Career kab set hoga?",
        "💰 Dhan-Daulat aur Samriddhi Yog?",
        "✈️ Videsh Yatra ya Videsh me Basne ka Yog?",
        "⚕️ Swasthya evam Jeevan Rekha Vishleshan"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepPurple, Color(0xFF0F0C29), Color(0xFF1A0A2E))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardDark)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackToHome) {
                        Text("🏠", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "🔮 AI Hastrekha Chat",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                        Text(
                            text = "3 Classical Books Knowledge Base",
                            fontSize = 11.sp,
                            color = SoftWhite.copy(alpha = 0.7f)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Audio TTS Button
                    IconButton(
                        onClick = {
                            val lastAiMsg = messages.lastOrNull { !it.isUser }?.text ?: ""
                            if (isSpeaking) {
                                ttsEngine?.stop()
                                isSpeaking = false
                            } else if (lastAiMsg.isNotBlank()) {
                                isSpeaking = true
                                ttsEngine?.speak(lastAiMsg, TextToSpeech.QUEUE_FLUSH, null, "tts_reading")
                            }
                        }
                    ) {
                        Text(if (isSpeaking) "🔊 ON" else "🔈 TTS", fontSize = 13.sp, color = GoldAccent)
                    }

                    // Share Button
                    IconButton(
                        onClick = {
                            val shareText = messages.lastOrNull { !it.isUser }?.text ?: initialReading
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "✨ AI Palmistry Pro Reading:\n\n$shareText")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Reading"))
                        }
                    ) {
                        Text("📤", fontSize = 18.sp)
                    }
                }
            }

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubble(message = msg)
                }

                if (isAiResponding) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = GoldAccent,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Acharya AI is analyzing 3 Samudrik books...",
                                fontSize = 13.sp,
                                color = GoldAccent
                            )
                        }
                    }
                }
            }

            // Quick Suggestion Chips (1-Tap Questions)
            AnimatedVisibility(visible = !isAiResponding) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quickQuestions.take(2).forEach { questionText ->
                        SuggestionChip(
                            onClick = {
                                if (!isAiResponding) {
                                    messages.add(ChatMessage(isUser = true, text = questionText))
                                    isAiResponding = true
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                    onSendFollowUpQuestion(questionText) { aiResponse ->
                                        isAiResponding = false
                                        messages.add(ChatMessage(isUser = false, text = aiResponse))
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(messages.size - 1)
                                        }
                                    }
                                }
                            },
                            label = { Text(questionText, fontSize = 11.sp, color = SoftWhite) },
                            colors = SuggestionChipDefaults.suggestionChipColors(containerColor = CardDark)
                        )
                    }
                }
            }

            // Input Box & Send Action
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = currentInputText,
                        onValueChange = { currentInputText = it },
                        placeholder = {
                            Text(
                                "Dusra sawal pochhein (e.g. Job, Shadi, Wealth)...",
                                fontSize = 13.sp,
                                color = SoftWhite.copy(alpha = 0.5f)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = SoftWhite,
                            unfocusedTextColor = SoftWhite
                        )
                    )

                    IconButton(
                        onClick = {
                            if (currentInputText.isNotBlank() && !isAiResponding) {
                                val questionToSend = currentInputText
                                currentInputText = ""
                                messages.add(ChatMessage(isUser = true, text = questionToSend))
                                isAiResponding = true
                                coroutineScope.launch {
                                    listState.animateScrollToItem(messages.size - 1)
                                }
                                onSendFollowUpQuestion(questionToSend) { aiResponse ->
                                    isAiResponding = false
                                    messages.add(ChatMessage(isUser = false, text = aiResponse))
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(GoldAccent, CircleShape)
                    ) {
                        Text("➔", fontSize = 20.sp, color = DeepPurple)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (message.isUser) 18.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 18.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) NeonViolet else CardDark
            ),
            border = if (!message.isUser) androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.4f)) else null,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    color = SoftWhite,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.timestamp,
                    color = SoftWhite.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
