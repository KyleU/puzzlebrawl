/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  function Board(id, width, height, client) {
    Phaser.Group.call(this, client, null, 'board-' + id);
    client.add.existing(this);

    this.bgTileSprite = new Phaser.TileSprite(client, 0, 0, 128 * width, 128 * height, 'board-bg-a');
    this.add(this.bgTileSprite);
  }

  Board.prototype = Phaser.Group.prototype;
  Board.prototype.constructor = Board;

  return Board;
});
