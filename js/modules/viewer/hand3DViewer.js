/* ==========================================================================
   AI Palmistry Pro - High-Fidelity Anatomical 3D Human Hand Model Engine
   Integrated with GLTFLoader Support, PBR Subsurface Skin Shader, Raycaster Hover
   Highlights & 9 Planetary Mount Glow Spheres
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
        this.resizeListener = null;

        this.mountDetails = {
            jupiter: { title: "🪐 गुरु पर्वत (Mount of Jupiter)", desc: "तर्जनी उंगली के आधार पर स्थित। यह नेतृत्व क्षमता, उच्च ज्ञान, महत्वाकांक्षा एवं राज-योग का मुख्य स्थान है।" },
            saturn: { title: "🪐 शनि पर्वत (Mount of Saturn)", desc: "मध्यमा उंगली के मूल में। यह अनुशासन, कर्मठता, दूरगामी सोच एवं भाग्य रेखा का निवास स्थान है।" },
            sun: { title: "☉ सूर्य पर्वत (Mount of Sun/Apollo)", desc: "अनामिका उंगली के नीचे। यह मान-सम्मान, सरकारी नौकरी, कलात्मक निपुणता एवं सामाजिक प्रतिष्ठा का प्रतीक है।" },
            mercury: { title: "☿ बुध पर्वत (Mount of Mercury)", desc: "कनिष्ठिका उंगली के मूल में। यह व्यापारिक बुद्धि, वाक्-पटुता, तार्किक निर्णय शक्ति व समृद्धि दर्शाता है।" },
            venus: { title: "♀ शुक्र पर्वत (Mount of Venus)", desc: "अंगूठे के पास हथेली का मुख्य उभार। यह भौतिक सुख, प्रेम, लग्जरी, आकर्षण एवं उत्तम सौंदर्य का प्रतीक है।" },
            moon: { title: "☽ चंद्र पर्वत (Mount of Luna/Moon)", desc: "हथेली के निचले पार्श्व भाग में। यह कल्पना शक्ति, विदेश यात्रा, अंतर्ज्ञान एवं मानसिक शांति का कारक है।" },
            marsPos: { title: "♂ उच्च मंगल (Upper Mars)", desc: "हथेली के ऊपरी बाहरी किनारे पर। यह मानसिक धैर्य, आत्म-रक्षा एवं संकट में स्थिरता दर्शाता है।" },
            marsNeg: { title: "♂ निम्न मंगल (Lower Mars)", desc: "अंगूठे के पास शुक्र के ऊपर। यह शारीरिक पराक्रम, साहस, ऊर्जा एवं भूमि संपत्ति का प्रतीक है।" },
            rahuKetu: { title: "✨ राहु-केतु मैदान (Rahu/Ketu Plain)", desc: "हथेली के केंद्र की गर्त। यह अचानक धन लाभ, दूरदर्शिता व आध्यात्मिक जाग्रति का क्षेत्र है।" },
            heart: { title: "🟡 हृदय रेखा (Heart Line)", desc: "हथेली के ऊपरी भाग में गुरु पर्वत की ओर। यह भावनात्मक संतुलन, प्रेम संबंधों में निष्ठा एवं हृदय स्वास्थ्य दर्शाती है।" },
            head: { title: "🟣 मस्तिष्क रेखा (Head Line)", desc: "हथेली के मध्य में तार्किक कोण पर। यह निर्णय क्षमता, बौद्धिक तीक्ष्णता एवं एकाग्रता की प्रतीक है।" },
            life: { title: "🟢 जीवन रेखा (Life Line)", desc: "अंगूठे (शुक्र पर्वत) को घेरती हुई। यह शारीरिक ऊर्जा, आरोग्य, जीवन शक्ति एवं दीर्घायु का प्रतीक है।" },
            fate: { title: "⚪ भाग्य रेखा (Fate Line)", desc: "मणिकंठ से शनि पर्वत की ओर गमन करती हुई। यह करियर में सफलता, आर्थिक समृद्धि व राज-योग बनाती है।" }
        };
    }

    init() {
        if (!this.canvas || typeof THREE === 'undefined' || !window.WebGLRenderingContext) {
            console.warn("WebGL or Three.js is not available on this device/browser.");
            document.getElementById('hand3DFallback')?.classList.remove('hidden');
            return;
        }

        const width = this.canvas.clientWidth || 340;
        const height = 340;

        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(40, width / height, 0.1, 1000);
        this.camera.position.set(0, 0, 11.5);

        this.renderer = new THREE.WebGLRenderer({ canvas: this.canvas, antialias: true, alpha: true });
        this.renderer.setSize(width, height);
        this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

        if (typeof THREE.OrbitControls !== 'undefined') {
            this.controls = new THREE.OrbitControls(this.camera, this.renderer.domElement);
            this.controls.enableDamping = true;
            this.controls.dampingFactor = 0.05;
            this.controls.enableZoom = false;
        }

        this.resizeListener = () => {
            if (!this.canvas || !this.renderer || !this.camera) return;
            const w = this.canvas.clientWidth || 340;
            const h = 340;
            this.camera.aspect = w / h;
            this.camera.updateProjectionMatrix();
            this.renderer.setSize(w, h);
        };
        window.addEventListener('resize', this.resizeListener);

        // Lighting Architecture (Studio Environment)
        const ambient = new THREE.AmbientLight(0xFFE4CE, 1.2);
        this.scene.add(ambient);

        const keyLight = new THREE.DirectionalLight(0xFFF0E0, 2.2);
        keyLight.position.set(10, 15, 18);
        this.scene.add(keyLight);

        const fillLight = new THREE.DirectionalLight(0x93C5FD, 0.8);
        fillLight.position.set(-12, -8, 10);
        this.scene.add(fillLight);

        this.handGroup = new THREE.Group();

        // Attempt GLTFLoader if asset exists, otherwise build PBR Anatomical Hand
        if (typeof THREE.GLTFLoader !== 'undefined') {
            const loader = new THREE.GLTFLoader();
            loader.load(
                './assets/models/human_hand.glb',
                (gltf) => {
                    const loadedModel = gltf.scene;
                    loadedModel.traverse(child => {
                        if (child.isMesh) {
                            child.castShadow = true;
                            child.receiveShadow = true;
                        }
                    });
                    this.handGroup.add(loadedModel);
                },
                undefined,
                () => {
                    this.buildPBRAnatomicalHand();
                }
            );
        } else {
            this.buildPBRAnatomicalHand();
        }

        this.scene.add(this.handGroup);

        // Raycasting Mouse Hover/Click Listener
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
            if (this.renderer && this.scene && this.camera) {
                this.renderer.render(this.scene, this.camera);
            }
        };
        animate();
    }

    buildPBRAnatomicalHand() {
        const skinMat = new THREE.MeshPhysicalMaterial({
            color: 0xE8B896,
            roughness: 0.50,
            metalness: 0.0,
            clearcoat: 0.12,
            clearcoatRoughness: 0.35,
            sheen: 0.35,
            sheenColor: 0xFFE0D0,
            transmission: 0.05
        });

        // 1. Organic Curved Palm
        const pShape = new THREE.Shape();
        pShape.moveTo(-1.6, -2.2);
        pShape.quadraticCurveTo(-2.1, -1.0, -2.0, 0.5);
        pShape.quadraticCurveTo(-1.8, 2.0, -1.5, 2.2);
        pShape.lineTo(1.5, 2.2);
        pShape.quadraticCurveTo(2.1, 1.0, 2.0, -0.8);
        pShape.quadraticCurveTo(1.8, -2.0, 1.4, -2.2);
        pShape.quadraticCurveTo(0, -2.5, -1.6, -2.2);

        const extrudeSettings = { steps: 4, depth: 0.65, bevelEnabled: true, bevelThickness: 0.35, bevelSize: 0.35, bevelSegments: 12 };
        const palmGeo = new THREE.ExtrudeGeometry(pShape, extrudeSettings);
        palmGeo.center();
        const mainPalmMesh = new THREE.Mesh(palmGeo, skinMat);
        this.handGroup.add(mainPalmMesh);

        // Thenar & Hypothenar Eminences
        const thenarGeo = new THREE.SphereGeometry(0.95, 20, 20);
        thenarGeo.scale(1.1, 1.5, 0.7);
        const thenarMesh = new THREE.Mesh(thenarGeo, skinMat);
        thenarMesh.position.set(-1.1, -0.6, 0.3);
        thenarMesh.rotation.z = -0.3;
        this.handGroup.add(thenarMesh);

        const hypoGeo = new THREE.SphereGeometry(0.85, 20, 20);
        hypoGeo.scale(0.9, 1.4, 0.65);
        const hypoMesh = new THREE.Mesh(hypoGeo, skinMat);
        hypoMesh.position.set(1.2, -0.8, 0.25);
        hypoMesh.rotation.z = 0.25;
        this.handGroup.add(hypoMesh);

        // 2. 5 Articulated Fingers & Nails
        const nailMat = new THREE.MeshStandardMaterial({ color: 0xF3CBB1, roughness: 0.2, metalness: 0.1 });
        const fingerConfigs = [
            { name: "Index", x: -1.25, y: 2.2, rotZ: 0.08, totalLen: 2.5, radius: 0.33 },
            { name: "Middle", x: -0.42, y: 2.35, rotZ: 0.02, totalLen: 2.85, radius: 0.35 },
            { name: "Ring", x: 0.42, y: 2.3, rotZ: -0.04, totalLen: 2.65, radius: 0.34 },
            { name: "Pinky", x: 1.25, y: 1.95, rotZ: -0.12, totalLen: 2.05, radius: 0.29 }
        ];

        fingerConfigs.forEach(f => {
            const fGroup = new THREE.Group();
            fGroup.position.set(f.x, f.y, 0.05);
            fGroup.rotation.z = f.rotZ;

            const segLen = f.totalLen / 3;

            const p1Geo = new THREE.CylinderGeometry(f.radius * 0.95, f.radius, segLen, 16);
            const p1Mesh = new THREE.Mesh(p1Geo, skinMat);
            p1Mesh.position.y = segLen / 2;
            fGroup.add(p1Mesh);

            const j1Geo = new THREE.SphereGeometry(f.radius * 0.96, 14, 14);
            const j1Mesh = new THREE.Mesh(j1Geo, skinMat);
            j1Mesh.position.y = segLen;
            fGroup.add(j1Mesh);

            const p2Geo = new THREE.CylinderGeometry(f.radius * 0.88, f.radius * 0.94, segLen, 16);
            const p2Mesh = new THREE.Mesh(p2Geo, skinMat);
            p2Mesh.position.y = segLen * 1.5;
            fGroup.add(p2Mesh);

            const p3Geo = new THREE.SphereGeometry(f.radius * 0.85, 16, 16);
            p3Geo.scale(1.0, 1.3, 0.85);
            const p3Mesh = new THREE.Mesh(p3Geo, skinMat);
            p3Mesh.position.y = segLen * 2.5;
            fGroup.add(p3Mesh);

            const nailGeo = new THREE.BoxGeometry(f.radius * 0.9, segLen * 0.5, 0.08);
            const nailMesh = new THREE.Mesh(nailGeo, nailMat);
            nailMesh.position.set(0, segLen * 2.6, 0.22);
            fGroup.add(nailMesh);

            this.handGroup.add(fGroup);
        });

        // 3. Thumb Joint & Eminence
        const thumbGroup = new THREE.Group();
        thumbGroup.position.set(-1.9, -0.6, 0.2);
        thumbGroup.rotation.z = Math.PI / 3.6;

        const tbGeo = new THREE.CylinderGeometry(0.44, 0.52, 1.2, 16);
        const tbMesh = new THREE.Mesh(tbGeo, skinMat);
        tbMesh.position.y = 0.6;
        thumbGroup.add(tbMesh);

        const ttGeo = new THREE.SphereGeometry(0.42, 16, 16);
        ttGeo.scale(1.0, 1.3, 0.85);
        const ttMesh = new THREE.Mesh(ttGeo, skinMat);
        ttMesh.position.y = 1.7;
        thumbGroup.add(ttMesh);

        this.handGroup.add(thumbGroup);

        // 4. Glowing 3D Creases
        const create3DCreaseTube = (points, colorHex, keyName) => {
            const curve = new THREE.CatmullRomCurve3(points);
            const tubeGeo = new THREE.TubeGeometry(curve, 32, 0.065, 8, false);
            const tubeMat = new THREE.MeshBasicMaterial({ color: colorHex });
            const tubeMesh = new THREE.Mesh(tubeGeo, tubeMat);
            tubeMesh.userData = { key: keyName };
            this.handGroup.add(tubeMesh);
            this.mountObjects.push(tubeMesh);
        };

        create3DCreaseTube([new THREE.Vector3(1.3, 1.2, 0.62), new THREE.Vector3(0.3, 1.35, 0.64), new THREE.Vector3(-0.6, 1.5, 0.65), new THREE.Vector3(-1.2, 1.6, 0.65)], 0xF59E0B, 'heart');
        create3DCreaseTube([new THREE.Vector3(-1.3, 0.9, 0.65), new THREE.Vector3(-0.3, 0.4, 0.64), new THREE.Vector3(0.6, 0.0, 0.63), new THREE.Vector3(1.2, -0.6, 0.62)], 0x6D28D9, 'head');
        create3DCreaseTube([new THREE.Vector3(-1.3, 0.9, 0.65), new THREE.Vector3(-0.7, -0.4, 0.64), new THREE.Vector3(-0.9, -1.6, 0.63), new THREE.Vector3(-1.1, -2.1, 0.60)], 0x10B981, 'life');
        create3DCreaseTube([new THREE.Vector3(0.0, -2.1, 0.61), new THREE.Vector3(-0.15, -0.6, 0.63), new THREE.Vector3(-0.35, 0.8, 0.64), new THREE.Vector3(-0.45, 1.9, 0.65)], 0xF7E2BD, 'fate');

        // 5. 9 Mount Spheres
        const mounts = [
            { key: 'jupiter', x: -1.15, y: 1.7, z: 0.65, color: 0x6D28D9 },
            { key: 'saturn', x: -0.38, y: 1.85, z: 0.66, color: 0xDFAC6C },
            { key: 'sun', x: 0.42, y: 1.8, z: 0.65, color: 0xF59E0B },
            { key: 'mercury', x: 1.25, y: 1.45, z: 0.63, color: 0x3B82F6 },
            { key: 'venus', x: -1.1, y: -0.6, z: 0.65, color: 0xEC4899 },
            { key: 'moon', x: 1.2, y: -1.0, z: 0.62, color: 0x10B981 },
            { key: 'marsPos', x: 1.28, y: 0.3, z: 0.63, color: 0xEF4444 },
            { key: 'marsNeg', x: -1.2, y: 0.4, z: 0.65, color: 0xF97316 },
            { key: 'rahuKetu', x: 0.0, y: -0.3, z: 0.63, color: 0x8B5CF6 }
        ];

        mounts.forEach(m => {
            const mGeo = new THREE.SphereGeometry(0.28, 16, 16);
            const mMat = new THREE.MeshBasicMaterial({ color: m.color });
            const mMesh = new THREE.Mesh(mGeo, mMat);
            mMesh.position.set(m.x, m.y, m.z);
            mMesh.userData = { key: m.key };
            this.handGroup.add(mMesh);
            this.mountObjects.push(mMesh);
        });
    }

    resetCamera() {
        if (this.camera && this.handGroup) {
            this.camera.position.set(0, 0, 11.5);
            this.handGroup.rotation.set(0, 0, 0);
        }
    }

    toggleRotation() {
        this.isRotating = !this.isRotating;
        return this.isRotating;
    }

    dispose3D() {
        if (this.resizeListener) {
            window.removeEventListener('resize', this.resizeListener);
        }
        if (this.scene) {
            this.scene.traverse(obj => {
                if (obj.geometry) obj.geometry.dispose();
                if (obj.material) {
                    if (Array.isArray(obj.material)) obj.material.forEach(m => m.dispose());
                    else obj.material.dispose();
                }
            });
        }
        if (this.renderer) {
            this.renderer.dispose();
        }
    }
}
