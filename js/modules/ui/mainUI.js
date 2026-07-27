/* ==========================================================================
   AI Palmistry Pro - Apple/VisionOS Clean Luxury UI Controller
   Restores i18n, PWA Banner, Starfield Canvas, TTS Voice, PDF Export, Kundli,
   Tarot Deck, Numerology, History List & WhatsApp Share Engine
   ========================================================================== */

export class MainUIController {
    constructor() {
        this.currentLang = localStorage.getItem('selectedLang') || 'hi';
        this.currentTheme = localStorage.getItem('appTheme') || 'gold';
        this.deferredPrompt = null;
        this.isSpeaking = false;

        this.translations = {
            hi: {
                sub_logo: "वैदिक सामुद्रिक शास्त्र एवं हस्तरेखा",
                nav_palmistry: "हस्तरेखा",
                nav_horoscope: "राशिफल",
                nav_kundli: "कुंडली",
                nav_gemstones: "रत्न सुझाव",
                nav_tarot: "टैरो",
                nav_numerology: "अंकशास्त्र",
                nav_history: "इतिहास",
                pwa_title: "AI Palmistry Pro ऐप",
                pwa_subtitle: "अपने मोबाइल होम स्क्रीन पर इनस्टॉल करें",
                pwa_btn: "इनस्टॉल करें",
                scanner_title: "चरण 1: हथेली स्कैन करें",
                scanner_desc: "हाथ की स्पष्ट छवि कैमरे से लें या फोटो अपलोड करें",
                scanner_placeholder: "हस्तरेखा स्कैन शुरू करने के लिए कैमरा चालू करें या फोटो अपलोड करें",
                btn_camera: "कैमरा चालू करें",
                btn_upload: "फोटो अपलोड करें",
                btn_analyze: "स्कैन व रेखाएं निकालें",
                step2_title: "चरण 2: स्कैन की गई रेखाओं एवं लक्षणों की पुष्टि करें",
                badge_editable: "संपादन योग्य",
                btn_confirm_gen: "पुष्टि करें एवं 4 ग्रंथों से विस्तृत फलकथन निकालें",
                result_title: "शास्त्र-आधारित फलकथन",
                badge_shastra: "4 प्राचीन ग्रंथों द्वारा प्रमाणित",
                btn_listen: "शास्त्र वॉयस सुनें",
                loading_text: "हथेली के कोण एवं पिक्सेल कंट्रास्ट द्वारा रेखाएं स्नैप की जा रही हैं...",
                chat_header_title: "शास्त्र सलाहकार से प्रश्न पूछें (Ask Scripture Chatbot)",
                chat_welcome: "🙏 प्रणाम! आप अपनी हस्तरेखा फलादेश, करियर, विवाह या ग्रह शांति के विषय में कोई भी प्रश्न यहाँ पूछ सकते हैं।",
                chat_ph: "उदा. मेरी सरकारी नौकरी या विवाह का योग कब है?",
                btn_reedit: "पुनः संपादन करें",
                btn_save: "सुरक्षित करें",
                btn_pdf: "PDF डाउनलोड",
                guide_title: "3D हस्तरेखा एवं नवग्रह पर्वत निर्देशिका (Photorealistic 3D Hand)",
                guide_badge: "3D फोटोरिएलिस्टिक मॉडल",
                kundli_title: "जन्म विवरण",
                lbl_fullname: "पूरा नाम",
                lbl_dob: "जन्म तिथि",
                lbl_time: "जन्म समय",
                lbl_place: "जन्म स्थान",
                btn_gen_kundli: "कुंडली निर्मित करें",
                tarot_title: "3-कार्ड टैरो दिव्य वाचन",
                num_title: "अंकशास्त्र मूलांक व भाग्यांक",
                btn_calc: "गणना करें",
                history_title: "सहेजे गए फलकथन",
                btn_clear_history: "इतिहास साफ़ करें",
                footer_text: "© 2026 AI Palmistry Pro. 100% प्रमाणिक वैदिक ज्योतिष एवं सामुद्रिक शास्त्र।"
            },
            en: {
                sub_logo: "Vedic Samudrik Shastra & Palmistry",
                nav_palmistry: "Palmistry",
                nav_horoscope: "Horoscope",
                nav_kundli: "Kundli",
                nav_gemstones: "Gemstones",
                nav_tarot: "Tarot",
                nav_numerology: "Numerology",
                nav_history: "History",
                pwa_title: "AI Palmistry Pro App",
                pwa_subtitle: "Install on your mobile home screen",
                pwa_btn: "Install",
                scanner_title: "Step 1: Scan Human Palm",
                scanner_desc: "Capture a clear image of your palm using camera or upload",
                scanner_placeholder: "Start camera or upload a photo to begin palm analysis",
                btn_camera: "Start Camera",
                btn_upload: "Upload Photo",
                btn_analyze: "Scan & Extract Features",
                step2_title: "Step 2: Confirm & Edit Palm Lines & Attributes",
                badge_editable: "Editable",
                btn_confirm_gen: "Confirm & Generate Detailed Scripture Reading",
                result_title: "Scripture-Grounded Reading",
                badge_shastra: "Certified by 4 Classical Texts",
                btn_listen: "Listen Voice",
                loading_text: "Detecting hand orientation & snapping pixel creases...",
                chat_header_title: "Ask Scripture Chat Assistant",
                chat_welcome: "Greetings! Ask any question about your palm reading, career, marriage or astrological remedies here.",
                chat_ph: "e.g. When will I get a job or marriage yoga?",
                btn_reedit: "Edit Features Again",
                btn_save: "Save Reading",
                btn_pdf: "Download PDF",
                guide_title: "3D Palm & Mount Reference Guide",
                guide_badge: "Photorealistic 3D Model",
                kundli_title: "Birth Details",
                lbl_fullname: "Full Name",
                lbl_dob: "Date of Birth",
                lbl_time: "Time of Birth",
                lbl_place: "Place of Birth",
                btn_gen_kundli: "Generate Kundli",
                tarot_title: "3-Card Tarot Reading",
                num_title: "Numerology Calculator",
                btn_calc: "Calculate",
                history_title: "Saved Readings History",
                btn_clear_history: "Clear History",
                footer_text: "© 2026 AI Palmistry Pro. 100% Authentic Vedic Astrology."
            },
            hin: {
                sub_logo: "Vedic Samudrik Shastra & Palmistry",
                nav_palmistry: "Hastrekha",
                nav_horoscope: "Rashiphal",
                nav_kundli: "Kundli",
                nav_gemstones: "Ratna",
                nav_tarot: "Tarot",
                nav_numerology: "Ank-Shastra",
                nav_history: "History",
                pwa_title: "AI Palmistry Pro App",
                pwa_subtitle: "Mobile Home Screen par install karein",
                pwa_btn: "Install Karein",
                scanner_title: "Step 1: Palm Scan Karein",
                scanner_desc: "Aapne haath ki clear photo camera se lein ya upload karein",
                scanner_placeholder: "Scan start karne ke liye camera chalu karein ya photo upload karein",
                btn_camera: "Camera Chalu Karein",
                btn_upload: "Photo Upload Karein",
                btn_analyze: "Scan & Lines Nikalein",
                step2_title: "Step 2: Scanned Lines Confirm Karein",
                badge_editable: "Edit Kar Sakte Hain",
                btn_confirm_gen: "Confirm Karein & 4 Grantho Se Reading Nikalein",
                result_title: "Shastra-Based Reading Result",
                badge_shastra: "4 Ancient Books Certified",
                btn_listen: "Voice Sunein",
                loading_text: "Palm angle check karke crease lines snap ki ja rahi hain...",
                chat_header_title: "Scripture Chatbot Se Sawal Puchein",
                chat_welcome: "Pranam! Aap apni palm reading, career, marriage ya remedies ke baare me koi bhi sawal yahan pooch sakte hain.",
                chat_ph: "e.g. Meri govt job ya marriage yoga kab hai?",
                btn_reedit: "Phir Se Edit Karein",
                btn_save: "Save Karein",
                btn_pdf: "PDF Download",
                guide_title: "3D Palm & Mount Reference Guide",
                guide_badge: "3D Photorealistic Model",
                kundli_title: "Birth Details",
                lbl_fullname: "Full Name",
                lbl_dob: "Date of Birth",
                lbl_time: "Birth Time",
                lbl_place: "Birth Place",
                btn_gen_kundli: "Kundli Banayein",
                tarot_title: "3-Card Tarot Reading",
                num_title: "Ank-Shastra Calculator",
                btn_calc: "Calculate Karein",
                history_title: "Saved History",
                btn_clear_history: "History Clear Karein",
                footer_text: "© 2026 AI Palmistry Pro. 100% Authentic Vedic Astrology."
            }
        };
    }

