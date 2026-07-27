/* ==========================================================================
   AI Palmistry Pro - Production ES6 Main Entry Point
   Wires Vision Detector, 3D PBR Hand Viewer, XAI Engine & Complete UI Controllers
   ========================================================================== */

import { PalmDetector } from './modules/vision/palmDetector.js';
import { Hand3DViewer } from './modules/viewer/hand3DViewer.js';
import { XAIEngine } from './modules/ai/xaiEngine.js';
import { MainUIController } from './modules/ui/mainUI.js';

document.addEventListener('DOMContentLoaded', () => {
    // 1. Initialize Main UI Controller & Restored UI Listeners
    const ui = new MainUIController();
    ui.initUI();

    // 2. Initialize Computer Vision Landmark Detector & Camera/Upload Event Handlers
    const detector = new PalmDetector();
    detector.setupCameraAndUploadHandlers();

    // 3. Initialize Photorealistic 3D PBR Hand Viewer with DOM Readiness Guard
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

    // Capture & Scan Analysis Flow Handlers
    const captureBtn = document.getElementById('captureScanBtn');
    const confirmBtn = document.getElementById('confirmAndGenerateBtn');
    const reEditBtn = document.getElementById('reEditFeaturesBtn');

    const readingTextContent = document.getElementById('readingTextContent');
    const readingResults = document.getElementById('readingResults');
    const palmVerificationBox = document.getElementById('palmVerificationBox');
    const readingLoading = document.getElementById('readingLoading');
    const emptyPlaceholder = document.getElementById('emptyResultPlaceholder');
    const scanLaser = document.getElementById('scanLaser');

    if (captureBtn) {
        captureBtn.addEventListener('click', async () => {
            const isValid = detector.validateHumanPalmImage();
            if (!isValid) {
                alert("⚠️ हाथ की हथेली पहचाने नहीं गई! कृपया अपने हाथ की स्पष्ट फोटो अपलोड करें।");
                return;
            }

            ui.playTempleChime();
            if (scanLaser) scanLaser.classList.remove('hidden');
            if (emptyPlaceholder) emptyPlaceholder.classList.add('hidden');
            if (readingResults) readingResults.classList.add('hidden');
            if (palmVerificationBox) palmVerificationBox.classList.add('hidden');
            if (readingLoading) readingLoading.classList.remove('hidden');

            await detector.detectLandmarks();

            setTimeout(() => {
                if (scanLaser) scanLaser.classList.add('hidden');
                if (readingLoading) readingLoading.classList.add('hidden');
                if (palmVerificationBox) palmVerificationBox.classList.remove('hidden');
            }, 1800);
        });
    }

    if (confirmBtn) {
        confirmBtn.addEventListener('click', () => {
            ui.playTempleChime();
            const selectedHeart = document.getElementById('vHeartLine')?.value || 'deep_jupiter';
            const selectedHead = document.getElementById('vHeadLine')?.value || 'straight_sharp';
            const selectedLife = document.getElementById('vLifeLine')?.value || 'full_curve';
            const selectedFate = document.getElementById('vFateLine')?.value || 'wrist_saturn';
            const selectedSkin = document.getElementById('vSkinColor')?.value || 'pink';
            const selectedFinger = document.getElementById('vFingerType')?.value || 'conical';

            const features = {
                heart: selectedHeart,
                head: selectedHead,
                life: selectedLife,
                fate: selectedFate,
                skin: selectedSkin,
                finger: selectedFinger
            };

            const currentLang = localStorage.getItem('selectedLang') || 'hi';
            const readingHTML = xai.generateExplainableReading(currentLang, features);

            if (palmVerificationBox) palmVerificationBox.classList.add('hidden');
            if (readingResults) readingResults.classList.remove('hidden');
            if (readingTextContent) readingTextContent.innerHTML = readingHTML;
        });
    }

    if (reEditBtn) {
        reEditBtn.addEventListener('click', () => {
            if (readingResults) readingResults.classList.add('hidden');
            if (palmVerificationBox) palmVerificationBox.classList.remove('hidden');
        });
    }
});
