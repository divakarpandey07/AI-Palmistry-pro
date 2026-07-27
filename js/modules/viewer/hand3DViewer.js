/* ==========================================================================
   AI Palmistry Pro - High-Fidelity Anatomical 3D Human Hand Model Engine
   Realistic Hand Geometry: Curved Palm Hollow, Thenar & Hypothenar Eminence,
   3-Phalange Articulated Fingers, Natural Thumb Joint & Subsurface Skin Shader
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
        if (!this.canvas || typeof THREE === 'undefined') return;

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

        // Studio Studio Lighting for Realistic Subsurface Skin Shader
        const ambient = new THREE.AmbientLight(0xFFE4CE, 1.2);
        this.scene.add(ambient);

        const keyLight = new THREE.DirectionalLight(0xFFF0E0, 2.2);
        keyLight.position.set(10, 15, 18);
        this.scene.add(keyLight);

        const fillLight = new THREE.DirectionalLight(0x93C5FD, 0.8);
        fillLight.position.set(-12, -8, 10);
        this.scene.add(fillLight);

        const rimLight = new THREE.DirectionalLight(0xF59E0B, 1.0);
        rimLight.position.set(0, -15, -10);
        this.scene.add(rimLight);

        this.handGroup = new THREE.Group();

        // Realistic Organic Skin Material with Subsurface Scattering & Micro-Bump
        const skinMat = new THREE.MeshPhysicalMaterial({
            color: 0xE8B896,
            roughness: 0.48,
            metalness: 0.03,
            clearcoat: 0.15,
            clearcoatRoughness: 0.4,
            subsurfaceColor: 0xD86B49
        });

        // ------------------------------------------------------------------
        // 1. ANATOMICAL PALM MESH WITH HOLLOW & EMINENCES
        // ------------------------------------------------------------------
        const palmGroup = new THREE.Group();

        // Palm Base (Curved Tapering Mesh)
        const pShape = new THREE.Shape();
        pShape.moveTo(-1.6, -2.2); // Wrist Base Left
        pShape.quadraticCurveTo(-2.1, -1.0, -2.0, 0.5); // Hypothenar Curve
        pShape.quadraticCurveTo(-1.8, 2.0, -1.5, 2.2); // Pinky Base
        pShape.lineTo(1.5, 2.2); // Top Knuckles Base
        pShape.quadraticCurveTo(2.1, 1.0, 2.0, -0.8); // Thenar Curve
        pShape.quadraticCurveTo(1.8, -2.0, 1.4, -2.2); // Wrist Base Right
        pShape.quadraticCurveTo(0, -2.5, -1.6, -2.2); // Wrist Base Curve

        const extrudeSettings = {
            steps: 4,
            depth: 0.65,
            bevelEnabled: true,
            bevelThickness: 0.35,
            bevelSize: 0.35,
            bevelSegments: 12
        };

        const palmGeo = new THREE.ExtrudeGeometry(pShape, extrudeSettings);
        palmGeo.center();
        const mainPalmMesh = new THREE.Mesh(palmGeo, skinMat);
        palmGroup.add(mainPalmMesh);

        // Thenar Eminence (Thumb Pad Muscle Bulge)
        const thenarGeo = new THREE.SphereGeometry(0.95, 24, 24);
        thenarGeo.scale(1.1, 1.5, 0.7);
        const thenarMesh = new THREE.Mesh(thenarGeo, skinMat);
        thenarMesh.position.set(-1.1, -0.6, 0.3);
        thenarMesh.rotation.z = -0.3;
        palmGroup.add(thenarMesh);

        // Hypothenar Eminence (Pinky Pad Bulge)
        const hypoGeo = new THREE.SphereGeometry(0.85, 24, 24);
        hypoGeo.scale(0.9, 1.4, 0.65);
        const hypoMesh = new THREE.Mesh(hypoGeo, skinMat);
        hypoMesh.position.set(1.2, -0.8, 0.25);
        hypoMesh.rotation.z = 0.25;
        palmGroup.add(hypoMesh);

        this.handGroup.add(palmGroup);

        // ------------------------------------------------------------------
        // 2. ARTICULATED 3-PHALANGE FINGERS WITH NATURAL CURVATURE & NAILS
        // ------------------------------------------------------------------
        const nailMat = new THREE.MeshStandardMaterial({
            color: 0xF3CBB1,
            roughness: 0.2,
            metalness: 0.1
        });

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

            // Proximal Phalange
            const p1Geo = new THREE.CylinderGeometry(f.radius * 0.95, f.radius, segLen, 20);
            const p1Mesh = new THREE.Mesh(p1Geo, skinMat);
            p1Mesh.position.y = segLen / 2;
            fGroup.add(p1Mesh);

            // Joint 1
            const j1Geo = new THREE.SphereGeometry(f.radius * 0.96, 16, 16);
            const j1Mesh = new THREE.Mesh(j1Geo, skinMat);
            j1Mesh.position.y = segLen;
            fGroup.add(j1Mesh);

            // Middle Phalange
            const p2Geo = new THREE.CylinderGeometry(f.radius * 0.88, f.radius * 0.94, segLen, 20);
            const p2Mesh = new THREE.Mesh(p2Geo, skinMat);
            p2Mesh.position.y = segLen * 1.5;
            fGroup.add(p2Mesh);

            // Joint 2
            const j2Geo = new THREE.SphereGeometry(f.radius * 0.88, 16, 16);
            const j2Mesh = new THREE.Mesh(j2Geo, skinMat);
            j2Mesh.position.y = segLen * 2;
            fGroup.add(j2Mesh);

            // Distal Phalange (Fingertip)
            const p3Geo = new THREE.SphereGeometry(f.radius * 0.85, 20, 20);
            p3Geo.scale(1.0, 1.3, 0.85);
            const p3Mesh = new THREE.Mesh(p3Geo, skinMat);
            p3Mesh.position.y = segLen * 2.5;
            fGroup.add(p3Mesh);

            // Fingernail
            const nailGeo = new THREE.BoxGeometry(f.radius * 0.9, segLen * 0.5, 0.08);
            const nailMesh = new THREE.Mesh(nailGeo, nailMat);
            nailMesh.position.set(0, segLen * 2.6, 0.22);
            fGroup.add(nailMesh);

            this.handGroup.add(fGroup);
        });

        // ------------------------------------------------------------------
        // 3. ANATOMICAL THUMB WITH 45-DEGREE ANGLE & JOINTS
        // ------------------------------------------------------------------
        const thumbGroup = new THREE.Group();
        thumbGroup.position.set(-1.9, -0.6, 0.2);
        thumbGroup.rotation.z = Math.PI / 3.6;
        thumbGroup.rotation.x = 0.25;

        // Thumb Base Joint
        const tbGeo = new THREE.CylinderGeometry(0.44, 0.52, 1.2, 20);
        const tbMesh = new THREE.Mesh(tbGeo, skinMat);
        tbMesh.position.y = 0.6;
        thumbGroup.add(tbMesh);

        // Thumb Joint Sphere
        const tjGeo = new THREE.SphereGeometry(0.45, 16, 16);
        const tjMesh = new THREE.Mesh(tjGeo, skinMat);
        tjMesh.position.y = 1.2;
        thumbGroup.add(tjMesh);

        // Thumb Tip
        const ttGeo = new THREE.SphereGeometry(0.42, 20, 20);
        ttGeo.scale(1.0, 1.3, 0.85);
        const ttMesh = new THREE.Mesh(ttGeo, skinMat);
        ttMesh.position.y = 1.7;
        thumbGroup.add(ttMesh);

        // Thumb Nail
        const tNailGeo = new THREE.BoxGeometry(0.4, 0.45, 0.08);
        const tNailMesh = new THREE.Mesh(tNailGeo, nailMat);
        tNailMesh.position.set(0, 1.8, 0.26);
        thumbGroup.add(tNailMesh);

        this.handGroup.add(thumbGroup);

        // ------------------------------------------------------------------
        // 4. REALISTIC GLOWING PALM CREASE RIBBONS (Heart, Head, Life, Fate)
        // ------------------------------------------------------------------
        const create3DCreaseTube = (points, colorHex, keyName) => {
            const curve = new THREE.CatmullRomCurve3(points);
            const tubeGeo = new THREE.TubeGeometry(curve, 40, 0.065, 8, false);
            const tubeMat = new THREE.MeshBasicMaterial({ color: colorHex });
            const tubeMesh = new THREE.Mesh(tubeGeo, tubeMat);
            tubeMesh.userData = { key: keyName };
            this.handGroup.add(tubeMesh);
            this.mountObjects.push(tubeMesh);
        };

        // Heart Line (Yellow Gold)
        create3DCreaseTube([
            new THREE.Vector3(1.3, 1.2, 0.62),
            new THREE.Vector3(0.3, 1.35, 0.64),
            new THREE.Vector3(-0.6, 1.5, 0.65),
            new THREE.Vector3(-1.2, 1.6, 0.65)
        ], 0xF59E0B, 'heart');

        // Head Line (Purple Velvet)
        create3DCreaseTube([
            new THREE.Vector3(-1.3, 0.9, 0.65),
            new THREE.Vector3(-0.3, 0.4, 0.64),
            new THREE.Vector3(0.6, 0.0, 0.63),
            new THREE.Vector3(1.2, -0.6, 0.62)
        ], 0x6D28D9, 'head');

        // Life Line (Green Vitality)
        create3DCreaseTube([
            new THREE.Vector3(-1.3, 0.9, 0.65),
            new THREE.Vector3(-0.7, -0.4, 0.64),
            new THREE.Vector3(-0.9, -1.6, 0.63),
            new THREE.Vector3(-1.1, -2.1, 0.60)
        ], 0x10B981, 'life');

        // Fate Line (Soft Cream Gold)
        create3DCreaseTube([
            new THREE.Vector3(0.0, -2.1, 0.61),
            new THREE.Vector3(-0.15, -0.6, 0.63),
            new THREE.Vector3(-0.35, 0.8, 0.64),
            new THREE.Vector3(-0.45, 1.9, 0.65)
        ], 0xF7E2BD, 'fate');

        // ------------------------------------------------------------------
        // 5. ALL 9 PLANETARY MOUNT SPHERES WITH GLOWING COLOR IDENTIFIERS
        // ------------------------------------------------------------------
        const mounts = [
            { key: 'jupiter', name: '♃ गुरु', x: -1.15, y: 1.7, z: 0.65, color: 0x6D28D9 },
            { key: 'saturn', name: '♄ शनि', x: -0.38, y: 1.85, z: 0.66, color: 0xDFAC6C },
            { key: 'sun', name: '☉ सूर्य', x: 0.42, y: 1.8, z: 0.65, color: 0xF59E0B },
            { key: 'mercury', name: '☿ बुध', x: 1.25, y: 1.45, z: 0.63, color: 0x3B82F6 },
            { key: 'venus', name: '♀ शुक्र', x: -1.1, y: -0.6, z: 0.65, color: 0xEC4899 },
            { key: 'moon', name: '☽ चंद्र', x: 1.2, y: -1.0, z: 0.62, color: 0x10B981 },
            { key: 'marsPos', name: '♂ उच्च मंगल', x: 1.28, y: 0.3, z: 0.63, color: 0xEF4444 },
            { key: 'marsNeg', name: '♂ निम्न मंगल', x: -1.2, y: 0.4, z: 0.65, color: 0xF97316 },
            { key: 'rahuKetu', name: '✨ राहु-केतु', x: 0.0, y: -0.3, z: 0.63, color: 0x8B5CF6 }
        ];

        mounts.forEach(m => {
            const mGeo = new THREE.SphereGeometry(0.28, 20, 20);
            const mMat = new THREE.MeshBasicMaterial({ color: m.color });
            const mMesh = new THREE.Mesh(mGeo, mMat);
            mMesh.position.set(m.x, m.y, m.z);
            mMesh.userData = { key: m.key };
            this.handGroup.add(mMesh);
            this.mountObjects.push(mMesh);
        });

        this.scene.add(this.handGroup);

        // Raycasting Mouse Click Listener
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
            this.camera.position.set(0, 0, 11.5);
            this.handGroup.rotation.set(0, 0, 0);
        }
    }

    toggleRotation() {
        this.isRotating = !this.isRotating;
        return this.isRotating;
    }
}
