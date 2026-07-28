/* ==========================================================================
   AI Palmistry Pro - Production ES6 Main Entry Point
   Wires MediaPipe Vision Detector, GLTF/PBR 3D Hand Viewer, X-Ray Mode,
   AI Heatmap Mode, Dynamic Sequential 3D Line Highlighting & XAI Engine
   ========================================================================== */

import { PalmDetector } from './modules/vision/palmDetector.js';
import { Hand3DViewer } from './modules/viewer/hand3DViewer.js';
import { XAIEngine } from './modules/ai/xaiEngine.js';
import { MainUIController } from './modules/ui/mainUI.js';

document.addEventListener('DOMContentLoaded', () => {
    const ui = new MainUIController();
    ui.initUI();

    const detector = new PalmDetector();
    detector.setupCameraAndUploadHandlers();

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

    const xai = new XAIEngine();
    let currentMeasurements = {};

    // 3D Viewport Controls
    const resetBtn = document.getElementById('reset3DCamBtn');
    const rotateBtn = document.getElementById('toggle3DRotateBtn');
    const xrayBtn = document.getElementById('toggleXRayBtn');
    const heatmapBtn = document.getElementById('toggleHeatmapBtn');

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

    if (xrayBtn) {
        xrayBtn.addEventListener('click', () => {
            if (!hand3D) return;
            const isXRay = hand3D.toggleXRayMode();
            xrayBtn.classList.toggle('active', isXRay);
        });
    }

    if (heatmapBtn) {
        heatmapBtn.addEventListener('click', () => {
            if (!hand3D) return;
            const isHeatmap = hand3D.toggleHeatmapMode();
            heatmapBtn.classList.toggle('active', isHeatmap);
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

            const scanRes = await detector.detectLandmarks();
            currentMeasurements = scanRes.measurements || {};

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

            // Dynamic Sequential 3D Region Highlighting for all 4 Palm Lines
            if (hand3D) {
                const regionsToHighlight = ['heart', 'head', 'life', 'fate'];
                regionsToHighlight.forEach((key, idx) => {
                    setTimeout(() => hand3D.highlightRegion(key), idx * 900);
                });
            }

            const currentLang = localStorage.getItem('selectedLang') || 'hi';
            const readingHTML = xai.generateExplainableReading(currentLang, features, currentMeasurements);

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
