/*	TODO list
http://lab.aerotwist.com/webgl/reactive-ball/

 * */

if (CanvasRenderingContext2D && !CanvasRenderingContext2D.renderText) {
	// @param  letterSpacing  {float}  CSS letter-spacing property
	CanvasRenderingContext2D.prototype.renderText = function (text, x, y, letterSpacing) {
		if (!text || typeof text !== 'string' || text.length === 0) {
			return;
		}

		if (typeof letterSpacing === 'undefined') {
			letterSpacing = 0;
		}

		// letterSpacing of 0 means normal letter-spacing

		var characters = String.prototype.split.call(text, ''),
			index = 0,
			current,
			currentPosition = x,
			align = 1;

		if (this.textAlign === 'right') {
			characters = characters.reverse();
			align = -1;
		} else if (this.textAlign === 'center') {
			var totalWidth = 0;
			for (var i = 0; i < characters.length; i++) {
				totalWidth += (this.measureText(characters[i]).width + letterSpacing);
			}
			currentPosition = x - (totalWidth / 2);
		}

		while (index < text.length) {
			current = characters[index++];
			this.fillText(current, currentPosition, y);
			currentPosition += (align * (this.measureText(current).width + letterSpacing));
		}
	}
}


/*


2D and 3D
http://yomotsu.github.com/threejs-examples/box2dwebjs/


asteroids:
http://whiteflashwhitehit.com/content/2011/02/moon_webgl.html



2D coordinate:
http://catchvar.com/threejs-game-transforming-isometric-screen-co

https://github.com/mrdoob/three.js/issues/78


            PERFOMANCE:
            http://learningthreejs.com/blog/2011/09/16/performance-caching-material/    --caching the renderer

 */

function runPrefixMethod(obj, method) {
    var pfx = ["webkit", "moz", "ms", "o", ""];
    var p = 0, m, t;
    while (p < pfx.length && !obj[m]) {
        m = method;
        if (pfx[p] == "") {
            m = m.substr(0,1).toLowerCase() + m.substr(1);
        }
        m = pfx[p] + m;
        t = typeof obj[m];
        if (t != "undefined") {
            pfx = [pfx[p]];
            return (t == "function" ? obj[m]() : obj[m]);
        }
        p++;
    }
}


THREE.OrbitControlsMod = function ( object, domElement, centers ) {

    THREE.EventTarget.call( this );

    this.object = object;
    this.domElement = ( domElement !== undefined ) ? domElement : document;

    // API

    this.center = centers == undefined ? new THREE.Vector3(): centers[0];

    this.centers = centers == undefined ? [new THREE.Vector3()]: centers;
    //        console.log(this.centers);
    this.currentCenter = 0;

    this.userZoom = true;
    this.userZoomSpeed = 1.0;

    this.userRotate = true;
    this.userRotateSpeed = 1.0;

    this.autoRotate = false;
    this.autoRotateSpeed = 2.0; // 30 seconds per round when fps is 60

    // internals

    var scope = this;

    var EPS = 0.000001;
    var PIXELS_PER_ROUND = 1800;

    var rotateStart = new THREE.Vector2();
    var rotateEnd = new THREE.Vector2();
    var rotateDelta = new THREE.Vector2();

    var zoomStart = new THREE.Vector2();
    var zoomEnd = new THREE.Vector2();
    var zoomDelta = new THREE.Vector2();

    var phiDelta = 0;
    var thetaDelta = 0;
    var scale = 1;

    var lastPosition = new THREE.Vector3();

    var STATE = {
        NONE : -1,
        ROTATE : 0,
        ZOOM : 1,
        PAN : 2
    };
    var state = STATE.NONE;

    // events

    var changeEvent = {
        type: 'change'
    };


    this.rotateLeft = function ( angle ) {

        if ( angle === undefined ) {

            angle = getAutoRotationAngle();

        }

        thetaDelta -= angle;

    };

    this.rotateRight = function ( angle ) {

        if ( angle === undefined ) {

            angle = getAutoRotationAngle();

        }

        thetaDelta += angle;

    };

    this.rotateUp = function ( angle ) {

        if ( angle === undefined ) {

            angle = getAutoRotationAngle();

        }

        phiDelta -= angle;

    };

    this.rotateDown = function ( angle ) {

        if ( angle === undefined ) {

            angle = getAutoRotationAngle();

        }

        phiDelta += angle;

    };

    this.zoomIn = function ( zoomScale ) {

        if ( zoomScale === undefined ) {

            zoomScale = getZoomScale();

        }

        scale /= zoomScale;

    };

    this.zoomOut = function ( zoomScale ) {

        if ( zoomScale === undefined ) {

            zoomScale = getZoomScale();

        }

        scale *= zoomScale;

    };

    this.update = function () {

        var position = this.object.position;
        var offset = position.clone().subSelf( this.center );

        // angle from z-axis around y-axis

        var theta = Math.atan2( offset.x, offset.z );

        // angle from y-axis

        var phi = Math.atan2( Math.sqrt( offset.x * offset.x + offset.z * offset.z ), offset.y );

        if ( this.autoRotate ) {

            this.rotateLeft( getAutoRotationAngle() );

        }

        theta += thetaDelta;
        phi += phiDelta;

        // restrict phi to be betwee EPS and PI-EPS

        phi = Math.max( EPS, Math.min( Math.PI - EPS, phi ) );

        var radius = offset.length();
        offset.x = radius * Math.sin( phi ) * Math.sin( theta );
        offset.y = radius * Math.cos( phi );
        offset.z = radius * Math.sin( phi ) * Math.cos( theta );
        offset.multiplyScalar( scale );

        position.copy( this.center ).addSelf( offset );

        this.object.lookAt( this.center );

        thetaDelta = 0;
        phiDelta = 0;
        scale = 1;

        if ( lastPosition.distanceTo( this.object.position ) > 0 ) {

            this.dispatchEvent( changeEvent );

            lastPosition.copy( this.object.position );

        }

    };


    function getAutoRotationAngle() {

        return 2 * Math.PI / 60 / 60 * scope.autoRotateSpeed;

    }

    function getZoomScale() {

        return Math.pow( 2.95, scope.userZoomSpeed );

    }

    function onMouseDown( event ) {

        if ( !scope.userRotate ) return;

        event.preventDefault();

        if ( event.button === 0 ) {

            state = STATE.ROTATE;

            rotateStart.set( event.clientX, event.clientY );

        }
        else
        if ( event.button === 2 ) {
            if(scope.currentCenter < scope.centers.length-1) {
                scope.currentCenter++;
            } else {
                scope.currentCenter = 0;
            }

            scope.center = scope.centers[scope.currentCenter];

        }

        document.addEventListener( 'mousemove', onMouseMove, false );
        document.addEventListener( 'mouseup', onMouseUp, false );

    }

    function onMouseMove( event ) {

        event.preventDefault();

        if ( state === STATE.ROTATE ) {

            rotateEnd.set( event.clientX, event.clientY );
            rotateDelta.sub( rotateEnd, rotateStart );

            scope.rotateLeft( 2 * Math.PI * rotateDelta.x / PIXELS_PER_ROUND * scope.userRotateSpeed );
            scope.rotateUp( 2 * Math.PI * rotateDelta.y / PIXELS_PER_ROUND * scope.userRotateSpeed );

            rotateStart.copy( rotateEnd );

        } else if ( state === STATE.ZOOM ) {

            zoomEnd.set( event.clientX, event.clientY );
            zoomDelta.sub( zoomEnd, zoomStart );

            if ( zoomDelta.y > 0 ) {
                scope.zoomOut();

            } else {

                scope.zoomIn();

            }

            zoomStart.copy( zoomEnd );

        }

    }

    function onMouseUp( event ) {

        if ( ! scope.userRotate ) return;

        document.removeEventListener( 'mousemove', onMouseMove, false );
        document.removeEventListener( 'mouseup', onMouseUp, false );

        state = STATE.NONE;

    }

    function onMouseWheel( event ) {
        event.preventDefault();
        if ( ! scope.userZoom ) return;

        if ( event.wheelDelta > 0 || event.detail > 0) {

            scope.zoomOut();

        } else {

            scope.zoomIn();

        }

    }

    this.domElement.addEventListener( 'contextmenu', function ( event ) {
        event.preventDefault();
    }, false );
    this.domElement.addEventListener( 'mousedown', onMouseDown, false );
    this.domElement.addEventListener( 'mousewheel', onMouseWheel, false );
    this.domElement.addEventListener('DOMMouseScroll', onMouseWheel, false);

    window.ChromeWheel  = function() {
        var evt = document.createEvent("MouseEvents");
        evt.initMouseEvent(
            'DOMMouseScroll', // in DOMString typeArg,
            true,  // in boolean canBubbleArg,
            true,  // in boolean cancelableArg,
            window,// in views::AbstractView viewArg,
            120,   // in long detailArg,
            0,     // in long screenXArg,
            0,     // in long screenYArg,
            0,     // in long clientXArg,
            0,     // in long clientYArg,
            0,     // in boolean ctrlKeyArg,
            0,     // in boolean altKeyArg,
            0,     // in boolean shiftKeyArg,
            0,     // in boolean metaKeyArg,
            0,     // in unsigned short buttonArg,
            null   // in EventTarget relatedTargetArg
            );
        this.domElement.dispatchEvent(evt);
    }





};





var renderType = 'WebGL';


var shaderBlock = {
    sunUniforms : null,

    sunFShader : [
    "uniform float time;",
    "uniform vec2 resolution;",
    "uniform float fogDensity;",
    "uniform vec3 fogColor;	",
    "uniform sampler2D texture1;",
    "uniform sampler2D texture2;",

    "varying vec2 vUv;",

    "void main( void ) {",
    "vec2 position = -1.0 + 2.0 * vUv;",
    "vec4 noise = texture2D( texture1, vUv );",

    "vec2 T1 = vUv + vec2( 1.5, -1.5 ) * time  *0.02;",
    "vec2 T2 = vUv + vec2( -0.5, 2.0 ) * time * 0.01;",

    "T1.x += noise.x * 2.0;",
    "T1.y += noise.y * 2.0;",
    "T2.x -= noise.y * 0.2;",
    "T2.y += noise.z * 0.2;",

    "float p = texture2D( texture1, T1 * 2.0 ).a;",
    "vec4 color = texture2D( texture2, T2 * 2.0 );",
    "vec4 temp = color * ( vec4( p, p, p, p ) * 2.0 ) + ( color * color - 0.1 );",

    "if( temp.r > 1.0 ){ temp.bg += clamp( temp.r - 2.0, 0.0, 100.0 ); }",
    "if( temp.g > 1.0 ){ temp.rb += temp.g - 1.0; }	",
    "if( temp.b > 1.0 ){ temp.rg += temp.b - 1.0; }	",

    "gl_FragColor = temp;",
    "float depth = gl_FragCoord.z / gl_FragCoord.w;",
    "const float LOG2 = 1.442695;",
    "float fogFactor = exp2( - fogDensity * fogDensity * depth * depth * LOG2 );",
    "fogFactor = 1.0 - clamp( fogFactor, 0.0, 1.0 );",
    "gl_FragColor = mix( gl_FragColor, vec4( fogColor, gl_FragColor.w ), fogFactor );}"
    ].join("\n") ,

    sunVShader : [
    "uniform vec2 uvScale;",

    "varying vec2 vUv;",

    "void main()	{",
    "vUv = uvScale * uv;",
    "vec4 mvPosition = modelViewMatrix * vec4( position, 0.9 );",
    "gl_Position = projectionMatrix * mvPosition;	",
    "}"
    ].join("\n") ,


    material: null,

    loadSun : function (size) {

        uniforms = {
            fogDensity: {
                type: "f",
                value: 0.00001
            },
            fogColor: {
                type: "v3",
                value: new THREE.Vector3( 0,0, 0 )
            },
            time: {
                type: "f",
                value: 1.0
            },
            resolution: {
                type: "v2",
                value: new THREE.Vector2()
            },
            uvScale: {
                type: "v2",
                value: new THREE.Vector2( 3.0, 1.0 )
            },
            texture1: {
                type: "t",
                value: 0,
                texture: THREE.ImageUtils.loadTexture( './js/maps/08.jpg')
            //                texture: THREE.ImageUtils.loadTexture( './js/lavatile.jpg')
            },
            texture2: {
                type: "t",
                value: 1,
                texture: THREE.ImageUtils.loadTexture( './js/maps/08.jpg')
            //                texture: THREE.ImageUtils.loadTexture( './js/lavatile.jpg')
            }
        }

        uniforms.texture1.texture.wrapS = uniforms.texture1.texture.wrapT = THREE.RepeatWrapping;

        uniforms.texture2.texture.wrapS = uniforms.texture2.texture.wrapT = THREE.RepeatWrapping;
        shaderBlock.sunUniforms = uniforms;

        //        var size = renderBlock.getPlanetSize('TYPE_A');

        shaderBlock.material = new THREE.ShaderMaterial( {

            uniforms: shaderBlock.sunUniforms,
            vertexShader: shaderBlock.sunVShader,
            fragmentShader: shaderBlock.sunFShader

        } );
        //shaderBlock.material.transparent = true;
        //shaderBlock.material.opacity = 0.9;
        shaderBlock.material.color=0x222222;
        // sphere
        var sun = new THREE.Mesh(

            new THREE.SphereGeometry(
                size,
                renderBlock.config.lod*2,
                renderBlock.config.lod),

            shaderBlock.material
            );

        //        sun.overdraw = true;

        //        console.log(sun);
        renderBlock.sun = sun;
        return sun;

    }

};



var modelBlock = {

    initiated: false,
    players: {},
    planetMap: {},
    actionMap: {},
    prevStep: {},
    controllers:{},
    jsonResponse: [],
    stats  : {

    },
    max: 0,
    turn: 0,
    statGui : null,


    initStatGui : function() {

        modelBlock.statGui = new DAT.GUI({
            autoPlace: false,
            height	: modelBlock.players.length * 32 - 1
        });

        var customContainer = document.body.appendChild(document.createElement('div'));
        customContainer.id = 'stats';
        customContainer.appendChild(modelBlock.statGui.domElement);

        var style = customContainer.style;
        style.visibility = "visible";
        style.position = "fixed";
        style.top = '0px';
        style.left = '0px';
        customContainer.style = style;
        for(player in modelBlock.players ){
            name = modelBlock.players[player].name;
            modelBlock.stats[name] = 0;
            var controller =  modelBlock.statGui.add(modelBlock.stats, name,0);
            controller.listen();
            controller.min(0);
            controller.max(0);
            modelBlock.controllers[name] = controller;
        }
    },
    initStats: function() {
        for(player in modelBlock.players ){
            name = modelBlock.players[player].name;
            modelBlock.stats[name] = 0;
        }
    },

    updateStats: function() {
        for(player in modelBlock.players) {
            name = modelBlock.players[player].name;
            modelBlock.stats[name] = 0;
        }
        map = modelBlock.planetMap;

        for (planetnum in map) {
            planet = map[planetnum];
            if(planet.owner !='neutral') {
                modelBlock.stats[planet.owner] +=planet.unitsCount;
            }
        }
        var max = 0;
        var leader = '';
        for(stat in modelBlock.stats) {
            if(modelBlock.stats[stat] > max) {
                max = modelBlock.stats[stat];
                leader = stat;
            }
        }
        modelBlock.max = max;
        modelBlock.leader = leader;
        var leaders = [];
        for(stat in modelBlock.stats) {
            if(modelBlock.stats[stat] == max) {
                leaders.push(stat);
            }
        }
        modelBlock.leaders = leaders;

    //        console.log(modelBlock.leader);
    //        if(renderBlock.statCanvas != null)renderBlock.programStats(renderBlock.statCanvas.getContext("2d"));

    //        for(c in modelBlock.controllers) {
    //            modelBlock.controllers[c].max(max);
    //        }

    },




    vars: {
        container: null,
        playerCount: null
    },

    init: function(json) {
        //        console.log("entered the modelBLock.init() with json:");
        //        console.log(json);
        if (!modelBlock.initiated) {

            //            var divs = document.getElementsByClassName('gameStatusMessage');
            //            for (var i = 0; i < divs.length; i++) {
            //                divs[i].style.display = "none";
            //            }

            if (json.planetMap.length > 0) {

                modelBlock.planetMap = json.planetMap;
                modelBlock.turn = json.turnNumber;
                modelBlock.jsonResponse.push(json);
                modelBlock.prevStep[modelBlock.turn] = modelBlock.planetMap.slice();
                modelBlock.players = json.players;

                modelBlock.vars.playerCount = modelBlock.players.length;
                modelBlock.initiated = true;
                //                modelBlock.initStatGui();
                modelBlock.initStats();

            } else {
                console.log('empty response');
            }
        }
    }
};
/////////////////////////////////////////





