/* global define:false */
define(['state/GameState', 'gem/GemSprites'], function (GameState, GemSprites) {
  'use strict';

  function Sandbox(client) {
    GameState.call(this, 'sandbox', client);
  }

  Sandbox.prototype = Object.create(GameState.prototype);
  Sandbox.prototype.constructor = Sandbox;

  Sandbox.prototype.preload = function() {
  };

  Sandbox.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    var idx = GemSprites.spriteFor({ color: 'b', role: 'x'});
    console.log(idx);
    var gem = this.add.sprite(this.client.world.centerX, this.client.world.centerY, 'gems', idx);
    gem.anchor.setTo(0.5, 0.5);
  };

  return Sandbox;
});
