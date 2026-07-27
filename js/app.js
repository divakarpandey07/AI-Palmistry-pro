/* ==========================================================================
   AI Palmistry Pro - Production ES6 Main Entry Point
   Imports Modular Subsystems for 3D PBR Hand, Computer Vision & XAI Engine
   ========================================================================== */

import { PalmDetector } from './modules/vision/palmDetector.js';
import { Hand3DViewer } from './modules/viewer/hand3DViewer.js';
import { XAIEngine } from './modules/ai/xaiEngine.js';
import { MainUIController } from './modules/ui/mainUI.js';

document.addEventListener('DOMContentLoaded', () => {
    // 1. Initialize Main UI Controller
    const ui = new MainUIController();
    ui.initUI();

    // 2. Initialize Computer Vision Landmark Detector
    const detector = new PalmDetector();

    // 3. Initialize Photorealistic 3D PBR Hand Viewer with DOM Readiness Guard (FIX P1 ITEM 9)
    let hand3D = null;
    function initWhenReady() {
        const container = document.getElementById('hand3DCanvas');
        if (container && container.clientWidth > 0) {
            hand3D = new Hand3DViewer('hand3DCanvas', 'gTitle', 'gDesc');
            hand3D.init();
        } else {
            requestAnimationFrame(initWhenReady);
        }
    }
    initWhenReady();

    // 4. Initialize Explainable AI (XAI) Engine
    const xai = new XAIEngine();

    // Setup 3D Control Buttons
    const resetBtn = document.getElementById('reset3DCamBtn');
    const rotateBtn = document.getElementById('toggle3DRotateBtn');

    if (resetBtn) resetBtn.addEventListener('click', () => hand3D && hand3D.resetCamera());
    if (rotateBtn) {
        rotateBtn.addEventListener('click', () => {
            if (!hand3D) return;
            const isRotating = hand3D.toggleRotation();
            rotateBtn.innerHTML = isRotating ?
                '<i class="fa-solid fa-pause"></i> <span>घूमना रोकें</span>' :
                '<i class="fa-solid fa-play"></i> <span>घूमना शुरू करें</span>';
        });
    }

    // Capture & Scan Analysis Flow
    const captureBtn = document.getElementById('captureScanBtn');
    const confirmBtn = document.getElementById('confirmAndGenerateBtn');
    const readingTextContent = document.getElementById('readingTextContent');
    const readingResults = document.getElementById('readingResults');
    const palmVerificationBox = document.getElementById('palmVerificationBox');
    const readingLoading = document.getElementById('readingLoading');
    const emptyPlaceholder = document.getElementById('emptyResultPlaceholder');

    if (captureBtn) {
        captureBtn.addEventListener('click', async () => {
            if (emptyPlaceholder) emptyPlaceholder.classList.add('hidden');
            if (readingResults) readingResults.classList.add('hidden');
            if (palmVerificationBox) palmVerificationBox.classList.add('hidden');
            if (readingLoading) readingLoading.classList.remove('hidden');

            const result = await detector.detectLandmarks();

            setTimeout(() => {
                if (readingLoading) readingLoading.classList.add('hidden');
                if (palmVerificationBox) palmVerificationBox.classList.remove('hidden');
            }, 1500);
        });
    }

    if (confirmBtn) {
        confirmBtn.addEventListener('click', () => {
            const currentLang = localStorage.getItem('selectedLang') || 'hi';
            const readingHTML = xai.generateExplainableReading(currentLang, {});

            if (palmVerificationBox) palmVerificationBox.classList.add('hidden');
            if (readingResults) readingResults.classList.remove('hidden');
            if (readingTextContent) readingTextContent.innerHTML = readingHTML;
        });
    }
});