    initUI() {
        this.setupNavigation();
        this.setupThemeToggle();
        this.setupLanguageSelector();
        this.setupPWAInstaller();
        this.setupStarfieldCanvas();
        this.setupVoiceNarration();
        this.setupPdfExport();
        this.setupKundliForm();
        this.setupTarotDeck();
        this.setupNumerologyCalculator();
        this.setupHistorySection();
        this.setupSocialShare();
    }

    playTempleChime() {
        try {
            const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
            const osc = audioCtx.createOscillator();
            const gain = audioCtx.createGain();
            osc.type = 'sine';
            osc.frequency.setValueAtTime(528, audioCtx.currentTime);
            gain.gain.setValueAtTime(0.2, audioCtx.currentTime);
            gain.gain.exponentialRampToValueAtTime(0.0001, audioCtx.currentTime + 1.8);
            osc.connect(gain);
            gain.connect(audioCtx.destination);
            osc.start();
            osc.stop(audioCtx.currentTime + 1.8);
        } catch(e) {
            console.log('Audio chime not supported');
        }
    }

    setupNavigation() {
        const desktopBtns = document.querySelectorAll('.desktop-nav .nav-btn');
        const mobileBtns = document.querySelectorAll('.mobile-bottom-nav .mobile-nav-btn');
        const sections = document.querySelectorAll('.tab-section');

        const switchTab = (target) => {
            desktopBtns.forEach(b => b.classList.toggle('active', b.getAttribute('data-tab') === target));
            mobileBtns.forEach(b => b.classList.toggle('active', b.getAttribute('data-tab') === target));

            sections.forEach(s => {
                if (s.id === `${target}Section`) {
                    s.classList.remove('hidden');
                    s.classList.add('active');
                } else {
                    s.classList.remove('active');
                    s.classList.add('hidden');
                }
            });

            if (target === 'history') this.renderHistoryList();
        };

        desktopBtns.forEach(b => b.addEventListener('click', () => switchTab(b.getAttribute('data-tab'))));
        mobileBtns.forEach(b => b.addEventListener('click', () => switchTab(b.getAttribute('data-tab'))));
    }

