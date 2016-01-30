/* global define:false */
/* global PuzzleBrawl:false */
define([
  'audio/Audio', 'game/Navigation', 'gem/GemTextures', 'input/Gamepad', 'input/Gesture', 'input/Keyboard', 'panels/Panels', 'playmat/Playmat'
], function (Audio, Navigation, GemTextures, Gamepad, Gesture, Keyboard, Panels, Playmat) {
  'use strict';

  if(window.PhaserGlobal === undefined) {
    window.PhaserGlobal = {};
  }
  window.PhaserGlobal.hideBanner = true;

  function createLocalServer(game) {
    var pb = new PuzzleBrawl();
    var callback = function(json) {
      var ret = JSON.parse(json);
      game.onMessage(ret.c, ret.v);
    };
    pb.register(callback);
    game.localServer = pb;
  }

  return function(game) {
    if(game.initialized) {
      throw new Error('Game already initialized.');
    }

    // Textures
    game.gemTextures = new GemTextures(game);

    // Playmat
    game.playmat = new Playmat(game);

    // Panels
    game.panels = new Panels();
    game.panels.show('connecting');
    game.navigation = new Navigation(game);

    // Input
    game.keyboard = new Keyboard(game);
    game.keyboard.init();

    game.gamepad = new Gamepad(game);
    game.gamepad.init();

    game.gesture = new Gesture(game);
    game.gesture.init();

    game.gameInput.pause();

    // Audio
    game.gameAudio = new Audio(game);

    // Scala.js
    createLocalServer(game);
  };
});
