/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var enabled = false;

  return {
    preload: function(game) {
      if(enabled) {
        game.load.image('fire1', 'assets/images/particles/fire1.png');
        game.load.image('fire2', 'assets/images/particles/fire2.png');
        game.load.image('fire3', 'assets/images/particles/fire3.png');
        game.load.image('smoke', 'assets/images/particles/smoke-puff.png');
        game.physics.startSystem(Phaser.Physics.ARCADE);
      }
    },

    start: function(game) {
      if(enabled) {
        var emitter = game.add.emitter(game.world.centerX, game.world.centerY, 400);
        emitter.makeParticles(['fire1', 'fire2', 'fire3', 'smoke']);
        emitter.gravity = -200;
        emitter.setAlpha(1, 0, 3000);
        emitter.setScale(0.8, 0, 0.8, 0, 3000);
        emitter.start(false, 3000, 5);
      }
    }
  };
});