    setupThemeToggle() {
        const themeBtn = document.getElementById('themeToggleBtn');
        const themeLabel = document.getElementById('themeLabel');

        const apply = (theme) => {
            this.currentTheme = theme;
            localStorage.setItem('appTheme', theme);
            if (theme === 'purple') {
                document.body.classList.remove('theme-royal-gold');
                document.body.classList.add('theme-velvet-purple');
                if (themeLabel) themeLabel.innerText = 'पर्पल थीम';
            } else {
                document.body.classList.remove('theme-velvet-purple');
                document.body.classList.add('theme-royal-gold');
                if (themeLabel) themeLabel.innerText = 'रॉयल थीम';
            }
        };

        if (themeBtn) {
            apply(this.currentTheme);
            themeBtn.addEventListener('click', () => {
                apply(this.currentTheme === 'gold' ? 'purple' : 'gold');
            });
        }
    }

    setupLanguageSelector() {
        const langSelect = document.getElementById('langSelect');
        const applyLang = (lang) => {
            this.currentLang = lang;
            localStorage.setItem('selectedLang', lang);
            if (langSelect) langSelect.value = lang;
            const dict = this.translations[lang] || this.translations.hi;

            document.querySelectorAll('[data-i18n]').forEach(el => {
                const key = el.getAttribute('data-i18n');
                if (dict[key]) el.innerText = dict[key];
            });
            document.querySelectorAll('[data-i18n-ph]').forEach(el => {
                const key = el.getAttribute('data-i18n-ph');
                if (dict[key]) el.placeholder = dict[key];
            });
        };

        if (langSelect) {
            langSelect.value = this.currentLang;
            applyLang(this.currentLang);
            langSelect.addEventListener('change', (e) => applyLang(e.target.value));
        }
    }

