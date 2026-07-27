/* ==========================================================================
   AI Palmistry Pro - Interactive JavaScript PWA Mobile App Engine
   ========================================================================== */

document.addEventListener('DOMContentLoaded', () => {
    
    // ----------------------------------------------------------------------
    // 1. PWA Service Worker Registration & Installation Manager
    // ----------------------------------------------------------------------
    let deferredPrompt = null;
    const pwaInstallBanner = document.getElementById('pwaInstallBanner');
    const pwaInstallBtn = document.getElementById('pwaInstallBtn');

    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('./sw.js')
            .then(reg => console.log('Service Worker Registered Successfully:', reg.scope))
            .catch(err => console.error('Service Worker Registration Failed:', err));
    }

    // Capture PWA Install Prompt Event
    window.addEventListener('beforeinstallprompt', (e) => {
        e.preventDefault();
        deferredPrompt = e;
        if (pwaInstallBanner) {
            pwaInstallBanner.classList.remove('hidden');
        }
    });

    if (pwaInstallBtn) {
        pwaInstallBtn.addEventListener('click', async () => {
            if (deferredPrompt) {
                deferredPrompt.prompt();
                const { outcome } = await deferredPrompt.userChoice;
                if (outcome === 'accepted') {
                    console.log('User accepted PWA installation');
                }
                deferredPrompt = null;
                pwaInstallBanner.classList.add('hidden');
            }
        });
    }

    // ----------------------------------------------------------------------
    // 2. Cosmic Starfield Background Canvas
    // ----------------------------------------------------------------------
    const canvas = document.getElementById('starsCanvas');
    const ctx = canvas.getContext('2d');
    let stars = [];

    function initCanvas() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
        stars = [];
        for (let i = 0; i < 150; i++) {
            stars.push({
                x: Math.random() * canvas.width,
                y: Math.random() * canvas.height,
                radius: Math.random() * 1.5 + 0.5,
                alpha: Math.random(),
                speed: Math.random() * 0.02 + 0.005
            });
        }
    }

    function animateStars() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        stars.forEach(star => {
            star.alpha += star.speed;
            if (star.alpha > 1 || star.alpha < 0) star.speed = -star.speed;
            ctx.fillStyle = `rgba(245, 158, 11, ${Math.abs(star.alpha)})`;
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
    // 3. Navigation System (Desktop & Mobile Bottom Bar Sync)
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

    desktopNavBtns.forEach(btn => {
        btn.addEventListener('click', () => switchTab(btn.getAttribute('data-tab')));
    });

    mobileNavBtns.forEach(btn => {
        btn.addEventListener('click', () => switchTab(btn.getAttribute('data-tab')));
    });

    // ----------------------------------------------------------------------
    // 4. Palmistry Scanner Engine
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

    // Camera Feed Handler
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
            alert('कैमरा शुरू करने में असमर्थ। कृपया फ़ोटो अपलोड विकल्प का उपयोग करें।');
        }
    });

    // File Upload Handler
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

        pCtx.strokeStyle = '#EC4899';
        pCtx.lineWidth = 4;
        pCtx.shadowColor = '#EC4899';
        pCtx.shadowBlur = 12;
        pCtx.beginPath();
        pCtx.moveTo(w * 0.25, h * 0.42);
        pCtx.quadraticCurveTo(w * 0.5, h * 0.35, w * 0.8, h * 0.32);
        pCtx.stroke();

        pCtx.strokeStyle = '#7C3AED';
        pCtx.lineWidth = 4;
        pCtx.shadowColor = '#7C3AED';
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

        pCtx.strokeStyle = '#F59E0B';
        pCtx.lineWidth = 3.5;
        pCtx.shadowColor = '#F59E0B';
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

            const readingText = generatePureHindiShastraReading();
            readingTextContent.innerHTML = readingText;
            window.latestReadingText = readingText;
        }, 2200);
    });

    function generatePureHindiShastraReading() {
        return `
            <h3>📌 शास्त्र-आधारित हस्तरेखा एवं फलकथन विश्लेषण</h3>
            <p><em>(कीरो हस्तरेखा शास्त्र, सामुद्रिक शास्त्र, वृहद् हस्तरेखा शास्त्र एवं सामुद्रिक हस्तरेखा विज्ञान पर आधारित)</em></p>
            
            <h3>✋ मुख्य रेखाओं एवं पर्वतों का विस्तृत विश्लेषण:</h3>
            <ul>
                <li><strong>हृदय रेखा (Heart Line):</strong> कीरो हस्तरेखा शास्त्र के अनुसार आपकी हृदय रेखा अत्यंत स्पष्ट, गहरी एवं गुरु पर्वत तक विस्तृत है। यह आपके उच्च भावनात्मक संतुलन, निष्ठावान स्वभाव एवं प्रगाढ़ आत्म-बल का प्रतीक है।</li>
                <li><strong>मस्तिष्क रेखा (Head Line):</strong> वृहद् हस्तरेखा शास्त्र के अनुसार आपकी मस्तिष्क रेखा सीधी एवं सुदृढ़ है। यह आपकी तीव्र तार्किक क्षमता, दूरदर्शिता एवं त्वरित निर्णय शक्ति को दर्शाती है।</li>
                <li><strong>जीवन रेखा (Life Line):</strong> सामुद्रिक शास्त्र के अनुसार आपकी जीवन रेखा की गोलाई आरोग्य, दीर्घायु एवं असीम ऊर्जा शक्ति का संकेत देती है।</li>
                <li><strong>भाग्य रेखा एवं पर्वत (Fate Line & Mounts):</strong> गुरु एवं मंगल पर्वत पूर्ण विकसित हैं। भाग्य रेखा मणिकंठ से निकलकर शनि पर्वत की ओर अग्रसर है, जो राज-योग एवं अपार व्यावसायिक सफलता का योग निर्मित करती है।</li>
            </ul>

            <h3>🔮 पुस्तक-आधारित काल निर्धारण एवं भविष्यवाणियां:</h3>
            <ul>
                <li><strong>करियर एवं धन योग:</strong> आगामी 12 से 16 महीनों के भीतर गुरु एवं शनि के अनुकूल प्रभाव से आपके जीवन में नए व्यापारिक अवसर, पदोन्नति एवं धन लाभ के प्रबल योग हैं।</li>
                <li><strong>स्वास्थ्य एवं पारिवारिक सुख:</strong> आपका स्वास्थ्य अनुकूल रहेगा तथा परिजनों एवं मित्रों का पूर्ण सहयोग प्राप्त होगा।</li>
            </ul>

            <h3>💡 शास्त्र-सम्मत अचूक उपाय एवं मार्गदर्शन:</h3>
            <ul>
                <li>प्रत्येक गुरुवार को जल में हल्दी मिलाकर सूर्य देव को अर्घ्य दें तथा <em>ॐ बृं बृहस्पतये नमः</em> मंत्र का नियमित 108 बार जाप करें।</li>
                <li>मंगलवार के दिन हनुमान चालीसा अथवा सुंदरकांड का पाठ करें, जिससे मंगल ग्रह का प्रभाव अत्यंत शुभ बना रहेगा।</li>
            </ul>
        `;
    }

    document.getElementById('saveReadingBtn').addEventListener('click', () => {
        if (!window.latestReadingText) return;
        const history = JSON.parse(localStorage.getItem('palmistryHistory') || '[]');
        const newReading = {
            id: Date.now(),
            date: new Date().toLocaleDateString('hi-IN', { day: 'numeric', month: 'short', year: 'numeric' }),
            text: window.latestReadingText
        };
        history.unshift(newReading);
        localStorage.setItem('palmistryHistory', JSON.stringify(history));
        alert('फलकथन आपके इतिहास में सुरक्षित कर लिया गया है!');
    });

    document.getElementById('exportPdfBtn').addEventListener('click', () => {
        if (!window.latestReadingText) return;
        const element = document.createElement('div');
        element.style.padding = '30px';
        element.style.color = '#1A0A2E';
        element.style.fontFamily = 'serif';
        element.innerHTML = `
            <h1 style="color: #7C3AED; text-align: center;">AI Palmistry Pro - वैदिक रिपोर्ट</h1>
            <hr style="border: 1px solid #F59E0B; margin: 15px 0;">
            ${window.latestReadingText}
            <br><hr>
            <p style="text-align: center; font-size: 12px; color: #666;">© 2026 AI Palmistry Pro PWA. सर्वाधिकार सुरक्षित।</p>
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
            <h3><i class="fa-solid fa-gem"></i> ${kName} जी की जन्म पत्रिका (${kDob}):</h3>
            <ul>
                <li><strong>लग्न एवं स्वामी:</strong> मेष लग्न (स्वामी मंगल)। यह आपको ऊर्जा व नेतृत्व शक्ति प्रदान करता है।</li>
                <li><strong>सूर्य व गुरु (प्रथम भाव):</strong> प्रथम भाव में सूर्य-गुरु की युति से उच्च राजयोग बनता है।</li>
                <li><strong>शनि महादशा फलादेश:</strong> शनिवार को पीपल पर सरसों तेल का दीपक जलाएं।</li>
            </ul>
        `;
    });

    // Tarot Section
    const tarotDeck = document.getElementById('tarotDeck');
    const tarotCardsData = [
        { name: "द मैजिशियन (The Magician)" },
        { name: "द सन (The Sun)" },
        { name: "द व्हील ऑफ फॉर्च्यून (Wheel of Fortune)" },
        { name: "द स्टार (The Star)" },
        { name: "द एम्प्रेस (The Empress)" }
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
                        <p>कार्ड ${i + 1}</p>
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
                    <h3>🔮 दिव्य टैरो फलादेश:</h3>
                    <p><strong>वर्तमान स्थिति:</strong> आपके द्वारा चुने गए ताश के पत्ते बताते हैं कि आपके जीवन में सकारात्मक ऊर्जा का संचार हो रहा है।</p>
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

        const planets = ["", "सूर्य", "चंद्र", "गुरु", "राहु", "बुध", "शुक्र", "केतु", "शनि", "मंगल"];

        document.getElementById('mulankVal').innerText = mulank;
        document.getElementById('mulankPlanet').innerText = `स्वामी: ${planets[mulank] || ''}`;
        document.getElementById('bhagyankVal').innerText = bhagyank;
        document.getElementById('bhagyankPlanet').innerText = `स्वामी: ${planets[bhagyank] || ''}`;

        document.getElementById('numerologyText').innerHTML = `
            <h3><i class="fa-solid fa-star"></i> मूलांक ${mulank} एवं भाग्यांक ${bhagyank} का फलकथन:</h3>
            <ul>
                <li><strong>व्यक्तित्व:</strong> बौद्धिक, विचारशील एवं स्वतंत्र विचारधारा वाले व्यक्ति।</li>
                <li><strong>शुभ रंग:</strong> पीला, हरा एवं सफेद।</li>
            </ul>
        `;
    });
    calcNumerologyBtn.click();

    // History Section
    function renderHistoryList() {
        const historyList = document.getElementById('historyList');
        const history = JSON.parse(localStorage.getItem('palmistryHistory') || '[]');
        
        if (history.length === 0) {
            historyList.innerHTML = '<p class="empty-placeholder">कोई सहेजा गया इतिहास नहीं मिला।</p>';
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
        if (confirm('क्या आप पूरा इतिहास साफ़ करना चाहते हैं?')) {
            localStorage.removeItem('palmistryHistory');
            renderHistoryList();
        }
    });

});
