/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  function Board(id, width, height, game) {
    Phaser.Group.call(this, game, null, 'board-' + id);
    game.add.existing(this);

    this.bgTileSprite = new Phaser.TileSprite(game, 0, 0, 128 * width, 128 * height, 'board-bg-a');
    this.bgTileSprite.name = 'background';
    this.add(this.bgTileSprite);
  }

  Board.prototype = Phaser.Group.prototype;
  Board.prototype.constructor = Board;

  Board.prototype.addGem = function(gem, x, y) {
    gem.x = (x * 128) + 64;
    gem.y = this.height - ((y * 128) + 64);
    this.add(gem);
  };

  return Board;
});