    setupPWAInstaller() {
        const banner = document.getElementById('pwaInstallBanner');
        const btn = document.getElementById('pwaInstallBtn');

        window.addEventListener('beforeinstallprompt', (e) => {
            e.preventDefault();
            this.deferredPrompt = e;
            if (banner) banner.classList.remove('hidden');
        });

        if (btn) {
            btn.addEventListener('click', async () => {
                if (this.deferredPrompt) {
                    this.deferredPrompt.prompt();
                    const { outcome } = await this.deferredPrompt.userChoice;
                    if (outcome === 'accepted') console.log('PWA installation accepted');
                    this.deferredPrompt = null;
                    if (banner) banner.classList.add('hidden');
                }
            });
        }
    }

    setupStarfieldCanvas() {
        const canvas = document.getElementById('starsCanvas');
        if (!canvas) return;
        const ctx = canvas.getContext('2d');
        let stars = [];

        const initCanvas = () => {
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            stars = [];
            for (let i = 0; i < 140; i++) {
                stars.push({
                    x: Math.random() * canvas.width,
                    y: Math.random() * canvas.height,
                    radius: Math.random() * 1.4 + 0.5,
                    alpha: Math.random(),
                    speed: Math.random() * 0.015 + 0.005
                });
            }
        };

        const animate = () => {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            stars.forEach(s => {
                s.alpha += s.speed;
                if (s.alpha > 1 || s.alpha < 0) s.speed = -s.speed;
                ctx.fillStyle = `rgba(223, 172, 108, ${Math.abs(s.alpha)})`;
                ctx.beginPath();
                ctx.arc(s.x, s.y, s.radius, 0, Math.PI * 2);
                ctx.fill();
            });
            requestAnimationFrame(animate);
        };

        window.addEventListener('resize', initCanvas);
        initCanvas();
        animate();
    }

    setupVoiceNarration() {
        const listenBtn = document.getElementById('listenReadingBtn');
        const textContent = document.getElementById('readingTextContent');

        if (listenBtn) {
            listenBtn.addEventListener('click', () => {
                if (!('speechSynthesis' in window)) {
                    alert('Audio Speech Synthesis is not supported in your browser.');
                    return;
                }
                if (this.isSpeaking) {
                    window.speechSynthesis.cancel();
                    this.isSpeaking = false;
                    listenBtn.innerHTML = '<i class="fa-solid fa-volume-high"></i> <span>शास्त्र वॉयस सुनें</span>';
                    return;
                }
                const rawText = textContent ? textContent.innerText : '';
                if (!rawText) return;

                const utterance = new SpeechSynthesisUtterance(rawText);
                utterance.lang = this.currentLang === 'en' ? 'en-US' : 'hi-IN';
                utterance.rate = 0.95;

                utterance.onstart = () => {
                    this.isSpeaking = true;
                    listenBtn.innerHTML = '<i class="fa-solid fa-square-stop"></i> <span>रोकें (Stop Audio)</span>';
                };
                utterance.onend = () => {
                    this.isSpeaking = false;
                    listenBtn.innerHTML = '<i class="fa-solid fa-volume-high"></i> <span>शास्त्र वॉयस सुनें</span>';
                };
                window.speechSynthesis.speak(utterance);
            });
        }
    }