///// RENDER BLOCK /////
var renderBlock = {
    turnSpeed: 2000,
    stats: null,
    sun: null,
    started: false,
    config: {
        lod: 50,
        lightIntensity: 1,
        lightDistance: 100000,

        bgColor: 0x07070c,
        //
        //        bgColor: 0xffffff,
        //bgColor: 0x00ee,
        lightColor: 0xaaaaff,
        sunColor:  0xaaaaff,
        godRayIntensity: 0.22,
        sunIntensity : 0.5
    },
    rings : null,

    //cloudTexture: THREE.ImageUtils.loadTexture( '/Game/js/cloud.png'),

    //    sunTexture: THREE.ImageUtils.loadTexture( './js/lavatile.jpg'),
    novaTexture : renderType == 'WebGL' ? THREE.ImageUtils.loadTexture( './js/nova.png') : null,
    
    whiteTexture: renderType == 'WebGL' ?THREE.ImageUtils.loadTexture( './js/maps/white.png') : null,
    smallNovaTexture:renderType == 'WebGL' ? THREE.ImageUtils.loadTexture( './js/nova_particle.png') : null,
    atmos1Texture : renderType == 'WebGL' ? THREE.ImageUtils.loadTexture( './js/atmosphere.png'): null,
    atmos2Texture : renderType == 'WebGL' ? THREE.ImageUtils.loadTexture( './js/atmosphere2.png') :null,
    neptuneTexture: renderType == 'WebGL' ?  THREE.ImageUtils.loadTexture( './js/maps/Neptune.jpg'): null,
    //    cloudsTexture: THREE.ImageUtils.loadTexture( './js/maps/clouds.png'),
    //galaxyTexture:  THREE.ImageUtils.loadTexture( '/Game/js/maps/galaxy3.jpg'),
    plutoTexture: renderType == 'WebGL' ? THREE.ImageUtils.loadTexture( './js/maps/Pluto.jpg') : null,
    iceTexture: renderType == 'WebGL' ? THREE.ImageUtils.loadTexture( './js/maps/11.jpg') :null,
    jupiterTexture: renderType == 'WebGL' ? THREE.ImageUtils.loadTexture( './js/maps/Jupiter.jpg') : null,
    //    lightBackgroundTexture : THREE.ImageUtils.loadTexture( './js/maps/background.png'),
    //    unitTexture : THREE.ImageUtils.loadTexture( './js/maps/particleA.png'),

    SCREEN_WIDTH: $('#container').outerWidth(),
    SCREEN_HEIGHT: $('#container').outerHeight(),
    screenSpacePosition: new THREE.Vector3(),
    r: 0,
    $container: null,
    renderer: null,
    postprocessing: {
        sunIntensity: 0.9,
        enabled: true
    },
    materialDepth: null,
    materials: {

    },
    cameraConfig: {
        fov: 30,
        near: 0.1,
        far: 200000
    },
    theta: 0,
    mesh: null,
    planets: {},
    
    actionMap: {},
    projector: new THREE.Projector(),
    textures: new Object(),
    canvases: null,
    controls: null,
    camera: null,
    scene: null,
    mouse: {
        x: 0,
        y: 0

    },
	fontFamily: "sans-serif",
    showTrace : true,
    isMesh : true,
    blurEnable: true,
    r : 0,
    strangeCamera: false,
    gui: null,
    GUIOptions: {
        hideUnits: false,
        hideText: false,
        hideWire: true,
        hideAtmosphere: false,
        sunIntensity: 1.25,
        fov: null,
        fullScreen: false,
        globalRadius: 2.0,
        tempRad: -2.0,

        showSelection: true
    },
    effectFXAA : null,
    INTERSECTED: null,
    composer: null,
    sunComposer: null,
    clock : new THREE.Clock(),



    onDocumentMouseDown : function(event) {
        event.preventDefault();

        var vector;
        if(renderBlock.GUIOptions.fullScreen ) {
            vector = new THREE.Vector3( ( event.clientX / renderBlock.SCREEN_WIDTH ) * 2 - 1, - ( event.clientY / renderBlock.SCREEN_HEIGHT ) * 2 + 1, 0.5 );
        } else {

            _this = $('#container');
            vector = new THREE.Vector3(
                ( (event.clientX - (_this.offset().left - $(window).scrollLeft()) ) / renderBlock.SCREEN_WIDTH ) * 2 - 1,
                -( (event.clientY - (_this.offset().top - $(window).scrollTop()) ) / renderBlock.SCREEN_HEIGHT) * 2 + 1,
                0.5
                );
        }
        if( event.button == 0) {
            renderBlock.projector.unprojectVector( vector, renderBlock.camera );

            var ray = new THREE.Ray( renderBlock.camera.position, vector.subSelf( renderBlock.camera.position ).normalize() );

            var intersects = ray.intersectObjects( renderBlock.mesh.children);

            if ( intersects.length > 0 ) {
                intersects[ 0 ].object.triggered = !intersects[ 0 ].object.triggered;
                
                //                                renderBlock.controls.center = intersects[ 0 ].object.position;
                id = intersects[ 0 ].object.planetId;

                for(arrow in renderBlock.actionMap) {
                    //                    if(actionMap[arrow])

                    if(arrow.split(' ')[0] == id){

                        renderBlock.actionMap[arrow].line.visible = intersects[ 0 ].object.triggered;
                    }
                }
            }
        }
    },

    matChanger : function( ) {

        renderBlock.postprocessing.bokeh_uniforms[ "focus" ].value = renderBlock.effectController.focus;
        renderBlock.postprocessing.bokeh_uniforms[ "aperture" ].value = renderBlock.effectController.aperture;
        renderBlock.postprocessing.bokeh_uniforms[ "maxblur" ].value = renderBlock.effectController.maxblur;
    //renderBlock.postprocessing
    },

    cameraChanger: function() {
        renderBlock.camera.position.x = renderBlock.cameraController.x;
        renderBlock.camera.position.y = renderBlock.cameraController.y;
        renderBlock.camera.position.z = renderBlock.cameraController.z;
    //        renderBlock.camera.rotation.x = renderBlock.cameraController.xa;
    //        renderBlock.camera.rotation.y = renderBlock.cameraController.ya;
    //        renderBlock.camera.rotation.z = renderBlock.cameraController.za;
    },

    effectController  : {

        focus: 		1.0,
        aperture:	0.025,
        maxblur:	1.0

    },

    cameraController : {
        x : 100,
        y : 100,
        z : 100,
        xa :1.0,
        ya :1.0,
        za :1.0
    },

    meshController: {
        x : 0.0,
        y : 0.0,
        z : 0.0
    },

    bloomStrength: 0.5,
    enableFXAA : false,
    fxaaEnabled: false,
    composerEnabled: true,

    statCanvas : null,

    getPlanetSize : function(planetType) {
        var size = 12.0;
        switch(planetType) {
            case'TYPE_A' :
                size *= 0.4;
                break;
            case'TYPE_B' :
                size *= 0.7;
                break;
            case'TYPE_C' :
                size *= 1.0;
                break;
            case'TYPE_D' :
                size *= 1.5;
                break;
            default:
                size *= 0.5;
                break;
        }
        return size;
    },

	getMaxPlanetUnit: function(planetType) {
		var size = 100;
		/*тип TYPE_A: процент регенерации - 10%, предел - 100 юнитов
		 тип TYPE_B: процент регенерации - 15%, предел - 200 юнитов.
		 тип TYPE_C: процент регенерации - 20%, предел - 500 юнитов.
		 тип TYPE_D: процент регенерации - 30%, предел - 1000 юнитов.*/
		switch(planetType) {
			case'TYPE_A' :
				size *= 1;
				break;
			case'TYPE_B' :
				size *= 2;
				break;
			case'TYPE_C' :
				size *= 5;
				break;
			case'TYPE_D' :
				size *= 10;
				break;
			default:
				size *= 0;
				break;
		}
		return size;
	} ,

    getPlanetTexture : function(planetType) {
        //console.log(planetType);
        switch(planetType) {
            case'TYPE_A' :
                //                console.log('A');
                return renderBlock.sunTexture;
            case'TYPE_B' :
                //                     console.log('B');
                return renderBlock.iceTexture
            case'TYPE_C' :
                //                     console.log('C');
                return renderBlock.neptuneTexture;
            case'TYPE_D' :
                //                     console.log('D');
                return renderBlock.jupiterTexture;
            case 'clouds':
                return renderBlock.neptuneTexture;
            default:
                //                     console.log('default');
                return renderBlock.galaxyTexture;
        }

    },


    meshChanger: function() {
        x = renderBlock.meshController.x;
        y= renderBlock.meshController.y;
        z = renderBlock.meshController.z;
        renderBlock.mesh.rotation.set(x, y, z);
    },

    turnOnFullScreen: function() {
        if (renderBlock.GUIOptions.fullScreen) {
            //                cont = $('#container');

            if(THREEx.FullScreen.available() && ! THREEx.FullScreen.activated()){
                THREEx.FullScreen.request();
                $('canvas').css({
                    "background-image":'url("./img/background_alt.jpg")'
                });
                try{
                    leaveGame = document.getElementById("leaveGame");
                    leaveGame.style.visibility = "hidden";
                }catch(e){}

                divs = document.getElementsByClassName('wrap');
                for (var i = 0; i < divs.length; i++) {
                    divs[i].style.visibility = "hidden";
                }

                var style = renderer.domElement.style;
                style.visibility = "visible";
                style.position = "fixed";
                style.top = '0px';
                style.left = '0px';
                style.width = window.innerWidth;
                style.height = window.innerHeight;


                renderBlock.SCREEN_WIDTH = window.innerWidth;
                renderBlock.SCREEN_HEIGHT = window.innerHeight;
                renderBlock.renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);


                MARGIN = 0;

                renderBlock.renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);

                renderBlock.renderer.domElement.style.top = MARGIN + 'px';
                renderBlock.camera.aspect = renderBlock.SCREEN_WIDTH / renderBlock.SCREEN_HEIGHT;
                renderBlock.camera.updateProjectionMatrix();
                if(renderType=='WebGL') {
                    renderBlock.effectFXAA.uniforms[ 'resolution' ].value.set( 1 / window.innerWidth, 1 / window.innerHeight );
                    renderBlock.initComposer(renderBlock.enableFXAA);
                }
            }


        } else {
            if(THREEx.FullScreen.activated() )THREEx.FullScreen.cancel();
            $('canvas').css({
                "background-image":'none'
            });
            divs = document.getElementsByClassName('wrap');
            try{
                leaveGame = document.getElementById("leaveGame");
                leaveGame.style.visibility = "visible";
            }catch(e){}
            for ( i = 0; i < divs.length; i++) {
                divs[i].style.visibility = "visible";
            }
            renderBlock.renderer.domElement.style.top = '0px';
            renderBlock.SCREEN_WIDTH = $('#container').outerWidth();
            renderBlock.SCREEN_HEIGHT = $('#container').outerHeight();
            renderBlock.renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);
            renderBlock.camera.aspect = renderBlock.SCREEN_WIDTH / renderBlock.SCREEN_HEIGHT;
            renderBlock.camera.updateProjectionMatrix();
            if(renderType=='WebGL') {
                renderBlock.effectFXAA.uniforms[ 'resolution' ].value.set( 1 / window.innerWidth, 1 / window.innerHeight );
                renderBlock.initComposer(renderBlock.enableFXAA);
            }
        }
	    renderBlock.placeStats();
    },


    initGUI: function (gui, GUIOptions) {
        gui = new DAT.GUI({
            height	: 10 * 32
        });//({ height	: 9 * 32 - 1 })
        //        gui.add()
        gui.add(renderBlock, 'strangeCamera');
        gui.add( renderBlock, "bloomStrength", 0.0, 1.0, 0.025 ).onChange(function() {
            renderBlock.initComposer(renderBlock.enableFXAA);

        });
        gui.add(renderBlock, 'blurEnable').name('enable blur').onChange(function() {
            renderBlock.initComposer(renderBlock.effectFXAA);
        });
        //        /*    camera controls block*/
        //           gui.add( renderBlock.cameraController, "x", -9000, 9000, 10 ).onChange( renderBlock.cameraChanger );
        //           gui.add( renderBlock.cameraController, "y" , -9000, 9000, 10).onChange( renderBlock.cameraChanger );
        //           gui.add( renderBlock.cameraController, "z", -9000, 9000, 10).onChange( renderBlock.cameraChanger );
        //
        //            gui.add( renderBlock.meshController, "x", -2 *Math.PI, -2 *Math.PI, Math.PI/180 ).onChange( renderBlock.meshChanger );
        //           gui.add( renderBlock.meshController, "y" , -2 *Math.PI, -2 *Math.PI, Math.PI/180).onChange( renderBlock.meshChanger );
        //           gui.add( renderBlock.meshController, "z", -2 *Math.PI, -2 *Math.PI, Math.PI/180).onChange( renderBlock.meshChanger );
        //               gui.add( renderBlock.cameraController, "x", -990, 990, 0.1 ).onChange( renderBlock.cameraChanger );
        //           gui.add( renderBlock.cameraController, "y" , -990, 990, 0.1).onChange( renderBlock.cameraChanger );
        //           gui.add( renderBlock.cameraController, "z", -990, 990, 0.1).onChange( renderBlock.cameraChanger );
        /**/
        //    gui.add( renderBlock.effectController, "maxblur", 0.0, 3.0, 0.025 ).onChange( renderBlock.matChanger );

        //gui.add(mouse, 'x').name('mouse.x').listen();
        //	gui.add(mouse, 'y').name('mouse.y').listen();

        //	gui.add(camera.rotation, 'x').name('camera.rot.x').listen();
        //	gui.add(camera.rotation, 'y').name('camera.rot.y').listen();
        //	gui.add(camera.rotation, 'z').name('camera.rot.z').listen();

        //        	gui.add(renderBlock.camera.position, 'x').name('camera.x').listen();
        //        	gui.add(renderBlock.camera.position, 'y').name('camera.y').listen();
        //        	gui.add(renderBlock.camera.position, 'z').name('camera.z').listen();


        gui.add(GUIOptions, 'hideAtmosphere').name( 'camera trigger').onChange(function () {
            scope = renderBlock.controls;
            if(renderType=='WebGL'){
                if(GUIOptions.hideAtmosphere) {
                    scope.currentCenter = 1;
                    renderBlock.camera.position.set(1110, 0, 1900);
                } else {
                    scope.currentCenter = 0;

                //                    renderBlock.camera.position.set(-200, 50, 777);
                }
                scope.center = scope.centers[scope.currentCenter];
            } else {
                if(!GUIOptions.hideAtmosphere) {
                    scope.currentCenter = 1;
                } else {
                    scope.currentCenter = 0;

                //                    renderBlock.camera.position.set(-200, 50, 777);
                }

                
                //                renderBlock.camera.position.set(0, 0, 1500);
                scope.center = scope.centers[scope.currentCenter];
            }
        });
        gui.add(renderBlock, 'enableFXAA').name("FXAA trigger (better text, ugly egdes)").onChange(function() {

            renderBlock.initComposer(renderBlock.enableFXAA);


        });

        /* gui.add(renderBlock, 'composerEnabled').name("postprocessing trigger").onChange(function() {
            if(renderBlock.composerEnabled) {

                //bgColor =0x07070c;
                //  renderBlock.renderer.setClearColor(bgColor);
                renderBlock.initPostprocessing(renderBlock.postprocessing,
                    renderBlock.SCREEN_HEIGHT,
                    renderBlock.SCREEN_WIDTH,
                    renderBlock.config.sunIntensity,
                    renderBlock.config.bgColor,
                    renderBlock.config.sunColor,
                    renderBlock.config.godRayIntensity);
            } else {
                //bgColor =0x000207;
                // renderBlock.renderer.setClearColor(bgColor);
                renderBlock.initPostprocessing(renderBlock.postprocessing,
                    renderBlock.SCREEN_HEIGHT,
                    renderBlock.SCREEN_WIDTH,
                    renderBlock.config.sunIntensity,
                    renderBlock.config.bgColor,
                    renderBlock.config.sunColor,
                    renderBlock.config.godRayIntensity);
            }
        });*/


        gui.add(GUIOptions, 'hideText').name('hide text').onChange(function () {
            moons = renderBlock.planets;
            if (GUIOptions.hideText) {
                for (moon in moons) {
                    moons[moon].children[0].visible = false;
                }
            } else {
                for (moon in moons) {
                    moons[moon].children[0].visible = true;
                }
            }
            renderBlock.planets = moons;
        });
        gui.add(GUIOptions, 'hideUnits').name("hide units").listen;
        gui.add(GUIOptions, 'hideWire').name('hide planet wire').onChange(function () {
            moons = renderBlock.planets;
            if (GUIOptions.hideWire) {
                for (moon in moons) {
                    //                    if(moon!=1)
                    moons[moon].children[1].visible = false;
                }
            } else {
                for (moon in moons) {
                    moons[moon].children[1].visible = true;
                }
            }
            renderBlock.planets = moons;
        });
        gui.add(GUIOptions, 'showSelection').name('show selection always (off - only on mouse hover)').onChange(function () {

            for(planet in renderBlock.planets) {
                renderBlock.planets[planet].triggered = GUIOptions.showSelection;
            }
            for(arrow in renderBlock.actionMap){
                renderBlock.actionMap[arrow].line.visible = GUIOptions.showSelection;
            }

        });

        //	gui.add(this, 'godRayIntensity').min(0.1).max(1.0).step(0.01).onChange(function (strength) {
        //			godRayIntensity = strength;
        //			initPostprocessing();
        //
        //		});
        /*    gui.add(GUIOptions, 'sunIntensity').min(0.1).max(9.5).step(0.01).onChange(function (strength) {
            renderBlock.config.sunIntensity = strength;
            cameraPosition = renderBlock.camera.position;

            distance = Math.pow((cameraPosition.x*cameraPosition.x + cameraPosition.y*cameraPosition.y + cameraPosition.z*cameraPosition.z),0.5);

            renderBlock.initPostprocessing(renderBlock.postprocessing,
                renderBlock.SCREEN_HEIGHT,
                renderBlock.SCREEN_WIDTH,
                renderBlock.config.sunIntensity,
                renderBlock.config.bgColor,
                renderBlock.config.sunColor,
                renderBlock.config.godRayIntensity);
        });*/

        /*
        gui.add(renderBlock.cameraConfig, 'fov').min(10).max(120).step(0.01).onChange(function (v) {
            renderBlock.camera.fov = v;
            renderBlock.camera.updateProjectionMatrix();
        });*/





        gui.add(GUIOptions, 'fullScreen').name("FULLSCREEN ").listen().onChange(function () {
            renderBlock.turnOnFullScreen();
        })
        renderBlock.gui = gui;
    },



    initMaterials : function(){
        for(planetMesh in renderBlock.planets) {
            renderBlock.renderer.initMaterial(planetMesh.material, renderBlock.light, renderBlock.scene.fog, planetMesh);
        }
    },



    createTextTexture: function (planetId) {

        var canvas = renderBlock.canvases[planetId];
        owner = renderBlock.planets[planetId].owner;

        size = renderType == 'WebGL' ? 512: 256;
	    var type = renderBlock.planets[planetId].type;
	    var planetSize = renderBlock.getPlanetSize(type)*Math.PI + 26;
	    var maxUnits = renderBlock.getMaxPlanetUnit(type);
	    var multiplier = renderBlock.planets[planetId].unitsCount/maxUnits;
	    if(multiplier> 1) multiplier = 1;

        canvas.width = canvas.height = size;

        var context = canvas.getContext('2d');
	    context.fillStyle = "rgba(127,255,159, 0.5)";
//	    context.fillRect(0,0,size,size);

//	    context.fillRect(canvas.width/2,canvas.width/2,planetSize,planetSize);
        if(renderType == 'WebGL') {
            context.shadowColor = "#000";
            context.shadowOffsetX = 1;
            context.shadowOffsetY = 1;
            context.shadowBlur = 1;
        }
//	    context.font = "normal 46px " + renderBlock.fontFamily;
        context.font = renderType == 'WebGL' ? "normal 42pt Play" : "bold 26pt Play";// + renderBlock.fontFamily;
//	    context.font += renderBlock.fontFamily;



        context.fillStyle =   renderBlock.getMaterial(owner).color.getContextStyle();


        context.textAlign = "center";

        context.fillText(renderBlock.planets[planetId].unitsCount, canvas.height / 2,  canvas.height /2 - planetSize, canvas.width);

//        context.font =  renderType == 'WebGL' ? "22pt ": "20pt ";

        context.fillText(owner , canvas.height / 2,  canvas.height / 2 + planetSize+22,canvas.width);
        //        context.fillText(renderBlock.planets[planetId].name+ ' (' + planetId + ')' , canvas.height / 3,  3* canvas.height / 6,canvas.width);

	    planetSize = renderBlock.getPlanetSize(type)*Math.PI*1.3 -4;
	     context.fillStyle = "rgba(127,127,127, 0.5)";
//	    context.lineWidth = 0.03;


	    context.moveTo(canvas.height / 2, canvas.height / 2);
	    context.beginPath();
	    context.arc( canvas.height / 2, canvas.height / 2, planetSize, Math.PI * 1.5, Math.PI*2*multiplier + Math.PI*1.5, false );
	    context.lineTo(canvas.height / 2,canvas.height / 2 );

	    context.closePath();

	    context.fill();

//	    context.stroke();
        return canvas;



    },


    initStatTexture: function(size) {
        renderBlock.statCanvas = document.createElement( 'canvas' );
        renderBlock.statCanvas.width =size;
        renderBlock.statCanvas.height = size/10*modelBlock.players.length;
    },

    updateStatTexture: function(canvas, stats, max) {
    /*var ctx=c.getContext("2d");
        var gradient = ctx.createRadialGradient(
            c.width / 2,
            c.height / 2,
            c.height / 8,
            c.width / 2,
            c.height / 2,
            c.width /2 );
        gradient.addColorStop( 0, 'rgba(255,255,255,1)' );
        gradient.addColorStop( 0.3, 'rgba(157,164,247,1)' );
        gradient.addColorStop( 0.8, 'rgba(31,34,66,1)' );
        gradient.addColorStop( 1, 'rgba(0,0,0,1)' );

        ctx.fillStyle=gradient;
        ctx.fillRect(0,0,c.width,c.height);*/

    },

    switchCamera: function() {

    },

    textGenerator: function (planetId, angle) {

        var texture = new THREE.Texture(renderBlock.createTextTexture(planetId)); //TODO check the arguments - have promlem with undefined
        //var texture = new THREE.Texture(renderBlock.generateSpriteTexture(256));
        texture.premultiplyAlpha = true;
        renderBlock.textures[planetId] = texture;
        texture.needsUpdate = true;
        if(renderType=='WebGL') {
            var sprite = new THREE.Sprite({
                map: texture,
                //                   blending: THREE.AdditiveBlending,
                useScreenCoordinates: false
            });
            //            sprite.scaleByViewport = false;
            //            sprite.affectedByDistance = true;
            sprite.mergeWith3D = false;
            //            sprite.rotation3d = new THREE.Vector3();
            return sprite;
        } else {
            var geo = new THREE.PlaneGeometry(256, 256, 1, 1);
            var material = new THREE.MeshBasicMaterial({
                overdraw: true,
                useScreenCoordinates: false,
                map: texture
            });
            material.side = THREE.DoubleSide;
            sprite = new THREE.Mesh(geo, material);
            sprite.rotation.z = angle ? angle : 0;

            return sprite;
        }
    },

    initRenderer: function () {
        // renderer = new THREE.CanvasRenderer();
        if(renderType=='WebGL'){
            renderer = new THREE.WebGLRenderer({
                preserveDrawingBuffer: true,
                alpha: true,
                antialias: true,
                maxLights: 16,
                clearAlpha: 1

            });
            //            renderer.generateMipmaps = false;
            //            renderer.shadowMapEnabled = true;
            //            renderer.shadowMapSoft = true;
            //    renderer = new THREE.SoftwareRenderer3();
            //            renderer.sortObjects = false;
            renderer.autoClear = false;

            renderer.setClearColorHex(renderBlock.config.bgColor, 1);
            //            renderer.setClearColorHex(0x282828, 0.5);
            

            renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);
            renderer.domElement.style.position = "relative";
            renderBlock.renderer = renderer;

        } else {
            renderer = new THREE.CanvasRenderer();
            //            renderer.generateMipmaps = false;
            //    renderer = new THREE.SoftwareRenderer3();
            //            renderer.sortObjects = true;
            //            renderer.autoClear = false;

            renderer.setClearColorHex(renderBlock.config.bgColor, 0);

            //            renderer.setClearColorHex(0xa6a3a0, 1);
            
            renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);
            renderer.domElement.style.position = "relative";
            renderBlock.renderer = renderer;
        }

        renderer.domElement.addEventListener( 'mousemove', loopBlock.onDocumentMouseMove, false );
        renderer.domElement.addEventListener( 'mousedown', renderBlock.onDocumentMouseDown, false );


    },

    drawRing: function(rad, color) {
        var _color = color == undefined ? 0xffffff : color;
        var arcShape = new THREE.Shape();
        arcShape.absarc(0, 0, rad, 0, Math.PI * 2, false);
        var arcPoints = renderType == 'WebGL' ?  arcShape.createPointsGeometry(32) : arcShape.createPointsGeometry(32);
        //    var circlePoints = circleShape.createPointsGeometry();
        var particles; //= new THREE.ParticleSystem(geometry, new THREE.ParticleBasicMaterial({ color: 0x444444 }));
        particles = new THREE.Line(arcPoints, new THREE.LineBasicMaterial({
            color: _color
        //                        opacity: 0.5
        }));

        particles.visible = true;
        //  particles.rotation.set(0.5, 0.5, 0);
        //   particles.rotation.set(Math.random(), Math.random(), Math.random());
        //        particles.rotation.x = Math.PI/2;
        particles.gameName = "selectCircle";
        return particles
    },

    getRingsNumber: function (type) {
        switch(type) {
            case 'TYPE_A':
                return 4;
            case 'TYPE_B':
                return 3;
            case 'TYPE_C':
                return 2;
            case 'TYPE_D':
                return 1;
            default:
                return 1;
        }
    },

    generateStars: function () {
        renderBlock.rings = {};
        map = modelBlock.planetMap;
        moons = renderBlock.planets;
        if(renderBlock.showTrace && 0  ){
            for (moon in moons) { //TODO remake geometry

                //                var rad = moons[moon].geometry.boundingSphere.radius * 2.3;
                //
                //                var i = renderBlock.getRingsNumber(moons[moon].type);
                //                renderBlock.rings[moon] = [];
                //                if(renderBlock.showTrace ){
                //                    while(--i) {
                //                        particles = renderBlock.drawRing(rad/1.61+i*Math.pow(rad,0.5));
                //                        //                        moons[moon].add(particles);
                //                        renderBlock.rings[moon].push(particles);
                //                    }
                //                }
                rad = renderBlock.getDistance(renderBlock.mesh.position, moons[moon].position);
                renderBlock.mesh.add(renderBlock.drawRing(rad, 0x444444));

            }

        }
        var rad = renderType=='WebGL' ? 40000 : 31000;
        opt = {
            color: 0xaaaaaa
        };
        texture =  new THREE.Texture( renderBlock.generateSpriteTexture(8));
        texture.needsUpdate = false;
        material = renderType == 'WebGL' ? new THREE.ParticleBasicMaterial() : new THREE.ParticleBasicMaterial( {
            //            map: texture,
            blending: THREE.AdditiveBlending
        } );
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var cloud = new THREE.Geometry();
        var lambda = 1.0;
        starCount = renderType == 'WebGL' ? 5000 : 1500;
        if(renderType == 'WebGL'){
            for(var i = 0;  i < starCount; i++) {
                var angle1 = Math.random() * 2 * Math.PI ;
                var angle2 = Math.random() * 2 * Math.PI ;
                var vertex = new THREE.Vector3();
                vertex.x = rad * Math.cos(angle1) * Math.sin(angle2);
                vertex.y = rad * Math.sin(angle1) * Math.sin(angle2);
                vertex.z = rad * Math.cos(angle2);
                cloud.vertices.push(vertex);

            }
        } else {
            for(var i = 0;  i < starCount; i++) {
                particle = new THREE.Particle( material );
                particle.position.x  = (Math.random()-0.5)*renderBlock.SCREEN_WIDTH*3.5;
                particle.position.y  = (Math.random()-0.5)*renderBlock.SCREEN_HEIGHT*3.5;
                particle.position.z  = -20;
            //                    renderBlock.scene.add(particle);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        var _size =8;
        //var aTexture = new THREE.Texture(renderBlock.generateSpriteTexture(_size));
        aTexture = renderBlock.smallNovaTexture;
        aTexture.needsUpdate = true;


        particles = new THREE.ParticleSystem(cloud, material );
        particles.visible = true;
        particles.sortParticles = true;

        renderBlock.scene.add(particles);

    //        renderBlock.planets = moons;
    },

    programStats: function( context , size) {
	    context = renderBlock.stats.getContext("2d");
        _size = size == undefined ? 0 : size;
	    _size = 512;
        step = size / modelBlock.players.length;
        stats = modelBlock.stats;
        height	= modelBlock.players.length * 16 - 1;
        max = modelBlock.max;
        players = modelBlock.players;
        var i = 1;

        context.scale(1,-1);
	    context.clearRect(0,0,_size, _size);


	    heightSize = renderType == 'WebGL' ? 26 : 16;

	    barsize = 100;
	    spacing = 10;
	    x = 150;
	    var offset = 30;
	    if(renderBlock.GUIOptions.fullScreen) {
		    heightSize = 26;
		    barsize = 200;

	    }

        for(player in players ){

	        name = players[player].name;
            y = i*heightSize *1.4 + offset;
            width = stats[name] / (max ); //TODO  normalization
            //                        context.rotate(Math.PI*2/(i*modelBlock.players.length));
            fontSize = heightSize;
            font = renderBlock.fontFamily;
            context.textAlign = "right";
            context.textBaseline = "middle";



            context.font = fontSize+'pt ' + font;
            var color = new THREE.Color(1- renderBlock.getMaterial(name).color.getHex());
            context.fillStyle = renderBlock.getMaterial(name).color.getContextStyle();
            //            context.shadowColor = color.getContextStyle();
            //            context.shadowOffsetX = 0;
            //            context.shadowOffsetY = 0;
            //            context.shadowBlur = 5;


            context.fillText(name,  x, y, x);
//	        context.renderText(name, x, y, 3);
            context.fillRect(x+spacing, y-fontSize/4, width*barsize,  fontSize/2);
            context.strokeStyle = color.getContextStyle();
            //            context.strokeRect(x+110, y, width*200,  fontSize/2)

            context.textAlign = "left";
            context.fillText(stats[name],  x+barsize+spacing*2, y);
            i++ ;

        }


    },


    createStats: function(){
	    renderBlock.stats = $("#canv").get(0);
       /* statMaterial = new THREE.ParticleCanvasMaterial(  {
            //                    color: 0xffffff,
            program: renderBlock.programStats
        } );

        if(renderType == 'Canvas') {
            stats = new THREE.Particle(statMaterial);
            stats.position.x = -renderBlock.SCREEN_WIDTH*renderBlock.scene.scale.x*0.9;
            stats.position.y = renderBlock.SCREEN_HEIGHT*renderBlock.scene.scale.y;
//            stats.scale = new THREE.Vector3(2,2,2);
            renderBlock.scene.add(stats);
        } else {

            renderBlock.statCanvas =  document.createElement('canvas');
            size = 512;
            renderBlock.statCanvas.width = size;
            renderBlock.statCanvas.height = size;
            renderBlock.programStats(renderBlock.statCanvas.getContext('2d'));
            texture = new THREE.Texture(renderBlock.statCanvas);
            texture.needsUpdate = true;
            var geo = new THREE.PlaneGeometry(512, 512, 1, 1);
            var material = new THREE.MeshBasicMaterial({
                //                overdraw: true,
                useScreenCoordinates: true,
                map: texture
            });
            material.side = THREE.DoubleSide;
            stats = new THREE.Sprite({
                useScreenCoordinates: false,
                map: texture
            });

            //            sprite.scaleByViewport = false;
            //            sprite.affectedByDistance = true;

            stats.position.x = 0;// -renderBlock.SCREEN_WIDTH/2 ;
            stats.position.y = 0;// renderBlock.SCREEN_HEIGHT/2;
        //            renderBlock.scene.add(stats);

        }

        //             stats.rotation(Math.PI/2, Math.PI/2*3 ,0);
        //        stats.scale.x = stats.scale.y = 1;

        
        renderBlock.stats = stats; */



    },

    init : function() {
        //        console.log("initializing renderBlock...");
        
        
        renderBlock.SCREEN_WIDTH  = $('#container').outerWidth();//TODO remake to the size of container
        renderBlock.SCREEN_HEIGHT = $('#container').outerHeight();

        renderBlock.scene = new THREE.Scene();
        cameraConfig = renderBlock.cameraConfig;
        if(renderType=='WebGL') {
            renderBlock.camera = new THREE.PerspectiveCamera(cameraConfig.fov, renderBlock.SCREEN_WIDTH /  renderBlock.SCREEN_HEIGHT, cameraConfig.near, cameraConfig.far);
        } else {
            renderBlock.camera = new THREE.PerspectiveCamera(cameraConfig.fov, renderBlock.SCREEN_WIDTH /  renderBlock.SCREEN_HEIGHT, cameraConfig.near, cameraConfig.far);
        //            renderBlock.camera = new THREE.OrthographicCamera( renderBlock.SCREEN_WIDTH / - 2, renderBlock.SCREEN_WIDTH / 2, renderBlock.SCREEN_HEIGHT / 2, renderBlock.SCREEN_HEIGHT / - 2, - 2000, 10000 );
        }
        renderBlock.scene.add(renderBlock.camera);


        if(renderType=='WebGL'){
            renderBlock.fillGLModel();
            renderBlock.createSun();
            renderBlock.createSystem();
        } else {
            renderBlock.fillCanvasModel();
            renderBlock.createSun();
        //            renderBlock.createSun();
        }
        renderBlock.scene.matrixAutoUpdate = false;

        //        if(renderType=='WebGL'){
        //            renderBlock.generateUnits();
        //
        //        }

        renderBlock.generateStars();
        renderBlock.createStats();
        renderBlock.initRenderer();

        //renderBlock.initMaterials();

        renderBlock.addToDocument();

        try {
            if(renderType == 'WebGL')renderBlock.initGUI(renderBlock.gui, renderBlock.GUIOptions);
        } catch ( e )    {}
        
        //renderBlock.camera.position.set(-518, 0,0);
        renderBlock.addArrows();
        for(planet in renderBlock.planets) {
            renderBlock.planets[planet].triggered = renderBlock.GUIOptions.showSelection;
        }
        for(arrow in renderBlock.actionMap){
            renderBlock.actionMap[arrow].line.visible = renderBlock.GUIOptions.showSelection;
        }

        renderBlock.addControls(renderBlock.camera, false);
        if(renderType=='WebGL'){
            //            renderBlock.camera.position.set(-518, -624, 1208);
            //            renderBlock.camera.rotation.set(0.4, -0.2, 0.35);
            //            renderBlock.camera.position.set(0, 100, 2500);
            renderBlock.camera.position.set(1327, -980, 777);
            renderBlock.camera.rotation.set(0.0, 0.0, 0.0);
            renderBlock.initComposer(renderBlock.enableFXAA);
        } else {
            //            renderBlock.camera.position.set(1327, -980, 777);
            //            renderBlock.camera.rotation.set(0.0, 0.0, 0.0);
            renderBlock.controls.currentCenter = 1;
            renderBlock.camera.position.set(0, 200, 2500);
            renderBlock.controls.center = renderBlock.controls.centers[renderBlock.controls.currentCenter];

        }
    //        renderBlock.deCentralizeCamera();



    },

    programFill: function ( context ) {
        context.beginPath();
        context.arc( 0, 0, 1, 0, Math.PI * 2, true );
        context.closePath();
        context.fill();
    

    },
    programStroke: function ( context ) {
        var color = context.fillStyle.valueOf();
        var grd = context.createRadialGradient(0, 0, 12, 0, 0, 13);
        // light blue
        //        grd.addColorStop(0, '#000');
        //1E2730
        grd.addColorStop(.5, 'rgba(60,78,96,0.2)');
        // dark blue
        grd.addColorStop(1, '#fff');
        context.fillStyle = grd;
        context.lineWidth = 0.1;
        context.beginPath();
        context.arc( 0, 0, 1, 0, Math.PI * 2, true );
        context.closePath();
        context.fill();

        context.fillStyle = color;
        context.stroke();


                context.lineWidth = 0.03;
                context.beginPath();
                context.arc( 0, 0, 1.2, 0, Math.PI * 2, true );
                context.closePath();



                context.lineWidth = 0.01;
                context.beginPath();
                context.arc( 0, 0, 1.7, 0, Math.PI * 2, true );
                context.closePath();
                context.stroke();

//                context.lineWidth = 0.1;
//                context.beginPath();
//                context.arc( 0, 0, 0.5, 0, Math.PI * 2, true );
//                context.closePath();
//                context.stroke();

        context.lineWidth = 0.1;
        context.beginPath();
        context.arc( 0, 0, 0.1, 0, Math.PI * 2, true );
        context.closePath();
        context.fill();




    },
    programSun: function(context) {
        //    -webkit-radial-gradient(50% 51%, circle farthest-corner,
        //    rgba(150, 234, 255, 0.01) 0%,
        //    rgba(150, 234, 255, 0.7) 53%,
        //     rgba(150, 234, 255, 0.5) 55%,
        //      rgba(150, 234, 255, 0.98) 62%,
        //      rgba(150, 234, 255, 0.1) 99%);
        var color = context.fillStyle.valueOf();
        var rgb = 'rgba(150,234,255,';
        var grd = context.createRadialGradient(0, 0, 1, 0, 0, 13);
        grd.addColorStop(0,rgb+'0.01)');
        grd.addColorStop(0.53,rgb+'0.7)');
        grd.addColorStop(0.55,rgb+'0.5)');
        grd.addColorStop(0.62,rgb+'0.98)');
        grd.addColorStop(0.99,rgb+'0.1)');


        var gradient = context.createRadialGradient(
            10 / 2,
            10 / 2,
            10 / 8,
            10 / 2,
            10 / 2,
            10 /2 );
        gradient.addColorStop( 0, 'rgba(255,255,255,1)' );
        gradient.addColorStop( 0.3, 'rgba(157,164,247,1)' );
        gradient.addColorStop( 0.8, 'rgba(31,34,66,1)' );
        gradient.addColorStop( 1, 'rgba(0,0,0,1)' );
        //
        ////        context.fillStyle=gradient;



        context.fillStyle = gradient;
        context.lineWidth = 0.1;
        context.beginPath();
        context.arc( 0, 0, 1, 0, Math.PI * 2, true );
        context.closePath();
        context.fill();
    },

    correctAngle: function(angle) {
        //        angle -= Math.PI/180;
        angle = 0;
        if(angle < 0) angle = angle + Math.PI;
        if(angle >= 0 && angle < Math.PI/4) return angle;
        if(angle >=Math.PI/4 && angle < 3*Math.PI/4 ) return angle - Math.PI/2;
        if(angle >= 3*Math.PI/4 && angle <5*Math.PI/4) return angle - Math.PI;
        return angle;
    },

    fillCanvasModel: function() {
        map = modelBlock.planetMap;
        renderBlock.planets = {};
        renderBlock.initCanvases(map);
        renderBlock.mesh = new THREE.Object3D();



        for (planetnum in map) {
            planet = map[planetnum];
            /*

 var particle = new THREE.Particle( new THREE.ParticleCanvasMaterial( { color: Math.random() * 0x808080 + 0x808080, program: renderBlock.programStroke } ) );
            particle.scale.x = particle.scale.y = Math.random() * 10 + 10;
            renderBlock.scene.add( particle );

         **/

            if (planet.id == 1) {
                sunMaterial = new THREE.ParticleCanvasMaterial(  {
                    color: 0xffffff,
                    program: renderBlock.programStroke
                } );

                sun = new THREE.Particle(sunMaterial);
                sun.scale.x = sun.scale.y = sun.scale.z = renderBlock.getPlanetSize(planet.type)*3;

                //                sun.rotation.x = Math.PI/180*90;
                renderBlock.fillPlanet(sun, planet);
                renderBlock.planets[planet.id] = sun;
                //                renderBlock.mesh = sun;
                renderBlock.mesh.add(sun);
                //                sun.position.z = -1;

                sprite = renderBlock.textGenerator(sun.planetId);
                //                sprite.position.x = sprite.position.y = sun.scale.x ;

                sprite.scale.x = sprite.scale.y = 1.3/ sun.scale.x ;
                sprite.position.z = 0.01  ;
                sprite.position.y = 0;//2.1;
                sprite.position.x = 0;//.5;
                sprite.useScreenCoordinates = false;

                //                 sprite.position.x = 1;
                //                sprite.position.y = 1.5;
                //TODO
                //                sprite.position.set(sun.geometry.boundingSphere.radius + 5, sun.geometry.boundingSphere.radius + 5, sun.geometry.boundingSphere.radius + 5);
                sun.add(sprite);
                var geo = new THREE.PlaneGeometry(256, 256, 1, 1);
                var material = new THREE.MeshBasicMaterial({
                    blending: THREE.AdditiveBlending,
                    overdraw: true,
                    map: renderBlock.novaTexture
                });
                atmos = new THREE.Mesh(geo, material);
                size = renderBlock.getPlanetSize(planet.type)/2800;
                atmos.scale.set(size,size,size);
                atmos.position.z = 0;
                //                                sun.add(atmos);

                wireMaterial = new THREE.MeshBasicMaterial();
                wireMaterial.wireframe = true;

                //                var wire = new THREE.Mesh(new THREE.SphereGeometry(renderBlock.getPlanetSize(planet.type)*0.030, 16/2, 16 / 4), wireMaterial);
                wire = new THREE.Mesh(new THREE.IcosahedronGeometry(renderBlock.getPlanetSize(planet.type)*0.1, 1), wireMaterial);
                //                sun.add(wire);
                renderBlock.planets[planet.id] = sun;

            } else {


                //                planetMaterial = renderBlock.getMaterial(planet.owner);
                planetMaterial = new THREE.ParticleCanvasMaterial(  {
                    color: renderBlock.getMaterial(planet.owner).color,
                    program: renderBlock.programStroke
                } );

                // TO DECIDE
                //                planetMaterial.map = renderBlock.getPlanetTexture(planet.type);

                var planetMesh = new THREE.Particle(planetMaterial);
                planetMesh.scale.x = planetMesh.scale.y = planetMesh.scale.z  = renderBlock.getPlanetSize(planet.type)*3;
                renderBlock.fillPlanet(planetMesh, planet);


                planetMesh.position.x = planet.xCoord * 3.0;
                planetMesh.position.y = planet.yCoord * 3.0;
                planetMesh.position.z = 10;

                //                planetMesh.position.z = 0;
                angle = Math.atan2(planetMesh.position.y, planetMesh.position.x);
                angle = renderBlock.correctAngle(angle);
                //                angle = angle > Math.PI/2 ? angle - Math.PI: angle < 0 ? angle + Math.PI : angle;
                //
                //arctan2(y2-y1,x2-x1)

                //                angle = Math.PI/6;

                renderBlock.planets[planet.id] = planetMesh;
                sprite = renderBlock.textGenerator(planetMesh.planetId, angle);
                sprite.scale.x = sprite.scale.y = 1.3/ planetMesh.scale.x;// /2000 ;
	            console.log(sprite.scale.x);
                //                sprite.position.set(planetMesh.geometry.boundingSphere.radius + 5, planetMesh.geometry.boundingSphere.radius + 5, planetMesh.geometry.boundingSphere.radius + 5);
                sprite.position.x = 0;//2;//1.2;
	            sprite.position.z = 0.01;
	            sprite.position.y = 0;//2.1;
                planetMesh.add(sprite);
                //think about frame buffer


                var geo = new THREE.PlaneGeometry(256, 256, 1, 1);
                var material = new THREE.MeshBasicMaterial({
                    blending: THREE.AdditiveBlending,
                    overdraw: true,
                    map: renderBlock.atmos2Texture
                });
                atmos = new THREE.Mesh(geo, material);
                size = renderBlock.getPlanetSize(planet.type)/500;
                atmos.scale.set(size,size,size);

                //                planetMesh.add(atmos);



                //                planetMesh.add(ring);
                //                renderBlock.scene.add(planetMesh);

                wireMaterial = new THREE.MeshBasicMaterial();
                wireMaterial.wireframe = true;

                //                var wire = new THREE.Mesh(new THREE.SphereGeometry(renderBlock.getPlanetSize(planet.type)*0.030, 16/2, 16 / 4), wireMaterial);
                wire = new THREE.Mesh(new THREE.IcosahedronGeometry(renderBlock.getPlanetSize(planet.type)*0.1, 1), wireMaterial);
                //                planetMesh.add(wire);
                renderBlock.mesh.add(planetMesh);
            }

        }
        //                renderBlock.mesh.rotation.y = Math.PI / 50 * 6;
        renderBlock.mesh.scale.x = renderBlock.mesh.scale.y = 0.6;
        renderBlock.scene.add(renderBlock.mesh);



    },


    initComposer: function(enablefxaa) {
        if(renderType=='WebGL'){
            var width = renderBlock.SCREEN_WIDTH ;
            var height = renderBlock.SCREEN_HEIGHT ;

            options = {
                magFilter : THREE.LinearFilter,
                minFilter : THREE.LinearMipMapLinearFilter,
                anisotropy : 0,
                format : THREE.RGBAFormat,
                type : THREE.UnsignedByteType,
                depthBuffer : true,
                stencilBuffer : true
            };

            renderBlock.renderTarget = new THREE.WebGLRenderTarget(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT, options);


            var renderModel = new THREE.RenderPass( renderBlock.scene, renderBlock.camera );

            var effectBloom = new THREE.BloomPass( renderBlock.bloomStrength, 25, 4.0, 128  );


            var effectScreen = new THREE.ShaderPass( THREE.ShaderExtras[ "screen" ] );

            renderBlock.effectFXAA = new THREE.ShaderPass( THREE.ShaderExtras[ "fxaa" ] );

            effectScreen.renderToScreen = true;
            renderBlock.composer = new THREE.EffectComposer( renderBlock.renderer ,renderBlock.renderTarget);

            renderBlock.composer.addPass( renderModel );
            renderBlock.composer.addPass( effectBloom );


            if(renderBlock.blurEnable && 0) {
                hblur = new THREE.ShaderPass( THREE.ShaderExtras[ "horizontalTiltShift" ] );
                vblur = new THREE.ShaderPass( THREE.ShaderExtras[ "verticalTiltShift" ] );
                var bluriness = 0.3;

                hblur.uniforms[ 'h' ].value = bluriness / renderBlock.SCREEN_WIDTH;
                vblur.uniforms[ 'v' ].value = bluriness / renderBlock.SCREEN_HEIGHT;

                hblur.uniforms[ 'r' ].value = vblur.uniforms[ 'r' ].value = 0.5;

                renderBlock.composer.addPass( vblur );
                renderBlock.composer.addPass( hblur );


            }

            renderBlock.effectFXAA.uniforms[ 'resolution' ].value.set( 1 / width, 1 / height );
            if(renderBlock.enableFXAA) {
                renderBlock.composer.addPass( renderBlock.effectFXAA );

            //            renderBlock.enableFXAA = true;
            }
            renderBlock.composer.addPass( effectScreen );

        }
    },

    fillPlanet: function (mesh, planet) {
        mesh.planetId = planet.id;
        mesh.neighbors = planet.neighbors;
        mesh.owner = planet.owner;
        mesh.unitsCount = planet.unitsCount;
        mesh.name = planet.name;
        mesh.regenRate = planet.regenRate;
        mesh.type = planet.type;
        mesh.triggered = true;
    },
    addToDocument: function () {
        $container = $('#container');

        $('canvas').css({
            "background-image":"none"
        });
        
        $container.append(renderer.domElement);
        

        var info = document.createElement( 'div' );
        //				info.style.position = 'absolute';
        info.style.top = '-100px';
        info.style.width = '100%';
        info.style.textAlign = 'center';
        info.style.fontFamily='Arial';
        info.style.fontSize = '18px';
        info.innerHTML = 'F - игру на полный экран, S - показать все связи между планетами';
        $container.append( info );

    },

    createSun: function() {
        
        //                secondSun.scale.set(70,70,70);


        //                                sun.add(plane);
        //
        //                                var backGround = new THREE.Sprite({
        //                                    map:tex,
        //                                     blending: THREE.AdditiveBlending,
        //                                     useScreenCoordinates: false
        //                                });
        //                                backGround.mergeWith3D = true;
        ////                               THREE.adjustHSV( backGround.color,0.0, -0.6);
        //                                backGround.transparent = true;
        //                                backGround.opacity = 0.9;
        //                                backGround.useScreenCoordinates = false
        //                                backGround.rotationAutoUpdate = false;
        //                                backGround.position.set(renderBlock.SCREEN_WIDTH/2,renderBlock.SCREEN_HEIGHT/2,-1000);
        //                                sun.add(backGround);
        //                                 sunAtmos = new THREE.Sprite(
        //                                 {
        //                                     map:renderBlock.atmos2Texture,
        //                                     blending: THREE.AdditiveBlending,
        //                                     useScreenCoordinates: false
        //                                 });
        //
        //         material = new THREE.MeshBasicMaterial({
        //                    map:renderBlock.sunTexture
        //                });
        //                //                console.log("galaxy");
        //                //               console.log(renderBlock.galaxyTexture);
        //                material.side = THREE.DoubleSide;
        //                galaxy = new THREE.Mesh(new THREE.SphereGeometry(100000, lod, lod), material);
        //                //                galaxy.side = ;
        //                sun.add(galaxy);
        //
        //
        //                                sun.add(sunAtmos);


        //        renderBlock.light = new THREE.PointLight(renderBlock.config.lightColor, renderBlock.config.lightIntensity, renderBlock.config.lightDistance );
        if(renderType == 'WebGL'){
            renderBlock.light = new THREE.DirectionalLight(renderBlock.config.lightColor, renderBlock.config.lightIntensity, renderBlock.config.lightDistance);
            renderBlock.light.lookAt(renderBlock.mesh);
            //            renderBlock.light.shadowCameraNear = 3;
            renderBlock.light.intensity = 1.0;
            //            renderBlock.light.shadowCameraFar = renderBlock.config.lightDistance;

            //            renderBlock.light.shadowMapBias = 0.0039;
            //            renderBlock.light.shadowMapDarkness = 0.9;
            //            renderBlock.light.shadowMapWidth = 1024;
            //            renderBlock.light.shadowMapHeight = 1024;
            //            renderBlock.light.castShadow = true;

            sun = shaderBlock.loadSun(5000);
            sun = renderBlock.sun;
            sun.position.x = -20000;
            sun.position.y = 11000;
            sun.position.z = 0;
            sun.rotation.x =  Math.PI/180*90;
            sun.rotation.y =  Math.PI/180*90;

            renderBlock.light.position =sun.position;
            renderBlock.scene.add(renderBlock.light);

            //        renderBlock.sun = sun;
            //        renderBlock.sun.matrixAutoUpdate = false;
            //        renderBlock.sun.updateMatrix();
            sunAtmos = new THREE.Sprite(
            {
                map:renderBlock.novaTexture,
                blending: THREE.AdditiveBlending,
                //                color:0xaaeeff,
                useScreenCoordinates: false
            });
            //                sunAtmos.scale.set(1.9,1.9,1.9);
            sunAtmos.scale.set(320.2,320.2,320.2);
            
            //            sunAtmos.mergeWith3D = false;
            //                                                sunAtmos.position.z =-10000;
            //                                                sunAtmos.position.y =-10000;
            //                                                sunAtmos.position.x =-10000;
            //                        sunAtmos.affectedByDistance = true;
            sunAtmos.opacity = 1;
            sunAtmos.rotation = 10;

            sun.add(sunAtmos);
            renderBlock.scene.add(sun);
            //            backLight = new THREE.DirectionalLight(renderBlock.config.lightColor, renderBlock.config.lightIntensity, renderBlock.config.lightDistance);
            //            backLight.lookAt(renderBlock.mesh);
            //            backLight.intensity = 0.1;
            //            backLight.position.set(20000, -11000,0);
            ////            renderBlock.scene.add(backLight);
            amb = new THREE.AmbientLight(renderBlock.config.lightColor*0.2);
            amb.intensity = 0.002;
            renderBlock.scene.add(amb);

        }
        else {
            sunMaterial = new THREE.ParticleCanvasMaterial(  {
                color: 0xff6600,
                program: renderBlock.programSun
            } );

            sun = new THREE.Particle(sunMaterial);
            sun.scale.x = sun.scale.y = 500;
            sun.position.x = 0;
            sun.position.y = 0;
            sun.position.z = -2;
            renderBlock.sun = sun;
//                    renderBlock.scene.add(renderBlock.sun);
        }
    },

    createSystem: function() {

        uniforms = {
            fogDensity: {
                type: "f",
                value: 0.00001
            },
            fogColor: {
                type: "v3",
                value: new THREE.Vector3( 0, 0, 0 )
            },
            time: {
                type: "f",
                value: 1.0
            },
            resolution: {
                type: "v2",
                value: new THREE.Vector2()
            },
            uvScale: {
                type: "v2",
                value: new THREE.Vector2( 3.0, 1.0 )
            },
            texture1: {
                type: "t",
                value: 0,
//                texture:renderBlock.e
                            texture: THREE.ImageUtils.loadTexture( './js/lavatile.jpg')
            },
            texture2: {
                type: "t",
                value: 1,
//                texture: renderBlock.atmos1Texture
                            texture: THREE.ImageUtils.loadTexture( './js/lavatile.jpg')
            }
        }

        uniforms.texture1.texture.wrapS = uniforms.texture1.texture.wrapT = THREE.RepeatWrapping;

        uniforms.texture2.texture.wrapS = uniforms.texture2.texture.wrapT = THREE.RepeatWrapping;


        //        var size = renderBlock.getPlanetSize('TYPE_A');

        material = new THREE.ShaderMaterial( {

            uniforms: uniforms,
            vertexShader: shaderBlock.sunVShader,
            fragmentShader: shaderBlock.sunFShader

        } );
        
        material.color=0x222222;
        // sphere
        var sun = new THREE.Mesh(

            new THREE.SphereGeometry(
                size,
                renderBlock.config.lod*2,
                renderBlock.config.lod),

            shaderBlock.material
            );


        for(var i = 6; i < 20; i++) {
            planetUniforms = THREE.UniformsUtils.clone(uniforms);
            material = new THREE.ShaderMaterial( {

                uniforms: planetUniforms,
                vertexShader: shaderBlock.sunVShader,
                fragmentShader: shaderBlock.sunFShader

            } );
           /* planet = new THREE.Mesh(new THREE.SphereGeometry(30*i*Math.sqrt(i) , 64, 32), new THREE.MeshLambertMaterial({
                color:0xffffff*Math.random()
            }));*/
            planet = new THREE.Mesh(new THREE.SphereGeometry((Math.random()*35 + 5)*i*Math.sqrt(i) , 64, 32), material);
            phi =  Math.PI*20 / (i-6) * Math.PI * 2 ;
            planet.position.y = i*Math.log(i) * 1000 * Math.cos(phi);
            planet.position.x = i*Math.log(i) * 1000 * Math.sin(phi);
            renderBlock.sun.add(planet);
        }
    },



    createPlanetRing: function(planetRad, ringRad) {

        var cloud = new THREE.Geometry();
        var lambda = 1.0;
        for(var i = 0;  i < 500; i++) {
            var angle1 = Math.random() * 2 * Math.PI ;
            var angle2 = Math.random() * 2 * Math.PI ;
            var vertex = new THREE.Vector3();
            //            vertex.x = rad * Math.cos(angle1) * Math.sin(angle2);
            //            vertex.y = rad * Math.sin(angle1) * Math.sin(angle2);
            //            vertex.z = rad * Math.cos(angle2);
            //            rad =3*Math.random()+1;
            rad = ringRad*(THREE.Math.randFloatSpread(0.5))+planetRad*3;
            vertex.x = (rad)* (Math.cos(angle2));
            vertex.y = (rad)*(Math.sin(angle2));
            vertex.z = Math.pow((ringRad-rad)/rad, 3)*(Math.random()-0.5);
            cloud.vertices.push(vertex);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        var _size =8;
        //var aTexture = new THREE.Texture(renderBlock.generateSpriteTexture(_size));
        aTexture = renderBlock.smallNovaTexture;
        aTexture.needsUpdate = true;

        material = new THREE.ParticleBasicMaterial(
        {
            color: 0xaaaaaa
        //            size : _size,
        //            blending: THREE.AdditiveBlending,
        //            depthTest : false
        //             map: aTexture

        });
        particles = new THREE.ParticleSystem(cloud, material );
        particles.visible = true;
        particles.sortParticles = true;

        return particles;
    },

    fillGLModel: function () {
        //        console.log("filling the render model");

        
        renderBlock.materialDepth = new THREE.MeshDepthMaterial();
        renderBlock.mesh = new THREE.Object3D();

        mergeAtmos = true;
        mergeText = false;
        var sprite;

        var materialScene = new THREE.MeshBasicMaterial({
            color: renderBlock.config.sunColor,
            shading: THREE.SmoothShading
        });
        map = modelBlock.planetMap;
        lod = renderBlock.config.lod;
        renderBlock.planets = {};

        renderBlock.initCanvases(map);

        for (planetnum in map) {
            planet = map[planetnum];

            if (planet.id == 1) {

                planetMaterial = new THREE.MeshPhongMaterial({
                    color: 0xffffff,
                    emissive: 0x07070c,
                    shading: THREE.SmoothShading
                });
                planetMaterial.map = renderBlock.getPlanetTexture(planet.type);
                sun = new THREE.Mesh(new THREE.SphereGeometry(renderBlock.getPlanetSize(planet.type), lod, lod / 2), planetMaterial);

                sun.rotation.x = Math.PI/180*90;
                sun.rotation.y = Math.PI/2;
                //                sun.castShadow = true;
                //                sun.receiveShadow = true;
                renderBlock.fillPlanet(sun, planet);
                renderBlock.planets[planet.id] = sun;
                //                renderBlock.mesh = sun;
                //                renderBlock.mesh.matrixAutoUpdate = false;
                //                renderBlock.mesh.updateMatrix();
                sprite = renderBlock.textGenerator(sun.planetId);
                sprite.position.set(10, sun.geometry.boundingSphere.radius * 1.5, 10 );
                sprite.mergeWith3D = mergeText;
                //                                sprite.scale.set(2.1,2.1,2.1);
                sun.add(sprite);


                wireMaterial = new THREE.MeshBasicMaterial();
                wireMaterial.wireframe = true;

                var wire = new THREE.Mesh(new THREE.SphereGeometry(renderBlock.getPlanetSize(planet.type)*1.2, lod/2, lod / 4), wireMaterial);
                //                wire.rotation.x = Math.PI/180 * 90;
                wire.visible = false;
                sun.add(wire);

                //                sun.matrixAutoUpdate = false;
                //                sun.updateMatrix();
                atmos = new THREE.Sprite(
                {
                    map:renderBlock.atmos2Texture,

                    blending: THREE.AdditiveBlending,
                    
                    //                    affectedByDistance: true,
                    useScreenCoordinates: false
                });

                atmos.mergeWith3D = mergeAtmos;

                size = renderBlock.getPlanetSize(planet.type)/50;
                atmos.scale.set(size,size,size);

                sun.add(atmos);

                //                renderBlock.scene.add(sun);

                renderBlock.planets[planet.id] = sun;
                renderBlock.mesh.add(sun);
            } else {


                //                planetMaterial = renderBlock.getMaterial(planet.owner);
                planetMaterial = new THREE.MeshPhongMaterial({
                    color: 0xffffff,
                    emissive: 0x07070c,
                    shading: THREE.SmoothShading
                });

                planetMaterial.map = renderBlock.getPlanetTexture(planet.type);

                var planetMesh = new THREE.Mesh(new THREE.SphereGeometry(renderBlock.getPlanetSize(planet.type), lod, lod / 2),planetMaterial );
                renderBlock.fillPlanet(planetMesh, planet);
                planetMesh.rotation.x = Math.PI/180 * 90
                //                planetMesh.castShadow = true;
                //                planetMesh.receiveShadow = true;
                planetMesh.rotation.y = Math.PI/2;
                planetMesh.position.x = planet.xCoord * 1.5 ;
                planetMesh.position.y = planet.yCoord * 1.5 ;
                planetMesh.position.z = 0;

                renderBlock.planets[planet.id] = planetMesh;
                sprite = renderBlock.textGenerator(planetMesh.planetId);
                sprite.position.set(10, planetMesh.geometry.boundingSphere.radius * 1.2, 10);
                sprite.mergeWith3D = mergeText;
                planetMesh.add(sprite);
                //think about frame buffer

                wireMaterial = new THREE.MeshBasicMaterial();
                wireMaterial.wireframe = true;

                var wire = new THREE.Mesh(new THREE.SphereGeometry(renderBlock.getPlanetSize(planet.type)*1.2, lod/2, lod / 4), wireMaterial);
                //                wire.rotation.x = Math.PI/180 * 90;
                wire.visible = false;
                planetMesh.add(wire);

                //                planetMesh.matrixAutoUpdate = false;
                //                planetMesh.updateMatrix();
                renderBlock.planets[planet.id] = planetMesh;

                geometry = new THREE.SphereGeometry(renderBlock.getPlanetSize(planet.type)*1.2, lod, lod / 2)
                // geometry.computeVertexNormals();

                atmos = new THREE.Sprite(
                {
                    map:renderBlock.atmos2Texture,

                    blending: THREE.AdditiveBlending,
                    //                    affectedByDistance: true,
                    useScreenCoordinates: false
                });
                size = renderBlock.getPlanetSize(planet.type)/50;
                atmos.scale.set(size,size,size);
                atmos.mergeWith3D = mergeAtmos;
                planetMesh.add(atmos);

                ring = renderBlock.createPlanetRing(renderBlock.getPlanetSize(planet.type)*2.05, renderBlock.getPlanetSize(planet.type)*2.1);
                ring.rotation.x = Math.PI/180 * 90;
                //                                planetMesh.add(ring);
                //                renderBlock.scene.add(planetMesh);
                renderBlock.mesh.add(planetMesh);
            }
        }
        renderBlock.mesh.rotation.y = Math.PI / 6;
        //        renderBlock.mesh.rotation.z = -Math.PI / 6;
        //        renderBlock.mesh.updateMatrix();
        renderBlock.scene.add(renderBlock.mesh);
    },


    initPostprocessing: function (postprocessing, SCREEN_HEIGHT, SCREEN_WIDTH, sunIntensity, bgColor, sunColor,godRayIntensity) {
        postprocessing.scene = new THREE.Scene();

        postprocessing.camera = new THREE.OrthographicCamera(SCREEN_WIDTH / -2, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SCREEN_HEIGHT / -2, -21000, 21000);
        postprocessing.camera.position.z = 100;

        postprocessing.scene.add(postprocessing.camera);

        var pars = {
            minFilter: THREE.LinearFilter,
            magFilter: THREE.LinearFilter,
            format: THREE.RGBFormat
        };


        postprocessing.rtTextureColors = new THREE.WebGLRenderTarget(SCREEN_WIDTH, SCREEN_HEIGHT, pars);

        postprocessing.rtTextureDepth = new THREE.WebGLRenderTarget(SCREEN_WIDTH, SCREEN_HEIGHT, pars);

        // Aggressive downsize god-ray ping-pong render targets to minimize cost
        var w = SCREEN_WIDTH / 4.0;
        var h = SCREEN_HEIGHT / 4.0;
        postprocessing.rtTextureGodRays1 = new THREE.WebGLRenderTarget(w, h, pars);
        postprocessing.rtTextureGodRays2 = new THREE.WebGLRenderTarget(w, h, pars);


        var godraysGenShader = THREE.ShaderGodRays[ "godrays_generate" ];
        postprocessing.godrayGenUniforms = THREE.UniformsUtils.clone( godraysGenShader.uniforms );
        postprocessing.materialGodraysGenerate = new THREE.ShaderMaterial( {

            uniforms: postprocessing.godrayGenUniforms,
            vertexShader: godraysGenShader.vertexShader,
            fragmentShader: godraysGenShader.fragmentShader

        } );

        var godraysCombineShader = THREE.ShaderGodRays[ "godrays_combine" ];
        postprocessing.godrayCombineUniforms = THREE.UniformsUtils.clone( godraysCombineShader.uniforms );
        postprocessing.materialGodraysCombine = new THREE.ShaderMaterial( {

            uniforms: postprocessing.godrayCombineUniforms,
            vertexShader: godraysCombineShader.vertexShader,
            fragmentShader: godraysCombineShader.fragmentShader

        } );

        //				var godraysFakeSunShader = THREE.ShaderGodRays[ "godrays_fake_sun" ];
        //				postprocessing.godraysFakeSunUniforms = THREE.UniformsUtils.clone( godraysFakeSunShader.uniforms );
        //				postprocessing.materialGodraysFakeSun = new THREE.ShaderMaterial( {
        //
        //					uniforms: postprocessing.godraysFakeSunUniforms,
        //					vertexShader: godraysFakeSunShader.vertexShader,
        //					fragmentShader: godraysFakeSunShader.fragmentShader
        //
        //				} );

        //        postprocessing.godraysFakeSunUniforms.bgColor.value.setHex( bgColor); //TODO
        //        postprocessing.godraysFakeSunUniforms.sunColor.value.setHex(1.0 - sunColor); //TODO
        postprocessing.godrayCombineUniforms.fGodRayIntensity.value = godRayIntensity;

        postprocessing.quad = new THREE.Mesh(new THREE.PlaneGeometry(SCREEN_WIDTH, SCREEN_HEIGHT), postprocessing.materialGodraysGenerate);
        postprocessing.quad.position.z = -9900;
        //          postprocessing.quad.rotation.x = Math.PI / 2;
        postprocessing.scene.add(postprocessing.quad);



    },


    postprocessingRender : function( mesh, projector, postprocessing, SCREEN_HEIGHT, SCREEN_WIDTH, renderer, scene, camera) {
        //
        // Find the screenspace position of the sun
        materialDepth=renderBlock.materialDepth;
        materialDepth = new THREE.MeshDepthMaterial();
        screenSpacePosition = renderBlock.screenSpacePosition;
        screenSpacePosition.copy(mesh.position);
        projector.projectVector(screenSpacePosition, camera);

        screenSpacePosition.x = (screenSpacePosition.x + 1) / 2;
        screenSpacePosition.y = (screenSpacePosition.y + 1) / 2;

        // Give it to the god-ray and sun shaders
        postprocessing.godrayGenUniforms["vSunPositionScreenSpace"].value.x = screenSpacePosition.x;
        postprocessing.godrayGenUniforms["vSunPositionScreenSpace"].value.y = screenSpacePosition.y;

        //        postprocessing.godraysFakeSunUniforms["vSunPositionScreenSpace"].value.x = screenSpacePosition.x;
        //        postprocessing.godraysFakeSunUniforms["vSunPositionScreenSpace"].value.y = screenSpacePosition.y;

        // -- Draw sky and sun --

        // Clear colors and depths, will clear to sky color
        renderer.clearTarget(postprocessing.rtTextureColors, true, true, false);

        // Sun render. Runs a shader that gives a brightness based on the screen
        // space distance to the sun. Not very efficient, so i make a scissor
        // rectangle around the suns position to avoid rendering surrounding pixels.
        //        var sunsqH = 0.74 * SCREEN_HEIGHT; // 0.74 depends on extent of sun from shader
        //        var sunsqW = 0.74 * SCREEN_HEIGHT; // both depend on height because sun is aspect-corrected
        screenSpacePosition.x *= SCREEN_WIDTH;
        screenSpacePosition.y *= SCREEN_HEIGHT;

        //     renderer.setScissor(screenSpacePosition.x - sunsqW / 2, screenSpacePosition.y - sunsqH / 2, sunsqW, sunsqH);
        //    renderer.enableScissorTest(true);
        //        postprocessing.godraysFakeSunUniforms["fAspect"].value = SCREEN_WIDTH / SCREEN_HEIGHT;

        //        postprocessing.scene.overrideMaterial = postprocessing.materialGodraysFakeSun;
        //        renderer.render(postprocessing.scene, postprocessing.camera, postprocessing.rtTextureColors);
        renderer.enableScissorTest(false);

        // -- Draw scene objects --

        // Colors
        scene.overrideMaterial = null;
        renderer.render(scene, camera, postprocessing.rtTextureColors);

        // Depth
        scene.overrideMaterial = materialDepth;
        renderer.render(scene, camera, postprocessing.rtTextureDepth, true);

        // -- Render god-rays --

        // Maximum length of god-rays (in texture space [0,1]X[0,1])
        var filterLen = 1.0;

        // Samples taken by filter

        var TAPS_PER_PASS = 6.0;

        // Pass order could equivalently be 3,2,1 (instead of 1,2,3), which
        // would start with a small filter support and grow to large. however
        // the large-to-small order produces less objectionable aliasing artifacts that
        // appear as a glimmer along the length of the beams

        // pass 1 - render into first ping-pong target

        var pass = 1.0;
        var stepLen = filterLen * Math.pow( TAPS_PER_PASS, -pass );

        postprocessing.godrayGenUniforms[ "fStepSize" ].value = stepLen;
        postprocessing.godrayGenUniforms[ "tInput" ].texture = postprocessing.rtTextureDepth;

        postprocessing.scene.overrideMaterial = postprocessing.materialGodraysGenerate;

        renderer.render( postprocessing.scene, postprocessing.camera, postprocessing.rtTextureGodRays2 );

        // pass 2 - render into second ping-pong target

        pass = 2.0;
        stepLen = filterLen * Math.pow( TAPS_PER_PASS, -pass );

        postprocessing.godrayGenUniforms[ "fStepSize" ].value = stepLen;
        postprocessing.godrayGenUniforms[ "tInput" ].texture = postprocessing.rtTextureGodRays2;

        renderer.render( postprocessing.scene, postprocessing.camera, postprocessing.rtTextureGodRays1  );

        // pass 3 - 1st RT

        pass = 3.0;
        stepLen = filterLen * Math.pow( TAPS_PER_PASS, -pass );

        postprocessing.godrayGenUniforms[ "fStepSize" ].value = stepLen;
        postprocessing.godrayGenUniforms[ "tInput" ].texture = postprocessing.rtTextureGodRays1;

        renderer.render( postprocessing.scene, postprocessing.camera , postprocessing.rtTextureGodRays2  );

        // final pass - composite god-rays onto colors

        postprocessing.godrayCombineUniforms["tColors"].texture = postprocessing.rtTextureColors;
        postprocessing.godrayCombineUniforms["tGodRays"].texture = postprocessing.rtTextureGodRays2;

        postprocessing.scene.overrideMaterial = postprocessing.materialGodraysCombine;

        renderer.render( postprocessing.scene, postprocessing.camera );
        postprocessing.scene.overrideMaterial = null;
    },


    generateSpriteTexture : function (size) {

        var c = document.createElement( 'canvas' );
        c.width =size;
        c.height = size;
        var ctx=c.getContext("2d");

        //
        var gradient = ctx.createRadialGradient(
            c.width / 2,
            c.height / 2,
            c.height / 8,
            c.width / 2,
            c.height / 2,
            c.width /2 );
        gradient.addColorStop( 0, 'rgba(255,255,255,1)' );
        gradient.addColorStop( 0.3, 'rgba(157,164,247,1)' );
        gradient.addColorStop( 0.8, 'rgba(31,34,66,1)' );
        gradient.addColorStop( 1, 'rgba(0,0,0,1)' );

        ctx.fillStyle=gradient;
        //        ctx.fillStyle = 'rgb(255,255,255)';
        ctx.fillRect(0,0,c.width,c.height);

        return c;

    },

    wantedCenter: new THREE.Vector3(),

    render: function () {
        //
        //        if(renderBlock.wantedCenter != renderBlock.controls.center)  {
        //            renderBlock.controls.center.sub(renderBlock.wantedCenter.normalize());
        //        }
        //console.log(shaderBlock.sunUniforms.time.value);
        //                renderBlock.underMouseHighlight();
	    renderBlock.programStats();
        if(renderBlock.strangeCamera && 0){
            renderBlock.controls.currentCenter = 1;
            renderBlock.controls.center = renderBlock.controls.centers[renderBlock.controls.currentCenter];
            theta = renderBlock.theta;
            theta += 0.3;
            var radius = 3600 * Math.cos(theta/100) ;
            radius = 2000;
            renderBlock.camera.rotation.z = Math.cos( theta * Math.PI / 360 );
            renderBlock.camera.rotation.y = Math.sin( theta * Math.PI / 360 );
            renderBlock.camera.position.x = radius * Math.cos( theta * Math.PI / 360 );
            renderBlock.camera.position.y = radius * Math.cos( theta * Math.PI / 360 );
            renderBlock.camera.position.z = radius * Math.sin( theta * Math.PI / 360 );
            renderBlock.theta = theta;
        }
        var delta = renderBlock.clock.getDelta();
        renderBlock.controls.update(delta);
        renderBlock.r += delta/30;
        renderBlock.shiftArrows(delta);
        if(renderType=='WebGL'){
            var delta = renderBlock.clock.getDelta();
            //            renderBlock.controls.update(delta);
            renderBlock.r += delta/30;
            //            shaderBlock.sunUniforms.time.value += delta;
            shaderBlock.sunUniforms.time.value += 0.03;
            //            renderBlock.sun.rotation.z -= 0.0005;
//            renderBlock.sun.rotation.x -= 0.005;
            renderBlock.sun.rotation.z -= 0.0005;
            for(var i = 1; i < renderBlock.sun.children.length; i++) {
                renderBlock.sun.children[i].material.uniforms.time.value += 0.03;
            }
            //            renderBlock.mesh.rotation.z += 0.0002;
            //            renderBlock.scene.updateMatrix();

            for(planet in renderBlock.planets) {
                renderBlock.planets[planet].rotation.y -= 0.01 * (1 / renderBlock.getPlanetSize( renderBlock.planets[planet].type));
            }

        //            renderBlock.sun.position.y += 0.5*10000;
        //            console.log(renderBlock.sun);
        //            console.log('1');
        //            renderBlock.shiftArrows(delta*10);
        //            if(renderBlock.r){}


        }

        if(renderType=='Canvas') {
        //console.log(renderBlock.mouse);
        //				renderBlock.camera.position.x += ( renderBlock.mouse.x - renderBlock.camera.position.x ) * .05;
        //				renderBlock.camera.position.y += ( - renderBlock.mouse.y - renderBlock.camera.position.y ) * .05;
        //				renderBlock.camera.lookAt( renderBlock.scene.position );
        }
        

        renderBlock.renderer.clear();
        if (renderBlock.postprocessing.enabled && renderType=='WebGL' ) {


            if(renderBlock.composerEnabled) {
                renderBlock.renderer.clear();

                //renderBlock.renderer.render( renderBlock.scene, renderBlock.camera, renderBlock.composer.renderTarget2, true );

                //renderer.shadowMapEnabled = false;
                //depthPassPlugin.enabled = false;
                renderBlock.composer.render( 0.1 );
            //                renderBlock.s.render(0.1);


            // do postprocessing
            //renderBlock.postprocessingRender(renderBlock.mesh, renderBlock.projector, renderBlock.postprocessing, renderBlock.SCREEN_HEIGHT, renderBlock.SCREEN_WIDTH, renderBlock.renderer, renderBlock.scene,  renderBlock.camera);
            //
            //              renderBlock.composer.render();
            //                   renderBlock.postprocessingRender(renderBlock.mesh, renderBlock.projector, renderBlock.postprocessing, renderBlock.SCREEN_HEIGHT, renderBlock.SCREEN_WIDTH, renderBlock.renderer, renderBlock.scene,  renderBlock.camera);
            } else {
                renderBlock.renderer.clear();
                renderBlock.renderer.render(renderBlock.scene, renderBlock.camera);
            //    renderBlock.composer.render();
            //                renderBlock.postprocessingRender(renderBlock.mesh, renderBlock.projector, renderBlock.postprocessing, renderBlock.SCREEN_HEIGHT, renderBlock.SCREEN_WIDTH, renderBlock.renderer, renderBlock.scene,  renderBlock.camera);
            //                renderBlock.renderer.clear();
            //                renderBlock.renderer.render(renderBlock.scene, renderBlock.camera);
            }
        //



        } else {

            renderBlock.renderer.clear();
            renderBlock.renderer.render(renderBlock.scene, renderBlock.camera);

        }
    },


    onWindowResize: function (event) {
        renderBlock.camera.updateProjectionMatrix();
        if (!renderBlock.GUIOptions.fullScreen) {

            renderBlock.SCREEN_WIDTH  = $('#container').outerWidth();
            renderBlock.SCREEN_HEIGHT = $('#container').outerHeight();
//            renderBlock.stats.position.x = -renderBlock.SCREEN_WIDTH*renderBlock.scene.scale.x;
//            renderBlock.stats.position.y = renderBlock.SCREEN_HEIGHT*renderBlock.scene.scale.y;


            MARGIN = 0;
            //            RESIZE_FACTOR = renderBlock.SCREEN_WIDTH / OPTIMIZED_WIDTH;

            // Resize Stage
            renderBlock.renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);

            renderBlock.renderer.domElement.style.top = MARGIN + 'px';

            //     WIDE SCREEN  */

            renderBlock.camera.aspect = renderBlock.SCREEN_WIDTH / renderBlock.SCREEN_HEIGHT;
            renderBlock.camera.updateProjectionMatrix();
            if(renderType=='WebGL')renderBlock.initComposer();

        } else {
            renderBlock.SCREEN_WIDTH = window.innerWidth;
            renderBlock.SCREEN_HEIGHT = window.innerHeight;
            renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);

            //*WIDE SCREEN
            //            TARGET_RATIO = 3; // Ultra Wide screen format
            //            var OPTIMIZED_WIDTH = 1440;

            //            var WINDOW_RATIO = renderBlock.SCREEN_WIDTH / renderBlock.SCREEN_HEIGHT;

            //            var TARGET_HEIGHT = Math.round(renderBlock.SCREEN_WIDTH / TARGET_RATIO);
            //            var DIFF = renderBlock.SCREEN_HEIGHT - TARGET_HEIGHT;

            //            renderBlock.SCREEN_HEIGHT = TARGET_HEIGHT;

            //            var MARGIN = DIFF / 2;
            //            if (MARGIN < 0) MARGIN = 0;
            MARGIN = 0;
            //            RESIZE_FACTOR = renderBlock.SCREEN_WIDTH / OPTIMIZED_WIDTH;

            // Resize Stage
            renderBlock.renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);

            renderBlock.renderer.domElement.style.top = MARGIN + 'px';

            //     WIDE SCREEN  */

            renderBlock.camera.aspect = renderBlock.SCREEN_WIDTH / renderBlock.SCREEN_HEIGHT;
            renderBlock.camera.updateProjectionMatrix();
            if(renderType=='WebGL')renderBlock.initComposer();
        }
	    renderBlock.placeStats();
    },

	placeStats: function(){
		var pos = $(renderer.domElement).offset();
		if(renderBlock.GUIOptions.fullScreen) pos.left = pos.left + 50;
		$(renderBlock.stats).offset(pos);
	},

    getMaterial: function (owner) {

        if (renderBlock.materials[owner]) {
        } else {
            var _color = new THREE.Color();
            if(owner == 'neutral') {
                if(renderType == 'WebGL') {
                    _color.setHex(0x000000);
                } else {
                    _color.setHex(0xffffff);
                }
                _color.setHex(0x888888);
            } else {
                var result = 0;
                for(var i = 0; i < modelBlock.players.length; i++) {
                    if(modelBlock.players[i].name === owner) {
                        result = i;
                    }
                }
                var step = 1/(modelBlock.players.length+1) ;
                
                temp = result * step - 2*(result % 2) * step  +  1.5 * step;
                if(temp < 0 || temp > 1) console.log(result + ' ' + step + ' ' + temp);
                if(renderType == 'WebGL') {
                    _color.setHSV(temp, 0.9, 1.0);
                } else {
                    _color.setHSV(temp, 0.5 + (result % 2)*0.5, 1.0 - (result % 2)*0.2 );
	                _color.setHSV(temp, 0.5 + (result % 2)*0.5, 1.0 - (result % 2)*0.2 );
                }
            }
            renderBlock.materials[owner] = new THREE.MeshLambertMaterial({
                color: _color
            //                shading: THREE.SmoothShading
            });
        }

        return renderBlock.materials[owner];
    },



    updateView: function () {//TODO check all this
        // loopBlock.updateModels();
        //        renderBlock.changeMaterials();


        renderBlock.updatePlanets();
        //        renderBlock.updateUnits();
        
        renderBlock.updateText();
        renderBlock.updateRings();
        res = renderBlock.updateActionMap();
        return res;
    },

    updateRings: function() {
        for (id in modelBlock.planetMap) {
            planet = modelBlock.planetMap[id];
            if( renderType=='WebGL') {
                for( i in renderBlock.rings[planet.id])

                    renderBlock.rings[planet.id][i].material.color=
                    renderBlock.getMaterial(planet.owner).color;
            }
        }
    },

    generateCountTexture : function (count, color, size, _canvas) {
        var canvas = _canvas == undefined ? document.createElement( 'canvas' ) : _canvas;
        canvas.width = size;
        canvas.height = size;
        var context = canvas.getContext('2d');
        //        context.font = "bold 22pt sans-serif";
        context.fillStyle =   color.getContextStyle();
        context.textAlign = "center";

        //        context.fillText(renderBlock.planets[planetId].unitsCount, canvas.height / 3,  2*canvas.height / 6);

        context.font = "24pt " + renderBlock.fontFamily;
        //        context.fillText(owner , canvas.height / 3,  2.5*canvas.height / 6);
        context.fillText(count + ' ' , canvas.height / 2,   canvas.height / 2);

        return canvas;
    },

    getDistance : function(a, b) {
        return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)* (a.y-b.y) + (a.z-b.z)*(a.z-b.z) );
    },


    boomIn:function(arrow,delta, pos) {
        max = arrow.path.length-1;
        booms = arrow.boomsIn;
        planetSize = renderType == 'WebGL' ? renderBlock.planets[arrow.toId].boundRadius/20 : 1;
        scale = renderBlock.planets[arrow.toId].scale.x;
        
        for(var e = 0; e < booms.length; e++) {
            if(renderType=='Canvas'){
                booms[e].scale.x = booms[e].scale.y +=  0.3*(e + scale/30) * delta * (planetSize) ;
            } else {
                booms[e].scale.x += 0.001*(e + scale/30) * delta * (Math.random()/planetSize);
                booms[e].scale.y += 0.001*(e + scale/30) * delta * (Math.random()/planetSize) ;
            }
            if(booms[e].scale.x >scale * planetSize) {
                booms[e].scale.x = booms[e].scale.y =  25;
                booms[e].visible = false;
            }
        }
    },

    
    boomOut:function(arrow,delta, pos) {
        max = arrow.path.length-1;
        booms = arrow.boomsOut;
        scale = renderBlock.planets[arrow.toId].scale.x;
        for(var e = 0; e < booms.length; e++) {
            booms[e].scale.x = booms[e].scale.y -=  0.3*(e+20/scale)*delta;
            if(booms[e].scale.x < 1) {
                booms[e].scale.x = booms[e].scale.y =  scale;
                booms[e].visible = false;

            }
        }

    },

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    shiftArrows : function(delta) {
        if(renderBlock.started) {
            delta *= renderType=='Canvas' ? 30*(2000/renderBlock.turnSpeed) :  20*(2000/renderBlock.turnSpeed);
            map = renderBlock.actionMap;
            for(  arw in map) {

                arrow = map[arw];
                ships = map[arw].children;

                for(var i = 0; i < ships.length-1; i++ ) {
                    curPos = arrow.path[ships[i].iter];
                    var adder = i>ships.length/2 ? ships.length - i : i;
                    adder = Math.pow(i*2, 0.5);
                    speed = Math.floor(arrow.speed * delta + adder*adder / 2);

                    if(ships[i].iter + speed >= arrow.path.length) {
                        iter = arrow.path.length-1;
                        newPos = arrow.path[iter];
                        ships[i].iter = iter;
                        ships[i].visible = false;
                    } else {
                        newPos = arrow.path[ships[i].iter+speed];
                    }
                    if(renderType=='WebGL') {
                        ships[i].position = newPos;
                    } else {
                        ships[i].position.x = newPos.x;
                        ships[i].position.y = newPos.y;
                    }
                    ships[i].iter += speed;
                    if(ships[i].iter > 3*(arrow.path.length-1)/4)   renderBlock.boomIn(arrow,delta,0);
                //                    if(ships[i].iter < (arrow.path.length-1)/4)renderBlock.boomOut(arrow,delta,0);
                }
            }
        }
    },
    newTurn: function() {
        //        console.log('clearing the ships');
        map = renderBlock.actionMap;
        for(  arw in map) {

            arrow = map[arw];
            ships = map[arw].children;
            for(var i = 0; i < ships.length-1; i++ ) {
                newPos = arrow.path[0];
                if(renderType=='WebGL') {
                    ships[i].position = newPos;
                } else {
                    ships[i].position.x = newPos.x;
                    ships[i].position.y = newPos.y;
                }
                ships[i].iter = 0;
            }
            //            booms = ships[ships.length-1].children;
            //            for(var e = 0; e < booms.length; e++) {
            //                //                booms[e].scale.x = booms[e].scale.y =  1;
            //                booms[e].visible = true;
            //            }
            booms = arrow.boomsIn;
            for(var e = 0; e < booms.length; e++) {
                booms[e].scale.x = booms[e].scale.y =  0.1;
                booms[e].visible = false;
            }
            booms = arrow.boomsOut;
            for(var e = 0; e < booms.length; e++) {
                booms[e].scale.x = booms[e].scale.y =  0.1;
                booms[e].visible = false;
            }
        }
    },

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    updateActionMap: function() {//TODO - still have a need in unit owner if I want to refresh the page and have no history of turns, but already have neutral planet
        res = true;
        correctNeutrals = true;
        renderBlock.newTurn();

        for(arrow in renderBlock.actionMap) {
            //            renderBlock.actionMap[arrow].countText.visible = false;
            for(var k = 0; k < renderBlock.actionMap[arrow].children.length; k++ ) {
                renderBlock.actionMap[arrow].children[k].visible = false;
                if(renderType =='WebGL' && renderBlock.isMesh && k != renderBlock.actionMap[arrow].children.length-1){
                    renderBlock.actionMap[arrow].children[k].children[0].visible = false;
                }

            }
        }
        if(!renderBlock.GUIOptions.hideUnits) {
            //            console.log('-1) turn ' + modelBlock.turn);
            map =  modelBlock.actionMap;
            jPos = modelBlock.jsonResponse.length-1;
            pm = modelBlock.jsonResponse;
            prevMap = jPos == 0 ? pm[jPos].planetMap : pm[jPos].playersActions.planetOwners;

            for(var i = 0; i < map.length; i++)   {
                id = map[i].from + ' ' + map[i].to;

                owner = renderBlock.planets[map[i].from].owner;
                temp =  renderBlock.planets[map[i].from].owner;
                //                if(renderBlock.planets[map[i].from].owner != modelBlock.planetMap[].owner)
                emptyPlanet = true;
                //                if(owner == 'neutral')
                for(r = 0; r < modelBlock.players.length; r++) {
                    if(owner == modelBlock.players[r].name) {
                        emptyPlanet = false;
                    }
                }
                if(correctNeutrals)
                    if(emptyPlanet){
                        var place = -1;
                        for(var t = 0; t < prevMap.length; t++) {
                            if(prevMap[t].id == map[i].from ){
                                place = t;
                            }
                        }
                        owner = prevMap[place].owner;
                    }
                
                color = renderBlock.getMaterial(owner).color;
                arrow = renderBlock.actionMap[id];
                sprites = renderBlock.actionMap[id].children;
                for(var j = 0; j < sprites.length-1; j++) {
                    sprites[j].visible = true;

                    if(renderBlock.isMesh && renderType == 'WebGL'){
                        sprites[j].children[0].visible = true;
                        sprites[j].material.color = color;
                        sprites[j].children[0].color = color;

                        sprites[j].material.map = renderBlock.unitTexture;
                    } else if(renderType == 'Canvas') {
                        sprites[j].material.color = color;
                    } else {
                        sprites[j].color = color;
                    }
                }
                if(!renderBlock.GUIOptions.hideText) {
                    if(map[i].unitCount> 0){
                        sprites[sprites.length-1].visible = true;
                        if(renderType=='WebGL') {
                            renderBlock.generateCountTexture(map[i].unitCount, color,256,renderBlock.actionMap[id].canvas);
                            sprites[sprites.length-1].map.needsUpdate = true;
                        } else {
                            var position =  sprites[sprites.length-1].position;
                            renderBlock.generateCountTexture(map[i].unitCount, color,256,renderBlock.actionMap[id].canvas);
                            sprites[sprites.length-1].material.map.needsUpdate = true;

                        }
                    }
                }
                booms = arrow.boomsIn;
                for(var e = 0; e < booms.length; e++) {
                    if(renderType=='Canvas') {
                        booms[e].material.color = color;
                    }else {
                        booms[e].color = color;
                    }
                    booms[e].visible = true;
                }
                booms = arrow.boomsOut;
                for(var e = 0; e < booms.length; e++) {
                    booms[e].material.color = color;
                    booms[e].visible = true;
                }

            }
        }
        renderBlock.started = true;
        return res;
    },



    updatePlanets: function () {
        for (id in modelBlock.planetMap) {
            planet = modelBlock.planetMap[id];
            renderBlock.planets[planet.id].owner = planet.owner;
            renderBlock.planets[planet.id].unitsCount = planet.unitsCount;
            //            if( modelBlock.planetMap[id].id != 1)
            if( renderType=='WebGL') {
                wireColor = renderBlock.getMaterial(planet.owner).color;
                renderBlock.planets[planet.id].children[1].material.color =
                //                renderBlock.rings[planet.id].material.color=
                renderBlock.getMaterial(planet.owner).color;

            } else {

                wireColor = renderBlock.getMaterial(planet.owner).color;
                renderBlock.planets[planet.id].material.color = renderBlock.getMaterial(planet.owner).color;

            }

        }
    },


    initCanvases: function (planetMap) {
        renderBlock.canvases = new Object();
        for (id in planetMap) {
            renderBlock.canvases[planetMap[id].id] = document.createElement('canvas');
        }
    },

    programBoom : function( context ) {
        
        context.lineWidth = 0.035;
        context.beginPath();
        context.arc( 0, 0, 1, 0, Math.PI * 2 , true );
        context.closePath();
        context.stroke();

    },

    addArrows : function() {
        map = modelBlock.planetMap;
        explosions = new THREE.Object3D();
        renderBlock.mesh.add(explosions);
        renderBlock.mesh.exp = explosions;
        for (planetnum in map) {
            planet = map[planetnum];
            for (var i = 0; i < planet.neighbors.length; i++) {
                id = planet.id + ' ' + planet.neighbors[i];
                reverseId = planet.neighbors[i] + ' ' + planet.id;
                flag = -1.0;
                if(renderBlock.actionMap[reverseId]){
                    flag = 1.0;
                }
                renderBlock.actionMap[id] = renderBlock.arrowBuilder(planet.id, planet.neighbors[i], flag);
                //                renderBlock.actionMap[id].visible = false;
                temp =renderBlock.actionMap[planet.id + ' ' + planet.neighbors[i]];
                //                renderBlock.scene.add(temp);
                renderBlock.mesh.add(temp);
            //               console.log(temp);
            //               for(var j = 0; j < temp.children.length; j++) {
            //                   temp.children[j].visible = false;
            //               }
            //                renderBlock.mesh.add(renderBlock.actionMap[planet.id + ' ' + planet.neighbors[i]]);
            }
        }
    //        if(renderType == 'WebGL'){
    //            renderBlock.initComposer(renderBlock.enableFXAA);
    //        }
    //        renderBlock.updateText();

    },








    drawTrace: function() {

    },


    arrowBuilder: function (from, to, up) { //add cases for boundary values
        moons = renderBlock.planets;
        var fromVect = moons[from].position; //death on 5 1
        var toVect = moons[to].position;


        var m = toVect.y - fromVect.y;
        var l = toVect.x - fromVect.x;
        var k = toVect.z - fromVect.z;
        
        length = Math.sqrt(Math.pow(l, 2) + Math.pow(m, 2) + Math.pow(k, 2));
        nullPoint = new THREE.Vector3().sub(fromVect, toVect);
        phi = Math.atan2(nullPoint.y, nullPoint.x);

        
        var seedsCount = renderType == 'WebGL' ? Math.round(length / 20) : Math.round(length / 80);
        var arrow = new THREE.Object3D();
        arrow.from = fromVect;
        arrow.to = toVect;
        arrow.fromId = from;
        arrow.toId = to;
        arrow.countCoef = 0;
        signum = up >= 0 ? 1 : -1;

        diff =  up*0.8;
        rad = moons[from].boundRadius ;

        max = renderType=='WebGL' ? Math.max(moons[from].geometry.boundingSphere.radius, moons[to].geometry.boundingSphere.radius)
        :Math.max( renderBlock.getPlanetSize(moons[from].planetType), renderBlock.getPlanetSize(moons[to].planetType));

        mid = new THREE.Vector3();
        //        mid.x = diff * (toVect.x+fromVect.x);
        //        mid.y = diff * (toVect.y+fromVect.y);
        if(renderType == 'WebGL') {
            mid.x =((arrow.from.x + arrow.to.x))/2 + (length/16) * Math.cos(phi+4*Math.PI/3) * signum * up;
            mid.y =((arrow.from.y + arrow.to.y))/2 + (length/16) * Math.sin(phi+4*Math.PI/3) * signum * up;
            mid.z = Math.log(max)*max *up/2;
        }
        else {
            mid.x =((arrow.from.x + arrow.to.x))/2;
            mid.y =((arrow.from.y + arrow.to.y))/2;

        }


        back = new THREE.Vector3();
        back.x = (rad) * Math.cos(phi) + fromVect.x ;
        back.y = (rad)* Math.sin(phi) + fromVect.y ;
        back.z = Math.log(max)*max *up/2;


        

        var spline = new THREE.SplineCurve3([
            arrow.from,
            mid,
            arrow.to
            ]);
        start = new THREE.Vector2(arrow.from.x, arrow.from.y);
        mid2 = new THREE.Vector2(mid.x, mid.y);
        end = new THREE.Vector2(arrow.to.x, arrow.to.y);
      /*  var spline2D = new THREE.SplineCurve3([
            arrow.from,
            mid,
            arrow.to]); */
        var spline2D = new THREE.LineCurve(arrow.from, arrow.to);
        ///////////////////////////////
        var vertexCountDesired = Math.floor( length ) * 2;

        var points = spline.getPoints( vertexCountDesired );
        var points2 = spline2D.getPoints( Math.floor(vertexCountDesired/20) );
        //        console.log(points2);
        geoLine = new THREE.Geometry();
        geoLine2 = new THREE.Geometry();
        for (var t = 0; t < points.length; t++) {
            geoLine.vertices.push(points[t]);
        }
        for (var t = 0; t < points2.length; t++) {
            geoLine2.vertices.push(points2[t]);
        }
        line = new THREE.Line(geoLine, new THREE.LineBasicMaterial({
            color: 0xaaeeff                    //<- for GL
        }));

	    /*
	     this.color = new THREE.Color( 0xffffff );

	     this.linewidth = 1;
	     this.linecap = 'round';
	     this.linejoin = 'round';

	     this.vertexColors = false;

	     this.fog = true;

	     this.setValues( parameters );
	    * */


        var texture;
        texture = renderBlock.whiteTexture;
        texture.needsUpdate = true;

        for (var i = 0; i < seedsCount; i += 1.0) {
            if(!renderBlock.isMesh || renderType == 'Canvas') {
                var temp;
                if(renderType=='WebGL') {
                    temp = new THREE.Sprite(   {
                        map:renderBlock.smallNovaTexture,
                        blending: THREE.AdditiveBlending,
                        useScreenCoordinates: false
                    });
                } else {
                    var material = new THREE.ParticleCanvasMaterial({
                        color: 0x223355,
                        program: renderBlock.programFill
                    });
                    temp = new THREE.Particle(material);
                    temp.scale.x = temp.scale.y = 3.0;
                    temp.visible = false;
                }
            } else {
                geo = new THREE.IcosahedronGeometry(1.2, 1.2);
                temp = new THREE.Mesh(geo, new THREE.MeshBasicMaterial({
                    wireframe: true,
                }));
                sunAtmos = new THREE.Sprite(
                {
                    map:renderBlock.novaTexture,
                    blending: THREE.AdditiveBlending,
                    useScreenCoordinates: false
                });
                sunAtmos.scale.set(0.05,0.05,0.05);
                sunAtmos.visible = false;

                temp.add(sunAtmos);
            //                temp.add();
            }
            pos = Math.floor(i*(points.length/seedsCount));
            pos = 0;
            if(pos>points.length-1) {
                pos = points.length - 2;
            }
            if(renderType =='WebGL'){
                temp.position = points[pos];
                temp.iter = pos;
            } else {
                temp.position.x = points[pos].x;
                temp.position.y = points[pos].y;
                temp.iter = pos;

            }
            if(!renderBlock.isMesh) {
                temp.visible = false;
            }
            arrow.add(temp);
        }
        
        arrow.path = points;
        arrow.speed = points.length/30;
	   	var geoLine2 = new THREE.Geometry();
	    geoLine2.vertices.push(arrow.from);
	    geoLine2.vertices.push(mid);
	    geoLine2.vertices.push(arrow.to);
	    line2 = new THREE.Line(geoLine2, new THREE.LineBasicMaterial({  //<-for canvas
//            color: 0x9966FF,
		    color: 0x9BD3FF,
		    linewidth:0.3,
		    vertexColors: true
	    }));


        if(renderBlock.showTrace ) {
            if (renderType == 'WebGL') {
                arrow.line = line;
                renderBlock.mesh.add(line);
            }else {
//	            if(up) {
                    arrow.line = line2;
                    renderBlock.mesh.add(line2);
//	            }
            }
        }


        //TEXT BLOCK
        canvas = renderBlock.generateCountTexture(0,  renderBlock.getMaterial(moons[from].owner).color, 64);
        texture = new THREE.Texture(canvas); //TODO check the arguments - have promlem with undefined
        arrow.canvas = canvas;
        arrow.texture = texture;
        arrow.texture.needsUpdate = true;
        if(renderType=='WebGL') {
            sprite = new THREE.Sprite({
                map: texture,

                useScreenCoordinates: false
            });
            sprite.mergeWith3D = false;
            sprite.position.set(fromVect.x +(seedsCount/4) * l / seedsCount,fromVect.y +(seedsCount/4) * m / seedsCount, 20)
            if(renderType=='WebGL' && from == 1) {
                sprite.position.set(fromVect.x +(seedsCount/2) * l / seedsCount,fromVect.y +(seedsCount/2) * m / seedsCount, 40)
            }
            sprite.scaleByViewport = false;

        } else {

            var geo = new THREE.PlaneGeometry(128, 128, 1, 1);
            var material = new THREE.MeshBasicMaterial({
                map: arrow.texture,
                blending: THREE.AdditiveBlending
            });
            material.side = THREE.DoubleSide;
            
            sprite = new THREE.Mesh(geo, material);
            sprite.position.x = points2[Math.floor(points2.length/3)].x;
            sprite.position.y = points2[Math.floor(points2.length/3)].y;
            sprite.scale.x = sprite.scale.y = 3;
            sprite.visible = false;
        }
        // END TEXT BLOCK

        booms = [];
        //        booms.visible = false;

        minWaves = renderType == 'canvas' ? 2: 1;
        maxWaves = renderType == 'canvas' ? 6: 8;
        for(var e = minWaves; e < maxWaves; e++) {
            if(renderType == 'Canvas') {
                boomMaterial = new THREE.ParticleCanvasMaterial(  {
                    color: 0xffffff,
                    program: renderBlock.programBoom
                } );

                boom = new THREE.Particle(boomMaterial);
                boom.scale.x = boom.scale.y = boom.scale.z = 0.1;
                boom.position.x = points2[points2.length-1].x;
                boom.position.y = points2[points2.length-1].y;
              
                boom.visible = false;
            } else {
                boom = new THREE.Sprite(
                {
                    map:renderBlock.atmos2Texture,
                    blending: THREE.AdditiveBlending,
                    useScreenCoordinates: false
                });
                //                boom.mergeWith3D = false;
                boom.scale.x = boom.scale.y = boom.scale.z = 0.1;
                boom.position.x = points2[points2.length-1].x;
                boom.position.y = points2[points2.length-1].y;

                boom.visible = false;

            }
            booms.push(boom);
            renderBlock.mesh.exp.add(boom);
        }

        arrow.boomsIn = booms;


        booms = [];
        //        booms.visible = false;
        for(var e = 2; e < 0; e++) {
            if(renderType == 'Canvas') {
                boomMaterial = new THREE.ParticleCanvasMaterial(  {
                    color: 0xffffff,
                    program: renderBlock.programBoom
                } );

                boom = new THREE.Particle(boomMaterial);
                boom.scale.x = boom.scale.y = boom.scale.z = 0.1;
                boom.position.x = points2[0].x;
                boom.position.y = points2[0].y;
                
                boom.visible = false;
            } else {
                
            }

            booms.push(boom);
            renderBlock.mesh.exp.add(boom);
        }

        arrow.boomsOut = booms;

        arrow.add(sprite);

        return arrow;
    },


    updateText: function () {
        system = modelBlock.planetMap;
        for (planet in system) {
            //            console.log(this);
            this.createTextTexture(system[planet].id);
            renderBlock.textures[system[planet].id].needsUpdate = true;
        }
    },


    addControls: function (camera, debugMode) {
        //        console.log('adding the controls');
        
        if(!debugMode) {
            centers = [];

            center = new THREE.Vector3();
            if(renderType == 'WebGL' ){
                center.x = renderBlock.sun.position.x/20;
                center.y = renderBlock.sun.position.y/20;
                center.z = renderBlock.sun.position.z/20;
            } else {
                center.x = -200;
                center.y = 50;
                center.z = 0;
            }

            centers.push(center);
            //            center = new THREE.Vector3();
            //            center.y = 50;
            centers.push(center);
            //            centers.push(renderBlock.sun.position);


            controls = new THREE.OrbitControlsMod(renderBlock.camera, renderBlock.renderer.domElement, centers);

            controls.userZoomSpeed = 0.1;
            if(renderType!='WebGL' ) {
                controls.userRotate = false;
                controls.userZoom = false;
            }

        } else {
            controls = new THREE.FlyControls( camera );

            controls.movementSpeed = 500;
            controls.domElement = renderBlock.renderer.domElement;
            controls.rollSpeed = Math.PI/10 ;
            controls.autoForward = false;
            controls.dragToLook = true;

        }
        renderBlock.controls = controls;
    }



};







