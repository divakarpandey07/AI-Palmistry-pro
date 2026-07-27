const CACHE_NAME = 'ai-palmistry-pro-v22';

const CRITICAL_LOCAL_ASSETS = [
    './',
    './index.html',
    './css/style.css',
    './js/app.js',
    './js/modules/vision/palmDetector.js',
    './js/modules/viewer/hand3DViewer.js',
    './js/modules/ai/xaiEngine.js',
    './js/modules/ui/mainUI.js',
    './manifest.json'
];

const CDN_ASSETS = [
    'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css',
    'https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js',
    'https://cdn.jsdelivr.net/npm/three@0.128.0/examples/js/controls/OrbitControls.js',
    'https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js'
];

// FIX P3 ITEM 15: Resilient Service Worker Install Handler
self.addEventListener('install', (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME).then(async (cache) => {
            // Cache critical local assets atomically
            await cache.addAll(CRITICAL_LOCAL_ASSETS);
            // Cache CDN assets resiliently with Promise.allSettled()
            await Promise.allSettled(
                CDN_ASSETS.map(url => cache.add(url).catch(err => {
                    console.warn(`Resilient SW: CDN asset fetch skipped (${url}):`, err);
                }))
            );
        })
    );
    self.skipWaiting();
});

self.addEventListener('activate', (event) => {
    event.waitUntil(
        caches.keys().then((keys) => {
            return Promise.all(
                keys.map((key) => {
                    if (key !== CACHE_NAME) {
                        return caches.delete(key);
                    }
                })
            );
        })
    );
    self.clients.claim();
});

self.addEventListener('fetch', (event) => {
    event.respondWith(
        caches.match(event.request).then((cachedResponse) => {
            if (cachedResponse) {
                return cachedResponse;
            }
            return fetch(event.request).catch(() => {
                if (event.request.mode === 'navigate') {
                    return caches.match('./index.html');
                }
            });
        })
    );
});
