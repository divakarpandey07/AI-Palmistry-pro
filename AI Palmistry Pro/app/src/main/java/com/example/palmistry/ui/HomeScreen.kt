package com.example.palmistry.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onStartScan: () -> Unit,
    onViewHistory: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "home_anim")

    // Floating glow animation
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val palmScale by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "palm_scale"
    )
    val starRotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "star_rot"
    )

    // Fade-in on first composition
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800),
        label = "fade_in"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D0621),
                        Color(0xFF1A0A2E),
                        Color(0xFF0F0C29)
                    )
                )
            )
    ) {
        // Background decorative orbs
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-60).dp, y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF7C3AED).copy(alpha = glowAlpha * 0.4f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 40.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFF59E0B).copy(alpha = glowAlpha * 0.3f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // ── Logo / Icon area ──────────────────────────────────────────
            Box(contentAlignment = Alignment.Center) {
                // Glowing ring
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .scale(palmScale)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(0xFF7C3AED).copy(alpha = glowAlpha * 0.5f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )
                // Palm emoji
                Text(
                    text = "🤚",
                    fontSize = (72 * palmScale).sp,
                    modifier = Modifier.scale(palmScale)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── App title with shimmer ────────────────────────────────────
            Text(
                text = "AI Palmistry Pro",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFF59E0B), Color(0xFFFFD700), Color(0xFFF59E0B))
                    ),
                    shadow = Shadow(
                        color = Color(0xFF7C3AED).copy(alpha = 0.6f),
                        offset = Offset(0f, 4f),
                        blurRadius = 12f
                    )
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Apne haath ki rekhaon se apna\nbhavishya jaanein",
                fontSize = 15.sp,
                color = Color(0xFFF1F5F9).copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ── Feature cards ─────────────────────────────────────────────
            FeatureCard(
                emoji = "🔮",
                title = "AI-Powered Reading",
                desc = "Google Gemini se powered deep palm analysis"
            )
            Spacer(modifier = Modifier.height(12.dp))
            FeatureCard(
                emoji = "🛡️",
                title = "100% Private",
                desc = "AES-256 encryption – aapka data safe hai"
            )
            Spacer(modifier = Modifier.height(12.dp))
            FeatureCard(
                emoji = "📜",
                title = "Reading History",
                desc = "Apni sabhi purani readings dekhen"
            )

            Spacer(modifier = Modifier.height(44.dp))

            // ── Scan button ───────────────────────────────────────────────
            Button(
                onClick = onStartScan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF7C3AED), Color(0xFF4C1D95), Color(0xFF7C3AED))
                            ),
                            RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("✋", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Scan Karein",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── History button ────────────────────────────────────────────
            OutlinedButton(
                onClick = onViewHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, Color(0xFF7C3AED).copy(alpha = 0.6f))
            ) {
                Icon(Icons.Filled.History, contentDescription = null, tint = Color(0xFF7C3AED))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Reading History",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFF1F5F9)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Stars footer
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(5) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("10,000+ readings done", color = Color(0xFFF1F5F9).copy(alpha = 0.5f), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FeatureCard(emoji: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D1B69).copy(alpha = 0.6f))
            .border(1.dp, Color(0xFF7C3AED).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, fontSize = 28.sp)
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(desc, color = Color(0xFFF1F5F9).copy(alpha = 0.6f), fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}
