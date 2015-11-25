/* global define:false */
define(['state/GameState'], function (GameState) {
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

    var gem = this.add.sprite(this.client.world.centerX, this.client.world.centerY, 'gems');
    gem.anchor.setTo(0.5, 0.5);
  };

  return Sandbox;
});