///// LOOP BLOCK ///////////////////////////////////////////////////////////////////////////////////////////////////////////
var loopBlock = {
	/*
    //TODO get the coordinates correction, caused by fullscreen and div placement
    onDocumentMouseMove : function(event) {
        //TODO add support for the non-fullscreen mode
        var vector;
        if(renderBlock.GUIOptions.fullScreen ) {
            vector = new THREE.Vector3( ( event.clientX / renderBlock.SCREEN_WIDTH ) * 2 - 1, - ( event.clientY / renderBlock.SCREEN_HEIGHT ) * 2 + 1, 0.5 );
        } else {

            _this = $('#container');
            vector = new THREE.Vector3(
                ( (event.clientX - (_this.offset().left - $(window).scrollLeft()) ) / renderBlock.SCREEN_WIDTH ) * 2 - 1,
                -( (event.clientY - (_this.offset().top - $(window).scrollTop()) ) / renderBlock.SCREEN_HEIGHT) * 2 + 1,
                0.5
                );
        //           console.log(_this.offset().top - $(window).scrollTop()) ;
        }
        //         console.log(vector.x + ' ' + vector.y + ' ' + vector.z);
        renderBlock.projector.unprojectVector( vector, renderBlock.camera );
        INTERSECTED = renderBlock.INTERSECTED;
        var ray = new THREE.Ray( renderBlock.camera.position, vector.subSelf(renderBlock.camera.position ).normalize() );

        var intersects = ray.intersectObjects( renderBlock.mesh.children );

        if ( intersects.length > 0 ) {

            if ( INTERSECTED != intersects[ 0 ].object ) {

                if(renderType == 'WebGL'){

                    if ( INTERSECTED ) {
                        INTERSECTED.children[1].visible = !renderBlock.GUIOptions.hideWire;
                        INTERSECTED.children[1].scale.set(1.0, 1.0, 1.0);
                    }
                    intersects[ 0 ].object.children[1].visible = true;
                    intersects[ 0 ].object.children[1].scale.set(1.2, 1.2, 1.2);
                    INTERSECTED = intersects[ 0 ].object;

                } else {

                    if ( INTERSECTED ) INTERSECTED.material.program = renderBlock.programStroke;
                    INTERSECTED = intersects[ 0 ].object;
                    INTERSECTED.material.program = renderBlock.programFill;
                }

                id = intersects[ 0 ].object.planetId;
                for(arrow in renderBlock.actionMap) {
                    if(arrow.split(' ')[0] == id){
                        renderBlock.actionMap[arrow].line.visible = true;
                    }
                }

            }

        } else if(INTERSECTED){
            if(renderType == 'WebGL'){
                INTERSECTED.children[1].visible = !renderBlock.GUIOptions.hideWire;
                INTERSECTED.children[1].scale.set(1.0, 1.0, 1.0);
            } else {
                INTERSECTED.material.program = renderBlock.programStroke;
            }
            id = INTERSECTED.planetId;
            for(arrow in renderBlock.actionMap) {
                if(arrow.split(' ')[0] == id){
                    renderBlock.actionMap[arrow].line.visible = INTERSECTED.triggered;
                }
            }
            INTERSECTED = null;

        }
        renderBlock.INTERSECTED = INTERSECTED;

    },
   */

    actionsFinished : false,
    



    jsonHandler: function(json, xmlhttp) {
        //        console.log('\n\nprevTurn: ' + modelBlock.turn + ' newTurn: ' + json.turnNumber );
        modelBlock.turn = json.turnNumber;
        //        modelBlock.prevStep[modelBlock.turn] = modelBlock.planetMap.slice();

        //                        modelBlock.jsonResponse.push(json);
        planetOwners = json.playersActions.planetOwners;
        for(p in planetOwners) {

            for(mp in modelBlock.planetMap)  {
                if(planetOwners[p].id == modelBlock.planetMap[mp].id) {
                    modelBlock.planetMap[mp].owner = planetOwners[p].owner;
                    modelBlock.planetMap[mp].unitsCount = planetOwners[p].unitsCount;
                }
            }

        }
        modelBlock.actionMap = json.playersActions.actions;

        //        console.log('new turn:' + modelBlock.turn );

        res = renderBlock.updateView();

        if(!res) {
            console.log(xmlhttp.responseText);
        }
        modelBlock.updateStats();

    },




    updateActions: function ( ){

        var mapId = gameId;
        var xmlhttp;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP ");
        }

        var request = "game/viewData.html?gameId=" + mapId + "&type=PLAYERS_ACTIONS";
        xmlhttp.open("GET", request, true);
        xmlhttp.send();

        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

                try {
                    json = JSON.parse(xmlhttp.responseText);
                } catch(e) {
                    window.clearInterval(initBlock.actionUpdater);
                }
                
                switch(json.gameState) {
                    case 'started':{
                        stopTableUpdater();
                        if(modelBlock.turn != json.turnNumber){
                            loopBlock.jsonHandler(json, xmlhttp);
                            modelBlock.jsonResponse.push(json);
                        }
                        break;
                    }
                    case 'finished': {
                        stopTableUpdater();
                        console.log('hrum hrum');
                        if(!loopBlock.actionsFinished) {
                            console.log('yup yup');
                            //                            if(modelBlock.turn != json.turnNumber){
                            console.log('om nom nom');
                            loopBlock.jsonHandler(json, xmlhttp);
                            window.clearInterval(initBlock.actionUpdater);
                            loopBlock.updateActions();
                            modelBlock.turn = json.turnNumber;
                            loopBlock.actionsFinished = true;
                            console.log('ending');
                        //                            }

                        } else {
                            console.log('ended');
                            window.clearInterval(initBlock.actionUpdater);

                            initBlock.notGameStatus(json);
                        }
                        break;
                    }
                    default:
                        initBlock.notGameStatus(json);
                        
                        break;

                }
            }
        };
    }
};
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