    setupPdfExport() {
        const exportBtn = document.getElementById('exportPdfBtn');
        const textContent = document.getElementById('readingTextContent');

        if (exportBtn) {
            exportBtn.addEventListener('click', () => {
                const rawText = textContent ? textContent.innerHTML : '';
                if (!rawText) return;
                const element = document.createElement('div');
                element.style.padding = '30px';
                element.style.color = '#1A0A2E';
                element.style.fontFamily = 'serif';
                element.innerHTML = `
                    <h1 style="color: #6D28D9; text-align: center;">AI Palmistry Pro - Astrological Report</h1>
                    <hr style="border: 1px solid #DFAC6C; margin: 15px 0;">
                    ${rawText}
                    <br><hr>
                    <p style="text-align: center; font-size: 12px; color: #666;">© 2026 AI Palmistry Pro Multilingual App.</p>
                `;
                html2pdf().set({
                    margin: 10,
                    filename: 'AI_Palmistry_Pro_Report.pdf',
                    image: { type: 'jpeg', quality: 0.98 },
                    html2canvas: { scale: 2 },
                    jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
                }).from(element).save();
            });
        }
    }

    setupKundliForm() {
        const form = document.getElementById('kundliForm');
        if (form) {
            form.addEventListener('submit', (e) => {
                e.preventDefault();
                const name = document.getElementById('kName').value;
                const dob = document.getElementById('kDob').value;
                const output = document.getElementById('kundliAnalysisText');
                if (output) {
                    output.innerHTML = `
                        <h3><i class="fa-solid fa-gem"></i> ${name} (${dob}):</h3>
                        <ul>
                            <li><strong>Lagna:</strong> Aries Lagna (Mars Lordship). Leadership & vitality.</li>
                            <li><strong>Raj-Yoga:</strong> Sun & Jupiter confluence in 1st house creates powerful Dhan-Yoga.</li>
                        </ul>
                    `;
                }
            });
        }
    }

    setupTarotDeck() {
        const tarotDeck = document.getElementById('tarotDeck');
        if (!tarotDeck) return;
        const tarotCardsData = [
            { name: "The Magician", text: "🔮 Power & Vision: Focus your intentions. Universal energy supports your endeavors." },
            { name: "The Sun", text: "☀️ Success & Joy: Radiance, high vitality and clarity in career and relationships." },
            { name: "Wheel of Fortune", text: "☸️ Destiny Turn: Auspicious shifts in luck. Age 28 brings financial breakthrough." },
            { name: "The Star", text: "⭐ Hope & Inspiration: Serenity, spiritual peace and new creative opportunities." },
            { name: "The Empress", text: "👑 Abundance & Growth: Luxury, material comfort and strong emotional harmony." }
        ];

        tarotDeck.innerHTML = '';
        tarotCardsData.forEach((card, idx) => {
            const cardEl = document.createElement('div');
            cardEl.className = 'tarot-card-item';
            cardEl.innerHTML = `
                <div class="tarot-card-inner">
                    <div class="tarot-card-back">
                        <i class="fa-solid fa-eye"></i>
                        <p>Card ${idx + 1}</p>
                    </div>
                    <div class="tarot-card-front">
                        <i class="fa-solid fa-sun-plant-wilt fa-2x"></i>
                        <h4>${card.name}</h4>
                    </div>
                </div>
            `;
            cardEl.addEventListener('click', () => {
                cardEl.classList.toggle('flipped');
                const output = document.getElementById('tarotOutput');
                const textResult = document.getElementById('tarotTextResult');
                if (output && textResult) {
                    output.classList.remove('hidden');
                    textResult.innerHTML = `<h3>🔮 ${card.name}:</h3><p>${card.text}</p>`;
                }
            });
            tarotDeck.appendChild(cardEl);
        });
    }

