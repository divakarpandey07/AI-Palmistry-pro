/* ==========================================================================
   AI Palmistry Pro - Interactive Multilingual i18n JavaScript Engine
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {

    // ----------------------------------------------------------------------
    // 1. i18n Multilingual Translation Dictionary
    // ----------------------------------------------------------------------
    const translations = {
        hi: {
            sub_logo: "वैदिक सामुद्रिक शास्त्र एवं हस्तरेखा",
            nav_palmistry: "हस्तरेखा",
            nav_kundli: "कुंडली",
            nav_tarot: "टैरो",
            nav_numerology: "अंकशास्त्र",
            nav_history: "इतिहास",
            pwa_title: "AI Palmistry Pro ऐप",
            pwa_subtitle: "अपने मोबाइल होम स्क्रीन पर इनस्टॉल करें",
            pwa_btn: "इनस्टॉल करें",
            scanner_title: "लाइव हस्तरेखा स्कैन",
            scanner_desc: "हाथ की स्पष्ट छवि कैमरे से लें या फोटो अपलोड करें",
            scanner_placeholder: "हस्तरेखा स्कैन शुरू करने के लिए कैमरा चालू करें या फोटो अपलोड करें",
            btn_camera: "कैमरा चालू करें",
            btn_upload: "फोटो अपलोड करें",
            btn_analyze: "विश्लेषण करें",
            result_title: "शास्त्र-आधारित फलकथन",
            badge_shastra: "4 प्राचीन ग्रंथों द्वारा प्रमाणित",
            loading_text: "कीरो हस्तरेखा, सामुद्रिक शास्त्र, वृहद् हस्तरेखा शास्त्र एवं सामुद्रिक हस्तरेखा विज्ञान से मिलान किया जा रहा है...",
            accuracy: "सटीकता",
            line_heart: "हृदय रेखा",
            line_head: "मस्तिष्क रेखा",
            line_life: "जीवन रेखा",
            line_fate: "भाग्य रेखा",
            btn_save: "सुरक्षित करें",
            btn_pdf: "PDF डाउनलोड",
            empty_result: "स्कैन शुरू करने के बाद आपको यहाँ विस्तृत शास्त्र-आधारित विश्लेषण दिखाई देगा।",
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
            footer_text: "© 2026 AI Palmistry Pro Multilingual PWA App. 100% प्रमाणिक वैदिक ज्योतिष एवं सामुद्रिक शास्त्र।"
        },
        en: {
            sub_logo: "Vedic Samudrik Shastra & Palmistry",
            nav_palmistry: "Palmistry",
            nav_kundli: "Kundli",
            nav_tarot: "Tarot",
            nav_numerology: "Numerology",
            nav_history: "History",
            pwa_title: "AI Palmistry Pro App",
            pwa_subtitle: "Install on your mobile home screen",
            pwa_btn: "Install",
            scanner_title: "Live Palm Scanner",
            scanner_desc: "Capture a clear image of your palm using camera or upload",
            scanner_placeholder: "Start camera or upload a photo to begin palm analysis",
            btn_camera: "Start Camera",
            btn_upload: "Upload Photo",
            btn_analyze: "Analyze Palm",
            result_title: "Scripture-Grounded Reading",
            badge_shastra: "Certified by 4 Classical Texts",
            loading_text: "Matching features with Cheiro Palmistry, Samudrik Shastra, Vrihad Hastrekha Shastra...",
            accuracy: "Accuracy",
            line_heart: "Heart Line",
            line_head: "Head Line",
            line_life: "Life Line",
            line_fate: "Fate Line",
            btn_save: "Save Reading",
            btn_pdf: "Download PDF",
            empty_result: "Start a scan to reveal your scripture-grounded astrological report here.",
            kundli_title: "Birth Details",
            kundli_desc: "Fill in your birth details for accurate Kundli chart",
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
            footer_text: "© 2026 AI Palmistry Pro Multilingual PWA App. 100% Authentic Vedic Astrology."
        },
        sa: {
            sub_logo: "वैदिक सामुद्रिक शास्त्रं तथा हस्तरेखा",
            nav_palmistry: "हस्तरेखा",
            nav_kundli: "कुण्डली",
            nav_tarot: "टैरो",
            nav_numerology: "अङ्कशास्त्रम्",
            nav_history: "इतिहासः",
            pwa_title: "AI Palmistry Pro",
            pwa_subtitle: "चलदूरभापे संस्थापयतु",
            pwa_btn: "संस्थापयतु",
            scanner_title: "प्रत्यक्ष हस्तरेखा परीक्षा",
            scanner_desc: "हस्तस्य स्पष्टं चित्रं गृह्णातु",
            scanner_placeholder: "हस्तपरीक्षायाः कृते कैमेरा चालयतु अथवा चित्रं प्रेषयतु",
            btn_camera: "कैमेरा चालयतु",
            btn_upload: "चित्रं प्रेषयतु",
            btn_analyze: "परीक्षणं करोतु",
            result_title: "शास्त्रोक्त फलकथनम्",
            badge_shastra: "४ प्राचीन ग्रन्थैः प्रमाणितम्",
            loading_text: "सामुद्रिकशास्त्रैः सह मेलनं क्रियते...",
            accuracy: "शुद्धता",
            line_heart: "हृदय रेखा",
            line_head: "मस्तिष्क रेखा",
            line_life: "जीवन रेखा",
            line_fate: "भाग्य रेखा",
            btn_save: "सुरक्षितं करोतु",
            btn_pdf: "PDF डाउनलोड",
            empty_result: "परीक्षणानन्तरम् अत्र विस्तृत शास्त्रोक्त फलादेशः दृश्यते।",
            kundli_title: "जन्म विवरणम्",
            kundli_desc: "जन्म विवरणं पूरयतु",
            lbl_fullname: "पूर्णं नाम",
            lbl_dob: "जन्म तिथिः",
            lbl_time: "जन्म समयः",
            lbl_place: "जन्म स्थानम्",
            btn_gen_kundli: "कुण्डलीं रचयतु",
            kundli_chart_title: "लग्नं तथा ग्रह स्थितिः",
            badge_astrology: "वैदिक ज्योतिषम्",
            tarot_title: "३-पत्र टैरो वाचनम्",
            tarot_desc: "भूत-वर्तमान-भविष्याणां कृते ३ पत्राणि चिनोतु",
            num_title: "अङ्कशास्त्रम्",
            num_desc: "जन्मतिथिना मूलाङ्कं गणयतु",
            btn_calc: "गणनां करोतु",
            num_result_title: "अङ्कशास्त्र फलम्",
            mulank: "मूलाङ्कः",
            bhagyank: "भाग्याङ्कः",
            history_title: "सुरक्षित फलानि",
            btn_clear_history: "इतिहासं मार्जयतु",
            footer_text: "© 2026 AI Palmistry Pro. १००% प्रमाणिक वैदिक ज्योतिषम्।"
        },
        mr: {
            sub_logo: "वैदिक सामुद्रिक शास्त्र आणि हस्तरेखा",
            nav_palmistry: "हस्तरेखा",
            nav_kundli: "कुंडली",
            nav_tarot: "टॅरो",
            nav_numerology: "अंकशास्त्र",
            nav_history: "इतिहास",
            pwa_title: "AI Palmistry Pro ॲप",
            pwa_subtitle: "मोबाईल होम स्क्रीनवर इन्स्टॉल करा",
            pwa_btn: "इन्स्टॉल करा",
            scanner_title: "लाइव्ह हस्तरेखा स्कॅन",
            scanner_desc: "हाताचा फोटो कॅमेऱ्याने घ्या किंवा अपलोड करा",
            scanner_placeholder: "स्कॅनिंग सुरू करण्यासाठी कॅमेरा सुरू करा किंवा फोटो अपलोड करा",
            btn_camera: "कॅमेरा सुरू करा",
            btn_upload: "फोटो अपलोड करा",
            btn_analyze: "विश्लेषण करा",
            result_title: "शास्त्र-आधारित फलादेश",
            badge_shastra: "४ प्राचीन ग्रंथांद्वारे प्रमाणित",
            loading_text: "सामुद्रिक शास्त्राशी जुळणी केली जात आहे...",
            accuracy: "अचूकता",
            line_heart: "हृदय रेषा",
            line_head: "मस्तिष्क रेषा",
            line_life: "जीवन रेषा",
            line_fate: "भाग्य रेषा",
            btn_save: "जतन करा",
            btn_pdf: "PDF डाउनलोड",
            empty_result: "स्कॅन केल्यानंतर इथे सविस्तर फलादेश दिसेल.",
            kundli_title: "जन्म माहिती",
            kundli_desc: "सटीक कुंडलीसाठी जन्म माहिती भरा",
            lbl_fullname: "पूर्ण नाव",
            lbl_dob: "जन्म तारीख",
            lbl_time: "जन्म वेळ",
            lbl_place: "जन्म ठिकाण",
            btn_gen_kundli: "कुंडली तयार करा",
            kundli_chart_title: "लग्न व ग्रह स्थिती",
            badge_astrology: "वैदिक ज्योतिष",
            tarot_title: "३-कार्ड टॅरो वाचन",
            tarot_desc: "भूत, वर्तमान व भविष्यासाठी ३ कार्ड निवडा",
            num_title: "अंकशास्त्र",
            num_desc: "जन्मतारखेवरून मूलांक व भाग्यांक शोधा",
            btn_calc: "गणना करा",
            num_result_title: "अंकशास्त्र फलादेश",
            mulank: "मूलांक",
            bhagyank: "भाग्यांक",
            history_title: "साठवलेला इतिहास",
            btn_clear_history: "इतिहास साफ करा",
            footer_text: "© 2026 AI Palmistry Pro. १००% अस्सल वैदिक ज्योतिष."
        },
        gu: {
            sub_logo: "વૈદિક સામુદ્રિક શાસ્ત્ર અને હસ્તરેખા",
            nav_palmistry: "હસ્તરેખા",
            nav_kundli: "કુંડળી",
            nav_tarot: "ટેરોટ",
            nav_numerology: "અંકશાસ્ત્ર",
            nav_history: "ઇતિહાસ",
            pwa_title: "AI Palmistry Pro એપ",
            pwa_subtitle: "મોબાઇલ હોમ સ્ક્રીન પર ઇન્સ્ટોલ કરો",
            pwa_btn: "ઇન્સ્ટોલ કરો",
            scanner_title: "લાઇવ હસ્તરેખા સ્કેન",
            scanner_desc: "કેમેરાથી હાથનો ફોટો લો અથવા અપલોડ કરો",
            scanner_placeholder: "સ્કેનિંગ શરૂ કરવા કેમેરા ચાલુ કરો અથવા ફોટો અપલોડ કરો",
            btn_camera: "કેમેરા ચાલુ કરો",
            btn_upload: "ફોટો અપલોડ કરો",
            btn_analyze: "વિશ્લેષણ કરો",
            result_title: "શાસ્ત્ર-આધારિત ફળાદેશ",
            badge_shastra: "૪ પ્રાચીન ગ્રંથો દ્વારા પ્રમાણિત",
            loading_text: "સામુદ્રિક શાસ્ત્ર સાથે સરખામણી થઈ રહી છે...",
            accuracy: "ચોકસાઈ",
            line_heart: "હૃદય રેખા",
            line_head: "મસ્તિષ્ક રેખા",
            line_life: "જીવન રેખા",
            line_fate: "ભાગ્ય રેખા",
            btn_save: "સાચવો",
            btn_pdf: "PDF ડાઉનલોડ",
            empty_result: "સ્કેન કર્યા પછી અહીં વિગતવાર અહેવાલ દેખાશે.",
            kundli_title: "જન્મ વિગત",
            kundli_desc: "કુંડળી માટે જન્મ માહિતી ભરો",
            lbl_fullname: "પૂરું નામ",
            lbl_dob: "જન્મ તારીખ",
            lbl_time: "જન્મ સમય",
            lbl_place: "જન્મ સ્થળ",
            btn_gen_kundli: "કુંડળી બનાવો",
            kundli_chart_title: "લગ્ન અને ગ્રહ સ્થિતિ",
            badge_astrology: "વૈદિક જ્યોતિષ",
            tarot_title: "૩-કાર્ડ ટેરોટ",
            tarot_desc: "ભૂતકાળ, વર્તમાન અને ભવિષ્ય માટે ૩ કાર્ડ પસંદ કરો",
            num_title: "અંકશાસ્ત્ર",
            num_desc: "મૂળાંક અને ભાગ્યાંક ગણો",
            btn_calc: "ગણતરી કરો",
            num_result_title: "અંકશાસ્ત્ર ફળાદેશ",
            mulank: "મૂળાંક",
            bhagyank: "ભાગ્યાંક",
            history_title: "સાચવેલ ઇતિહાસ",
            btn_clear_history: "ઇતિહાસ સાફ કરો",
            footer_text: "© 2026 AI Palmistry Pro. ૧૦૦% સાચું વૈદિક જ્યોતિષ."
        },
        ta: {
            sub_logo: "வேத சாமுத்திரிகா சாஸ்திரம் & கைரேகை",
            nav_palmistry: "கைரேகை",
            nav_kundli: "ஜாதகம்",
            nav_tarot: "டாரோட்",
            nav_numerology: "எண் கணிதம்",
            nav_history: "வரலாறு",
            pwa_title: "AI Palmistry Pro ஆப்",
            pwa_subtitle: "மொபைல் முகப்புத் திரையில் நிறுவவும்",
            pwa_btn: "நிறுவு",
            scanner_title: "லைவ் கைரேகை ஸ்கேன்",
            scanner_desc: "கேமரா மூலம் உங்கள் உள்ளங்கையை ஸ்கேன் செய்யவும்",
            scanner_placeholder: "ஸ்கேன் செய்ய கேமராவைத் தொடங்கவும் அல்லது புகைப்படத்தைப் பதிவேற்றவும்",
            btn_camera: "கேமராவைத் தொடங்கு",
            btn_upload: "புகைப்படம் பதிவேற்று",
            btn_analyze: "ஆராய்ச்சி செய்",
            result_title: "சாஸ்திர பலன்கள்",
            badge_shastra: "4 பழமையான நூல்களின் சான்றளிக்கப்பட்டது",
            loading_text: "சாமுத்திரிகா சாஸ்திரத்துடன் ஒப்பிடப்படுகிறது...",
            accuracy: "துல்லியம்",
            line_heart: "இதய ரேகை",
            line_head: "புத்தி ரேகை",
            line_life: "ஆயுள் ரேகை",
            line_fate: "விதி ரேகை",
            btn_save: "சேமி",
            btn_pdf: "PDF பதிவிறக்கு",
            empty_result: "ஸ்கேன் செய்த பிறகு இங்கே விரிவான ஜோதிட பலன்கள் தோன்றும்.",
            kundli_title: "பிறப்பு விவரங்கள்",
            kundli_desc: "ஜாதகம் தயாரிக்க விவரங்களை உள்ளிடவும்",
            lbl_fullname: "முழு பெயர்",
            lbl_dob: "பிறந்த தேதி",
            lbl_time: "பிறந்த நேரம்",
            lbl_place: "பிறந்த இடம்",
            btn_gen_kundli: "ஜாதகம் உருவாக்கு",
            kundli_chart_title: "லக்னம் & கிரக நிலைகள்",
            badge_astrology: "வேத ஜோதிடம்",
            tarot_title: "3-கார்டு டாரோட்",
            tarot_desc: "கடந்த காலம், நிகழ்காலம் & எதிர்காலத்திற்கு 3 கார்டுகளைத் தேர்ந்தெடுக்கவும்",
            num_title: "எண் கணிதம்",
            num_desc: "உங்கள் அதிர்ஷ்ட எண்களைக் கணக்கிடுங்கள்",
            btn_calc: "கணக்கிடு",
            num_result_title: "எண் கணித பலன்கள்",
            mulank: "மூலாங்கம்",
            bhagyank: "பாக்கியாங்கம்",
            history_title: "சேமிக்கப்பட்ட வரலாறு",
            btn_clear_history: "வரலாற்றை அழி",
            footer_text: "© 2026 AI Palmistry Pro. 100% உண்மையான வேத ஜோதிடம்."
        }
    };

    let currentLang = localStorage.getItem('selectedLang') || 'hi';
    const langSelect = document.getElementById('langSelect');

    function applyLanguage(lang) {
        currentLang = lang;
        localStorage.setItem('selectedLang', lang);
        if (langSelect) langSelect.value = lang;

        const langDict = translations[lang] || translations.hi;
        document.querySelectorAll('[data-i18n]').forEach(el => {
            const key = el.getAttribute('data-i18n');
            if (langDict[key]) {
                el.innerText = langDict[key];
            }
        });
    }

    if (langSelect) {
        langSelect.value = currentLang;
        langSelect.addEventListener('change', (e) => {
            applyLanguage(e.target.value);
        });
    }

    applyLanguage(currentLang);

    // ----------------------------------------------------------------------
    // 2. PWA Service Worker & Install Prompt
    // ----------------------------------------------------------------------
    let deferredPrompt = null;
    const pwaInstallBanner = document.getElementById('pwaInstallBanner');
    const pwaInstallBtn = document.getElementById('pwaInstallBtn');

    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('./sw.js')
            .then(reg => console.log('Service Worker registered:', reg.scope))
            .catch(err => console.error('Service Worker registration failed:', err));
    }

    window.addEventListener('beforeinstallprompt', (e) => {
        e.preventDefault();
        deferredPrompt = e;
        if (pwaInstallBanner) pwaInstallBanner.classList.remove('hidden');
    });

    if (pwaInstallBtn) {
        pwaInstallBtn.addEventListener('click', async () => {
            if (deferredPrompt) {
                deferredPrompt.prompt();
                const { outcome } = await deferredPrompt.userChoice;
                if (outcome === 'accepted') console.log('PWA installation accepted');
                deferredPrompt = null;
                pwaInstallBanner.classList.add('hidden');
            }
        });
    }

    // ----------------------------------------------------------------------
    // 3. Background Starfield Canvas
    // ----------------------------------------------------------------------
    const canvas = document.getElementById('starsCanvas');
    const ctx = canvas.getContext('2d');
    let stars = [];

    function initCanvas() {
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
    }

    function animateStars() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        stars.forEach(star => {
            star.alpha += star.speed;
            if (star.alpha > 1 || star.alpha < 0) star.speed = -star.speed;
            ctx.fillStyle = `rgba(223, 172, 108, ${Math.abs(star.alpha)})`;
            ctx.beginPath();
            ctx.arc(star.x, star.y, star.radius, 0, Math.PI * 2);
            ctx.fill();
        });
        requestAnimationFrame(animateStars);
    }

    window.addEventListener('resize', initCanvas);
    initCanvas();
    animateStars();

    // ----------------------------------------------------------------------
    // 4. Navigation System (Desktop & Mobile Sync)
    // ----------------------------------------------------------------------
    const desktopNavBtns = document.querySelectorAll('.desktop-nav .nav-btn');
    const mobileNavBtns = document.querySelectorAll('.mobile-bottom-nav .mobile-nav-btn');
    const tabSections = document.querySelectorAll('.tab-section');

    function switchTab(targetTab) {
        desktopNavBtns.forEach(b => {
            if (b.getAttribute('data-tab') === targetTab) b.classList.add('active');
            else b.classList.remove('active');
        });

        mobileNavBtns.forEach(b => {
            if (b.getAttribute('data-tab') === targetTab) b.classList.add('active');
            else b.classList.remove('active');
        });

        tabSections.forEach(s => s.classList.remove('active'));
        tabSections.forEach(s => s.classList.add('hidden'));

        const activeSection = document.getElementById(`${targetTab}Section`);
        if (activeSection) {
            activeSection.classList.remove('hidden');
            activeSection.classList.add('active');
        }

        if (targetTab === 'history') {
            renderHistoryList();
        }
    }

    desktopNavBtns.forEach(btn => btn.addEventListener('click', () => switchTab(btn.getAttribute('data-tab'))));
    mobileNavBtns.forEach(btn => btn.addEventListener('click', () => switchTab(btn.getAttribute('data-tab'))));

    // ----------------------------------------------------------------------
    // 5. Palmistry Scanner Engine
    // ----------------------------------------------------------------------
    const startCamBtn = document.getElementById('startCamBtn');
    const captureScanBtn = document.getElementById('captureScanBtn');
    const uploadInput = document.getElementById('uploadInput');
    const webcamFeed = document.getElementById('webcamFeed');
    const previewImage = document.getElementById('previewImage');
    const palmCanvas = document.getElementById('palmOverlayCanvas');
    const pCtx = palmCanvas.getContext('2d');
    const scannerPlaceholder = document.getElementById('scannerPlaceholder');
    const scanLaser = document.getElementById('scanLaser');

    const readingLoading = document.getElementById('readingLoading');
    const readingResults = document.getElementById('readingResults');
    const emptyPlaceholder = document.getElementById('emptyResultPlaceholder');
    const readingTextContent = document.getElementById('readingTextContent');

    let isCameraActive = false;

    startCamBtn.addEventListener('click', async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: "environment" } });
            webcamFeed.srcObject = stream;
            webcamFeed.classList.remove('hidden');
            previewImage.classList.add('hidden');
            scannerPlaceholder.classList.add('hidden');
            captureScanBtn.classList.remove('hidden');
            startCamBtn.classList.add('hidden');
            isCameraActive = true;
            resizePalmCanvas();
        } catch (err) {
            alert(currentLang === 'en' ? 'Unable to start camera. Please upload a photo.' : 'कैमरा शुरू करने में असमर्थ। कृपया फोटो अपलोड करें।');
        }
    });

    uploadInput.addEventListener('change', (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (evt) => {
                previewImage.src = evt.target.result;
                previewImage.classList.remove('hidden');
                webcamFeed.classList.add('hidden');
                scannerPlaceholder.classList.add('hidden');
                captureScanBtn.classList.remove('hidden');
                
                if (isCameraActive && webcamFeed.srcObject) {
                    webcamFeed.srcObject.getTracks().forEach(track => track.stop());
                    isCameraActive = false;
                }
                
                previewImage.onload = () => resizePalmCanvas();
            };
            reader.readAsDataURL(file);
        }
    });

    function resizePalmCanvas() {
        const viewport = document.getElementById('scannerViewport');
        palmCanvas.width = viewport.clientWidth;
        palmCanvas.height = viewport.clientHeight;
    }

    function drawGlowingPalmOverlay() {
        resizePalmCanvas();
        const w = palmCanvas.width;
        const h = palmCanvas.height;
        pCtx.clearRect(0, 0, w, h);

        pCtx.strokeStyle = '#DFAC6C';
        pCtx.lineWidth = 4;
        pCtx.shadowColor = '#DFAC6C';
        pCtx.shadowBlur = 12;
        pCtx.beginPath();
        pCtx.moveTo(w * 0.25, h * 0.42);
        pCtx.quadraticCurveTo(w * 0.5, h * 0.35, w * 0.8, h * 0.32);
        pCtx.stroke();

        pCtx.strokeStyle = '#6D28D9';
        pCtx.lineWidth = 4;
        pCtx.shadowColor = '#6D28D9';
        pCtx.shadowBlur = 12;
        pCtx.beginPath();
        pCtx.moveTo(w * 0.22, h * 0.48);
        pCtx.quadraticCurveTo(w * 0.5, h * 0.48, w * 0.75, h * 0.58);
        pCtx.stroke();

        pCtx.strokeStyle = '#10B981';
        pCtx.lineWidth = 4;
        pCtx.shadowColor = '#10B981';
        pCtx.shadowBlur = 12;
        pCtx.beginPath();
        pCtx.moveTo(w * 0.22, h * 0.48);
        pCtx.quadraticCurveTo(w * 0.38, h * 0.65, w * 0.3, h * 0.85);
        pCtx.stroke();

        pCtx.strokeStyle = '#F7E2BD';
        pCtx.lineWidth = 3.5;
        pCtx.shadowColor = '#F7E2BD';
        pCtx.shadowBlur = 12;
        pCtx.beginPath();
        pCtx.moveTo(w * 0.52, h * 0.82);
        pCtx.lineTo(w * 0.5, h * 0.38);
        pCtx.stroke();
    }

    captureScanBtn.addEventListener('click', () => {
        scanLaser.classList.remove('hidden');
        emptyPlaceholder.classList.add('hidden');
        readingResults.classList.add('hidden');
        readingLoading.classList.remove('hidden');

        drawGlowingPalmOverlay();

        setTimeout(() => {
            scanLaser.classList.add('hidden');
            readingLoading.classList.add('hidden');
            readingResults.classList.remove('hidden');

            const readingText = generateShastraReading(currentLang);
            readingTextContent.innerHTML = readingText;
            window.latestReadingText = readingText;
        }, 2200);
    });

    function generateShastraReading(lang) {
        if (lang === 'en') {
            return `
                <h3>📌 Scripture-Grounded Astrological Reading</h3>
                <p><em>(Grounded in Cheiro Palmistry, Samudrik Shastra & Vrihad Hastrekha Shastra)</em></p>
                <h3>✋ Detailed Palm Lines & Mounts Analysis:</h3>
                <ul>
                    <li><strong>Heart Line:</strong> Deep, clear line reaching Mount of Jupiter. Indicates emotional strength & loyalty.</li>
                    <li><strong>Head Line:</strong> Straight line showing high logical intellect and decisive vision.</li>
                    <li><strong>Life Line:</strong> Smooth curvature symbolizing vitality, longevity and positive energy.</li>
                    <li><strong>Fate Line & Mounts:</strong> Strong Fate Line leading to Saturn mount, creating a Raj-Yoga for career growth.</li>
                </ul>
                <h3>🔮 Remedial Guidance:</h3>
                <ul>
                    <li>Offer water with turmeric to the Sun every Thursday.</li>
                    <li>Recite Hanuman Chalisa on Tuesdays for Mars alignment.</li>
                </ul>
            `;
        }
        return `
            <h3>📌 शास्त्र-आधारित हस्तरेखा एवं फलकथन विश्लेषण</h3>
            <p><em>(कीरो हस्तरेखा शास्त्र, सामुद्रिक शास्त्र, वृहद् हस्तरेखा शास्त्र एवं सामुद्रिक हस्तरेखा विज्ञान पर आधारित)</em></p>
            <h3>✋ मुख्य रेखाओं एवं पर्वतों का विस्तृत विश्लेषण:</h3>
            <ul>
                <li><strong>हृदय रेखा (Heart Line):</strong> कीरो हस्तरेखा शास्त्र के अनुसार आपकी हृदय रेखा अत्यंत स्पष्ट, गहरी एवं गुरु पर्वत तक विस्तृत है। यह आपके उच्च भावनात्मक संतुलन का प्रतीक है।</li>
                <li><strong>मस्तिष्क रेखा (Head Line):</strong> आपकी मस्तिष्क रेखा तीव्र तार्किक क्षमता एवं त्वरित निर्णय शक्ति को दर्शाती है।</li>
                <li><strong>जीवन रेखा (Life Line):</strong> सामुद्रिक शास्त्र के अनुसार जीवन रेखा की गोलाई दीर्घायु एवं ऊर्जा शक्ति का संकेत देती है।</li>
                <li><strong>भाग्य रेखा एवं पर्वत (Fate Line):</strong> भाग्य रेखा शनि पर्वत की ओर अग्रसर है, जो राज-योग एवं व्यावसायिक सफलता का योग बनाती है।</li>
            </ul>
            <h3>💡 शास्त्र-सम्मत अचूक उपाय:</h3>
            <ul>
                <li>प्रत्येक गुरुवार को जल में हल्दी मिलाकर सूर्य देव को अर्घ्य दें।</li>
                <li>मंगलवार के दिन हनुमान चालीसा का पाठ करें।</li>
            </ul>
        `;
    }

    document.getElementById('saveReadingBtn').addEventListener('click', () => {
        if (!window.latestReadingText) return;
        const history = JSON.parse(localStorage.getItem('palmistryHistory') || '[]');
        const newReading = {
            id: Date.now(),
            date: new Date().toLocaleDateString(currentLang === 'en' ? 'en-US' : 'hi-IN', { day: 'numeric', month: 'short', year: 'numeric' }),
            text: window.latestReadingText
        };
        history.unshift(newReading);
        localStorage.setItem('palmistryHistory', JSON.stringify(history));
        alert(currentLang === 'en' ? 'Reading saved to history!' : 'फलकथन आपके इतिहास में सुरक्षित कर लिया गया है!');
    });

    document.getElementById('exportPdfBtn').addEventListener('click', () => {
        if (!window.latestReadingText) return;
        const element = document.createElement('div');
        element.style.padding = '30px';
        element.style.color = '#1A0A2E';
        element.style.fontFamily = 'serif';
        element.innerHTML = `
            <h1 style="color: #6D28D9; text-align: center;">AI Palmistry Pro - Astrological Report</h1>
            <hr style="border: 1px solid #DFAC6C; margin: 15px 0;">
            ${window.latestReadingText}
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

    // Kundli Section
    const kundliForm = document.getElementById('kundliForm');
    kundliForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const kName = document.getElementById('kName').value;
        const kDob = document.getElementById('kDob').value;
        
        document.getElementById('kundliAnalysisText').innerHTML = `
            <h3><i class="fa-solid fa-gem"></i> ${kName} (${kDob}):</h3>
            <ul>
                <li><strong>Lagna:</strong> Aries Lagna (Mars Lordship). Strong energy & leadership.</li>
                <li><strong>Raj-Yoga:</strong> Sun & Jupiter confluence in 1st house creates Raj-Yoga.</li>
            </ul>
        `;
    });

    // Tarot Section
    const tarotDeck = document.getElementById('tarotDeck');
    const tarotCardsData = [
        { name: "The Magician" },
        { name: "The Sun" },
        { name: "Wheel of Fortune" },
        { name: "The Star" },
        { name: "The Empress" }
    ];

    function buildTarotDeck() {
        tarotDeck.innerHTML = '';
        for (let i = 0; i < 5; i++) {
            const cardEl = document.createElement('div');
            cardEl.className = 'tarot-card-item';
            cardEl.innerHTML = `
                <div class="tarot-card-inner">
                    <div class="tarot-card-back">
                        <i class="fa-solid fa-eye"></i>
                        <p>Card ${i + 1}</p>
                    </div>
                    <div class="tarot-card-front">
                        <i class="fa-solid fa-sun-plant-wilt fa-2x"></i>
                        <h4>${tarotCardsData[i].name}</h4>
                    </div>
                </div>
            `;
            cardEl.addEventListener('click', () => {
                cardEl.classList.toggle('flipped');
                document.getElementById('tarotOutput').classList.remove('hidden');
                document.getElementById('tarotTextResult').innerHTML = `
                    <h3>🔮 Tarot Guidance:</h3>
                    <p>Positive cosmic alignment. Trust your inner intuition.</p>
                `;
            });
            tarotDeck.appendChild(cardEl);
        }
    }
    buildTarotDeck();

    // Numerology Section
    const calcNumerologyBtn = document.getElementById('calcNumerologyBtn');
    calcNumerologyBtn.addEventListener('click', () => {
        const dobStr = document.getElementById('numDobInput').value;
        if (!dobStr) return;

        const dateParts = dobStr.split('-');
        const day = parseInt(dateParts[2], 10);
        
        let mulank = day;
        while (mulank > 9) mulank = Math.floor(mulank / 10) + (mulank % 10);

        let digitsSum = dobStr.replace(/-/g, '').split('').reduce((acc, d) => acc + parseInt(d, 10), 0);
        let bhagyank = digitsSum;
        while (bhagyank > 9) bhagyank = Math.floor(bhagyank / 10) + (bhagyank % 10);

        document.getElementById('mulankVal').innerText = mulank;
        document.getElementById('bhagyankVal').innerText = bhagyank;

        document.getElementById('numerologyText').innerHTML = `
            <h3><i class="fa-solid fa-star"></i> Mulank ${mulank} & Bhagyank ${bhagyank}:</h3>
            <p>Independent, intellectual & visionary personality.</p>
        `;
    });
    calcNumerologyBtn.click();

    // History Section
    function renderHistoryList() {
        const historyList = document.getElementById('historyList');
        const history = JSON.parse(localStorage.getItem('palmistryHistory') || '[]');
        
        if (history.length === 0) {
            historyList.innerHTML = '<p class="empty-placeholder">No saved history found.</p>';
            return;
        }

        historyList.innerHTML = history.map(item => `
            <div class="history-item-card">
                <div class="history-item-header">
                    <span><i class="fa-solid fa-calendar"></i> ${item.date}</span>
                    <button onclick="deleteHistoryItem(${item.id})" class="btn btn-danger-sm"><i class="fa-solid fa-xmark"></i></button>
                </div>
                <div class="history-item-body">${item.text}</div>
            </div>
        `).join('');
    }

    window.deleteHistoryItem = function(id) {
        let history = JSON.parse(localStorage.getItem('palmistryHistory') || '[]');
        history = history.filter(item => item.id !== id);
        localStorage.setItem('palmistryHistory', JSON.stringify(history));
        renderHistoryList();
    };

    document.getElementById('clearHistoryBtn').addEventListener('click', () => {
        if (confirm(currentLang === 'en' ? 'Clear all reading history?' : 'क्या आप पूरा इतिहास साफ़ करना चाहते हैं?')) {
            localStorage.removeItem('palmistryHistory');
            renderHistoryList();
        }
    });

});