///// INIT BLOCK ///////////////////////////////////////////////////////////////////////////////////////////////////////////
var initBlock = {
    modelUpdater: null,
    actionUpdater:null,
    jsonGetter: null,
    config: {
        gameId: null,
        sunAsObject: true
    },

    init: function (json) {
        window.clearInterval(initBlock.jsonGetter);
        document.body.appendChild(document.createElement('div')).id = 'border';
        modelBlock.init(json);
        //        modelBlock.planetMap = json.planetMap;
        modelBlock.updateStats();
        renderBlock.init();
        loopBlock.updateActions();

        window.addEventListener('resize', renderBlock.onWindowResize, false);
	    renderBlock.renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);
	    renderBlock.onWindowResize();
        animate();

        initBlock.actionUpdater = window.setInterval(loopBlock.updateActions, renderBlock.turnSpeed/2);

    },

    showWinners: function(scores) {

        span = document.getElementById("winner");
        span.innerHTML = "Победители:&nbsp";
        span.style.display = "none";
        span = document.getElementById("winnerName");
        var table = "<table id='winnerTable' class='playersTable' width = '100%'> \n\
         <tr>\n\
            <th>Игрок</th>\n\
            <th>Последний ход</th>\n\
            <th>Количество дроидов</th>\n\
            <th>Место</th>\n\
        </tr>"

        for(var i = 0; i < scores.length; i++) {
            table +="<tr><td>" + scores[i].player + "</td>";
            table +="<td>" + scores[i].lastTurn + "</td>";
            table +="<td>" + scores[i].unitsCount + "</td>";
            table+="<td>" + scores[i].place + "</td></tr>";
        }
        table +="</table>";

        span.innerHTML = table;
        span.style.fontSize = '24px';
        span.style.display = "block";
        //        span.style.marginTop = '20px';
        span.style.backgroundColor = 'rgba(42,42,42,0.7)';

        

    },

    notGameStatus: function (message) {
        if (message.gameState == "finished") {
            
            console.log('finished');
            divs = document.getElementsByClassName('gameStatusMessage');
            for (var i = 0; i < divs.length; i++) {
                divs[i].style.display = "block";
            }
            span = document.getElementById("notStarted");
            span.style.display = "none";
            //            span.style.visibility = 'hidden';
            
            span = document.getElementById("finished");
            span.style.display = "block ";
            span.style.paddingTop = '20px';

            if(renderBlock.started) {
                initBlock.showWinners(message.scores);
            }
            if(!renderBlock.started) {
                $('#container').css('height', '0px');
            }

        } else { //not started
            runTableUpdater();
            if(renderBlock.started==false) {
                if(!initBlock.modelUpdater)
                    initBlock.modelUpdater = window.setInterval(initBlock.getJSONModel, renderBlock.turnSpeed/2);
            }
            divs = document.getElementsByClassName('gameStatusMessage');
            for (var i = 0; i < divs.length; i++) {
                divs[i].style.display = "block ";
            }
            span = document.getElementById("notStarted");
            span.style.display = "block ";
            //            span.style.visibility = 'visible';
            span = document.getElementById("finished");
            span.style.display = "none ";
        //            span.style.visibility = 'hidden';
        }
    },

    hideWarnings: function() {
        //        divs = document.getElementsByClassName('gameStatusMessage');
        //        for (var i = 0; i < divs.length; i++) {
        //            divs[i].style.display = "none";
        //        }
        span = document.getElementById("notStarted");
        //        span.style.visibility = 'hidden';
        span.style.display = "none ";
        span = document.getElementById("finished");
        //        span.style.visibility = 'hidden';
        span.style.display = "none ";
    },
    started : false,

    getJSONModel: function () {

        //        gameId = $('#gameId').text();
        if(typeof gameId !== 'undefined') {
            var mapId = gameId;
        
            xmlhttp = undefined;
            if (window.XMLHttpRequest) {
                xmlhttp = new XMLHttpRequest();
            } else {
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            request = "game/viewData.html?gameId=" + mapId + "&type=GAME_FIELD";
            xmlhttp.open("GET", request, true);
            xmlhttp.send();

            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    if(xmlhttp.responseText !== "") {
                        json = JSON.parse(xmlhttp.responseText);
                    } else {
	                    json = {gameState: "notStarted"};
                    }
                    switch(json.gameState) {
                        case 'started':
                            if(!initBlock.started) {
                                try {
                                    window.clearInterval(initBlock.modelUpdater);
                                } catch(e) {}
                                initBlock.hideWarnings();
                                initBlock.started = true;
                                initBlock.init(json);
                            }
                            break;
                        default:
                            initBlock.notGameStatus(json);
                        
                            break;

                    }

                }
            }
        } else {
            return;
        }
    }
};




