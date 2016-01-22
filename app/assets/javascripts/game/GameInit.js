/* global define:false */
/* global PuzzleBrawl:false */
define([
  'gem/GemTextures', 'input/Gamepad', 'input/Gesture', 'input/Keyboard', 'playmat/Playmat'
], function (GemTextures, Gamepad, Gesture, Keyboard, Playmat) {
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
      throw 'Game already initialized.';
    }

    game.keyboard = new Keyboard(game);
    game.keyboard.init();

    game.gamepad = new Gamepad(game);
    game.gamepad.init();

    game.gesture = new Gesture(game);
    game.gesture.init();

    game.gemTextures = new GemTextures(game);

    //game.splashScreen = new SplashScreen(game);

    game.playmat = new Playmat(game);

    createLocalServer(game);
    game.localServer.start();
  };
});
