/* global define:false */
/* global PuzzleBrawl:false */
define([
  'game/Navigation', 'gem/GemTextures', 'input/Gamepad', 'input/Gesture', 'input/Keyboard', 'panels/Panels', 'playmat/Playmat'
], function (Navigation, GemTextures, Gamepad, Gesture, Keyboard, Panels, Playmat) {
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

    game.panels = new Panels();
    game.panels.show('connecting');

    game.keyboard = new Keyboard(game);
    game.keyboard.init();

    game.gamepad = new Gamepad(game);
    game.gamepad.init();

    game.gesture = new Gesture(game);
    game.gesture.init();

    game.gameInput.pause();

    game.gemTextures = new GemTextures(game);

    game.playmat = new Playmat(game);

    game.navigation = new Navigation(game);

    createLocalServer(game);
  };
});