    setupNumerologyCalculator() {
        const btn = document.getElementById('calcNumerologyBtn');
        if (btn) {
            btn.addEventListener('click', () => {
                const dobStr = document.getElementById('numDobInput').value;
                if (!dobStr) return;
                const parts = dobStr.split('-');
                const day = parseInt(parts[2], 10);
                
                let mulank = day;
                while (mulank > 9) mulank = Math.floor(mulank / 10) + (mulank % 10);

                let sum = dobStr.replace(/-/g, '').split('').reduce((a, d) => a + parseInt(d, 10), 0);
                let bhagyank = sum;
                while (bhagyank > 9) bhagyank = Math.floor(bhagyank / 10) + (bhagyank % 10);

                const mVal = document.getElementById('mulankVal');
                const bVal = document.getElementById('bhagyankVal');
                const numText = document.getElementById('numerologyText');
                if (mVal) mVal.innerText = mulank;
                if (bVal) bVal.innerText = bhagyank;
                if (numText) {
                    numText.innerHTML = `
                        <h3><i class="fa-solid fa-star"></i> Mulank ${mulank} & Bhagyank ${bhagyank}:</h3>
                        <p>Independent, intellectual & visionary personality with strong financial yoga.</p>
                    `;
                }
            });
            btn.click();
        }
    }

    setupHistorySection() {
        const saveBtn = document.getElementById('saveReadingBtn');
        const clearBtn = document.getElementById('clearHistoryBtn');
        const textContent = document.getElementById('readingTextContent');

        if (saveBtn) {
            saveBtn.addEventListener('click', () => {
                const text = textContent ? textContent.innerHTML : '';
                if (!text) return;
                const history = JSON.parse(localStorage.getItem('palmistryHistory') || '[]');
                history.unshift({
                    id: Date.now(),
                    date: new Date().toLocaleDateString('hi-IN', { day: 'numeric', month: 'short', year: 'numeric' }),
                    text: text
                });
                localStorage.setItem('palmistryHistory', JSON.stringify(history));
                alert('फलकथन आपके इतिहास में सुरक्षित कर लिया गया है!');
            });
        }

        if (clearBtn) {
            clearBtn.addEventListener('click', () => {
                if (confirm('क्या आप पूरा इतिहास साफ़ करना चाहते हैं?')) {
                    localStorage.removeItem('palmistryHistory');
                    this.renderHistoryList();
                }
            });
        }
    }

    renderHistoryList() {
        const historyList = document.getElementById('historyList');
        if (!historyList) return;
        const history = JSON.parse(localStorage.getItem('palmistryHistory') || '[]');
        if (history.length === 0) {
            historyList.innerHTML = '<p class="empty-placeholder">No saved history found.</p>';
            return;
        }
        historyList.innerHTML = history.map(item => `
            <div class="history-item-card">
                <div class="history-item-header">
                    <span><i class="fa-solid fa-calendar"></i> ${item.date}</span>
                </div>
                <div class="history-item-body">${item.text}</div>
            </div>
        `).join('');
    }

    setupSocialShare() {
        const shareBtn = document.getElementById('shareStoryBtn');
        if (shareBtn) {
            shareBtn.addEventListener('click', () => {
                const text = "🖐️ AI Palmistry Pro द्वारा मेरी हस्तरेखा का शास्त्र फलकथन! देखें आपकी हस्तरेखा क्या कहती है: https://divakarpandey07.github.io/AI-Palmistry-pro";
                window.open(`https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`, '_blank');
            });
        }
    }
}