function  animate() {

    window.requestAnimationFrame(animate, renderBlock.renderer.domElement);

    renderBlock.render();
}

function chooseRenderType() {
    var xmlhttp;
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    } else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }



    var request = "./settings.html?type=TURN_DURATION";
    xmlhttp.open("GET", request, true);
    xmlhttp.send();

    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            renderBlock.turnSpeed = JSON.parse(xmlhttp.responseText).TurnDelay;
//            console.log(xmlhttp.responseText);
        }
    }




    //    console.log('a');

    //    if (!Detector.webgl) {
    //        //            Detector.addGetWebGLMessage();
    //        window.renderType='Canvas';
    //        console.log(' canvas render');
    //        initBlock.getJSONModel();
    //
    //    } else {
    //        window.renderType='WebGL';
    //        console.log(' GL render');
    //        initBlock.getJSONModel();
    //    }

    window.renderType='Canvas';
//    console.log('chosed the canvas render(or try "WebGL" if wants more)');
    initBlock.getJSONModel();

}




function onDocumentKeyDown( event ) {
    
    switch( event.keyCode ) {

        case 13: {
            
            break;
        }
        case 38: {//up
            console.log(renderBlock.camera.position);
            break;
        }
        case 40 :{
            
            break;
        }
        case 9 : {
            
            break;
        }
        case 70: {//f - fullscreen
            console.log('f was pressed');
            if(renderBlock.started) {
                renderBlock.GUIOptions.fullScreen = ! renderBlock.GUIOptions.fullScreen;
                renderBlock.turnOnFullScreen();
            }
            break;
        }
        case 83: { //s - show trace
            renderBlock.GUIOptions.showSelection = !renderBlock.GUIOptions.showSelection;
            for(planet in renderBlock.planets) {
                renderBlock.planets[planet].triggered = renderBlock.GUIOptions.showSelection;
            }
            for(arrow in renderBlock.actionMap){
                renderBlock.actionMap[arrow].line.visible = renderBlock.GUIOptions.showSelection;
            }
        }
        default: {
            //            console.log(event.keyCode);
            break;
        }
    }
}

