/* ==========================================================================
   AI Palmistry Pro - Apple/VisionOS Clean Luxury UI Controller
   Handles Tabs, Theme Switcher, Audio Synthesizer & i18n Dictionary
   ========================================================================== */

export class MainUIController {
    constructor() {
        this.currentLang = localStorage.getItem('selectedLang') || 'hi';
        this.currentTheme = localStorage.getItem('appTheme') || 'gold';
    }

    initUI() {
        this.setupNavigation();
        this.setupThemeToggle();
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
}
