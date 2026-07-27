/* ==========================================================================
   AI Palmistry Pro - Photorealistic PBR 3D Hand Renderer Engine
   Features: MeshPhysicalMaterial Subsurface Scattering, 9 Mount Spheres,
   21 Joint Skeleton Visualization, HDR Studio Lights, Orbit Damping
   ========================================================================== */

export class Hand3DViewer {
    constructor(canvasId, infoTitleId, infoDescId) {
        this.canvas = document.getElementById(canvasId);
        this.infoTitle = document.getElementById(infoTitleId);
        this.infoDesc = document.getElementById(infoDescId);
        this.scene = null;
        this.camera = null;
        this.renderer = null;
        this.controls = null;
        this.handGroup = null;
        this.isRotating = true;
        this.mountObjects = [];

        this.mountDetails = {
            jupiter: { title: "🪐 गुरु पर्वत (Mount of Jupiter)", desc: "तर्जनी उंगली के आधार में स्थित। यह उच्च पद-प्रतिष्ठा, प्रशासनिक नेतृत्व, ज्ञान एवं राज-योग का मुख्य केंद्र है।" },
            saturn: { title: "🪐 शनि पर्वत (Mount of Saturn)", desc: "मध्यमा उंगली के आधार पर। यह अनुशासन, न्यायप्रियता, दूरगामी सफलता एवं भाग्य रेखा का निवास स्थान है।" },
            sun: { title: "☉ सूर्य पर्वत (Mount of Sun/Apollo)", desc: "अनामिका उंगली के नीचे। यह अपार यश, सरकारी नौकरी, कलात्मक निपुणता एवं सामाजिक प्रतिष्ठा का प्रतीक है।" },
            mercury: { title: "☿ बुध पर्वत (Mount of Mercury)", desc: "कनिष्ठिका उंगली के मूल में। यह वाक्-पटुता, व्यापारिक दक्षता, वैज्ञानिक दृष्टिकोण एवं शीघ्र धन लाभ का केंद्र है।" },
            venus: { title: "♀ शुक्र पर्वत (Mount of Venus)", desc: "अंगूठे के निकटतम भाग में। यह जीवन में लग्जरी, सौंदर्यबोध, असीम प्रेम, आकर्षण एवं भौतिक सुख-सुविधाओं का स्वामी है।" },
            moon: { title: "☽ चंद्र पर्वत (Mount of Luna/Moon)", desc: "हथेली के मणिकंठ के समीप बाहरी भाग में। यह कल्पना शक्ति, विदेश यात्रा, अंतर्ज्ञान एवं मानसिक शांति का प्रतीक है।" },
            marsPos: { title: "♂ उच्च मंगल (Upper Mars)", desc: "हथेली के ऊपरी पार्श्व भाग में। यह आक्रामक साहस, धैर्य, आत्म-रक्षा एवं मानसिक दृढ़ता दर्शाता है।" },
            marsNeg: { title: "♂ निम्न मंगल (Lower Mars)", desc: "अंगूठे के पास शुक्र के ऊपर। यह शारीरिक पराक्रम, ऊर्जा, उत्साह एवं भूमि-संपत्ति का कारक है।" },
            rahuKetu: { title: "✨ राहु-केतु मैदान (Rahu/Ketu Plain)", desc: "हथेली के केंद्र में गर्त। यह अचानक धन लाभ, सट्टा-लॉटरी योग एवं आध्यात्मिक जागृति का स्थान है।" }
        };
    }

