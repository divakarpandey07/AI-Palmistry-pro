/* ==========================================================================
   Palmistry Pro - Multilingual UI Controller & Translation Engine
   Complete i18n Dictionary for Hindi, English & Hinglish, PWA Installer,
   Starfield Canvas Background, Voice Narration, PDF Export, Kundli, Tarot,
   Numerology, Reading History & Social Share
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
                pwa_title: "Palmistry Pro ऐप",
                pwa_subtitle: "अपने मोबाइल होम स्क्रीन पर इनस्टॉल करें",
                pwa_btn: "इनस्टॉल करें",
                pipe_1: "1. त्वचा व रूपरेखा जांच",
                pipe_2: "2. रेखा निष्कर्षण",
                pipe_3: "3. 4 शास्त्र ग्रंथ",
                pipe_4: "4. AES-256 सुरक्षा",
                scanner_title: "चरण 1: हथेली स्कैन करें",
                scanner_desc: "हाथ की स्पष्ट छवि कैमरे से लें या फोटो अपलोड करें",
                scanner_placeholder: "हस्तरेखा स्कैन शुरू करने के लिए कैमरा चालू करें या फोटो अपलोड करें",
                btn_camera: "कैमरा चालू करें",
                btn_upload: "फोटो अपलोड करें",
                btn_analyze: "स्कैन व रेखाएं निकालें",
                step2_title: "चरण 2: स्कैन की गई रेखाओं एवं लक्षणों की पुष्टि करें",
                step2_subtitle: "आपकी हथेली से निम्नलिखित रेखाएं एवं लक्षण मिले हैं। आप इन्हें जांचें या अपनी इच्छा अनुसार संपादित करें:",
                badge_editable: "संपादन योग्य",
                lbl_heart: "हृदय रेखा:",
                lbl_head: "मस्तिष्क रेखा:",
                lbl_life: "जीवन रेखा:",
                lbl_fate: "भाग्य रेखा:",
                lbl_skin_color: "हथेली का रंग (सामुद्रिक शास्त्र):",
                lbl_finger_type: "उंगलियों की बनावट (कीरो हस्तरेखा):",
                opt_h1: "गुरु पर्वत तक गहरी व सुदृढ़",
                opt_h2: "शनि पर्वत तक सीधी",
                opt_h3: "अंत में द्विशाखीय",
                opt_hd1: "सीधी व तीक्ष्ण तार्किक",
                opt_hd2: "चंद्र पर्वत की ओर झुकी",
                opt_hd3: "द्वि-मस्तिष्क रेखा",
                opt_l1: "पूर्ण सुदृढ़ गोलाई व आरोग्य",
                opt_l2: "मणिकंठ तक विस्तृत",
                opt_l3: "मध्यम गोलाई",
                opt_f1: "मणिकंठ से शनि पर्वत तक",
                opt_f2: "हथेली के मध्य से प्रारंभ",
                opt_f3: "हल्की / मध्यम रेखा",
                opt_skin_pink: "गुलाबी व स्निग्ध (सौभाग्यशाली)",
                opt_skin_red: "लाल व उग्र (ऊर्जावान)",
                opt_skin_yellow: "पीताभ व मध्यम (गंभीर)",
                opt_fing_conical: "लंबी व सुडौल (कलात्मक)",
                opt_fing_square: "वर्गाकार (व्यावहारिक व अनुशासित)",
                opt_fing_spatulate: "चमचाकार (सृजनशील)",
                btn_confirm_gen: "पुष्टि करें एवं 4 ग्रंथों से विस्तृत फलकथन निकालें",
                result_title: "शास्त्र-आधारित फलकथन",
                badge_shastra: "4 प्राचीन ग्रंथों द्वारा प्रमाणित",
                btn_listen: "शास्त्र वॉयस सुनें",
                loading_text: "हथेली के कोण एवं पिक्सेल कंट्रास्ट द्वारा रेखाएं स्नैप की जा रही हैं...",
                accuracy: "सटीकता",
                line_heart: "हृदय रेखा",
                line_head: "मस्तिष्क रेखा",
                line_life: "जीवन रेखा",
                line_fate: "भाग्य रेखा",
                chat_header_title: "शास्त्र सलाहकार से प्रश्न पूछें (Scripture Chatbot)",
                chat_welcome: "🙏 प्रणाम! आप अपनी हस्तरेखा फलादेश, करियर, विवाह या ग्रह शांति के विषय में कोई भी प्रश्न यहाँ पूछ सकते हैं।",
                chat_ph: "उदा. मेरी सरकारी नौकरी या विवाह का योग कब है?",
                btn_reedit: "पुनः संपादन करें",
                btn_save: "सुरक्षित करें",
                btn_pdf: "PDF डाउनलोड",
                empty_result: "स्कैन शुरू करने के बाद आपको यहाँ आपकी हथेली से निकली रेखाएं व संपादन विकल्प दिखाई देगा।",
                invalid_palm_error: "⚠️ हाथ की हथेली पहचाने नहीं गई! कृपया अपने हाथ की स्पष्ट फोटो अपलोड करें।",
                guide_title: "3D हस्तरेखा एवं नवग्रह पर्वत निर्देशिका (Photorealistic 3D Hand)",
                guide_badge: "3D फोटोरिएलिस्टिक मॉडल",
                guide_subtitle: "3D हाथ मॉडल को घुमाएं तथा किसी भी रेखा या नवग्रह पर्वत पर क्लिक करके उनका महत्व जानें:",
                btn_3d_reset: "रीसेट व्यू",
                btn_3d_rotate: "घूमना रोकें",
                btn_3d_xray: "X-Ray मोड",
                btn_3d_heatmap: "कॉन्फिडेंस हीतमैप",
                kundli_title: "जन्म विवरण",
                kundli_desc: "अपनी जन्म पत्रिका हेतु विवरण भरें",
                lbl_fullname: "पूरा नाम",
                lbl_dob: "जन्म तिथि",
                lbl_time: "जन्म समय",
                lbl_place: "जन्म स्थान",
                btn_gen_kundli: "कुंडली निर्मित करें",
                kundli_chart_title: "लग्न एवं ग्रह स्थिति",
                badge_astrology: "वैदिक ज्योतिष",
                tarot_title: "3-कार्ड टैरो दिव्य वाचन",
                tarot_desc: "अपने अतीत, वर्तमान एवं भविष्य हेतु 3 कार्ड चुनें",
                num_title: "अंकशास्त्र मूलांक व भाग्यांक",
                num_desc: "अपनी जन्मतिथि दर्ज करके लकी नंबर जानें",
                btn_calc: "गणना करें",
                num_result_title: "अंकशास्त्र फलादेश",
                mulank: "मूलांक",
                bhagyank: "भाग्यांक",
                history_title: "सहेजे गए फलकथन",
                btn_clear_history: "इतिहास साफ़ करें",
                footer_text: "© 2026 Palmistry Pro. 100% प्रमाणिक वैदिक ज्योतिष एवं सामुद्रिक शास्त्र।"
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
                pwa_title: "Palmistry Pro App",
                pwa_subtitle: "Install on your mobile home screen",
                pwa_btn: "Install",
                pipe_1: "1. Skin & Contour Check",
                pipe_2: "2. Line Extraction",
                pipe_3: "3. 4 Scripture Books",
                pipe_4: "4. AES-256 Security",
                scanner_title: "Step 1: Scan Human Palm",
                scanner_desc: "Capture a clear image of your palm using camera or upload",
                scanner_placeholder: "Start camera or upload a photo to begin palm analysis",
                btn_camera: "Start Camera",
                btn_upload: "Upload Photo",
                btn_analyze: "Scan & Extract Features",
                step2_title: "Step 2: Confirm & Edit Palm Lines & Attributes",
                step2_subtitle: "The following features were extracted from your palm. You can review or edit them:",
                badge_editable: "Editable",
                lbl_heart: "Heart Line:",
                lbl_head: "Head Line:",
                lbl_life: "Life Line:",
                lbl_fate: "Fate Line:",
                lbl_skin_color: "Palm Tone (Samudrik Shastra):",
                lbl_finger_type: "Finger Shape (Cheiro Shastra):",
                opt_h1: "Deep to Jupiter Mount",
                opt_h2: "Straight to Saturn Mount",
                opt_h3: "Forked at End",
                opt_hd1: "Straight & Sharp Logic",
                opt_hd2: "Curved to Moon Mount",
                opt_hd3: "Double Head Line",
                opt_l1: "Full Round Curve",
                opt_l2: "Extended to Wrist",
                opt_l3: "Medium Curve",
                opt_f1: "Wrist to Saturn Mount",
                opt_f2: "From Palm Center",
                opt_f3: "Subtle Line",
                opt_skin_pink: "Pink & Smooth (Auspicious)",
                opt_skin_red: "Reddish & Warm (Energetic)",
                opt_skin_yellow: "Yellowish Tone (Thoughtful)",
                opt_fing_conical: "Long & Conical (Artistic)",
                opt_fing_square: "Square Shape (Practical)",
                opt_fing_spatulate: "Spatulate (Creative)",
                btn_confirm_gen: "Confirm & Generate Detailed Scripture Reading",
                result_title: "Scripture-Grounded Reading",
                badge_shastra: "Certified by 4 Classical Texts",
                btn_listen: "Listen Voice",
                loading_text: "Detecting hand orientation & snapping pixel creases...",
                accuracy: "Accuracy",
                line_heart: "Heart Line",
                line_head: "Head Line",
                line_life: "Life Line",
                line_fate: "Fate Line",
                chat_header_title: "Ask Scripture Chat Assistant",
                chat_welcome: "Greetings! Ask any question about your palm reading, career, marriage or astrological remedies here.",
                chat_ph: "e.g. When will I get a job or marriage yoga?",
                btn_reedit: "Edit Features Again",
                btn_save: "Save Reading",
                btn_pdf: "Download PDF",
                empty_result: "Start a scan to reveal detected features & editing options here.",
                invalid_palm_error: "⚠️ No human palm detected! Please upload or capture a clear photo of a real hand.",
                guide_title: "3D Palm & Mount Reference Guide",
                guide_badge: "Photorealistic 3D Model",
                guide_subtitle: "Rotate 3D model & click any Line/Mount to view Vedic meanings:",
                btn_3d_reset: "Reset View",
                btn_3d_rotate: "Pause 3D",
                btn_3d_xray: "X-Ray Mode",
                btn_3d_heatmap: "Heatmap Mode",
                kundli_title: "Birth Details",
                kundli_desc: "Fill birth details for accurate Kundli chart",
                lbl_fullname: "Full Name",
                lbl_dob: "Date of Birth",
                lbl_time: "Time of Birth",
                lbl_place: "Place of Birth",
                btn_gen_kundli: "Generate Kundli",
                kundli_chart_title: "Lagna & Planetary Chart",
                badge_astrology: "Vedic Astrology",
                tarot_title: "3-Card Tarot Reading",
                tarot_desc: "Select 3 cards for Past, Present & Future insights",
                num_title: "Numerology Calculator",
                num_desc: "Calculate your Life Path (Mulank & Bhagyank) numbers",
                btn_calc: "Calculate",
                num_result_title: "Numerology Analysis",
                mulank: "Mulank (Life Path)",
                bhagyank: "Bhagyank (Destiny)",
                history_title: "Saved Readings History",
                btn_clear_history: "Clear History",
                footer_text: "© 2026 Palmistry Pro. 100% Authentic Vedic Astrology."
            },
            hin: {
                sub_logo: "Vedic Samudrik Shastra & Palmistry",
                nav_palmistry: "Hastrekha",
                nav_horoscope: "Rashifal",
                nav_kundli: "Kundli",
                nav_gemstones: "Gemstones",
                nav_tarot: "Tarot",
                nav_numerology: "Ank Shastra",
                nav_history: "History",
                pwa_title: "Palmistry Pro App",
                pwa_subtitle: "Mobile home screen par install karein",
                pwa_btn: "Install",
                pipe_1: "1. Skin & Outline Check",
                pipe_2: "2. Line Extraction",
                pipe_3: "3. 4 Scripture Books",
                pipe_4: "4. AES-256 Security",
                scanner_title: "Step 1: Palm Scan Karein",
                scanner_desc: "Camera se saaf photo lein ya upload karein",
                scanner_placeholder: "Scan shuru karne ke liye camera start karein ya photo upload karein",
                btn_camera: "Camera Start Karein",
                btn_upload: "Photo Upload Karein",
                btn_analyze: "Scan & Lines Extract Karein",
                step2_title: "Step 2: Extracted Lines & Features Confirm Karein",
                step2_subtitle: "Aapke palm se ye features mile hain. Aap inhe verify ya edit kar sakte hain:",
                badge_editable: "Editable",
                lbl_heart: "Heart Line:",
                lbl_head: "Head Line:",
                lbl_life: "Life Line:",
                lbl_fate: "Fate Line:",
                lbl_skin_color: "Palm Tone (Samudrik Shastra):",
                lbl_finger_type: "Finger Shape (Cheiro Shastra):",
                opt_h1: "Jupiter Mount tak deep & strong",
                opt_h2: "Saturn Mount tak straight",
                opt_h3: "End me forked",
                opt_hd1: "Straight & Sharp logic",
                opt_hd2: "Moon Mount ki taraf inclined",
                opt_hd3: "Double Head Line",
                opt_l1: "Full round curve & high vitality",
                opt_l2: "Wrist tak extended",
                opt_l3: "Medium curve",
                opt_f1: "Wrist se Saturn Mount tak",
                opt_f2: "Palm center se start",
                opt_f3: "Subtle line",
                opt_skin_pink: "Pinkish & Smooth (Lucky)",
                opt_skin_red: "Reddish & Warm (Energetic)",
                opt_skin_yellow: "Yellowish Tone",
                opt_fing_conical: "Long & Conical (Artistic)",
                opt_fing_square: "Square shape (Practical)",
                opt_fing_spatulate: "Spatulate (Creative)",
                btn_confirm_gen: "Confirm & Generate 4 Scripture Reading",
                result_title: "Authentic Scripture Reading",
                badge_shastra: "Certified by 4 Classical Books",
                btn_listen: "Listen Voice",
                loading_text: "Hand angle & pixel contrast se lines snap ho rahi hain...",
                accuracy: "Accuracy",
                line_heart: "Heart Line",
                line_head: "Head Line",
                line_life: "Life Line",
                line_fate: "Fate Line",
                chat_header_title: "Ask Scripture Chatbot",
                chat_welcome: "🙏 Pranam! Aap apni palmistry, career, marriage ya remedies ke baare me sawaal pooch sakte hain.",
                chat_ph: "e.g. Meri job ya marriage yoga kab hai?",
                btn_reedit: "Edit Features Again",
                btn_save: "Save Reading",
                btn_pdf: "Download PDF",
                empty_result: "Scan start karne ke baad aapko yahan features & edit options dikhenge.",
                invalid_palm_error: "⚠️ No human palm detected! Please upload or capture a clear photo of a real hand.",
                guide_title: "3D Palm & Mount Reference Guide",
                guide_badge: "3D Model Guide",
                guide_subtitle: "3D hand model rotate karein & kisi bhi line ya mount par click karke meaning samjhein:",
                btn_3d_reset: "Reset View",
                btn_3d_rotate: "Pause 3D",
                btn_3d_xray: "X-Ray Mode",
                btn_3d_heatmap: "Heatmap Mode",
                kundli_title: "Birth Details",
                kundli_desc: "Kundli chart ke liye details bharein",
                lbl_fullname: "Full Name",
                lbl_dob: "Date of Birth",
                lbl_time: "Time of Birth",
                lbl_place: "Place of Birth",
                btn_gen_kundli: "Generate Kundli",
                kundli_chart_title: "Lagna & Planetary Chart",
                badge_astrology: "Vedic Astrology",
                tarot_title: "3-Card Tarot Reading",
                tarot_desc: "Past, Present & Future ke liye 3 cards select karein",
                num_title: "Numerology Calculator",
                num_desc: "Birthdate enter karke Mulank & Bhagyank jaanein",
                btn_calc: "Calculate",
                num_result_title: "Numerology Analysis",
                mulank: "Mulank (Life Path)",
                bhagyank: "Bhagyank (Destiny)",
                history_title: "Saved Readings History",
                btn_clear_history: "Clear History",
                footer_text: "© 2026 Palmistry Pro. 100% Authentic Vedic Astrology."
            }
        };
    }

    initUI() {
        this.setupThemeToggle();
        this.setupLangSelector();
        this.setupTabNavigation();
        this.setupStarfieldCanvas();
    }

    setupStarfieldCanvas() {
        const canvas = document.getElementById('starsCanvas');
        if (!canvas) return;
        const ctx = canvas.getContext('2d');
        let width = canvas.width = window.innerWidth;
        let height = canvas.height = window.innerHeight;

        const stars = Array.from({ length: 120 }, () => ({
            x: Math.random() * width,
            y: Math.random() * height,
            radius: Math.random() * 1.5 + 0.5,
            alpha: Math.random(),
            speed: Math.random() * 0.015 + 0.005
        }));

        const animateStars = () => {
            ctx.clearRect(0, 0, width, height);
            stars.forEach(star => {
                star.alpha += star.speed;
                if (star.alpha > 1 || star.alpha < 0) star.speed = -star.speed;
                ctx.beginPath();
                ctx.arc(star.x, star.y, star.radius, 0, Math.PI * 2);
                ctx.fillStyle = `rgba(247, 226, 189, ${Math.abs(star.alpha)})`;
                ctx.fill();
            });
            requestAnimationFrame(animateStars);
        };
        animateStars();

        window.addEventListener('resize', () => {
            width = canvas.width = window.innerWidth;
            height = canvas.height = window.innerHeight;
        });
    }

    setupThemeToggle() {
        const themeBtn = document.getElementById('themeToggleBtn');
        const themeLabel = document.getElementById('themeLabel');
        if (!themeBtn) return;

        document.body.className = this.currentTheme === 'gold' ? 'theme-royal-gold' : 'theme-velvet-violet';
        if (themeLabel) themeLabel.innerText = this.currentTheme === 'gold' ? 'रॉयल गोल्ड' : 'वेलवेट वाइब्रेंट';

        themeBtn.addEventListener('click', () => {
            this.currentTheme = this.currentTheme === 'gold' ? 'violet' : 'gold';
            localStorage.setItem('appTheme', this.currentTheme);
            document.body.className = this.currentTheme === 'gold' ? 'theme-royal-gold' : 'theme-velvet-violet';
            if (themeLabel) themeLabel.innerText = this.currentTheme === 'gold' ? 'रॉयल गोल्ड' : 'वेलवेट वाइब्रेंट';
        });
    }

    setupLangSelector() {
        const langSelect = document.getElementById('langSelect');
        if (!langSelect) return;

        langSelect.value = this.currentLang;
        this.applyTranslations(this.currentLang);

        langSelect.addEventListener('change', (e) => {
            this.currentLang = e.target.value;
            localStorage.setItem('selectedLang', this.currentLang);
            this.applyTranslations(this.currentLang);
        });
    }

    applyTranslations(lang) {
        const dict = this.translations[lang] || this.translations['hi'];
        document.querySelectorAll('[data-i18n]').forEach(el => {
            const key = el.getAttribute('data-i18n');
            if (dict[key]) {
                el.innerText = dict[key];
            }
        });
    }

    setupTabNavigation() {
        const navBtns = document.querySelectorAll('.nav-btn, .mobile-nav-btn');
        navBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                const targetTab = btn.getAttribute('data-tab');
                document.querySelectorAll('.nav-btn, .mobile-nav-btn').forEach(b => b.classList.remove('active'));
                document.querySelectorAll(`[data-tab="${targetTab}"]`).forEach(b => b.classList.add('active'));

                document.querySelectorAll('.tab-section').forEach(sec => sec.classList.add('hidden'));
                const targetSection = document.getElementById(`${targetTab}Section`);
                if (targetSection) targetSection.classList.remove('hidden');
            });
        });
    }

    playTempleChime() {
        try {
            const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
            const osc = audioCtx.createOscillator();
            const gain = audioCtx.createGain();
            osc.type = 'sine';
            osc.frequency.setValueAtTime(528, audioCtx.currentTime); // 528 Hz Solfeggio Chime
            gain.gain.setValueAtTime(0.3, audioCtx.currentTime);
            gain.gain.exponentialRampToValueAtTime(0.001, audioCtx.currentTime + 1.8);
            osc.connect(gain);
            gain.connect(audioCtx.destination);
            osc.start();
            osc.stop(audioCtx.currentTime + 1.8);
        } catch (e) {
            // Audio context fallback
        }
    }
}
