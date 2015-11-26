/* global define:false */
define(['state/GameState', 'gem/GemSprites', 'board/Board'], function (GameState, GemSprites, Board) {
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

    var board = new Board('sandbox', 6, 12, this.client);
    board.scale = { x: 0.4, y: 0.4 };
    board.x = this.client.world.centerX - (board.width / 2);
    board.y = this.client.world.centerY - (board.height / 2);

    var idx1 = GemSprites.spriteFor({ color: 'b', role: 'x'});
    var gem1 = this.add.sprite(this.client.world.centerX - 64, this.client.world.centerY, 'gems', idx1);
    gem1.name = 'Gem 1';
    gem1.anchor.setTo(0.5, 0.5);

    var idx2 = GemSprites.spriteFor({ color: 'b' });
    var gem2 = this.add.sprite(this.client.world.centerX + 64, this.client.world.centerY, 'gems', idx2);
    gem2.name = 'Gem 2';
    gem2.anchor.setTo(0.5, 0.5);
  };

  return Sandbox;
});