document.addEventListener( 'keydown', onDocumentKeyDown, false );

document.onmozfullscreenchange = document.onwebkitfullscreenchange = function() {
    if(!THREEx.FullScreen.activated() ) {
        THREEx.FullScreen.cancel();
        $('canvas').css({
            "background-image":"none"
        });
        try {
            leaveGame = document.getElementById("leaveGame");
            leaveGame.style.visibility = "visible";
        } catch(e){}
        divs = document.getElementsByClassName('wrap');
        for ( i = 0; i < divs.length; i++) {
            divs[i].style.visibility = "visible";
        }
        renderBlock.renderer.domElement.style.top = '0px';
        renderBlock.SCREEN_WIDTH = $('#container').outerWidth();
        renderBlock.SCREEN_HEIGHT = $('#container').outerHeight();
        renderBlock.renderer.setSize(renderBlock.SCREEN_WIDTH, renderBlock.SCREEN_HEIGHT);
        renderBlock.renderer.domElement.style.position = "relative";

        renderBlock.camera.aspect = renderBlock.SCREEN_WIDTH / renderBlock.SCREEN_HEIGHT;
        renderBlock.camera.updateProjectionMatrix();
        if(renderType=='WebGL') {
            renderBlock.effectFXAA.uniforms[ 'resolution' ].value.set( 1 / window.innerWidth, 1 / window.innerHeight );
            renderBlock.initComposer(renderBlock.enableFXAA);
        }
        renderBlock.GUIOptions.fullScreen = false;
    }
};
window.onload = chooseRenderType;