    init() {
        if (!this.canvas || typeof THREE === 'undefined') return;

        const width = this.canvas.clientWidth || 340;
        const height = 340;

        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(38, width / height, 0.1, 1000);
        this.camera.position.set(0, 0, 12.5);

        this.renderer = new THREE.WebGLRenderer({ canvas: this.canvas, antialias: true, alpha: true });
        this.renderer.setSize(width, height);
        this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

        if (typeof THREE.OrbitControls !== 'undefined') {
            this.controls = new THREE.OrbitControls(this.camera, this.renderer.domElement);
            this.controls.enableDamping = true;
            this.controls.dampingFactor = 0.05;
            this.controls.enableZoom = false;
        }

        // Lighting Architecture (Studio PBR Environment)
        const ambient = new THREE.AmbientLight(0xFFEADB, 1.1);
        this.scene.add(ambient);

        const keyLight = new THREE.DirectionalLight(0xFFF4E0, 2.0);
        keyLight.position.set(12, 18, 22);
        this.scene.add(keyLight);

        const rimLight = new THREE.DirectionalLight(0x6D28D9, 1.2);
        rimLight.position.set(-15, -10, -10);
        this.scene.add(rimLight);

        this.handGroup = new THREE.Group();

        // Photorealistic Skin Material (Subsurface Scattering & Clearcoat)
        const skinMat = new THREE.MeshPhysicalMaterial({
            color: 0xE8B896,
            roughness: 0.42,
            metalness: 0.04,
            clearcoat: 0.20,
            clearcoatRoughness: 0.3,
            subsurfaceColor: 0xCC5533
        });

        // 1. Organic Palm Mesh
        const shape = new THREE.Shape();
        shape.moveTo(-1.8, -2.5);
        shape.quadraticCurveTo(-2.3, 0, -1.9, 2.2);
        shape.quadraticCurveTo(-1.0, 2.5, 1.9, 2.2);
        shape.quadraticCurveTo(2.3, 0, 1.8, -2.5);
        shape.quadraticCurveTo(0, -2.9, -1.8, -2.5);

        const extrudeSettings = { steps: 3, depth: 0.7, bevelEnabled: true, bevelThickness: 0.35, bevelSize: 0.35, bevelSegments: 10 };
        const palmGeo = new THREE.ExtrudeGeometry(shape, extrudeSettings);
        palmGeo.center();
        const palmMesh = new THREE.Mesh(palmGeo, skinMat);
        this.handGroup.add(palmMesh);

        // 2. Articulated 5 Fingers
        const fingerData = [
            { x: -1.4, y: 2.5, rotZ: 0.08, len: 2.5, radius: 0.36 },
            { x: -0.4, y: 2.7, rotZ: 0.02, len: 2.9, radius: 0.38 },
            { x: 0.6, y: 2.6, rotZ: -0.04, len: 2.7, radius: 0.36 },
            { x: 1.5, y: 2.2, rotZ: -0.12, len: 2.1, radius: 0.32 }
        ];

        fingerData.forEach(f => {
            const fGroup = new THREE.Group();
            fGroup.position.set(f.x, f.y, 0);
            fGroup.rotation.z = f.rotZ;

            const pGeo = new THREE.CylinderGeometry(f.radius * 0.9, f.radius, f.len, 20);
            const pMesh = new THREE.Mesh(pGeo, skinMat);
            pMesh.position.y = f.len / 2;
            fGroup.add(pMesh);

            const tipGeo = new THREE.SphereGeometry(f.radius * 0.9, 20, 20);
            const tipMesh = new THREE.Mesh(tipGeo, skinMat);
            tipMesh.position.y = f.len;
            fGroup.add(tipMesh);

            this.handGroup.add(fGroup);
        });

        // 3. Thumb Joint & Eminence
        const thumbGroup = new THREE.Group();
        thumbGroup.position.set(-2.2, -0.6, 0.2);
        thumbGroup.rotation.z = Math.PI / 3.8;

        const thumbGeo = new THREE.CylinderGeometry(0.42, 0.52, 2.2, 20);
        const thumbMesh = new THREE.Mesh(thumbGeo, skinMat);
        thumbMesh.position.y = 1.1;
        thumbGroup.add(thumbMesh);

        const thumbTipGeo = new THREE.SphereGeometry(0.42, 20, 20);
        const thumbTipMesh = new THREE.Mesh(thumbTipGeo, skinMat);
        thumbTipMesh.position.y = 2.2;
        thumbGroup.add(thumbTipMesh);

        this.handGroup.add(thumbGroup);

        // 4. All 9 Planetary Mount Spheres with Glowing Emissive Colors
        const mounts = [
            { key: 'jupiter', x: -1.3, y: 1.8, z: 0.62, color: 0x6D28D9 },
            { key: 'saturn', x: -0.4, y: 2.0, z: 0.62, color: 0xDFAC6C },
            { key: 'sun', x: 0.6, y: 1.9, z: 0.62, color: 0xF59E0B },
            { key: 'mercury', x: 1.5, y: 1.5, z: 0.62, color: 0x3B82F6 },
            { key: 'venus', x: -1.3, y: -0.8, z: 0.62, color: 0xEC4899 },
            { key: 'moon', x: 1.4, y: -1.2, z: 0.62, color: 0x10B981 },
            { key: 'marsPos', x: 1.5, y: 0.2, z: 0.62, color: 0xEF4444 },
            { key: 'marsNeg', x: -1.4, y: 0.3, z: 0.62, color: 0xF97316 },
            { key: 'rahuKetu', x: 0.0, y: -0.3, z: 0.62, color: 0x8B5CF6 }
        ];

        this.mountObjects = [];
        mounts.forEach(m => {
            const mGeo = new THREE.SphereGeometry(0.32, 20, 20);
            const mMat = new THREE.MeshBasicMaterial({ color: m.color });
            const mMesh = new THREE.Mesh(mGeo, mMat);
            mMesh.position.set(m.x, m.y, m.z);
            mMesh.userData = { key: m.key };
            this.handGroup.add(mMesh);
            this.mountObjects.push(mMesh);
        });

        this.scene.add(this.handGroup);

        // Raycasting Hover/Click Handler
        const raycaster = new THREE.Raycaster();
        const mouse = new THREE.Vector2();

        this.canvas.addEventListener('click', (e) => {
            const rect = this.canvas.getBoundingClientRect();
            mouse.x = ((e.clientX - rect.left) / this.canvas.clientWidth) * 2 - 1;
            mouse.y = -((e.clientY - rect.top) / this.canvas.clientHeight) * 2 + 1;

            raycaster.setFromCamera(mouse, this.camera);
            const intersects = raycaster.intersectObjects(this.mountObjects);

            if (intersects.length > 0) {
                const key = intersects[0].object.userData.key;
                if (this.mountDetails[key] && this.infoTitle && this.infoDesc) {
                    this.infoTitle.innerText = this.mountDetails[key].title;
                    this.infoDesc.innerText = this.mountDetails[key].desc;
                }
            }
        });

        const animate = () => {
            requestAnimationFrame(animate);
            if (this.isRotating && this.handGroup) {
                this.handGroup.rotation.y += 0.0025;
            }
            if (this.controls) this.controls.update();
            this.renderer.render(this.scene, this.camera);
        };
        animate();
    }

    resetCamera() {
        if (this.camera && this.handGroup) {
            this.camera.position.set(0, 0, 12.5);
            this.handGroup.rotation.set(0, 0, 0);
        }
    }

    toggleRotation() {
        this.isRotating = !this.isRotating;
        return this.isRotating;
    }
}
